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
// Created 18.9.08 by Hendrik
// 
// host counter part for Modulus, doing the initilization with BigInteger
// 
// $Id: Host_modulus.java,v 1.23 2009-06-19 20:37:35 tews Exp $

#include "bignatconfig"


package ds.ov2.bignat;

import java.math.BigInteger;
import ds.ov2.util.APDU_Serializable;
#ifdef BIGNAT_USE_BYTE
   import ds.ov2.util.APDU_short;
#else
   import ds.ov2.util.APDU_long;
#endif
import ds.ov2.util.Serializable_array;


/** 
 * Host counterpart of {@link Modulus}. Used to send and receive
 * objects of type {@link Modulus} to and from the card. The
 * constructor conveniently computes auxiliary numbers from the pure
 * modulus that are needed on the card ({@link
 * Modulus#last_digit_inverse}) and on the host ({@link #mont_fac},
 * {@link #demont_fac}). 
 * <P>
 *
 * This class can only be used for odd moduli, because it is designed
 * to be used with <a
 * href="package-summary.html#montgomery_factor">Montgomery
 * multiplication.</a>
 * <P>
 *
 * This class provides a {@link BigInteger} interface to the outside.
 * When communicating with the card, conversion to and from {@link
 * Bignat} is done internally with the help of {@link
 * APDU_BigInteger}.
 * <P>
 *
 * For checking <a
 * href="../util/APDU_Serializable.html#apdu_compatibility">compatibility</a>
 * and sizes it is necessary to specify the size of the {@link Bignat}
 * inside the {@link Modulus} on the card in advance. This size will
 * be called the <U><I>configured size</I></U> hereafter. The Modulus
 * must be less than {@code 2^(bignat_size * 8)}.
 * <P>
 *
 * This is a host data type. It is compatible with nothing. The card
 * data type {@link Modulus} is compatible with this class. 
 *
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#BIGNAT_USE_BYTE ">BIGNAT_USE_BYTE </a>,
 *   <a href="../../../overview-summary.html#DOUBLE_DIGIT_TYPE">DOUBLE_DIGIT_TYPE</a>,
 *   <a href="../../../overview-summary.html#APDU_DOUBLE_DIGIT_TYPE">APDU_DOUBLE_DIGIT_TYPE</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.23 $
 * @commitdate $Date: 2009-06-19 20:37:35 $ by $Author: tews $
 * @environment host
 */
public class Host_modulus 
    extends Serializable_array
    implements APDU_Serializable 
{

    /**
     * 
     * Configured Bignat size. Initialized from the constructor
     * argument {@code bignat_size}.
     */
    public final int bignat_size;

    /**
     * 
     * Modulus. Initialized in the {@link #Host_modulus(int,
     * BigInteger) sending constructor} or after receipt.
     */
    public BigInteger m;

    /**
     * 
     * <a
     * href="package-summary.html#montgomery_factor">Montgomerization</a>
     * factor. Initialized only by the {@link #Host_modulus(int,
     * BigInteger) sending constructor}. 
     * <P>
     *
     * To montgomerize a number {@code x} on the host, compute
     * {@code x.multiply(mod.mont_fac).mod(mod.m)}, where {@code mod}
     * is the Host_modulus. 
     */
    public BigInteger mont_fac;

    /**
     * 
     * <a
     * href="package-summary.html#montgomery_factor">Demontgomerization</a>
     * factor. Initialized only by the {@link #Host_modulus(int,
     * BigInteger) sending constructor}.
     * <P>
     *
     * To demontgomerize a number {@code x} on the host, compute
     * {@code x.multiply(mod.demont_fac).mod(mod.m)}, where {@code mod}
     * is the Host_modulus. 
     */
    public BigInteger demont_fac;

    /**
     * 
     * Negated modular inverse of the last digit of {@link #m}. See {@link
     * Modulus#last_digit_inverse}. Initialized to the correct value
     * in the {@link #Host_modulus(int, BigInteger) sending
     * constructor}. Left uninitialized by the {@link
     * #Host_modulus(int) receiving constructor}. Is set to the
     * received value after receipt. 
     */
    public long last_digit_inverse;


    /**
     * 
     * APDU wrapper for the modulus {@link #m}.
     */
    private final APDU_BigInteger apdu_m;

    /**
     * 
     * APDU wrapper of type APDU_DOUBLE_DIGIT_TYPE for {@link
     * #last_digit_inverse}. 
     */
    private final APDU_DOUBLE_DIGIT_TYPE apdu_last_digit_inverse;

    /**
     * 
     * Serializable array for the {@link Serializable_array} base
     * class. Will contain {@link Modulus#serializable_contents_length}
     * length elements, currently the modulus {@link #m} and {@link
     * #apdu_last_digit_inverse} in this order. Must be compatible with
     * {@link Modulus#serializable_contents}.
     */
    private final APDU_Serializable[] serializable_array;


    /**
     * 
     * Constructor for receiving. Initializes the arrays and the APDU
     * wrappers but not the auxiliary data {@link #mont_fac}, {@link
     * #demont_fac} and {@link #last_digit_inverse}. The two factors
     * remains invalid for ever, even after a complete receipt from
     * the card. Only the modulus {@link #m} and {@link
     * #last_digit_inverse} will be initialized after
     * receipt. <P>
     * 
     * Only a {@link Modulus} of size {@code bignat_size} is
     * compatible with an object created from this constructor.
     * 
     * @param bignat_size configured size
     */
    public Host_modulus(int bignat_size) {
        assert 0 < bignat_size && bignat_size <= Short.MAX_VALUE;
        this.bignat_size = bignat_size;

        apdu_m = new APDU_BigInteger((short)bignat_size);
        apdu_last_digit_inverse = 
            new APDU_DOUBLE_DIGIT_TYPE((DOUBLE_DIGIT_TYPE)last_digit_inverse);

        serializable_array = 
            new APDU_Serializable[Modulus.serializable_contents_length];
        serializable_array[0] = apdu_m;
        serializable_array[1] = apdu_last_digit_inverse;            

        m = null;
        mont_fac = null;
        demont_fac = null;
        last_digit_inverse = -1;

        return;
    }


    /**
     * 
     * Constructor for sending. Initializes everything. 
     * <P>
     * 
     * Only a {@link Modulus} of size {@code bignat_size} is
     * compatible with an object created from this constructor.
     * 
     * @param bignat_size configured size in bytes
     * @param m modulus
     */
    public Host_modulus(int bignat_size, BigInteger m) {
        this(bignat_size);

        this.m = m;
        BigInteger two = new BigInteger(new byte[]{2});
        BigInteger pow_two = 
            two.pow((bignat_size - (2 * Bignat.size_multiplier)) * 8);
        demont_fac = pow_two.modInverse(m);
        mont_fac = pow_two.mod(m);

        BigInteger bignat_digit;
        if(Bignat.use_byte_digits)
            bignat_digit = new BigInteger(new byte[]{1, 0});
        else
            bignat_digit = new BigInteger(new byte[]{1, 0, 0, 0, 0});
        last_digit_inverse = 
            (m.modInverse(bignat_digit).longValue() * (-1)) 
                & Bignat.digit_mask;
        assert
            m.multiply(BigInteger.valueOf(last_digit_inverse)).
            mod(bignat_digit).longValue() == Bignat.digit_mask;

        apdu_m.value = m;
        apdu_last_digit_inverse.value = (DOUBLE_DIGIT_TYPE)last_digit_inverse;
    }


    //########################################################################
    // Serializable_array support
    // 

    /**
     * Return {@link #serializable_array} in support for abstract
     * {@link Serializable_array}.
     *
     * @return array of objects to (de-)serialize
     */
    protected APDU_Serializable[] get_array() {
        return serializable_array;
    }


    /**
     * Deserialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#from_byte_array 
     * APDU_Serializable.from_byte_array}.
     * <P>
     *
     * Overridden here to set {@link #m} and {@link
     * #last_digit_inverse} after receipt.
     *
     * @param len available data in {@code byte_array}
     * @param this_index number of bytes that
     * have already been read in preceeding calls
     * @param byte_array data array to deserialize from
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually read, except for the case 
     * where deserialization finished by reading precisely 
     * {@code len} bytes, in this case {@code len + 1} is 
     * returned.
     */
    // Override to copy data back.
    public short from_byte_array(short len, short this_index,
                                 byte[] byte_array, short byte_index) {
        short res = super.from_byte_array(len, this_index,
                                          byte_array, byte_index);
        if(res != len) {
            m = apdu_m.value;
            last_digit_inverse = apdu_last_digit_inverse.value;
        }

        return res;
    }   
}
