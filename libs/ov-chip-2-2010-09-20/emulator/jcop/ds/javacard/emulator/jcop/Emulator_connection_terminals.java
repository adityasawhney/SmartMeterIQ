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
// CardTerminals instance for emulators
// 
// $Id: Emulator_connection_terminals.java,v 1.5 2009-06-17 20:14:02 tews Exp $

package ds.javacard.emulator.jcop;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;


public class Emulator_connection_terminals extends CardTerminals {

    /**
     * 
     * Array of terminals that this instance controls. Contains one
     * terminal for each port that has been passed to the constructor.
     */
    private final Emulator_connection_terminal[] terminals;

    // The following boolean captures the state of the terminal
    // when waitForChange is called: true for card present (emulator running)
    // false for card absent (emulator not running);
    private boolean[] wait_for_change_state;
    private int verbosity;

    private boolean wait_for_change_has_been_called = false;


    // Constructor.
    protected Emulator_connection_terminals(int[] ports, int verbosity) {
        this.verbosity = verbosity;
        terminals = new Emulator_connection_terminal[ports.length];
        for(int i = 0; i < ports.length; i++)
            terminals[i] = new Emulator_connection_terminal(ports[i], 
                                                            verbosity);

        if(verbosity > 0) {
            System.out.println("JC terminals constructor");
        }
    }


    // Returns an unmodifiable list of all available terminals.
    // Throws CardException if the card operation failed.
    public List<CardTerminal> list()
    {
        if(verbosity > 0) {
            System.out.println("JC terminals list all");
        }

        List<CardTerminal> l = Arrays.asList((CardTerminal[])terminals);

        return Collections.unmodifiableList(l);
    }


    /**
     * 
     * Query and return the present state of all terminals.
     * 
     * @return boolean array with the present state for each terminal
     * in {@link #terminals}
     * @throws CardException if checking the present state failed
     */
    private boolean[] get_present_array()
        throws CardException
    {
        boolean[] res = new boolean[terminals.length];
        for(int i = 0; i < terminals.length; i++)
            res[i] = terminals[i].isCardPresent();
        return res;
    }


    // Returns an unmodifiable list of all terminals matching the 
    // specified state.
    // 
    // If state is State.ALL, this method returns all CardTerminals
    // encapsulated by this object. If state is State.CARD_PRESENT
    // or State.CARD_ABSENT, it returns all CardTerminals where a card
    // is currently present or absent, respectively.
    // 
    // If state is State.CARD_INSERTION or State.CARD_REMOVAL, it 
    // returns all CardTerminals for which an insertion 
    // (or removal, respectively) was detected during the last call 
    // to waitForChange(). If waitForChange() has not been called 
    // on this object, CARD_INSERTION is equivalent to CARD_PRESENT
    // and CARD_REMOVAL is equivalent to CARD_ABSENT. For an example
    // of the use of CARD_INSERTION, see waitForChange().
    // 
    // Throws:
    //     NullPointerException - if state is null 
    //     CardException - if the card operation failed

    public List<CardTerminal> list(CardTerminals.State state)
        throws CardException
    {
        if(verbosity > 0) {
            System.out.println("JC terminals list some");
        }
        if(!wait_for_change_has_been_called && 
           state == CardTerminals.State.CARD_INSERTION)
            state = CardTerminals.State.CARD_PRESENT;
        if(!wait_for_change_has_been_called &&
           state == CardTerminals.State.CARD_REMOVAL)
            state = CardTerminals.State.CARD_ABSENT;

        boolean[] now_present = get_present_array();

        LinkedList<CardTerminal> l = new LinkedList<CardTerminal>();

        for(int i = terminals.length -1; i >= 0; i--) {
            if(state == CardTerminals.State.ALL 
               ||
               (state == CardTerminals.State.CARD_ABSENT 
                && now_present[i] == false) 
               ||
               (state == CardTerminals.State.CARD_PRESENT 
                && now_present[i] == true) 
               ||
               (state == CardTerminals.State.CARD_INSERTION 
                && now_present[i] == true 
                && wait_for_change_state[i] == false) 
               ||
               (state == CardTerminals.State.CARD_REMOVAL 
                && now_present[i] == false 
                && wait_for_change_state[i] == true)
               )
                {
                    l.add(terminals[i]);
                }
        }

        return Collections.unmodifiableList(l);
    }


    // Returns the terminal with the specified name or null 
    // if no such terminal exists.
    // Throws NullPointerException if name is null.
    public CardTerminal getTerminal(String name)
    {
        throw new UnsupportedOperationException(
            "Emulator_connection_terminals.getTerminal(name): " +
            "not implemented yet");
    }


    // Waits for card insertion or removal in any of the terminals 
    // of this object.
    // This call is equivalent to calling waitForChange(0).
    // Throws:
    //     IllegalStateException - if this CardTerminals object does 
    //                          not contain any terminals 
    //     CardException - if the card operation failed
    public void waitForChange()
        throws CardException
    {
        waitForChange(0);
    }



    // Waits for card insertion or removal in any of the terminals
    // of this object or until the timeout expires.
    // 
    // This method examines each CardTerminal of this object. If a card
    // was inserted into or removed from a CardTerminal since the
    // previous call to waitForChange(), it returns immediately.
    // Otherwise, or if this is the first call to waitForChange() on this
    // object, it blocks until a card is inserted into or removed from a
    // CardTerminal.
    // 
    // If timeout is greater than 0, the method returns after timeout
    // milliseconds even if there is no change in state. In that case,
    // this method returns false; otherwise it returns true.
    // 
    // This method is often used in a loop in combination with
    // list(State.CARD_INSERTION), for example:
    // 
    //   TerminalFactory factory = ...;
    //   CardTerminals terminals = factory.terminals();
    //   while (true) {
    //       for (CardTerminal terminal : terminals.list(CARD_INSERTION)) {
    //           // examine Card in terminal, return if it matches
    //       }
    //       terminals.waitForChange();
    //   }
    // 
    // Parameters:
    //     timeout - if positive, block for up to timeout milliseconds; 
    //               if zero, block indefinitely; must not be negative.
    // Returns:
    //     false if the method returns due to an expired timeout, 
    //     true otherwise. 
    // Throws:
    //     IllegalStateException - if this CardTerminals object does
    //                             not contain any terminals 
    //     IllegalArgumentException - if timeout is negative 
    //     CardException - if the card operation failed
    public boolean waitForChange(long timeout)
        throws CardException
    {
        if(verbosity > 0) {
            System.out.println("JC terminals wait for change");
        }
        if(!wait_for_change_has_been_called) {
            wait_for_change_state = get_present_array();
            wait_for_change_has_been_called = true;
        }

        boolean current_state[];
        long end_time = System.currentTimeMillis() + timeout;

        long sleep_time = 0;
        do {
            if(sleep_time > 0) {
                try{
                    Thread.sleep(sleep_time);
                }
                catch(InterruptedException e) {
                    // The interface does however not permit us to
                    // throw the InterruptedException at the caller.
                    // To prevent the loss of the interrupt we have to
                    // interrupt ourselves.
                    Thread.currentThread().interrupt();
                }
            }

            current_state = get_present_array();
            if(!Arrays.equals(wait_for_change_state, current_state))
                return true;

            // Determine how much to sleep in the next round.
            sleep_time = 200;

            // Ensure timesteps of 0.1 * timeout for small timeouts.
            if(timeout > 0 & timeout <= 20) 
                sleep_time = timeout;
            else if(timeout > 20 && timeout < 2000)
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
}
