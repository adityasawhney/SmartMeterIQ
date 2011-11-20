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
// Created 4.12.08 by Hendrik
// 
// card test frame
// 
// $Id: Card_testframe.java,v 1.25 2010-03-12 15:40:20 tews Exp $

package ds.ov2.front;


import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;

import ds.ov2.util.Card_terminal;
import ds.ov2.util.Card_terminal.Terminal_type;
import ds.ov2.util.Reference;
import ds.ov2.util.Option;
import ds.ov2.util.Value_option;
import ds.ov2.util.Int_option;
import ds.ov2.util.Bool_option;
import ds.ov2.util.Commandline;


/** 
 * Main class of the front office testframe. This test frame can
 * install and personalize all variants of the RSA applet, namely the
 * plain RSA applet, the Montgomerizing RSA applet, the squaring and
 * the square 4 RSA applet. It installs both, on Java Card or the jcop
 * emulator. Further it tests the resign and the proof protocols and
 * measures their performance for different key sizes. <P>
 *
 * This test frame is a non-interactive terminal program whose
 * behavior can be controlled by command line options. One can select
 * between two kinds of modes:
 * <DL>
 * <DT><STRONG>-test-const        </STRONG>
 * <DD>Test a fixed key size.
 * <DT><STRONG>-test-size</STRONG>
 * <DD>Test loop with increasing key size.
 * </DL>
 *
 * The test is organized in a number of nested loops. 
 * <UL>
 * <LI>For each key size {@link Test_state#ptls_param_rounds} many
 * PTLS key generation rounds are performed (controlled by option
 * {@code -ptls-rounds}). 
 * </LI>
 *
 * <LI>In each PTLS key generation round the test frame generates a
 * fresh set of PTLS keys and parameters and performs {@link
 * Test_state#card_init_rounds} many card initialization rounds
 * (controlled by option {@code -card-init-rounds}).
 * </LI>
 *
 * <LI>In each card initialization round the test frame (re-)installs
 * the applet on the card or emulator, generates a random set of
 * attributes (whose number is controlled by {@code -attr}) and
 * personalizes the applet (which includes running the resign protocol
 * once to provide the card with a blinding and a valid signature).
 * The test frame then performs {@link Test_state#resign_rounds} many
 * resign rounds (controlled by option {@code -resign-rounds})
 * </LI>
 *
 * <LI>In each resign round the test frame queries the status of the
 * card with the status protocol to be able to compute attribute
 * updates. It then computes attribute updates such that with
 * non-negligible propability the attribute value on the card becomes
 * 0 or the maximal possible value. Attribute updates are performed
 * both as addition and subtraction. With the attribute updates the
 * test frame runs the resign protocol and performs then {@link
 * Test_state#proof_rounds} many proof rounds (controlled by the
 * option {@code -proof-rounds}).
 * </LI>
 *
 * <LI>In each proof round the test frame runs the proof protocol
 * once. This involves checking the signature.
 * </LI>
 * </UL>
 *
 * For this test frame, which communicates with a Card or an emulator,
 * the defaults are 3 PTLS rounds per key size, 1 card initialization
 * round per PTLS parameter set, 3 resign rounds per applet, and 2
 * proof rounds per resign round. (The host test frames have different
 * defaults.)
 * <P>
 *
 * This test frame accepts the following options for customization.
 *
 * <DL>
 * <DT><STRONG>-mont</STRONG>
 * <DD>Install and test the Montgomerizing RSA applet [default].
 * <DT><STRONG>-plain</STRONG>
 * <DD>Install and test the plain RSA applet.
 * <DT><STRONG>-square</STRONG>
 * <DD>Install and test the squaring RSA applet.
 * <DT><STRONG>-square4</STRONG>
 * <DD>Install and test the square 4 RSA applet.
 * <DT><STRONG>-base-length n</STRONG>
 * <DD>Set key length to n bits, which will be identical to the base
 * size. If the exponent length has not been set before automatically
 * adapts the exponent length according to Lenstra's "Key length"
 * estimations. 
 * <DT><STRONG>-exp-length n      </STRONG>
 * <DD>Set exponent length to n bits.
 * <DT><STRONG>-attr n            </STRONG>
 * <DD>Set number of attributes that the card receives during
 * personalization. 
 * <DT><STRONG>-ptls-rounds n     </STRONG>
 * <DD>Number of ptls key generation rounds.
 * <DT><STRONG>-card-init-rounds n</STRONG>
 * <DD>Number of card initialization rounds.
 * <DT><STRONG>-resign-rounds n   </STRONG>
 * <DD>Number of resigning rounds.
 * <DT><STRONG>-proof-rounds n    </STRONG>
 * <DD>Number of proof rounds.
 * <DT><STRONG>-768               </STRONG>
 * <DD>Use the RSA 768 factorization of 2009 to calibrate the security
 * parameter estimation
 * <DT><STRONG>-ignore            </STRONG>
 * <DD>Ignore all wrong certificates and proofs.
 * <DT><STRONG>-jcop              </STRONG>
 * <DD>Connect to jcop emulator (default if the the necessary
 * libraries are present at runtime)
 * <DT><STRONG>-jcop-port p</STRONG>
 * <DD>Set the port number for the jcop emulator (default 8015).
 * <DT><STRONG>-sun               </STRONG>
 * <DD>Connect to a SUN emulator
 * <DT><STRONG>-c                 </STRONG>
 * <DD>Connect to a real card in the first card reader found (default
 * if the libraries for connecting to the jcop emulator are not present).
 * <DT><STRONG>-r n               </STRONG>
 * <DD>Connect to a real card in card reader number n.
 * <DT><STRONG>-list-readers      </STRONG>
 * <DD>List all connected readers.
 * <DT><STRONG>-i n               </STRONG>
 * <DD>Provide decimal n as input for the test.
 * <DT><STRONG>-hex n             </STRONG>
 * <DD>Provide hexadecimal n as input for the test.
 * <DT><STRONG>-h                 </STRONG>
 * <DD>Print usage information.
 * <DT><STRONG>-d, -dd, -ddd</STRONG>
 * <DD>Print debug and progress messages, the more d's the more
 * messages. 
 * </DL>
 *
 * The measurments for the resign and proof protocol are interspersed
 * in the output. To produce gnuplot charts the output must probably
 * be filtered, see the subdirectory front/Measurements for examples.
 * <P>
 *
 * The test frame can connect to the SUN emulators cref and jcwde too.
 * This would even be an option for the Montgomerizing RSA applet.
 * However, the builtin applet installation does not work with these
 * emulators ...
 * <P>
 *
 * Normally the host driver checks all signatures from the card and
 * the acceptance condition during the proof protocol. If any of these
 * checks fails the test frame terminates immediately. With the option
 * {@code -ignore} the test frame continues when these checks fail.
 * This is useful for the performance measurement of incorrect applets
 * (that have, for instance, been compiled with <a
 * href="../../../overview-summary.html#MONTGOMERY_MULT_SHORTCUT">MONTGOMERY_MULT_SHORTCUT<a>).
 * <P>
 *
 * This class contains the special parts for the card test frame. Many
 * parts that are shared with the host test frames ({@link
 * Host_testframe}) are contained in {@link Testframe}.
 * <P>
 *
 * Static class.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.25 $
 * @commitdate $Date: 2010-03-12 15:40:20 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Card_testframe {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected Card_testframe() {}
    

    /** Short application name for error messages. */
    public static final String short_application_name = "Test_card";

    /** Long application name. */
    public static final String long_application_name = 
        "Test applet host driver";


    /**
     * 
     * Default terminal. If the right libraries are present the jcop
     * emulator is the default, otherwise a PCSC card reader.
     */
    private static final Terminal_type terminal_type_default = 
        Card_terminal.jcop_provider_present() ? 
        Terminal_type.JCOP_EMULATOR : Terminal_type.PCSC_TERMINAL;


    /**
     * 
     * Terminal type to use. Set by options {@code -jcop, -sun, -r}
     * and {@code -c}.
     */
    private static Reference<Terminal_type> terminal_type = 
        new Reference<Terminal_type>(terminal_type_default);


    /**
     * 
     * The port number used for the jcop emulator.
     */
    private static Reference<Integer> jcop_port_number =
        new Reference<Integer>(8015);


    /**
     * 
     * Index of the card reader to use. Set by option {@code -r}.
     */
    private static Reference<Integer> card_reader_number = 
        new Reference<Integer>(0);


    /**
     * 
     * Whether to list all connected card readers. Set by option
     * {@code -list-readers}.
     */
    private static Reference<Boolean> run_list_readers = 
        new Reference<Boolean>(false);



    //########################################################################
    // configuration section, argument parsing
    //


    /**
     * 
     * Option array with the special options of the card test frame.
     */
    public static Option[] card_test_frame_options = new Option[]{
        new Value_option<Applet_type>(
             "-mont", Test_state.applet_type, Applet_type.MONT_RSA_APPLET,
             "test the Montgomerizing RSA applet" +
             (Test_state.init_applet_type == Applet_type.MONT_RSA_APPLET ?
              " [default]" : "")),
        new Value_option<Applet_type>(
             "-plain", Test_state.applet_type, Applet_type.PLAIN_RSA_APPLET,
             "test the plain RSA applet" +
             (Test_state.init_applet_type == Applet_type.PLAIN_RSA_APPLET ?
              " [default]" : "")),
        new Value_option<Applet_type>(
             "-square", Test_state.applet_type, Applet_type.SQUARED_RSA_APPLET,
             "test the squaring RSA applet" +
             (Test_state.init_applet_type == Applet_type.SQUARED_RSA_APPLET ?
              " [default]" : "")),
        new Value_option<Applet_type>(
             "-square4", Test_state.applet_type, 
             Applet_type.SQUARED4_RSA_APPLET,
             "test the square 4 RSA applet" +
             (Test_state.init_applet_type == Applet_type.SQUARED4_RSA_APPLET ?
              " [default]" : "")),
        new Value_option<Terminal_type>(
             "-jcop", terminal_type, Terminal_type.JCOP_EMULATOR,
             "connect to jcop emulator" + 
             (terminal_type_default == Terminal_type.JCOP_EMULATOR
              ? " [default]" : "")),
        new Int_option("-jcop-port", jcop_port_number, "p",
                       "use port p for the jcop emulator"),
        new Value_option<Terminal_type>(
             "-sun", terminal_type, Terminal_type.SUN_EMULATOR,
             "connect to a sun emulator" + 
             (terminal_type_default == Terminal_type.SUN_EMULATOR
              ? " [default]" : "")),
        new Value_option<Terminal_type>(
             "-c", terminal_type, Terminal_type.PCSC_TERMINAL,
             "connect to a real card reader" + 
             (terminal_type_default == Terminal_type.PCSC_TERMINAL
              ? " [default]" : "")),
        new Int_option("-r", card_reader_number, "n",
                       "connect to reader number n (implies -c)") {
            public void matched(Commandline cl) {
                super.matched(cl);
                terminal_type.ref = Terminal_type.PCSC_TERMINAL;
            }
        },
        new Bool_option("-list-readers", run_list_readers,
                        "list all connected readers")
    };


    //########################################################################
    // test code
    //


    /**
     * 
     * Main method of the card test frame. First sets the global
     * variables that determine various rounds in the applet life
     * simulation to somewhat lower values. Then parse the command
     * line options and run the actions that have been requested.
     * 
     * @param args command line arguments
     * @throws CardException on communication errors
     * @throws IOException if the applet cap file is not accessible
     * @throws NoSuchAlgorithmException if no provider for RSA key
     * generation can be found
     */
    public static void main(String[] args) 
        throws NoSuchAlgorithmException, CardException, IOException
    {
        Test_state.ptls_param_rounds.ref = 3;
        Test_state.card_init_rounds.ref = 1;
        Test_state.resign_rounds.ref = 3;
        Test_state.proof_rounds.ref = 2;

        System.out.println("### " + long_application_name);

        Testframe.parse_commandline(args, card_test_frame_options, 
                                    short_application_name);

        if(!State.base_length_set.ref) {
            State.base_length.ref = 
                Test_state.applet_type.ref.minimal_base_size();
            State.update_exponent_length();
        }

        if(run_list_readers.ref) {
            Card_terminal.print_readers(new PrintWriter(System.out, true));
            System.exit(0);
        }


        PrintWriter out = new PrintWriter(System.out, true);

        out.format("### Card testframe started on %s\n" +
                   "### command ./cardtestframe ",
                   new SimpleDateFormat("d MMMM yyyy HH:mm:ss z")
                   .format(new Date()));
        for(String arg : args) {
            System.out.print(arg);
            System.out.print(" ");
        }
        out.println("\n### Parameters:");
        out.format("###    type   %s\n", Test_state.applet_type.ref);
        out.format("###    base   %d\n", State.base_length.ref);
        out.format("###    exp    %d\n", State.exponent_length.ref);
        out.format("###    attr   %d\n", State.attribute_number.ref);
        out.format("###    year   %d\n", State.security_year);
        out.format("###    PTLS   %d\n", Test_state.ptls_param_rounds.ref);
        out.format("###    card   %d\n", Test_state.card_init_rounds.ref);
        out.format("###    resign %d\n", Test_state.resign_rounds.ref);
        out.format("###    proof  %d\n", Test_state.proof_rounds.ref);
        out.format("###    verb   %d\n", Test_state.verbosity.ref);
                   

        Object get_instance_parameter;
        switch(terminal_type.ref){
        case PCSC_TERMINAL:
        case SUN_EMULATOR:
            get_instance_parameter = null;
            break;
        case JCOP_EMULATOR:
            get_instance_parameter = new int[]{ jcop_port_number.ref };
            break;
        default:
            assert false;
            get_instance_parameter = null;
            break;
        }

        CardChannel card_channel = 
            Card_terminal.open_card_channel(card_reader_number.ref, 
                                            terminal_type.ref, 
                                            get_instance_parameter,
                                            out);

        if(Test_state.test_increase_size.ref)
            Testframe.test_size_loop(card_channel, out,
                                     State.attribute_number.ref,
                                     State.base_length.ref);
        else if(Test_state.test_const_size.ref)
            Testframe.test_init_resign_proof(card_channel, out,
                                             State.attribute_number.ref,
                                             State.base_length.ref,
                                             State.exponent_length.ref);
    }
}
