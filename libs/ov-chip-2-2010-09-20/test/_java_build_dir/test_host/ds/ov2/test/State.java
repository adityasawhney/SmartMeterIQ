//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/State.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/State.java"
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
// Created 27.8.08 by Hendrik
// 
// general state variables
// 
// $Id: State.java,v 1.28 2010-03-12 15:40:21 tews Exp $

package ds.ov2.test;

import ds.ov2.util.Security_parameter;

import ds.ov2.util.Reference;
import ds.ov2.util.BigInteger_inputs;
import ds.ov2.bignat.Bignat;


/**
 * Global variables of the host driver for the test applet. Most of
 * them can be set via command-line options.
 * <P>
 *
 * Static class.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.28 $
 * @commitdate $Date: 2010-03-12 15:40:21 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class State {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected State() {}


    /**
     * 
     * Maximal size of short bignats (exponents). Must be identical to
     * the configured size of the short bignats in the applet. For
     * this, this number must be passed as first applet-installation
     * argument, see {@link Test_applet#install Test_applet.install}
     * and {@link Test_host#make_installation_arguments}. The current
     * value ({@value}) is identical to the maximal RSA key size of my
     * cards.
     */
    public final static short short_bignat_max_size = 244;


    /**
     * 
     * Maximal size of long bignats (bases) in bytes. Must be identical to the
     * configured size of the long bignats in the applet. For this,
     * this number must be passed as second applet-installation
     * argument, see {@link Test_applet#install Test_applet.install}
     * and {@link Test_host#make_installation_arguments}. The current
     * value is 256 + 2 Montgomery digits (= {@value}) to enable
     * measurments up to 256 byte numbers.
     */
    public final static short long_bignat_max_size =
        256 + 2 * Bignat.size_multiplier;


    /**
     * 
     * Maximal size of double-sized bignats in bytes. Must be
     * identical to the configured size of the double-sized bignats in
     * the applet. For this, this number must be passed as third
     * applet-installation argument, see {@link Test_applet#install
     * Test_applet.install} and {@link
     * Test_host#make_installation_arguments}. The current value is
     * 128.
     */
    public final static short double_bignat_max_size = 130;


    /**
     * 
     * Maximal number of bases and exponents for {@link
     * ds.ov2.bignat.Vector#exponent_mod Vector.exponent_mod} and
     * {@link ds.ov2.bignat.Vector#mont_rsa_exponent_mod
     * Vector.mont_rsa_exponent_mod}. Must be identical to the configured
     * size of the vector objects in the applet. For this, this number
     * must be passed as fourth applet-installation argument, see
     * {@link Test_applet#install Test_applet.install} and {@link
     * Test_host#make_installation_arguments}. The current value is
     * {@value}.
     */
    public final static short max_vector_length = 5;


    /**
     * 
     * Year for the estimation of the exponent length. Various
     * performance tests run in a loop over a range of base or RSA
     * key-length sizes. If an exponent size is needed and non has
     * been explicitely set it is estimated with {@link
     * ds.ov2.util.Security_parameter#exponent_length_for_modulus_length
     * Security_parameter.exponent_length_for_modulus_length}. This is
     * the year up to which this estimation is valid. Higher numbers
     * result in smaller exponent sizes. <P>
     *
     * Current value is {@value}.
     */
    public final static int rsa_year = 2009;


    /**
     * 
     * Default verbosity. Changed with the options {@code
     * -d}, {@code -dd}, {@code -ddd}. Controls the amount of data
     * printed to the terminal. Value 0 prints almost only performance
     * data. Value 5 ({@code -d}) prints some progress messages. Value
     * 10 ({@code -dd}) additionally prints all parameters of the
     * tests. Value 15 ({@code -ddd}) additionally prints the APDU's
     * exchanged with the card.
     */
    public static Reference<Integer> verbosity = new Reference<Integer>(0);


    /**
     * 
     * Print apdutool lines in addition to the APDU contents. Useful
     * if an apdutool script file must be created. 
     * <P>
     *
     * Default value is false, enabled with option {@code
     * -apdutool}. 
     */
    public static Reference<Boolean> apduscript =
        new Reference<Boolean>(false);


    /**
     * 
     * Changed to true if {@link #rounds} is set via an option. Then some
     * automatic round number changes are disabled.
     */
    public static Reference<Boolean> rounds_set = new Reference<Boolean>(false);


    /**
     * 
     * Number of rounds to run for various tests.
     * <P>
     *
     * The default value is 1, set with option {@code -rounds}.
     */
    public static Reference<Integer> rounds = new Reference<Integer>(1);


    /**
     * 
     * Size of short (exponent) bignats in bytes. Used in many tests. 
     * <P>
     *
     * The default value is {@link #short_bignat_max_size}, indirectly
     * set via option {@code -exp-size}.
     */
    public static int short_size = short_bignat_max_size;


    /**
     * 
     * Size of long (base) bignats in bytes. Used in many tests. 
     * <P>
     *
     * The default value is {@link #long_bignat_max_size}, set with
     * option {@code -size}.
     */
    public static Reference<Integer> long_size =
        new Reference<Integer>(long_bignat_max_size
                               - 2 * Bignat.size_multiplier);


    /**
     * 
     * Size of double-sized bignats in bytes. 
     * <P>
     *
     * The default value is {@link #long_bignat_max_size}.
     */
    public static int double_size = double_bignat_max_size;


    /**
     * 
     * Size of short (exponent) bignats in bits. Used in many tests.
     * <P>
     *
     * The default value is {@link #short_bignat_max_size}{@code * 8},
     * set via option {@code -exp-size}.
     */
    public static Reference<Integer> short_bit_size =
        new Reference<Integer>(short_bignat_max_size * 8);


    /**
     * 
     * Changed to true, if {@link #long_size} is set via an option.
     * Then some automatic size changes are disabled.
     */
    public static Reference<Boolean> long_size_set =
        new Reference<Boolean>(false);


    /**
     * 
     * Changed to true, if {@link #short_bit_size} is set via options.
     * Then some automatic size changes are diabled.
     */
    public static Reference<Boolean> short_bit_size_set =
        new Reference<Boolean>(false);


    /**
     * 
     * Actual lenght of base and exponent vectors. Used in the vector
     * tests and measurements.
     * <P>
     *
     * The default value is {@link #max_vector_length}, set with
     * command-line option {@code -vec-len}.
     */
    public static Reference<Integer> vector_length =
        new Reference<Integer>((int)max_vector_length);


    /**
     * Number of bases for which the factors are precomputed, see
     * {@link ds.ov2.bignat.Vector#exponent_mod Vector.exponent_mod}.
     * Used for the (non-RSA) vector exponent checks and measurements.
     * <P>
     *
     * The default value is {@link #max_vector_length}, set via option
     * {@code -pre-base-fac}.
     */
    public static Reference<Integer> pre_computed_base_factors =
        new Reference<Integer>((int)max_vector_length);


    /**
     * Use the same modulus for a number of successive tests to
     * simulate the OV-chip use-case. Used in vector exponent
     * measurements (both RSA and non-RSA, although for non-RSA it is
     * irrelevant). 
     * <P>
     *
     * The default value is false, set via option {@code
     * -keep-mod}.
     */
    public static Reference<Boolean> keep_modulus =
        new Reference<Boolean>(false);


    /**
     * Size to start with for those performance measurements that loop
     * over a range of sizes. If left at the default value 0, those
     * measurements select the smallest sensible value.
     * <P>
     *
     * Set via option {@code -start-size}.
     */
    public static Reference<Integer> start_size = new Reference<Integer>(0);


    /**
     * Length of the divisor as percentage of the divident length for
     * the division test and measurement. For instance, set to 50 to
     * measure the division with the divident twice as long as the
     * divisor. 
     * <P>
     *
     * The default value is 100 (divident and divisor have the same
     * length), set via option {@code -div-length}.
     */
    public static Reference<Integer> divisor_length =
        new Reference<Integer>(100);


    /**
     * 
     * Corner case checking. Check programmed corner cases if set.
     * Enabled with option {@code -check-corner-cases}. Currently most
     * checks do not support corner cases, the only exceptions being
     * squared multiplication and short squared multiplication.
     */
    public static Reference<Boolean> check_corner_cases =
        new Reference<Boolean>(false);


    /**
     * 
     * Exponent to be used. Set with option {@code -fixed-exponent}.
     * Currently only supported by the RSA power checks started with
     * {@code -rsa-exp-check} and {@code -rsa-exp-perf}.
     */
    public static Reference<String> fixed_exponent = new Reference<String>();


    /**
     * 
     * File name of the applet cap file. Used for applet installation.
     * Currently a path relative to the sources directory.
     */
    public static String test_card_applet_file =
        "_java_build_dir/card/test/ov_test/javacard/ov_test.cap";


    /**
     * 
     * Applet ID of the test applet. Used for applet installation.
     */
    public static String test_card_applet_name = "ov_test.app";


    /**
     * 
     * Applet ID of the test applet package. Used for applet
     * installation. 
     */
    public static String test_card_package_name = "ov_test";


    /**
     * Vector of input arguments from the command line. Normally tests
     * and measurements use randomly created parameters. To repeat a
     * test with some given numbers those numbers can be passed with
     * the options {@code -i} and {@code -hex}. They are then stored
     * in order in this vector. Many tests take their numbers from this
     * vector, if there are enough numbers available. 
     */
    public static BigInteger_inputs fix_inputs = new BigInteger_inputs();


    /**
     * 
     * (Re-)Compute the short (exponent) bignat size in bits for the
     * given long (base) size. Relies on {@link Security_parameter}
     * for the estimation if {@link #short_bit_size} has not
     * explicitely set. 
     * 
     * @param long_byte_size effective long (base) size in bytes,
     * without any potential Montgomery digits
     * @return short (exponent) size in bits
     * @throws IllegalArgumentException if long_byte_size is less than
     * or equal to 0
     */
    public static int make_short_bit_size(int long_byte_size)
        throws IllegalArgumentException
    {
        if(long_byte_size <= 0)
            throw new IllegalArgumentException("invalid base size");
        if(short_bit_size_set.ref)
            return short_bit_size.ref;
        else {
            int short_size =
                Security_parameter.exponent_length_for_modulus_length
                (rsa_year, long_byte_size * 8, null);
            return short_size;
        }
    }


    /**
     * 
     * (Re-)Compute a suitable short (exponent) bignat size in bytes
     * for the given long (base) size. This method divides the result
     * of {@link #make_short_bit_size} by 8 and changes the result
     * such that the various side conditions are fulfiled.
     * 
     * @param long_byte_size effective long (base) size in bytes,
     * without any potential Montgomery digits
     * @return short (exponent) size in bytes.
     */
    public static int make_short_size(int long_byte_size) {
        int short_bit_size = make_short_bit_size(long_byte_size);
        // int short_byte_size = (short_bit_size + 7) / 8 + 2;
        int short_byte_size = (short_bit_size + 7) / 8;

        // Effective size of exponent should be one byte at least.
        // short_byte_size = short_byte_size < 3 ? 3 : short_byte_size;
        short_byte_size = short_byte_size < 1 ? 1 : short_byte_size;

        // short_byte_size must be a multiple of Bignat.size_multiplier.
        if(short_byte_size % Bignat.size_multiplier != 0) {
            short_byte_size += Bignat.size_multiplier -
                short_byte_size % Bignat.size_multiplier;
        }

        if(short_byte_size > short_bignat_max_size) {
            System.err.format("short size %d exceeds maximum %d\n",
                              short_byte_size, short_bignat_max_size);
            assert false;
            System.exit(1);
        }

        return short_byte_size;
    }


    /**
     * 
     * Update the {@link #short_size} after the {@link #long_size} has
     * been changed. The new value of {@link #short_size} will only
     * depend on {@link #long_size} if {@link #short_bit_size} has not
     * explicitely been set. 
     * 
     */
    public static void update_short_size() {
        short_size = make_short_size(long_size.ref);
        return;
    }
}
