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
// Created 24.9.08 by Hendrik
// 
// vector exponent test host side
// 
// $Id: Exponent_perf_host.java,v 1.37 2010-02-16 11:06:06 tews Exp $

package ds.ov2.test;

import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.Random;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;

import ds.ov2.util.Misc_host;
import ds.ov2.util.BigIntUtil;
import ds.ov2.util.Response_status;
import ds.ov2.util.Response_apdu;
import ds.ov2.util.Response_apdu.Card_response_error;
import ds.ov2.bignat.Bignat;
import ds.ov2.bignat.Host_modulus;
import ds.ov2.bignat.Host_vector;
import ds.ov2.bignat.Resize;


/** 
 * Correctness check and performance measurement for all
 * multi-exponent variants in {@link ds.ov2.bignat.Vector}. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.37 $
 * @commitdate $Date: 2010-02-16 11:06:06 $ by $Author: tews $
 * @environment host
 */
public class Exponent_perf_host {

    /**
     * 
     * Protocol array instance to set the result sizes after changing
     * the vector length.
     */
    private Test_protocols test_protocols;

    /**
     * 
     * Debug and progress channel.
     */
    private PrintWriter out;

    /**
     * 
     * Misc protocols instance for the resize protocol.
     */
    private Misc_protocols_host misc_host;

    /**
     * 
     * Stubs for the Vector_exp protocol.
     */
    private Exponent_perf_stubs stubs;

    /**
     * 
     * Channel to the applet.
     */
    private CardChannel card_channel;


    /**
     * 
     * Constructor. Initialize all the fields.
     * 
     * @param test_protocols protocol array instance
     * @param card_channel channel to the applet
     */
    public Exponent_perf_host(Test_protocols test_protocols,
                              CardChannel card_channel) 
    {
        this.test_protocols = test_protocols;
        this.card_channel = card_channel;
        out = new PrintWriter(System.out, true);
        misc_host = 
            new Misc_protocols_host(test_protocols, card_channel, out);
        PrintWriter stub_out = State.verbosity.ref >= 10 ? out : null;
        stubs = new Exponent_perf_stubs(test_protocols.exponent_protocols,
                                        stub_out, 
                                        State.apduscript.ref);
    }


    //#########################################################################
    //#########################################################################
    // 
    // change vector length
    // 
    //#########################################################################
    //#########################################################################


    /**
     * 
     * Run the Vector_length protocol to resize base, exponents and
     * base factors vectors.
     * 
     * @param new_length new length for bases and exponents
     * @param new_factors_length number of factors the factor array
     * gets, the real array size will then be {@code
     * 2^new_factors_length -1}
     * @throws CardException for communication errors
     */
    private void change_vector_length(int new_length, int new_factors_length) 
        throws CardException
    {
        int sleep = 1500;

        Misc_host.sleep(sleep); Runtime.getRuntime().gc();
        stubs.set_vector_length_call(card_channel, 
                                     new_length, new_factors_length);
        Misc_host.sleep(sleep); Runtime.getRuntime().gc();

        Resize.resize_vectors((short)new_length, (short)new_factors_length);
        test_protocols.set_result_sizes();
    }


    //########################################################################
    //########################################################################
    // 
    // vector exponent in all variants
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Enumeration for the different multi-power implementations.
     */
    public static enum Vector_exponent_variant {

        /** Pure Java, Montgomery multiplication used everywhere,
         * {@link ds.ov2.bignat.Vector#exponent_mod Vector.exponent_mod}. */
        PURE_JAVA,

        /** RSA power and Montgomery for multiplication, {@link
         * ds.ov2.bignat.Vector#mont_rsa_exponent_mod
         * Vector.mont_rsa_exponent_mod} */
        MONT_RSA,

        /** RSA power and sqared RSA multiplication, {@link
         * ds.ov2.bignat.Vector#squared_rsa_exponent_mod
         * Vector.squared_rsa_exponent_mod} */
        SQUARED_RSA;

        /**
         * 
         * Convert this enumeration value into a short, to be used on
         * the card.
         */
        public short to_short() {
            switch(this) {
            case PURE_JAVA: return 0;
            case MONT_RSA: return 1;
            case SQUARED_RSA: return 2;
            default: return -1;
            }
        }
    }


    /**
     * 
     * Result record of the Vector_exp protocol.
     */
    private class Exp_call_result {
        /** Duration of the vector_exp_1 step in nanoseconds. */
        public final long duration_1;

        /** Duration of the vector_exp_2 step in nanoseconds. */
        public final long duration_2;

        /** Result computed on the card. */
        public final BigInteger result;

        /** Record creation.
         * 
         * @param d1 duration of vector_exp_1 in nanoseconds
         * @param d2 duration of vector_exp_2 in nanoseconds
         * @param r result
         */
        public Exp_call_result(long d1, long d2, BigInteger r) {
            duration_1 = d1;
            duration_2 = d2;
            result = r;
        }
    }


    /**
     * 
     * Run the complete vector exponent protocol. Computes {@code
     * base[0]^exponent[0] * base[1]^exponent[1] * ... mod modulus} on
     * the card and returns the result and the performance timings.
     * Depending on the implementation selected with {@code variant},
     * different extra data is necessary.
     * <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 400
     * milliseconds). 
     * 
     * @param base base vector, Montgomerized for {@link
     * Vector_exponent_variant#PURE_JAVA},
     * normal otherwise
     * @param exponent exponent vector, always normal
     * @param modulus modulus
     * @param base_factor_size only necessary for the {@link
     * Vector_exponent_variant#PURE_JAVA} variant: number of factors
     * whose products are precomputed in {@code base_factors}
     * @param base_factors array of all possible products of the first
     * {@code base_factor_size} bases, see {@link
     * ds.ov2.bignat.Vector#exponent_mod Vector.exponent_mod}; Only
     * needed for {@link Vector_exponent_variant#PURE_JAVA}, must
     * otherwise contain at least one (arbitrary) element, because the
     * protocol layer does not support empty objects
     * @param one_or_correction for {@link
     * Vector_exponent_variant#PURE_JAVA} montgomerized 1, for {@link
     * Vector_exponent_variant#MONT_RSA} Montgomery correction factor,
     * not used for {@link Vector_exponent_variant#SQUARED_RSA}
     * @param variant chosen implementation variant
     * @param keep_modulus whether the modulus is the same as in the
     * last test and should thus not be installed
     * @return record with result and duration in nanoseconds
     * @throws CardException on communiction errors
     */
    public Exp_call_result card_vector_exp(Host_vector base,
                                           Host_vector exponent,
                                           Host_modulus modulus,
                                           int base_factor_size,
                                           Host_vector base_factors,
                                           BigInteger one_or_correction,
                                           Vector_exponent_variant variant,
                                           boolean keep_modulus)
        throws CardException
    {
        int sleep = 200;
        Misc_host.gc_sleep(sleep); 
        stubs.vector_exp_choose_mod_call(card_channel, variant.to_short());
        Misc_host.gc_sleep(sleep); 
        stubs.vector_exp_init_call(card_channel, base, exponent, modulus, 
                                   base_factor_size, base_factors,
                                   one_or_correction, 
                                   keep_modulus);
        Misc_host.gc_sleep(sleep); 
        long d1 = stubs.vector_exp_1_call(card_channel);
        Misc_host.gc_sleep(sleep); 
        long d2 = stubs.vector_exp_2_call(card_channel);
        Misc_host.gc_sleep(sleep); 
        BigInteger r = stubs.vector_exp_result_call(card_channel);
        Misc_host.gc_sleep(sleep); 
        return new Exp_call_result(d1, d2, r);
    }


    /**
     * 
     * Generate input data, run the vector exponent protocol once and
     * check the result. Terminate the program if the card result is
     * wrong. The multi-power method to be used on the card is
     * determined by the argument {@code variant}. For the RSA
     * variants, where changing the modulus costs some time, the same
     * modulus (passed as {@code mod} argument) can be used for a
     * number of successive test.
     * 
     * @param mod modulus if non-null, if null generate modulus randomly
     * @param rand randomness source
     * @param short_bit_size exponent size in bits
     * @param short_byte_size exponent size in bytes
     * @param effective_byte_size base size in bytes, excluding the
     * Montgomery digits for those variants that use them
     * @param long_byte_size base size in bytes, including the
     * Montgomery digits for those variants that use them
     * @param pre_computed_factors number of bases for which their
     * products are precomputed on the host side; must be at least 1,
     * even for the RSA variants that do not use precomputed products 
     * @param variant the muli-power implementation to use on the card
     * @return combined result record
     * @throws CardException on communication errors
     */
    public Exp_call_result vector_exp_once(BigInteger mod,
                                           Random rand, 
                                           int short_bit_size,
                                           int short_byte_size,
                                           int effective_byte_size,
                                           int long_byte_size,
                                           int pre_computed_factors,
                                           Vector_exponent_variant variant,
                                           boolean keep_modulus)
        throws CardException
    {
        // long_byte_size possibly contains additional Montgomery
        // digits, thus it must be larger or at least equal.
        assert effective_byte_size <= long_byte_size;

        // For RSA the precomputed factors are not needed and
        // never even touched. Our RMI protocol implementation doesn't
        // like empty data types, therefore use 1 factor for rsa.
        assert variant == Vector_exponent_variant.PURE_JAVA 
            || pre_computed_factors == 1;
        
        // We can only reuse the modulus, if the mod-argument is
        // non-null.
        assert !keep_modulus || mod != null;

        int effective_bit_size, minimal_mod_bit_size;
        switch(variant){
        case PURE_JAVA:
        case MONT_RSA:
            effective_bit_size = effective_byte_size * 8;
            minimal_mod_bit_size = effective_bit_size - 7;
            break;
        case SQUARED_RSA:
            // For squared multiplication the highest bit must be
            // empty. For squared 4 multiplication the highest two
            // bits must empty. 
            #ifdef USE_SQUARED_RSA_MULT_4
                effective_bit_size = effective_byte_size * 8 - 2;
            #else
                effective_bit_size = effective_byte_size * 8 - 1;
            #endif
            minimal_mod_bit_size = (effective_byte_size -1) * 8 + 1;
            break;
        default:
            assert false;
            effective_bit_size = -1;
            minimal_mod_bit_size = -1;
        }

        // Initialize base and pow.
        BigInteger base[] = new BigInteger[State.vector_length.ref];
        BigInteger pow[] = new BigInteger[State.vector_length.ref];

        // Initialize from command line arguments?
        if(State.fix_inputs.has_n_inputs(State.vector_length.ref * 2 + 1, 
                              "vector exponent")) {
            for(int i = 0; i < State.vector_length.ref; i++)
                base[i] = State.fix_inputs.pop();
            for(int i = 0; i < State.vector_length.ref; i++)
                pow[i] = State.fix_inputs.pop();

            assert mod == null;
            mod = State.fix_inputs.pop();
        }
        // Or initialize randomly?
        else {
            for(int i = 0; i < State.vector_length.ref; i++)
                base[i] = new BigInteger(effective_bit_size, rand);
            for(int i = 0; i < State.vector_length.ref; i++)
                pow[i] = new BigInteger(short_bit_size, rand);

            if(mod == null) {
                do {
                    mod = new BigInteger(effective_bit_size, rand);
                } while(mod.bitLength() < minimal_mod_bit_size);

                // make mod odd
                mod = mod.setBit(0);
                // If square 4 mult is used the modulus must be
                // equivalent to 1 modulo 4
                #ifdef USE_SQUARED_RSA_MULT_4
                    mod = mod.clearBit(1);
                #endif
            }
        }

        // Output base, exponent and modulus.
        if(State.verbosity.ref >= 5) {
            BigIntUtil.print_array(out, "## %d bases\n", "## base", base);
            BigIntUtil.print_array(out, "## %d exponents\n", "## expo", pow);
            out.format("## mod = %s\n" +
                       "##     = %s\n", 
                       mod,
                       BigIntUtil.to_byte_hex_string(mod));
        }

        // Compute the expected (right) result.
        BigInteger result = BigIntUtil.multi_exponent(base, pow, mod);
        if(State.verbosity.ref >= 5)
            out.format("## result = %s\n", result);

        // Prepare modulus, this will compute all necessary incredients 
        // for montgomery multiplication internally and store them in hmod.
        Host_modulus hmod = new Host_modulus(long_byte_size, mod);

        // For the case of use_rsa we multiply n numbers in the card. 
        // To multiply n (here State.vector_length.ref) numbers with 
        // Montgomery multiplication one has to add one factor
        // mont_fac^n, where mont_fac is the Montgomery factor.
        // 
        // In the case of !use_rsa we need a montgomerized one for 
        // initializing the accumulator.
        BigInteger one_or_correction;
        switch(variant) {
        case PURE_JAVA:
            // Montgomerized 1.
            one_or_correction = hmod.mont_fac;
            break;
        case MONT_RSA:
            // Correction factor.
            one_or_correction = 
                hmod.mont_fac.pow(State.vector_length.ref).mod(mod);
            break;
        case SQUARED_RSA:
            // Not needed, use an arbitrary value.
            one_or_correction = BigInteger.ZERO;
            break;
        default:
            one_or_correction = null;
            assert false;
        }

        if(State.verbosity.ref > 9) {
            out.format("## mont fac = %s\n" +
                       "##          = %s\n",
                       hmod.mont_fac,
                       BigIntUtil.to_byte_hex_string(hmod.mont_fac));
            out.format("## demont fac = %s\n" +
                       "##            = %s\n",
                       hmod.demont_fac,
                       BigIntUtil.to_byte_hex_string(hmod.demont_fac));
            out.format("## lbi = %03d = %02X\n",
                       hmod.last_digit_inverse, 
                       hmod.last_digit_inverse);
            if(variant == Vector_exponent_variant.MONT_RSA)
                out.format("## one/corr = %s\n" +
                           "##          = %s\n",
                           one_or_correction,
                           BigIntUtil.to_byte_hex_string(one_or_correction));
        }

        // For the non-rsa implementation the base must be montgomerized.
        // For the rsa implementation the base must be normal.
        Host_vector hbase;
        switch(variant) {
        case PURE_JAVA:
            // Montgomerize the base.
            hbase = Host_vector.make_montgomerized_vector(long_byte_size, 
                                                          base, hmod);
            break;
        case MONT_RSA:
        case SQUARED_RSA:
            hbase = new Host_vector(long_byte_size, base);
            break;
        default:
            hbase = null;
            assert false;
        }

        if(State.verbosity.ref > 9) {
            switch(variant) {
            case PURE_JAVA:
                BigIntUtil.print_array(out, "## montgomerized base\n", 
                                       "## mbase", hbase.a);
                break;
            case MONT_RSA:
            case SQUARED_RSA:
                BigIntUtil.print_array(out, "## base\n", "## mbase", 
                                       hbase.a);
                break;
            default:
                assert false;
            }
        }

        // The exponent is never montgomerized.
        Host_vector hpow = new Host_vector(short_byte_size, pow);


        // Decide how many factors of the base will be precomputed and 
        // set them up in a factors Host_vector.
        BigInteger short_base[] = new BigInteger[pre_computed_factors];
        System.arraycopy(base, 0, short_base, 0, pre_computed_factors);
        Host_vector hfactors = 
            Host_vector.make_montgomerized_factors(long_byte_size, 
                                                   short_base, hmod);

        if(State.verbosity.ref >= 5)
            out.format("## %d precomputed (montgomerized) factors\n", 
                       pre_computed_factors);
        if(State.verbosity.ref >= 10)
            BigIntUtil.print_array(out, "", "## fac", hfactors.a);
            
        

        // Compute the exponent, for PURE_JAVA the result will be
        // montgomerized. 
        Exp_call_result res = 
            card_vector_exp(hbase, hpow, hmod, pre_computed_factors, 
                            hfactors, one_or_correction, variant, 
                            keep_modulus);


        if(State.verbosity.ref > 14) {
            switch(variant) {
            case PURE_JAVA:
                out.format("## mont result %s\n", res.result);
                break;
            case MONT_RSA:
            case SQUARED_RSA:
                out.format("## result %s\n", res.result);
                break;
            default:
                assert false;
            }
        }

        // The non-rsa implementation delivers a montgomerized result.
        BigInteger card_result;
        switch(variant) {
        case PURE_JAVA:
            card_result = res.result.multiply(hmod.demont_fac).mod(hmod.m);
            break;
        case MONT_RSA:
        case SQUARED_RSA:
            card_result = res.result;
            break;
        default:
            card_result = null;
            assert false;
        }

        if(card_result.compareTo(result) == 0 && State.verbosity.ref >= 5)
            out.println("## SUCCESS");

        if(card_result.compareTo(result) != 0) {
            out.println("## FAILURE");
            out.format("## got %s\n", card_result);
            if(State.verbosity.ref < 5) {
                BigIntUtil.print_array(out, "## %d bases\n", "## base", base);
                BigIntUtil.print_array(out, "## %d exponents\n",
                                       "## expo", pow);
                out.format("## mod = %s\n", mod);
                out.format("## %d precomputed factors\n", pre_computed_factors);
            }
            System.exit(1);
        }

        return res;
    }


    /**
     * Check {@link State#rounds} rounds the multi-power computation
     * on the card. On any error the program is terminated and the
     * involved numbers are printed.
     * 
     * 
     * @param variant multi-power implementation to use on the card
     * @throws CardException on communication errors
     */
    public void vector_exp_check(Vector_exponent_variant variant) 
        throws CardException
    {
        Random rand = new Random();

        if(State.pre_computed_base_factors.ref > State.vector_length.ref) 
            State.pre_computed_base_factors.ref = State.vector_length.ref;

        int pre_computed_factors;
        int effective_size;     // without Montgomery digits
        int long_size;          // with Montgomery digits
        int double_size;
        int cipher_size;
        // The RSA implementations do not use any pre-computed factors.
        // Our RMI protocol implementation doesn't
        // like empty data types, therefore use 1 factor for rsa.
        switch(variant) {
        case PURE_JAVA:
            pre_computed_factors = State.pre_computed_base_factors.ref;
            effective_size = State.long_size.ref;
            long_size = effective_size + 2 * Bignat.size_multiplier;
            double_size = State.double_size;
            cipher_size = 0;    // Don't resize ciphers
            break;
        case MONT_RSA:
            pre_computed_factors = 1;
            effective_size = State.long_size.ref;
            long_size = effective_size + 2 * Bignat.size_multiplier;
            double_size = State.double_size;
            cipher_size = effective_size;
            break;
        case SQUARED_RSA:
            pre_computed_factors = 1;
            effective_size = State.long_size.ref;
            long_size = effective_size;
            double_size = 2 * State.short_size + 1;
            cipher_size = effective_size;
            break;
        default:
            pre_computed_factors = -1;
            effective_size = -1;
            long_size = -1;
            double_size = -1;
            cipher_size = -1;
            assert false;
        }

        out.format("## Effective size base %d exponent %d\n", 
                   effective_size, State.short_size);

        misc_host.set_size(State.short_size, long_size, 
                           double_size, cipher_size);
        change_vector_length(State.vector_length.ref, 
                             pre_computed_factors);

        for(int i = 0; i < State.rounds.ref; i++)
            vector_exp_once(null,
                            rand,
                            State.short_size * 8, 
                            State.short_size, 
                            effective_size,
                            long_size,
                            pre_computed_factors, 
                            variant,
                            false);
    }


    /**
     * 
     * Measure one round of multi-power and print the timings in
     * gnuplot friendly way. Input-data generation and the actual test
     * is done by {@link #vector_exp_once vector_exp_once}. Terminate
     * the program if the card result is wrong. The multi-power method
     * to be used on the card is determined by the argument {@code
     * variant}. For the RSA variants, where changing the modulus
     * costs some time, the same modulus (passed as {@code mod}
     * argument) can be used for a number of successive test.
     * 
     * @param mod modulus if non-null, if null generate modulus randomly
     * @param rand randomness source
     * @param short_bit_size exponent size in bits
     * @param short_byte_size exponent size in bytes
     * @param effective_byte_size base size in bytes, excluding the
     * Montgomery digits for those variants that use them
     * @param long_byte_size base size in bytes, including the
     * Montgomery digits for those variants that use them
     * @param pre_computed_factors number of bases for which their
     * products are precomputed on the host side; must be at least 1,
     * even for the RSA variants that do not use precomputed products 
     * @param variant the muli-power implementation to use on the card
     * @param keep_modulus whether the modulus is the same as in the
     * last test and should thus not be installed
     * @throws CardException on communication errors
     */
    public void vector_exp_measure_size(BigInteger mod,
                                        Random rand, 
                                        int short_bit_size,
                                        int short_byte_size,
                                        int effective_byte_size,
                                        int long_byte_size,
                                        int pre_computed_factors,
                                        Vector_exponent_variant variant,
                                        boolean keep_modulus)
        throws CardException
    {
        // long_byte_size possibly contains additional Montgomery
        // digits, thus it must be larger or at least equal.
        assert effective_byte_size <= long_byte_size;

        // For RSA the precomputed factors are not needed and
        // never even touched. Our RMI protocol implementation doesn't
        // like empty data types, therefore use 1 factor for rsa.
        assert variant == Vector_exponent_variant.PURE_JAVA 
            || pre_computed_factors == 1;

        Exp_call_result r = vector_exp_once(mod, 
                                            rand, 
                                            short_bit_size,
                                            short_byte_size,
                                            effective_byte_size,
                                            long_byte_size,
                                            pre_computed_factors,
                                            variant,
                                            keep_modulus);

        out.format("size %d ^ %d %.03f s " +
                   "(%.03f s %.03f s)\n",
                   effective_byte_size * 8, 
                   short_bit_size,
                   (r.duration_2 - r.duration_1) / 1E9,
                   r.duration_1 / 1.0E9,
                   r.duration_2 / 1.0E9
                   );
        return;
    }


    /**
     * 
     * Measure multi-power with increasing number sizes. Perform
     * {@link State#rounds} measurements for each size between {@link
     * State#start_size} and {@link State#long_bignat_max_size}. If
     * {@link State#start_size} has not been set, a very small start
     * size is used. Each size is measured and printed with {@link
     * #vector_exp_measure_size vector_exp_measure_size}. The program
     * is terminated if the card computes anything wrong. If {@link
     * State#keep_modulus} is set, the same modulus is used for all
     * measurements of one size.
     * 
     * @param variant the multi-power variant to measure
     * @throws CardException on communication errors
     */
    public void vector_exp_measure(Vector_exponent_variant variant)
        throws CardException
    {
        Random rand = new Random();

        if(State.keep_modulus.ref && State.fix_inputs.size() != 0) {
            System.err.println("-keep-mod does not work with -i/-hex");
            System.exit(1);
        }

        out.format("################# %s power perf start\n", variant);

        if(State.pre_computed_base_factors.ref > State.vector_length.ref) 
            State.pre_computed_base_factors.ref = State.vector_length.ref;

        int pre_computed_factors;
        // The rsa implementation does not use any pre-computed factors.
        // Our RMI protocol implementation doesn't
        // like empty data types, therefore use 1 factor for rsa.
        switch(variant) {
        case PURE_JAVA:
            pre_computed_factors = State.pre_computed_base_factors.ref;
            break;
        case MONT_RSA:
        case SQUARED_RSA:
            pre_computed_factors = 1;
            break;
        default:
            pre_computed_factors = -1;
            assert false;
        }

        change_vector_length(State.vector_length.ref, 
                             pre_computed_factors);

        int start = State.start_size.ref > 0 ? State.start_size.ref : 
                                       4 * Bignat.size_multiplier;

        int increment;
        int mont_digits_bytes;
        switch(variant) {
        case PURE_JAVA:
            increment = Bignat.size_multiplier;
            mont_digits_bytes = 2 * Bignat.size_multiplier;
            break;
        case MONT_RSA:
            increment = 2 * Bignat.size_multiplier;
            mont_digits_bytes = 2 * Bignat.size_multiplier;
            break;
        case SQUARED_RSA:
            increment = 2 * Bignat.size_multiplier;
            mont_digits_bytes = 0;
            break;
        default:
            increment = 10000;
            mont_digits_bytes = -1;
            assert false;
        }


        for(int i = start; 
            i + mont_digits_bytes <= State.long_bignat_max_size; 
            i += increment)
        {
            int short_bit_size = 
                State.make_short_bit_size(i);
            int short_byte_size = State.make_short_size(i);
            
            out.format("## Effective size base %d bytes (%d bits) " +
                       "exponent %d bits (in %d bytes)\n", 
                       i,
                       i * 8,
                       short_bit_size,
                       short_byte_size);

            switch(variant) {
            case PURE_JAVA:
                misc_host.set_size(short_byte_size, i + mont_digits_bytes, 
                                   State.double_size, 0);

                break;
            case MONT_RSA:
            case SQUARED_RSA:
                try {
                    misc_host.set_size(short_byte_size, i + mont_digits_bytes, 
                                       State.double_size, i);
                }
                catch(Card_response_error e) {
                    if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE)
                        {
                            out.format("## Size %d not supported\n", i);
                        }
                    else if(e.response_code() ==
                            Response_status.OV_RSA_EXP_FAILURE)
                        {
                            out.format("#### Size %d not supported ", 
                                       "(exception when setting exponent)\n",
                                       i);
                        }
                    else {
                        throw e;
                    }
                    continue;
                }
                break;
            default:
                assert false;
            }

            BigInteger mod = null;
            if(State.keep_modulus.ref) {
                int mod_bit_size, minimal_mod_bit_size;
                switch(variant) {
                case PURE_JAVA:
                case MONT_RSA:
                    mod_bit_size = i * 8;
                    minimal_mod_bit_size = mod_bit_size - 7;
                    break;
                case SQUARED_RSA:
                    // For squared multiplication the highest bit must be
                    // empty. For squared 4 multiplication the highest two
                    // bits must empty. 
                    #ifdef USE_SQUARED_RSA_MULT_4
                        mod_bit_size = i * 8 - 2;
                    #else
                        mod_bit_size = i * 8 - 1;
                    #endif
                    minimal_mod_bit_size = (i - 1) * 8 + 1;
                    break;
                default:
                    mod_bit_size = -1;
                    minimal_mod_bit_size = -1;
                    assert false;
                }
                do {
                    mod = new BigInteger(mod_bit_size, rand);
                } while(mod.bitLength() < minimal_mod_bit_size);
                // make mod odd
                mod = mod.setBit(0);
                // If square 4 mult is used the modulus must be
                // equivalent to 1 modulo 4
                #ifdef USE_SQUARED_RSA_MULT_4
                    mod = mod.clearBit(1);
                #endif
            }

            rounds_loop: 
            for(int j = 0; j < State.rounds.ref; j++) {
                switch(variant) {
                case PURE_JAVA:
                    vector_exp_measure_size(mod,
                                            rand, 
                                            short_bit_size, 
                                            short_byte_size, 
                                            i,
                                            i + mont_digits_bytes,
                                            pre_computed_factors,
                                            variant,
                                            false);
                    break;
                case MONT_RSA:
                case SQUARED_RSA:
                    try {
                        vector_exp_measure_size(mod,
                                                rand, 
                                                short_bit_size, 
                                                short_byte_size, 
                                                i,
                                                i + mont_digits_bytes,
                                                pre_computed_factors,
                                                variant,
                                                j > 0 && mod != null);
                    }
                    catch(Card_response_error e) {
                        if(e.response_code() == 
                           Response_status.OV_RSA_EXP_FAILURE)
                            {
                                out.format("## Size %d not supported " +
                                           "(exception when setting " +
                                           "exponent)\n",
                                           i);
                            }
                        else {
                            throw e;
                        }
                        break rounds_loop;
                    }
                    break;
                default:
                    assert false;
                }
            }
        }

        out.format("################# %s power perf finished\n", variant);
    }


    //########################################################################
    //########################################################################
    // 
    // run methods
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Exception wrapper for {@link #vector_exp_check
     * vector_exp_check}. Checks the given multi-power method and
     * prints traces for all escaping exceptions. If one check fails
     * the program is immediately terminated.
     * 
     * @param variant the multi-power variant to check
     */
    public void run_vector_exp_check(Vector_exponent_variant variant) {
        try {
            out.format("################# %s vector exponent start\n", 
                       variant);
            vector_exp_check(variant);
            out.format("################# %s vector exponent finished\n", 
                       variant);
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 9)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 9)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #vector_exp_measure
     * vector_exp_measure}. Measures the given multi-power
     * implementation over a range of sizes and prints traces for
     * escaping exceptions. If one computation on the card yields a
     * wrong result, the program is terminated.
     * 
     * @param variant the multi-power variant to measure
     */
    public void run_vector_exp_perf(Vector_exponent_variant variant) {
        try {
            vector_exp_measure(variant);
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 9)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 9)
                e.printStackTrace();
        }
    }
}
