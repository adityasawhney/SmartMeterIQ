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
// Created 2.10.08 by Hendrik
// 
// host for test applet
// 
// $Id: Host.java,v 1.10 2009-04-22 14:36:08 tews Exp $

import javax.smartcardio.*;
import java.util.*;
import java.io.*;
import java.security.*;
import javacard.framework.ISO7816;


class Host {

    public static enum Terminal_type {
        PCSC_TERMINAL,
        SUN_EMULATOR,
        JCOP_EMULTOR
    }    


    public static final String short_application_name = "test host";

    public static final String long_application_name = 
        "Test applet host driver";

    //########################################################################
    // configuration section
    //

    public static String applet_id = "rsa_test.app";

    public static int card_reader_number = 0;

    public static int verbosity = 0;

    public static int rounds = 10;

    private static final Terminal_type terminal_type_default = 
        Terminal_type.JCOP_EMULTOR;
    public static Terminal_type terminal_type = terminal_type_default;

    // Print apduscript lines if verbosity is big enough.
    public static boolean apduscript = false;


    public static void parse_commandline(String[] args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-h") || 
               args[i].equals("-help") ||
               args[i].equals("--help")) {
                usage();
                System.exit(0);
            }
            else if(args[i].equals("-id")) {
                applet_id = get_string_argument(i, args);
                i++;
            }
            else if(args[i].equals("-jcop")) {
                terminal_type = Terminal_type.JCOP_EMULTOR;
            }
            else if(args[i].equals("-sun")) {
                terminal_type = Terminal_type.SUN_EMULATOR;
            }
            else if(args[i].equals("-c")) {
                terminal_type = Terminal_type.PCSC_TERMINAL;
            }
            else if(args[i].equals("-r")) {
                card_reader_number = get_int_argument(i, args);
                i++;
                terminal_type = Terminal_type.PCSC_TERMINAL;
            }
            else if(args[i].equals("-apdutool")) {
                apduscript = true;
            }
            else if(args[i].equals("-rounds")) {
                rounds = get_int_argument(i, args);
                i++;
            }
            else if(args[i].equals("-d")) {
                verbosity = 5;
            }
            else if(args[i].equals("-dd")) {
                verbosity = 10;
            }
            else if(args[i].equals("-ddd")) {
                verbosity = 15;
            }
            else {
                System.out.format("%s: unrecognized option %s\n",
                                  short_application_name, args[i]);
                usage();
                System.exit(1);
            }
        }
        return;
    }


    // print this for -h or when command line parsing fails
    public static void usage() {
        System.out.println(long_application_name);
        System.out.println("Usage: java ds/ov/test/Test_host [options...]");
        System.out.println("Recognized options are:");
        format_option_description(new String[][]{
            {"-id applet-id", "set applet id to select"},
            {"-jcop",         "connect to jcop emulator" + 
                                 (terminal_type_default == 
                                  Terminal_type.JCOP_EMULTOR
                                  ? " [default]" : "")},
            {"-sun",          "connect to a sun emulator" + 
                                 (terminal_type_default == 
                                  Terminal_type.SUN_EMULATOR
                                  ? " [default]" : "")},
            {"-c",            "connect to a real card reader" + 
                                 (terminal_type_default == 
                                  Terminal_type.PCSC_TERMINAL
                                  ? " [default]" : "")},
            {"-r n",          "connect to reader number n (implies -c)"},
            {"-h",            "print usage information"},
            {"-d[d[d]]",      "print debug messages, " + 
                                    "the more d's the more messages"},
            {"-apdutool",     "print apdutool lines"},
            {"-rounds",       "set number of rounds (only for Int_test)"}
        });
    }


    public static void format_option_description(String[][] opt) {
        int longest_left = 0;
        for(int i = 0; i < opt.length; i++) {
            if(opt[i][0].length() > longest_left)
                longest_left = opt[i][0].length();
        }
        for(int i = 0; i < opt.length; i++) {
            String[] o = opt[i];
            System.out.print("  ");
            System.out.print(o[0]);
            for(int j = o[0].length(); j < longest_left; j++)
                System.out.print(' ');
            System.out.print("  ");
            System.out.println(o[1]);
        }
    }


    public static int get_int_argument(int i, String[] args) {
        if(i + 1  < args.length)
            return Integer.parseInt(args[i + 1]);
        else {
            System.out.format("%s: option %s requires an int argument",
                              short_application_name,
                              args[i]);
            System.exit(1);
        }
        return -1;
    }

    public static String get_string_argument(int i, String[] args) {
        if(i + 1  < args.length)
            return args[i + 1];
        else {
            System.out.format("%s: option %s requires a string argument",
                              short_application_name,
                              args[i]);
            System.exit(1);
        }
        return "";
    }


    // Encode an applet id string into a byte array.
    public static byte[] encode_applet_id(String id) {
        if(id.length() < 5 || id.length() > 16) {
            System.err.println("An application identifier is between 5 and " +
                               "16 characters long!");
            System.exit(1);
        }

        byte[] ba = new byte[id.length()];

        for(int j = 0; j < id.length(); j++) {
            ba[j] = (byte)(id.codePointAt(j));
        }
        return ba;
    }


    // Print apdu in human readable format to out, if out != null.
    // Print additionally an apdutool line if apduscript == true.
    // Message msg is inserted in the output.
    public static void print_apdu(PrintWriter out, CommandAPDU apdu, 
                                  String msg, boolean apduscript) 
    {
        if(out == null) return;

        out.format("%s CLA:%02X INS:%02X P1:%02X P2:%02X ",
                   msg,
                   apdu.getCLA(), apdu.getINS(), 
                   apdu.getP1(), apdu.getP2());

        String apdutool_line = "";
        if(apduscript)
            apdutool_line = 
                String.format("// apdutool\n0x%02X 0x%02X 0x%02X 0x%02X",
                              apdu.getCLA(), apdu.getINS(), 
                              apdu.getP1(), apdu.getP2());

        if(apdu.getNc() == 0 && apdu.getNe() == 0) {
            out.println("no send and no receive data");
            if(apduscript)
                out.format("%s 0x00 0x00;\n", apdutool_line);
            return;
        }
        
        if(apdu.getNc() > 0) {
            out.format("NC:%02X ", apdu.getNc());
            if(apduscript)
                apdutool_line = 
                    apdutool_line.concat(String.format(" 0x%02X",
                                                       apdu.getNc()));
            if(apdu.getNe() > 0) {
                out.format("NE:%02X ", apdu.getNe());
            }
            else {
                out.print("no NE ");
            }
            out.format("\n   data: ");
            byte[] data = apdu.getData();
            for(int i = 0; i < data.length; i++) {
                out.format("%02X", data[i]);
                if(apduscript)
                    apdutool_line = 
                        apdutool_line.concat(String.format(" 0x%02X", data[i]));
                if(i % 2 == 1 && i + 1 < data.length)
                    out.append('.');
                }
            out.format("\n");
        }
        else {
            out.format("no data NE:%02X\n", apdu.getNe());
            if(apduscript)
                // add only Nc to apdutool_line
                apdutool_line = apdutool_line.concat(" 0x00");
        }

        if(apduscript)
            out.format("%s 0x%02X;\n", apdutool_line, apdu.getNe());
    }
                   

    public static void print_response_apdu(PrintWriter out, ResponseAPDU res) {
        out.format("Status 0x%04X), received %d bytes",
                   res.getSW(), res.getNr());

        if(res.getNr() == 0)
            out.println(" no data");
        else {
            byte[] data = res.getData();
            out.format("\ndata: %02X", data[0]);
            for(int i = 1; i < res.getNr(); i++) {
                out.format(" %02X", data[i]);
            }
            out.println("");
        }
    }


    // Get all connected readers.
    public static List<CardTerminal> get_all_terminals() 
        throws NoSuchAlgorithmException, CardException
    {
        //TerminalFactory factory = TerminalFactory.getDefault();
        TerminalFactory factory = TerminalFactory.getInstance("PC/SC", null);
        // System.out.println("Factory type " + factory.getType());
        return factory.terminals().list();
    }


    /**
     * Returns a terminal list for connecting to one of the SUN
     * emulators (cref or jcwde). 
     * Loads the {@link Provider Provider} for the SunEmulator
     * {@link TerminalFactory TerminalFactory} that gives access to
     * one of the SUN emulators via the {@link javax.smartcardio} package.
     * This works only if emulatorio.jar 
     * and apduio.jar are accessible at runtime. 
     *
     * @return terminal list for connecting to a SUN emulator
     * @throws CardException if one of the necessary libraries is missing
     *     or if communication error with the emulator occurs
     * @throws NoSuchAlgorithmException is never thrown when the right
     *     emulatorio.jar is provided.
     */
    public static List<CardTerminal> get_all_sun_emulators() 
        throws NoSuchAlgorithmException, CardException
    {
        Class<?> sun_provider;

        try {
            sun_provider =
                Class.forName("ds.javacard.emulator.smartcardio.DS_provider");
            Security.addProvider((Provider)(sun_provider.newInstance()));
        }
        catch(ClassNotFoundException e) {
            throw new CardException("emulatorio.jar missing", e);
        }
        catch(ProviderException e) {
            throw new CardException("apduio.jar missing", e);
        }
        catch(InstantiationException e) {
            assert false;
            throw new CardException
                ("sun emulator communication library incompatibility", e);
        }
        catch(IllegalAccessException e) {
            assert false;
            throw new CardException
                ("sun emulator communication library incompatibility", e);
        }
    

        TerminalFactory factory = 
            TerminalFactory.getInstance("SunEmulator", null);

        // System.out.println("Factory type " + factory.getType());
        return factory.terminals().list();
    }


    /**
     * Returns a terminal list for connecting to the jcop emulator.
     * For that the appropriate provider is loaded via {@link 
     * #add_jcop_provider}.
     * 
     * @return a terminal list for accessing a jcop emulator
     * @throws CardException if one of the necessary libraries is missing 
     *    (see throws clause at {@link #add_jcop_provider})
     *    or a communication problem is detected
     * @throws NoSuchAlgorithmException never thrown if the right libraries 
     *    are used
     */
    public static List<CardTerminal> get_all_jcop_emulators() 
        throws NoSuchAlgorithmException, CardException
    {
        Class<?> jcop_provider;

        try {
            jcop_provider = 
                Class.forName("ds.javacard.emulator.jcop.DS_provider");
            Security.addProvider((Provider)(jcop_provider.newInstance()));
        }
        catch(ClassNotFoundException e) {
            throw new CardException("jcopio.jar missing", e);
        }
        catch(ProviderException e) {
            throw new CardException("offcard.jar missing", e);
        }
        catch(InstantiationException e) {
            assert false;
            throw new CardException
                ("jcop communication library incompatibility", e);
        }
        catch(IllegalAccessException e) {
            assert false;
            throw new CardException
                ("jcop communication library incompatibility", e);
        }
        TerminalFactory factory = 
            TerminalFactory.getInstance("JcopEmulator", null);

        // System.out.println("Factory type " + factory.getType());
        return factory.terminals().list();
    }



    public static void check_status_ok(PrintWriter out, String name, 
                                       ResponseAPDU res) 
    {
        if(res.getSW() != (ISO7816.SW_NO_ERROR & 0xffff)) {
            out.format("%s FAILED with status 0x%04X\n",
                       name,
                       res.getSW());
            System.exit(1);
        }
    }


    // Connect to the terminal with number card_reader, 
    // select the applet named applet_id and return it.
    // If card_reader == 0 the first card reader is used.
    // If use_emulator is true a connection to the jcop emulator is 
    // made (and the card_reader is ignored then).
    // If out is non-null report debug information there.
    public static CardChannel connect_and_select_card_channel_ex
        (int terminal_number, Terminal_type terminal_type, String applet_id, 
         PrintWriter out, boolean apduscript) 
        throws CardException, NoSuchAlgorithmException
    {
        List<CardTerminal> terminals;
        switch(terminal_type) {
        case PCSC_TERMINAL:
            if(verbosity >= 5 && out != null)
                out.print("Connecting to card terminals ...");
            terminals = get_all_terminals();
            break;

        case SUN_EMULATOR:
            if(verbosity >= 5 && out != null) {
                out.print("Connecting to a SUN emulator ...");
                out.flush();
            }
            terminals = get_all_sun_emulators();
            break;

        case JCOP_EMULTOR:
            if(verbosity >= 5 && out != null)
                out.print("Connecting to the jcop emulator ...");
            terminals = get_all_jcop_emulators();
            break;

        default:
            // There are only three constants in Terminal_type, nevertheless
            // java demands that we assign here something to terminals.
            terminals = null;
            assert false;
            System.exit(1);
        }

        if(terminals.size() == 0) {
            if(verbosity >= 5 && out != null)
                out.println("");
            throw new CardException("Found no card terminals");
        }
        if(terminal_number > terminals.size()) {
            if(verbosity >= 5 && out != null)
                out.println("");
            throw new CardException(String.format("Invalid terminal number %d", 
                                                  terminal_number));
        }

        Card card = terminals.get(terminal_number).connect("*");
        if(verbosity >= 5 && out != null)
            out.println(" connected");

        // gives exclusive exess to the card from this thread
        card.beginExclusive();
    
        if(verbosity >= 5 && out != null)
            out.println("select applet " + applet_id);
        CardChannel ch = card.getBasicChannel();
        byte[] byte_id = encode_applet_id(applet_id);
        CommandAPDU apdu = new CommandAPDU(0x00,               // CLA
                                           0xA4,               // INS
                                           0x04,               // P1
                                           0x00,               // P2
                                           byte_id);     // data
        if(out != null && verbosity >= 15)
            print_apdu(out, apdu, "Select applet", apduscript);
  
        ResponseAPDU res = ch.transmit(apdu);

        if(res.getSW() != (ISO7816.SW_NO_ERROR & 0xffff)) {
            out.println("Applet selection failed.");
            print_response_apdu(out, res);
            throw new CardException("Applet selection failed");
        }

        return ch;
    }


    public static CardChannel connect_and_select_card_channel
        (int terminal_number, Terminal_type terminal_type, String applet_id, 
         PrintWriter out, boolean apduscript) 
    {
        try {
            return connect_and_select_card_channel_ex(terminal_number, 
                                                      terminal_type, 
                                                      applet_id, 
                                                      out, 
                                                      apduscript);
        }
        catch(NoSuchAlgorithmException e) {
            PrintWriter oout = 
                out == null ? new PrintWriter(System.err, true) : out;
            oout.format("Driver error during connect %s\n", e);
            System.exit(1);
            return null;
        }
        catch(CardException e) {
            PrintWriter oout = 
                out == null ? new PrintWriter(System.err, true) : out;
            oout.format("Card communication error during connect %s\n", e);
            System.exit(1);
            return null;
        }
    }



    public static void main(String[] argv) 
        throws javax.smartcardio.CardException,
               java.security.NoSuchAlgorithmException
    {
        PrintWriter out = new PrintWriter(System.out, true);

        out.println("Host driver for test applet");
        parse_commandline(argv);
        

        CardChannel channel = 
            connect_and_select_card_channel(card_reader_number, terminal_type,
                                            applet_id, out, apduscript);


        CommandAPDU apdu = new CommandAPDU(0x00,               // CLA
                                           0x00,               // INS
                                           0x00,               // P1
                                           0x00);              // P2
        print_apdu(out, apdu, "ping", apduscript);
  
        ResponseAPDU res = channel.transmit(apdu);

        print_response_apdu(out, res);

        

        // disconnect
        Card card = channel.getCard();
        card.endExclusive();
        card.disconnect(false);
    }
}
