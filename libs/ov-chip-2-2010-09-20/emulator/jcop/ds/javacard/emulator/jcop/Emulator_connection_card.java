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
// card implementation for the jcop emulator connection
// 
// $Id: Emulator_connection_card.java,v 1.4 2009-02-20 15:29:21 tews Exp $

package ds.javacard.emulator.jcop;

import javax.smartcardio.ATR;
import javax.smartcardio.CardException;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;

import com.ibm.jc.JCard;


public class Emulator_connection_card extends Card {

    private Emulator_connection_terminal terminal;
    private JCard jcard;
    private ATR smio_atr;
    private int verbosity;

    private Emulator_connection_channel basic_channel = null;

    // The owning thread, if beginExclusive has been issued.
    Thread owner;

    Emulator_connection_card(Emulator_connection_terminal terminal,
                             JCard jcard, ATR atr, int verbosity) 
    {
        if(verbosity > 0) {
            System.out.println("JC card constructor");
        }
        this.terminal = terminal;
        this.jcard = jcard;
        this.smio_atr = atr;
        this.verbosity = verbosity;
        owner = null;
    }


    // Checks whether the current thread is allowed to communicate 
    // with the card.
    public void check_exclusive() 
        throws CardException
    {
        if(owner != null && !owner.equals(Thread.currentThread()))
            throw new CardException
                ("Card is exclusively used by a different thread");
        return;
    }


    // Checks whether this card has been disconnected.
    public void check_not_disconnected() {
        if(jcard == null)
            throw new IllegalStateException("Card has been disconnected");
    }


    // Returns the ATR of this card.
    public ATR getATR() {
        return smio_atr;
    }


    // Returns the protocol in use for this card.
    public String getProtocol() {
        return "*";
    }


    // Returns the CardChannel for the basic logical channel. The basic
    // logical channel has a channel number of 0.
    // 
    // Throws:
    //     SecurityException - if a SecurityManager exists and the caller
    //                         does not have the required permission
    //     IllegalStateException - if this card object has been disposed
    //                             of via the disconnect() method
    public CardChannel getBasicChannel() 
        throws IllegalStateException
    {
        if(verbosity > 0) {
            System.out.println("JC card basic channel");
        }

        // XXX check security and disconnect!
        check_not_disconnected();

        if(basic_channel == null) {
            basic_channel = 
                new Emulator_connection_channel(jcard, this, 0, verbosity);
        }
        return basic_channel;
    }


    // Opens a new logical channel to the card and returns it. The
    // channel is opened by issuing a MANAGE CHANNEL command that should
    // use the format [00 70 00 00 01].
    // 
    // Throws:
    //     SecurityException - if a SecurityManager exists and the caller
    //                         does not have the required permission
    //     CardException - is a new logical channel could not be opened 
    //     IllegalStateException - if this card object has been disposed
    //                             of via the disconnect() method
    public CardChannel openLogicalChannel()
        throws CardException
    {
        // XXX check security and disconnect!
        check_not_disconnected();

        check_exclusive();

        throw new UnsupportedOperationException("not implemented yet");

        // open_channels.addFirst(new_channel);
    }


    // Requests exclusive access to this card.
    // 
    // Once a thread has invoked beginExclusive, only this thread is
    // allowed to communicate with this card until it calls endExclusive.
    // Other threads attempting communication will receive a
    // CardException.
    // 
    // Applications have to ensure that exclusive access is correctly
    // released. This can be achieved by executing the beginExclusive()
    // and endExclusive calls in a try ... finally block.
    // 
    // Throws:
    //     SecurityException - if a SecurityManager exists and the caller
    //                         does not have the required permission
    //     CardException - if exclusive access has already been set or if
    //                     exclusive access could not be established
    //     IllegalStateException - if this card object has been disposed
    //                             of via the disconnect() method
    public void beginExclusive()
        throws CardException
    {
        check_exclusive();

        if(verbosity > 0) {
            System.out.println("JC card begin exclusive");
        }


        owner = Thread.currentThread();
    }


    // Releases the exclusive access previously established using
    // beginExclusive.
    // Throws:
    //     SecurityException - if a SecurityManager exists and the caller
    //                         does not have the required permission
    //     IllegalStateException - if the active Thread does not
    //                             currently have exclusive access to
    //                             this card or if this card object has
    //                             been disposed of via the disconnect()
    //                             method
    //     CardException - if the operation failed
    public void endExclusive()
        throws CardException
    {
        check_exclusive();

        if(verbosity > 0) {
            System.out.println("JC card end exclusive");
        }

        owner = null;
    }


    // Transmits a control command to the terminal device.
    // This can be used to, for example, control terminal functions like
    // a built-in PIN pad or biometrics.
    // 
    // Parameters:
    //     controlCode - the control code of the command
    //     command - the command data 
    // Throws:
    //     SecurityException - if a SecurityManager exists and the caller
    //                         does not have the required permission
    //     NullPointerException - if command is null 
    //     CardException - if the card operation failed 
    //     IllegalStateException - if this card object has been disposed
    //                             of via the disconnect() method
    public byte[] transmitControlCommand(int controlCode,
                                                  byte[] command)
        throws CardException
    {
        check_exclusive();

        throw new UnsupportedOperationException("not implemented");
    }


    // Disconnects the connection with this card. After this method
    // returns, calling methods on this object or in CardChannels
    // associated with this object that require interaction with the card
    // will raise an IllegalStateException.
    // 
    // Parameters:
    //     reset - whether to reset the card after disconnecting. 
    // Throws:
    //     CardException - if the card operation failed 
    //     SecurityException - if a SecurityManager exists and the caller
    //                         does not have the required permission
    public void disconnect(boolean reset)
        throws CardException
    {
        // XXX check security

        check_exclusive();

        if(verbosity > 0) {
            System.out.println("JC card disconnect");
        }

        if(jcard != null) {
            if(reset)
                jcard.reset();

            jcard = null;
        }
        terminal.ensure_terminal_is_closed();
    }


    public String toString() {
        if(jcard == null)
            return "disconnected jcop emulator";
        else
            return "connected jcop emulator";
    }
}

