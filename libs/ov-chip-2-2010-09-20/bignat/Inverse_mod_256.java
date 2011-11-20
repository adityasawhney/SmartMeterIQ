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
// Created 22.8.08 by Hendrik
// 
// computes inverse modulo 256 of odd numbers
// CURRENTLY UNUSED
// 
// $Id: Inverse_mod_256.java,v 1.11 2009-03-26 15:51:28 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.bignat;
#endif


/** 
 * Modular inverse modulo 256 for Java Card (currently unused). Can be
 * used to compute {@link Modulus#last_digit_inverse} on the Card.
 * Currently unused, because the last_digit_inverse is much more
 * conviniently computed in {@link Host_modulus}.
 * <P>
 *
 * Static class.
 *
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#ASSERT_TAG">ASSERT_TAG(condition, tag)</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.11 $
 * @commitdate $Date: 2009-03-26 15:51:28 $ by $Author: tews $
 * @environment currently not used
 */
class Inverse_mod_256 {

    /**
     * 
     * Static class, object construction disabled.
     */
    protected Inverse_mod_256() {}


    /**
     * 
     * Modular inverse modulo 256. Computes {@code x} such that {@code
     * x * n == 1 } (modulo 256). Uses the iterative method of the
     * <a
     * href="http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm">extended
     * eucledian algorithm.</a>
     * <P>
     * 
     * The inverse does only exists for odd arguments.
     * 
     * @param n n
     * @return the inverse of {@code n} modulo 256
     */
    public static short get_inverse(short n) {
        // n must be odd and in the range 1 .. 255 
        ASSERT(n < 256 && n > 0 && n % 2 == 1);

        short y_very_old;
        short y_old = 0;
        short y = 1;

        short a_old;
        short a = 256;
        short b = n;
        while(a % b != 0) {
            // System.out.format("%d * %d = %d (modulo 256)\n",
            //                y, n, b);
            y_very_old = y_old;
            y_old = y;
            y = (short)(y_very_old - (a / b) * y_old);

            a_old = a;
            a = b;
            b = (short)(a_old % b);

            // System.out.format("y %d * %d * %d | a %d * %d | b %d\n",
            //                y_very_old, y_old, y, a_old, a, b);
        }
        if(y < 0) y += 256;

        // System.out.format("GI n = %d y = %d b = %d\n",
        //                n, y, b);

        ASSERT(y > 0 && y < 256);
        ASSERT(b == 1 && ((short)(n * y) & 0xff) == 1);

        return y;
    }
}
