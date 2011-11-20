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
// Created 26.8.08 by Hendrik
//
// Host driver for the test applet
//
// $Id: Test_host.java,v 1.56 2010-02-18 11:42:49 tews Exp $

package ds.ov2.test;

import java.util.Date;
import java.util.Vector;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.nio.charset.Charset;
import java.math.BigInteger;

import javax.smartcardio.CardException;
import javax.smartcardio.CardChannel;

import cardservices.GPUtil;
import cardservices.AID;
import cardservices.AIDRegistryEntry;
import cardservices.CapFile;
import cardservices.GlobalPlatformService;

import ds.ov2.util.Misc;
import ds.ov2.util.Misc_host;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Convert_serializable;
import ds.ov2.util.APDU_short;
import ds.ov2.util.APDU_byte_array;
import ds.ov2.util.Card_terminal;
import ds.ov2.util.Card_terminal.Terminal_type;
import ds.ov2.util.Card_terminal.Applet_selection_exception;
import ds.ov2.util.PrintWriter_APDUListener;
import ds.ov2.util.Reference;
import ds.ov2.util.Commandline;
import ds.ov2.util.Option;
import ds.ov2.util.Bool_option;
import ds.ov2.util.Int_option;
import ds.ov2.util.Bool_int_option;
import ds.ov2.util.String_option;
import ds.ov2.util.BigInt_input_option;
import ds.ov2.util.BigInt_hex_input_option;
import ds.ov2.util.Value_option;
import ds.ov2.util.Parse_commandline;
import ds.ov2.bignat.Resize;
import ds.ov2.test.Misc_protocols_stubs.Status_result;
import ds.ov2.test.Exponent_perf_host.Vector_exponent_variant;


/** 
 * Main class of the host driver for the test applet that gives access
 * to all the 
 * functionality of the test applet. This host driver automatically
 * (re-)installs the applet if it is not found or not up-to-date. The
 * driver can talk to real cards or to the jcop emulator, 
 * see the options {@code -c} and {@code -r}.
 * (It can also talk to the SUN emulators cref and jcwde, however,
 * applet installation fails there because of the absense of the RSA
 * cipher.)
 * <P>
 *
 * The test host driver is a non-interactive terminal program that can
 * run several tests and performance measurements. What test and
 * measurments are run depends on the command line options. As default
 * nothing is run. The measurements results are printed in gnuplot
 * compatible way. For examples on how to produce nice charts out of
 * the performance data, see the gnuplot scripts in the subdirectory
 * test/Measurments. 
 * <P>
 *
 * Some parts of the command line that is necessary to start this
 * program depend on the configuration. Therefore the makefile
 * generates the <KBD>test_host</KBD> shell script for a convenient
 * invocation.
 * <P>
 *
 * <A NAME="test-host-options"></A>
 * There are two different kinds of command line options for this
 * program: The task selection options that enable one of the checks
 * or measurements and the configuration options that change some more
 * or less common behavior (for instance to talk to a card or an
 * emulator). The complete list of options is printed for the options
 * {@code -h}, {@code -help} and {@code --help}.
 * <P>
 *
 * <H3>Checks and Measurments</H3>
 *
 * The *-check options enable checks while the *-perf options enable
 * performance measurements. With the exception of {@code -data-perf}
 * all measurements do also check the results of the card and do
 * immediately stop the program on any error encountered.
 * <P>
 *
 * The checks perform their computations with numbers of the long
 * (base) size, which can be set with {@code -size}. Except for {@code
 * -data-perf} all performance measurements measure the time for
 * different sizes. That is, the measurements run in a loop with
 * increasing sizes. Except {@code -data-perf} all measurements
 * recognize the {@code -start-size} option to set the starting size. 
 * <P>
 *
 * 
 * The possible tests and measurements and the corresponding task
 * selection options are as follows:
 * <DL>
 * <DT><STRONG>-ping</STRONG>
 * <DD>Run the ping protocol: Send an empty APDU to the applet.
 * <DT><STRONG>-data-check</STRONG>
 * <DD>Checks the OV-chip protocol layer by running protocols with
 * various argument and result sizes. The bytes of the APDU's have
 * successive values from 0 to 255 to perform integrity checks.
 * <DT><STRONG>-data-perf</STRONG>
 * <DD>Performance measurement of the communication time, including
 * the OV-chip protocol overhead. Performs only transmission time
 * measurements, the payload of the APDU's is not checked for
 * integrity. 
 * <DT><STRONG>-mont-mult-check</STRONG>
 * <DD>Check Montgomery multiplication and demongomerization.
 * <DT><STRONG>-mont-mult-perf</STRONG>
 * <DD>Measure Montgomery multiplication and demongomerization
 * performance.
 * <DT><STRONG>-div-check</STRONG>
 * <DD>Check division/remainder.
 * <DT><STRONG>-div-perf</STRONG>
 * <DD>Measure Divison/remainder performance.
 * <DT><STRONG>-vec-exp-check</STRONG>
 * <DD>Check multi-exponent computation (simultaneous squaring version).
 * <DT><STRONG>-vec-exp-perf</STRONG>
 * <DD>Measure multi-exponent computation (simultaneous squaring version).
 * <DT><STRONG>-rsa-vec-exp-check</STRONG>
 * <DD>Check RSA multi-exponent computation.
 * <DT><STRONG>-rsa-vec-exp-perf</STRONG>
 * <DD>Measure RSA multi-exponent computation.
 * <DT><STRONG>-squared-vec-exp-check</STRONG>
 * <DD>Check squared or square 4 RSA multi-exponent computation.
 * <DT><STRONG>-squared-vec-exp-perf</STRONG>
 * <DD>Measure squared or squared 4 RSA multi-exponent computation.
 * <DT><STRONG>-rsa-exp-check</STRONG>
 * <DD>Check single RSA exponent computation.
 * <DT><STRONG>-rsa-exp-perf</STRONG>
 * <DD>Measure single RSA exponent computation.
 * <DT><STRONG>-squared-mult-check</STRONG>
 * <DD>Check Bignat.squared_rsa_mult.
 * <DT><STRONG>-squared-mult-perf</STRONG>
 * <DD>Measure Bignat.squared_rsa_mult.
 * <DT><STRONG>-short-squared-mult-perf</STRONG>
 * <DD>Measure Bignat.short_squared_rsa_mult.
 * <DT><STRONG>-squared-mult-4-check</STRONG>
 * <DD>Check Bignat.squared_rsa_mult_4.
 * <DT><STRONG>-squared-mult-4-perf</STRONG>
 * <DD>Measure Bignat.squared_rsa_mult_4.
 * <DT><STRONG>-short-square-mult-4-perf</STRONG>
 * <DD>Measure Bignat.short_squared_rsa_mult_4.
 * <DT><STRONG>-add-perf</STRONG>
 * <DD>Measure Bignat.add.
 * <DT><STRONG>-normal-mult-perf</STRONG>
 * <DD>Measure Bignat.mult.
 * <DT><STRONG>-subtract-perf</STRONG>
 * <DD>Measure Bignat.subtract.
 * <DT><STRONG>-list-readers</STRONG>
 * <DD>List all connected PC/SC readers and exit immediately
 * afterwards.
 * <DT><STRONG>-all-checks</STRONG>
 * <DD>Enables all checks: ping, data transmission,
 * Montgomery multiplication/demongomerization, divison,
 * multi-exponent in both 
 * version, RSA single exponent, squared multiplication and squared 4
 * multiplication. If a size
 * has been specified with 
 * {@code -size} that size is used for all check. Otherwise the size
 * is adjusted so that the check do not take too long.
 * <DT><STRONG>-check-all</STRONG>
 * <DD>Synonym for -all-checks
 * </DL>
 *
 * <H3>Configuration Options</H3>
 *
 *
 * The configuration options change aspects of one or several checks
 * and measurements. 
 * <DL>
 * <DT><STRONG>-size n</STRONG>
 * <DD>Set the size of the long (base) bignats to n bytes. Adjust the
 * short (exponent) size according to Lenstras key length estimation
 * (see {@link ds.ov2.util.Security_parameter}) if no exponent size
 * has been set before. All checks perform their computaitions with
 * the long bignat size. Many tests and measurements need to take care
 * of <a 
 * href="../bignat/package-summary.html#montgomery_factor">Montgomery
 * digits.</a> This means that the numbers that are used in these
 * computations are two digits (i.e., two bytes for the byte/short
 * configuration of the bignat library) shorter than what specified
 * with this option. 
 * <DT><STRONG>-exp-size n</STRONG>
 * <DD>Set the size of the short (exponent) bignats to n bits. 
 * <DT><STRONG>-fixed-exponent e</STRONG>
 * <DD>Use exponent e. Currently only supported by the RSA exponent
 * checks (-rsa-exp-check and -rsa-exp-perf). The exponent size must
 * be set independently.
 * <DT><STRONG>-start-size n</STRONG>
 * <DD>For performance measurements: Set the starting size of the
 * performance loop.
 * <DT><STRONG>-div-length n</STRONG>
 * <DD>Set the divisor length to n percent of the divident length.
 * Only used for the division check and measurement. To measure
 * division with dividents twice as big as divisors use {@code
 * -div-length 50}
 * <DT><STRONG>-vec-len n</STRONG>
 * <DD>Set the length of the vectors used in the multi-exponent checks
 * and measurements to n.
 * <DT><STRONG>-pre-base-fac n</STRONG>
 * <DD>Set the number of bases for which all their factors are
 * precomputed. Only used for the simultaneous squaring tasks. There
 * precomputed factor tables of the bases make a big performance
 * difference. 
 * <DT><STRONG>-keep-mod</STRONG>
 * <DD>Run a number of consequtive tests/measurements with the same
 * modulus. This simulates the OV-chip application case where the
 * modulus stays constant. Should be beneficial for the RSA
 * multi-exponent tasks, because there modulus must installed in the
 * RSA key. 
 * <DT><STRONG>-rounds n</STRONG>
 * <DD>Run n rounds of every enabled check. For performance
 * measurements run n rounds for every measured size.
 * <DT><STRONG>-i n</STRONG>
 * <DD>Supply the fixed argument n, which must be in decimal
 * BigInteger string. If enough fixed arguments have been supplied,
 * all tests and measurements take their numbers form the fixed
 * arguments. 
 * <DT><STRONG>-hex n</STRONG>
 * <DD>Same as {@code -i}, except that n is a hexadicimal number
 * string, which can contain dots to separate digit groups.
 * <DT><STRONG>-jcop</STRONG>
 * <DD>Connect to the jcop emulator. This is the default if the
 * necessary libraries are in the class path at runtime.
 * <DT><STRONG>-sun</STRONG>
 * <DD>Connect to a SUN emulator. Pretty much useless option, because
 * applet installation does always fail on these emulators because of
 * the lacking RSA support.
 * <DT><STRONG>-c</STRONG>
 * <DD>Connect to the first PC/SC terminal that is reported to us.
 * This is the default if not all necessary librarie for the jcop
 * emulator are present.
 * <DT><STRONG>-r n</STRONG>
 * <DD>Connect to the PC/SC reader with index n. To get an idea about
 * the choice, use {@code -list-reader}.
 * <DT><STRONG>-jcop-port p</STRONG>
 * <DD>Set the port number for the jcop emulator (default 8015).
 * <DT><STRONG>-list-applets</STRONG>
 * <DD>Output the list of applets that are present on the card during
 * applet installation.
 * <DT><STRONG>-reinstall</STRONG>
 * <DD>Force applet reinstallation.
 * <DT><STRONG>-keep-applet</STRONG>
 * <DD>Keep installed applet, even if its timestamp is different from
 * the current cap file.
 * <DT><STRONG>-mem-size</STRONG>
 * <DD>Query the amount of available memory on the card.
 * <DT><STRONG>-check-corner-cases</STRONG>
 * <DD>Check programmed corner cases. Currently only supported by
 * squared multiplication, short squared multiplication and squared 4
 * multiplication.
 * <DT><STRONG>-h, -help, --help</STRONG>
 * <DD>Display online help for all available options.
 * <DT><STRONG>-d, -dd, -ddd</STRONG>
 * <DD>Set verbosity level to 5, 10, and 15, respectively. Higher
 * numbers mean more output. See {@link State#verbosity} for
 * explanations about these levels.
 * <DT><STRONG>-apdutool</STRONG>
 * <DD>If the verbosity is high enough to print APSU's then print also
 * apdutool script lines.
 * </DL>
 *
 *
 * <H3>This class</H3>
 *
 * This class contains the command line parsing, the logic to invoke
 * the enables tests and measurements and applet installation. The
 * methods that implement the tests and measurements are in different
 * classes. 
 * <P>
 *
 * Static class.
 *
 * 
 * @author Hendrik Tews
 * @version $Revision: 1.56 $
 * @commitdate $Date: 2010-02-18 11:42:49 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Test_host {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected Test_host() {}


    /**
     * 
     * Short application name. Printed on fatal errors.
     */
    public static final String short_application_name = "Test_host";

    /**
     * 
     * Long application name. Printed in the usage message.
     */
    public static final String long_application_name =
        "Test applet host driver";

    //########################################################################
    // configuration section
    //

    // some more general state is in State.java

    /**
     * 
     * Number of rounds to run all checks for the {@code -all-checks} option.
     */
    private static final int all_checks_rounds = 5;


    /** Controls running all checks. Set by option {@code
     * -all-checks}. */
    private static Reference<Boolean> run_all_checks = 
        new Reference<Boolean>(false);

    /** Controls running ping. Set by option {@code -ping}. */
    private static Reference<Boolean> run_ping = 
        new Reference<Boolean>(false);

    /** Controls the data transfer check. Set by option {@code
     * -data-check}. */
    private static Reference<Boolean> run_data_check = 
        new Reference<Boolean>(false);

    /** Controls the data transfer measurement. 
        Set by option {@code -data-perf}. */
    private static Reference<Boolean> run_data_perf = 
        new Reference<Boolean>(false);

    /** Controls the Montgomery multiplication check. Set by option
     * {@code -mont-mult-check}. */
    private static Reference<Boolean> run_mont_mult_check = 
        new Reference<Boolean>(false);

    /** Controls the Montgomery multiplication measurement. Set by
     * option {@code -mont-mult-perf}. */
    private static Reference<Boolean> run_mont_mult_perf = 
        new Reference<Boolean>(false);

    /** Controls the division check. Set by option {@code -div}. */
    private static Reference<Boolean> run_div_check = 
        new Reference<Boolean>(false);

    /** Controls the division performance. Set by option {@code
     * -div-perf}. */
    private static Reference<Boolean> run_div_perf = 
        new Reference<Boolean>(false);

    /** Controls the vector exponent check. Set by option {@code
     * -vec-exp-check}. */
    private static Reference<Boolean> run_vec_exp_check = 
        new Reference<Boolean>(false);

    /** Controls the vector exponent performance. Set by option {@code
     * -vec-exp-perf}. */
    private static Reference<Boolean> run_vec_exp_perf = 
        new Reference<Boolean>(false);

    /** Controls the RSA vector exponent check. Set by option {@code
     * -rsa-vec-exp-check}. */
    private static Reference<Boolean> run_rsa_vec_exp_check = 
        new Reference<Boolean>(false);

    /** Controls the RSA vector exponent performance. Set by option
     * {@code -rsa-vec-exp-perf}. */
    private static Reference<Boolean> run_rsa_vec_exp_perf = 
        new Reference<Boolean>(false);

    /** Controls the squared/squared 4 RSA vector exponent check. Set
     * by option {@code -squared-vec-exp-check}. */
    private static Reference<Boolean> run_squared_rsa_vec_exp_check = 
        new Reference<Boolean>(false);

    /** Controls the squared/squared 4 RSA vector exponent
     * performance. Set by option {@code -squared-vec-exp-perf}. */
    private static Reference<Boolean> run_squared_rsa_vec_exp_perf = 
        new Reference<Boolean>(false);

    /** Controls the RSA exponent check. Set by option {@code
     * -rsa-exp-check}. */
    private static Reference<Boolean> run_rsa_exp_check = 
        new Reference<Boolean>(false);

    /** Controls the RSA exponent performance. Set by option {@code
     * -rsa-exp-perf}. */
    private static Reference<Boolean> run_rsa_exp_perf = 
        new Reference<Boolean>(false);

    /** Controls the squared multiplication check. Set by option
     * {@code -squared-mult-check}. */
    private static Reference<Boolean> run_squared_mult_check = 
        new Reference<Boolean>(false);

    /** Controls the squared multiplication performance. Set by option
     * {@code -squared-mult-perf}. */
    private static Reference<Boolean> run_squared_mult_perf = 
        new Reference<Boolean>(false);

    /** Controls the short squared multiplication performance
     * measurements. Set by option {@code -short-squared-mult-perf}. */
    private static Reference<Boolean> run_short_squared_mult_perf = 
        new Reference<Boolean>(false);

    /** Controls the squared 4 multiplication check. Set by option
     * {@code -squared-mult-4-check}. */
    private static Reference<Boolean> run_squared_mult_4_check = 
        new Reference<Boolean>(false);

    /** Controls the squared 4 multiplication performance. Set by option
     * {@code -squared-mult-4-perf}. */
    private static Reference<Boolean> run_squared_mult_4_perf = 
        new Reference<Boolean>(false);

    /** Controls the short squared 4 multiplication performance
     * measurements. Set by option {@code -short-square-4-mult-perf}. */
    private static Reference<Boolean> run_short_square_4_mult_perf = 
        new Reference<Boolean>(false);

    /** Controls addition performance. Set by option {@code
     * -add-perf}. */
    private static Reference<Boolean> run_add_perf = 
        new Reference<Boolean>(false);

    /** Controls normal multiplication performance. Set by option
     * {@code -normal-mult-perf}. */
    private static Reference<Boolean> run_normal_mult_perf =
        new Reference<Boolean>(false);

    /** Controls subtraction perfomrance Set by option {@code
     * -subtract-perf}. */
    private static Reference<Boolean> run_subtract_perf = 
        new Reference<Boolean>(false);

    /** Crontrols whether to query the memory size. Set by option
     * {@code -mem-size}. */
    private static Reference<Boolean> run_mem_size = 
        new Reference<Boolean>(false);

    /** Controls whether to list all connected readers. Set by option
     * {@code -list-readers}. */
    private static Reference<Boolean> run_list_readers =
        new Reference<Boolean>(false);


    /** Controls applet listing. Set by option {@code -list-applets}. */
    private static Reference<Boolean> run_list_applets = 
        new Reference<Boolean>(false);


    /** Controls applet (re-)installation. Set by option {@code
     * -reinstall}. */
    private static Reference<Boolean> reinstall_applet = 
        new Reference<Boolean>(false);


    /** Controls applet reinstallation. Set by option {@code
     * -keep-applet}. */
    private static Reference<Boolean> keep_applet = 
        new Reference<Boolean>(false);

    /**
     * 
     * US-ASCII charset for string-to-applet-ID conversion.
     */
    private static final Charset us_ascii = Charset.forName("US-ASCII");

    /**
     * 
     * Default terminal type. Initialized to {@link
     * Terminal_type#JCOP_EMULATOR} if the necessary libraries for
     * connecting to the jcop emulator are available. Initialized to
     * {@link Terminal_type#PCSC_TERMINAL} otherwise.
     */
    private static final Terminal_type terminal_type_default = 
        Card_terminal.jcop_provider_present() ? 
        Terminal_type.JCOP_EMULATOR : Terminal_type.PCSC_TERMINAL;

    /**
     * 
     * Terminal type to connect to. Changed by the options {@code
     * -jcop, -sun, -c} and {@code -r}. 
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
     * The index of the card reader to connect to. Set by the option
     * {@code -r}. 
     */
    private static Reference<Integer> card_reader_number = 
        new Reference<Integer>(0);


    /**
     * 
     * Date formatter used here at various places.
     */
    private static SimpleDateFormat date_format = 
        new SimpleDateFormat("d MMMM yyyy HH:mm:ss z");



    //########################################################################
    //
    // Command line arguments
    //

    /**
     * 
     * Array declaring all recognized command line options.
     */
    public static Option[] options = new Option[] {
        new Bool_option("-all-checks", run_all_checks, 
                        String.format("run %d rounds of all checks", 
                                      all_checks_rounds)),
        new Bool_option("-check-all", run_all_checks,
                        "Synonym for -all-checks"),
        new Bool_option("-ping", run_ping, "run ping"),
        new Bool_option("-data-check", run_data_perf,
                        "run data protocol checks"),
        new Bool_option("-data-perf", run_data_perf,
                        "measure data transmission performance"),
        new Bool_option("-mont-mult-check", run_mont_mult_check,
                        "check montgomery multiplication"),
        new Bool_option("-mont-mult-perf", run_mont_mult_perf,
                        "measure montgomery multiplication performance"),
        new Bool_option("-div-check", run_div_check,
                        "check division/remainder"),
        new Bool_option("-div-perf", run_div_perf,
                        "measure division performance"),
        new Int_option("-div-length", State.divisor_length, "n",
                       "set divisor length to n percent of size"),
        new Bool_option("-vec-exp-check", run_vec_exp_check,
                        "check vector exponent"),
        new Bool_option("-vec-exp-perf", run_vec_exp_perf,
                        "measure vector exponent (simulteneous sqaring)"),
        new Bool_option("-rsa-vec-exp-check", run_rsa_vec_exp_check,
                        "check rsa vector exponent (Montg. mult)"),
        new Bool_option("-rsa-vec-exp-perf", run_rsa_vec_exp_perf,
                        "measure rsa vector exponent (Montg. mult)"),
        new Bool_option("-squared-vec-exp-check", run_squared_rsa_vec_exp_check,
                        #ifdef USE_SQUARED_RSA_MULT_4
                          "check rsa vector exponent (Square 4)"
                        #else
                          "check rsa vector exponent (Squared)"
                        #endif
                        ),
        new Bool_option("-squared-vec-exp-perf", run_squared_rsa_vec_exp_perf,
                        #ifdef USE_SQUARED_RSA_MULT_4
                          "measure rsa vector exponent (Square 4)"
                        #else
                          "measure rsa vector exponent (Squared)"
                        #endif
                        ),
        new Bool_option("-rsa-exp-check", run_rsa_exp_check,
                        "check RSA single exponent"),
        new Bool_option("-rsa-exp-perf", run_rsa_exp_perf,
                        "measure RSA single exponent"),
        new Bool_option("-squared-mult-check", run_squared_mult_check,
                        "check Bignat.squared_rsa_mult"),
        new Bool_option("-squared-mult-perf", run_squared_mult_perf,
                        "measure Bignat.squared_rsa_mult"),
        new Bool_option("-short-squared-mult-perf",  
                        run_short_squared_mult_perf,
                        "measure Bignat.short_squared_rsa_mult"),
        new Bool_option("-squared-mult-4-check", run_squared_mult_4_check,
                        "check Bignat.squared_rsa_mult_4"),
        new Bool_option("-squared-mult-4-perf", run_squared_mult_4_perf,
                        "measure Bignat.squared_rsa_mult_4"),
        new Bool_option("-short-square-4-mult-perf",  
                        run_short_square_4_mult_perf,
                        "measure Bignat.short_squared_rsa_mult"),
        new Bool_option("-add-perf", run_add_perf, "measure addition"),
        new Bool_option("-normal-mult-perf", run_normal_mult_perf,
                        "measure (normal) multiplication"),
        new Bool_option("-subtract-perf", run_subtract_perf,
                        "measure subtraction"),
        new Bool_int_option("-size", State.long_size_set, State.long_size, 
                            "n", "set size(s) for some tests"),
        new Bool_int_option("-exp-size", State.short_bit_size_set, 
                            State.short_bit_size,  
                            "n",
                            "set exponent size (IN BITS!) for some tests"),
        new String_option("-fixed-exponent", State.fixed_exponent,
                          "e", "use exponent e"), 
        new Int_option("-start-size", State.start_size,
                       "n", "set start size for some tests"),
        new Int_option("-vec-len", State.vector_length,
                       "n", "set vector length for some tests"),
        new Int_option("-pre-base-fac", State.pre_computed_base_factors,
                       "n", "set number of precomputed base factors"),
        new Bool_option("-keep-mod", State.keep_modulus, 
                        "use same modulus in all rounds (in selected tests)"),
        new Bool_int_option("-rounds", State.rounds_set, State.rounds,
                            "n", "set rounds for each test"),
        new BigInt_input_option("-i", State.fix_inputs, "n",
                                "provide decimal n as input for the test"),
        new BigInt_hex_input_option("-hex", State.fix_inputs, "n",
                                    "provide hex n as input for the test"),
        new Value_option<Terminal_type>("-jcop", terminal_type,
                                        Terminal_type.JCOP_EMULATOR,
                                        "connect to jcop emulator" + 
                                        (terminal_type_default == 
                                         Terminal_type.JCOP_EMULATOR
                                         ? " [default]" : "")),
        new Value_option<Terminal_type>("-sun", terminal_type,
                                        Terminal_type.SUN_EMULATOR,
                                        "connect to a sun emulator" + 
                                        (terminal_type_default == 
                                         Terminal_type.SUN_EMULATOR
                                         ? " [default]" : "")),
        new Value_option<Terminal_type>("-c", terminal_type,
                                        Terminal_type.PCSC_TERMINAL,
                                        "connect to a real card reader" + 
                                        (terminal_type_default == 
                                         Terminal_type.PCSC_TERMINAL
                                         ? " [default]" : "")),
        new Int_option("-r", card_reader_number, "n",
                       "connect to reader number n (implies -c)") {
            public void matched(Commandline cl) {
                super.matched(cl);
                terminal_type.ref = Terminal_type.PCSC_TERMINAL;
            }
        },
        new Int_option("-jcop-port", jcop_port_number, "p",
                       "use port p for the jcop emulator"),
        new Bool_option("-list-readers", run_list_readers, 
                        "list all connected readers"),
        new Bool_option("-list-applets", run_list_applets,
                        "list applets on the card"),
        new Bool_option("-reinstall", reinstall_applet,
                        "(re-) install applet before starting"),
        new Bool_option("-keep-applet", keep_applet,
                        "keep installed applet, even if outdated"),
        new Bool_option("-mem-size", run_mem_size, "query available memory"),
        new Bool_option("-check-corner-cases", State.check_corner_cases,
                        "check corner cases (if supported)"),
        new Value_option<Integer>("-d", State.verbosity, 5, "be more verbose"),
        new Value_option<Integer>("-dd", State.verbosity, 10, 
                                  "output many debug/progress messages"),
        new Value_option<Integer>("-ddd", State.verbosity, 15, 
                                  "output anything which might be useful"),
        new Bool_option("-apdutool", State.apduscript, "print apdutool lines")
    };


    //########################################################################
    //
    // Methods
    //

    /**
     * 
     * Run all requested tests and performance measurements. Runs all
     * those tests and measurments whose corresponding run_* field is
     * true, that is, which have been enabled via their command line
     * option.
     * 
     * @param args the complete command line, as received in {@link
     * #main main}
     * @param test_protocols instance with all test protocols
     * @param card_channel communicaion channel to the test applet
     * (which must have been selected before)
     */
    public static void run_tests(String[] args, 
                                 Test_protocols test_protocols, 
                                 CardChannel card_channel) 
    {
        if(run_all_checks.ref) {
            run_ping.ref = true;
            run_data_check.ref = true;
            run_mont_mult_check.ref = true;
            run_div_check.ref = true;
            run_squared_mult_check.ref = true;
            run_squared_mult_4_check.ref = true;
            run_vec_exp_check.ref = true;
            run_rsa_vec_exp_check.ref = true;
            run_squared_rsa_vec_exp_check.ref = true;
            run_rsa_exp_check.ref = true;
            if(!State.rounds_set.ref)
                State.rounds.ref = all_checks_rounds;
        }

        // Put some additional info in the output for performance tests.
        if(run_data_perf.ref || run_mont_mult_perf.ref || run_div_perf.ref 
           || run_vec_exp_perf.ref
           || run_rsa_vec_exp_perf.ref || run_squared_rsa_vec_exp_perf.ref
           || run_rsa_exp_perf.ref 
           || run_squared_mult_perf.ref || run_short_squared_mult_perf.ref 
           || run_squared_mult_4_perf.ref || run_short_square_4_mult_perf.ref 
           || run_add_perf.ref || run_subtract_perf.ref 
           || run_normal_mult_perf.ref)
            {
                System.out.println("### Performance test started on " +
                                   date_format.format(new Date()));

                System.out.print("### command ./test_host ");
                for(String arg : args) {
                    System.out.print(arg);
                    System.out.print(" ");
                }

                System.out.println("\n### Performance test constants:");
                System.out.format("###   int rsa_year   : %s\n",
                                  State.rsa_year);
                System.out.format("###   short size     : %s\n", 
                                  State.short_size);
                System.out.format("###   long size      : %s\n", 
                                  State.long_size.ref);
                System.out.format("###   double size    : %s\n", 
                                  State.double_size);
                System.out.format("###   int short_bit_size : %s\n",
                                  State.short_bit_size.ref);
                System.out.format("###   int start_size : %s\n",
                                  State.start_size.ref);
                System.out.format("###   vector length  : %s\n", 
                                  State.vector_length.ref);
                System.out.format("###   pre computed   : %s\n", 
                                  State.pre_computed_base_factors.ref);
                System.out.format("###   divisor length : %s\n", 
                                  State.divisor_length.ref);
                System.out.format("###   keep modulus   : %s\n", 
                                  State.keep_modulus.ref);
                System.out.format("###   rounds         : %s\n", 
                                  State.rounds.ref);
                System.out.format("###   start size     : %s\n", 
                                  State.start_size.ref);
                System.out.format("###   verbosity      : %s\n", 
                                  State.verbosity.ref);
                System.out.format("###   apduscript     : %s\n", 
                                  State.apduscript.ref);
                System.out.format("###   terminal       : %s\n", 
                                  terminal_type.ref);
                System.out.format("###   jcop port      : %d\n",
                                  jcop_port_number.ref);
                System.out.format("###   card reader    : %s\n", 
                                  card_reader_number.ref);
            }

        if(run_mem_size.ref)
            new Misc_protocols_host(test_protocols, card_channel, 
                                    new PrintWriter(System.out))
                .report_mem_size();

        if(run_ping.ref)
            new Misc_protocols_host(test_protocols, card_channel,
                                    new PrintWriter(System.out))
                .run_ping();

        if(run_data_check.ref)
            new Check_data_transmission(test_protocols.data_protocol,
                                        card_channel).run();

        if(run_data_perf.ref)
            new Card_performance(test_protocols,
                                 card_channel).run();

        if(run_mont_mult_check.ref)
            new Performance_mult_host(test_protocols, 
                                      card_channel).run_mult_check();

        if(run_mont_mult_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_mult_perf();

        if(run_div_check.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_div_check();

        if(run_div_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_div_perf();

        if(run_squared_mult_check.ref) {
            int old_long_size = State.long_size.ref;
            if(run_all_checks.ref) {
                if(State.long_size_set.ref) {
                    State.long_size.ref -= State.long_size.ref % 4;
                    if(State.long_size.ref < 64)
                        State.long_size.ref = 64;
                    if(State.long_size.ref > 244)
                        State.long_size.ref = 244;
                }
                else {
                    State.long_size.ref = 244;
                }
            }
            new Performance_mult_host(test_protocols,
                                      card_channel).run_sq_mult_check();
            State.long_size.ref = old_long_size;
        }

        if(run_squared_mult_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_sq_mult_perf();

        if(run_short_squared_mult_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_short_sq_mult_perf();

        if(run_squared_mult_4_check.ref) {
            int old_long_size = State.long_size.ref;
            if(run_all_checks.ref) {
                if(State.long_size_set.ref) {
                    State.long_size.ref -= State.long_size.ref % 4;
                    if(State.long_size.ref < 64)
                        State.long_size.ref = 64;
                    if(State.long_size.ref > 244)
                        State.long_size.ref = 244;
                }
                else {
                    State.long_size.ref = 244;
                }
            }
            new Performance_mult_host(test_protocols,
                                      card_channel).run_sq_mult_4_check();
            State.long_size.ref = old_long_size;
        }

        if(run_squared_mult_4_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_sq_mult_4_perf();

        if(run_short_square_4_mult_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_short_sq_4_mult_perf();

        if(run_vec_exp_check.ref) {
            int old_long_size = State.long_size.ref;
            int old_short_size = State.short_size;
            if(run_all_checks.ref && !State.long_size_set.ref) {
                State.long_size.ref = 8;
                State.update_short_size();
            }
            new Exponent_perf_host(test_protocols, card_channel)
                .run_vector_exp_check(Vector_exponent_variant.PURE_JAVA);
            State.long_size.ref = old_long_size;
            State.short_size = old_short_size;
        }

        if(run_vec_exp_perf.ref)
            new Exponent_perf_host(test_protocols, card_channel)
                .run_vector_exp_perf(Vector_exponent_variant.PURE_JAVA);

        if(run_rsa_vec_exp_check.ref) {
            int old_long_size = State.long_size.ref;
            int old_short_size = State.short_size;
            if(run_all_checks.ref && !State.long_size_set.ref) {
                State.long_size.ref = 244;
                State.update_short_size();
            }
            new Exponent_perf_host(test_protocols, card_channel)
                .run_vector_exp_check(Vector_exponent_variant.MONT_RSA);
            State.long_size.ref = old_long_size;
            State.short_size = old_short_size;
        }

        if(run_rsa_vec_exp_perf.ref)
            new Exponent_perf_host(test_protocols, card_channel)
                .run_vector_exp_perf(Vector_exponent_variant.MONT_RSA);

        if(run_squared_rsa_vec_exp_check.ref) {
            int old_long_size = State.long_size.ref;
            int old_short_size = State.short_size;
            if(run_all_checks.ref && !State.long_size_set.ref) {
                State.long_size.ref = 244;
                State.update_short_size();
            }
            new Exponent_perf_host(test_protocols, card_channel)
                .run_vector_exp_check(Vector_exponent_variant.SQUARED_RSA);
            State.long_size.ref = old_long_size;
            State.short_size = old_short_size;
        }

        if(run_squared_rsa_vec_exp_perf.ref)
            new Exponent_perf_host(test_protocols, card_channel)
                .run_vector_exp_perf(Vector_exponent_variant.SQUARED_RSA);

        if(run_rsa_exp_check.ref) {
            int old_long_size = State.long_size.ref;
            int old_short_size = State.short_size;
            if(run_all_checks.ref && !State.long_size_set.ref) {
                State.long_size.ref = 244;
                State.update_short_size();
            }
            new Performance_mult_host(test_protocols,
                                      card_channel).run_rsa_exp_check();
            State.long_size.ref = old_long_size;
            State.short_size = old_short_size;
        }

        if(run_rsa_exp_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_rsa_exp_perf();

        if(run_add_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_add_perf();

        if(run_subtract_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_subtract_perf();

        if(run_normal_mult_perf.ref)
            new Performance_mult_host(test_protocols,
                                      card_channel).run_normal_mult_perf();
    }


    /**
     * 
     * List all applets. Retrieves the names of all applets from the
     * card or emulator with the help of the global platform manager.
     * They are then printed to {@code out}.
     * 
     * @param channel communication channel to the card
     * @param out output channel for progress and the applet list
     * @throws CardException for communication problems
     */
    public static void list_applets(CardChannel channel, PrintWriter out) 
        throws CardException
    {
        PrintWriter o = 
            out != null ? out : new PrintWriter(System.out, true);

        PrintWriter_APDUListener listener = new PrintWriter_APDUListener(out);

        if(out != null) {
            out.print("Query card status for applet list ");
            if(State.verbosity.ref >= 15)
                out.println("");
            else {
                out.flush();
                listener.print_dots(true);
            }
        }

        GlobalPlatformService gps = new GlobalPlatformService(channel);
        gps.addAPDUListener(listener);
        gps.openWithDefaultKeys();
        Vector<AIDRegistryEntry> applets = gps.getStatus().allApplets();

        if(out != null) {
            if(State.verbosity.ref >= 15)
                out.println("Query card status finished");
            else
                out.println(" finished");
        }

        if(applets.isEmpty()) {
            o.println("No applets found.");
        }
        else {
            o.format("Found %d applets on the card:\n", applets.size());
            for(AIDRegistryEntry e : applets) {
                o.println("  " + GPUtil.byteArrayToReadableString
                                                (e.getAID().getBytes()));
            }
        }
    }


    /**
     * 
     * Create the array of installation arguments for the test applet,
     * see {@link Test_applet#install Test_applet.install}. The
     * installation arguments are the short bignat size ({@link
     * State#short_bignat_max_size}), the long bignat size ({@link
     * State#long_bignat_max_size}), the double-sized bignat size
     * ({@link State#double_bignat_max_size}), the
     * base/exponent vector length ({@link State#max_vector_length}),
     * and the creation time of the applet cap file as 8-byte byte
     * array, which carries the long value as returned by {@link
     * File#lastModified}. 
     * 
     * @param cap_file_name applet cap file name
     * @return the installation arguments as serializable array
     */
    public static APDU_Serializable[] 
        make_installation_arguments(String cap_file_name) 
    {
        long cap_creation_time = new File(cap_file_name).lastModified();
        
        return new APDU_Serializable[]{
            new APDU_short(State.short_bignat_max_size), // short_bignat_size
            new APDU_short(State.long_bignat_max_size),  // long_bignat_size
            new APDU_short(State.double_bignat_max_size), // double_bignat_size
            new APDU_short(State.max_vector_length),     // max_vector_length
            new APDU_byte_array(                         // cap creation time
                     Misc_host.byte_array_from_long(cap_creation_time))
        };
    }


    /**
     * 
     * (Re-)Install the test applet. Uses the global platform manager
     * to install the applet in the file {@link
     * State#test_card_applet_file} on the card. If a package with
     * name {@link State#test_card_package_name} is on the card, it is
     * deleted before. If applet listing has been enabled, the
     * installed applets are printed to {@code out} at the end. 
     * 
     * @param channel communication channel to the card
     * @param out output channel for progress and status messages
     * @throws CardException for communication errors
     * @throws IOException if the cap file cannot be opened
     */
    public static void reinstall_applet(CardChannel channel, PrintWriter out) 
        throws CardException, IOException
    {
        PrintWriter o = 
            out != null ? out : new PrintWriter(System.out, true);

        PrintWriter_APDUListener listener = new PrintWriter_APDUListener(out);

        if(out != null) {
            out.print("(Re-)Install applet, query card status ");
            if(State.verbosity.ref >= 15)
                out.println("");
            else {
                out.flush();
                listener.print_dots(true);
            }
        }


        GlobalPlatformService gps = new GlobalPlatformService(channel);
        gps.addAPDUListener(listener);
        gps.openWithDefaultKeys();
        Vector<AIDRegistryEntry> packages = gps.getStatus().allPackages();

        if(out != null) {
            if(State.verbosity.ref >= 15)
                out.println("Query card status finished");
            else
                out.println(" finished");
        }

        for(AIDRegistryEntry e : packages) {
            if(Arrays.equals(e.getAID().getBytes(), 
                             State.test_card_package_name.getBytes(us_ascii)))
                {
                    o.print("Package found, deleting it. ");
                    o.flush();
                    if(State.verbosity.ref >= 15)
                        o.println("");
                    gps.deleteAID(e.getAID(), true);
                    o.println("Delete finished.");
                }
        }

        o.format("Load applet %s.\n", State.test_card_applet_file);
        o.flush();
        if(State.verbosity.ref >= 15)
            o.println("");
        gps.loadCapFile(State.test_card_applet_file, 
                        false,  // loadDebug
                        false,  // loadCompSep
                        GlobalPlatformService.defaultLoadSize,
                        false,  // loadParam
                        false   // useHash
                        );
        o.println("Applet loading finished.");

        APDU_Serializable[] arguments = 
            make_installation_arguments(State.test_card_applet_file);
        int arg_length = Misc.length_of_serializable_array(arguments);
        assert arg_length < 127;
        byte[] serialized_args = new byte[arg_length + 2];
        serialized_args[0] = (byte)0xC9;
        serialized_args[1] = (byte)arg_length;
        int res = Convert_serializable.array_to_bytes(arguments, 
                                                      serialized_args, 2); 
        assert res == serialized_args.length;

        o.format("Install applet %s of package %s.\n" +
                 "Installation arguments : %s\n", 
                 State.test_card_applet_name, 
                 State.test_card_package_name,
                 Misc_host.to_byte_hex_string(serialized_args)
                 );
        o.flush();
        gps.installAndMakeSelecatable
            (new AID(State.test_card_package_name.getBytes(us_ascii)),
             new AID(State.test_card_applet_name.getBytes(us_ascii)),
             null,              // instance AID
             (byte)0,           // privileges
             serialized_args,
             null               // install token
             );
        o.println("Install finished.");

        if(run_list_applets.ref)
            list_applets(channel, out);

    }


    /**
     * 
     * Print applet status to out.
     * 
     * @param out output channel
     * @param stat applet status record
     */
    public static void print_applet_status(PrintWriter out, Status_result stat) 
    {
        long applet_creation_time =
            Misc_host.long_from_byte_array(stat.cap_creation_time.buf);

        out.format("Connected to applet created at %s\n" +
                   "with maximal sizes %d, %d, %d and %d.\n" +
                   "Applet compile time config: assertion checks %s, " +
                   "use square 4 %s\n",
                   date_format.format(new Date(applet_creation_time)),
                   stat.max_short_bignat_size,
                   stat.max_long_bignat_size,
                   stat.max_double_bignat_size,
                   stat.max_vector_length,
                   stat.assertions_on ? "ON" : "OFF",
                   stat.use_squared_rsa_mult_4 ? "ON" : "OFF");
        out.flush();
    }


    /**
     * 
     * Select the test applet on the card. (Re)Install the applet if
     * it cannot be selected or if its timestamp (from the status
     * protocol) differs from the cap-file's last modification date.
     * 
     * @param card_channel channel to the card
     * @param test_protocols the protocol array instance
     * @param out progress and debug channel, if null, only
     * minimalistic messages are printed on {@link System#out}. 
     * @throws CardException on communication errors
     * @throws IOException if the cap file cannot be accessed
     */
    public static void open_test_applet(CardChannel card_channel, 
                                        Test_protocols test_protocols,
                                        PrintWriter out)
        throws CardException, IOException
    {
        PrintWriter oout = out == null ? new PrintWriter(System.out) : out;

        try {
            Card_terminal.open_applet_ex(card_channel, 
                                         State.test_card_applet_name
                                                        .getBytes(us_ascii), 
                                         out, 
                                         State.apduscript.ref);
        }
        catch(Applet_selection_exception e) {
            oout.println("Applet selection failed, reinstall.");
            oout.flush();
            
            reinstall_applet(card_channel, out);

            try {
                Card_terminal.open_applet_ex(card_channel, 
                                             State.test_card_applet_name
                                             .getBytes(us_ascii), 
                                             out, 
                                             State.apduscript.ref);
            }
            catch(Applet_selection_exception f) {
                oout.println("Applet selection failed directly after " +
                             "installing. Exit now.");
                System.exit(1);
            }
            return;
        }


        Status_result stat = 
            new Misc_protocols_host(test_protocols, card_channel, out)
            .get_applet_stat();

        print_applet_status(oout, stat);

        long applet_creation_time =
            Misc_host.long_from_byte_array(stat.cap_creation_time.buf);

        boolean same_max_values =
            stat.max_short_bignat_size == State.short_bignat_max_size &&
            stat.max_long_bignat_size == State.long_bignat_max_size &&
            stat.max_double_bignat_size == State.double_bignat_max_size &&
            stat.max_vector_length == State.max_vector_length;


        long cap_file_creation_time =
            new File(State.test_card_applet_file).lastModified();

        boolean host_app_square_4 =
            #ifdef USE_SQUARED_RSA_MULT_4
                true;
            #else
                false;
            #endif

        if(same_max_values &&
           applet_creation_time == cap_file_creation_time &&
           host_app_square_4 == stat.use_squared_rsa_mult_4) 
            {
                oout.println("Applet status matches, continue.");
                oout.flush();
            }
        else {
            String reason = "!!INTERNAL ERROR!!";
            boolean reason_set = false;

            if(!same_max_values) {
                reason = "Applet has different maximal sizes.";
                reason_set = true;
            }
            if(applet_creation_time != cap_file_creation_time) {
                String my_reason = 
                    String.format("Cap file was created on %s.",
                                  date_format.format
                                  (new Date(cap_file_creation_time)));
                if(reason_set)
                    reason = reason + "\n" + my_reason;
                else
                    reason = my_reason;
                reason_set = true;
            }
            if(host_app_square_4 != stat.use_squared_rsa_mult_4) {
                String my_reason = 
                    String.format("Different USE_SQUARED_RSA_MULT_4 " +
                                  "config: applet %s, host %s.",
                                  stat.use_squared_rsa_mult_4 ? "ON" : "OFF",
                                  host_app_square_4 ? "ON" : "OFF");
                if(reason_set)
                    reason = reason + "\n" + my_reason;
                else
                    reason = my_reason;
                reason_set = true;
            }

            if(keep_applet.ref) {
                oout.println(reason + "\n" +
                             "Keeping outdated applet at your risk.");
                oout.flush();
            }
            else {
                oout.println(reason + "\nReinstalling applet.");
                oout.flush();

                reinstall_applet(card_channel, out);

                Card_terminal.open_applet(card_channel, 
                                          State.test_card_applet_name
                                                    .getBytes(us_ascii), 
                                          out, 
                                          State.apduscript.ref);

                print_applet_status(oout, 
                                    new Misc_protocols_host(test_protocols, 
                                                            card_channel, out)
                                    .get_applet_stat());
            }
        }
    }

            



    /**
     * 
     * Real main method. Performs all the following actions:
     * <UL>
     * <li>parses the command line</li>
     * <li>initializes the host instance of the protocols</li>
     * <li>establishes a connection to the card or emulator</li>
     * <li>reinstalls the applet if requested</li>
     * <li>runs the requested test and measurments</li>
     * </ul>
     * 
     * @param args the complete command line options
     * @throws CardException for card communication errors
     * @throws IOException on IO errors (eg, for the applet cap file)
     */
    public static void main_ex(String[] args) 
        throws CardException, IOException
    {
        new Parse_commandline(options, short_application_name).parse(args);

        if(0 >= State.divisor_length.ref || State.divisor_length.ref > 100) {
            System.err.println("The divisor length must be a nonzero " +
                               "percentage [1-100]");
            System.exit(1);
        }

        if(State.long_size.ref < 0 || State.long_size.ref > Short.MAX_VALUE) {
            System.err.println("size argument must be a positive short");
            System.exit(1);
        }

        if(State.vector_length.ref > State.max_vector_length) {
            System.err.format("Maximal vector length is %d\n",
                              State.max_vector_length);
            System.exit(1);
        }

        if(State.pre_computed_base_factors.ref > State.max_vector_length) {
            System.err.format(
                    "Maximal number of pre-computed base factors is %d\n",
                    State.max_vector_length);
            System.exit(1);
        }

        if(reinstall_applet.ref && keep_applet.ref) {
            System.err.println("Options -reinstall and -keep-applet " +
                               "exclude each other.\n" +
                               "Make up your mind and try again.");
            System.exit(1);
        }

        // Update the short size, in case the long size or the short
        // bit size has been set with options.
        State.update_short_size();

        assert State.short_size >= 0 && State.short_size <= Short.MAX_VALUE &&
            State.long_size.ref >= 0 && State.long_size.ref <= Short.MAX_VALUE;


        // If -list-readers was given, list the readers, but nothing else.
        if(run_list_readers.ref) {
            Card_terminal.print_readers(new PrintWriter(System.out, true));
            System.exit(0);
        }

        Resize.init();
        Test_protocols test_protocols =
            new Test_protocols
            (new APDU_short(State.short_bignat_max_size),
             new APDU_short(State.long_bignat_max_size),
             new APDU_short(State.double_bignat_max_size),
             new APDU_short(State.max_vector_length),
             new APDU_byte_array((short)8));

        PrintWriter out = null;
        if(State.verbosity.ref >= 10)
            out = new PrintWriter(System.err, true);

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
                                            get_instance_parameter, out);

        if(run_list_applets.ref)
            list_applets(card_channel, out);

        if(reinstall_applet.ref)
            reinstall_applet(card_channel, out);

        open_test_applet(card_channel, test_protocols, out);
        
        try {
            run_tests(args, test_protocols, card_channel);
        }
        finally {
            Card_terminal.close_connection(out, card_channel);
        }

    }


    /**
     * 
     * Entry point. Does nothing but calling {@link #main_ex main_ex}.
     * 
     * @param args the complete command line options
     * @throws IOException on IO errors (eg, for the applet cap file)
     * @throws CardException for communication errors
     */
    public static void main(String[] args) 
        throws CardException, IOException
    {
        //try {
            main_ex(args);
        // }
        // catch(Invalid_reader_number e) {
        //     System.err.format("Invalid card reader number %d, " +
        //                    "try -list-readers!\n",
        //                    e.invalid_number);
        // }
    }
}
