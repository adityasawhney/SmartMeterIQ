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
// Created 4.10.08 by Hendrik
// 
// test the emulator TerminalFactory
// 
// $Id: Test_emulator.java,v 1.5 2009-02-20 15:29:22 tews Exp $

package ds.javacard.emulator.test;

import java.util.List;
import java.util.ListIterator;
import java.security.Security;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.CardChannel;
import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import ds.javacard.emulator.smartcardio.*;


public class Test_emulator {

    public static void main(String[] argv) 
        throws javax.smartcardio.CardException,
               java.security.NoSuchAlgorithmException
    {

        System.out.println("hallo");

        DS_provider p = new DS_provider();
        Security.addProvider(p);

        // show the list of available terminals
        TerminalFactory factory = 
            TerminalFactory.getInstance("SunEmulator", null);
        //TerminalFactory factory = TerminalFactory.getDefault();
        System.out.println("Factory type " + factory.getType());
        List<CardTerminal> terminals = factory.terminals().list();
        System.out.println("Terminals: " + terminals);
        // get the first terminal

        for(ListIterator<CardTerminal> i = terminals.listIterator(); 
            i.hasNext(); ) 
        {
            System.out.format("  %2d: %s\n", 
                              i.nextIndex(), 
                              i.next().toString());
        }


       CardTerminal terminal = terminals.get(0);

       // establish a connection with the card
       Card card = terminal.connect("T=1");
       System.out.println("card: " + card);

       CardChannel channel = card.getBasicChannel();

       byte[] select = new byte[]{00, (byte)0xA4, (byte)0x04, 00, 
                                  (byte)0x0B, (byte)0x6F, (byte)0x76, 
                                  (byte)0x5F, (byte)0x74, (byte)0x65, 
                                  (byte)0x73, (byte)0x74, (byte)0x2E, 
                                  (byte)0x61, (byte)0x70, (byte)0x70, 
                                  (byte)0xFF};

       CommandAPDU apdu = new CommandAPDU(select);
       System.out.format("Send apdu %s\n", apdu.toString());
       ResponseAPDU r = channel.transmit(apdu);
       System.out.println("response: " + r.toString());

//      byte[] ping = new byte[]{(byte)0x00, (byte)0x00, (byte)0x00, 
//                               (byte)0x00, (byte)0x00};
//
//        r = channel.transmit(new CommandAPDU(ping));
//        System.out.println("response: " + r.toString());
//
//        // disconnect
//        card.disconnect(false);
    }
}
