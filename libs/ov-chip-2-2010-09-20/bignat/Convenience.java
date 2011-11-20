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
// Created 28.1.09 by Hendrik
// 
// BigInteger convenience interface to some Bignat methods
// 
// $Id: Convenience.java,v 1.5 2009-02-26 12:23:51 tews Exp $

package ds.ov2.bignat;


import java.math.BigInteger;

import ds.ov2.util.Convert_serializable;
import ds.ov2.util.BigIntUtil;


/**
 * {@link java.math.BigInteger BigInteger} convenience interface
 * to selected {@link Bignat} methods.
 *
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.5 $
 * @commitdate $Date: 2009-02-26 12:23:51 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Convenience {


    /**
     * Static class, object creation disabled.
     */
    private Convenience() {}


    /**
     * {@link java.math.BigInteger BigInteger}, long tuple for
     * methods that produce such results.
     */
    public static class Timed_result {
        /**
         * The BigInteger component of the tuple.
         */
        public final BigInteger result;
        /**
         * The long component of the tuple.
         */
        public final long duration;
        /**
         * Constructor for initializing the tuple.
         */
        public Timed_result(BigInteger result, long duration) {
            this.result = result;
            this.duration = duration;
        }
    }


    /**
     * Convert {@link BigInteger} into {@link Bignat}.
     * Converts BigInteger {@code bi} into a Bignat of size 
     * {@code bignat_size}, which, of course must be large enough.
     * The BigInteger must be positive or zero,
     * the result is arbitrary.
     *
     * @param bignat_size size of the returned Bignat
     * @param bi source BigInteger
     * @return a Bignat of the same value
     * @throws IllegalArgumentException if {@code bignat_size}
     *    is not a non-zero positive short or if {@code bi} does not fit
     *    into {@code bignat_size} bytes.
     */
    public static Bignat bn_from_bi(int bignat_size, BigInteger bi) 
        throws IllegalArgumentException
    {
        // if(!(0 < bignat_size && bignat_size <= Short.MAX_VALUE))
        //     System.out.format("XX %d\n", bignat_size);
        if(bignat_size <= 0 || bignat_size > Short.MAX_VALUE)
            throw new IllegalArgumentException(
               "Convenience.bn_from_bi: bignat_size must be a positive short");
        short bs = (short)bignat_size;
        Bignat bn = new Bignat(bs);
        Convert_serializable.to(new APDU_BigInteger(bs, bi), bn);
        return bn;
    }


    /**
     * Convert {@link Bignat} into {@code BigInteger}.
     *
     * @param bn Bignat to convert
     * @return a BigInteger of the same value
     */
    public static BigInteger bi_from_bn(Bignat bn) {
        APDU_BigInteger abi = new APDU_BigInteger(bn.size());
        Convert_serializable.from(abi, bn);
        return abi.value;
    }


    /**
     * Convenience interface to {@link Bignat#exponent_mod
     * Bignat.exponent_mod}. Computes {@code base^exponent (modulo modulus)}
     * using Bignat.exponent_mod and returns the result as BigInteger
     * together with the running time of Bignat.exponent_mod.
     * <P>
     * The sizes for the Bignats are computed internally.
     * <P>
     * Montgomerization and preparation of the modulus are done 
     * with the BigInteger class. Preparation and postprocessing is
     * not contained in the measured time.
     *
     * @param base 
     * @param exponent 
     * @param modulus
     * @return {@code base^exponent (modulo modulus)} together with 
     *      the duration of {@link Bignat#exponent_mod 
     *      Bignat.exponent_mod} in nanoseconds.
     * @throws IllegalArgumentException if the modulus is even or
     *       either base or exponent are longer than {@link 
     *       Short#MAX_VALUE}
     *       bytes.
     */
    public static Timed_result exponent_mod(BigInteger base, 
                                            BigInteger exponent,
                                            BigInteger modulus)
        throws IllegalArgumentException
    {

        if(!modulus.testBit(0))
            throw new IllegalArgumentException("even modulus");

        int base_size = BigIntUtil.byte_size(modulus);
        if(base_size < BigIntUtil.byte_size(base))
            base_size = BigIntUtil.byte_size(base);
        int exp_size = BigIntUtil.byte_size(exponent);

        // Adjust sizes to be a multiple of the digit sizes
        if(base_size % Bignat.size_multiplier != 0)
            base_size += 
                Bignat.size_multiplier - base_size % Bignat.size_multiplier;
        if(exp_size % Bignat.size_multiplier != 0)
            exp_size += 
                Bignat.size_multiplier - exp_size % Bignat.size_multiplier;

        // Adjust base size for Montgomery multiplication.
        base_size += 2 * Bignat.size_multiplier;

        if(base_size > Short.MAX_VALUE || exp_size > Short.MAX_VALUE)
            throw new IllegalArgumentException("base or exponent too long");
        
        // Prepare modulus, this will compute all necessary incredients 
        // for montgomery multiplication internally and store them in hmod.
        Host_modulus hmod = new Host_modulus(base_size, modulus);
        Modulus bn_mod = new Modulus((short)base_size, false);
        Convert_serializable.to(hmod, bn_mod);
        Bignat bn_one = bn_from_bi(base_size, hmod.mont_fac);

        // Montgomerize the base.
        BigInteger base_mont = base.multiply(hmod.mont_fac).mod(modulus);

        // Prepare Bignat versions of base and exponent.
        Bignat bn_base_mont = bn_from_bi(base_size, base_mont);
        Bignat bn_exponent = bn_from_bi(exp_size, exponent);

        // Allocate result and a temporary.
        Bignat bn_result = new Bignat((short)base_size);
        Bignat temp = new Bignat((short)base_size);

        // Compute the exponent, result will be montgomerized, 
        // because the base is.
        long start = System.nanoTime();
        bn_result.exponent_mod(bn_base_mont, bn_exponent, bn_mod, 
                               bn_one, temp);
        long exp_duration = System.nanoTime() - start;

        // Get real result.
        bn_result.demontgomerize(bn_mod);
        BigInteger bi_result = bi_from_bn(bn_result);

        return new Timed_result(bi_result, exp_duration);
    }


    /**
     * Convenience interface to {@link Vector#exponent_mod
     * Vector.exponent_mod}. Computes 
     * {@code bases[0]^exponents[0] * ... * bases[n]^exponents[n] 
     * (modulo modulus)}
     * using Vector.exponent_mod and returns the result as BigInteger
     * together with the running time of Vector.exponent_mod.
     * <P>
     * The sizes for the Bignats are computed internally. There are 
     * no size constraints on the base and exponent arguments.
     * <P>
     * Montgomerization and preparation of the modulus are done 
     * with the BigInteger class. Preparation and postprocessing is
     * not contained in the measured time.
     *
     * @param bases array of the bases
     * @param exponents array of the exponents
     * @param modulus
     * @return {@code bases[0]^exponents[0] * ... * bases[n]^exponents[n] 
     *                (modulo modulus)} together with the duration in 
     *                nonoseconds
     * @throws IllegalArgumentException if the modulus is even,
     *       if {@code bases} and {@code exponents} have different length,
     *       and if
     *       either bases or exponents are longer than {@link Short#MAX_VALUE}
     *       bytes.
     */
    static public Timed_result vector_exponent_mod(BigInteger[] bases,
                                                   BigInteger[] exponents,
                                                   BigInteger modulus)
        throws IllegalArgumentException
    {
        if(!modulus.testBit(0))
            throw new IllegalArgumentException("even modulus");
        if(bases.length != exponents.length)
            throw new IllegalArgumentException
                ("bases and exponent length differ");
        if(bases.length <= 0 || bases.length > 16)
            throw new IllegalArgumentException("invalid bases length");

        // Determine sizes.
        int base_size = BigIntUtil.byte_size(modulus);
        int exponent_size = 0;
        for(int i = 0; i < bases.length; i++) {
            if(base_size < BigIntUtil.byte_size(bases[i]))
                base_size = BigIntUtil.byte_size(bases[i]);
            if(exponent_size < BigIntUtil.byte_size(exponents[i]))
                exponent_size = BigIntUtil.byte_size(exponents[i]);
        }

        // Adjust sizes to be a multiple of the digit sizes
        if(base_size % Bignat.size_multiplier != 0)
            base_size += 
                Bignat.size_multiplier - base_size % Bignat.size_multiplier;
        if(exponent_size % Bignat.size_multiplier != 0)
            exponent_size += 
                Bignat.size_multiplier - exponent_size % Bignat.size_multiplier;

        // Adjust base size for Montgomery multiplication.
        base_size += 2 * Bignat.size_multiplier;

        if(base_size > Short.MAX_VALUE || exponent_size > Short.MAX_VALUE)
            throw new IllegalArgumentException("base or exponent too long");

        // Prepare modulus, this will compute all necessary incredients 
        // for montgomery multiplication internally and store them in hmod.
        Host_modulus hmod = new Host_modulus(base_size, modulus);
        Modulus bn_mod = new Modulus((short)base_size, false);
        Convert_serializable.to(hmod, bn_mod);
        Bignat bn_one = Convenience.bn_from_bi(base_size, hmod.mont_fac);

        // Montgomerize the base and fill it into a Host_vector.
        Host_vector hbase_mont = 
            Host_vector.make_montgomerized_vector(base_size, bases, hmod);

        Host_vector factors = 
            Host_vector.make_montgomerized_factors(base_size, bases, hmod);

        // Prepare Bignat (Java Card) versions of base, pow and factors.
        Vector bn_bases = new Vector((short)base_size, (short)bases.length,
                                     true, false);
        Vector bn_exponents = new Vector((short)exponent_size,
                                         (short)exponents.length,
                                         false, false);
        Vector bn_factors = new Vector((short)base_size,
                                       factors.get_length(), 
                                       false, false);

        // Allocate result and a temporary.
        Bignat bn_result = new Bignat((short)base_size);
        Bignat temp = new Bignat((short)base_size);

        // Convert BigInteger arguments into Bignat arguments.
        Convert_serializable.to(hbase_mont, bn_bases);
        Convert_serializable.to(new Host_vector(exponent_size, exponents), 
                                bn_exponents);
        Convert_serializable.to(factors, bn_factors);

        // Compute the exponent, result will be montgomerized, since the 
        // bn_base and the bn_factors are.
        long start = System.nanoTime();
        bn_bases.exponent_mod(bn_exponents, bn_mod, (short)bases.length, 
                              bn_factors, bn_one, bn_result, temp);
        long duration = System.nanoTime() - start;

        // Demontgomerize.
        bn_result.demontgomerize(bn_mod);
        BigInteger bi_result = bi_from_bn(bn_result);

        return new Timed_result(bi_result, duration);
    }
}
