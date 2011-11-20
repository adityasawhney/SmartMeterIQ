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
// Created 1.2.09 by Hendrik
// 
// central place with all Bignats, Vectors, and RSA exponents for the
// test applet
// 
// $Id: Bignats.java,v 1.7 2010-02-18 12:40:39 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.test;
#endif


#ifndef JAVACARD_APPLET
    import ds.ov2.bignat.Bignat;
    import ds.ov2.bignat.Modulus;
    import ds.ov2.bignat.Vector;
    import ds.ov2.bignat.Resize;
#endif

#ifdef JAVADOC
    import ds.ov2.bignat.RSA_exponent;
#endif


/**
 * Central point for allocating all {@link Bignat Bignat's}, {@link
 * Vector Vector's} and {@link RSA_exponent RSA_exponent's}. Central
 * allocation and reuse in different protocols lowers the memory
 * consumption, especially of scare RAM.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.7 $
 * @commitdate $Date: 2010-02-18 12:40:39 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
 *
 */

PUBLIC class Bignats {

    /** First long EEPROM Bignat. */
    public final Bignat n_1;

    /** Second long EEPROM Bignat. */
    public final Bignat n_2;

    /** First short EEPROM Bignat. */
    public final Bignat s_1;

    /** Second short EEPROM Bignat. */
    public final Bignat s_2;

    /** Modulus without multiples. */
    public final Modulus modulus;

    /** Modulus with multiples. */
    public final Modulus mult_modulus;

    /** First long RAM Bignat. */
    public final Bignat r_1;

    /** Second long RAM Bignat. */
    public final Bignat r_2;

    /** Third long RAM Bignat. */
    public final Bignat r_3;

    /** Fourth long RAM Bignat. */
    public final Bignat r_4;

    /** First double-sized RAM Bignat. */
    public final Bignat dr_1;

    // 5 bytes RAM for digit buf
    /** Bases vector */
    public final Vector base;

    /** Exponent vector */
    public final Vector exponent;

    /** Base factors vector */
    public final Vector base_factors;


    #if defined(JAVACARD_APPLET) || defined(JAVADOC)

        // each RSA cipher 2 bytes RAM

        /** RSA cipher instance for the normal computations. */
        public final RSA_exponent rsa_exponent;

        /** RSA cipher instance for squaring. Used in {@link
         *  Bignat#squared_rsa_mult_2 Bignat.squared_rsa_mult_2},
         *  {@link Bignat#short_squared_rsa_mult_2
         *  Bignat.short_squared_rsa_mult_2}, {@link
         *  Bignat#squared_rsa_mult_4 Bignat.squared_rsa_mult_4} and
         *  {@link Bignat#short_squared_rsa_mult_4
         *  Bignat.short_squared_rsa_mult_4} */
        public final RSA_exponent square_exp;

    #endif


    /**
     * 
     * Allocate all {@link Bignat Bignat's}, {@link Vector Vector's}
     * and {@link RSA_exponent RSA_exponent's} used in the test
     * applet. 
     * 
     * @param short_bignat_size size of the short Bignats in bytes
     * @param long_bignat_size size of the long Bignats in bytes
     * @param max_vector_length maximal length of the bases and
     * exponent vectors
     */
    public Bignats(short short_bignat_size, short long_bignat_size,
                   short double_bignat_size, short max_vector_length) 
    {

        n_1 = new Bignat(long_bignat_size, false);
        Resize.register_long_bignat(n_1);
        
        n_2 = new Bignat(long_bignat_size, false);
        Resize.register_long_bignat(n_2);

        s_1 = new Bignat(short_bignat_size, false);
        Resize.register_short_bignat(s_1);

        s_2 = new Bignat(short_bignat_size, false);
        Resize.register_short_bignat(s_2);

        modulus = new Modulus(long_bignat_size, false);
        modulus.register_long_bignats();

        mult_modulus = new Modulus(long_bignat_size, false);
        mult_modulus.allocate_multiples();
        mult_modulus.register_long_bignats();

        r_1 = new Bignat(long_bignat_size, true);
        Resize.register_long_bignat(r_1);

        r_2 = new Bignat(long_bignat_size, true);
        Resize.register_long_bignat(r_2);

        r_3 = new Bignat(long_bignat_size, true);
        Resize.register_long_bignat(r_3);

        r_4 = new Bignat(long_bignat_size, true);
        Resize.register_long_bignat(r_4);

        dr_1 = new Bignat(double_bignat_size, true);
        Resize.register_double_bignat(dr_1);


        base = new Vector(long_bignat_size, max_vector_length, true, false);
        base.register_long_bignats();
        Resize.register_vector(base);


        exponent = new Vector(short_bignat_size, max_vector_length, 
                              false, false);
        exponent.register_short_bignats();
        Resize.register_vector(exponent);

        base_factors = new Vector(long_bignat_size, 
                                  (short)((1 << max_vector_length) -1),
                                  false, false);
        base_factors.register_long_bignats();
        Resize.register_factor(base_factors);


        #ifdef JAVACARD_APPLET
            rsa_exponent = new RSA_exponent(long_bignat_size);
            Resize.register_long_rsa(rsa_exponent);

            square_exp = new RSA_exponent(long_bignat_size);
            Resize.register_long_rsa(square_exp);
        #endif
    }
}
