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
// Created 6.10.08 by Hendrik
// 
// Emulator connection terminal: simply a port number
// 
// $Id: Emulator_connection_terminal.java,v 1.6 2009-03-16 22:25:27 tews Exp $

package ds.javacard.emulator.smartcardio;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.smartcardio.CardException;
import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import com.sun.javacard.apduio.CadDevice;


public class Emulator_connection_terminal extends CardTerminal {

    private int port;
    // The socket connecting to the card. It will be initialized when
    // connecting to the card or querying the status of the card.
    // Calling disconnect on the card object will close this socket.
    private Socket socket;
    private int verbosity;

    Emulator_connection_terminal(int p, int verbosity) {
        if(verbosity > 0) {
            System.out.format("EC terminal constructor on port %d\n", p);
        }
        port = p;
        socket = null;
        this.verbosity = verbosity;
    }


    // Returns the unique name of this terminal.
    public String getName() {
        return Integer.toString(port);
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
        throws CardException
    {
        if(protocol_name.equals("*"))
           protocol_name = "T=1";
           
        byte protocol;
        if(protocol_name.equals("T=0"))
            protocol = CadDevice.PROTOCOL_T0;
        else if(protocol_name.equals("T=1"))
            protocol = CadDevice.PROTOCOL_T1;
        else if(protocol_name.equals("T=CL"))
            protocol = CadDevice.PROTOCOL_TCL;
        else
            throw new IllegalArgumentException(
                "Emulator_connection_terminal.connect(protocol): " +
                "protocol must be one of *, T=0, T=1, T=CL");

        try {
            if(socket == null || socket.isClosed()) {
                socket = new Socket("localhost", port);
            }

            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            return new Emulator_connection_card(
                 port,
                 socket,
                 CadDevice.getCadClientInstance(protocol, is, os),
                 protocol,
                 verbosity);
        }           
        catch(IOException e) {
            throw new CardException(e);
        }
    }


    // Returns whether a card is present in this terminal.
    // Throws:
    //     CardException - if the status could not be determined
    public boolean isCardPresent()
        // throws CardException
    {
        throw new UnsupportedOperationException("not implemented yet");
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
        // throws CardException
    {
        throw new UnsupportedOperationException("not implemented yet");
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
        // throws CardException
    {
        throw new UnsupportedOperationException("not implemented yet");
    }


    public String toString() {
        return String.format("cref/jcwde emulator terminal at port %d", port);
    }

}
