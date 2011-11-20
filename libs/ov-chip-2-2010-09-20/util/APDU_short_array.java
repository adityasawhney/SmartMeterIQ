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
// Created 2.10.08 by Hendrik
// 
// short array with APDU_Serializable interface
// 
// $Id: APDU_short_array.java,v 1.10 2009-03-26 15:51:31 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


/** 
 * {@link APDU_Serializable} wrapper around a short array. Provides
 * {@link #get get} and {@link #set set} methods for accessing the
 * array. Relies on {@link Serializable_array} for the {@link
 * APDU_Serializable} functionality. This is not terribly effient, but
 * good enough as long as this is only used for setting the sizes of
 * {@link Resizable_buffer Resizable_buffer's} in the tests of the
 * OV-chip protocol layer.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.10 $
 * @commitdate $Date: 2009-03-26 15:51:31 $ by $Author: tews $
 * @environment host, card
 * @todo Could be made more efficient by using a short array
 * internally instead of an {@link APDU_short} array. The latter is of
 * course much more convenient in the to/from_byte methods.
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
 */
PUBLIC class APDU_short_array 
    extends Serializable_array
    implements APDU_Serializable 
{
    /**
     * 
     * The internal array with the data.
     */
    private APDU_short[] a;


    /**
     * 
     * Noninitializing constructor. 
     * 
     * @param len length of the short array to create
     */
    public APDU_short_array(short len) {
        a = new APDU_short[len];
        for(short i = 0; i < len; i++)
            a[i] = new APDU_short();
    }

    #ifndef JAVACARD_APPLET
      /**
       * 
       * Convenience initializing constructor. Create a short array
       * with the same data as {@code int_array}. Asserts that all
       * elements of {@code int_array} fit into a short.
       * <P>
       *
       * Only available if <a
       * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
       * is undefined.
       * 
       * @param int_array provides length and data for the new short array.
       */
      public APDU_short_array(int[] int_array) {
          a = new APDU_short[int_array.length];
          for(short i = 0; i < int_array.length; i++)
              a[i] = new APDU_short(int_array[i]);
      }


      /**
       * 
       * Initializing constructor for the IDL compiler. The {@code
       * len} argument is redundant here, but the IDL compiler is not
       * smart enough to leave it out. This constructor is the same as
       * the {@link #APDU_short_array(int[]) convenience initializing
       * constructor}. It only additionally asserts that {@code len}
       * equals the {@code int_array} length.
       * 
       * @param len length of the new array
       * @param int_array data for the new array
       */
      public APDU_short_array(short len, int[] int_array) {
          // It would be more logically to assert first, but the 
          // constructor must be the first statement...
          this(int_array);
          assert len == int_array.length;
      }


      /**
       * 
       * Return the contents as int array. Convenient in the host
       * driver. 
       * <P>
       *
       * Only available if <a
       * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
       * is undefined.
       * 
       * @return contents of this array as int array of the same size
       */
      public int[] get_int_array() {
          int[] r = new int[a.length];
          for(int i = 0; i < a.length; i++)
              r[i] = a[i].value;
          return r;
      }


      /**
       * 
       * Reinitialize this array from an int array. Asserts that the
       * length of {@code int_array} equals the length of this array
       * and that all elements of {@code int_array} fit into a short.
       * <P>
       *
       * Only available if <a
       * href="../../../overview-summary.html#JAVACARD_APPLET">JAVACARD_APPLET<a>
       * is undefined.
       * 
       * @param int_array new data for this array
       */
      public void copy(int[] int_array) {
          assert a.length == int_array.length;
          for(short i = 0; i < int_array.length; i++)
              a[i].copy(int_array[i]);
      }
    #endif


    /**
     * 
     * Array read access. 
     * 
     * @param i index
     * @return ith element of this array
     * @throws ArrayIndexOutOfBoundsException if
     * the index {@code i} is invalid
     */
    public short get(short i) {
        return a[i].value;
    }


    /**
     * 
     * Array write access. Store a value at an index.
     * 
     * @param i index
     * @param v new value.
     * @throws ArrayIndexOutOfBoundsException if the index {@code i}
     * is invalid
     */
    public void set(short i, short v) {
        a[i].value = v;
        return;
    }


    //########################################################################
    // Serializable_array support
    // 

    /**
     * Return the short array {@link #a} in support for abstract
     * {@link Serializable_array}.
     *
     * @return array of objects to (de-)serialize
     */
    protected APDU_Serializable[] get_array() {
        return a;
    }


    /**
     * Size in bytes of this array. Necessary for the OV-chip protocol
     * layer, see {@link ds.ov2.util.APDU_Serializable#size
     * APDU_Serializable.size()}. Overriden here with a more efficient
     * method to compute the size.
     *
     * @return size in bytes
     */
    public short size() {
        return (short)(a.length * 2);
    }


    /**
     * Compatibility check for the OV-chip protocol layer.
     * See <a href="APDU_Serializable.html#apdu_compatibility">
     * the compatibility check 
     * explanations</a> and also
     * {@link ds.ov2.util.APDU_Serializable#is_compatible_with 
     * APDU_Serializable.is_compatible_with}.
     * <P>
     *
     * This object is compatible with instances of this class with the
     * same array length.
     *
     * @param o actual argument or result
     * @return true if {@code o} is an instance of APDU_short_array
     * with the same length
     */
    public boolean is_compatible_with(Object o) {
        if(o instanceof APDU_short_array) {
            return ((APDU_short_array)o).a.length == this.a.length;
        }
        else
            return false;
    }
}
