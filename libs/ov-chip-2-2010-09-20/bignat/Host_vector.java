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
// Host side of Vector, computing with BigInteger
// 
// $Id: Host_vector.java,v 1.21 2009-06-19 20:37:35 tews Exp $

package ds.ov2.bignat;

import java.math.BigInteger;
import ds.ov2.util.APDU_Serializable;
import ds.ov2.util.Serializable_array;


/** 
 * Host counterpart of {@link Vector}. Used to send and receive
 * vectors (i.e., arrays of {@link Bignat}) to and from the card.
 * Contains convenience methods to set up arrays of <a
 * href="package-summary.html#montgomery_factor">montgomerized</a>
 * numbers, to prepare the factors array needed for {@link
 * Vector#exponent_mod Vector.exponent_mod} and for creating arrays
 * with montgomery correction factors.
 * <P>
 *
 * Provides a {@link BigInteger} interface to the outside. Internally
 * {@link APDU_BigInteger} is used for the conversion from BigInteger
 * to {@link Bignat}.
 * <P>
 *
 * Objects of this class only contain {@link BigInteger BigIntegers},
 * which are variables sized. When sent to the card the numbers are
 * converted to {@link Bignat Bignats}, which have a fixed size. The
 * conversion to Bignats can only succeed if all the BigIntegers fit
 * into the size of the Bignats. This size constraint is checked in
 * the <a
 * href="../util/APDU_Serializable.html#apdu_compatibility">compatibility
 * check</a> and in {@link APDU_BigInteger#to_byte_array
 * APDU_BigInteger.to_byte_array}. But for these checks it is
 * necessary to specify the size of the Bignats on the card in advance
 * in {@code bignat_size} arguments of the various constructors. This
 * size is called the <U><I>configured size</I></U> hereafter. The
 * BigIntegers that end up in the host vector must be less than {@code
 * 2^(bignat_size * 8)}. <P>
 *
 * Host data type. Compatible with nothing. {@link Vector} is
 * compatible to this class if Bignat size and length match.
 *
 * <P>
 *
 * For a number of general topics <a
 * href="package-summary.html#package_description">see also the package
 * description.</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.21 $
 * @commitdate $Date: 2009-06-19 20:37:35 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Host_vector 
    extends Serializable_array
    implements APDU_Serializable 
{

    /**
     * 
     * BigInteger contents. Initialized in the constructor or after
     * receipt.
     */
    public final BigInteger[] a;


    /**
     * 
     * Configured size (in bytes) of the numbers.
     */
    public final int bignat_size;


    /**
     * 
     * Array for the transmission to and from the card.
     */
    private final APDU_BigInteger[] sa;


    /**
     * 
     * Constructor for sending. Creates a Host_vector out of contents
     * of {@code args}. The configured size of the numbers will be
     * {@code bignat_size}. Only {@link Vector Vectors} with the same
     * length (the same number of elements) and where the elements
     * have size {@code bignat_size} are compatible to the newly
     * created object. 
     * <P>
     *
     * The elements of {@code args} must not be negative and they must
     * fit into {@code bignat_size} bytes, i.e., be lesser than
     * 2^(bignat_size * 8).
     * 
     * @param bignat_size configured maximal size of the numbers in bytes 
     * @param args contents
     */
    public Host_vector(int bignat_size, BigInteger[] args) {
        assert 0 <= bignat_size && bignat_size <= Short.MAX_VALUE;
        this.bignat_size = bignat_size;
        int length = args.length;
        a = new BigInteger[length];
        System.arraycopy(args, 0, a, 0, length);
        sa = new APDU_BigInteger[length];
        for(int i = 0; i < length; i++)
            sa[i] = new APDU_BigInteger((short)bignat_size, a[i]);
        return;
    }


    /**
     * 
     * Constructor for receiving. Sets up an empty Host_vector of
     * length {@code length} for receiving {@link Bignat Bignats} of
     * size {@code bignat_size}. After receiving the numbers are
     * available in the array {@link #a}.
     * 
     * @param bignat_size configured Bignat size
     * @param length array size
     */
    public Host_vector(int bignat_size, int length) {
        assert 0 <= bignat_size && bignat_size <= Short.MAX_VALUE;
        // System.out.format("ZZ bs %d l %d\n", bignat_size, length);
        this.bignat_size = bignat_size;
        a = new BigInteger[length];
        sa = new APDU_BigInteger[length];
        for(int i = 0; i < length; i++)
            sa[i] = new APDU_BigInteger((short)bignat_size);
        return;
    }


    /**
     * 
     * Constructor for sending montgomerized numbers to the card. <a
     * href="package-summary.html#montgomery_factor">Montgomerizes</a>
     * the numbers in {@code args} and turns them into a Host_vector
     * of configured size {@code bignat_size}.
     * <P>
     *
     * This is a static method, because of the stupid restriction that
     * a call to a different constructor must be the first statement. 
     * 
     * @param bignat_size configured maximal size of the numbers in
     * bytes
     * @param args not yet montgomerized numbers 
     * @param mod modulus
     * @return Host_vector with the montgomerization of {@code args}
     * and configured size {@code bignat_size}
     */
    public static Host_vector make_montgomerized_vector(int bignat_size,
                                                        BigInteger[] args, 
                                                        Host_modulus mod) {
        // Do not modify args!
        BigInteger[] oargs = new BigInteger[args.length];
        for(int i = 0; i < args.length; i++)
            oargs[i] = args[i].multiply(mod.mont_fac).mod(mod.m);
        return new Host_vector(bignat_size, oargs);
    }


    // make_factors produces an Vector containing
    // all possible montgomerized products of the numbers in
    // args, besides the empty product. Thus it contains
    // 2^(args.length) -1 numbers. 
    // 
    // make_factors[0] = args[0]
    // make_factors[1] = args[1]
    // make_factors[2] = args[1] * args[0]
    // make_factors[3] = args[2]
    // make_factors[4] = args[2] * args[0]
    // make_factors[5] = args[2] * args[1]
    // make_factors[6] = args[2] * args[1] * args[0]
    // ...
    // The elements in the new Vector are not the products as 
    // in the table above, but their montgomerized versions.
    /**
     * 
     * Make montgomerized base factors for {@link Vector#exponent_mod
     * Vector.exponent_mod}. Computes all productes of numbers in args
     * and returns them in <a
     * href="package-summary.html#montgomery_factor">montgomerized</a>
     * form in a Host_vector of size {@code 2^args.length -1} (the
     * empty product is excluded). The products are ordered as
     * described for {@link Vector#exponent_mod Vector.exponent_mod}.
     * The returned Host_vector has configured size {@code
     * bignat_size}.
     * <P>
     *
     * This is a static method for the same stupid reason as {@link
     * #make_montgomerized_vector}.
     * 
     * @param bignat_size configured maximal size
     * @param args base array to compute the factors for
     * @param mod modulus
     * @return Host_vector of factors as required by {@link
     * Vector#exponent_mod Vector.exponent_mod} 
     */
    public static Host_vector make_montgomerized_factors(int bignat_size,
                                                         BigInteger[] args, 
                                                         Host_modulus mod) 
    {
        assert args.length < 16;
        int size = (1 << args.length) -1;
        BigInteger[] facs = new BigInteger[size];
        for(int i = 0; i < size; i++) {
            facs[i] = BigInteger.ONE;
            for(int j = 0; j < args.length; j++)
                if(((i + 1) & (1 << j)) != 0)
                    facs[i] = facs[i].multiply(args[j]).mod(mod.m);
        }

        return make_montgomerized_vector(bignat_size, facs, mod);
    }

        
    /**
     * 
     * Make Montgomery corrections. Returns a Host_vector with the
     * first {@code len} Montgomery corrections, that is with
     * <pre>
     *    mod.mont_fac^1
     *    mod.mont_fac^2
     *    mod.mont_fac^3
     *    ....
     * </pre>
     * Everything modulo {@code mod.m} of course. 
     * <P>
     *
     * This is a static method for the same stupid reason as {@link
     * #make_montgomerized_vector}.
     * 
     * @param bignat_size configured size of the returned Host_vector.
     * @param len length of the returned Host_vector
     * @param mod modulus
     * @return Host_vector with the first {@code len} montgomery
     * corrections. 
     */
    public static Host_vector make_montgomery_corrections(int bignat_size,
                                                          int len,
                                                          Host_modulus mod) 
    {
        BigInteger[] res = new BigInteger[len];
        for(int i = 0; i < len; i++)
            res[i] = mod.mont_fac.pow(i + 2).mod(mod.m);
        return new Host_vector(bignat_size, res);
    }


    //########################################################################
    // Serializable_array support
    // 

    /**
     * Return the {@link APDU_BigInteger} array {@link #sa} in support
     * for abstract {@link Serializable_array}.
     *
     * @return array of objects to (de-)serialize
     */
    protected APDU_Serializable[] get_array() {
        return sa;
    }


    /**
     * Deserialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#from_byte_array 
     * APDU_Serializable.from_byte_array}.
     * <P>
     * 
     * Overridden here to initialize the array {@link #a} after
     * receipt. 
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
    // Override to initialize the BigInteger array after deserialization.
    public short from_byte_array(short len, short this_index,
                                 byte[] byte_array, short byte_index) {
        short res = super.from_byte_array(len, this_index,
                                          byte_array, byte_index);
        if(res != len) {
            for(int i = 0; i < a.length; i++)
                a[i] = sa[i].value;
        }
        return res;
    }
}
