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
// Created 5.10.08 by Hendrik
// 
// CardTerminals instance for emulators
// 
// $Id: Emulator_connection_terminals.java,v 1.6 2009-03-16 22:25:27 tews Exp $

package ds.javacard.emulator.smartcardio;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.lang.IllegalArgumentException;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;


public class Emulator_connection_terminals extends CardTerminals {

    private final static int default_port = 9025;

    private Emulator_connection_terminal[] terminals;
    private int verbosity;


    // Constructor.
    protected Emulator_connection_terminals(int[] ports, int verbosity) {
        this.verbosity = verbosity;
        if(verbosity > 0) {
            if(ports == null)
                System.out.println("EC terminals constructor(null)");
            else
                System.out.format("EC terminals constructor " +
                                  "with %d arguments\n",
                                  ports.length);
        }

        if(ports == null || ports.length == 0) {
            ports = new int[]{default_port};
        }
        terminals = new Emulator_connection_terminal[ports.length];
        for(int i = 0; i < terminals.length; i++) {
            terminals[i] = 
                new Emulator_connection_terminal(ports[i], verbosity);
        }
    }


    // Returns an unmodifiable list of all available terminals.
    // Throws CardException if the card operation failed.
    public List<CardTerminal> list()
    {
        return Collections.unmodifiableList(
            Arrays.asList((CardTerminal[])terminals));
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
        throw new UnsupportedOperationException(
            "Emulator_connection_terminals.list(state): " +
            "Cannot query the presence or state of cref/jcwde emulators");
    }


    // Returns the terminal with the specified name or null 
    // if no such terminal exists.
    // Throws NullPointerException if name is null.
    public CardTerminal getTerminal(String name)
    {
        try {
            int port = Integer.parseInt(name);
            if(port <= 0)
                throw new IllegalArgumentException(
                    "Emulator_connection_terminals.getTerminal(port): " +
                    "port must be a positive integer");
            return new Emulator_connection_terminal(port, verbosity);
        }
        catch(NumberFormatException e) {
            throw new IllegalArgumentException(
                "Emulator_connection_terminals.getTerminal(name): " +
                "name must be a port number",
                e);
        }
    }


    // Waits for card insertion or removal in any of the terminals 
    // of this object.
    // This call is equivalent to calling waitForChange(0).
    // Throws:
    //     IllegalStateException - if this CardTerminals object does 
    //                          not contain any terminals 
    //     CardException - if the card operation failed
    public void waitForChange()
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
    {
        throw new UnsupportedOperationException(
            "Emulator_connection_terminals.list(state): " +
            "Cannot query the presence or state of cref/jcwde emulators");
    }

}
