// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2009
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
// Created 19.3.09 by Hendrik
// 
// integer performance test host driver
// 
// $Id: Int_host.java,v 1.2 2009-03-20 15:48:46 tews Exp $


import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.Card;


/**
 * 
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.2 $
 * @commitdate $Date: 2009-03-20 15:48:46 $ by $Author: tews $
 * @environment host, card
 * @CPP cpp preprocessing needed
 */
public class Int_host {

    public static void measure(PrintWriter out, CardChannel channel,
                               int rounds, int ins, String name) 
        throws javax.smartcardio.CardException
    {
        byte p1 = (byte)((rounds >> 8) & 0xff);
        byte p2 = (byte)(rounds & 0xff);


        // first measure overhead
        CommandAPDU apdu = new CommandAPDU(0x00,               // CLA
                                           (byte)ins,          // INS
                                           p1,                 // P1
                                           p2,                 // P2
                                           new byte[]{0x00});  // data
        if(Host.verbosity > 0)
            Host.print_apdu(out, apdu, name + " performance", Host.apduscript);
  
        long start = System.nanoTime();
        ResponseAPDU res = channel.transmit(apdu);
        long overhead = System.nanoTime() - start;

        Host.check_status_ok(out, "NOP perf", res);


        // measure now performance
        apdu = new CommandAPDU(0x00,                  // CLA
                               (byte)ins,             // INS
                               p1,                    // P1
                               p2,                    // P2
                               new byte[]{0x01});     // data
        if(Host.verbosity > 0)
            Host.print_apdu(out, apdu, name + " performance", Host.apduscript);
  
        start = System.nanoTime();
        res = channel.transmit(apdu);
        long duration = System.nanoTime() - start;

        Host.check_status_ok(out, "OP perf", res);
        if(Host.verbosity > 0)
            Host.print_response_apdu(out, res);
        out.format("%s %.1f micro (overhead %.1f ms all %.1f ms %d rounds)\n",
                   name,
                   ((duration - overhead) / 1E3) / rounds,
                   overhead / 1E6,
                   duration / 1E6,
                   rounds);
    }


    public static void main(String[] argv) 
        throws javax.smartcardio.CardException,
               java.security.NoSuchAlgorithmException
    {
        PrintWriter out = new PrintWriter(System.out, true);

        out.println("Int host driver");
        Host.applet_id = "int_test.app";
        Host.parse_commandline(argv);
        
        CardChannel channel = 
            Host.connect_and_select_card_channel(Host.card_reader_number, 
                                                 Host.terminal_type,
                                                 Host.applet_id, out, 
                                                 Host.apduscript);

        measure(out, channel, Host.rounds, 0x00, "short mult");

        measure(out, channel, Host.rounds, 0x01, "int mult");


        // disconnect
        Card card = channel.getCard();
        card.endExclusive();
        card.disconnect(true);
    }
}