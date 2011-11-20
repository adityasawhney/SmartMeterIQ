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
// measure the multiplication performance
// 
// $Id: Performance_mult_card.java,v 1.10 2009-06-02 13:27:57 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.test;
#endif

import javacard.framework.APDU;

#ifdef JAVADOC
  import ds.ov2.bignat.Bignat;
  import ds.ov2.bignat.Modulus;
#endif


/** 
 * Card/applet methods for testing Montgomery multiplication,
 * demontgomerization and division. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.10 $
 * @commitdate $Date: 2009-06-02 13:27:57 $ by $Author: tews $
 * @environment card
 */
PUBLIC class Performance_mult_card {

    /**
     * 
     * Empty constructor.
     */
    public Performance_mult_card() {}


    /**
     * 
     * Test Montgomery multiplication. Perform {@code rounds} calls to
     * {@link Bignat#montgomery_mult Bignat.montgomery_mult}.
     * 
     * @param fac_1 first factor
     * @param fac_2 second factor
     * @param m modulus
     * @param result result reference
     * @param rounds number of multiplication rounds
     */
    public void mont_mult(Bignat fac_1, Bignat fac_2, Modulus m,
                     Bignat result, short rounds) 
    {
        for(short i = 0; i < rounds; i++) {
            result.montgomery_mult(fac_1, fac_2, m);
        }
        return;
    }


    /**
     * 
     * Test Demontgomerization. Perform {@code rounds} calls to {@link
     * Bignat#demontgomerize Bignat.demontgomerize}.
     * 
     * @param n number to demontgomerize
     * @param m modulus
     * @param result result reference
     * @param rounds number of demontgomerization rounds
     */
    public void demontgomerize(Bignat n, Modulus m, 
                               Bignat result, short rounds) 
    {
        result.copy(n);
        if(rounds > 0) {
            result.demontgomerize(m);
            while(rounds-- > 1) {
                result.copy(n);
                result.demontgomerize(m);
            }
        }
    }


    /**
     * 
     * Test division and remainder. Perform {@code rounds} calls to
     * {@link Bignat#remainder_divide Bignat.remainder_divide}.
     * 
     * @param divident divident argument
     * @param divisor divisor argument
     * @param remainder reference for the remainder result
     * @param result reference for the quotient result (may be null)
     * @param rounds number of division rounds
     */
    public void remainder_divide(Bignat divident, Bignat divisor, 
                                 Bignat remainder, Bignat result, 
                                 short rounds) 
    {
        for(short i = 0; i < rounds; i++) {
            remainder.copy(divident);
            remainder.remainder_divide(divisor, result);
        }
        return;
    }
}
