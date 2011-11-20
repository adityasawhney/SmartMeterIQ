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
// CardChannel implementation for the emulator connection
// 
// $Id: Emulator_connection_channel.java,v 1.5 2009-02-20 15:29:22 tews Exp $

package ds.javacard.emulator.smartcardio;

import java.io.IOException;
import java.nio.ByteBuffer;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;
import javax.smartcardio.Card;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import com.sun.javacard.apduio.CadTransportException;
import com.sun.javacard.apduio.Apdu;
import com.sun.javacard.apduio.CadClientInterface;


public class Emulator_connection_channel extends CardChannel {


    private int verbosity;
    private Emulator_connection_card card;
    // The cad contains the streams that connect over the socket to the 
    // emulator. When the card or this channel is closed we set cad to null.
    private CadClientInterface cad;
    private int channel;

    Emulator_connection_channel(Emulator_connection_card card_arg, 
                                CadClientInterface cad_arg,
                                int channel_arg,
                                int verb) {
        assert channel_arg == 0;
        card = card_arg;
        cad = cad_arg;
        channel = channel_arg;
        verbosity = verb;
    }


    // Makes this channel unusable (because the card has been disconnected).
    void destroy() {
        cad = null;
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
        if(cad == null)
            throw new IllegalStateException
                ("Channel has been closed or card has been disconnected");

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

        if(cad == null)
            throw new IllegalStateException
                ("Channel has been closed or card has been disconnected");

        card.check_exclusive();

        // only do short apdu's
        if(capdu.getNc() >= 256 || capdu.getNe() >= 256)
            throw new CardException("Emulators do not support extended APDU's");

        Apdu apdu = new Apdu();
        apdu.isExtended = false;
        apdu.command[apdu.CLA] = (byte)capdu.getCLA();
        apdu.command[apdu.INS] = (byte)capdu.getINS();
        apdu.command[apdu.P1] = (byte)capdu.getP1();
        apdu.command[apdu.P2] = (byte)capdu.getP2();
        byte[] data = capdu.getData();
        assert data.length == capdu.getNc();
        apdu.setDataIn(data, data.length);
        // if(capdu.getNe() > 0)
        //     apdu.setLe(capdu.getNe());
        apdu.Le = capdu.getNe();

        assert apdu.isExtended == false;
        if(verbosity > 0)
            System.out.format("ET apdu %s\n", apdu.toString());

        try {
            cad.exchangeApdu(apdu);
        }
        catch(CadTransportException e) {
            throw new CardException(e.getMessage(), e);
        }
        catch(IOException e) {
            throw new CardException(e);
        }

        //byte[] res = apdu.getResponseApduBytes();
        byte[] res = new byte[apdu.dataOut.length + 2];
        System.arraycopy(apdu.dataOut, 0, res, 0, apdu.dataOut.length);
        System.arraycopy(apdu.sw1sw2, 0, res, apdu.dataOut.length, 2);

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
        throws CardException
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

        if(channel == 0)
            throw new IllegalStateException("Cannot close logical channel 0");
        cad = null;

        throw new UnsupportedOperationException("not fully implemented yet");
    }
}

