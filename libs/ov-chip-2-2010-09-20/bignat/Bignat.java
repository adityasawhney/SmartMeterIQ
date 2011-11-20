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
// Created 30.7.08 by Hendrik
// 
// javacard big integer trials
// 
// $Id: Bignat.java,v 1.52 2010-02-18 12:40:37 tews Exp $
// 
// next free assert tag 0x31

#include <config>

#include "bignatconfig"


#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.bignat;
#endif

#ifdef JAVACARD_APPLET
   import javacard.security.RandomData;
#else
   import ds.ov2.util.Misc;
   import ds.ov2.util.APDU_Serializable;
   import java.util.Random;
#endif


/** 
 * Allocation free big natural numbers for Java Card.
 * A number of general topics are discussed <a
 * href="package-summary.html#package_description">in the package
 * description.</a>
 *
 * <H3>Warning</H3>
 *
 * <strong>Depending on the configuration some types might be different
 *   from what is shown here. Especially some method parameters of 
 *   type short might have type long, because they are declared as 
 *   DOUBLE_DIGIT_TYPE.
 * </strong>
 *   
 *
 * <a name="data_representation"></a>
 * <H3>Data Representation</H3>
 *
 * Big naturals are stored in an array of digits of type
 * DIGIT_TYPE, which expands to byte or int. The most significant
 * digit is stored at index 0 and the least significant one at
 * index {@link #size} -1. The internal representation is
 * therefore almost compatible with {@link java.math.BigInteger}.
 * The length of the digit array is determined by the size
 * argument of the constructors. It stays constant for every
 * object. Different big naturals can have different sizes.
 * Methods that combine different big naturals check the size
 * constraint in an assertion. Some methods permit to combine big
 * naturals of different sizes. Size constraints are lifted on a
 * by-need basis, so some methods may still be overly
 * restrictive.
 * <P>
 *
 * It is the responsibility of the user to make sure that no
 * overflow occurs. Some methods assert a precondition that implies 
 * that no overflow occurs. In such a case the user has still to make
 * sure that the assertion holds. There is no error recovery possible,
 * because, when assertions are disabled, overflowing digits are just
 * droped (I believe).
 * <P>
 *
 *
 * <H3>Converting to/from {@link java.math.BigInteger}</H3>
 *
 * When configured such that DIGIT_TYPE is byte, the internal
 * representations of a Bignat and a BigInteger are almost
 * compatible except for the following three points:
 * <UL>
 * <LI>BigIntegers can be negative.</LI>
 * <LI>The byte array returned by {@link
 * java.math.BigInteger#toByteArray} is variable in size.</LI>
 * <LI>With a leading one bit {@link
 * java.math.BigInteger#BigInteger(byte[])} will interpred the
 * byte array as negative value.
 * </LI></UL>
 *
 * When converting from BigInteger to Bignat leading digets must
 * be filled with zeros.
 * <P>
 *
 * When converting from Bignat to BigInteger make sure the first
 * bit is zero, possibly by prepending a zero byte.
 * <P>
 *
 * When DIGIT_TYPE is int one has to convert every int into 4 bytes 
 * in the obvious way.
 * <P>
 *
 * Convertion can be done with {@link
 * ds.ov2.util.Convert_serializable} which works regardless of the 
 * setting of DIGIT_TYPE. 
 * Use {@code
 * Convert_serializable.to(bignat, bigint)} and {@code
 * Convert_serializable.from(bignat, bigint)}. Note that {@code
 * Convert_serializable.to(bigint, bignat)} will not work, see
 * {@link #is_compatible_with Bignat.is_compatible_with} and
 * {@link APDU_BigInteger#is_compatible_with
 * APDU_BigInteger.is_compatible_with}.
 * 
 *
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#ASSERT_TAG">ASSERT_TAG(condition, tag)</a>,
 *   <a href="../../../overview-summary.html#BIGNAT_USE_BYTE">BIGNAT_USE_BYTE</a>,
 *   <a href="../../../overview-summary.html#BIGNAT_USE_INT">BIGNAT_USE_INT</a>,
 *   <a href="../../../overview-summary.html#DIGIT_TYPE">DIGIT_TYPE</a>,
 *   <a href="../../../overview-summary.html#DOUBLE_DIGIT_TYPE">DOUBLE_DIGIT_TYPE</a>,
 *   <a href="../../../overview-summary.html#RANDOM">RANDOM</a>,
 *   <a href="../../../overview-summary.html#HOST_TESTFRAME">HOST_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#BIGNAT_TESTFRAME">BIGNAT_TESTFRAME</a>,
 *   <a href="../../../overview-summary.html#VARIABLE_SIZE_BIGNATS">VARIABLE_SIZE_BIGNATS</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET</a>,
 *   <a href="../../../overview-summary.html#NO_CARD_ASSERT">NO_CARD_ASSERT</a>,
 *   <a href="../../../overview-summary.html#NO_ASSERT">NO_ASSERT</a>,
 *   <a href="../../../overview-summary.html#SHIFT_LESSER">SHIFT_LESSER</a>,
 *   <a href="../../../overview-summary.html#OPT_DOUBLE_ADD">OPT_DOUBLE_ADD</a>,
 *   <a href="../../../overview-summary.html#OPT_SKIP_DEVIDE">OPT_SKIP_DEVIDE</a>,
 *   <a href="../../../overview-summary.html#OPT_SPECIAL_SQUARE">OPT_SPECIAL_SQUARE</a>,
 *   <a href="../../../overview-summary.html#MONTGOMERY_MULT_SHORTCUT">MONTGOMERY_MULT_SHORTCUT</a>,
 *   <a href="../../../overview-summary.html#JAVADOC">JAVADOC</a>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.52 $
 * @commitdate $Date: 2010-02-18 12:40:37 $ by $Author: tews $
 * @environment host, card
 * @todo measure the optimizations on the card
 */
PUBLIC class Bignat implements APDU_Serializable {

    // For java card Bignat operates on bytes. For performance tests,
    // and maybe for the phone, it operates on ints. This is controlled 
    // by the preprocessor directive BIGNAT_USE_INT. We define here a few
    // constants depending on BIGNAT_USE_INT / BIGNAT_USE_BYTE. 
    // These constant help to 
    // reduce the preprocessor directives below.

    #ifdef BIGNAT_USE_BYTE
      /**
       * True for the byte/short configuration, false otherwise.
       */
      public static final boolean use_byte_digits = true;

      /**
       * Factor for converting digit size into byte length.
       * 1 for the byte/short converting, 4 for the int/long configuration.
       *
       */      
      public static final short size_multiplier = 1;

      /**
       * Bitmask for extracting a digit out of a longer int/short value.
       * short 0xff for the byte/short configuration,  
       * long 0xffffffffL the int/long configuration. 
       */      
      public static final short digit_mask = 0xff;

      /**
       * Bitmask for the highest bit in a digit. short 0x80 for the
       * byte/short configuration, long 0x80000000 for the int/long
       * configuration. 
       * 
       */
      public static final short digit_first_bit_mask = 0x80;

      /**
       * Bitmask for the second highest bit in a digit. short 0x40 for
       * the byte/short configuration, long 0x40000000 for the
       * int/long configuration.
       * 
       */
      public static final short digit_second_bit_mask = 0x40;

      /**
       * Bitmask for the two highest bits in a digit. short 0xC0 for
       * the byte/short configuration, long 0xC0000000 for the
       * int/long configuration.
       * 
       */
      public static final short digit_first_two_bit_mask = 0xC0;

      /**
       * Size in bits of one digit.
       * 8 for the byte/short configuration, 32 for 
       * the int/long configuration. 
       */      
      public static final short digit_len = 8;

      /**
       * Size in bits of a double digit.
       * 16 for the byte/short configuration, 64 for 
       * the int/long configuration. 
       */      
      private static final short double_digit_len = 16;

      /**
       * Bitmask for erasing the sign bit in a double digit.
       * short 0x7fff for the byte/short configuration, 
       * long 0x7fffffffffffffffL for the int/long configuration. 
       */      
      private static final short positive_double_digit_mask = 0x7fff;
    #else // BIGNAT_USE_INT
      public  static final boolean use_byte_digits = false;
      public  static final short size_multiplier = 4;
      public  static final long digit_mask = 0xffffffffL;
      public  static final long digit_first_bit_mask = 0x80000000L;
      public  static final long digit_second_bit_mask = 0x40000000L;
      public  static final long digit_first_two_bit_mask = 0xC0000000L;
      public  static final int digit_len = 32;
      private static final int double_digit_len = 64;
      private static final long positive_double_digit_mask = 
          0x7fffffffffffffffL;
    #endif


    /**
     * Bitmask for the highest bit in a double digit.
     */
    public static final DOUBLE_DIGIT_TYPE highest_digit_bit = 
        (DOUBLE_DIGIT_TYPE)(1L << (digit_len -1));

    /**
     * The base as a double digit.
     * The base is first value that does not fit into a single digit. 
     * 2^8 for the byte/short configuration and 
     * 2^32 for the int/long configuration.
     */
    public static final DOUBLE_DIGIT_TYPE bignat_base = 
        (DOUBLE_DIGIT_TYPE)(1L << digit_len);

    /**
     * Bitmask with just the highest bit in a double digit.
     */
    public static final DOUBLE_DIGIT_TYPE highest_double_digit_bit = 
        (DOUBLE_DIGIT_TYPE)(1L << (double_digit_len -1));

    #ifdef HOST_TESTFRAME

      /**
       * Format string to print a digit.
       */
       private final static String digit_format =
           digit_len == 8 ? "%02X" : "%08X";

      /**
       * Format string to print a double digit.
       */
       private final static String double_digit_format =
           digit_len == 8 ? "%04X" : "%016X";
    #endif


    /**
     * Digit array.
     * Elements have type DIGIT_TYPE.
     */
    private DIGIT_TYPE[] value;


    /**
     * Return the digit array.
     * The return value has type {@code DIGIT_TYPE[]}.
     * The {@link #value} field is simply returned, without making a copy. 
     * Modifying the returned array will modify this Bignat.
     *
     * @return a reference to {@link #value} of type {@code DIGIT_TYPE[]}
     */
    public DIGIT_TYPE[] get_digit_array() {
        return value;
    }


    /**
     * Return this Bignat as byte array. For the byte/short
     * configuration simply the digit array is returned. For other
     * configurations a new byte array is allocated and returned.
     * Modifying the returned byte array therefore might or might not
     * change this bignat.
     *
     * @return this bignat as byte array
     */
    public byte[] as_byte_array() {
        #ifdef BIGNAT_USE_BYTE
            return value;
        #else // BIGNAT_USE_INT
            #ifdef JAVACARD_APPLET
                #error "Unsuitable implementation, allocates at runtime."
            #endif

            byte buf[] = new byte[size * size_multiplier];
            short res = to_byte_array((short)buf.length, (short)0, 
                                      buf, (short)0);
            ASSERT(res == buf.length + 1);
            return buf;
        #endif
    }


    #ifndef VARIABLE_SIZE_BIGNATS
       private final short size;
    #else

      /**
       * Length in digits.
       * Final field if VARIABLE_SIZE_BIGNATS is not defined.
       * Adjusted by the {@link #resize} method.
       * Note that the {@link #size()} method returns something different,
       * which is admittedly confusing.
       */
       private short size;


       /**
        * Resize this Bignat.
        * Only available when VARIABLE_SIZE_BIGNATS is defined. Works only 
        * for the byte/short configuration, otherwise an assertion is thrown.
        * Asserts that the new size {@code s} is smaller or equal to the 
        * size specified in the constructor.
        *
        * @param new_size new size in bytes, must be
        * divisible by 4 for the int/long configuration
        */
       void resize(short new_size) {
           ASSERT_TAG((new_size % size_multiplier == 0) &&
                      ((short)(new_size / size_multiplier) <= value.length), 
                      0x20);
           size = (short)(new_size / size_multiplier);
       }
    #endif
        


    #ifdef HOST_TESTFRAME
      /**
       * Controls the amount of debug messages printed.
       * Only available if HOST_TESTFRAME is defined.
       * Initialized to zero, must be set from the outside for different 
       * values. A value of zero disables all debug messages.
       */
      public static int verbosity = 0;
    #endif


    /**
     * Size in bytes necessary to send or receive this object 
     * via the OV-chip protocol layer, see 
     * {@link ds.ov2.util.APDU_Serializable#size APDU_Serializable.size()}.
     * <P>
     *
     * For configurations different from 
     * byte/short the returned value obviously differs from the length of
     * the {@link #value} array and also from the {@link #size} attribute.
     * <P>
     * The return value is adjusted by {@link #resize}.
     *
     * @return size in bytes
     */
    public short size() {
        return (short)(size * size_multiplier);
    }


    /**
     * Return the size in digits. 
     * Provides access to the internal {@link #size} field.
     * <P>
     * The return value is adjusted by {@link #resize}.
     *
     * @return size in digits.
     */
    public short length() {
        return size;
    }


    /**
     * Construct a Bignat of size {@code size} in bytes.
     * Allocated in RAM if {@code ram} is true, in EEPROM otherwise. 
     * In the int/long configuration asserts that size is divisable by 4. 
     * In this configuration the number of digits is obviously 
     * {@code size / 4}.
     * Relies on {@link ds.ov2.util.Misc#allocate_transient_byte_array 
     * Misc.allocate_transient_byte_array} for allocation in transient 
     * (RAM) memory.
     *
     * @param size   the size of the new Bignat in bytes, must be
     * divisible by 4 for the int/long configuration
     * @param ram    allocate in transient RAM if true
     */
    public Bignat(short size, boolean ram) {
        #ifdef BIGNAT_USE_BYTE
            this.size = size;
            if(ram)
                value = Misc.allocate_transient_byte_array(size);
            else
                value = new byte[size];
            return;
        #else // BIGNAT_USE_INT
            assert(size % 4 == 0);
            this.size = (short)(size / 4);
            value = new int[this.size];
            return;
        #endif
    }


    #ifndef JAVACARD_APPLET

        /**
         * Convenience constructor for the host.
         * Only available for the host.
         * Invokes {@code Bignat(size, false)} on the host, where the 
         * ram argument has no meaning.
         *
         * @param size the size of the new Bignat in bytes, must be
         * divisible by 4 for the int/long configuration
         */
        public Bignat(short size) {
            this(size, false);
        }
    #endif


    /**
     * Stores zero in this object.
     */
    public void zero() {
        for(short i = 0; i < size; i++)
            value[i] = 0;
    }

    /**
     * Stores one in this object.
     */
    public void one() {
        this.zero();
        value[(short)(size -1)] = 1;
    }


    /**
     * 
     * Stores two in this object.
     */
    public void two() {
        this.zero();
        value[(short)(size -1)] = 2;
    }



    /**
     * Copies {@code other} into this. No size requirements. If {@code
     * other} has more digits then the superfluous leading digits
     * of {@code other} are asserted to be zero. If this bignat has
     * more digits than its leading digits are correctly initilized to
     * zero.
     *
     * @param other Bignat to copy into this object.
     */
    public void copy(Bignat other) {
        short this_start, other_start, len;
        if(this.size >= other.size) {
            this_start = (short)(this.size - other.size);
            other_start = 0;
            len = other.size;
        }
        else {
            this_start = 0;
            other_start = (short)(other.size - this.size);
            len = this.size;
        }

        #ifndef NO_CARD_ASSERT
           for(short i = 0; i < other_start; i++)
               ASSERT_TAG(other.value[i] == 0, 0x21);
        #endif

        for(short i = 0; i < this_start; i++)
            this.value[i] = 0;

        #ifdef BIGNAT_USE_BYTE
          Misc.array_copy(other.value, other_start, 
                          this.value, this_start, len);
        #else
          System.arraycopy(other.value, other_start, 
                           this.value, this_start, len);
        #endif
    }


    /**
     * Equality check. Requires that this object and other have 
     * the same size. Returns true if all digits are equal.
     * 
     *
     * @param other Bignat to compare
     * @return true if this and other have the same value, false otherwise.
     */
    public boolean same_value(Bignat other) {
        ASSERT_TAG(this.size == other.size, 0x22);

        for(short i = 0; i < size; i++)
            if(this.value[i] != other.value[i])
                return false;
        return true;
    }


    /**
     * 
     * Subtraction. Subtract {@code other} from {@code this} and store
     * the result in {@code this}. If an overflow occurs the return
     * value is true and the value of this is the correct negative
     * result in two's complement. If there is no overflow the return
     * value is false.
     * <P>
     *
     * It would be more natural to report the overflow with an {@link
     * javacard.framework.UserException}, however its {@link
     * javacard.framework.UserException#throwIt throwIt} method dies
     * with a null pointer exception when it runs in a host test
     * frame...
     * <P>
     *
     * No size constraints, in particular, {@code other} can be longer
     * than {@code this}. However, if {@code other} is longer than
     * {@code this} the additional digits of {@code other} are
     * asserted to be zero. Without assertion checks these additional
     * digits of {@code other} are ignored and the method silently
     * returns a wrong result.
     * 
     * @param other value to subtract from this
     * @return true if an overflow occurs, false otherwise
     */
    public boolean subtract(Bignat other) {
        short i, j;

        #ifndef NO_CARD_ASSERT
            for(i = 0; i < (short)(other.size - this.size); i++) {
                ASSERT_TAG(other.value[i] == 0, 0x23);
            }
        #endif

        DOUBLE_DIGIT_TYPE subtraction_result = 0;
        DOUBLE_DIGIT_TYPE carry = 0;

        i = (short)(this.size - 1);
        j = (short)(other.size - 1);
        for(; i >= 0 && j >= 0; i--, j--) {
            subtraction_result = (DOUBLE_DIGIT_TYPE)
                ((this.value[i] & digit_mask) -
                 (other.value[j] & digit_mask) - carry);
            this.value[i] = (DIGIT_TYPE)(subtraction_result & digit_mask);
            carry = (short)(subtraction_result < 0 ? 1 : 0);
        }
        for(; i >= 0 && carry > 0; i--) {
            if(this.value[i] != 0)
                carry = 0;
            this.value[i] -= 1;
        }            
        
        return carry > 0;
    }


    /**
     * 
     * Modular subtraction. Computes {@code (this - other) modulo mod}
     * and stores the result in this Bignat. This bignat and {@code
     * other} must be less then {@code mod}, otherwise strange things
     * can happen. 
     * 
     * @param other value to subtract
     * @param mod modulus
     */
    public void modular_subtraction(Bignat other, Modulus mod) {
        if(subtract(other)) {
            if(!add_carry(mod.m)) {
                // Addition must produce an overflow, otherwise
                // something is wrong.
                ASSERT_TAG(false, 0x24);
            }
        }
        return;
    }


    /**
     * Scaled subtraction. 
     * Subtracts {@code mult * 2^(}{@link #digit_len}{@code  * shift) * other}
     * from this.
     * <P>
     * That is, shifts {@code mult * other} precisely {@code shift} digits to
     * the left and subtracts that value from this.
     * {@code mult} must be less than {@link #bignat_base}, that is,
     * it must fit into one digit. It is only declared as 
     * DOUBLE_DIGIT_TYPE here to avoid negative values.
     * <P>
     * {@code mult} has type DOUBLE_DIGIT_TYPE.
     * <P>
     * No size constraint. However, an assertion is thrown,
     * if the result would be negative. {@code other} can have more 
     * digits than this object, but then sufficiently many leading digits 
     * must be zero to avoid the underflow.
     * <P>
     * Used in division.
     *
     * @param other    Bignat to subtract from this object
     * @param shift    number of digits to shift {@code other} to the left
     * @param mult     of type DOUBLE_DIGIT_TYPE, multiple of {@code other} 
     *                 to subtract from this object. Must be below
     *                 {@link #bignat_base}.
     */
    public void times_minus(Bignat other, short shift, DOUBLE_DIGIT_TYPE mult) {
        DOUBLE_DIGIT_TYPE akku = 0;
        DOUBLE_DIGIT_TYPE subtraction_result;
        short i = (short)(this.size - 1 - shift); 
        short j = (short)(other.size -1);
        for( ; i >= 0 && j >= 0; i--, j--) {
            akku = (DOUBLE_DIGIT_TYPE)
                (akku + (DOUBLE_DIGIT_TYPE)
                 (mult * (other.value[j] & digit_mask)));
            subtraction_result = (DOUBLE_DIGIT_TYPE)
                ((value[i] & digit_mask) - (akku & digit_mask));

            // if(debug) {
            //  System.out.format("TM %d: this %02X other %02X mult %04X " +
            //                    "akku %04X sub res %04X\n",
            //                    i,
            //                    value[i] & 0xff,
            //                    other.value[i] & 0xff,
            //                    mult,
            //                    akku & 0xffff,
            //                    subtraction_result & 0xffff);
            // }

            value[i] = (DIGIT_TYPE)(subtraction_result & digit_mask);
            akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);
            if(subtraction_result < 0)
                akku++;
        }

        // deal with carry as long as there are digits left in this
        while(i >= 0 && akku != 0) {
            subtraction_result = (DOUBLE_DIGIT_TYPE)
                ((value[i] & digit_mask) - (akku & digit_mask));
            value[i] = (DIGIT_TYPE)(subtraction_result & digit_mask);
            akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);
            if(subtraction_result < 0)
                akku++;
            i--;
        }
        ASSERT(akku == 0);      // otherwise we have an underflow

        // check if superfluous digits in other are zero
        #ifndef NO_CARD_ASSERT
           while(j >= 0) {
               ASSERT(other.value[j] == 0);
               j--;
           }
        #endif
        
        return;
    }


    /**
     * Index of the most significant 1 bit. 
     * <P>
     * {@code x} has type DOUBLE_DIGIT_TYPE.
     * <P>
     * Utility method, used in division.
     *
     * @param x of type DOUBLE_DIGIT_TYPE
     * @return index of the most significant 1 bit in {@code x}, returns
     *         {@link #double_digit_len} for {@code x == 0}.
     */
    private static short highest_bit(DOUBLE_DIGIT_TYPE x) {
        for(short i = 0; i < double_digit_len; i++) {
            if(x < 0) return i;
            x <<= 1;
        }
        return double_digit_len;
    }


    /**
     * Shift to the left and fill.
     * Takes {@code high} {@code middle} {@code low} as 4 digits, 
     * shifts them {@code shift} bits to the left and returns the most 
     * significant {@link #double_digit_len} bits. 
     * <P>
     * Utility method, used in division.
     * 
     *
     * @param high    of type DOUBLE_DIGIT_TYPE, 
     *                most significant {@link #double_digit_len} bits
     * @param middle  of type DIGIT_TYPE, middle {@link #digit_len} bits
     * @param low     of type DIGIT_TYPE, 
     *                 least significant {@link #digit_len} bits
     * @param shift   amount of left shift
     * @return most significant {@link #double_digit_len} as DOUBLE_DIGIT_TYPE
     */
    private static DOUBLE_DIGIT_TYPE shift_bits(DOUBLE_DIGIT_TYPE high, 
                                                DIGIT_TYPE middle, 
                                                DIGIT_TYPE low, 
                                                short shift) 
    {
        #ifdef BIGNAT_TESTFRAME
           if(verbosity > 19) {
               System.out.format("SB shift %d high " +
                                 double_digit_format + "." +
                                 digit_format + "." + digit_format + "\n", 
                                 shift, high, middle, low);
           }
        #endif

        // shift high
        high <<= shift;

        #ifdef BIGNAT_TESTFRAME
           if(verbosity > 19)
               System.out.format("SB high " + double_digit_format + "\n", high);
        #endif

        // merge middle bits
        DIGIT_TYPE mask = (DIGIT_TYPE)
            (digit_mask << (shift >= digit_len ? 0 : digit_len - shift));
        DOUBLE_DIGIT_TYPE bits = (DOUBLE_DIGIT_TYPE)
            ((DOUBLE_DIGIT_TYPE)(middle & mask) & digit_mask);
        if(shift > digit_len)
            bits <<= shift - digit_len;
        else
            bits >>>= digit_len - shift;
        high |= bits;

        #ifdef BIGNAT_TESTFRAME
           if(verbosity > 19)
               System.out.format("SB mask " + digit_format + " bits " + 
                                 digit_format, 
                                 mask, bits);
        #endif

        if(shift <= digit_len) {
            #ifdef BIGNAT_TESTFRAME
               if(verbosity > 19) {
                   System.out.format(" = " + double_digit_format + "\n", high);
               }
            #endif
            return high;
        }

        // merge low bits
        mask = (DIGIT_TYPE)(digit_mask << double_digit_len - shift);
        bits = (DOUBLE_DIGIT_TYPE)
            ((((DOUBLE_DIGIT_TYPE)(low & mask) & digit_mask) 
              >> double_digit_len - shift));
        high |= bits;

        #ifdef BIGNAT_TESTFRAME
           if(verbosity > 19) {
               System.out.format(" = " + double_digit_format + "\n", high);
           }
        #endif

        return high;
    }


    /**
     * Scaled comparison. Compares this number with {@code other * 2^(}
     * {@link #digit_len} {@code * shift)}. That is, shifts {@code other} 
     * {@code shift} digits to the left and compares then. This bignat
     * and {@code other} will not be modified inside this method.
     * <P>
     *
     * As optimization {@code start} can be greater than zero to skip 
     * the first {@code start} digits in the comparison. These first 
     * digits must be zero then, otherwise an assertion is thrown. 
     * (So the optimization takes only effect when <a
     * href="../../../overview-summary.html#NO_CARD_ASSERT">NO_CARD_ASSERT</a>
     * is defined.)
     *
     * @param other Bignat to compare to
     * @param shift left shift of other before the comparison 
     * @param start digits to skip at the beginning
     * @return true if this number is strictly less than the shifted 
     *          {@code other}, false otherwise.
     */
    public boolean shift_lesser(Bignat other, short shift, short start) {
        short j;
        #ifndef NO_CARD_ASSERT
           j = (short)(other.size + shift - this.size);
           // If j is positive there are j digits of other hanging on 
           // the left of the first digit of this.
           for(short i = 0; i < j; i++) {
               ASSERT_TAG(other.value[i] == 0, 0x25);
           }
           // If j is negative, then this is longer than other and
           // -j digits are missing at the start of other.
           for(short i = 0; i < start; i++, j++) {
               ASSERT_TAG(this.value[i] == 0, 0x26);
               if(j >= 0) {
                   ASSERT_TAG(other.value[j] == 0, 0x27);
               }
           }
        #endif

        j = (short)(other.size + shift - this.size + start);
           
        DOUBLE_DIGIT_TYPE this_byte, other_byte;
        for(short i = start; i < this.size; i++, j++) {
            this_byte = (DOUBLE_DIGIT_TYPE)(this.value[i] & digit_mask);
            if(j >= 0 && j < other.size)
                other_byte = (DOUBLE_DIGIT_TYPE)
                    (other.value[j] & digit_mask);
            else 
                other_byte = 0;
            if(this_byte < other_byte) return true;
            if(this_byte > other_byte) return false;
        }
        return false;
    }


    /**
     * Comparison.
     *
     * @param other Bignat to compare with
     * @return true if this number is strictly lesser than {@code other},
     *         false otherwise.
     */
    // Return true, if this < other, false otherwise.
    public boolean lesser(Bignat other) {
        return this.shift_lesser(other, (short)0, (short)0);
    }


    /**
     * Test equality with zero.
     * 
     * @return true if this bignat equals zero.
     */
    public boolean is_zero() {
        for(short i = 0; i < size; i++) {
            if(value[i] != 0)
                return false;
        }
        return true;
    }


    #ifndef JAVACARD_APPLET

        /**
         * Comparison with debug output. Same as {@link #shift_lesser}
         * but prints debug output if {@link #verbosity} is big enough.
         * <P>
         * Only available if JAVACARD_APPLET is undefined.
         *
         * @param other Bignat to compare to
         * @param shift left shift of other before the comparison 
         * @param start digits to skip at the beginning
         * @return true if this number is strictly less than the shifted 
         *          {@code other}, false otherwise.
         */
        public boolean shift_lesser_debug(Bignat other, short shift,
                                          short start) 
        {
            boolean ret = shift_lesser(other, shift, start);
            if(verbosity > 19) {
                System.out.format("TL %s < (%s << %d) start %d ==> %s\n",
                                  this.to_hex_string(),
                                  other.to_hex_string(),
                                  shift,
                                  start,
                                  ret);
            }
            return ret;
        }

        #define SHIFT_LESSER shift_lesser_debug

    #else

        #define SHIFT_LESSER shift_lesser
    #endif


    /**
     * Remainder and Quotient. Divide this number by {@code divisor} 
     * and store the remainder in this. If {@code quotient} is non-null
     * store the quotient there.
     * <P>
     * There are no direct size constraints, but if {@code quotient} 
     * is non-null, it must be big enough for the quotient, otherwise 
     * an assertion is thrown.
     * <P>
     * Uses schoolbook division inside and has O^2 complexity 
     * in the difference of significant digits of the divident (in this
     * number) and the divisor. For numbers of equal size complexity is 
     * linear.
     *
     * @param divisor must be non-zero
     * @param quotient gets the quotient if non-null
     */
    public void remainder_divide(Bignat divisor, Bignat quotient) {
        // There are some size requirements, namely that quotient must
        // be big enough. However, this depends on the value of the 
        // divisor and is therefore not stated here.

        // zero-initialize the quotient, because we are only adding to it below
        if(quotient != null)
            quotient.zero();

        // divisor_index is the first nonzero digit (byte) in the divisor
        short divisor_index = 0;
        while(divisor.value[divisor_index] == 0)
            divisor_index++;

        // The size of this might be different from divisor. Therefore,
        // for the first subtraction round we have to shift the divisor
        // divisor_shift = this.size - divisor.size + divisor_index 
        // digits to the left. If this amount is negative, then
        // this is already smaller then divisor and we are done.
        // Below we do divisor_shift + 1 subtraction rounds. As an
        // additional loop index we also count the rounds (from 
        // zero upwards) in division_round. This gives access to the 
        // first remaining divident digits.

        short divisor_shift = (short)(this.size - divisor.size + divisor_index);
        short division_round = 0;

        // We could express now a size constraint, namely that
        //   divisor_shift + 1 <= quotient.size
        // However, in the proof protocol we divide x / v, where
        // x has 2*n digits when v has n digits. There the above size 
        // constraint is violated, the division is however valid, because
        // it will always hold that x < v * (v - 1) and therefore the 
        // quotient will always fit into n digits.

        // System.out.format("XX this size %d div ind %d div shift %d " +
        //                "quo size %d\n" +
        //                "%s / %s\n",
        //                this.size, 
        //                divisor_index,
        //                divisor_shift, 
        //                quotient != null ? quotient.size : -1,
        //                this.to_hex_string(),
        //                divisor.to_hex_string());

        // The first digits of the divisor are needed in every 
        // subtraction round.
        DOUBLE_DIGIT_TYPE first_divisor_digit = (DOUBLE_DIGIT_TYPE)
                    (divisor.value[divisor_index] & digit_mask);
        short divisor_bit_shift = (short)
            (highest_bit((DOUBLE_DIGIT_TYPE)(first_divisor_digit + 1)) -1);
        DIGIT_TYPE second_divisor_digit =
            divisor_index < (short)(divisor.size -1) ?
            divisor.value[(short)(divisor_index +1)] : 0;
        DIGIT_TYPE third_divisor_digit =
            divisor_index < (short)(divisor.size -2) ?
            divisor.value[(short)(divisor_index +2)] : 0;


        // The following variables are used inside the loop only.
        // Declared here as optimization.
        // divident_digits and divisor_digit hold the first one or two
        // digits. Needed to compute the multiple of the divisor to 
        // subtract from this.
        DOUBLE_DIGIT_TYPE divident_digits, divisor_digit;

        // To increase precisision the first digits are shifted to the 
        // left or right a bit. The following variables compute the shift.
        short divident_bit_shift, bit_shift;

        // Declaration of the multiple, with which the divident is 
        // multiplied in each round and the quotient_digit. Both are 
        // a single digit, but declared as a double digit to avoid the
        // trouble with negative numbers. If quotient != null multiple is
        // added to the quotient. This addition is done with quotient_digit.
        DOUBLE_DIGIT_TYPE multiple, quotient_digit;
        while(divisor_shift >= 0) {

            #ifdef BIGNAT_TESTFRAME
                if(verbosity > 19) {
                    System.out.format(
                        "RD round %d divisor_shift %d divisor_index %d\n",
                        division_round, divisor_shift, divisor_index);
                }
            #endif

            #ifdef BIGNAT_TESTFRAME
                short subtraction_rounds = 0;
            #endif

            // Keep subtracting from this until 
            // divisor * 2^(8 * divisor_shift) is bigger than this.
            while(! SHIFT_LESSER(divisor, divisor_shift, 
                                 (short)(division_round > 0 ? 
                                         division_round -1 : 0))
                  ) {
                // this is bigger or equal than the shifted divisor. 
                // Need to subtract some multiple of divisor from this.
                // Make a conservative estimation of the multiple to subtract.
                // We estimate a lower bound to avoid underflow, and continue
                // to subtract until the remainder in this gets smaller than
                // the shifted divisor.
                // For the estimation get first the two relevant digits 
                // from this and the first relevant digit from divisor.
                divident_digits = 
                    division_round == 0 ? 0 
                    : (DOUBLE_DIGIT_TYPE)
                         ((DOUBLE_DIGIT_TYPE)(value[(short)(division_round -1)])
                          << digit_len);
                divident_digits |= (DOUBLE_DIGIT_TYPE)
                    (value[division_round] & digit_mask);

                #ifdef BIGNAT_TESTFRAME
                    if(verbosity > 19) {
                        System.out.format("RD digits divident 0x%04X " +
                                          "divisor 0x%04X\n",
                                          divident_digits, first_divisor_digit);
                    }
                #endif

                // The multiple to subtract from this is 
                // divident_digits / divisor_digit, but there are two 
                // complications:
                // 1. divident_digits might be negative,
                // 2. both might be very small, in which case the estimated
                //    multiple is very inaccurate.
                if(divident_digits < 0) {
                    // case 1: shift both one bit to the right
                    // In standard java (ie. in the test frame) the operation
                    // for >>= and >>>= seems to be done in integers,
                    // even if the left hand side is a short. Therefore,
                    // for a short left hand side there is no difference 
                    // between >>= and >>>= !!!
                    // Do it the complicated way then.
                    divident_digits = (DOUBLE_DIGIT_TYPE)
                        ((divident_digits >>> 1) & positive_double_digit_mask);
                    divisor_digit = (DOUBLE_DIGIT_TYPE)
                        ((first_divisor_digit >>> 1) & 
                                           positive_double_digit_mask);

                    #ifdef BIGNAT_TESTFRAME
                        if(verbosity > 19) {
                            System.out.format("RD shifted right " +
                                              "divident 0x%04X divisor 0x%04X",
                                              divident_digits,
                                              divisor_digit);
                        }
                    #endif
                }
                else {
                    // To avoid case 2 shift both to the left 
                    // and add relevant bits.
                    divident_bit_shift = 
                        (short)(highest_bit(divident_digits) -1);
                    // Below we add one to divisor_digit to avoid underflow.
                    // Take therefore the highest bit of divisor_digit + 1
                    // to avoid running into the negatives.
                    bit_shift = divident_bit_shift <= divisor_bit_shift ? 
                                          divident_bit_shift : 
                                          divisor_bit_shift;

                    divident_digits = 
                        shift_bits(divident_digits, 
                            division_round < (short)(this.size -1) ? 
                                          value[(short)(division_round +1)] : 0,
                            division_round < (short)(this.size -2) ? 
                                          value[(short)(division_round +2)] : 0,
                            bit_shift);
                    divisor_digit =
                        shift_bits(first_divisor_digit,
                                   second_divisor_digit,
                                   third_divisor_digit,
                                   bit_shift);

                    #ifdef BIGNAT_TESTFRAME
                        if(verbosity > 19) {
                            System.out.format("RD shift div %d dis %d " +
                                              "shift %d : " +
                                              "divident 0x%04X divisor 0x%04X",
                                              divident_bit_shift,
                                              divisor_bit_shift,
                                              bit_shift,
                                              divident_digits,
                                              divisor_digit);
                        }
                    #endif
                    
                }

                // add one to divisor to avoid underflow
                multiple = 
                    (DOUBLE_DIGIT_TYPE)(divident_digits / 
                            (DOUBLE_DIGIT_TYPE)(divisor_digit + 1));

                #ifdef BIGNAT_TESTFRAME
                    if(verbosity > 19) {
                        System.out.format(" multiple %d == 0x%04X\n", 
                                          multiple, multiple);
                    }
                #endif

                // Our strategy to avoid underflow might yield multiple == 0.
                // We know however, that divident >= divisor, therefore make
                // sure multiple is at least 1.
                if(multiple < 1)
                    multiple = 1;

                times_minus(divisor, divisor_shift, multiple);

                // build quotient if desired
                if(quotient != null) {
                    // Express the size constraint only here. The check is
                    // essential only in the first round, because 
                    // divisor_shift decreases. divisor_shift must be 
                    // strictly lesser than quotient.size, otherwise
                    // quotient is not big enough. Note that the initially
                    // computed divisor_shift might be bigger, this
                    // is OK, as long as we don't reach this point.
                    ASSERT_TAG(divisor_shift < quotient.size, 0x28);

                    quotient_digit = (DOUBLE_DIGIT_TYPE)
                        ((quotient.value[(short)(quotient.size - 1 - 
                                                 divisor_shift)]
                          & digit_mask) 
                         + multiple);
                    ASSERT(quotient_digit < bignat_base);
                    quotient.value[(short)(quotient.size - 1 - 
                                           divisor_shift)] = 
                        (DIGIT_TYPE)(quotient_digit);
                }
                #ifdef BIGNAT_TESTFRAME
                    subtraction_rounds++; // XXX debug code!
                #endif
            }

            #ifdef BIGNAT_TESTFRAME
                if(verbosity > 19 && subtraction_rounds > 0)
                    System.out.format("RD %d subtractions in round %d\n",
                                      subtraction_rounds, division_round);
            #endif

            // treat loop indices
            division_round++;
            divisor_shift--;
        }
    }


    /**
     * Addition with carry report. Adds other to this number. If this
     * is too small for the result (i.e., an overflow occurs) the
     * method returns true. Further, the result in {@code this} will
     * then be the correct result of an addition modulo the first
     * number that does not fit into {@code this} ({@code 2^(}{@link
     * #digit_len}{@code * }{@link #size this.size}{@code )}), i.e.,
     * only one leading 1 bit is missing. If there is no overflow the
     * method will return false.
     * <P>
     *
     * It would be more natural to report the overflow with an {@link
     * javacard.framework.UserException}, however its {@link
     * javacard.framework.UserException#throwIt throwIt} method dies
     * with a null pointer exception when it runs in a host test
     * frame...
     * <P>
     *
     * Asserts that the size of other is not greater than the size of
     * this.
     *
     * @param other Bignat to add
     */
    public boolean add_carry(Bignat other)
    {
        ASSERT_TAG(this.size >= other.size, 0x29);
        DOUBLE_DIGIT_TYPE akku = 0;
        short j = (short)(this.size - 1);
        for(short i = (short)(other.size - 1); i >= 0; i--, j--) {
            akku = (DOUBLE_DIGIT_TYPE)(akku + 
                    (DOUBLE_DIGIT_TYPE)(this.value[j] & digit_mask) +
                    (DOUBLE_DIGIT_TYPE)(other.value[i] & digit_mask));

            this.value[j] = (DIGIT_TYPE)(akku & digit_mask);
            akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);
        }
        // add carry at position j
        while(akku > 0 && j >= 0) {
            akku = (DOUBLE_DIGIT_TYPE)(akku +
                        (DOUBLE_DIGIT_TYPE)(this.value[j] & digit_mask));
            this.value[j] = (DIGIT_TYPE)(akku & digit_mask);
            akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);
            j--;
        }

        return akku != 0;
    }


    /**
     * Addition. Adds other to this number. If this is too small for the 
     * result an assertion is thrown.
     * <P>
     * Same as {@link #times_add times_add}{@code (other, 1)}
     * but without the multiplication overhead.
     * <P>
     * Asserts that the size of other is not greater than the size of this.
     *
     * @param other Bignat to add
     */
    public void add(Bignat other) {
        if(add_carry(other)) {
            ASSERT_TAG(false, 0x2A);
        }
    }


    /**
     * Scaled addition. Add {@code mult * other} to this number. 
     * {@code mult} must be below {@link #bignat_base}, that is,
     * it must fit into one digit. It is only 
     * declared as a DOUBLE_DIGIT_TYPE here to avoid negative numbers.
     * <P>
     * Asserts (overly restrictive) that this and other have the 
     * same size.
     * <P>
     * Same as {@link #times_add_shift times_add_shift}{@code (other, 0, mult)}
     * but without the shift overhead.
     * <P>
     * Used in multiplication.
     *
     * @param other Bignat to add
     * @param mult of DOUBLE_DIGIT_TYPE, factor to multiply {@code other} 
     *               with before addition. Must be less 
     *               than {@link #bignat_base}.
     */
    public void times_add(Bignat other, DOUBLE_DIGIT_TYPE mult) {
        ASSERT_TAG(this.size == other.size, 0x2B);
        DOUBLE_DIGIT_TYPE akku = 0;
        for(short i = (short)(size - 1); i >= 0; i--) {
            akku = (DOUBLE_DIGIT_TYPE)(akku + 
                    (DOUBLE_DIGIT_TYPE)(this.value[i] & digit_mask) +
                    (DOUBLE_DIGIT_TYPE)(mult * (other.value[i] & digit_mask)));

            // if(debug) {
            //  System.out.format("TM %d: this %02X other %02X mult %04X " +
            //                    "akku %04X sub res %04X\n",
            //                    i,
            //                    value[i] & 0xff,
            //                    other.value[i] & 0xff,
            //                    mult,
            //                    akku & 0xffff,
            //                    subtraction_result & 0xffff);
            // }

            this.value[i] = (DIGIT_TYPE)(akku & digit_mask);
            akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);
        }
        ASSERT(akku == 0);      // otherwise we have an overflow
        return;
    }


    /**
     * Special addition for optimizing {@link #montgomery_mult 
     * montgomery_mult}.
     * Adds {@code second * second_mult + third * third_mult} to this.
     * This is an apparent optimization however, my measurements show that
     * it slows down the code (in the Bignat test frame).
     * <P>
     * {@code second_mult} and {@code third_mult} 
     * must be below {@link #bignat_base}, that is,
     * they must fit into one digit. They are only 
     * declared as a DOUBLE_DIGIT_TYPE here to avoid negative numbers.
     * <P>
     * Only available if OPT_DOUBLE_ADD is defined.
     * <P>
     * Asserts that this, {@code second} and {@code third} have the same size.
     *
     * @param second bignat to add, multiplied with {@code second_mult} 
     *          before added
     * @param second_mult of type DOUBLE_DIGIT_TYPE. Must be less 
     *               than {@link #bignat_base}.
     * @param third  other bignat to add, multiplied with 
     *          {@code third_mult} before added
     * @param third_mult of type DOUBLE_DIGIT_TYPE. Must be less 
     *               than {@link #bignat_base}.
     */
#if defined(OPT_DOUBLE_ADD) || defined(JAVADOC)
    public void times_add_add(Bignat second, DOUBLE_DIGIT_TYPE second_mult,
                              Bignat third, DOUBLE_DIGIT_TYPE third_mult) 
    {
        ASSERT_TAG(this.size == second.size && this.size == third.size, 0x2C);
        DOUBLE_DIGIT_TYPE akku_2 = 0;
        DOUBLE_DIGIT_TYPE akku_3 = 0;
        for(short i = (short)(size - 1); i >= 0; i--) {
            akku_2 = (DOUBLE_DIGIT_TYPE)(akku_2 + 
                     (DOUBLE_DIGIT_TYPE)(this.value[i] & digit_mask) +
                     (DOUBLE_DIGIT_TYPE)(second_mult * 
                                         (second.value[i] & digit_mask)));

            akku_3 = (DOUBLE_DIGIT_TYPE)(akku_3 + 
                     (DOUBLE_DIGIT_TYPE)(akku_2 & digit_mask) +
                     (DOUBLE_DIGIT_TYPE)(third_mult * 
                                         (third.value[i] & digit_mask)));

            this.value[i] = (DIGIT_TYPE)(akku_3 & digit_mask);
            akku_2 = (DOUBLE_DIGIT_TYPE)((akku_2 >> digit_len) & digit_mask);
            akku_3 = (DOUBLE_DIGIT_TYPE)((akku_3 >> digit_len) & digit_mask);
        }
        ASSERT(akku_2 == 0 && akku_3 == 0);     // otherwise we have an overflow
        return;
    }
#endif


    /**
     * Scaled addition.
     * Adds {@code mult * other * 2^(}{@link #digit_len}{@code * shift)}
     * to this. That is, shifts other {@code shift} digits to the left,
     * multiplies it with {@code mult} and adds then.
     * <P>
     * {@code mult} must be less than {@link #bignat_base}, that is,
     * it must fit into one digit. It is only 
     * declared as a DOUBLE_DIGIT_TYPE here to avoid negative numbers.
     * <P>
     * Asserts that the size of this is greater than or equal to
     * {@code other.size + shift + 1}.
     *
     * @param other Bignat to add
     * @param mult of DOUBLE_DIGIT_TYPE, factor to multiply {@code other} 
     *               with before addition. Must be less 
     *               than {@link #bignat_base}.
     * @param shift number of digits to shift {@code other} to the left, 
     *           before addition.
     */
    public void times_add_shift(Bignat other, short shift, 
                                DOUBLE_DIGIT_TYPE mult) 
    {
        // System.out.format("this.size %d other size %d shift %d\n",
        //                this.size, other.size, shift);
        ASSERT_TAG(this.size >= (short)(other.size + shift + 1), 0x2D);
        DOUBLE_DIGIT_TYPE akku = 0;
        short j = (short)(this.size - 1 - shift);
        for(short i = (short)(other.size - 1); i >= 0; i--, j--) 
            {
                akku = (DOUBLE_DIGIT_TYPE)(akku + 
                    (DOUBLE_DIGIT_TYPE)(this.value[j] & digit_mask) +
                    (DOUBLE_DIGIT_TYPE)(mult * (other.value[i] & digit_mask)));

            this.value[j] = (DIGIT_TYPE)(akku & digit_mask);
            akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);
        }
        // add carry at position j
        akku = (DOUBLE_DIGIT_TYPE)(akku +
                        (DOUBLE_DIGIT_TYPE)(this.value[j] & digit_mask));
        this.value[j] = (DIGIT_TYPE)(akku & digit_mask);
        // assert no overflow
        ASSERT(((akku >> digit_len) & digit_mask) == 0);
        return;
    }


    /**
     * Multiplication.
     * Stores {@code x * y} in this. To ensure this is big enough for 
     * the result it is asserted that the size of this is greater than 
     * or equal to the sum of the sizes of {@code x} and {@code y}.
     *
     * @param x first factor
     * @param y second factor
     */
    public void mult(Bignat x, Bignat y) {
        // System.out.format("this size %d x size %d y size %d\n",
        //                this.size, x.size, y.size);
        ASSERT_TAG(this.size >= (short)(x.size + y.size), 0x2E);

        this.zero();
        for(short i = (short)(y.size -1); i >= 0; i--) {
            this.times_add_shift(x, (short)(y.size - 1 - i),
                       (DOUBLE_DIGIT_TYPE)(y.value[i] & digit_mask));
        }
        return;
    }


    /**
     * One digit left shift.
     * <P>
     * Asserts that the first digit is zero.
     */
    public void shift_left() {
        ASSERT(value[0] == 0);
        #ifdef BIGNAT_USE_BYTE
          Misc.array_copy(this.value, (short)1, this.value, (short)0, 
                          (short)(size -1));
        #else
          System.arraycopy(this.value, 1, this.value, 0, size -1);
        #endif
        value[(short)(size -1)] = 0;
    }


    /**
     * Inefficient modular multiplication.
     * 
     * This bignat is assigned to {@code x * y} modulo {@code mod}. 
     * Inefficient, because it computes the modules with 
     * {@link #remainder_divide remainder_divide} in each multiplication round.
     * To avoid overflow the first two digits of {@code x} and {@code mod}
     * must be zero (which plays nicely with the requirements for 
     * montgomery multiplication, see {@link #montgomery_mult montgomery_mult}).
     * <P>
     * Asserts that {@code x} and {@code mod} have the same size. Argument 
     * {@code y} can be arbitrary in size.
     * <P>
     * Included here to make it possible to compute the squared
     * <a href="package-summary.html#montgomery_factor">montgomery
     * factor</a>, which 
     * is needed to montgomerize numbers before montgomery multiplication.
     * Until now this has never been used, because the 
     * montgomery factors are computed on the host and then installed 
     * on the card. Or numbers are montgomerized on the host already.
     *
     * @param x first factor, first two digits must be zero
     * @param y second factor
     * @param mod modulus, first two digits must be zero
     */
    public void mult_mod(Bignat x, Bignat y, Bignat mod) {
        ASSERT_TAG(this.size == x.size && this.size == mod.size, 0x2F);
        ASSERT(x.value[0] == 0 &&
               x.value[1] == 0 && 
               mod.value[0] == 0 &&
               mod.value[1] == 0);
        this.zero();
        for(short i = 0; i < y.size; i++) {
            #ifdef BIGNAT_TESTFRAME
               if(verbosity >= 10)
                   System.out.format("MM akku    %s\n", this.to_hex_string());
            #endif

            this.shift_left();

            #ifdef BIGNAT_TESTFRAME
               if(verbosity >= 10)
                   System.out.format("MM shifted %s\n", this.to_hex_string());
            #endif

            this.times_add(x, (DOUBLE_DIGIT_TYPE)(y.value[i] & digit_mask));

            #ifdef BIGNAT_TESTFRAME
               if(verbosity >= 10)
                   System.out.format("MM x * " + digit_format + " added %s\n", 
                                     y.value[i] & digit_mask,
                                     this.to_hex_string());
            #endif

            this.remainder_divide(mod, null);
        }
        return;
    }


    /**
     * One digit right shift.
     * Asserts that the least significant digit is zero.
     */
    public void shift_right() {
        ASSERT(value[(short)(size -1)] == 0);
        #ifdef BIGNAT_USE_BYTE
           Misc.array_copy(this.value, (short)0, this.value, (short)1, 
                           (short)(size -1));
        #else
           System.arraycopy(this.value, 0, this.value, 1, size -1);
        #endif
        value[0] = 0;
    }


    /**
     * <a href="package-summary.html#montgomery_factor">montgomery
     * factors</a>. Stores the <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a> with respect to {@code mod} and {@code
     * mont_fac.size} in argument {@code mont_fac} and the squared <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a> in this bignat. <P>
     *
     * Asserts that this and the two arguments have the same size.
     * <P>
     *
     * The argument {@code mont_fac} started out as temporary, that is
     * needed to compute the squared <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a>. When writing the documentation I noticed that it
     * acidentially contains the <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a> at the end.
     * 
     *
     * @param mod modulus
     *
     * @param mont_fac gets the <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a> assigned
     */
    // Stores the squared montgomery factor for mod in this. 
    // Montgomery multiplication below works with size - 2 
    // digits (bytes).
    // Therefore, the Montgomery factor is 
    // 2^(8 * (size -2)) modulo mod.
    // Need to supply a temp Bignat for the computation.
    public void montgomery_factor(Bignat mod, Bignat mont_fac) {
        ASSERT_TAG(this.size == mod.size && this.size == mont_fac.size, 0x30);
        mont_fac.zero();
        mont_fac.value[1] = 1;                  // mont_fac = 2^(8 * (size -2))
        mont_fac.remainder_divide(mod, null); // mont_fac = mont_fac modulo mod
        this.mult_mod(mont_fac, mont_fac, mod); // this = mont_fac^2 modulo mod
        return;
    }


    // Montgomery multiplication as I understood it: 
    // The aim is to compute x * y modulo mod without allocating
    // more memory and without taking the modulus in every 
    // multiplication round. Therefore one chooses b^n > mod, 
    // where b is the base that is used in the multiplication and
    // n is the maximal number of digits. For pen and paper 
    // multiplication usually b = 10. Here b = 256, because 
    // one byte is one digit here. Note that also x and y must
    // be less than b^n. Because everything is finally taken
    // modulo mod one works of course with b^n modulo mod.
    // This b^n modulo mod is called the Montgomery factor mont_fak.
    // 
    // Now instead of computing x * y one first computes 
    // x' = x * mont_fak and similarly for y. This is called 
    // montgomerization. 
    // Now one computes (x' * y' / mont_fac) modulo mod. 
    // Note that the result is again of the form  z * mont_fac, where
    // z = x * y denotes the real result.
    // The computation (x' * y' / mont_fac) modulo mod
    // can easily be done in (n+2) bytes by shifting the akkumulator
    // one digit to the right after each multiplication round 
    // (and ensuring that there are presicely n multiplication rounds).
    // Only one modulo operation is needed at the end. 
    // The only remaining problem is that the right shift,
    // which is in fact a division by the base b, must not change the
    // the remainder modulo mod. Therefore one adds a Montgomery
    // correction to the akkumulator after each multilication round.
    // This Montgomery correction will be chosen such that the 
    // akkumulator is divisible by the base b (ie. the last digit is 
    // zero). The Montgomery correction exists if the modulus is odd,
    // which we take here for granted. The Montgomery correction is
    // computed with the inverses modulo 256 of the last digit of mod,
    // see class Inverse_mod_256.

    /**
     * Montgomery multiplication (special modular multiplication).
     * Stores {@code x} * {@code y} / {@code mont_fac} (modulo {@code
     * mod}) in this bignat, where {@code mont_fac} is the <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a> for {@code mod} and {@code this.size}. Computes the
     * modular montgomerized product if the arguments are
     * montgomerized. <P>
     *
     * Asserts that this and the arguments all have the same size.
     * Further the first two digits of {@code x}, {@code y} and {@code mod}
     * must be zero.
     * As unchecked assumption {@code mod} must be odd. Strange things
     * will happen for even moduli (Note that with the standard procedure to
     * obtain a {@link Modulus}, namely by initializing and sending a 
     * {@link Host_modulus} to the card, it is impossible to obtain 
     * an even modulus).
     * <P>
     * The requirement for an odd modulus comes from the following.
     * The dividision by {@code mont_fac} is actually done by shifting
     * the akkumulator one digit to the right after each multiplication 
     * round. Because the first two digits of all arguments are zero, we do
     * {@link #size} -2 multiplication rounds, that is, with one shift
     * each round, we precisely divide by 
     * 2^({@link #digit_len} * ({@code size} - 2)), ie, by {@code mont_fac}.
     * Before the right shift the least significant digit must be 
     * zero, otherwise we compute wrong results. To get a zero
     * there we just add x * {@code mod}, where x is precisely that 
     * factor that makes the last digit of the sum zero. 
     * Assume byte digits (BIGNAT_USE_BYTE) and that the 
     * last digit of the akkumulator is 255. Then we need to add x * 
     * {@code mod} such that x * {@code mod} = 1 (modulo 256). 
     * That is, x must be the modular inverse of {@code mod}
     * modulo 256. If the last digit is 254 we simply add 
     * 2 * x * {@code mod}, which equals 2 (modulo 256).
     * The required modular inverse exists precisely when {@code mod} 
     * and 256 (or {@link #bignat_base}, more generally) are coprime,
     * which happens precisely when {@code mod} is odd.
     * <P>
     * With some computations one can show that after the right shift the 
     * akkumulator always fits into {@link #size} -1 digits without the 
     * need of computing remainders modulo {@code mod} in between. 
     * To ensure the result fits in {@link #size} -2 digits and is actually
     * less then {@code mod} a final 
     * {@link #remainder_divide remainder_divide} is done.
     * 
     * @param x first factor, first two digits must be zero
     * @param y second factor, first two digits must be zero
     * @param mod modulus, must be odd, first two digits must be zero
     * @CPP The following preprocessor directives select variants of
     *   this method:
     *   <a href="../../../overview-summary.html#OPT_DOUBLE_ADD">OPT_DOUBLE_ADD</a>, 
     *   <a href="../../../overview-summary.html#OPT_SKIP_DEVIDE">OPT_SKIP_DEVIDE</a>,
     *   <a href="../../../overview-summary.html#MONTGOMERY_MULT_SHORTCUT">MONTGOMERY_MULT_SHORTCUT</a>
     *
     *
     * Both optimizations produced only almost unnoticably effects in my tests.
     */
    public void montgomery_mult(Bignat x, Bignat y, Modulus mod) {
        ASSERT_TAG(this.size == x.size &&
                   this.size == y.size &&
                   this.size == mod.m.size, 0x31);
        // x, y and mod must have at most size - 2 digits.
        ASSERT(x.value[0] == 0 && x.value[1] == 0 &&
               y.value[0] == 0 && y.value[1] == 0 &&
               mod.m.value[0] == 0 && mod.m.value[1] == 0);

        #ifdef MONTGOMERY_MULT_SHORTCUT
            this.one();
            if(true)
                return;
        #endif

        DOUBLE_DIGIT_TYPE correction_factor; // used & initialized in the loop
        this.zero();
        for(short i = (short)(size -1); i >= 2; i--) {
          #ifdef OPT_DOUBLE_ADD
             // first compute the last digit of this + x * y.value[i]
             // in correction_factor
             correction_factor = 
                 (this.value[(short)(size -1)] +
                  (x.value[(short)(size -1)] * y.value[i])) & digit_mask;
             correction_factor = 
                 (correction_factor * mod.last_digit_inverse) & digit_mask;
             this.times_add_add(x, (DOUBLE_DIGIT_TYPE)(y.value[i] & digit_mask),
                                mod.m, correction_factor);
             this.shift_right(); // shift_right asserts value[size -1] == 0
          #else
             this.times_add(x, (DOUBLE_DIGIT_TYPE)(y.value[i] & digit_mask));
             correction_factor = (DOUBLE_DIGIT_TYPE)(
                           (this.value[(short)(size -1)] * 
                            mod.last_digit_inverse)
                           & digit_mask);
             this.times_add(mod.m, correction_factor);
             this.shift_right(); // shift_right asserts value[size -1] == 0
          #endif
        }
        #ifdef OPT_SKIP_DEVIDE
           if(this.value[1] != 0)
               this.remainder_divide(mod.m, null);
        #else
           this.remainder_divide(mod.m, null);
        #endif

        return;
    }



    // The optimization idea for squaring x is as follows. Assume
    // x has three digits, so x = abc. Then
    // x^2 = 2ac + 2bc + c^2   +   2ab + b^2   +   a^2
    // Well, in that equation I ignored digit positions, so more
    // precisely it would be  x = a*z^2 + b*z^1 + c*z^0 with base z and
    // x^2 = 2ac*z^2 + 2bc*z^1 + c^2*z^0   +   2ab*z^3 + b^2*z^2   +   a^2*z^4
    // 
    // One can thus save about the half of the single digit multiplications.
    // The problem is that the intermediate products (2ac, 2bc,...) do not
    // fit in a double digit. Computing x^2 / 2 as the BigInteger library
    // does not work here, because of the interwinded Montgomery
    // reduction. 
    // (To see this consider for instance x = 4, mod = 11 with 
    // respect to 2^32 or 2^12. Then the montgomery factor is 4 and its 
    // inverse is 3. Now 4^2 % 11 == 5 and 4^2 / 2 % 11 == 8. 
    // Montgomerizing 4 gives 5 [4 * 4 % 11], so this method should 
    // compute 5^2 / 2^12 % 11 == 5^2 * 3 % 11 == 9, which fits the original
    // result 5, because montgomerizing 5 gives 9. However, 
    // 5^2 / 2 * 3 % 11 is something completely different.)
    // 
    // Note that, for the factors 2ac... one needs just one extra bit. 
    // So we directly compute x^2 and keep one additional bit with the
    // akkumulator.

    /**
     * Optimization for modular Montgomery squares. Stores {@code x} *
     * {@code x} / {@code mont_fac} (modulo {@code mod}) in this
     * bignat, where {@code mont_fac} is the <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a> for {@code mod} and {@code this.size}. Computes the
     * modular montgomerized square of {@code x} if {@code x} is
     * montgomerized. Equivalent to {@link #montgomery_mult
     * montgomery_mult}(x, x, mod). <P>
     *
     * Asserts that {@code this} and the arguments all have the same size
     * and that the first two digits of {@code x} and {@code mod} are zero.
     * Does only work for odd moduli, as explained for 
     * {@link #montgomery_mult montgomery_mult}.
     * <P>
     * Only available when OPT_SPECIAL_SQUARE is defined.
     * <P>
     * The optimization is based on the fact that for squares about 
     * half of the single digit multiplications can be saved. There are 
     * however some complications, because some intermediate results do
     * not fit into DOUBLE_DIGIT_TYPE any longer (as they do inside {@link 
     * #montgomery_mult montgomery_mult}). 
     * <P>
     * Does not deliver any speedups in my measurements.
     *
     * @param x number to square, first two digits must be zero
     * @param mod modulus, first two digits must be zero
     */
#if defined(OPT_SPECIAL_SQUARE) || defined(JAVADOC)
    public void montgomery_square(Bignat x, Modulus mod) {
        ASSERT_TAG(this.size == x.size &&
                   this.size == mod.m.size, 0x32);
        // x, y and mod must have at most size - 2 digits.
        ASSERT(x.value[0] == 0 && x.value[1] == 0 &&
               mod.m.value[0] == 0 && mod.m.value[1] == 0);

        #ifdef BIGNAT_TESTFRAME
           if(verbosity > 14)
               System.out.format("MS square %s\n", x.to_hex_string());
        #endif

        // used in the loop
        DOUBLE_DIGIT_TYPE correction_factor, akku, current_digit; 
        DOUBLE_DIGIT_TYPE additional_bit, factor;
        this.zero();
        for(short i = (short)(size - 1); i >= 2; i--) {
            current_digit = (DOUBLE_DIGIT_TYPE)(x.value[i] & digit_mask);

            #ifdef BIGNAT_TESTFRAME
               if(verbosity > 14)
                   System.out.format("MS round %d current digit " + 
                                     digit_format + "\n" +
                                     "this = %s\n",
                                     i, current_digit,
                                     this.to_hex_string());
            #endif

            // first sqare digit i
            akku = (DOUBLE_DIGIT_TYPE)(current_digit * current_digit);

            // add squared digit i to result digit i
            akku = (DOUBLE_DIGIT_TYPE)(akku + (this.value[i] & digit_mask));
            this.value[i] = (DIGIT_TYPE)(akku & digit_mask);

            akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);

            // add now the remaining digit factors (2bc, 2ab,...)
            additional_bit = 0;
            for(short j = (short)(i - 1); j >= 2; j--) {
                factor = (DOUBLE_DIGIT_TYPE)
                    (current_digit * (x.value[j] & digit_mask));
                akku = (DOUBLE_DIGIT_TYPE)(akku +
                                           additional_bit +
                                           (this.value[j] & digit_mask) +
                                           2 * factor);
                // decide if we got a carry when computing akku
                if((factor & highest_double_digit_bit) != 0 || 
                   (((2 * factor) & highest_double_digit_bit) != 0 && 
                    (akku & highest_double_digit_bit) == 0))
                    additional_bit = bignat_base;
                else
                    additional_bit = 0;

                #ifdef BIGNAT_TESTFRAME
                   if(verbosity > 14)
                       System.out.format("MS hi bit " + double_digit_format + 
                                         " fac & hi " + double_digit_format +
                                         " 2fac & hi " + double_digit_format +
                                         " akku & hi " + double_digit_format +
                                         "\n",
                                         highest_double_digit_bit,
                                         factor & highest_double_digit_bit,
                                         (2 * factor) & 
                                               highest_double_digit_bit,
                                         akku & highest_double_digit_bit);

                   if(verbosity > 14)
                       System.out.format("MS inner loop %d factor " +
                                         double_digit_format + " akku " +
                                         double_digit_format + " bit " +
                                         double_digit_format + "\n" +
                                         "akku = %s\n",
                                         j, factor, akku, additional_bit,
                                         this.to_hex_string());
                #endif

                this.value[j] = (DIGIT_TYPE)(akku & digit_mask);
                akku = (DOUBLE_DIGIT_TYPE)((akku >> digit_len) & digit_mask);
            }
            // add the akkumulated carry in akku to digit 0 and digit 1
            akku = (DOUBLE_DIGIT_TYPE)(akku +
                                       additional_bit +
                                       (this.value[1] & digit_mask));
            this.value[1] = (DIGIT_TYPE)(akku & digit_mask);
            ASSERT(this.value[0] == 0);
            this.value[0] = (DIGIT_TYPE)((akku >> digit_len) & digit_mask);

            #ifdef BIGNAT_TESTFRAME
               if(verbosity > 14)
                   System.out.format("MS before reduction %s\n",
                                     this.to_hex_string());
            #endif

            // Do now the partial Montgomery reduction and shift 
            // this to the right.

            correction_factor = (DOUBLE_DIGIT_TYPE)(
                          (this.value[(short)(size -1)] * 
                           mod.last_digit_inverse)
                          & digit_mask);
            this.times_add(mod.m, correction_factor);

            #ifdef BIGNAT_TESTFRAME
               if(verbosity > 14)
                   System.out.format("MS correction factor " + 
                                     digit_format + "\n" +
                                     "akku = %s\n",
                                     correction_factor,
                                     this.to_hex_string());
            #endif

            this.shift_right(); // shift_right asserts value[size -1] == 0
        }

        this.remainder_divide(mod.m, null);
        return;
    }
#endif



    /**
     * Demontgomerization. Divides {@code this} by the 
     * <a href="package-summary.html#montgomery_factor">montgomery factor</a>.
     * Equivalent to montgomery multiplication with 1, but 
     * twice as fast.
     * <P>
     * Asserts that this and {@code mod} have the same size and 
     * that the first two digits of this and {@code mod} are zero.
     * {@code mod} must be odd, see disscussion in {@link
     * #montgomery_mult montgomery_mult}.
     *
     * @param mod modulus, the first two digits must be zero
     */
    public void demontgomerize(Modulus mod) {
        ASSERT_TAG(this.size == mod.m.size, 0x33);
        // this and mod must have at most size - 2 digits.
        ASSERT(this.value[0] == 0 && this.value[1] == 0 &&
               mod.m.value[0] == 0 && mod.m.value[1] == 0);

        DOUBLE_DIGIT_TYPE correction_factor; // used & initialized in the loop
        for(short i = (short)(size -1); i >= 2; i--) {
            correction_factor = (DOUBLE_DIGIT_TYPE)(
                          (this.value[(short)(size -1)] * 
                           mod.last_digit_inverse)
                          & digit_mask);
            this.times_add(mod.m, correction_factor);
            this.shift_right(); // shift_right asserts value[size -1] == 0
        }
        this.remainder_divide(mod.m, null);
        return;
    }


    /**
     * Modular power. Computes {@code base^exponent (modulo
     * modulus)} and stores the result in this bignat. {@code base}
     * must be montgomerized and {@code exponent} must not. The result
     * will be montgomerized. Needs a temporary for the computation
     * and a montgomerized 1 (which is equal to the <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a> and can be found, for instance, in {@link
     * Host_modulus#mont_fac}) to initialize the akkumulator. <P>
     *
     * Asserts that {@code this}, {@code base}, {@code modulus}, 
     * {@code mont_one} and {@code temp} all have the same size.
     * The size of the exponent can be arbitrary.
     * {@code base} and {@code modulus} must fulfill the preconditions
     * of montgomery multiplication, that is, there first two digits must
     * be zero.
     * <P>
     * Uses the repeated squaring method internally without further
     * optimizations (so it pays off if there are not too many leading 
     * zeros in the exponent).
     *
     * @param base montgomerized base
     * @param exponent exponent (not montgomerized)
     * @param modulus the modulus
     *
     * @param mont_one 1, montgomerized (which equals the <a
     * href="package-summary.html#montgomery_factor">montgomery
     * factor</a>, see {@link Host_modulus#mont_fac})
     *
     * @param temp a temporary, different from all other arguments.
     */
    public void exponent_mod(Bignat base, Bignat exponent, 
                             Modulus modulus,
                             Bignat mont_one, Bignat temp) 
    {
        ASSERT_TAG(this.size == base.size &&
                   this.size == modulus.m.size &&
                   this.size == mont_one.size &&
                   this.size == temp.size, 0x34);
        ASSERT(base.value[0] == 0 && base.value[1] == 0 &&
               modulus.m.value[0] == 0 && modulus.m.value[1] == 0);
        
        Bignat temp_1 = this;
        Bignat temp_2 = temp;
        Bignat temp_3;          // Used only for swapping temp_1 and temp_2.

        // Initialize temp_1 with one. That is, with a montgomerized one!
        temp_1.copy(mont_one);

        DIGIT_TYPE current_exponent_digit;
        DOUBLE_DIGIT_TYPE bit;
        for(short i = 0; i < exponent.size; i++) {
            current_exponent_digit = exponent.value[i];
            bit = Bignat.highest_digit_bit;
            while(bit != 0) {
                // System.out.format("XXX %d %02X\n", i, bit);
                // Square akku.
                #ifdef OPT_SPECIAL_SQUARE
                    temp_2.montgomery_square(temp_1, modulus);
                #else
                    temp_2.montgomery_mult(temp_1, temp_1, modulus);
                #endif

                // Multiply with base if exponent bit is 1.
                if((current_exponent_digit & bit & digit_mask) != 0) {
                    temp_1.montgomery_mult(temp_2, base, modulus);
                }
                else {
                    // Swap temp values to prepare for the next iteration
                    // where the akkumulator is expected in temp_1.
                    temp_3 = temp_1;
                    temp_1 = temp_2;
                    temp_2 = temp_3;
                }

                bit >>= 1;
            }
        }

        // Copy result in temp_1 into this, if they are different.
        if(temp_1 != this)
            this.copy(temp_1);
    }


    /**
     * 
     * Division by 2. Shifts all bits one to the right.
     */
    public void div_2() {
        DOUBLE_DIGIT_TYPE carry = 0;
        for(short i = 0; i < this.size; i++) {
            if((this.value[i] & 0x01) == 0) {
                this.value[i] = (DIGIT_TYPE)(
                    ((this.value[i] & digit_mask) >> 1) | carry);
                carry = 0;
            }
            else {
                this.value[i] = (DIGIT_TYPE)(
                    ((this.value[i] & digit_mask) >> 1) | carry);
                carry = digit_first_bit_mask;
            }
        }
    }


    /**
     * 
     * Modular division by 2. This method computes {@code x modulo
     * mod} on the assumption that {@code 2x modulo mod} is in this
     * bignat before calling this method. 
     * <P>
     *
     * The most significant bit of the modulus {@code mod} (and
     * therefore also the most significant bit of this bignat) must be
     * zero, otherwise an assertion might get triggered inside {@link
     * #add add}.
     * 
     * @param mod modulus
     */
    public void modular_div_2(Modulus mod) {
        if((this.value[(short)(this.size - 1)] & 0x01) != 0) {
            add(mod.m);
        }

        div_2();
    }


    /**
     * 
     * Modular multiplication. Computes {@code x * y modulo mod} and
     * stores the result in this bignat. Internally the equation
     * {@code x*y = ((x+y)^2 - x^2 - y^2)/2} will be exploited,
     * whereby the squares are computed via {@link RSA_exponent} on
     * the crypto coprocessor. <P>
     *
     * The most significant bit of the modulus {@code mod} must be
     * zero, otherwise an overflow might lead to a failing assertion
     * inside {@link #add add}. 
     * <P>
     *
     * The modulus and the exponent {@code 2} must have been installed
     * in {@code square_exp} before calling this method.
     * <P>
     *
     * This bignat and the arguments {@code x}, {@code y}, {@code
     * mod}, and {@code temp} must all have the same size, which must
     * be equal to the key size of {@code square_exp}.
     * 
     * @param x first factor
     * @param y second factor
     * @param mod modulus
     * @param square_exp the instance for fast squaring
     * @param temp temporary
     */
    public void squared_rsa_mult_2(Bignat x, Bignat y, Modulus mod, 
                                 RSA_exponent_interface square_exp,
                                 Bignat temp)
    {
        // 1: temp = x + y
        temp.copy(x);
        temp.add(y);

        // 2: this = (x + y)^2
        square_exp.fixed_power(temp, this, (short)0);

        // 3: this = (x + y)^2 - x^2
        square_exp.fixed_power(x, temp, (short)0);
        modular_subtraction(temp, mod);

        // 4: this = (x + y)^2 - x^2 - y^2 = 2*x*y
        square_exp.fixed_power(y, temp, (short)0);
        modular_subtraction(temp, mod);

        // 5: this = ((x + y)^2 - x^2 - y^2) / 2 = x*y
        modular_div_2(mod);
        return;
    }
                                 


    /**
     * 
     * Multiplication of short bignats. Computes {@code x * y} and
     * stores the result in this bignat. Internally the equation
     * {@code x*y = ((x+y)^2 - x^2 - y^2)/2} will be exploited,
     * whereby the squares are computed via {@link RSA_exponent} on
     * the crypto coprocessor.
     * <P>
     *
     * Does only work correctly under the following assumptions.
     * <UL>
     * <LI>The modulus inside {@code square_exp} is greater then
     * {@code (x + y)^2}.</LI>
     * <LI>This bignat is big enough to hold {@code (x + y)^2}.</LI>
     * <LI>The modulus and the exponent {@code 2} have been installed
     * in {@code square_exp} before calling this method.</LI>
     * <LI>All of {@code temp_1, temp_2, mod} and {@code square_exp}
     * have the same size.</LI>
     * </UL>
     * If the first condition is violated this method might silently
     * produce wrong results or some assertion might fail. An
     * assertion will definitely be thrown if one of the other
     * conditions is violated.
     * <P>
     *
     * The conditions permit in particular {@code x} and {@code y}
     * that are shorter than half the size of {@code mod}.
     * 
     * @param x first factor
     * @param y second factor
     * @param square_exp the instance for fast squaring
     * @param temp_1 temporary
     * @param temp_2 temporary
     */
    public void short_squared_rsa_mult_2(Bignat x, Bignat y, 
                                         RSA_exponent_interface square_exp,
                                         Bignat temp_1, Bignat temp_2)
    {
        // 1: temp_1 = x + y
        temp_1.copy(x);
        temp_1.add(y);

        // 2: this = (x + y)^2
        square_exp.fixed_power(temp_1, temp_2, (short)0);
        this.copy(temp_2);

        // 3: this = (x + y)^2 - x^2
        temp_1.copy(x);
        square_exp.fixed_power(temp_1, temp_2, (short)0);
        if(subtract(temp_2)) {
            ASSERT_TAG(false, 0x35);
        }

        // 4: this = (x + y)^2 - x^2 - y^2 = 2*x*y
        temp_1.copy(y);
        square_exp.fixed_power(temp_1, temp_2, (short)0);
        if(subtract(temp_2)) {
            ASSERT_TAG(false, 0x36);
        }

        // 5: this = ((x + y)^2 - x^2 - y^2) / 2 = x*y
        ASSERT_TAG((this.value[(short)(this.size - 1)] & 0x01) == 0, 0x37);
        div_2();
        return;
    }
                                 


    /**
     * 
     * Division by 4. Shifts all bits two places to the right.
     */
    public void div_4() {
        DOUBLE_DIGIT_TYPE carry = 0;
        for(short i = 0; i < this.size; i++) {
            switch(this.value[i] & 0x03) {
            case 0x00:
                this.value[i] = (DIGIT_TYPE)(
                    ((this.value[i] & digit_mask) >> 2) | carry);
                carry = 0;
                break;

            case 0x01:
                this.value[i] = (DIGIT_TYPE)(
                    ((this.value[i] & digit_mask) >> 2) | carry);
                carry = digit_second_bit_mask;
                break;

            case 0x02:
                this.value[i] = (DIGIT_TYPE)(
                    ((this.value[i] & digit_mask) >> 2) | carry);
                carry = digit_first_bit_mask;
                break;

            case 0x03:
                this.value[i] = (DIGIT_TYPE)(
                    ((this.value[i] & digit_mask) >> 2) | carry);
                carry = digit_first_two_bit_mask;
                break;

            default:
                ASSERT(false);
            }
        }
    }


    /**
     * 
     * Modular division by 4. This method computes {@code x modulo
     * mod} on the assumption that {@code 4x modulo mod} is in this
     * bignat before calling this method. 
     * <P>
     *
     * The two most significant bits of the modulus {@code mod} (and
     * therefore also the most significant bit of this bignat) must be
     * zero, otherwise an assertion might get triggered inside {@link
     * #add add}.
     * <P>
     *
     * The multiples of the modulus must had been allocated with
     * {@link Modulus#allocate_multiples Modulus.allocate_multiples}
     * before the modulus has been initialized via {@link
     * Modulus#from_byte_array Modulus.from_byte_array}.
     * <P>
     *
     * The modulus must be equivalent to {@code 1 modulo 4} otherwise
     * wrong results may be computed.
     * 
     * @param mod modulus
     */
    public void modular_div_4(Modulus mod) {
        ASSERT_TAG(mod.mod_x_2 != null && mod.mod_x_3 != null, 0x38);

        switch(this.value[(short)(this.size - 1)] & 0x03) {
        case 0x00:
            break;

        case 0x01:
            add(mod.mod_x_3);
            break;

        case 0x02:
            add(mod.mod_x_2);
            break;

        case 0x03:
            add(mod.m);
            break;

        default:
            ASSERT(false);
        }

        div_4();
    }


    /**
     * 
     * Yet another modular multiplication. Computes {@code x * y
     * modulo mod} and stores the result in this bignat. This time 
     * the equation {@code x*y = ((x+y)^2 - (x-y)^2)/4} will be
     * exploited. The squares are computed via {@link
     * RSA_exponent} on the crypto coprocessor. <P>
     *
     * The first two most significant bits of the modulus {@code mod}
     * must be zero, otherwise an overflow might lead to a failing
     * assertion inside {@link #add add}. The multiples of the modulus
     * must had been allocated with {@link Modulus#allocate_multiples
     * Modulus.allocate_multiples} before the modulus has been
     * initialized via {@link Modulus#from_byte_array
     * Modulus.from_byte_array}. 
     * <P>
     *
     * The modulus must be equivalent to {@code 1 modulo
     * 4} otherwise wrong results may be computed.
     * <P>
     *
     * The modulus and the exponent {@code 2} must have been installed
     * in {@code square_exp} before calling this method.
     * <P>
     *
     * This bignat and the arguments {@code x}, {@code y}, {@code
     * mod}, and {@code temp} must all have the same size, which must
     * be equal to the key size of {@code square_exp}.
     * <P>
     *
     * The second argument {@code y} is used as second temporary and
     * will therefore be modified.
     * 
     * @param x first factor
     * @param y second factor (used as temporary and modified in this method)
     * @param mod modulus
     * @param square_exp the instance for fast squaring
     * @param temp temporary
     */
    public void squared_rsa_mult_4(Bignat x, Bignat y, Modulus mod, 
                                   RSA_exponent_interface square_exp,
                                   Bignat temp)
    {
        // 1: this = x - y  OR  this = y - x
        if(y.shift_lesser(x, (short)0, (short)0)) {
            this.copy(x);
            if(this.subtract(y)) {
                ASSERT(false);
            }
        }
        else {
            this.copy(y);
            if(this.subtract(x)) {
                ASSERT(false);
            }
        }

        // 2: temp = (x-y)^2 = (y-x)^2
        square_exp.fixed_power(this, temp, (short)0);

        // 3: y = x + y
        y.add(x);

        // 4: this = (x+y)^2
        square_exp.fixed_power(y, this, (short)0);

        // 5: this = (x+y)^2 - (x-y)^2
        this.modular_subtraction(temp, mod);

        // 6: this = ((x + y)^2 - (x-y)^2) / 4 = x*y
        modular_div_4(mod);

        return;
    }
                                 


    /**
     * 
     * Another multiplication of short bignats. Computes {@code x * y}
     * and stores the result in this bignat. Internally the equation
     * {@code x*y = ((x+y)^2 - (x - y)^2)/4} is used, whereby the
     * squares are computed via {@link RSA_exponent} on the crypto
     * coprocessor. <P>
     *
     * Does only work correctly under the following assumptions.
     * <UL>
     * <LI>The modulus inside {@code square_exp} is greater then
     * {@code (x + y)^2}.</LI>
     * <LI>This bignat is big enough to hold {@code (x + y)^2}.</LI>
     * <LI>The modulus and the exponent {@code 2} have been installed
     * in {@code square_exp} before calling this method.</LI>
     * <LI>All of {@code temp_1, temp_2, mod} and {@code square_exp}
     * have the same size.</LI>
     * </UL>
     * If the first condition is violated this method might silently
     * produce wrong results or some assertion might fail. An
     * assertion will definitely be thrown if one of the other
     * conditions is violated.
     * <P>
     *
     * The conditions permit in particular {@code x} and {@code y}
     * that are shorter than half the size of {@code mod}. This bignat
     * should have the double size of {@code x} but can be smaller
     * than {@code mod}.
     * 
     * @param x first factor
     * @param y second factor
     * @param square_exp the instance for fast squaring
     * @param temp_1 temporary
     * @param temp_2 temporary
     */
    public void short_squared_rsa_mult_4(Bignat x, Bignat y, 
                                         RSA_exponent_interface square_exp,
                                         Bignat temp_1, Bignat temp_2)
    {
        // 1: temp_1 = x + y
        temp_1.copy(x);
        temp_1.add(y);

        // 2: this = (x + y)^2
        square_exp.fixed_power(temp_1, temp_2, (short)0);
        this.copy(temp_2);

        // 3: temp_1 = x - y  OR  temp_1 = y - x
        if(y.shift_lesser(x, (short)0, (short)0)) {
            temp_1.copy(x);
            if(temp_1.subtract(y)) {
                ASSERT(false);
            }
        }
        else {
            temp_1.copy(y);
            if(temp_1.subtract(x)) {
                ASSERT(false);
            }
        }

        // 4: temp_2 = (x-y)^2 = (y-x)^2
        square_exp.fixed_power(temp_1, temp_2, (short)0);

        // 5: this = (x + y)^2 - (x - y)^2 = 4*x*y
        if(subtract(temp_2)) {
            ASSERT_TAG(false, 0x39);
        }

        // 6: this = ((x + y)^2 - (x - y)^2) / 4 = x*y
        ASSERT_TAG((this.value[(short)(this.size - 1)] & 0x03) == 0, 0x3A);
        div_4();
        return;
    }
                                 


    /**
     * Bitmask for first digit.
     * Computes a bitmask to be used with {@code &} that masks all bits
     * obove the most significant 1 bit in the first digit of this bignat.
     * For instance, if the first digit is 10, then the mask
     * returned is {@code 0x1F}.
     * <P>
     * Needed to improve the efficiency of modular random number generation,
     * see {@link #rand_mod rand_mod}.
     * <P>
     * Asserts that digits before {@code first_digit_index} are zero.
     *
     * @param first_digit_index the index of the first digit
     * @return mask of type DIGIT_TYPE 
     *           for masking bits above the most significant 1 bit in
     *           digit first_digit_index
     */
    public DIGIT_TYPE get_first_digit_mask(short first_digit_index) {
        #ifndef NO_CARD_ASSERT
           for(short i = 0; i < first_digit_index; i++) {
               ASSERT(this.value[i] == 0);
           }
        #endif

        DIGIT_TYPE res = (DIGIT_TYPE)(
            (((DOUBLE_DIGIT_TYPE)1) << (double_digit_len - 
                     highest_bit((DOUBLE_DIGIT_TYPE)
                                 (this.value[first_digit_index] 
                                  & digit_mask))))
            - 1);
        // System.out.format("get_first_digit_mask digit %d " +
        //                   "res 0x" + digit_format + "\n" +
        //                   "for %s\n" +
        //                   "highest bit %d\n",
        //                   first_digit_index,
        //                   res,
        //                   this.to_hex_string(),
        //                   highest_bit((DOUBLE_DIGIT_TYPE)
        //                               (this.value[first_digit_index] & 
        //                                digit_mask))
        //                   );
        return res;
    }


    /**
     * Modular random number generator. Fills this bignat with a 
     * random number strictly lesser than {@code mod}. To be efficient
     * certain characteristics of the modulus {@code mod} must be passed
     * in as arguments. {@code first_mod_digit} is the index of the 
     * first non-zero digit of {@code mod} and {@code first_digit_mask} 
     * (of type DIGIT_TYPE) is a mask as computed by 
     * {@link #get_first_digit_mask get_first_digit_mask}. If the modulus is
     * constant these data can be computed once (therefore it is passed in 
     * as argument here). If this data is wrong, the random number might not 
     * be evenly distributed or this method might not return for a very long 
     * time.
     * <P>
     * Asserts that this bignat and {@code mod} have the same size.
     * <P>
     * Relies on {@link ds.ov2.util.Misc#rand_data Misc.rand_data},
     * for BIGNAT_USE_BYTE, and on 
     * {@link ds.ov2.util.Misc#rand_data_int Misc.rand_data_int}, for 
     * BIGNAT_USE_INT, respectively, for filling the digit array with 
     * random data.
     * <P>
     * The random number generator
     * has type {@link javacard.security.RandomData} 
     * on the card and {@link java.util.Random} on the host. Therefore 
     * the first argument changes its type, depending on the environment.
     * <P>
     * To generate a random number lesser than {@code mod} one cannot 
     * just take the remainder of the division by {@code mod}, because
     * this would result in random values that are not evenly distributed.
     * Therefore we just generate random data in a loop until we are
     * so lucky that one is below {@code mod}. To improve our chances 
     * we zero out all bits in the random data that are above the most 
     * significant 1 bit in {@code mod}. 
     *
     * @param rand of type RANDOM, random number generator
     * @param mod modulus
     * @param first_mod_digit index of the first non-zero digit in {@code mod}
     * @param first_digit_mask of type DIGIT_TYPE, mask for masking 
     *           bits above the most significant 1 bit in the first
     *           non-zero digit of {@code mod}, as returned by
     *           {@link #get_first_digit_mask get_first_digit_mask}.
     */
    public void rand_mod(RANDOM rand, Bignat mod, 
                         short first_mod_digit, 
                         DIGIT_TYPE first_digit_mask) 
    {
        // System.out.format("XXX rand_mod first_mod_digit %d " +
        //                "first_digit_mask 0x" + digit_format + "\n" +
        //                "mod = %s\n",
        //                first_mod_digit,
        //                first_digit_mask,
        //                mod.to_hex_string());

        ASSERT_TAG(this.size == mod.size, 0x3B);

        // Zero out the first digits.
        for(short i = 0; i < first_mod_digit; i++)
            this.value[i] = 0;

        do {
            #ifdef BIGNAT_USE_BYTE
                Misc.rand_data(rand, this.value, first_mod_digit, 
                               (short)(size - first_mod_digit));
            #else // BIGNAT_USE_INT
                Misc.rand_data_int(rand, this.value, first_mod_digit,
                                   size - first_mod_digit);
            #endif
                
            this.value[first_mod_digit] = 
                (DIGIT_TYPE)(this.value[first_mod_digit] & first_digit_mask);
            // System.out.format("XXX rand_mod generated %s\n",
            //                   this.to_hex_string());
        } while(!this.lesser(mod));
        return;
    }


    //########################################################################
    // APDU_Serializable interface
    // 

    /**
     * Compatibility check for the OV-chip protocol layer.
     * See <a href="../util/APDU_Serializable.html#apdu_compatibility">
     * the compatibility check 
     * explanations</a> and also
     * {@link ds.ov2.util.APDU_Serializable#is_compatible_with 
     * APDU_Serializable.is_compatible_with}.
     * <P>
     *
     * Bignat objects are compatible to Bignat's and {@link
     * APDU_BigInteger APDU_BigInteger's} of the same size.
     *
     * @param o actual argument or result
     * @return true if {@code o} is either a Bignat or an {@link
     * APDU_BigInteger} of the same size.
     */
    public boolean is_compatible_with(Object o) {
        if(o instanceof Bignat) {
            return this.size() == ((Bignat)o).size();
        }
        #ifndef JAVACARD_APPLET
            else if(o instanceof APDU_BigInteger) {
                return this.size() == ((APDU_BigInteger)o).max_size;
            }
        #endif
        return false;
    }


    /**
     * Serialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#to_byte_array 
     * APDU_Serializable.to_byte_array}.
     *
     * @param len available space in {@code byte_array}
     * @param this_index number of bytes that
     * have already been written in preceeding calls
     * @param byte_array data array to serialize the state into
     * @param byte_index index in {@code byte_array} 
     * @return the number of bytes actually written, except for the case 
     * where serialization finished by writing precisely 
     * {@code len} bytes, in this case {@code len + 1} is 
     * returned.
     */
    #ifdef BIGNAT_USE_BYTE
       public short to_byte_array(short len, short this_index, 
                                  byte[] byte_array, short byte_index) {
           ASSERT(this_index < this.size);
           short max = 
               (short)(this_index + len) <= this.size ? 
                           len : (short)(this.size - this_index);
           Misc.array_copy(value, this_index, byte_array, byte_index, max);
           if((short)(this_index + len) == this.size)
               return (short)(len + 1);
           else
               return max;
       }
    #else // BIGNAT_USE_INT
       public short to_byte_array(short len, short this_index, 
                                  byte[] byte_array, short byte_index) {
           ASSERT(this_index < this.size * 4);
           short count = 0;
           while(count < len && this_index + count < this.size * 4) {
               int mod_shift = (3 - (this_index + count) % 4) * 8;
               int div = (this_index + count) / 4;
               byte_array[byte_index] = (byte)
                   ((this.value[div] & (0xff << mod_shift)) >> mod_shift);
               byte_index++;
               count++;
           }
           if(count == len && (this_index + count) == this.size * 4)
               return (short)(count + 1);
           else
               return count;
       }
    #endif


    /**
     * Deserialization of this object for the OV-chip protocol layer. See {@link 
     * ds.ov2.util.APDU_Serializable#from_byte_array 
     * APDU_Serializable.from_byte_array}.
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
    #ifdef BIGNAT_USE_BYTE
       public short from_byte_array(short len, short this_index,
                                    byte[] byte_array, short byte_index) {
           ASSERT(this_index < this.size);
           short max = 
               (short)(this_index + len) <= this.size ? 
                         len : (short)(this.size - this_index);
           Misc.array_copy(byte_array, byte_index, value, this_index, max);
           if((short)(this_index + len) == this.size)
               return (short)(len + 1);
           else
               return max;
       }
    #else // BIGNAT_USE_INT
       public short from_byte_array(short len, short this_index,
                                    byte[] byte_array, short byte_index) {
           ASSERT(this_index < this.size * 4);
           if(this_index == 0)
               this.zero();
           short count = 0;
           while(count < len && this_index + count < this.size * 4) {
               int mod_shift = (3 - (this_index + count) % 4) * 8;
               int div = (this_index + count) / 4;
               this.value[div] |= (byte_array[byte_index] & 0xff) << mod_shift;
               byte_index++;
               count++;
           }
           if(count == len && (this_index + count) == this.size * 4)
               return (short)(count + 1);
           else
               return count;
       }
    #endif
    


    /**
     * Convert into a hex number as string with dots every 4 digits.
     * <P>
     * Only available if HOST_TESTFRAME is defined.
     *
     * @return hexadicimal number as string
     */
    #ifdef HOST_TESTFRAME
        public String to_hex_string() {
            String s = "";

            for(int i = 0; i < this.size; i++) {
                if(i > 0 && (size_multiplier > 1 || i % 4 == 0))
                    s = s + ".";
                s = s + String.format(digit_format, value[i] & digit_mask);
            }
            return s;
        }

    #endif
}
