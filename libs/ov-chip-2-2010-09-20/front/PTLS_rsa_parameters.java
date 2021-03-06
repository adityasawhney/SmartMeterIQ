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
// Created 13.11.08 by Hendrik
// 
// PTLS keys, RSA variant
// 
// $Id: PTLS_rsa_parameters.java,v 1.22 2010-02-16 10:26:07 tews Exp $

#include <config>

package ds.ov2.front;


import java.util.Random;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateCrtKey;

import ds.ov2.util.BigIntUtil;
import ds.ov2.util.BigIntUtil;
import ds.ov2.util.Unsecure_rsa_key;
import ds.ov2.bignat.Bignat;
import ds.ov2.bignat.Host_modulus;



/** 
 * PTLS parameters. The PTLS parameters are the static parameters that
 * are needed for the OV-chip RSA protocols. They need to be generated
 * once before the system starts. The PTLS parameter set contains the
 * key material as well as the number of attributes and the key
 * length'.
 * <P>
 *
 * Montgomerization and Demontgomerization factors depend on the RSA
 * modulus {@link #n} and they are therefore included here (in the
 * host modulus {@link #hmod}). However, they also depend on the
 * number of Montgomery digits and therefore on the applet type. Some
 * methods here take therefore an applet type argument, although the
 * PTLS parameters itself are independent of the applet type.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.22 $
 * @commitdate $Date: 2010-02-16 10:26:07 $ by $Author: tews $
 * @environment host
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>,
 *   <a href="../../../overview-summary.html#TESTFRAME">TESTFRAME<a>
 */
public class PTLS_rsa_parameters {

    /** Length of the bases and the RSA modulus in bits (long bignats).
     */
    public int base_length;

    /** The length of the attributes in bits (short or exponent bignats).
     */
    public int exponent_length;

    /** The number of attributes.
     */
    public final int attribute_number;

    /** The RSA modulus, product of two primes.
     */
    public BigInteger n;

    /** The RSA modulus wrapped in a host modulus that contains
     * (de-)montgomerization factors as well.
     */
    public Host_modulus hmod;

    /** The RSA exponent v, coprime to euler_phi(n). All attributes
     * must be smaller than v.
     */
    public BigInteger v;

    /** 1/v, the inverse of v modulus euler_phi(n)
     */
    /* package */ BigInteger inv_v;

    /** The private key x of the PTLS.
     */
    private BigInteger ptls_private_key;

    /** The public key of the PTLS h = x^v.
     */
    public BigInteger ptls_public_key;

    /** The array of the bases. Has length {@link #attribute_number}.
     */
    public BigInteger[] base;


    /** Prime certainy: certainy parameter for probabilistic prime
     * generation. 
     */
    public final static int prime_certainy = 1000000000;


    /** Randomness source.
     */
    private final Random rand = new Random();


    /** Output channel for debug and progress messages.
     */
    private PrintWriter out;


    /** Verbosity level.
     */
    private int verbosity;


    /**
     * 
     * Constructor. Copies only the arguments into the state of this
     * instance. Does not generate new PTLS parameters.
     * 
     * @param attribute_number number of attributes
     * @param out debug/progress messages
     * @param verbosity verbosity
     */
    public PTLS_rsa_parameters(int attribute_number, 
                               PrintWriter out, 
                               int verbosity) 
    {
        this.attribute_number = attribute_number;
        this.out = out;
        this.verbosity = verbosity;
        return;
    }


    /**
     * 
     * Generate the public RSA exponent v. If compiled with <a
     * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>
     * and if another fixed input is available, take the next fixed
     * input as v. Otherwise generate it randomly satisfying the
     * necessary conditions.
     * 
     * @param p first factor of the RSA modulus
     * @param q second factor of the RSA modulus
     * @return v of bit-length {@link #exponent_length}, prime, and
     * coprime to phi(p * q)
     */
    private BigInteger[] generate_v(BigInteger p, BigInteger q) {
        BigInteger phi =  
            p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        if(out != null && verbosity >= 10) {
            out.format("phi = %s\n" +
                       "    = %s\n", 
                       phi,
                       BigIntUtil.to_byte_hex_string(phi));
        }

        BigInteger v, gcd;
        int v_count = 0;

        #ifdef APPLET_TESTFRAME
            if(Test_state.fix_inputs.has_n_inputs(1, "coprime v")) {
                v = Test_state.fix_inputs.pop();
            }
            else 
        #endif

            do {
                v = new BigInteger(exponent_length, prime_certainy, rand);
                v_count++;

                if(out != null && verbosity >= 10)
                    out.format("v[%d] = %s\n" +
                               "      = %s\n", 
                               v_count,
                               BigIntUtil.to_byte_hex_string(v),
                               v);
            }
            while(v.bitLength() != exponent_length ||
                  !BigIntUtil.coprime(phi, v));

        BigInteger inv_v = v.modInverse(phi);
        if(out != null && verbosity >= 10)
            out.format("v   = %s\n" +
                       "    = %s\n" +
                       "1/v = %s\n" +
                       "    = %s\n",
                       v,
                       BigIntUtil.to_byte_hex_string(v),
                       inv_v,
                       BigIntUtil.to_byte_hex_string(inv_v));

        return new BigInteger[]{v, inv_v};
    }


    /**
     * 
     * Compute the length of the bases and the RSA modulus in bytes
     * for this instance.
     * 
     * 
     * @param montgomery_digits number of montgomery digits
     * @return base length in bytes
     */
    public int make_base_bytes(int montgomery_digits) {
        return (this.base_length + 7) / 8 + 
            montgomery_digits * Bignat.size_multiplier;
    }


    /**
     * 
     * Compute the length of the attributes/exponents in bytes for
     * this instance.
     * 
     * @return attribute/exponent length in bytes
     */
    public int make_exponent_bytes() {
        int bytes = (this.exponent_length + 7) / 8;
        // Make sure this is divisible by Bignat.size_multiplier for
        // Bignat configurations different from byte/short.
        if(bytes % Bignat.size_multiplier == 0)
            return bytes;
        else
            return bytes + 
                Bignat.size_multiplier - (bytes % Bignat.size_multiplier);
    }


    /**
     * 
     * Generate a new set of PTLS parameters and store them in this
     * instance. For base length greater than 512 bits the (hopefully
     * secure) Java RSA key generator is used. For base length below
     * 512 bits {@link Unsecure_rsa_key} is used.
     * <P>
     *
     * If compiled with <a
     * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>,
     * and if fixed inputs are available, they are taken as follows.
     * First two inputs: factors of the RSA modulus {@link #n}. Input
     * number 3: {@link #v}. Input number 4: x, the private PTLS key
     * {@link #ptls_private_key}. The next {@link #attribute_number}
     * inputs: the bases.
     * 
     * @param base_length length of the bases and the RSA modulus in bits
     * @param exponent_length length of the attributes (exponents) in bits
     * @param applet_type current applet type for determining the
     * number of Montgomery digits and the (de-)montgomerization
     * factors
     * @throws NoSuchAlgorithmException if no provider for RSA key
     * generation can be found
     */
    public void generate(int base_length, int exponent_length, 
                         Applet_type applet_type) 
        throws NoSuchAlgorithmException 
    {
        this.base_length = base_length;
        this.exponent_length = exponent_length;

        if(out != null && verbosity >= 0) {
            out.format("start RSA key generation base %d bits " +
                       "exponent %d bits\n",
                       base_length, exponent_length);
        }

        // The secret factors of the modulus n.
        BigInteger p, q;


        #ifdef APPLET_TESTFRAME
            if(Test_state.fix_inputs.has_n_inputs(2, "RSA parameters")) {
                p = Test_state.fix_inputs.pop();
                q = Test_state.fix_inputs.pop();
                n = p.multiply(q);
            }
            else 
        #endif


        // For the square 4 RSA applet the modulus must be equivalent
        // to 1 modulo 4. Therefore do the keygeneration in a loop.
        do {
            if(base_length >= 512) {
                // For real key length' the SUN provided RSA engine works.

                // Make an RSA key to get the modulus n.
                KeyPairGenerator key_generator =
                    KeyPairGenerator.getInstance("RSA");
                key_generator.initialize(base_length);
                KeyPair key_pair = key_generator.generateKeyPair();
                n = ((RSAPublicKey)key_pair.getPublic()).getModulus();

                // Extract the factors p and q to be able to compute
                // Eulers phi(n).
                RSAPrivateCrtKey priv = (RSAPrivateCrtKey)key_pair.getPrivate();
                p = priv.getPrimeP();
                q = priv.getPrimeQ();

            }
            else {
                // For unsecurely short keys we use our own completely unsecure
                // key generator.

                BigInteger[] npq = Unsecure_rsa_key.generate(base_length,
                                                             10, // certainty
                                                             rand);
                n = npq[0];
                p = npq[1];
                q = npq[2];
            }
        }
        while(applet_type == Applet_type.SQUARED4_RSA_APPLET &&
              n.mod(new BigInteger("4")).intValue() != 1);


        if(out != null)
            out.println("RSA key generation complete");

        if(out != null && verbosity >= 5) {
            out.format("Got modulus %s\n" +
                       "    = %s\n", 
                       BigIntUtil.to_byte_hex_string(n),
                       n);
        }

        if(out != null && verbosity >= 10) {
            out.format("p = %s\n" +
                       "  = %s\n" +
                       "q = %s\n" +
                       "  = %s\n", 
                       BigIntUtil.to_byte_hex_string(p), 
                       p,
                       BigIntUtil.to_byte_hex_string(q),
                       q);
        }

        assert n.equals(p.multiply(q));

        hmod = 
            new Host_modulus(make_base_bytes(applet_type.montgomery_digits()),
                             n);

        // Generate v, prime and coprime to phi(n), together with 
        // its inverse.
        BigInteger vs[] = generate_v(p, q);
        v = vs[0];
        inv_v = vs[1];

        // Generate the secret ptls key x and determine the public key h.
        #ifdef APPLET_TESTFRAME
            if(Test_state.fix_inputs.has_n_inputs(1, "ptls private key")) {
                ptls_private_key = Test_state.fix_inputs.pop();
            }
            else
        #endif

            ptls_private_key = BigIntUtil.mod_rand_with_inverse(rand, n);

        ptls_public_key = ptls_private_key.modPow(v, n);

        if(out != null && verbosity >= 10) {
            out.format("ptls x : %s\n" +
                       "       = %s\n" +
                       "ptls h : %s\n" +
                       "       = %s\n" +
                       "mon fac= %s\n" +
                       "       = %s\n" +
                       "dem fac= %s\n" +
                       "       = %s\n" +
                       "bit length " +
                       "p : %s " +
                       "q : %s " +
                       "n : %s " +
                       "v : %s " +
                       "1/v : %s " +
                       "x : %s " +
                       "h : %s\n",
                       BigIntUtil.to_byte_hex_string(ptls_private_key),
                       ptls_private_key,
                       BigIntUtil.to_byte_hex_string(ptls_public_key),
                       ptls_public_key,
                       hmod.mont_fac,
                       BigIntUtil.to_byte_hex_string(hmod.mont_fac),
                       hmod.demont_fac,
                       BigIntUtil.to_byte_hex_string(hmod.demont_fac),
                       p.bitLength(),
                       q.bitLength(),
                       n.bitLength(),
                       v.bitLength(),
                       inv_v.bitLength(),
                       ptls_private_key.bitLength(),
                       ptls_public_key.bitLength());
        }

        // Generate the bases.
        base = new BigInteger[attribute_number];

        #ifdef APPLET_TESTFRAME
            if(Test_state.fix_inputs.has_n_inputs(attribute_number, "bases")) {
                for(int i = 0; i < attribute_number; i++)
                    base[i] = Test_state.fix_inputs.pop();
            }
            else
        #endif

            for(int i = 0; i < attribute_number; i++)
                base[i] = BigIntUtil.mod_rand_with_inverse(rand, n);

        if(out != null && verbosity >= 10)
            BigIntUtil.print_array(out, "%d bases\n", "base", base);


        if(out != null)
            out.println("PTLS parameter setup complete");
    }


    /**
     * 
     * Exception for I/O problems during reading/writing PTLS
     * parameters from/to disk.
     */
    public static class PTLS_io_exception extends IOException {
        /** Field for the serialVersionUID warning. */
        public static final long serialVersionUID = 1L;


        /**
         * 
         * Construct a new PTLS_io_exception with the given detailed
         * reason.
         * 
         * @param message detailed reason
         */
        public PTLS_io_exception(String message) {
            super(message);
        }


        /**
         * 
         * Constructs a new PTLS_io_exception with the given reason
         * and cause.
         * 
         * @param message detailed reason
         * @param cause cause
         */
        public PTLS_io_exception(String message, Throwable cause) {
            super(message, cause);
        }
    }


    /**
     * 
     * cvs revision number, kept up to date by cvs keyword
     * substitution. The revision number is used to tag PTLS parameter
     * files. 
     */
    private static final String version = "$Revision: 1.22 $";


    /**
     * 
     * Write the PTLS parameters in this instance to file {@code
     * filename}. No compatibility with future and past versions. The
     * file format is simply ASCII with the numbers as decimal
     * strings. 
     * 
     * @param filename file to write to
     * @throws PTLS_io_exception if an IO error occurs
     */
    public void write_to_file(String filename) 
        throws PTLS_io_exception
    {
        PrintWriter fout;

        try {
            fout = new PrintWriter(filename);
        }
        catch(FileNotFoundException e) {
            throw new PTLS_io_exception("invalid file name", e);
        }

        fout.format("OV-chip 2.0 PTLS system parameters version : %s\n",
                    version.substring(11, version.length() -2));
        fout.format("attribute number : %d\n", attribute_number);
        fout.format("base length : %d\n", base_length);
        fout.format("exponent length : %d\n", exponent_length);

        fout.format("n : %s\n", n);

        fout.format("v : %s\n", v);
        fout.format("1/v : %s\n", inv_v);

        fout.format("ptls private key : %s\n", ptls_private_key);

        for(int i = 0; i < base.length; i++)
            fout.format("base %d : %s\n", i, base[i]);
        fout.close();
    }


    /**
     * 
     * Retrurn the part after the colon from the next line in the
     * input {@code fin}.
     * 
     * @param fin input reader
     * @return the part after the colon of the next line, which should
     * be a number in decimal base
     * @throws PTLS_io_exception for read errors
     */
    private static String get_next_data(BufferedReader fin) 
        throws PTLS_io_exception
    {
        String line;
        try {
            line = fin.readLine();
        }
        catch(IOException e) {
            throw new PTLS_io_exception("no next line", e);
        }

        return(line.substring(line.indexOf(':') + 2));
    }


    /**
     * 
     * Create a new PTLS parameter instance by loading file {@code
     * filename}.
     * 
     * @param filename file to read
     * @param applet_type current applet type for determining the
     * number of Montgomery digits and the (de-)montgomerization
     * factors
     * @param out debug/progress channel, may be null to supress output
     * @param verbosity verbosity level
     * @return parameter instance with parameters read from {@code
     * filename} 
     * @throws PTLS_io_exception if the file is not accessable, its
     * version tag does not match {@link #version}, or some other read
     * error occurs
     */
    public static PTLS_rsa_parameters read_from_file(String filename,
                                                     Applet_type applet_type,
                                                     PrintWriter out, 
                                                     int verbosity)
        throws PTLS_io_exception
    {   
        BufferedReader fin;
        try {
            fin = new BufferedReader(new FileReader(filename));
        }
        catch(FileNotFoundException e) {
            throw new PTLS_io_exception("file not found", e);
        }

        String file_version = get_next_data(fin);
        if(!version.equals(String.format("$" + "Revision: %s $", 
                                         file_version))) 
            {
                if(out != null) {
                    out.format("file version %s differs from " +
                               "programm version %s\n",
                               file_version,
                               version);
                }

                throw new PTLS_io_exception("version mismatch");
            }

        int attribute_number = Integer.parseInt(get_next_data(fin));

        PTLS_rsa_parameters params = new PTLS_rsa_parameters(attribute_number,
                                                             out,
                                                             verbosity);

        params.base_length = Integer.parseInt(get_next_data(fin));
        params.exponent_length = Integer.parseInt(get_next_data(fin));

        params.n = new BigInteger(get_next_data(fin));

        params.hmod = new Host_modulus
            (params.make_base_bytes(applet_type.montgomery_digits()),
             params.n);

        params.v = new BigInteger(get_next_data(fin));
        params.inv_v = new BigInteger(get_next_data(fin));

        params.ptls_private_key = new BigInteger(get_next_data(fin));
        params.ptls_public_key = 
            params.ptls_private_key.modPow(params.v, params.n);

        params.base = new BigInteger[attribute_number];
        for(int i = 0; i < attribute_number; i++)
            params.base[i] = new BigInteger(get_next_data(fin));

        try {
            fin.close();
        }
        catch(IOException e) {}

        return params;
    }


    #if defined(TESTFRAME) || defined(JAVADOC)

        /**
         * 
         * Print all parameters in this instance to the given channel. 
         * <P>
         *
         * Only available if <a
         * href="../../../overview-summary.html#TESTFRAME">TESTFRAME<a>
         * is defined.
         * 
         * @param applet_type current applet type for determining the
         * number of Montgomery digits and the (de-)montgomerization
         * factors
         * @param out channel to print to
         */
        void print_all(Applet_type applet_type, PrintWriter out) {
            out.format("base length = %d\n" +
                       "base bytes  = %d\n" +
                       "exp length  = %d\n" +
                       "exp bytes   = %d\n" +
                       "n           = %s\n" +
                       "            = %s\n" +
                       "v           = %s\n" +
                       "            = %s\n" +
                       "1/v         = %s\n" +
                       "            = %s\n" +
                       "x           = %s\n" +
                       "            = %s\n" +
                       "h           = %s\n" +
                       "            = %s\n",
                       base_length,
                       make_base_bytes(applet_type.montgomery_digits()),
                       exponent_length,
                       make_exponent_bytes(),
                       n,
                       BigIntUtil.to_byte_hex_string(n),
                       v,
                       BigIntUtil.to_byte_hex_string(v),
                       inv_v,
                       BigIntUtil.to_byte_hex_string(inv_v),
                       ptls_private_key,
                       BigIntUtil.to_byte_hex_string(ptls_private_key),
                       ptls_public_key,
                       BigIntUtil.to_byte_hex_string(ptls_public_key));
            BigIntUtil.print_array(out, "%d bases\n", "g", base);
        }
    #endif
}
