// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2008, 2009
// 
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation; either version 2 of
// the License, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// General Public License in file COPYING in this or one of the
// parent directories for more details.
// 
// Created 31.10.08 by Hendrik
// 
// jcop Emulator connection terminal: a port and a socket
// 
// $Id: Emulator_connection_terminal.java,v 1.7 2009-06-17 20:14:02 tews Exp $

package ds.javacard.emulator.jcop;

import javax.smartcardio.CardException;
import javax.smartcardio.CardNotPresentException;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;

import com.ibm.jc.JCException;
import com.ibm.jc.terminal.RemoteJCTerminal;
import com.ibm.jc.JCard;


public class Emulator_connection_terminal extends CardTerminal {

    private int port;
    private RemoteJCTerminal jc_terminal;
    private boolean terminal_is_open = false;
    private int verbosity;

    Emulator_connection_terminal(int port, int verbosity) {
        this.port = port;
        this.verbosity = verbosity;
        jc_terminal = new RemoteJCTerminal();
        jc_terminal.init(String.format("localhost:%d", port));
        if(verbosity > 0)
            System.out.println("JC terminal " + port + " constructur");

        // try {
        //     jc_terminal.open();
        //     System.out.println("opened");
        //     try{Thread.sleep(5000);} catch(InterruptedException e) {}
        //     System.out.println("continue");
        //     byte[] atrbytes = jc_terminal.waitForCard(0);
        // }
        // catch(JCException e) {
        //     System.out.format("caught JCE %s code %d (%s)\n",
        //                    e.getMessage(),
        //                    e.errorCode,
        //                    e.toString());
        //     e.printStackTrace();
        // }
    }


    void ensure_terminal_is_open() {
        if(!terminal_is_open) {
            if(verbosity > 0) {
                System.out.println("JC open jc_terminal");
            }
            jc_terminal.open();
            terminal_is_open = true;
        }
    }

    void ensure_terminal_is_closed() {
        if(terminal_is_open) {
            if(verbosity > 0) {
                System.out.println("JC close jc_terminal");
            }
            try {
                jc_terminal.close();
            }
            finally {
                terminal_is_open = false;
            }
        }
    }
    

    // Returns the unique name of this terminal.
    public String getName() {
        return String.format("jcop emulator on port %d", port);
    }


    // Establishes a connection to the card. If a connection has
    // previously established using the specified protocol, this method
    // returns the same Card object as the previous call.
    // 
    // Parameters:
    //     protocol - the protocol to use ("T=0", "T=1", or "T=CL"), or
    //                "*" to connect using any available protocol.
    // 
    // Throws:
    //     NullPointerException - if protocol is null 
    //     IllegalArgumentException - if protocol is an invalid protocol 
    //                                specification 
    //     CardNotPresentException - if no card is present in this terminal 
    // 
    //     CardException - if a connection could not be established using
    //                     the specified protocol or if a connection has
    //                     previously been established using a different
    //                     protocol
    // 
    //     SecurityException - if a SecurityManager exists and the caller
    //                         does not have the required permission
    public Card connect(String protocol_name)
        throws CardNotPresentException,
               CardException
    {
        try {
            if(verbosity > 0) {
                System.out.println("JC terminal " + port + " connect");
            }
            byte[] atrbytes;

            if(terminal_is_open) {
                try {
                    atrbytes = jc_terminal.waitForCard(0);
                }
                catch(JCException e) {
                    // Maybe someone restarted the emulator since when
                    // we opened the connection. Reopen and try again
                    // in this case.
                    if(e.errorCode == JCException.TERMINAL_ERROR) {
                        ensure_terminal_is_closed();
                        ensure_terminal_is_open();
                        atrbytes = jc_terminal.waitForCard(0);
                    }
                    else
                        throw e;
                }
            }
            else {
                ensure_terminal_is_open();
                atrbytes = jc_terminal.waitForCard(0);
            }

            ATR smio_atr = new ATR(atrbytes);
            com.ibm.jc.ATR jc_atr = new com.ibm.jc.ATR(atrbytes);
           
            JCard card = new JCard(jc_terminal, jc_atr, 0);

            return new Emulator_connection_card(this, card, 
                                                smio_atr, verbosity);
        }
        catch(JCException e) {
            if(e.errorCode == JCException.TERMINAL_ERROR) {
                throw new CardNotPresentException(e.getMessage(), e);
            }
            else {
                throw new CardException("connection failure", e);
            }
        }
    }


    // Returns whether a card is present in this terminal.
    // Throws:
    //     CardException - if the status could not be determined
    public boolean isCardPresent()
        throws CardException
    {
        if(verbosity > 0) {
            System.out.println("JC terminal " + port + " isCardPresent");
        }
        try {
            ensure_terminal_is_open();
            if(jc_terminal.getState() == RemoteJCTerminal.CARD_PRESENT) {
                return true;
            }
            else {
                // When I tried it getState on the emulator gave either
                // RemoteJCTerminal.CARD_PRESENT or threw an exception.
                // So this is fishy here! 
                ensure_terminal_is_closed();
                return false;
            }
        }
        catch(JCException e) {
            if(e.errorCode == JCException.TERMINAL_ERROR) {
                // No emulator listens on this port.
                ensure_terminal_is_closed();
                return false;
            }
            else {
                // Other exceptions are propagated.
                throw new CardException(e);
            }
        }               
    }


    // Waits until a card is present in this terminal or the timeout
    // expires. If the method returns due to an expired timeout, it
    // returns false. Otherwise it return true.
    // 
    // If a card is present in this terminal when this method is called,
    // it returns immediately.
    // 
    // Parameters:
    //     timeout - if positive, block for up to timeout milliseconds;
    //               if zero, block indefinitely; must not be negative
    // Returns:
    //     false if the method returns due to an expired timeout, 
    //     true otherwise. 
    // Throws:
    //     IllegalArgumentException - if timeout is negative 
    //     CardException - if the operation failed
    public boolean waitForCardPresent(long timeout)
    {
        if(timeout < 0)
            throw new 
                IllegalArgumentException("negative timeout not permitted");

        if(verbosity > 0) {
            System.out.println("JC terminal " + port + " wait for card present");
        }

        long end_time = System.currentTimeMillis() + timeout;
        long sleep_time = 0;
        do {
            if(sleep_time > 0)
                try {
                    Thread.sleep(sleep_time);
                }
                catch(InterruptedException e) {
                    // The interface does however not permit us to
                    // throw the InterruptedException at the caller.
                    // To prevent the loss of the interrupt we have to
                    // interrupt ourselves.
                    Thread.currentThread().interrupt();
                }

            try {
                if(isCardPresent())
                    return true;
            }
            catch(CardException e) {}

            // Determine how much to sleep in the next round.
            sleep_time = 200;

            // Ensure timesteps of 0.1 * timeout for small timeouts.
            if(timeout > 0 && timeout < 2000)
                // Make sure sleep_time is greater than 0.
                sleep_time = timeout / 10 + 1;

            // Do not sleep longer then end_time.
            if(timeout > 0) {
                long now = System.currentTimeMillis();
                if(sleep_time > end_time - now)
                    sleep_time = end_time - now;
            }

            // sleep_time is lesser than 0 if there was a timeout 
            // and that timeout has elapsed.
        } while(sleep_time >= 0);

        return false;
    }


    // Waits until a card is absent in this terminal or the timeout
    // expires. If the method returns due to an expired timeout, it
    // returns false. Otherwise it return true.
    // 
    // If no card is present in this terminal when this method is called,
    // it returns immediately.
    // 
    // Parameters:
    //     timeout - if positive, block for up to timeout milliseconds;
    //               if zero, block indefinitely; must not be negative
    // Returns:
    //     false if the method returns due to an expired timeout, 
    //     true otherwise. 
    // Throws:
    //     IllegalArgumentException - if timeout is negative 
    //     CardException - if the operation failed
    public boolean waitForCardAbsent(long timeout)
    {
        if(timeout < 0)
            throw new 
                IllegalArgumentException("negative timeout not permitted");

        if(verbosity > 0) {
            System.out.println("JC terminal " + port + " wait for card absent");
        }

        long end_time = System.currentTimeMillis() + timeout;
        long sleep_time = 0;
        do {
            if(sleep_time > 0)
                try {
                    Thread.sleep(sleep_time);
                }
                catch(InterruptedException e) {
                    // The interface does however not permit us to
                    // throw the InterruptedException at the caller.
                    // To prevent the loss of the interrupt we have to
                    // interrupt ourselves.
                    Thread.currentThread().interrupt();
                }

            try {
                if(!isCardPresent())
                    return true;
            }
            catch(CardException e) {
                return true;
            }

            // Determine how much to sleep in the next round.
            sleep_time = 200;

            // Ensure timesteps of 0.1 * timeout for small timeouts.
            if(timeout > 0 && timeout < 2000)
                // Make sure sleep_time is greater than 0.
                sleep_time = timeout / 10 + 1;

            // Do not sleep longer then end_time.
            if(timeout > 0) {
                long now = System.currentTimeMillis();
                if(sleep_time > end_time - now)
                    sleep_time = end_time - now;
            }

            // sleep_time is lesser than 0 if there was a timeout 
            // and that timeout has elapsed.
        } while(sleep_time >= 0);

        return false;
    }


    public String toString() {
        return String.format("jcop emulator terminal at port %d", port);
    }
}
