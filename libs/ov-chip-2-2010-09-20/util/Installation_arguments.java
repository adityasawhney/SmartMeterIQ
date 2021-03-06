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
// Created 15.9.08 by Hendrik
// 
// Decoding installation arguments
// 
// $Id: Installation_arguments.java,v 1.9 2009-04-09 10:42:16 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


/** 
 * Decode Java Card applet installation arguments into an {@link
 * APDU_Serializable} array. Relies on parts of the OV-chip protocol
 * layer. Therefore the installation arguments must be encoded as for
 * the OV-chip protocol: A sequence of plain data bytes without
 * intermediate length information. Two short arguments must for
 * instance be encoded with 4 bytes.
 * <P>
 *
 * The byte array which the applet install method receives contains
 * the applet ID, some control info and the actual installation
 * arguments (called applet data in the SUN docs) in a length-value
 * encoding (see {@link javacard.framework.Applet#install
 * Applet.install} for the details). This class can parse the complete
 * data to find the actual installation arguments. Decoded are however
 * only the installation arguments and not the other stuff. <P>
 *
 * When specifying the installation arguments in some applet
 * installation programm one typically has to prepend a size byte to
 * the encoded installation arguments. Sometimes (for instance for the
 * global platform manager) an additional leading 0xC9 must be
 * inserted. (This 0xC9 is a tag that tells the global platform that
 * the bytes that follow are applet installation arguments, if I
 * remember correctly.) In the example of the two shorts one would
 * have to specify 0xC9 0x04 plus 4 data bytes.
 * <P>
 *
 * To encode the installation arguments in the host driver one can use
 * {@link Convert_serializable#array_to_bytes
 * Convert_serializable.array_to_bytes} as in the following code
 * fragment 
 * <pre>
 *    APDU_Serializable[] arguments = ...
 *    int arg_length = Misc.length_of_serializable_array(arguments);
 *    assert arg_length < 127;
 *    byte[] serialized_args = new byte[arg_length + 2];
 *    serialized_args[0] = (byte)0xC9;
 *    serialized_args[1] = (byte)arg_length;
 *    int res = Convert_serializable.array_to_bytes(arguments, serialized_args, 2); 
 *    assert res == serialized_args.length;
 * </pre>
 * The {@code arguments} array in the host driver most be compatible
 * (in the sense of {@link Convert_serializable#check_compatibility
 * Convert_serializable.check_compatibility}) to the ones that are
 * used in the applet and passed into {@link #decode decode}.
 * <P>
 *
 * The specification of the data format of the installation arguments
 * comes from SUN as well as the emulators cref and jcwde. But it
 * would be expecting far too much that SUN tools follow SUN
 * specifications. An applet running in cref or jcwde only sees the
 * actual installation arguments in the byte array that the install
 * method gets. No applet ID, no control info, not even a length byte.
 * When <a
 * href="../../../overview-summary.html#CREF_INSTALL_ARG_HACK">CREF_INSTALL_ARG_HACK<a>
 * is defined the code here works around this bug. If it is undefined
 * one will get strange errors inside cref and jcwde. <P>
 *
 * This is a static class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.9 $
 * @commitdate $Date: 2009-04-09 10:42:16 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>,
 *   <a href="../../../overview-summary.html#ASSERT">ASSERT</a>,
 *   <a href="../../../overview-summary.html#CREF_INSTALL_ARG_HACK">CREF_INSTALL_ARG_HACK<a>
 */
PUBLIC class Installation_arguments {

  /**
   * 
   * Static class, object creation disabled.
   */
  protected Installation_arguments() {}


    /**
     * 
     * Decode the actual applet installation arguments. The first
     * three arguments are the unmodified arguments from the {@link
     * javacard.framework.Applet#install install} method. The last
     * argument are the declared installation arguments. This method
     * checks (with <a
     * href="../../../overview-summary.html#ASSERT">ASSERT<a>) that
     * the size of the installation arguments fits with the incoming
     * data. <P>
     *
     * When <a
     * href="../../../overview-summary.html#CREF_INSTALL_ARG_HACK">CREF_INSTALL_ARG_HACK<a>
     * is defined this method tries to work around the bugs in the
     * cref and jcwde emulators.
     * 
     * @param bytes the array containing the raw data
     * @param start the starting offset in {@code bytes}
     * @param len the length of the raw data
     * @param arguments the declared arguments into which the raw data
     * is decoded
     */
  public static void decode(byte[] bytes, short start, byte len, 
                            APDU_Serializable[] arguments) 
  {
    // The encoding of the installation data is as follows:
    // [size_0] [size_0 data bytes] [size_1] [size_1 data bytes] ...
    // The first argument is the applet id. 
    // The second argument is the card provided control info.
    // The third argument contains the real installation parameters.
    // See also the Applet.install docs.

    short arg_len;

    #ifdef CREF_INSTALL_ARG_HACK
    if(len == Misc.length_of_serializable_array(arguments)) {
      // cref compatibility hack: Got a length that fits
      // our expectations, use it and hope the best!
    }
    else {
    #endif

      // Skip over applet ID.
      arg_len = (short)(bytes[start] & 0xff);
      arg_len += 1;             // take size byte into account
      start += arg_len;         // skipp over AID
      len -= arg_len;

      // Skip over control info.
      arg_len = (short)(bytes[start] & 0xff);
      arg_len += 1;             // take size byte into account
      start += arg_len;         // skipp over control
      len -= arg_len;

      // Found the actual installation arguments.
      arg_len = (short)(bytes[start] & 0xff);
      start += 1;
      len -= 1;
      ASSERT(len == arg_len && 
             len == Misc.length_of_serializable_array(arguments));

    #ifdef CREF_INSTALL_ARG_HACK
    }
    #endif

    for(short i = 0; i < arguments.length; i++) {
      arg_len = arguments[i].from_byte_array(len, (short)0,
                                             bytes, start);

      ASSERT(arg_len < len || (arg_len == (short)(len + 1) &&
                               (short)(i + 1) == arguments.length));
      start += arg_len;
      len -= arg_len;
    }
  }
}



/// Local Variables:
/// c-basic-offset: 2
/// End:
