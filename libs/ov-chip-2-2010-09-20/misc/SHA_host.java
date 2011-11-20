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
// Created 26.11.08 by Hendrik
// 
// host for test_sha applet
// 
// $Id: SHA_host.java,v 1.3 2009-02-20 15:29:27 tews Exp $


import java.io.PrintWriter;
import java.security.MessageDigest;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.Card;


class SHA_host {

    static byte[] input = new byte[]{
        (byte)(0x00), (byte)(0x01), (byte)(0x02), (byte)(0x03),
        (byte)(0x04), (byte)(0x05), (byte)(0x06), (byte)(0x07),
        (byte)(0x08), (byte)(0x09), (byte)(0x10), (byte)(0x11),
        (byte)(0x12), (byte)(0x13), (byte)(0x14), (byte)(0x15),
        (byte)(0x16), (byte)(0x17), (byte)(0x18), (byte)(0x19), 
        (byte)(0x20), (byte)(0x21), (byte)(0x22), (byte)(0x23),
        (byte)(0x24), (byte)(0x25), (byte)(0x26), (byte)(0x27),
        (byte)(0x28), (byte)(0x29), (byte)(0x30), (byte)(0x31),
        (byte)(0x32), (byte)(0x33), (byte)(0x34), (byte)(0x35),
        (byte)(0x36), (byte)(0x37), (byte)(0x38), (byte)(0x39), 
        (byte)(0x50), (byte)(0x51), (byte)(0x52), (byte)(0x53),
        (byte)(0x54), (byte)(0x55), (byte)(0x56), (byte)(0x57),
        (byte)(0x58), (byte)(0x59), (byte)(0x60), (byte)(0x61),
        (byte)(0x62), (byte)(0x63), (byte)(0x64), (byte)(0x65),
        (byte)(0x66), (byte)(0x67), (byte)(0x68), (byte)(0x69)
    };


    public static void main(String[] argv) 
        throws javax.smartcardio.CardException,
               java.security.NoSuchAlgorithmException
    {
        PrintWriter out = new PrintWriter(System.out, true);

        out.println("SHA Host driver");
        Host.applet_id = "sha_test.app";
        Host.parse_commandline(argv);
        

        MessageDigest digest = MessageDigest.getInstance("SHA");
        byte [] output = digest.digest(input);
        out.format("Host digest length %d\n", output.length);
        for(int i = 0; i < output.length; i++)
            out.format(" %02X", output[i]);
        out.println("");


        CardChannel channel = 
            Host.connect_and_select_card_channel(Host.card_reader_number, 
                                                 Host.terminal_type,
                                                 Host.applet_id, out, 
                                                 Host.apduscript);


        CommandAPDU apdu = new CommandAPDU(0x00,               // CLA
                                           0x00,               // INS
                                           0x00,               // P1
                                           0x00);              // P2
        Host.print_apdu(out, apdu, "ping", Host.apduscript);
  
        ResponseAPDU res = channel.transmit(apdu);

        Host.print_response_apdu(out, res);

        byte[] card_data = res.getData();

        int card_len = ((card_data[0] & 0xff) << 8) | (card_data[1] & 0xff);

        boolean identical = true;

        if(output.length == card_len)
            out.format("Same card digest length %d\n", card_len);
        else {
            out.format("Different card digest length %d\n", card_len);
            identical = false;
        }           

        for(int i = 0; i < card_len; i++) 
            if(output[i] != card_data[i + 2]) {
                identical = false;
                out.format("difference at byte %d\n", i);
                break;
            }

        if(identical)
            out.println("Identical digest on host and card");

        // disconnect
        Card card = channel.getCard();
        card.endExclusive();
        card.disconnect(false);
    }
}
