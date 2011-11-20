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
// Created 1.9.08 by Hendrik
// 
// measure modular multiplication
// 
// $Id: Performance_mult_host.java,v 1.49 2010-02-18 12:40:39 tews Exp $

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
import ds.ov2.test.Bignat_protocols_stubs.Subtract_result_result;


/** 
 * Correctness check and performance measurements for various methods
 * of the bignat library. This class started as correctness check and
 * performance measusurement tool for multiplication. Now the class
 * name is just misleading. This class checks and measures:
 * <UL>
 * <LI>Montgomery multiplication and demontgomerization</LI>
 * <LI>Division</LI>
 * <LI>RSA exponent (computing exponents via the RSA NOPAD
 * cipher)</LI>
 * <LI>squared multiplication {@link Bignat#squared_rsa_mult_2
 * Bignat.squared_rsa_mult_2}</LI>
 * <LI>short squared multiplication {@link
 * Bignat#short_squared_rsa_mult_2 Bignat.short_squared_rsa_mult_2}</LI>
 * <LI>squared 4 multiplication {@link Bignat#squared_rsa_mult_4
 * Bignat.squared_rsa_mult_4}</LI>
 * <LI>short square 4 multiplication {@link
 * Bignat#short_squared_rsa_mult_4 Bignat.short_squared_rsa_mult_4}</LI>
 * <LI>Addition</LI>
 * <LI>Subtraction</LI>
 * <LI>normal multiplication {@link Bignat#mult Bignat.mult}</LI>
 * </UL>
 * This class contains the complete host driver code for all protocols
 * defined in Bignat_protcols.id. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.49 $
 * @commitdate $Date: 2010-02-18 12:40:39 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
class Performance_mult_host {

    /**
     * 
     * The output channel for test and performance data. Initialized
     * to {@link System#out} in the constructor.
     */
    private PrintWriter out;


    /**
     * 
     * Instance for changing the bignat sizes on the card.
     */
    private Misc_protocols_host misc_host;


    /**
     * 
     * Instance with the protocol-step method stubs.
     */
    private Bignat_protocols_stubs bignat_stubs;


    /**
     * 
     * The channel to the applet.
     */
    private CardChannel card_channel;


    /**
     * 
     * Construct a new test object. Creates the stub objects and
     * initializes all the fields. 
     */
    public Performance_mult_host(Test_protocols test_protocols, 
                                 CardChannel cc) {
        card_channel = cc;
        out = new PrintWriter(System.out, true);
        misc_host = new Misc_protocols_host(test_protocols, cc, out);
        PrintWriter stub_out = State.verbosity.ref >= 10 ? out : null;
        bignat_stubs = 
            new Bignat_protocols_stubs(test_protocols.bignat_protocols,
                                       stub_out, 
                                       State.apduscript.ref);
    }


    //########################################################################
    //########################################################################
    // 
    // Montgomery multiplication
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Combined result record for the Montgomery multiplication,
     * demontgomerization, squared multiplication, squared 4
     * multiplication and additon
     * protocols.
     */
    private class Mult_call_result {

        /** Duration of the first step in nanoseconds. */
        public final long duration_1;

        /** Duration of the second step in nanoseconds. */
        public final long duration_2;

        /** Result of the card. */
        public final BigInteger result;

        /** Initialize the record. */
        public Mult_call_result(long duration_1, long duration_2, 
                                BigInteger result) {
            this.duration_1 = duration_1;
            this.duration_2 = duration_2;
            this.result = result;
        }
    }

    
    /**
     * 
     * Run the mont_mult protocol (Montgomery multiplication on the card).
     * Sends the factors {@code f1} and {@code f2} and the modulus
     * {@code m} in step mont_mult_init to the card. Performs {@code
     * rounds_1} montgomery multiplications in step mont_mult_1 and {@code
     * rounds_2} rounds in step mont_mult_2. Finally queries the result
     * with the mont_mult_result step.
     * <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 900
     * milliseconds). 
     * 
     * @param f1 first factor
     * @param f2 second factor
     * @param m modulus
     * @param rounds_1 number of multiplications to perform in the
     * mont_mult_1 step
     * @param rounds_2 number of multiplications to perform in the
     * mont_mult_2 step
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_mont_mult(BigInteger f1, BigInteger f2, 
                                           Host_modulus m, 
                                           int rounds_1, int rounds_2) 
        throws CardException
    {
        int sleep = 900;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.mont_mult_init_call(card_channel, f1, f2, m);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.mont_mult_1_call(card_channel, rounds_1);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.mont_mult_2_call(card_channel, rounds_2);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.mont_mult_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }


    //########################################################################
    // demontgomerization call
    // 


    /**
     * 
     * Run the demontgomerize protocol (Demontgomerize a number on the
     * card). Sends {@code bi} and {@code m} in step demont_init to
     * the card, performs {@code rounds_1} demontgomerizations in step
     * demont_1, another {@code rounds_2} demontgomerizations in step
     * demont_2 and gets the demontgomerized result in step
     * demont_result. <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 900
     * milliseconds). 
     * 
     * @param bi number to demontgomerize
     * @param m modulus
     * @param rounds_1 number of demontgomerizations in step demont_1
     * @param rounds_2 number of demontgomerizations in step demont_2
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_demontgomerize(BigInteger bi,
                                                Host_modulus m, 
                                                int rounds_1, int rounds_2) 
        throws CardException
    {
        int sleep = 900;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.demont_init_call(card_channel, bi, m);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.demont_1_call(card_channel, rounds_1);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.demont_2_call(card_channel, rounds_2);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.demont_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }



    //########################################################################
    // Measure Montgomery multiplication
    // 

    /**
     * 
     * Performs one combined Montgomery multiplication and
     * demontgomerization check and performance measurement. If 3 user
     * provided inputs are available, the factors and the modulus are
     * taken from them (see {@link State#fix_inputs}). Otherwise
     * random numbers are generated of size {@code bignat_size} for
     * the factors and the modulus. The factors are montgomerized and
     * then multiplied on the card (with the mont_mult protocol) and
     * the result is then demontgomerized on the card (with the
     * demontgomerize protocol). <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined results of the
     * two protocols are given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before.
     * 
     * @param rand Randomness source
     * @param bignat_size the size of the factors and the modulus
     * without Montgomery digits
     * @param mult_rounds_1 number of multiplications in step mont_mult_1
     * @param mult_rounds_2 number of multiplications in step mont_mult_2
     * @param demont_rounds_1 number of demontgomerizations in step
     * demont_1 
     * @param demont_rounds_2 number of demontgomerizations in step
     * demont_2 
     * @return the combined results of the mont_mult and the demontgomerize
     * protocol in this order
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result[] mult_perf_once(Random rand, int bignat_size, 
                                             int mult_rounds_1, 
                                             int mult_rounds_2,
                                             int demont_rounds_1,
                                             int demont_rounds_2) 
        throws CardException
    {
        int mont_bignat_size = bignat_size + 2 * Bignat.size_multiplier;
        int max_bits = 8 * bignat_size;

        BigInteger f1, f2, m;

        if(State.fix_inputs.has_n_inputs(3, "mont_mult check")) {
            f1 = State.fix_inputs.pop();
            f2 = State.fix_inputs.pop();
            m = State.fix_inputs.pop();
        }
        else {
            f1 = new BigInteger(max_bits, rand);
            f2 = new BigInteger(max_bits, rand);
            m = new BigInteger(max_bits, rand);
            // make m odd
            m = m.setBit(0);
        }
        
        Host_modulus hmod = new Host_modulus(mont_bignat_size, m);

        BigInteger f1_mont = f1.multiply(hmod.mont_fac).mod(m);
        BigInteger f2_mont = f2.multiply(hmod.mont_fac).mod(m);

        if(State.verbosity.ref >= 5) {
            out.format("#### f1    = %s\n" +
                       "####       = %s\n" +
                       "#### f2    = %s\n" +
                       "####       = %s\n" +
                       "#### m     = %s\n" +
                       "####       = %s\n" +
                       "#### m_fak = %s\n" +
                       "####       = %s\n" +
                       "#### f1_m  = %s\n" +
                       "####       = %s\n" +
                       "#### f2_m  = %s\n" +
                       "####       = %s\n",
                       f1, BigIntUtil.to_byte_hex_string(f1),
                       f2, BigIntUtil.to_byte_hex_string(f2),
                       m, BigIntUtil.to_byte_hex_string(m),
                       hmod.mont_fac, 
                       BigIntUtil.to_byte_hex_string(hmod.mont_fac),
                       f1_mont, BigIntUtil.to_byte_hex_string(f1_mont),
                       f2_mont, BigIntUtil.to_byte_hex_string(f2_mont)
                       );
        }

        Mult_call_result res_mult, res_demont;

        try {
            if(State.verbosity.ref >= 5) 
                out.println("## montgomery mult f1 * f2 % m");

            res_mult = card_mont_mult(f1_mont, f2_mont, hmod, 
                                      mult_rounds_1, mult_rounds_2);
            if(State.verbosity.ref >= 5) 
                out.format("## res = %s\n" +
                           "##     = %s\n",
                           res_mult.result, 
                           BigIntUtil.to_byte_hex_string(res_mult.result)
                           );

            if(res_mult.result.compareTo(f1.multiply(f2_mont).mod(m)) == 0) {
                if(State.verbosity.ref >= 5) 
                    out.println("Intermediate montgomery result OK");
            }
            else {
                out.println("FAILIRE: intermediate montgomery result wrong");
            }

            if(State.verbosity.ref >= 5) 
                out.format("## demontgomerize\n");

            res_demont = card_demontgomerize(res_mult.result, hmod, 
                                             demont_rounds_1, demont_rounds_2);
            if(State.verbosity.ref >= 5) 
                out.format("## res = %s\n" +
                           "##     = %s\n",
                           res_demont.result, 
                           BigIntUtil.to_byte_hex_string(res_demont.result)
                           );
        }
        catch(CardException e){
            System.err.format("\nError at %s * %s mod %s\n",
                              f1, f2, m);
            throw e;
        }
        catch(RuntimeException e){
            System.err.format("\nError at %s * %s mod %s\n",
                              f1, f2, m);
            throw e;
        }



        boolean success = 
            res_demont.result.compareTo(f1.multiply(f2).mod(m)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s * %s mod %s\n",
                              f1, f2, m);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return new Mult_call_result[]{res_mult, res_demont};
    }


    /**
     * 
     * Montgomery multiplication correctness check. Set the bignat
     * size to {@link State#long_size} and performs {@link
     * State#rounds} multiplication and demontgomerization checks via
     * method {@link #mult_perf_once mult_perf_once}.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void mult_check() 
        throws CardException
    {
        Random rand = new Random();

        out.format("## Effective size %d bytes (%d bits)\n", 
                   State.long_size.ref,
                   State.long_size.ref * 8
                   );

        misc_host.set_long_size(State.long_size.ref 
                                + 2 * Bignat.size_multiplier);

        for(int i = 0; i < State.rounds.ref; i++)
            mult_perf_once(rand, State.long_size.ref, 1, 0, 0, 1);
    }


    /**
     * 
     * Measure Montgomery multiplication and demontgomerization
     * performance. This method runs {@link #mult_perf_once
     * mult_perf_once} once and prints the performance results of the
     * two protocol runs in a nice format for gnuplot. Uses the two
     * multiplication/demontgomerization steps to determine the
     * protocol and data transmission overhead. The measurement
     * is deduced from the timing differences between the respective
     * first and second steps.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @param size the bignat size to measure without Montgomery digits
     * @param rand Randomness source
     * @throws CardException for communication problems and protocol errors
     */
    public void mult_measure_size(int size, Random rand) 
        throws CardException
    {
        int mult_rounds_1 = 0;
        int mult_rounds_2 = 1;
        int demont_rounds_1 = 0;
        int demont_rounds_2 = 1;

        Mult_call_result[] r = mult_perf_once(rand, size, 
                                              mult_rounds_1, mult_rounds_2,
                                              demont_rounds_1, demont_rounds_2);
        Mult_call_result rmult = r[0];
        Mult_call_result rdemont = r[1];

        out.format("size %d %d mult rounds %.03f s " +
                   "%d demont rounds %.03f s " +
                   "(M %d %.03f s %d %.03f s D %d %.03f s %d %.03f s)\n",
                   size * 8, 
                   mult_rounds_2 - mult_rounds_1,
                   new Long(rmult.duration_2 - rmult.duration_1).doubleValue() /
                          (mult_rounds_2 - mult_rounds_1) / 1E9,
                   demont_rounds_2 - demont_rounds_1,
                   new Long(rdemont.duration_2 - 
                                    rdemont.duration_1).doubleValue() /
                          (demont_rounds_2 - demont_rounds_1) / 1E9,
                   mult_rounds_1,
                   rmult.duration_1 / 1.0E9,
                   mult_rounds_2,
                   rmult.duration_2 / 1.0E9,
                   demont_rounds_1,
                   rdemont.duration_1 / 1.0E9,
                   demont_rounds_2,
                   rdemont.duration_2 / 1.0E9
                   );
        return;
    }


    /**
     * 
     * Montgomery multiplication and demontgomerization performance.
     * Performs {@link State#rounds} measurments of montgomery
     * multipliation and demontgomerization for each bignat size
     * between {@link State#start_size} and {@link
     * State#long_bignat_max_size} with 4 byte increments. If {@link
     * State#start_size} is zero, starts at a very small size.
     * Each measurement is printed in a gnuplot compatible way.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void mult_measure()
        throws CardException
    {
        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; i <= State.long_bignat_max_size; i += 4) {
            out.format("## Effective size %d bytes (%d bits)\n", 
                       i, 
                       i * 8);
            misc_host.set_long_size(i + 2 * Bignat.size_multiplier);

            for(int j = 0; j < State.rounds.ref; j++) 
                mult_measure_size(i, rand);
        }
    }


    //########################################################################
    //########################################################################
    // 
    // Division
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Combined result record of the div protocol.
     */
    private class Div_call_result {

        /** Duration of the div_1 step in nanoseconds. */
        public final long duration_1;

        /** Duration of the div_2 step in nanoseconds. */
        public final long duration_2;

        /** Remainder result */
        public final BigInteger remainder;

        /** Quotient result */
        public final BigInteger quotient;

        /** Initialize the record */
        public Div_call_result(long duration_1, long duration_2, 
                               BigInteger remainder, BigInteger quotient) 
        {
            this.duration_1 = duration_1;
            this.duration_2 = duration_2;
            this.remainder = remainder;
            this.quotient = quotient;
        }
    }

    
    /**
     * 
     * Run the complete div protocol. Transfer divident and divisor to
     * the card in step div_init, perform {@code rounds_1} divitions
     * of them in step div_1 and another {@code rounds_2} divitions in
     * step div_2 and finally transfer the results back in step
     * div_result. 
     * <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 20
     * milliseconds). 
     * 
     * @param divident the divident
     * @param divisor the divisor
     * @param rounds_1 number of divitions in step div_1
     * @param rounds_2 number of divitions in step div_2
     * @return the combined div protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Div_call_result card_div(BigInteger divident, BigInteger divisor, 
                                    int rounds_1, int rounds_2) 
        throws CardException
    {
        int sleep = 20;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.div_init_call(card_channel, divident, divisor);
        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.div_1_call(card_channel, rounds_1);
        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.div_2_call(card_channel, rounds_2);
        Misc_host.gc_sleep(sleep); 
        Bignat_protocols_stubs.Div_result_result dr = 
            bignat_stubs.div_result_call(card_channel);
        return new Div_call_result(d1, d2, dr.bignats_r_1, dr.bignats_r_2);
    }


    /**
     * 
     * Performs one division check and measurement. If two user
     * provided inputs are available (see {@link State#fix_inputs})
     * divident and division are taken from there. Otherwise they are
     * randomly generated. The divident will be of size {@code size}.
     * For the divisor {@link State#divisor_length} is interpreted as
     * a percent value of {@code size} that the divisor should have.
     * <P>
     *
     * The bignat size must have been set to an appropriate value
     * before calling this method.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @param rand Randomness source
     * @param size divident size
     * @param rounds_1 number of divitions in step div_1
     * @param rounds_2 number of divitions in step div_2
     * @return the combined div protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Div_call_result div_perf_once(Random rand, int size, 
                                         int rounds_1, int rounds_2) 
        throws CardException
    {
        int max_bits = 8 * size;
        int dis_bits = (int)
            (max_bits * (State.divisor_length.ref / 100.0) + 0.5);

        BigInteger divident, divisor;

        if(State.fix_inputs.has_n_inputs(2, "div check")) {
            divident = State.fix_inputs.pop();
            divisor = State.fix_inputs.pop();
        }
        else {
            divident = new BigInteger(max_bits, rand);
            do {
                divisor = new BigInteger(dis_bits, rand);
            } while(divisor.compareTo(BigInteger.ZERO) == 0);
        }
        
        if(State.verbosity.ref >= 5) {
            out.format("#### div = %s\n" +
                       "####     = %s\n" +
                       "#### dis bits = %d\n" +
                       "#### dis = %s\n" +
                       "####     = %s\n",
                       divident, BigIntUtil.to_byte_hex_string(divident),
                       dis_bits,
                       divisor, BigIntUtil.to_byte_hex_string(divisor)
                       );
        }

        Div_call_result res;

        try {
            if(State.verbosity.ref >= 5) 
                out.println("## div / dis");
            res = card_div(divident, divisor, rounds_1, rounds_2);
            if(State.verbosity.ref >= 5) 
                out.format("## qot = %s\n" +
                           "##     = %s\n" +
                           "## rem = %s\n" +
                           "##     = %s\n",
                           res.quotient, 
                           BigIntUtil.to_byte_hex_string(res.quotient),
                           res.remainder, 
                           BigIntUtil.to_byte_hex_string(res.remainder)
                           );
        }
        catch(CardException e){
            System.err.format("\nError at %s / %s\n",
                              divident, divisor);
            throw e;
        }
        catch(RuntimeException e){
            System.err.format("\nError at %s / %s\n",
                              divident, divisor);
            throw e;
        }


        boolean success = 
            (res.quotient.compareTo(divident.divide(divisor)) == 0) &&
            (res.remainder.compareTo(divident.remainder(divisor)) == 0);

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s / %s\n",
                              divident, divisor);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res;
    }


    /**
     * 
     * Correctness check for division. Performs {@link State#rounds}
     * runs of the div protocol for size {@link State#long_size}. 
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void div_check() 
        throws CardException
    {
        Random rand = new Random();

        out.format("## Size %d\n", State.long_size.ref);

        misc_host.set_long_size(State.long_size.ref);

        for(int i = 0; i < State.rounds.ref; i++)
            div_perf_once(rand, State.long_size.ref, 1, 0);
    }


    /**
     * 
     * Measure and print performance of one div protocol run. The
     * print is gnuplot compatible. Uses the two division steps to
     * determine the protocol and data transmission overhead: The
     * measurement is deduced from the timing difference of the div_1
     * and the div_2 steps.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @param rand Randomness source
     * @param size divident size
     * @param rounds_1 number of divitions in step div_1
     * @param rounds_2 number of divitions in step div_2
     * @return the compined div protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Div_call_result div_measure_size(int size, Random rand,
                                            int rounds_1, int rounds_2) 
        throws CardException
    {
        Div_call_result r = div_perf_once(rand, size, rounds_1, rounds_2);
        long time = r.duration_2 - r.duration_1;
        
        out.format("size %d %d rounds %.03f s (%d %.03f s %d %.03f s)\n",
                   size * 8, 
                   rounds_2 - rounds_1,
                   new Long(time).doubleValue() /
                                  (rounds_2 - rounds_1) / 1E9,
                   rounds_1,
                   new Long(r.duration_1).doubleValue()  / 1E9,
                   rounds_2,
                   new Long(r.duration_2).doubleValue()  / 1E9
                   );
        return r;
    }


    /**
     * 
     * Division performance. Measures {@link State#rounds} runs of the
     * div protocol for each size between {@link State#start_size} and
     * {@link State#long_size} with size increments of 4 bytes. If
     * {@link State#start_size} is zero, a very small start size is
     * used. Prints all data in gnuplot compatible format. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void div_measure()
        throws CardException
    {
        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;
        int rounds_1 = 1;
        int rounds_2 = 6;

        for(int i = start; i <= State.long_size.ref; i += 4) {
            out.format("#### Size %d\n", i);
            misc_host.set_long_size(i);

            for(int j = 0; j < State.rounds.ref; j++) {
                Div_call_result r = 
                    div_measure_size(i, rand, rounds_1, rounds_2);
                if(r.duration_2 / 1E9 > 100) {
                    assert !(rounds_1 == 0 && rounds_2 == 1);
                    rounds_2 -= 1;
                    if(rounds_2 == 1 && rounds_1 == 1)
                        rounds_1 = 0;
                }
            }
        }
    }


    //########################################################################
    //########################################################################
    // 
    // RSA exponent
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Combined result record for the RSA_exp protocol.
     */
    public class RSA_exp_result {

        /** 
         * Duration of the rsa_exp_parts_empty step in nanoseconds.
         * This step measures the protocol overhead for calling the
         * {@link ds.ov2.bignat.RSA_exponent#fixed_power
         * RSA_exponent.fixed_power} method on the card.
         */
        public final long duration_parts_empty;

        /**
         * Duration of the rsa_exp_parts_exp step in nanoseconds. This
         * step performs only the RSA encryption (method {@link
         * ds.ov2.bignat.RSA_exponent#fixed_power
         * RSA_exponent.fixed_power} on the card). Key
         * initialization for setting exponent and modulus has been
         * done in the rsa_exp_init step.
         */
        public final long duration_parts_exp;

        /**
         * Duration of the rsa_exp_full_empty step in nanoseconds.
         * This step measures the protocol overhead for calling the
         * {@link ds.ov2.bignat.RSA_exponent#power
         * RSA_exponent.power} method on the card.
         */
        public final long duration_full_empty;

        /**
         * Duration of the rsa_exp_full_exp step in nanoseconds. This
         * step measures one exponent computation (method {@link
         * ds.ov2.bignat.RSA_exponent#power RSA_exponent.power}
         * on the card) with setting the exponent but without setting
         * the modulus.
         */
        public final long duration_full_exp;

        /** The result produced in the rsa_exp_parts_exp step. */
        public final BigInteger parts_result;

        /** The result produced in the rsa_exp_full_exp step. */
        public final BigInteger full_result;

        /** Initialize the record. */
        public RSA_exp_result(long duration_parts_empty, 
                              long duration_parts_exp, 
                              long duration_full_empty, 
                              long duration_full_exp,
                              BigInteger parts_result, 
                              BigInteger full_result) 
        {
            this.duration_parts_empty = duration_parts_empty;
            this.duration_parts_exp = duration_parts_exp;
            this.duration_full_empty = duration_full_empty;
            this.duration_full_exp = duration_full_exp;
            this.parts_result = parts_result;
            this.full_result = full_result;
        }
    }


    /**
     * 
     * Run the RSA_exp protocol. The init step transfers the base, the
     * exponent and the modulus to the card. There the modulus and the
     * exponent are installed in the public key. The second step
     * measures the overhead for calling {@link
     * ds.ov2.bignat.RSA_exponent#fixed_power
     * RSA_exponent.fixed_power}, the third step calls that method
     * to just do the encryption. The fourth step measures the
     * overhead for {@link ds.ov2.bignat.RSA_exponent#power
     * RSA_exponent.power}, the fifth step calls that method to
     * compute the power (install the exponent and encrypt). The last
     * step transfers the results back. <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 100
     * milliseconds). 
     * 
     * @param base the base
     * @param exp the exponent
     * @param mod the modulus
     * @return combined results for the RSA_exp protocol
     * @throws CardException for communication problems and protocol errors
     */
    public RSA_exp_result rsa_exp_card(BigInteger base, 
                                       BigInteger exp,
                                       BigInteger mod)
        throws CardException
    {
        int sleep = 100;
        Misc_host.gc_sleep(sleep);
        bignat_stubs.rsa_exp_init_call(card_channel, base, exp, mod);
        Misc_host.gc_sleep(sleep);
        long d_parts_empty = 
            bignat_stubs.rsa_exp_parts_empty_call(card_channel);
        Misc_host.gc_sleep(sleep);
        long d_parts_full = bignat_stubs.rsa_exp_parts_exp_call(card_channel);
        Misc_host.gc_sleep(sleep);
        long d_full_empty = bignat_stubs.rsa_exp_full_empty_call(card_channel);
        Misc_host.gc_sleep(sleep);
        long d_full_exp = bignat_stubs.rsa_exp_full_exp_call(card_channel);
        Misc_host.gc_sleep(sleep);
        Bignat_protocols_stubs.Rsa_exp_result_result r = 
            bignat_stubs.rsa_exp_result_call(card_channel);
        return new RSA_exp_result(d_parts_empty, d_parts_full,
                                  d_full_empty, d_full_exp,
                                  r.bignats_r_1, r.bignats_r_2);
    }


    /**
     * 
     * Performs one RSA exponent check and measurement. If three user
     * provided inputs are available (see {@link State#fix_inputs})
     * base, exponent and modulus are taken from there. Otherwise they
     * are randomly generated. If an exponent has been fixed with
     * {@code -fixed-exponent} that exponent is used instead of a
     * randomly generated one.
     * <P>
     *
     * For the application in the selective disclosure protocols, the
     * modulus and the base should have the same size and the exponent
     * should be shorter. However for performance measurements it
     * would be nice to permit the exponent to be longer than the
     * base. On the card the exponent must be smaller than the key
     * size. Further, the modulus must have key-size many significant
     * bytes. If the first byte in the modulus is zero the jcop
     * emulator and the cards that I tested crash.
     * <P>
     *
     * As solution we have three sizes here. {@code base_bignat_size}
     * equals the key size plus two Montgomery digits. It is given in
     * bytes. {@code effective_long_size} is the size of the base in
     * bytes which should not be greater than {@code base_bignat_size}
     * minus the Montgomery digits. {@code short_bit_size} is the size
     * of the exponent in bits. 
     * <P>
     *
     * The bignat size must have been set to an appropriate value
     * before calling this method.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @param rand Randomness source
     * @param short_bit_size size of the exponent in bits
     * @param effective_long_size size of the base in bytes
     * @param base_bignat_size size of the modulus in bytes plus two
     * montgomery digits (that is, to obtain the size of the modulus
     * 2 * {@link Bignat#size_multiplier} must be subtracted from
     * {@code base_bignat_size})
     * @return the combined RSA_exp protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public RSA_exp_result rsa_exp_once(Random rand, 
                                       int short_bit_size,
                                       int effective_long_size,
                                       int base_bignat_size)
        throws CardException
    {
        int long_bit_size = 8 * effective_long_size;
        
        // The first byte of the modulus must have a 1 bit, otherwise
        // the jcop cards and the jcop emulator crash. Therefore,
        // the modulus must be generated according to base_bignat_size.
        int mod_bit_size = 8 * base_bignat_size;

        BigInteger base, exp, mod;

        if(State.fix_inputs.has_n_inputs(3, "rsa exp")) {
            base = State.fix_inputs.pop();
            exp = State.fix_inputs.pop();
            mod = State.fix_inputs.pop();
        }
        else {
            base = new BigInteger(long_bit_size, rand);
            if(State.fixed_exponent.ref != null)
                exp = new BigInteger(State.fixed_exponent.ref);
            else
                exp = new BigInteger(short_bit_size, rand);

            do {
                mod = new BigInteger(mod_bit_size, rand);
            } while(mod.bitLength() <= mod_bit_size -8);

            // make m odd
            mod.setBit(0);
        }

        BigInteger expected_result = base.modPow(exp, mod);
        if(State.verbosity.ref >= 5) 
            out.format("## base %s\n" +
                       "## exp  %s\n" +
                       "## mod %s\n" +
                       "## res  %s\n",
                       BigIntUtil.to_byte_hex_string(base), 
                       BigIntUtil.to_byte_hex_string(exp),
                       BigIntUtil.to_byte_hex_string(mod),
                       BigIntUtil.to_byte_hex_string(expected_result));

        RSA_exp_result res;

        try {
            res = rsa_exp_card(base, exp, mod);
        }
        catch(Card_response_error e) {
            if(e.response_code() != Response_status.OV_RSA_KEY_FAILURE &&
               e.response_code() != Response_status.OV_RSA_EXP_FAILURE)
                {
                    System.err.format("\nError at %s ^ %s mod %s\n",
                                      base, exp, mod);
                }
            throw e;
        }

        if(State.verbosity.ref > 4)
            out.format("## parts res %s\n" +
                       "## full res  %s\n",
                       BigIntUtil.to_byte_hex_string(res.parts_result),
                       BigIntUtil.to_byte_hex_string(res.full_result));

        boolean success =
            expected_result.compareTo(res.parts_result) == 0 &&
            expected_result.compareTo(res.full_result) == 0;

        if(success && State.verbosity.ref > 4)
            out.format("## Success\n");

        if(!success && State.verbosity.ref < 5)
            System.err.format("\nError at %s ^ %s mod %s\n",
                              base, exp, mod);

        if(!success && State.verbosity.ref > 4)
            out.println("## Failure");

        if(!success)
            System.exit(1);

        return res;
    }


    /**
     * 
     * Correctness check for the RSA exponent. Performs {@link
     * State#rounds} tests with sizes derived from {@link
     * State#long_size}. 
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void rsa_exp_check()
        throws CardException
    {
        Random rand = new Random();

        int short_bit_size = 
            State.make_short_bit_size(State.long_size.ref);
        int short_byte_size = State.make_short_size(State.long_size.ref);

        // It can happen that the "short" exponent
        // size is greater than the "long" base size. With such absurd
        // settings the applet will break in various places. We 
        // therefore leave the effective_long_size as it is 
        // (so the base will have at most effective_long_size 
        // significant bits) but make the base Bignat at least as
        // big as the exponent. This is done via the following variable.
        int base_bignat_size = 
            short_byte_size <= State.long_size.ref ? State.long_size.ref 
            : short_byte_size;

        out.format("## Effective size base %d bytes (%d bits in %d bytes) " +
                   "exponent %d bits (in %d bytes)\n",
                   State.long_size.ref, 
                   State.long_size.ref * 8,
                   base_bignat_size,
                   short_bit_size,
                   short_byte_size);

        misc_host.set_size(short_byte_size, base_bignat_size, 
                           State.double_size, base_bignat_size);
        for(int i = 0; i < State.rounds.ref; i++)
            rsa_exp_once(rand, short_bit_size, 
                         State.long_size.ref, 
                         base_bignat_size);
    }


    /**
     * 
     * Measure the computation of one power via RSA encryption and
     * print the timings in gnuplot compatible way. For an explanation
     * about the various size parameters, see {@link #rsa_exp_once
     * rsa_exp_once}. 
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @param rand Randomness source
     * @param short_bit_size size of the exponent in bits
     * @param effective_long_size size of the base in bytes
     * @param base_bignat_size size of the modulus in bytes plus two
     * montgomery digits (that is, to obtain the size of the modulus
     * 2 * {@link Bignat#size_multiplier} must be subtracted from
     * {@code base_bignat_size})
     * @throws CardException for communication problems and protocol errors
     */
    public void rsa_exp_measure_size(Random rand, 
                                     int short_bit_size,
                                     int effective_long_size,
                                     int base_bignat_size)
        throws CardException
    {
        RSA_exp_result r = rsa_exp_once(rand, short_bit_size, 
                                        effective_long_size, 
                                        base_bignat_size);

        out.format("size %d ^ %d rsa exp parts %.03f s full %.03f s " +
                   "(%.03f s %.03f s %.03f s %.03f s)\n",
                   8 * effective_long_size,
                   short_bit_size,
                   (r.duration_parts_exp - r.duration_parts_empty) / 1E9,
                   (r.duration_full_exp - r.duration_full_empty) / 1E9,
                   r.duration_parts_empty / 1E9,
                   r.duration_parts_exp / 1E9,
                   r.duration_full_empty / 1E9,
                   r.duration_full_exp / 1E9);
        return;
    }


    /**
     * 
     * Measure the RSA exponent. Start at {@link State#start_size} and
     * perform {@link State#rounds} test for each size with 2 byte
     * increments. If {@link State#start_size} is 0 a sufficiently
     * small default start size is used. The exponent length will be
     * computed according to the estimations of Lenstra, see {@link
     * ds.ov2.util.Security_parameter}, except for the case that {@link
     * State#short_bit_size} has been set explicitely. Then this
     * exponent size is used for all measurements. 
     * <P>
     *
     * If the card throws one of the known exceptions for an
     * unsupported key size then this size is dropped and the
     * measurement continues with the next size.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void rsa_exp_measure()
        throws CardException
    {
        Random rand = new Random();

        out.println("################# rsa exp perf start");

        int start = State.start_size.ref > 0 ? State.start_size.ref 
                                         : 4 * Bignat.size_multiplier;

        for(int i = start; i <= State.long_bignat_max_size; 
            i += 2 * Bignat.size_multiplier) {
            int short_bit_size = State.make_short_bit_size(i);
            int short_byte_size = State.make_short_size(i);

            // For measurements it can happen that the "short" exponent
            // size is greater than the "long" base size. With such absurd
            // settings the applet will break in various places. We 
            // therefore leave the effective_long_size as it is 
            // (so the base will have at most effective_long_size 
            // significant bits) but make the base Bignat at least as
            // big as the exponent. This is done via the following variable.
            int base_bignat_size = short_byte_size <= i ? i : short_byte_size;

            out.format("## Effective size base %d bytes " +
                       "(%d bits in %d bytes) " +
                       "exponent %d bits (in %d bytes)\n", 
                       i,
                       i * 8, 
                       base_bignat_size,
                       short_bit_size,
                       short_byte_size);

            try {
                misc_host.set_size(short_byte_size, 
                                   base_bignat_size, 
                                   State.double_size,
                                   base_bignat_size);
            }
            catch(Card_response_error e) {
                if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE) 
                    {
                        out.format("## Size %d not supported\n", 
                                   base_bignat_size);
                    }
                else if(e.response_code() == Response_status.OV_RSA_EXP_FAILURE)
                    {
                        out.format("#### Size %d not supported " +
                                   "(exception when setting exponent)\n", 
                                   base_bignat_size);
                    }
                else {
                    throw e;
                }
                continue;
            }



            for(int j = 0; j < State.rounds.ref; j++) {
                try {
                    rsa_exp_measure_size(rand, short_bit_size, 
                                         i, base_bignat_size);
                }
                catch(Card_response_error e) {
                    if(e.response_code() == Response_status.OV_RSA_EXP_FAILURE)
                        {
                            out.format("## Size %d not supported " +
                                       "(exception when setting exponent)\n",
                                       base_bignat_size);
                        }
                    else
                        throw e;
                    break;
                }
                // Give the card some rest.
                Misc_host.sleep(500);
            }
        }

        out.println("################# rsa exp perf finished");
    }


    //########################################################################
    //########################################################################
    // 
    // Squared multiplication
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Run the squared mult protocol ({@link Bignat#squared_rsa_mult_2
     * Bignat.squared_rsa_mult_2}). Sends the factors {@code f1} and
     * {@code f2} and the modulus {@code m} in step sq_mult_init to
     * the card. Performs {@code rounds_1} multiplications
     * in step mult_1 and {@code rounds_2} rounds in step mult_2.
     * Finally queries the result with the sq_mult_result step. <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 1
     * milliseconds). 
     * 
     * @param f1 first factor
     * @param f2 second factor
     * @param m modulus
     * @param rounds_1 number of multiplications to perform in the
     * mult_1 step
     * @param rounds_2 number of multiplications to perform in the
     * mult_2 step
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_sq_mult(BigInteger f1, BigInteger f2, 
                                         Host_modulus m, 
                                         int rounds_1, int rounds_2) 
        throws CardException
    {
        int sleep = 1;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.sq_mult_init_call(card_channel, f1, f2, m);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.sq_mult_1_call(card_channel, rounds_1);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.sq_mult_2_call(card_channel, rounds_2);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.sq_mult_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }


    /**
     * 
     * Checks and measures squared multiplication on the card ({@link
     * Bignat#squared_rsa_mult_2 Bignat.squared_rsa_mult_2}). If 3 user
     * provided inputs are available, the factors and the modulus are
     * taken from them (see {@link State#fix_inputs}). Otherwise, if
     * the arguments {@code x} and {@code y} are non-null they are
     * taken as factors with a randomly generated modulus of size
     * {@code long_bignat_size}. Otherwise random numbers are
     * generated of size {@code bignat_size} for the factors and the
     * modulus. The factors are then multiplied on the card with the
     * squared mult protocol. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined result of the
     * protocol is given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before.
     * 
     * @param rand Randomness source
     * @param bignat_size the size of the factors and the modulus
     * @param mult_rounds_1 number of multiplications in step sq_mult_1
     * @param mult_rounds_2 number of multiplications in step sq_mult_2
     * @param x first factor when non-null
     * @param y first factor when non-null
     * @return the combined result of the squared mult protocol
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result sq_mult_perf_once(Random rand, int bignat_size, 
                                              int mult_rounds_1, 
                                              int mult_rounds_2,
                                              BigInteger x,
                                              BigInteger y) 
        throws CardException
    {
        int max_bits = 8 * bignat_size;
        // leave the highest bit empty
        max_bits--;

        BigInteger f1, f2, m;

        if(State.fix_inputs.has_n_inputs(3, "squared mult check")) {
            f1 = State.fix_inputs.pop();
            f2 = State.fix_inputs.pop();
            m = State.fix_inputs.pop();
        }
        else if(x != null && y != null) {
            f1 = x;
            f2 = y;
            do {
                m = new BigInteger(max_bits, rand);
            } while(m.bitLength() <= (bignat_size - 1) * 8);
            // make m odd
            m = m.setBit(0);
        }
        else {
            f1 = new BigInteger(max_bits, rand);
            f2 = new BigInteger(max_bits, rand);
            do {
                m = new BigInteger(max_bits, rand);
            } while(m.bitLength() <= (bignat_size - 1) * 8);
            // make m odd
            m = m.setBit(0);
        }
        
        Host_modulus hmod = new Host_modulus(bignat_size, m);

        if(State.verbosity.ref >= 5) {
            out.format("#### f1    = %s\n" +
                       "####       = %s\n" +
                       "#### f2    = %s\n" +
                       "####       = %s\n" +
                       "#### m     = %s\n" +
                       "####       = %s\n",
                       f1, BigIntUtil.to_byte_hex_string(f1),
                       f2, BigIntUtil.to_byte_hex_string(f2),
                       m, BigIntUtil.to_byte_hex_string(m)
                       );
        }

        Mult_call_result res_mult;

        try {
            if(State.verbosity.ref >= 5) 
                out.println("## squared mult f1 * f2 % m");

            res_mult = card_sq_mult(f1, f2, hmod, 
                                    mult_rounds_1, mult_rounds_2);
            if(State.verbosity.ref >= 5) 
                out.format("## res = %s\n" +
                           "##     = %s\n",
                           res_mult.result, 
                           BigIntUtil.to_byte_hex_string(res_mult.result)
                           );
        }
        catch(Response_apdu.Card_response_error e){
            if(e.response_code() != Response_status.OV_RSA_KEY_FAILURE &&
               e.response_code() != Response_status.OV_RSA_EXP_FAILURE)
                {
                    System.err.format("\nError at %s * %s mod %s\n",
                                      f1, f2, m);
                }
            throw e;
        }
        catch(CardException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, m);
            throw e;
        }
        catch(RuntimeException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, m);
            throw e;
        }

        boolean success = 
            res_mult.result.compareTo(f1.multiply(f2).mod(m)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s * %s mod %s\n",
                              f1, f2, m);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res_mult;
    }


    /**
     * 
     * Squared multiplication correctness check. Set the bignat
     * size to {@link State#long_size} and performs {@link
     * State#rounds} squared multiplications checks via
     * method {@link #sq_mult_perf_once sq_mult_perf_once}.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void sq_mult_check() 
        throws CardException
    {
        Random rand = new Random();

        out.format("## Effective size %d bytes (%d bits)\n", 
                   State.long_size.ref,
                   State.long_size.ref * 8
                   );

        try {
            misc_host.set_long_cipher_size(State.long_size.ref, 
                                           State.long_size.ref);
        }
        catch(Card_response_error e) {
            if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE) {
                out.format("## Size %d not supported\n", State.long_size.ref);
            }
            throw e;
        }

        BigInteger[][] corner_cases = null;
        if(State.check_corner_cases.ref)
            corner_cases = make_mult_corner_cases(State.long_size.ref * 8 - 1, 
                                                  rand);

        for(int i = 0; i < State.rounds.ref; i++) {
            BigInteger x = null;
            BigInteger y = null;
            if(State.check_corner_cases.ref && i < corner_cases.length) {
                x = corner_cases[i][0];
                y = corner_cases[i][1];
            }

            try {
                sq_mult_perf_once(rand, State.long_size.ref, 1, 0, x, y);
            }
            catch(Card_response_error e) {
                if(e.response_code() == Response_status.OV_RSA_EXP_FAILURE) {
                    out.format("## Size %d not supported " +
                               "(exception when setting exponent)\n", 
                               State.long_size.ref);
                }
                throw e;
            }
        }
    }


    /**
     * 
     * Squared multiplication performance. Performs {@link
     * State#rounds} measurments of squared multipliation for each
     * bignat size between {@link State#start_size} and {@link
     * State#long_bignat_max_size} with 4 byte increments. If {@link
     * State#start_size} is zero, starts at a very small size. Each
     * measurement is printed in a gnuplot compatible way. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void sq_mult_measure()
        throws CardException
    {
        int mult_rounds_1 = 0;
        int mult_rounds_2 = 1;

        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; i <= State.long_bignat_max_size; i += 4) {
            out.format("## Effective size %d bytes (%d bits)\n", i, i * 8 - 1);

            BigInteger[][] corner_cases = null;
            if(State.check_corner_cases.ref)
                corner_cases = make_mult_corner_cases(i * 8 - 1, rand);

            try {
                misc_host.set_long_cipher_size(i, i);

                for(int j = 0; j < State.rounds.ref; j++) {
                    BigInteger x = null;
                    BigInteger y = null;
                    if(State.check_corner_cases.ref 
                       && j < corner_cases.length) 
                        {
                            x = corner_cases[j][0];
                            y = corner_cases[j][1];
                        }

                    Mult_call_result result = sq_mult_perf_once(rand, i, 
                                                                mult_rounds_1, 
                                                                mult_rounds_2,
                                                                x, y);

                    out.format("size %d %d sq_mult rounds %.03f s " +
                               "( %d %.03f s %d %.03f s )\n",
                               i * 8 - 1, 
                               mult_rounds_2 - mult_rounds_1,
                               new Long(result.duration_2 - 
                                        result.duration_1).doubleValue() /
                               (mult_rounds_2 - mult_rounds_1) / 1E9,
                               mult_rounds_1,
                               result.duration_1 / 1.0E9,
                               mult_rounds_2,
                               result.duration_2 / 1.0E9
                               );
                }
            }
            catch(Card_response_error e) {
                if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE) {
                    out.format("## Size %d not supported\n", i);
                }
                else if(e.response_code() == 
                        Response_status.OV_RSA_EXP_FAILURE) 
                    {
                        out.format("## Size %d not supported " +
                                   "(exception when setting exponent)\n", 
                                   i);
                    }
                else {
                    throw e;
                }
                continue;
            }

        }
    }


    //########################################################################
    //########################################################################
    // 
    // Short squared multiplication
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Run the short squared mult protocol ({@link
     * Bignat#short_squared_rsa_mult_2 Bignat.short_squared_rsa_mult_2}).
     * Sends the factors {@code f1} and {@code f2} and the modulus
     * {@code m} in step short_sq_mult_init to the card. Performs {@code
     * rounds_1} multiplications in step mult_1 and {@code rounds_2}
     * rounds in step mult_2. Finally queries the result with the
     * sq_mult_result step. <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 1
     * milliseconds). 
     * 
     * @param f1 first factor
     * @param f2 second factor
     * @param m modulus
     * @param rounds_1 number of multiplications to perform in the
     * mult_1 step
     * @param rounds_2 number of multiplications to perform in the
     * mult_2 step
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_short_sq_mult(BigInteger f1, BigInteger f2, 
                                               Host_modulus m, 
                                               int rounds_1, int rounds_2) 
        throws CardException
    {
        int sleep = 1;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.short_sq_mult_init_call(card_channel, f1, f2, m);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.short_sq_mult_1_call(card_channel, rounds_1);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.short_sq_mult_2_call(card_channel, rounds_2);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.short_sq_mult_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }


    /**
     * 
     * Generate multiplication corner cases. Generates an array of
     * factor pairs, each pair being a multiplication corner case.
     * 
     * @param max_bits maximal number of significant bits of the
     * factors 
     * @param rand randomness source
     * @return corner cases as array of factor pairs 
     */
    public BigInteger[][] make_mult_corner_cases(int max_bits, 
                                                 Random rand) 
    {
        // Get a mask for the most significant byte.
        int first_byte_mask = 0xff;
        if(max_bits % 8 != 0)
            first_byte_mask >>= 8 - (max_bits % 8);
        
        // Round max_bits upwards to obtain the number of bytes. 
        // Add one for a leading zero byte.
        byte[] max_value = new byte[(max_bits + 7) / 8 + 1];
        max_value[0] = 0;
        max_value[1] = (byte)first_byte_mask;
        for(int i = 2; i < max_value.length; i++)
            max_value[i] = (byte)0xff;
        BigInteger max = new BigInteger(max_value);
    
        BigInteger[][] fixed_corner_cases = new BigInteger[][]
            { {BigInteger.ZERO, BigInteger.ZERO },
              {BigInteger.ZERO, new BigInteger(max_bits, rand) },
              {new BigInteger(max_bits, rand), BigInteger.ZERO },
              {BigInteger.ONE, BigInteger.ONE },
              {BigInteger.ONE, new BigInteger(max_bits, rand) },
              {new BigInteger(max_bits, rand), BigInteger.ONE },
              {max, max },
              {max, new BigInteger(max_bits, rand) },
              {new BigInteger(max_bits, rand), max }};

        int var_cases = 10;
        BigInteger[][] var_corner_cases = new BigInteger[3 * var_cases][2];

        for(int i = 0; i < var_cases; i++) {
            var_corner_cases[i][0] = 
                new BigInteger(rand.nextInt(max_bits), rand);
            var_corner_cases[i][1] = 
                new BigInteger(rand.nextInt(max_bits), rand);
        }
        for(int i = var_cases; i < 2 * var_cases; i++) {
            var_corner_cases[i][0] = 
                new BigInteger(rand.nextInt(max_bits), rand);
            var_corner_cases[i][1] = 
                new BigInteger(max_bits, rand);
        }
        for(int i = 2 * var_cases; i < 3 * var_cases; i++) {
            var_corner_cases[i][0] = 
                new BigInteger(max_bits, rand);
            var_corner_cases[i][1] = 
                new BigInteger(rand.nextInt(max_bits), rand);
        }

        BigInteger[][] res = new BigInteger[fixed_corner_cases.length +
                                            var_corner_cases.length][];

        System.arraycopy(fixed_corner_cases, 0, res, 0,
                         fixed_corner_cases.length);
        System.arraycopy(var_corner_cases, 0, 
                         res, fixed_corner_cases.length,
                         var_corner_cases.length);

        return res;     
    }



    /**
     * 
     * Checks and measures short squared multiplication on the card
     * ({@link Bignat#short_squared_rsa_mult_2
     * Bignat.short_squared_rsa_mult_2}). If 3 user provided inputs are
     * available, the factors and the modulus are taken from them (see
     * {@link State#fix_inputs}). Otherwise, if the arguments {@code
     * x} and {@code y} are non-null they are taken as factors with a
     * randomly generated modulus of size {@code long_bignat_size}.
     * Otherwise random numbers are generated of size {@code
     * short_bignat_size} for the factors and of size {@code
     * long_bignat_size} for the modulus. The factors are then
     * multiplied on the card with the short squared mult protocol.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined result of the
     * protocol is given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before.
     * <P>
     *
     * The {@code long_bignat_size} must be more than twice as long as
     * the {@code short_bignat_size}, otherwise the method on the card
     * might throw an assertion or silently produce wrong values.
     * 
     * @param rand Randomness source
     * @param short_bignat_size the size of the factors
     * @param long_bignat_size the size of the modulus
     * @param mult_rounds_1 number of multiplications in step sq_mult_1
     * @param mult_rounds_2 number of multiplications in step sq_mult_2
     * @param x first factor when non-null
     * @param y first factor when non-null
     * @return the combined result of the short squared mult protocol
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result short_sq_mult_perf_once(Random rand, 
                                                    int short_bignat_size, 
                                                    int long_bignat_size,
                                                    int mult_rounds_1, 
                                                    int mult_rounds_2,
                                                    BigInteger x,
                                                    BigInteger y) 
        throws CardException
    {
        int short_max_bits = 8 * short_bignat_size;
        int long_max_bits = 8 * long_bignat_size;

        BigInteger f1, f2, m;

        if(State.fix_inputs.has_n_inputs(3, "shared squared mult check")) {
            f1 = State.fix_inputs.pop();
            f2 = State.fix_inputs.pop();
            m = State.fix_inputs.pop();
        }
        else if(x != null && y != null) {
            f1 = x;
            f2 = y;
            do {
                m = new BigInteger(long_max_bits, rand);
            } while(m.bitLength() <= long_max_bits -8);
            // make m odd
            m = m.setBit(0);
        }
        else {
            f1 = new BigInteger(short_max_bits, rand);
            f2 = new BigInteger(short_max_bits, rand);
            do {
                m = new BigInteger(long_max_bits, rand);
            } while(m.bitLength() <= long_max_bits -8);
            // make m odd
            m = m.setBit(0);
        }
        
        Host_modulus hmod = new Host_modulus(long_bignat_size, m);

        if(State.verbosity.ref >= 5) {
            out.format("#### f1    = %s\n" +
                       "####       = %s\n" +
                       "#### f2    = %s\n" +
                       "####       = %s\n" +
                       "#### m     = %s\n" +
                       "####       = %s\n",
                       f1, BigIntUtil.to_byte_hex_string(f1),
                       f2, BigIntUtil.to_byte_hex_string(f2),
                       m, BigIntUtil.to_byte_hex_string(m)
                       );
        }

        Mult_call_result res_mult;

        try {
            if(State.verbosity.ref >= 5) 
                out.println("## short squared mult f1 * f2");

            res_mult = card_short_sq_mult(f1, f2, hmod, 
                                          mult_rounds_1, 
                                          mult_rounds_2);
            if(State.verbosity.ref >= 5) 
                out.format("## res = %s\n" +
                           "##     = %s\n",
                           res_mult.result, 
                           BigIntUtil.to_byte_hex_string(res_mult.result)
                           );
        }
        catch(Response_apdu.Card_response_error e){
            if(e.response_code() != Response_status.OV_RSA_KEY_FAILURE &&
               e.response_code() != Response_status.OV_RSA_EXP_FAILURE)
                {
                    System.err.format("\nError at %s * %s mod %s\n",
                                      f1, f2, m);
                }
            throw e;
        }
        catch(CardException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, m);
            throw e;
        }
        catch(RuntimeException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, m);
            throw e;
        }

        boolean success = 
            res_mult.result.compareTo(f1.multiply(f2)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s * %s with modulus %s\n",
                              f1, f2, m);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res_mult;
    }


    /**
     * 
     * Short squared multiplication performance. Performs {@link
     * State#rounds} measurments of short squared multipliation for
     * each bignat size between {@link State#start_size} and {@link
     * State#short_size} with 4 byte increments. If {@link
     * State#start_size} is zero, starts at a very small size. Each
     * measurement is printed in a gnuplot compatible way. If {@link
     * State#long_size} has not been set via option {@code -size} the
     * size of the RSA modulus and the temporaries will increase as
     * necessary. Otherwise {@link State#long_size} will be used.
     * <P>
     *
     * If {@link State#check_corner_cases} is set will generate
     * multiplication corner cases {@link #make_mult_corner_cases
     * make_mult_corner_cases} and use them in the first rounds of
     * every size.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void short_sq_mult_measure()
        throws CardException
    {
        if(!State.long_size_set.ref)
            State.long_size.ref = 64;

        int mult_rounds_1 = 0;
        int mult_rounds_2 = 1;

        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; i <= State.short_size; i += 4) {
            int double_size = 2 * i + 1;
            if(double_size > State.double_bignat_max_size)
                break;

            if(!State.long_size_set.ref && 2 * i + 2 > State.long_size.ref) {
                State.long_size.ref = 2 * i + 2;
                if(State.long_size.ref % 4 != 0)
                    State.long_size.ref += 4 - State.long_size.ref % 4;
            }

            out.format("## Effective size: factors %d (%d bits) " +
                       "double %d (%d bits), mod %d (%d bits)\n", 
                       i, i * 8,
                       double_size,
                       double_size * 8,
                       State.long_size.ref,
                       State.long_size.ref * 8
                       );

            BigInteger[][] corner_cases = null;
            if(State.check_corner_cases.ref)
                corner_cases = make_mult_corner_cases(i * 8, rand);

            try {
                misc_host.set_size(i, State.long_size.ref, 
                                   double_size, State.long_size.ref);

                for(int j = 0; j < State.rounds.ref; j++) {
                    BigInteger x = null;
                    BigInteger y = null;
                    if(State.check_corner_cases.ref 
                       && j < corner_cases.length) 
                        {
                            x = corner_cases[j][0];
                            y = corner_cases[j][1];
                        }

                    Mult_call_result result = 
                        short_sq_mult_perf_once(rand, i,
                                                State.long_size.ref,
                                                mult_rounds_1, 
                                                mult_rounds_2,
                                                x, y);

                    out.format("size %d %d sq_mult rounds %.03f s " +
                               "( %d %.03f s %d %.03f s )\n",
                               i * 8, 
                               mult_rounds_2 - mult_rounds_1,
                               new Long(result.duration_2 - 
                                        result.duration_1).doubleValue() /
                               (mult_rounds_2 - mult_rounds_1) / 1E9,
                               mult_rounds_1,
                               result.duration_1 / 1.0E9,
                               mult_rounds_2,
                               result.duration_2 / 1.0E9
                               );
                }
            }
            catch(Card_response_error e) {
                if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE) {
                    out.format("## Size %d not supported\n", i);
                }
                else if(e.response_code() == 
                        Response_status.OV_RSA_EXP_FAILURE) 
                    {
                        out.format("## Size %d not supported " +
                                   "(exception when setting exponent)\n", 
                                   i);
                    }
                else {
                    throw e;
                }
                continue;
            }
        }
    }


    //########################################################################
    //########################################################################
    // 
    // Squared 4 multiplication
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Run the squared 4 mult protocol ({@link
     * Bignat#squared_rsa_mult_4 Bignat.squared_rsa_mult_4}). Sends
     * the factors {@code f1} and {@code f2}, the modulus {@code m}
     * and some multiples of the modulus in step sq_mult_4_init to the
     * card. Performs one multiplication in step sq_mult_4_2 and
     * queries the result with the sq_mult_4_result step. <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 1
     * milliseconds). 
     * 
     * @param f1 first factor
     * @param f2 second factor
     * @param mod modulus
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_sq_mult_4(BigInteger f1, BigInteger f2, 
                                           Host_modulus mod)
        throws CardException
    {
        int sleep = 1;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.sq_mult_4_init_call(card_channel, f1, f2, mod);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.sq_mult_4_1_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.sq_mult_4_2_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.sq_mult_4_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }


    /**
     * 
     * Checks and measures squared 4 multiplication on the card ({@link
     * Bignat#squared_rsa_mult_4 Bignat.squared_rsa_mult_4}). If 3 user
     * provided inputs are available, the factors and the modulus are
     * taken from them (see {@link State#fix_inputs}). Otherwise, if
     * the arguments {@code x} and {@code y} are non-null they are
     * taken as factors with a randomly generated modulus of size
     * {@code long_bignat_size}. Otherwise random numbers are
     * generated of size {@code bignat_size} for the factors and the
     * modulus. The factors are then multiplied on the card with the
     * squared mult 4 protocol. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined result of the
     * protocol is given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before.
     * 
     * @param rand Randomness source
     * @param bignat_size the size of the factors and the modulus
     * @param x first factor when non-null
     * @param y first factor when non-null
     * @return the combined result of the squared 4 mult protocol
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result sq_mult_4_perf_once(Random rand, int bignat_size, 
                                                BigInteger x,
                                                BigInteger y) 
        throws CardException
    {
        // leave the highest two bits empty
        int max_bits = 8 * bignat_size - 2;

        BigInteger f1, f2, mod;

        if(State.fix_inputs.has_n_inputs(3, "squared mult check")) {
            f1 = State.fix_inputs.pop();
            f2 = State.fix_inputs.pop();
            mod = State.fix_inputs.pop();
        }
        else if(x != null && y != null) {
            f1 = x;
            f2 = y;
            do {
                mod = new BigInteger(max_bits, rand);
            } while(mod.bitLength() <= (bignat_size - 1) * 8);
            // make mod = 1 (modulo 4)
            mod = mod.setBit(0).clearBit(1);
        }
        else {
            f1 = new BigInteger(max_bits, rand);
            f2 = new BigInteger(max_bits, rand);
            do {
                mod = new BigInteger(max_bits, rand);
            } while(mod.bitLength() <= (bignat_size - 1) * 8);
            // make mod = 1 (modulo 4)
            mod = mod.setBit(0).clearBit(1);
        }
        
        Host_modulus hmod = new Host_modulus(bignat_size, mod);

        if(State.verbosity.ref >= 5) {
            out.format("#### f1    = %s\n" +
                       "####       = %s\n" +
                       "#### f2    = %s\n" +
                       "####       = %s\n" +
                       "#### mod   = %s\n" +
                       "####       = %s\n",
                       f1, BigIntUtil.to_byte_hex_string(f1),
                       f2, BigIntUtil.to_byte_hex_string(f2),
                       mod, BigIntUtil.to_byte_hex_string(mod)
                       );
        }

        Mult_call_result res_mult;

        try {
            if(State.verbosity.ref >= 5) 
                out.println("## squared mult f1 * f2 % m");

            res_mult = card_sq_mult_4(f1, f2, hmod);
            if(State.verbosity.ref >= 5) 
                out.format("## res = %s\n" +
                           "##     = %s\n",
                           res_mult.result, 
                           BigIntUtil.to_byte_hex_string(res_mult.result)
                           );
        }
        catch(Response_apdu.Card_response_error e){
            if(e.response_code() != Response_status.OV_RSA_KEY_FAILURE &&
               e.response_code() != Response_status.OV_RSA_EXP_FAILURE)
                {
                    System.err.format("\nError at %s * %s mod %s\n",
                                      f1, f2, mod);
                }
            throw e;
        }
        catch(CardException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, mod);
            throw e;
        }
        catch(RuntimeException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, mod);
            throw e;
        }

        boolean success = 
            res_mult.result.compareTo(f1.multiply(f2).mod(mod)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s * %s mod %s\n",
                              f1, f2, mod);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res_mult;
    }


    /**
     * 
     * Squared 4 multiplication correctness check. Set the bignat
     * size to {@link State#long_size} and performs {@link
     * State#rounds} squared 4 multiplications checks via
     * method {@link #sq_mult_4_perf_once sq_mult_4_perf_once}.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void sq_mult_4_check() 
        throws CardException
    {
        Random rand = new Random();

        out.format("## Effective size %d bytes (%d bits)\n", 
                   State.long_size.ref,
                   State.long_size.ref * 8
                   );

        try {
            misc_host.set_long_cipher_size(State.long_size.ref, 
                                           State.long_size.ref);
        }
        catch(Card_response_error e) {
            if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE) {
                out.format("## Size %d not supported\n", State.long_size.ref);
            }
            throw e;
        }

        BigInteger[][] corner_cases = null;
        if(State.check_corner_cases.ref)
            corner_cases = make_mult_corner_cases(State.long_size.ref * 8 - 2, 
                                                  rand);

        for(int i = 0; i < State.rounds.ref; i++) {
            BigInteger x = null;
            BigInteger y = null;
            if(State.check_corner_cases.ref && i < corner_cases.length) {
                x = corner_cases[i][0];
                y = corner_cases[i][1];
            }

            try {
                sq_mult_4_perf_once(rand, State.long_size.ref, x, y);
            }
            catch(Card_response_error e) {
                if(e.response_code() == Response_status.OV_RSA_EXP_FAILURE) {
                    out.format("## Size %d not supported " +
                               "(exception when setting exponent)\n", 
                               State.long_size.ref);
                }
                throw e;
            }
        }
    }


    /**
     * 
     * Squared 4 multiplication performance. Performs {@link
     * State#rounds} measurments of squared 4 multipliation for each
     * bignat size between {@link State#start_size} and {@link
     * State#long_bignat_max_size} with 4 byte increments. If {@link
     * State#start_size} is zero, starts at a very small size. Each
     * measurement is printed in a gnuplot compatible way. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void sq_mult_4_measure()
        throws CardException
    {
        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; i <= State.long_bignat_max_size; i += 4) {
            out.format("## Effective size %d bytes (%d bits)\n", i, i * 8 - 2);

            BigInteger[][] corner_cases = null;
            if(State.check_corner_cases.ref)
                corner_cases = make_mult_corner_cases(i * 8 - 2, rand);

            try {
                misc_host.set_long_cipher_size(i, i);

                for(int j = 0; j < State.rounds.ref; j++) {
                    BigInteger x = null;
                    BigInteger y = null;
                    if(State.check_corner_cases.ref 
                       && j < corner_cases.length) 
                        {
                            x = corner_cases[j][0];
                            y = corner_cases[j][1];
                        }

                    Mult_call_result result = sq_mult_4_perf_once(rand, i, 
                                                                  x, y);

                    out.format("size %d %.03f s " +
                               "( %.03f s %.03f s )\n",
                               i * 8 - 2, 
                               (result.duration_2 - result.duration_1) / 1E9,
                               result.duration_1 / 1.0E9,
                               result.duration_2 / 1.0E9
                               );
                }
            }
            catch(Card_response_error e) {
                if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE) {
                    out.format("## Size %d not supported\n", i);
                }
                else if(e.response_code() == 
                        Response_status.OV_RSA_EXP_FAILURE) 
                    {
                        out.format("## Size %d not supported " +
                                   "(exception when setting exponent)\n", 
                                   i);
                    }
                else {
                    throw e;
                }
                continue;
            }

        }
    }


    //########################################################################
    //########################################################################
    // 
    // Short squared 4 multiplication
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Run the short squared 4 mult protocol ({@link
     * Bignat#short_squared_rsa_mult_4
     * Bignat.short_squared_rsa_mult_4}). Sends the factors {@code f1}
     * and {@code f2} and the modulus {@code m} in step
     * short_sq_4_mult_init to the card. Step 2 is empty, the
     * multiplication is done in step 3. Finally queries the result
     * with the short_sq_4_mult_result step. <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 1
     * milliseconds). 
     * 
     * @param f1 first factor
     * @param f2 second factor
     * @param m modulus
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_short_sq_4_mult(BigInteger f1, BigInteger f2, 
                                                 Host_modulus m) 
        throws CardException
    {
        int sleep = 1;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.short_sq_4_mult_init_call(card_channel, f1, f2, m);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.short_sq_4_mult_1_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.short_sq_4_mult_2_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.short_sq_4_mult_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }


    /**
     * 
     * Checks and measures short square 4 multiplication on the card
     * ({@link Bignat#short_squared_rsa_mult_4
     * Bignat.short_squared_rsa_mult_4}). If 3 user provided inputs are
     * available, the factors and the modulus are taken from them (see
     * {@link State#fix_inputs}). Otherwise, if the arguments {@code
     * x} and {@code y} are non-null they are taken as factors with a
     * randomly generated modulus of size {@code long_bignat_size}.
     * Otherwise random numbers are generated of size {@code
     * short_bignat_size} for the factors and of size {@code
     * long_bignat_size} for the modulus. The factors are then
     * multiplied on the card with the short square 4 mult protocol.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined result of the
     * protocol is given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before.
     * <P>
     *
     * The {@code long_bignat_size} must be more than twice as long as
     * the {@code short_bignat_size}, otherwise the method on the card
     * might throw an assertion or silently produce wrong values.
     * 
     * @param rand Randomness source
     * @param short_bignat_size the size of the factors
     * @param long_bignat_size the size of the modulus
     * @param x first factor when non-null
     * @param y first factor when non-null
     * @return the combined result of the short squared mult protocol
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result short_sq_4_mult_perf_once(Random rand, 
                                                      int short_bignat_size, 
                                                      int long_bignat_size,
                                                      BigInteger x,
                                                      BigInteger y) 
        throws CardException
    {
        int short_max_bits = 8 * short_bignat_size;
        int long_max_bits = 8 * long_bignat_size;

        BigInteger f1, f2, m;

        if(State.fix_inputs.has_n_inputs(3, "shared square 4 mult check")) {
            f1 = State.fix_inputs.pop();
            f2 = State.fix_inputs.pop();
            m = State.fix_inputs.pop();
        }
        else if(x != null && y != null) {
            f1 = x;
            f2 = y;
            do {
                m = new BigInteger(long_max_bits, rand);
            } while(m.bitLength() <= long_max_bits -8);
            // make m odd
            m = m.setBit(0);
        }
        else {
            f1 = new BigInteger(short_max_bits, rand);
            f2 = new BigInteger(short_max_bits, rand);
            do {
                m = new BigInteger(long_max_bits, rand);
            } while(m.bitLength() <= long_max_bits -8);
            // make m odd
            m = m.setBit(0);
        }
        
        Host_modulus hmod = new Host_modulus(long_bignat_size, m);

        if(State.verbosity.ref >= 5) {
            out.format("#### f1    = %s\n" +
                       "####       = %s\n" +
                       "#### f2    = %s\n" +
                       "####       = %s\n" +
                       "#### m     = %s\n" +
                       "####       = %s\n",
                       f1, BigIntUtil.to_byte_hex_string(f1),
                       f2, BigIntUtil.to_byte_hex_string(f2),
                       m, BigIntUtil.to_byte_hex_string(m)
                       );
        }

        Mult_call_result res_mult;

        try {
            if(State.verbosity.ref >= 5) 
                out.println("## short square 4 mult f1 * f2");

            res_mult = card_short_sq_4_mult(f1, f2, hmod);
            if(State.verbosity.ref >= 5) 
                out.format("## res = %s\n" +
                           "##     = %s\n",
                           res_mult.result, 
                           BigIntUtil.to_byte_hex_string(res_mult.result)
                           );
        }
        catch(Response_apdu.Card_response_error e){
            if(e.response_code() != Response_status.OV_RSA_KEY_FAILURE &&
               e.response_code() != Response_status.OV_RSA_EXP_FAILURE)
                {
                    System.err.format("\nError at %s * %s mod %s\n",
                                      f1, f2, m);
                }
            throw e;
        }
        catch(CardException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, m);
            throw e;
        }
        catch(RuntimeException e){
            System.err.format("\nError at %s * %s mod %s\n", f1, f2, m);
            throw e;
        }

        boolean success = 
            res_mult.result.compareTo(f1.multiply(f2)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s * %s with modulus %s\n",
                              f1, f2, m);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res_mult;
    }


    /**
     * 
     * Short square 4 multiplication performance. Performs {@link
     * State#rounds} measurments of short squared multipliation for
     * each bignat size between {@link State#start_size} and {@link
     * State#short_size} with 4 byte increments. If {@link
     * State#start_size} is zero, starts at a very small size. Each
     * measurement is printed in a gnuplot compatible way. If {@link
     * State#long_size} has not been set via option {@code -size} the
     * size of the RSA modulus and the temporaries will increase as
     * necessary. Otherwise {@link State#long_size} will be used.
     * <P>
     *
     * If {@link State#check_corner_cases} is set will generate
     * multiplication corner cases {@link #make_mult_corner_cases
     * make_mult_corner_cases} and use them in the first rounds of
     * every size.
     * <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void short_sq_4_mult_measure()
        throws CardException
    {
        if(!State.long_size_set.ref)
            State.long_size.ref = 64;

        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; i <= State.short_size; i += 4) {
            int double_size = 2 * i + 1;
            if(double_size > State.double_bignat_max_size)
                break;

            if(!State.long_size_set.ref && 2 * i + 2 > State.long_size.ref) {
                State.long_size.ref = 2 * i + 2;
                if(State.long_size.ref % 4 != 0)
                    State.long_size.ref += 4 - State.long_size.ref % 4;
            }

            out.format("## Effective size: factors %d (%d bits) " +
                       "double %d (%d bits), mod %d (%d bits)\n", 
                       i, i * 8,
                       double_size,
                       double_size * 8,
                       State.long_size.ref,
                       State.long_size.ref * 8
                       );

            BigInteger[][] corner_cases = null;
            if(State.check_corner_cases.ref)
                corner_cases = make_mult_corner_cases(i * 8, rand);

            try {
                misc_host.set_size(i, State.long_size.ref, 
                                   double_size, State.long_size.ref);

                for(int j = 0; j < State.rounds.ref; j++) {
                    BigInteger x = null;
                    BigInteger y = null;
                    if(State.check_corner_cases.ref 
                       && j < corner_cases.length) 
                        {
                            x = corner_cases[j][0];
                            y = corner_cases[j][1];
                        }

                    Mult_call_result result = 
                        short_sq_4_mult_perf_once(rand, i,
                                                  State.long_size.ref,
                                                  x, y);

                    out.format("size %d sq_mult rounds %.03f s " +
                               "( %.03f s %.03f s )\n",
                               i * 8, 
                               (result.duration_2 - result.duration_1) / 1E9,
                               result.duration_1 / 1.0E9,
                               result.duration_2 / 1.0E9
                               );
                }
            }
            catch(Card_response_error e) {
                if(e.response_code() == Response_status.OV_RSA_KEY_FAILURE) {
                    out.format("## Size %d not supported\n", i);
                }
                else if(e.response_code() == 
                        Response_status.OV_RSA_EXP_FAILURE) 
                    {
                        out.format("## Size %d not supported " +
                                   "(exception when setting exponent)\n", 
                                   i);
                    }
                else {
                    throw e;
                }
                continue;
            }
        }
    }


    //########################################################################
    //########################################################################
    // 
    // Addition
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Run the add protocol to add two numbers on the card. 
     * <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 10
     * milliseconds). 
     * 
     * @param s1 first summand
     * @param s2 second summand
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_add(BigInteger s1, BigInteger s2) 
        throws CardException
    {
        int sleep = 10;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.add_init_call(card_channel, s1, s2);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.add_1_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.add_2_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.add_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }


    /**
     * 
     * Checks and measures addition on the card. If 2 user provided
     * inputs are available, the summands are taken from them (see
     * {@link State#fix_inputs}). Otherwise random numbers are
     * generated of size {@code bignat_size}. The summands are then
     * added on the card with the add protocol. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined result of the
     * protocol is given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before.
     * 
     * @param rand Randomness source
     * @param bignat_size the size of the factors 
     * @return the combined result of the add protocol
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result add_perf_once(Random rand, int bignat_size)
        throws CardException
    {
        int max_bits = 8 * bignat_size;
        // Leave the hightes bit empty to avoid overflows.
        max_bits--;

        BigInteger s1, s2;

        if(State.fix_inputs.has_n_inputs(2, "add check")) {
            s1 = State.fix_inputs.pop();
            s2 = State.fix_inputs.pop();
        }
        else {
            s1 = new BigInteger(max_bits, rand);
            s2 = new BigInteger(max_bits, rand);
        }
        
        if(State.verbosity.ref >= 5) {
            out.format("#### s1    = %s\n" +
                       "####       = %s\n" +
                       "#### s2    = %s\n" +
                       "####       = %s\n",
                       s1, BigIntUtil.to_byte_hex_string(s1),
                       s2, BigIntUtil.to_byte_hex_string(s2)
                       );
        }

        if(State.verbosity.ref >= 5) 
            out.println("## add s1 + s2");

        Mult_call_result res_add;

        try {
            res_add = card_add(s1, s2);
        }
        catch(CardException e) {
            System.err.format("\nError at %s + %s\n",
                              s1, s2);
            throw e;
        }

        if(State.verbosity.ref >= 5) 
            out.format("## res = %s\n" +
                       "##     = %s\n",
                       res_add.result, 
                       BigIntUtil.to_byte_hex_string(res_add.result)
                       );

        boolean success = 
            res_add.result.compareTo(s1.add(s2)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s + %s\n",
                              s1, s2);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res_add;
    }


    /**
     * 
     * Addition performance. Performs {@link State#rounds} measurments
     * of addition for each bignat size between {@link
     * State#start_size} and {@link State#long_bignat_max_size} with 4
     * byte increments. If {@link State#start_size} is zero, starts at
     * a very small size. Each measurement is printed in a gnuplot
     * compatible way. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void add_measure()
        throws CardException
    {
        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; i <= State.long_bignat_max_size; i += 4) {
            out.format("## Effective size %d bytes (%d bits)\n", 
                       i, i * 8 - 1);

            misc_host.set_long_size(i);

            for(int j = 0; j < State.rounds.ref; j++) {

                Mult_call_result result = add_perf_once(rand, i);

                out.format("size %d %.03f s " +
                           "( %.03f s %.03f s )\n",
                           i * 8 - 1, 
                           (result.duration_2 - result.duration_1) / 1E9,
                           result.duration_1 / 1.0E9,
                           result.duration_2 / 1.0E9
                           );
            }
        }
    }


    //########################################################################
    //########################################################################
    // 
    // Subtraction
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Combined result record for the subtraction protocol.
     */
    private class Subtract_call_result {

        /** Duration of the first step in nanoseconds. */
        public final long duration_1;

        /** Duration of the second step in nanoseconds. */
        public final long duration_2;

        /** Result of the card. */
        public final BigInteger result;

        /** Overflow from the card. */
        public final boolean overflow;

        /** Initialize the record. */
        public Subtract_call_result(long duration_1, long duration_2, 
                                    BigInteger result, boolean overflow) {
            this.duration_1 = duration_1;
            this.duration_2 = duration_2;
            this.result = result;
            this.overflow = overflow;
        }
    }

    
    /**
     * 
     * Run the subtract protocol to subtract two numbers on the card. 
     * <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 10
     * milliseconds). 
     * 
     * @param x minuend
     * @param y subtrahend
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Subtract_call_result card_subtract(BigInteger x, BigInteger y) 
        throws CardException
    {
        int sleep = 10;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.subtract_init_call(card_channel, x, y);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.subtract_1_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.subtract_2_call(card_channel);

        Misc_host.gc_sleep(sleep); 
        Subtract_result_result r = 
            bignat_stubs.subtract_result_call(card_channel);

        return new Subtract_call_result(d1, d2, r.bignats_r_1, r.carry);
    }


    /**
     * 
     * Checks and measures subtraction on the card. If 2 user provided
     * inputs are available, they are taken as minuend and subtrahend
     * (see {@link State#fix_inputs}). Otherwise random numbers are
     * generated of size {@code bignat_size}. The numbers are then
     * subtracted on the card with the subtract protocol. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined result of the
     * protocol is given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before.
     * 
     * @param rand Randomness source
     * @param bignat_size the size of the factors 
     * @return the combined result of the subtract protocol
     * @throws CardException for communication problems and protocol errors
     */
    public Subtract_call_result subtract_perf_once(Random rand, int bignat_size)
        throws CardException
    {
        int max_bits = 8 * bignat_size;

        BigInteger x, y;

        if(State.fix_inputs.has_n_inputs(2, "subtract check")) {
            x = State.fix_inputs.pop();
            y = State.fix_inputs.pop();
        }
        else {
            x = new BigInteger(max_bits, rand);
            y = new BigInteger(max_bits, rand);
        }
        
        if(State.verbosity.ref >= 5) {
            out.format("#### x    = %s\n" +
                       "####      = %s\n" +
                       "#### y    = %s\n" +
                       "####      = %s\n",
                       x, BigIntUtil.to_byte_hex_string(x),
                       y, BigIntUtil.to_byte_hex_string(y)
                       );
        }

        if(State.verbosity.ref >= 5) 
            out.println("## subtract x - y");

        Subtract_call_result res_subtract;

        try {
            res_subtract = card_subtract(x, y);
        }
        catch(CardException e) {
            System.err.format("\nError at %s - %s\n",
                              x, y);
            throw e;
        }

        if(State.verbosity.ref >= 5) 
            out.format("## res = %s\n" +
                       "##     = %s\n" +
                       "## overflow = %s\n",
                       res_subtract.result, 
                       BigIntUtil.to_byte_hex_string(res_subtract.result),
                       res_subtract.overflow
                       );

        BigInteger card_result = res_subtract.result;
        if(res_subtract.overflow) {
            card_result = 
                card_result.subtract(BigInteger.ONE.add(BigInteger.ONE)
                                     .pow(max_bits));
        }

        boolean success = 
            card_result.compareTo(x.subtract(y)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s - %s\n",
                              x, y);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res_subtract;
    }


    /**
     * 
     * Subtraction performance. Performs {@link State#rounds}
     * measurments of subtraction for each bignat size between {@link
     * State#start_size} and {@link State#long_bignat_max_size} with 4
     * byte increments. If {@link State#start_size} is zero, starts at
     * a very small size. Each measurement is printed in a gnuplot
     * compatible way. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void subtract_measure()
        throws CardException
    {
        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; i <= State.long_bignat_max_size; i += 4) {
            out.format("## Effective size %d bytes (%d bits)\n", 
                       i, i * 8);

            misc_host.set_long_size(i);

            for(int j = 0; j < State.rounds.ref; j++) {

                Subtract_call_result result = subtract_perf_once(rand, i);

                out.format("size %d %.03f s " +
                           "( %.03f s %.03f s )\n",
                           i * 8, 
                           (result.duration_2 - result.duration_1) / 1E9,
                           result.duration_1 / 1.0E9,
                           result.duration_2 / 1.0E9
                           );
            }
        }
    }


    //########################################################################
    //########################################################################
    // 
    // Normal multiplication
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Run the mult protocol to multiply two numbers on the card. 
     * <P>
     *
     * To reduce card communication errors the method performs a
     * garbage collection and sleeps some time before each step. The
     * sleep time is hardwired in the method (currently 10
     * milliseconds). 
     * 
     * @param f1 first factor
     * @param f2 second factor
     * @param mult_rounds_1 number of multiplications in step mult_1
     * @param mult_rounds_2 number of multiplications in step mult_2
     * @return combined protocol results
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result card_normal_mult(BigInteger f1, BigInteger f2,
                                             int mult_rounds_1, 
                                             int mult_rounds_2) 
        throws CardException
    {
        int sleep = 10;

        Misc_host.gc_sleep(sleep); 
        bignat_stubs.mult_init_call(card_channel, f1, f2);

        Misc_host.gc_sleep(sleep); 
        long d1 = bignat_stubs.mult_1_call(card_channel, mult_rounds_1);

        Misc_host.gc_sleep(sleep); 
        long d2 = bignat_stubs.mult_2_call(card_channel, mult_rounds_2);

        Misc_host.gc_sleep(sleep); 
        BigInteger r = bignat_stubs.mult_result_call(card_channel);

        return new Mult_call_result(d1, d2, r);
    }


    /**
     * 
     * Checks and measures normal multiplication on the card. If 2
     * user provided inputs are available, the factors are taken from
     * them (see {@link State#fix_inputs}). Otherwise random numbers
     * are generated of size {@code bignat_size}. The factors are
     * then multiplied on the card with the mult protocol. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated. Otherwise the combined result of the
     * protocol is given back.
     * <P>
     *
     * Assumes that the bignat size has been set to an appropriate
     * value before. Especially the double_sized bignats must have
     * been set to {@code 2 * bignat_size}.
     * 
     * @param rand Randomness source
     * @param bignat_size the size of the factors
     * @param mult_rounds_1 number of multiplications in step mult_1
     * @param mult_rounds_2 number of multiplications in step mult_2
     * @return the combined result of the normal mult protocol
     * @throws CardException for communication problems and protocol errors
     */
    public Mult_call_result normal_mult_perf_once(Random rand, int bignat_size,
                                                  int mult_rounds_1, 
                                                  int mult_rounds_2)
        throws CardException
    {
        int max_bits = 8 * bignat_size;

        BigInteger f1, f2;

        if(State.fix_inputs.has_n_inputs(2, "normal mult check")) {
            f1 = State.fix_inputs.pop();
            f2 = State.fix_inputs.pop();
        }
        else {
            f1 = new BigInteger(max_bits, rand);
            f2 = new BigInteger(max_bits, rand);
        }
        
        if(State.verbosity.ref >= 5) {
            out.format("#### f1    = %s\n" +
                       "####       = %s\n" +
                       "#### f2    = %s\n" +
                       "####       = %s\n",
                       f1, BigIntUtil.to_byte_hex_string(f1),
                       f2, BigIntUtil.to_byte_hex_string(f2)
                       );
        }

        if(State.verbosity.ref >= 5) 
            out.println("## multiply f1 * f2");

        Mult_call_result res_mult;

        try {
            res_mult = card_normal_mult(f1, f2, mult_rounds_1, mult_rounds_2);
        }
        catch(CardException e) {
            System.err.format("\nError at %s * %s\n",
                              f1, f2);
            throw e;
        }

        if(State.verbosity.ref >= 5) 
            out.format("## res = %s\n" +
                       "##     = %s\n",
                       res_mult.result, 
                       BigIntUtil.to_byte_hex_string(res_mult.result)
                       );

        boolean success = 
            res_mult.result.compareTo(f1.multiply(f2)) == 0;

        if(success && State.verbosity.ref >= 5)
            out.format("#### Success\n");

        if(!success & State.verbosity.ref < 5)
            System.err.format("\nError at %s * %s\n",
                              f1, f2);
        if(!success) {
            out.format("#### FAILURE\n");
            System.exit(1);
        }

        return res_mult;
    }


    /**
     * 
     * Normal multiplication performance. Performs {@link
     * State#rounds} measurments of multiplication for each bignat
     * size between {@link State#start_size} and {@link
     * State#double_size}{@code / 2} with 4 byte increments. If {@link
     * State#start_size} is zero, starts at a very small size. Each
     * measurement is printed in a gnuplot compatible way. <P>
     *
     * If the card makes an error the parameters are printed and the
     * program is terminated.
     * 
     * @throws CardException for communication problems and protocol errors
     */
    public void normal_mult_measure()
        throws CardException
    {
        int mult_rounds_1 = 2;
        int mult_rounds_2 = 4;

        Random rand = new Random();

        int start = State.start_size.ref > 0 ? State.start_size.ref : 4;

        for(int i = start; 2 * i <= State.double_size; i += 4) {
            out.format("## Effective size %d bytes (%d bits)\n", i, i * 8);

            misc_host.set_size(State.short_size, i, 2 * i, 0);

            for(int j = 0; j < State.rounds.ref; j++) {

                Mult_call_result result = 
                    normal_mult_perf_once(rand, i, 
                                          mult_rounds_1, mult_rounds_2);

                out.format("size %d %d mult rounds %.03f s " +
                           "( %d %.03f s %d %.03f s )\n",
                           i * 8, 
                           mult_rounds_2 - mult_rounds_1,
                           new Long(result.duration_2 - 
                                    result.duration_1).doubleValue() /
                           (mult_rounds_2 - mult_rounds_1) / 1E9,
                           mult_rounds_1,
                           result.duration_1 / 1.0E9,
                           mult_rounds_2,
                           result.duration_2 / 1.0E9
                           );
            }
        }
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
     * Exception wrapper for {@link #mult_check}. Same as {@link
     * #mult_check}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_mult_check() {
        try {
            out.println("################# mult check start");
            mult_check();
            out.println("################# mult check finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #mult_measure}. Same as {@link
     * #mult_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_mult_perf() {
        try {
            out.println("################# mult perf start");
            mult_measure();
            out.println("################# mult perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #div_check}. Same as {@link
     * #div_check}, except that exceptions are cought and stack traces
     * are printed for them.
     * 
     */
    public void run_div_check() {
        try {
            out.println("################# div check start");
            div_check();
            out.println("################# div check finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #div_measure}. Same as {@link
     * #div_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_div_perf() {
        try {
            out.println("################# div perf start");
            div_measure();
            out.println("################# div perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #rsa_exp_check}. Same as {@link
     * #rsa_exp_check}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_rsa_exp_check() {
        try {
            out.println("################# rsa exponent check start");
            rsa_exp_check();
            out.println("################# rsa exponent check finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #rsa_exp_measure}. Same as {@link
     * #rsa_exp_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_rsa_exp_perf() {
        try {
            rsa_exp_measure();
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #sq_mult_check}. Same as {@link
     * #sq_mult_check}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_sq_mult_check() {
        try {
            out.println("################# squared mult check start");
            sq_mult_check();
            out.println("################# squared mult check finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #sq_mult_measure}. Same as {@link
     * #sq_mult_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_sq_mult_perf() {
        try {
            out.println("################# squared mult perf start");
            sq_mult_measure();
            out.println("################# squared mult perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #short_sq_mult_measure}. Same as
     * {@link #short_sq_mult_measure}, except that exceptions are
     * cought and stack traces are printed for them.
     * 
     */
    public void run_short_sq_mult_perf() {
        try {
            out.println("################# short squared mult perf start");
            short_sq_mult_measure();
            out.println("################# short squared mult perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #sq_mult_4_check}. Same as {@link
     * #sq_mult_4_check}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_sq_mult_4_check() {
        try {
            out.println("################# squared 4 mult check start");
            sq_mult_4_check();
            out.println("################# squared 4 mult check finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #sq_mult_4_measure}. Same as {@link
     * #sq_mult_4_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_sq_mult_4_perf() {
        try {
            out.println("################# squared 4 mult perf start");
            sq_mult_4_measure();
            out.println("################# squared 4 mult perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #short_sq_4_mult_measure}. Same as
     * {@link #short_sq_4_mult_measure}, except that exceptions are
     * cought and stack traces are printed for them.
     * 
     */
    public void run_short_sq_4_mult_perf() {
        try {
            out.println("################# short squared 4 mult perf start");
            short_sq_4_mult_measure();
            out.println("################# short squared 4 mult perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #add_measure}. Same as {@link
     * #add_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_add_perf() {
        try {
            out.println("################# add perf start");
            add_measure();
            out.println("################# add perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #subtract_measure}. Same as {@link
     * #subtract_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_subtract_perf() {
        try {
            out.println("################# subtract perf start");
            subtract_measure();
            out.println("################# subtract perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }


    /**
     * 
     * Exception wrapper for {@link #normal_mult_measure}. Same as {@link
     * #normal_mult_measure}, except that exceptions are cought and stack
     * traces are printed for them.
     * 
     */
    public void run_normal_mult_perf() {
        try {
            out.println("################# normal mult perf start");
            normal_mult_measure();
            out.println("################# normal mult perf finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
    }
}
