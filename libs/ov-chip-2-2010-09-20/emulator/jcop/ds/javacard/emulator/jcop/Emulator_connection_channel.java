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
// CardChannel implementation for the emulator connection
// 
// $Id: Emulator_connection_channel.java,v 1.4 2009-02-20 15:29:21 tews Exp $

package ds.javacard.emulator.jcop;

import java.nio.ByteBuffer;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;
import javax.smartcardio.Card;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import com.ibm.jc.JCard;
import com.ibm.jc.JCException;


public class Emulator_connection_channel extends CardChannel {

    private JCard jcard;
    private Emulator_connection_card card;
    private int channel;
    private int verbosity;

    Emulator_connection_channel(JCard jcard, 
                                Emulator_connection_card card, 
                                int channel,
                                int verbosity) 
    {
        if(verbosity > 0) {
            System.out.println("JC channel constructor");
        }
        this.jcard = jcard;
        this.card = card;
        this.channel = channel;
        this.verbosity = verbosity;
    }


    // Checks whether this channel has not closed and 
    // whether the card has not been disconnected.
    public void check_not_closed_or_disconnected() {
        if(jcard == null)
            throw new IllegalStateException("Channel has been disconnected");
        card.check_not_disconnected();
    }


    // Returns the Card this channel is associated with.
    public Card getCard() {
        return card;
    }


    // Returns the channel number of this CardChannel. A channel number
    // of 0 indicates the basic logical channel.
    // Throws:
    //     IllegalStateException - if this channel has been closed or if
    //                             the corresponding Card has been
    //                             disconnected.
    public int getChannelNumber() {
        return channel;
    }



    // Transmits the specified command APDU to the Smart Card and returns
    // the response APDU.
    // 
    // The CLA byte of the command APDU is automatically adjusted to
    // match the channel number of this CardChannel.
    // 
    // Note that this method cannot be used to transmit MANAGE CHANNEL
    // APDUs. Logical channels should be managed using the
    // Card.openLogicalChannel() and CardChannel.close() methods.
    // 
    // Implementations should transparently handle artifacts of the
    // transmission protocol. For example, when using the T=0 protocol,
    // the following processing should occur as described in ISO/IEC
    // 7816-4:
    // 
    //     * if the response APDU has an SW1 of 61, the implementation
    //       should issue a GET RESPONSE command using SW2 as the
    //       Lefield. This process is repeated as long as an SW1 of 61 is
    //       received. The response body of these exchanges is
    //       concatenated to form the final response body.
    // 
    //     * if the response APDU is 6C XX, the implementation should
    //       reissue the command using XX as the Le field.
    // 
    // The ResponseAPDU returned by this method is the result after this
    // processing has been performed.
    // 
    // Parameters:
    //     command - the command APDU 
    // Returns:
    //     the response APDU received from the card 
    // Throws:
    //     IllegalStateException - if this channel has been closed or if
    //                             the corresponding Card has been
    //                             disconnected.
    //     IllegalArgumentException - if the APDU encodes a MANAGE
    //                                CHANNEL command
    //     NullPointerException - if command is null 
    //     CardException - if the card operation failed
    public ResponseAPDU transmit(CommandAPDU capdu)
        throws CardException
    {
        // XXX check for manage channel commands
        // XXX manage T=0 pecularities

        if(verbosity > 0) {
            System.out.println("JC transmit");
        }

        card.check_exclusive();
        check_not_closed_or_disconnected();

        // only do short apdu's
        // if(capdu.getNc() >= 256 || capdu.getNe() >= 256)
        //     throw new
        //      CardException("Emulators do not support extended APDU's");

        byte[] apdu_bytes = capdu.getBytes();
        byte[] res = null;
        try {
            res = jcard.send(0, apdu_bytes, 0, apdu_bytes.length);
        }
        catch(JCException e) {
            if(e.errorCode == JCException.TERMINAL_ERROR)
                throw new IllegalStateException("card error (disconnected?)",
                                                e);
            else
                throw new CardException(e);
        }

        if(verbosity > 0) {
            System.out.format("response apdu (%d bytes): ", res.length);
            System.out.flush();
            for(int i = 0; i < res.length; i++) {
                System.out.format(" %02X", res[i]);
            }
            System.out.println("");
        }

        return new ResponseAPDU(res);
    }


    // Transmits the command APDU stored in the command ByteBuffer and
    // receives the reponse APDU in the response ByteBuffer.
    // 
    // The command buffer must contain valid command APDU data starting
    // at command.position() and the APDU must be command.remaining()
    // bytes long. Upon return, the command buffer's position will be
    // equal to its limit; its limit will not have changed. The output
    // buffer will have received the response APDU bytes. Its position
    // will have advanced by the number of bytes received, which is also
    // the return value of this method.
    // 
    // The CLA byte of the command APDU is automatically adjusted to
    // match the channel number of this CardChannel.
    // 
    // Note that this method cannot be used to transmit MANAGE CHANNEL
    // APDUs. Logical channels should be managed using the
    // Card.openLogicalChannel() and CardChannel.close() methods.
    // 
    // See transmit() for a discussion of the handling of response APDUs
    // with the SW1 values 61 or 6C.
    // 
    // Parameters:
    //     command - the buffer containing the command APDU
    // 
    //     response - the buffer that shall receive the response APDU
    //                from the card
    // 
    // Returns:
    //     the length of the received response APDU 
    // Throws:
    // 
    //     IllegalStateException - if this channel has been closed or if
    //                             the corresponding Card has been
    //                             disconnected.
    // 
    //     NullPointerException - if command or response is null 
    //     ReadOnlyBufferException - if the response buffer is read-only 
    // 
    //     IllegalArgumentException - if command and response are the
    //                                same object, if response may not
    //                                have sufficient space to receive
    //                                the response APDU or if the APDU
    //                                encodes a MANAGE CHANNEL command
    // 
    //     CardException - if the card operation failed
    public int transmit(ByteBuffer command,
                        ByteBuffer response)
    {
        throw new UnsupportedOperationException("not implemented yet");
    }


    // Closes this CardChannel. The logical channel is closed by issuing
    // a MANAGE CHANNEL command that should use the format [xx 70 80 0n]
    // where n is the channel number of this channel and xx is the CLA
    // byte that encodes this logical channel and has all other bits set
    // to 0. After this method returns, calling other methods in this
    // class will raise an IllegalStateException.
    // 
    // Note that the basic logical channel cannot be closed using this
    // method. It can be closed by calling Card.disconnect(boolean).
    // 
    // Throws:
    //     CardException - if the card operation failed 
    // 
    //     IllegalStateException - if this CardChannel represents a
    //                             connection the basic logical channel
    public void close()
        throws CardException
    {
        card.check_exclusive();

        if(verbosity > 0) {
            System.out.println("JC channel close");
        }

        if(channel == 0)
            throw new IllegalStateException("Cannot close logical channel 0");

        jcard = null;

        throw new UnsupportedOperationException("not fully implemented yet");
    }
}

