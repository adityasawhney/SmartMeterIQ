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
// Created 23.9.08 by Hendrik
// 
// Card methods for vector exponents
// 
// $Id: Exponent_perf_card.java,v 1.12 2009-05-25 07:51:19 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.test;
#endif


#ifdef JAVADOC
  import ds.ov2.bignat.Bignat;
  import ds.ov2.bignat.Modulus;
  import ds.ov2.bignat.Vector;
  import ds.ov2.bignat.RSA_exponent;
#endif


/** 
 * Methods for the vector_exp protocol. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.12 $
 * @commitdate $Date: 2009-05-25 07:51:19 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 */
PUBLIC class Exponent_perf_card {

    /**
     * 
     * The cipher used for exponentiations. Alias of {@link
     * Bignats#rsa_exponent}.
     */
    private final RSA_exponent rsa_exponent;

    /**
     * 
     * The cipher for the squarings if squared multiplication is used.
     * Alias of {@link Bignats#square_exp}.
     */
    private final RSA_exponent square_exponent;

    /**
     * 
     * The result. Alias of {@link Bignats#r_1}. Must contain the same
     * reference as {@link Exponent_perf_description#result}.
     */
    public final Bignat result;

    /**
     * 
     * First temporary. Alias of {@link Bignats#r_2}.
     */
    private final Bignat temp_1;

    /**
     * 
     * Second temporary. Alias of {@link Bignats#r_3}.
     */
    private final Bignat temp_2;

    /**
     * 
     * Third temporary. Alias of {@link Bignats#r_4}.
     */
    private final Bignat temp_3;


    /**
     * 
     * Constructor. Initializes all the aliases to {@code bignats}.
     * 
     * @param bignats data allocation instance
     */
    public Exponent_perf_card(Bignats bignats) {
        rsa_exponent = bignats.rsa_exponent;
        square_exponent = bignats.square_exp;
        result = bignats.r_1;
        temp_1 = bignats.r_2;
        temp_2 = bignats.r_3;
        temp_3 = bignats.r_4;
        return;
    }


    /**
     * 
     * Initialization method of the vector_exp_init step. For the init
     * step the protocol layer transfers all the data (most
     * importantly bases, exponents, modulus and base factors) to the
     * applet. This method initializes the ciphers to be used for
     * exponentiation (if any).
     * 
     * @param modulus modulus
     * @param variant multi-power implementation to use
     * @param keep_modulus true if the modulus is the same as in the
     * last test and it therefore should not be installed again; false
     * to reinstall the modulus in all the ciphers.
     */
    public void vector_exp_init(Modulus modulus, short variant, 
                                boolean keep_modulus) 
    {
        switch(variant){
        case 0:                 // exponent_mod
            break;
        case 1:                 // mont_rsa_exponent_mod
            if(!keep_modulus)
                rsa_exponent.set_modulus(modulus.m, (short)2);
            break;
        case 2:                 // squared_rsa_exponent_mod
            if(!keep_modulus) {
                rsa_exponent.set_modulus(modulus.m, (short)0);
                square_exponent.set_modulus(modulus.m, (short)0);
                temp_1.two();
                square_exponent.set_exponent(temp_1, temp_2, (short)0);
            }
            break;
        default:
            ASSERT(false);
        }
    }


    /**
     * 
     * Computation method for the vector_exp_1 and vector_exp_2 steps.
     * Performs the computation if {@code doit} is true. Stores the
     * moduluar multi-power {@code bases[0]^exponent[0] *
     * bases[1]^exponent[1] ... mod modulus} in {@link #result}. 
     * 
     * @param base the bases
     * @param exponent the exponents
     * @param modulus modulus
     * @param base_factor_size number of bases for which {@code
     * base_factors} contains precomputed products (only relevant for
     * the PURE_JAVA variant)
     * @param base_factors the precomputed products of the bases
     * @param one_or_correction a montgomerized one or a suitable
     * Montgomery correction factor
     * @param variant the implementation variant to use, 0 for {@link
     * Exponent_perf_host.Vector_exponent_variant#PURE_JAVA}, 1 for
     * {@link Exponent_perf_host.Vector_exponent_variant#MONT_RSA}, 2
     * for {@link
     * Exponent_perf_host.Vector_exponent_variant#SQUARED_RSA}
     * @param doit if false skip to call to the multi-power method to
     * measure the transmission overhead
     */
    public void vector_exp(Vector base, Vector exponent, Modulus modulus, 
                           short base_factor_size, Vector base_factors, 
                           Bignat one_or_correction,
                           short variant, 
                           boolean doit) 
    {
        switch(variant) {
        case 0:                 // exponent_mod
            if(doit) 
                base.exponent_mod(exponent, modulus, base_factor_size, 
                                  base_factors, one_or_correction,
                                  result, temp_1);
            break;
        case 1:                 // mont_rsa_exponent_mod
            if(doit)
                base.mont_rsa_exponent_mod(exponent, modulus, 
                                           rsa_exponent,
                                           one_or_correction,
                                           result, temp_1, temp_2);
            break;
        case 2:
            if(doit)
                base.squared_rsa_exponent_mod(exponent, modulus, result,
                                              rsa_exponent, square_exponent,
                                              temp_1, temp_2, temp_3);
            break;
        default:
            ASSERT(false);
        }
    }
}
