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
// Created 26.8.08 by Hendrik
// 
// some utility functionality
// 
// $Id: Misc.java,v 1.14 2009-06-17 21:16:39 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif

#if defined(JAVACARD_APPLET) || defined(JAVADOC)
    import javacard.framework.ISO7816;
    import javacard.framework.ISOException;
    import javacard.framework.JCSystem;
    import javacard.security.RandomData;
    import javacard.security.MessageDigest;
#else
    import java.util.Random;
#endif


/** 
 * Collection of miscellaneous methods that do not fit anywhere else,
 * most of them abstracting from differences between standard Java and
 * Java Card.
 * <P>
 *
 * Static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.14 $
 * @commitdate $Date: 2009-06-17 21:16:39 $ by $Author: tews $
 * @environment host, card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>,
 *   <a href="../../../overview-summary.html#MESSAGE_DIGEST">MESSAGE_DIGEST<a>,
 *   <a href="../../../overview-summary.html#RANDOM">RANDOM<a>
 */
PUBLIC class Misc {

    /**
     * 
     * Static class, object creation disabled.
     */
    protected Misc() {}


    /**
     * 
     * Implementation of the <a
     * href="../../../overview-summary.html#ASSERT">ASSERT<a> and <a
     * href="../../../overview-summary.html#ASSERT_TAG">ASSERT_TAG<a>
     * macros. Aborts the computation if {@code condition} is false.
     * On the card (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is defined) this is done by throwing an {@link ISOException}
     * with status {@link Response_status#OV_ASSERTION_00}, where
     * {@code tag} is or-ed into the lower 8 bits of the response
     * status. On the host (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is undefined) it does simply {@code assert false}.
     * <P>
     *
     * A list with all currently used assert tag is contained in the
     * <a href="../../../overview-summary.html#assert_tags">overview page.<a>
     * 
     * @param condition abort computation if condition is false
     * @param tag 1-byte tag providing additional information about
     * the assertion
     * @throws ISOException with {@link
     * Response_status#OV_ASSERTION_00} on the card, if the
     * {@code condition} is false
     * @throws java.lang.AssertionError on the host, if the {@code
     * condition} is false
     */
    public static void myassert(boolean condition, short tag) {
        if(! condition) {
            #ifdef JAVACARD_APPLET
                ISOException.throwIt((short)(
                    Response_status.OV_ASSERTION_00 | (tag & 0xff)));
            #else
                System.err.format("Assertion %02X failed\n", tag);
                assert(false);
            #endif
        }
    }


    /**
     * 
     * Portable array copy. Abstracts from the different names of the
     * array copy routines on standard Java and Java Card. Does a
     * non-atomic copy on the card.
     * <P>
     *
     * Behaves either like {@link
     * javacard.framework.Util#arrayCopyNonAtomic arrayCopyNonAtomic}
     * or {@link System#arraycopy System.arraycopy} depending on
     * whether <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is defined.
     * 
     * @param src the source array
     * @param src_off starting position in the source array
     * @param dest the destination array
     * @param dest_off starting position in the destination array
     * @param len the number of elements to be copied
     */
    public static final void
        array_copy(byte[] src, short src_off, byte[] dest, short dest_off,
                   short len) {
        #ifdef JAVACARD_APPLET
            javacard.framework.Util.arrayCopyNonAtomic(src, src_off, 
                                                       dest, dest_off, len);
        #else
            System.arraycopy(src, src_off, dest, dest_off, len);
        #endif

        return;
    }


    /**
     * 
     * Portable RAM allocation. Allocates a byte array in transient
     * memory (RAM) on the card (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is defined). Does a normal allocation on the host (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is undefined).
     * 
     * @param size length of the byte array
     * @return a freshly allocated byte array
     * @see JCSystem#makeTransientByteArray
     * JCSystem.makeTransientByteArray
     */
    public static byte [] allocate_transient_byte_array(short size) {
        #ifdef JAVACARD_APPLET
            return JCSystem.makeTransientByteArray(size,
                                                   JCSystem.CLEAR_ON_DESELECT);
        #else
            return new byte[size];
        #endif
    }


    /**
     * 
     * Total size of an {@link APDU_Serializable} array. 
     * 
     * @param a the array, might be a null reference
     * @return the sum of the sizes of all elements in {@code a}
     * @throws NullPointerException if one of the elements in {@code
     * a} is null
     */
    public static short length_of_serializable_array(APDU_Serializable[] a) {
        if(a == null)
            return 0;

        short length = 0;
        for(short i = 0; i < a.length; i++) {
            length += a[i].size();
        }
        return length;
    }
        

    /**
     * 
     * Portable way to get an instance of a random number generator.
     * Does {@link RandomData#getInstance
     * RandomData.getInstance}{@link RandomData#ALG_SECURE_RANDOM
     * (RandomData.ALG_SECURE_RANDOM)} on the card (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is defined) and {@code new }{@link java.util.Random} on the host
     * (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is undefined). <P>
     *
     * The return type is <a
     * href="../../../overview-summary.html#RANDOM">RANDOM<a>, which
     * expands to {@link RandomData} on the card and {@link
     * java.util.Random} on the host.
     *
     * @return an instance of a random number generator
     */
    public static RANDOM get_new_rand() {
        #ifdef JAVACARD_APPLET
        // XXX need to seed ALG_SECURE_RANDOM? I always get the same instance??
        // XXX need to catch CryptoException.NO_SUCH_ALGORITHM ?
             return RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
        #else
             return new Random();
        #endif
    }


    /**
     * 
     * Portable way to fill a byte array with random data. Does {@link
     * RandomData#generateData generateData} on the card (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is defined) and {@link java.util.Random#nextBytes nextBytes} on
     * the host (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is undefined).
     * 
     * @param rand the random number instance of type <a
     * href="../../../overview-summary.html#RANDOM">RANDOM<a>
     * @param ba the byte array to fill with random data
     * @param start starting index in {@code ba}
     * @param len number of random bytes to store in {@code ba}
     * starting at {@code start}
     */
    public static void rand_data(RANDOM rand, byte[] ba, 
                                 short start, short len) {
        #ifdef JAVACARD_APPLET
            rand.generateData(ba, start, len);
        #else
            byte[] bx = new byte[len];
            rand.nextBytes(bx);
            array_copy(bx, (short)0, ba, start, len);
        #endif
    }


    #ifndef JAVACARD_APPLET
       /**
        * 
        * Fill an integer array with random data. Same as {@link
        * #rand_data rand_data} but for an integer array.
        * <P>
        *
        * Only available if <a
        * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
        * is undefined (there are no ints on Java Card anyway).
        * 
        * @param rand the random number instance of type <a
        * href="../../../overview-summary.html#RANDOM">RANDOM<a>
        * @param ba the int array to fill with random data
        * @param start starting index in {@code ba}
        * @param len number of random ints to store in {@code ba}
        * starting at {@code start}
        */
       public static void rand_data_int(RANDOM rand, int[] ba, 
                                        short start, int len) {
           for(int i = 0; i < len; i++)
               ba[i + start] = rand.nextInt();
           return;
       }
    #endif


    #ifdef MESSAGE_DIGEST
        /**
         * 
         * Portable way of creating a portable message digest instance
         * computing SHA 160 bit digests. Does {@link
         * MessageDigest#getInstance MessageDigest.getInstance}{@link
         * MessageDigest#ALG_SHA (MessageDigest.ALG_SHA)} on the card
         * (if <a
         * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
         * is defined) and {@code new }{@link Message_digest_wrapper}
         * on the host (if <a
         * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
         * is undefined).
         * <P>
         *
         * Only available if <a
         * href="../../../overview-summary.html#MESSAGE_DIGEST">MESSAGE_DIGEST<a>
         * is defined to avoid needing {@link Message_digest_wrapper}
         * in all host side programs.
         * 
         * @return a message digest instance of type <a
         * href="../../../overview-summary.html#MESSAGE_DIGEST">MESSAGE_DIGEST<a>,
         * which expands to {@link MessageDigest} on the card and
         * {@link Message_digest_wrapper} on the host.
         */
        public static MESSAGE_DIGEST get_message_digest() {
            #ifdef JAVACARD_APPLET
               return MessageDigest.getInstance(MessageDigest.ALG_SHA, false);
            #else
               return new Message_digest_wrapper();
            #endif
        }
    #endif


    /**
     * 
     * Portable transaction start call. Does {@link
     * JCSystem#beginTransaction} on the card (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is defined) and nothing on the host (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is undefined). <STRONG>So on the host no transaction is
     * started.</STRONG> Needed for the <a
     * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>. 
     * 
     */
    public static void begin_transaction() {
        #ifdef JAVACARD_APPLET
            JCSystem.beginTransaction();
        #endif
    }


    /**
     * 
     * Portable transaction end call. Does {@link
     * JCSystem#commitTransaction} on the card (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is defined) and nothing on the host (if <a
     * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
     * is undefined). <STRONG>So on the host no transaction is
     * done.</STRONG> Needed for the <a
     * href="../../../overview-summary.html#APPLET_TESTFRAME">APPLET_TESTFRAME<a>. 
     * 
     */
    public static void commit_transaction() {
        #ifdef JAVACARD_APPLET
            JCSystem.commitTransaction();
        #endif
    }
}
