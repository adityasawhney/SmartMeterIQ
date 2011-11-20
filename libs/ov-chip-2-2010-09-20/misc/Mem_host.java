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
// $Id: Mem_host.java,v 1.1 2009-04-22 14:36:08 tews Exp $


import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.Card;
import javax.smartcardio.ATR;

import javacard.framework.APDU;


class Mem_host {

    public static int get_short(byte buf[], int index) {
        return ((buf[index] << 8) & 0xff00) | (buf[index + 1] & 0xff);
    }

    public static boolean get_boolean(byte buf[], int index) {
        if(buf[index] == 0x01)
            return true;
        else
            return false;
    }



    public static String string_of_atr(ATR atr) {
        byte[] buf = atr.getBytes();
        String res = "";
        for(int i = 0; i < buf.length; i++) {
            res = res + String.format("%02X ", buf[i] & 0xff);
        }
        return res;
    }


    public static void main(String[] argv) 
        throws javax.smartcardio.CardException,
               java.security.NoSuchAlgorithmException
    {
        PrintWriter out = new PrintWriter(System.out, true);

        out.println("MEM Host driver");
        Host.applet_id = "mem_test.app";
        Host.parse_commandline(argv);
        
        CardChannel channel = 
            Host.connect_and_select_card_channel(Host.card_reader_number, 
                                                 Host.terminal_type,
                                                 Host.applet_id, out, 
                                                 Host.apduscript);


        CommandAPDU apdu = new CommandAPDU(0x00,               // CLA
                                           0x00,               // INS
                                           0x00,               // P1
                                           0x00,               // P2
                                           0xff);              // NE
        if(out != null && Host.verbosity >= 15)
            Host.print_apdu(out, apdu, "Send mem apdu", Host.apduscript);

        ResponseAPDU res = channel.transmit(apdu);

        if(out != null && Host.verbosity >= 15)
            Host.print_response_apdu(out, res);

        byte[] card_data = res.getData();

        int index = 0;

        if(get_short(card_data, index) != 0x95A0) {
            out.println("Data tranmission error, exiting!");
            System.exit(1);
        }

        index += 2;

        int mem_persistent = get_short(card_data, index);
        index += 2;

        int mem_transient_reset = get_short(card_data, index);
        index += 2;

        int mem_transient_deselect = get_short(card_data, index);
        index += 2;

        int version = get_short(card_data, index);
        index += 2;

        int apdu_length = get_short(card_data, index);
        index += 2;

        boolean gc = get_boolean(card_data, index);
        index += 1;

        byte protocol = card_data[index];
        index += 1;

        if(index != card_data.length) {
            out.println("Data tranmission error, length wrong, exiting!");
            System.exit(1);
        }


        String media_type;

        switch(protocol & APDU.PROTOCOL_MEDIA_MASK) {
        case APDU.PROTOCOL_MEDIA_CONTACTLESS_TYPE_A:
            media_type = "contactless A";
            break;
        
        case APDU.PROTOCOL_MEDIA_CONTACTLESS_TYPE_B:
            media_type = "contactless B";
            break;
        
        case APDU.PROTOCOL_MEDIA_DEFAULT:
            media_type = "default (contact)";
            break;
        
        case APDU.PROTOCOL_MEDIA_USB:
            media_type = "USB";
            break;
        
        default:
            media_type = String.format("unknown media type %01X",
                                       (protocol >> 4) & 0xf);
            break;
        }

        String protocol_type;
        switch(protocol & APDU.PROTOCOL_TYPE_MASK) {
        case APDU.PROTOCOL_T0:
            protocol_type = "T0";
            break;

        case APDU.PROTOCOL_T1:
            protocol_type = "T1";
            break;

        default:
            protocol_type = String.format("unkown protocol type %01X",
                                          (protocol & 0xf));
            break;
        }



        out.format("\nCARD INFORMATION\n" +
                   "  ATR     %s\n" +
                   "  java card version        0x%04X = %d\n" +
                   "  APDU buffer length       0x%04X = %d\n" +
                   "  connection / protocol    0x%02X   = %s over %s\n" +
                   "  Persistent memory        0x%04X = %d\n" +
                   "  Transient reset mem      0x%04X = %d\n" +
                   "  Transient deselect mem   0x%04X = %d\n" +
                   "  object deletion support  %s\n",
                   string_of_atr(channel.getCard().getATR()),
                   version, version,
                   apdu_length, apdu_length,
                   protocol, protocol_type, media_type,
                   mem_persistent, mem_persistent,
                   mem_transient_reset, mem_transient_reset,
                   mem_transient_deselect, mem_transient_deselect,
                   gc ? "YES" : "NO");
                   

        // disconnect
        Card card = channel.getCard();
        card.endExclusive();
        card.disconnect(false);
    }
}
