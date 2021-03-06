//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/Testframe.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/Testframe.java"
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
// Created 13.11.08 by Hendrik
// 
// common testframe functionality for RSA applet
// 
// $Id: Testframe.java,v 1.41 2010-03-12 15:40:20 tews Exp $

//# 1 "./config" 1
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
// Created 5.11.08 by Hendrik
// 
// preprocessor config directives
// 
// $Id: config,v 1.16 2010-02-18 12:40:38 tews Exp $
//# 200 "./config"
/// Local Variables:
/// mode: c
/// End:
//# 26 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/front/Testframe.java" 2


package ds.ov2.front;


import java.util.Date;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;

import ds.ov2.util.Misc_host;
import ds.ov2.util.Card_terminal;
import ds.ov2.util.Reference;
import ds.ov2.util.Option;
import ds.ov2.util.Bool_option;
import ds.ov2.util.Int_option;
import ds.ov2.util.Bool_int_option;
import ds.ov2.util.BigInt_input_option;
import ds.ov2.util.BigInt_hex_input_option;
import ds.ov2.util.Value_option;
import ds.ov2.util.Parse_commandline;
import ds.ov2.util.Security_parameter;
import ds.ov2.util.Security_parameter.Calibration;


/** 
 * Common part of the host test frame and the card test frame. The
 * host test frame is that test frame that runs one applet and the
 * host driver together on the standard Java Virtual Machine for easy
 * applet debugging. The card test frame runs the host driver to talk
 * to a real applet on a real card or emulator.
 * <P>
 *
 * This class contains the options that are shared between all the
 * test frames and the shared methods that realize the test loops.
 * <P>
 *
 * Static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.41 $
 * @commitdate $Date: 2010-03-12 15:40:20 $ by $Author: tews $
 * @environment host
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#RSA_CARD_PROTOCOL_STUBS">RSA_CARD_PROTOCOL_STUBS<a>,
 *   <a href="../../../overview-summary.html#RSA_DEBUG_PROTOCOL_STUBS">RSA_DEBUG_PROTOCOL_STUBS<a>
 */
public class Testframe {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected Testframe() {}


    /**
     * 
     * Host driver instance for the regular protocols. The host driver
     * instances (together with {@link #debug_card}) are the only
     * necessary thing to run the protocols. But behind them a little
     * object hierarchy is hiding: the stubs {@link
     * RSA_card_protocol_stubs} or {@link
     * RSA_card_protocol_test_stubs} and {@link RSA_card_debug_stubs}
     * or {@link RSA_card_debug_test_stubs}, the protocols array
     * {@link Front_protocols}, the protocol descriptions {@link
     * RSA_card_protocol_description} and {@link
     * RSA_card_debug_description}, and the service instance for
     * registered protocols {@link ds.ov2.util.Registered_protocols}.
     * In the host test frames the complete applet code hides behind
     * this stubs instance. 
     * <P>
     *
     * The stubs are initialized in {@link #initialize_stubs}. This
     * should be done whenever the applet is installed.
     */
    private static RSA_host_card host_card = null;

    /**
     * 
     * Stubs instance for the debug protocols. See {@link #host_card}
     * for more information.
     */
    private static RSA_card_debug debug_card = null;




    //########################################################################
    // Commandline parsing
    //

    /**
     * 
     * If true reset the applet before a new personalization instead
     * of resinstalling it.
     */
    private static Reference<Boolean> use_applet_reset =
        new Reference<Boolean>(false);


    /**
     * 
     * Option array with the shared options.
     */
    public static final Option[] shared_options = new Option[]{
        new Bool_option("-test-size", Test_state.test_increase_size,
                        "test loop with increasing base size"),
        new Bool_option("-test-const", Test_state.test_const_size,
                        "test fixed size"),
        new Bool_int_option("-base-length",
                            State.base_length_set, State.base_length,
                            "n", "set base size"),
        new Bool_int_option("-exp-length",
                            State.exponent_length_set, State.exponent_length,
                            "n", "set exponent size"),
        new Int_option("-attr", State.attribute_number,
                       "n", "set number of attributes [" +
                       State.attribute_number.ref + "]"),
        new Int_option("-ptls-rounds", Test_state.ptls_param_rounds, "n",
                       "number of ptls key generation rounds [" +
                       Test_state.ptls_param_rounds.ref + "]"),
        new Int_option("-card-init-rounds", Test_state.card_init_rounds, "n",
                       "number of card init generation rounds [" +
                       Test_state.card_init_rounds.ref + "]"),
        new Int_option("-resign-rounds", Test_state.resign_rounds, "n",
                       "number of resigning rounds [" +
                       Test_state.resign_rounds.ref + "]"),
        new Int_option("-proof-rounds", Test_state.proof_rounds, "n",
                       "number of proof rounds [" +
                       Test_state.proof_rounds.ref + "]"),
        new Bool_option("-reset", use_applet_reset,
                        "reset the applet instead of reinstalling it"),
        new Bool_option("-ignore", Test_state.ignore_all_kinds_of_problems,
                        "ignore all wrong certificates/proofs"),
        new Value_option<Calibration>("-768",
                                      State.security_calibration,
                                      Security_parameter.rsa_768_break_2009,
                               "use the RSA 768 factorization for calibration"),
        new BigInt_input_option("-i", Test_state.fix_inputs, "n",
                                "provide decimal n as input for the test"),
        new BigInt_hex_input_option("-hex", Test_state.fix_inputs, "n",
                                    "provide hex n as input for the test"),
        new Value_option<Integer>("-d", Test_state.verbosity, 5,
                                  "be more verbose"),
        new Value_option<Integer>("-dd", Test_state.verbosity, 10,
                                  "output many debug/progress messages"),
        new Value_option<Integer>("-ddd", Test_state.verbosity, 15,
                                  "output anything which might be useful")
    };


    /**
     * 
     * Parse the command line for the host and the card test frames.
     * The specific options of the host or card test frame and the
     * application name to use are passed into this method. Calls
     * {@link State#update_exponent_length} at the end in case the
     * base or exponent length has been changed via options.
     * 
     * @param args command line as received in main
     * @param additional_options additional options to prepend to the
     * shared options
     * @param short_application_name name to print in case of a
     * command line parsing error
     */
    public static void parse_commandline(String args[],
                                         Option[] additional_options,
                                         String short_application_name) {
        Option[] all_options = new Option[additional_options.length +
                                          shared_options.length];
        System.arraycopy(additional_options, 0,
                         all_options, 0,
                         additional_options.length);
        System.arraycopy(shared_options, 0,
                         all_options, additional_options.length,
                         shared_options.length);
        new Parse_commandline(all_options, short_application_name)
            .parse(args);

        State.update_exponent_length();
    }


    //########################################################################
    // general RSA applet test code
    //


    /**
     * 
     * (Re-)Install the applet and select it.
     * 
     * @param card_channel channel to the card
     * @param out debug and progress channel
     * @throws CardException on communication errors
     * @throws IOException if the applet cap file is not accessible
     */
    public static void install_applet_and_select(CardChannel card_channel,
                                                 PrintWriter out)
        throws CardException, IOException
    {
        Card_services.reinstall_applet
            (card_channel, out,
             Test_state.verbosity.ref >= 15, // print full gpm APDU's ?
             Test_state.applet_type.ref,
             Test_state.applet_type.ref.applet_file());
        Misc_host.gc_sleep(Test_state.sleep_time);
        Card_terminal.open_applet(card_channel,
                                  Test_state.applet_type.ref.applet_aid(),
                                  out, false);
        Misc_host.gc_sleep(Test_state.sleep_time);
    }
    /**
     * 
     * (Re-)Initialize the host drivers for the regular and debug
     * protocols {@link #host_card} and {@link #debug_card}. Together
     * with these host drivers the whole protocol layer that hides
     * behind them is (re-)initialized. In the host test frames even
     * the complete applets are reinitialized.
     * <P>
     *
     * Therefore this is somewhat the host-side counterpart for applet
     * installation. When the applet is (re-)installed, this method should
     * be called to (re-)install the appropriate host data structures.
     * 
     * @param out output channel for debug and progress messages
     */
    public static void initialize_stubs(PrintWriter out) {
        // Initialize the meta-protocol class.
        Front_protocols front_protocols = new Front_protocols();
        front_protocols.init_protocols();

        // Get the protocol description data that is used to 
        // communicate to ccard.
        RSA_card_protocol_description p = front_protocols.rsa_description;
        RSA_card_debug_description pd = front_protocols.rsa_debug;

        // Initialize the host stub code.
        PrintWriter stub_out = Test_state.verbosity.ref >= 15 ? out : null;
        RSA_card_protocol_stubs stubs =
            new RSA_card_protocol_stubs(p, stub_out, false);

        RSA_card_debug_stubs dstubs =
            new RSA_card_debug_stubs(pd, stub_out, false);

        // Make a host-side card instance.
        host_card = new RSA_host_card(front_protocols,
                                      stubs, out,
                                      Test_state.verbosity.ref);

        // Make the debug object.
        debug_card = new RSA_card_debug(front_protocols,
                                        dstubs, out,
                                        Test_state.verbosity.ref);
    }


    /**
     * 
     * Simulate the life of an OV-chip applet. Install the applet and
     * the host driver instances for it or resets the applet.
     * Personalize the applet then (download PTLS parameters and
     * attributes) and run the resign and gate (proof) protocol a few
     * times. The number of resign and gate rounds is determined by
     * some global variables that can be changed via command line
     * options, see {@link Card_testframe}. 
     * <P>
     *
     * In the card test frame the applet to install can be chosen via
     * options, see {@link Card_testframe}. In the host test frame the
     * applet type is fixed. 
     * <P>
     *
     * This method uses the {@code card_channel} argument to determine
     * whether it runs inside a host test frame. Applet deletion and
     * installation is only done if {@code card_channel} is nonnull.
     * 
     * @param card_channel channel to the card or null for a host test frame
     * @param out output channel for debug and progress messages
     * @param err output channel for serious problems
     * @param ptls_params PTLS parameters instance
     * @throws CardException on communication errors
     * @throws IOException if the applet cap file is not accessible
     */
    public static void test_one_card_issue_ex(CardChannel card_channel,
                                              PrintWriter out,
                                              PrintWriter err,
                                              PTLS_rsa_parameters ptls_params)
        throws CardException, IOException
    {
        // Avoid double installation in the first loop round.
        boolean just_installed = false;
        if(host_card == null) {
            initialize_stubs(out);
            just_installed = true;
            if(card_channel != null)
                install_applet_and_select(card_channel, out);
        }

        if(use_applet_reset.ref) {
            debug_card.reset_applet(card_channel);
        }
        else {
            // Avoid double installation.
            if(!just_installed) {
                // Reinitialize the applet and the host driver.
                initialize_stubs(out);

                // If we are using a real card, ie. if card_channel != null,
                // we have to reload the applet.
                if(card_channel != null) {
                    Misc_host.gc_sleep(Test_state.sleep_time);
                    install_applet_and_select(card_channel, out);
                }
            }
        }


        if(Test_state.verbosity.ref >= 0)
            debug_card.print_applet_memory(out, card_channel);

        out.println("### Start issue procedure");
        boolean issue_ok =
            host_card.issue_card(ptls_params, card_channel, null,
                             Test_state.applet_type.ref);
        Misc_host.gc_sleep(Test_state.sleep_time);

        // Finish host side initialization in the debug part.
        debug_card.host_side_init();

        if(!issue_ok && !Test_state.ignore_all_kinds_of_problems.ref) {
            err.println("CARD ISSUE FAILURE!!!!!!!!!");
            debug_card.get_and_print_status(out, 0, card_channel);
            System.exit(1);
        }

        if(Test_state.verbosity.ref >= 0)
            debug_card.print_applet_memory(out, card_channel);

        out.format("### Start %d resign rounds\n", Test_state.resign_rounds.ref);

        for(int i = 0; i < Test_state.resign_rounds.ref; i++) {

            debug_card.get_and_print_status(out, 10, card_channel);
            Misc_host.gc_sleep(Test_state.sleep_time);

            // if(out != null) 
            //  out.format("resign round %s\n", i);

            BigInteger[] updates =
                debug_card.invent_attribute_updates(ptls_params, card_channel);
            Misc_host.gc_sleep(Test_state.sleep_time);

            if(!host_card.resign(updates,
                             !Test_state.ignore_all_kinds_of_problems.ref,
                             ptls_params, card_channel, null)
               && !Test_state.ignore_all_kinds_of_problems.ref)
            {
                err.println("SIGNATURE FAILURE!!!!!!!!!");
                throw new RuntimeException("signature failure");
            }
            Misc_host.gc_sleep(Test_state.sleep_time);

            for(int j = 0; j < Test_state.proof_rounds.ref; j++) {
                // if(out != null) 
                //     out.format("gate round %s\n", j);

                if(!host_card.check_gate(ptls_params, card_channel, null,
                                     !Test_state
                                     .ignore_all_kinds_of_problems.ref)
                   && !Test_state.ignore_all_kinds_of_problems.ref)
                    {
                        err.println("PROOF PROTOCOL FAILURE!!!!!!!!");
                        throw new RuntimeException("proof failure");
                    }
                Misc_host.gc_sleep(Test_state.sleep_time);
            }
        }
    }


    /**
     * 
     * Exception wrapper for {@link #test_one_card_issue_ex
     * test_one_card_issue_ex}. Simulates the life of an OV-chip
     * applet and catches the exception that is raised when the card
     * guesses one of the secret factors of the RSA modulus when
     * generating a new random blinding. This can actually be easily
     * observed for RSA key sizes below 16 bits. If the card guesses
     * one of the factors some response is not invertible any more and
     * {@link BigInteger#modInverse} throws and {@link
     * ArithmeticException}. 
     * <P>
     *
     * All other exceptions are not touched.
     * 
     * @param card_channel channel to the card or null for a host test frame
     * @param out output channel for debug and progress messages
     * @param ptls_params PTLS parameters instance
     * @throws CardException on communication errors
     * @throws IOException if the applet cap file is not accessible
     */
    public static void test_one_card_issue(CardChannel card_channel,
                                           PrintWriter out,
                                           PTLS_rsa_parameters ptls_params)
        throws CardException, IOException
    {
        PrintWriter err = out;
        // This is not really serious. Actually if out == null then
        // test_one_card_issue_ex will break. But maybe this gets
        // fixed at some stage and then one can use out == null...
        if(err == null)
            err = new PrintWriter(System.err, true);

        try {
            test_one_card_issue_ex(card_channel, out, err, ptls_params);
        }
        catch(RuntimeException e) {
            err.println("Exception handling test_one_card_issue:");
            e.printStackTrace();
            if(e instanceof ArithmeticException &&
               e.getMessage().equals("BigInteger not invertible."))
            {
                BigInteger b =
                    debug_card.get_status(card_channel).data_current_blinding;
                BigInteger gcd = b.gcd(ptls_params.n);
                if(gcd.compareTo(BigInteger.ONE) != 0) {
                    err.format("Card guessed factor %s of modulus %s\n",
                               gcd, ptls_params.n);
                    System.exit(0);
                }
            }
            err.format("Exception %s\n", e);
            debug_card.get_and_print_status(err, 0, card_channel);
            ptls_params.print_all(Test_state.applet_type.ref, err);
            throw e;
        }
    }


    /**
     * 
     * Simulates the life of several applets for one newly generated
     * set of PTLS parameters. The number of applets and the number of
     * proof/resign rounds to run on each of them is determined by
     * some global variables that can be changed via command line
     * options, see {@link Card_testframe}. 
     *
     * @param card_channel channal to the card or null for the host
     * test frame
     * @param out output channel for progress and debug messages
     * @param attribute_number number of attributes to use
     * @param base_length length of the RSA key and the bases
     * @param exponent_length length of the attributes and the public
     * RSA exponent
     * @throws CardException on communication errors
     * @throws IOException if the applet cap file is not accessible
     * @throws NoSuchAlgorithmException if no provider for RSA key
     * generation can be found
     */

    // Generate PTLS parameters and do then card_init_rounds many 
    // live-cycle simulations.
    public static void test_one_ptls_round(CardChannel card_channel,
                                           PrintWriter out,
                                           int attribute_number,
                                           int base_length,
                                           int exponent_length)
        throws NoSuchAlgorithmException, CardException, IOException
    {
        // Generate private/public keys and random bases.
        PTLS_rsa_parameters ptls_params =
            new PTLS_rsa_parameters(attribute_number,
                                    out, Test_state.verbosity.ref);
        ptls_params.generate(base_length,
                             exponent_length,
                             Test_state.applet_type.ref);

        for(int i = 0; i < Test_state.card_init_rounds.ref; i++) {
            if(out != null)
                out.format("Init round %d\n", i);
            test_one_card_issue(card_channel, out, ptls_params);
        }

        return;
    }


    /**
     * 
     * Simulates the applet life for several sets of PTLS parameters
     * for one base and exponent length. The numbers of what is
     * simulated how often is determined by some global variables that
     * can be changed via the command line, see {@link
     * Card_testframe}.
     * 
     * @param card_channel channal to the card or null for the host
     * test frame
     * @param out output channel for progress and debug messages
     * @param attribute_number number of attributes to use
     * @param base_length length of the RSA key and the bases
     * @param exponent_length length of the attributes and the public
     * RSA exponent
     * @throws CardException on communication errors
     * @throws IOException if the applet cap file is not accessible
     * @throws NoSuchAlgorithmException if no provider for RSA key
     * generation can be found
     */
    public static void test_init_resign_proof(CardChannel card_channel,
                                              PrintWriter out,
                                              int attribute_number,
                                              int base_length,
                                              int exponent_length)
        throws NoSuchAlgorithmException, CardException, IOException
    {
        long start = System.currentTimeMillis();

        out.println("##################################################" +
                    "############################");
        out.println("### TEST INIT RESIGN PARAMETERS:");
        out.format("###    base   %d \t%d bytes (without montgomery digits)\n",
                   base_length, (base_length + 7) / 8);
        out.format("###    exp    %d \t%d bytes\n",
                   exponent_length, (exponent_length + 7) / 8);
        out.format("###    attr   %d\n", attribute_number);
        out.format("###    started at %s\n",
                   new SimpleDateFormat("d MMMM yyyy HH:mm:ss z")
                   .format(new Date()));

        for(int i = 0; i < Test_state.ptls_param_rounds.ref; i++) {
            test_one_ptls_round(card_channel, out,
                                attribute_number,
                                base_length, exponent_length);
        }


        SimpleDateFormat diff_format = new SimpleDateFormat("HH:mm:ss");
        diff_format.setTimeZone(TimeZone.getTimeZone("GMT"));
        out.format("### TEST finished at %s (duration %s)\n",
                   new SimpleDateFormat("d MMMM yyyy HH:mm:ss z")
                   .format(new Date()),
                   diff_format.format(new Date(System.currentTimeMillis() -
                                               start)));
        out.println("##################################################" +
                    "############################");
    }


    /**
     * 
     * Runs the applet life simulation for all base sizes from {@code
     * start_size} up to 2048.
     * 
     * @param card_channel channal to the card or null for the host
     * test frame
     * @param out output channel for progress and debug messages
     * @param attribute_number number of attributes to use
     * @param start_size base start size in bits
     * @throws CardException on communication errors
     * @throws IOException if the applet cap file is not accessible
     * @throws NoSuchAlgorithmException if no provider for RSA key
     * generation can be found
     */
    public static void test_size_loop(CardChannel card_channel,
                                      PrintWriter out,
                                      int attribute_number,
                                      int start_size)
        throws NoSuchAlgorithmException, CardException, IOException
    {
        int end_size = 2048;
        int size_incr = 32;

        for(int size = start_size; size <= end_size; size += size_incr)
            test_init_resign_proof(card_channel, out,
                                   attribute_number,
                                   size,
                                   State.make_exponent_length(size));
    }
}
