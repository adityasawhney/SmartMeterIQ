//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//!!!   DO NOT EDIT OR CHANGE THIS FILE. CHANGE THE ORIGINAL INSTEAD.      !!!
//!!!   THIS FILE HAS BEEN GENERATED BY CPP AND SED,                       !!!
//!!!   BECAUSE JAVA DOES NOT SUPPORT CONDITIONAL COMPILATION.             !!!
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../util/BigInteger_inputs.java"
//# 1 "<built-in>"
//# 1 "<command-line>"
//# 1 "/tmp/tews/ov-dist/ov-chip-2-2010-09-20/test/../util/BigInteger_inputs.java"
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
// Created 29.5.09 by Hendrik
// 
// storage behind -i and -hex options
// 
// $Id: BigInteger_inputs.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


import java.util.Vector;
import java.math.BigInteger;


/**
 * A collection of {@link BigInteger BigInteger} inputs. To be used in
 * conjunction with {@link BigInt_input_option} and {@link
 * BigInt_hex_input_option}. 
 * <P>
 *
 * Use {@link #pop} to pop off the first element from this collection.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class BigInteger_inputs extends Vector<BigInteger> {

    /** Field to disable the serialVersionUID warning. */
    public static final long serialVersionUID = 1L;

    /**
     * 
     * Construct a new instance for recording {@link BigInteger}
     * inputs. 
     * 
     */
    public BigInteger_inputs() {
        super();
    }

    /**
     * 
     * Check whether there are still {@code n} inputs available. If
     * there are some but not enough inputs the message {@code msg} is
     * printed with some diagnostics.
     * 
     * @param n number of fix inputs to check for
     * @param msg identification string for the warning message
     * @return true if {@code n} inputs are still available
     */
    public boolean has_n_inputs(int n, String msg) {
        if(size() == 0)
            return false;
        if(size() < n) {
            // inputs, but not enough
            System.err.format("%s: need %d inputs but found only %d. " +
                              "Ignore them.\n",
                              msg, n, size());
            return false;
        }
        return true;
    }


    /**
     * 
     * Return and remove the first element. Equal to {@link #remove
     * remove}{@code (0)}.
     * 
     * @return first element
     * @throws ArrayIndexOutOfBoundsException if the collection is
     * empty. 
     */
    public BigInteger pop() {
        return remove(0);
    }
}
