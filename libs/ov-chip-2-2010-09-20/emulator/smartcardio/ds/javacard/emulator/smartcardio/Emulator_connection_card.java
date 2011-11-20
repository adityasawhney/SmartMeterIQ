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
// card implementation for the emulator connection
// 
// $Id: Emulator_connection_card.java,v 1.5 2009-02-20 15:29:22 tews Exp $

package ds.javacard.emulator.smartcardio;

import java.io.IOException;
import java.lang.Thread;
import java.lang.IllegalStateException;
import java.net.Socket;
import java.util.LinkedList;
import javax.smartcardio.ATR;
import javax.smartcardio.CardException;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import com.sun.javacard.apduio.CadTransportException;
import com.sun.javacard.apduio.CadClientInterface;
import com.sun.javacard.apduio.CadDevice;


public class Emulator_connection_card extends Card {

    private int port;
    // This is the socket connecting to the emulator. It has been 
    // opened in Emulator_connection_terminal and with its streams
    // the CadClientInterface has been constructed.
    // The disconnect method here invalidates all open channels,
    // closes the socket and sets 
    // the socket and the cad to null. 
    private Socket socket;
    private ATR atr;
    private byte protocol;
    private CadClientInterface cad;
    private int verbosity;

    private Emulator_connection_channel basic_channel;
    private LinkedList<Emulator_connection_channel> open_channels;

    // The owning thread, if beginExclusive has been issued.
    Thread owner;

    Emulator_connection_card(int port_arg, Socket s, CadClientInterface c, 
                             byte p, int verb) 
        throws CardException
    {
        assert p == CadDevice.PROTOCOL_T0 || 
            p == CadDevice.PROTOCOL_T1 ||
            p == CadDevice.PROTOCOL_TCL;
        assert s.isConnected();

        port = port_arg;
        socket = s;
        cad = c;
        protocol = p;
        verbosity = verb;
        basic_channel = null;
        open_channels = new LinkedList<Emulator_connection_channel>();
        owner = null;

        try {
            atr = new ATR(c.powerUp());
        }
        catch(CadTransportException e) {
            throw new CardException("apduio error: " + e.getMessage(), e);
        }
        catch(IOException e) {
            throw new CardException(e);
        }
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


    // Returns the ATR of this card.
    public ATR getATR() {
        return atr;
    }


    // Returns the protocol in use for this card.
    public String getProtocol() {
        switch(protocol) {
        case CadDevice.PROTOCOL_T0:
            return "T=0";
        case CadDevice.PROTOCOL_T1:
            return "T=1";
        case CadDevice.PROTOCOL_TCL:
            return "T=CL";
        default:
            assert false;
            return "internal error";
        }
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
        // XXX check security and disconnect!
        if(cad == null)
            throw new IllegalStateException("Card has been closed");

        if(basic_channel == null) {
            basic_channel = 
                new Emulator_connection_channel(this, cad, 0, verbosity);
            open_channels.addFirst(basic_channel);
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
        if(cad == null)
            throw new IllegalStateException("Card has been closed");

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

        for(Emulator_connection_channel c : open_channels) {
            c.destroy();
        }
        if(reset) {
            try {
                cad.powerDown();
            }
            catch(IOException e) {
                throw new CardException(e);
            }
            catch(CadTransportException e) {
                throw new CardException(e);
            }
        }
        cad = null;
        try {
            socket.close();
        }
        catch(IOException e) {
            throw new CardException(e);
        }
        socket = null;
        open_channels = null;
    }


    public String toString() {
        if(cad == null)
            return String.format
                ("disconnected cref/jcwde emulator card at port %d", port);
        else
            return String.format
                ("connected cref/jcwde emulator card at port %d", port);
    }
}

