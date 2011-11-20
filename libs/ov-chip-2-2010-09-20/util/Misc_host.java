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
// misc utilities that are needed only on the host
// 
// $Id: Misc_host.java,v 1.18 2009-05-19 14:59:53 tews Exp $


package ds.ov2.util;

import java.math.BigInteger;
import java.io.PrintWriter;


/** 
 * Miscellaneous methods needed only in host driver code. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.18 $
 * @commitdate $Date: 2009-05-19 14:59:53 $ by $Author: tews $
 * @environment host
 * @CPP This class needs no cpp preprocessing.
 */
public class Misc_host {


    /**
     * 
     * Static class, object creation disabled.
     */
    protected Misc_host() {}


    /**
     * 
     * Convert a long into a big-endian byte array. 
     * 
     * @param l long to convert
     * @return byte array of length 8 containing the bytes of {@code
     * l} in big-endian format
     */
    public static byte[] byte_array_from_long(long l) {
        byte[] buf = new byte[8];

        buf[7] = (byte)(l & 0xff); l >>= 8;
        buf[6] = (byte)(l & 0xff); l >>= 8;
        buf[5] = (byte)(l & 0xff); l >>= 8;
        buf[4] = (byte)(l & 0xff); l >>= 8;
        buf[3] = (byte)(l & 0xff); l >>= 8;
        buf[2] = (byte)(l & 0xff); l >>= 8;
        buf[1] = (byte)(l & 0xff); l >>= 8;
        buf[0] = (byte)(l & 0xff);
        return buf;
    }


    /**
     * 
     * Convert a big-endian byte array into a long value. 
     * 
     * @param buf big-endian byte array
     * @return equivalent long value
     * @throws IlleagalArgumentException if the length of buf is
     * different from 8
     */
    public static long long_from_byte_array(byte[] buf) {
        if(buf.length != 8)
            throw new IllegalArgumentException
                ("buf must contain precisely 8 bytes");

        return
            (((((((buf[0] & 0xffL) * 256 +
                  (buf[1] & 0xffL)) * 256 +
                 (buf[2] & 0xffL)) * 256 +
                (buf[3] & 0xffL)) * 256 +
               (buf[4] & 0xffL)) * 256 +
              (buf[5] & 0xffL)) * 256 +
             (buf[6] & 0xffL)) * 256 +
            (buf[7] & 0xffL);
    }



    /**
     * 
     * Converts a byte array into a hex string. The string looks like
     * 23EA.4F11...
     * 
     * @param data array to convert
     * @return hex string
     */
    public static String to_byte_hex_string(byte[] data) {
        int i = data.length;

        if(i == 0)
            return "";
        if(i == 1)
            return String.format("%02X", data[0]);
        else {
            String res = String.format("%02X%02X", data[i-2], data[i-1]);
            i -= 2;

            while(i > 0) {
                if(i == 1)
                    res = String.format("%02X.", data[0]) + res;
                else
                    res = String.format("%02X%02X.", data[i-2], data[i-1]) + 
                        res;
                i -= 2;
            }
            return res;
        }
    }


    /**
     * 
     * Sleep for {@code m} milliseconds. Does not throw {@link
     * InterruptedException} in contrast to {@link Thread#sleep
     * Thread.sleep}. 
     * 
     * @param m milliseconds to sleep
     */
    public static void sleep(int m) {
        try {
            Thread.sleep(m);
        }
        catch(InterruptedException e) {
        }
    }


    /**
     * 
     * Collect garbage and sleep. Return immediately (without GC) if
     * the argument {@code m} is 0.
     * 
     * @param m milliseconds to sleep
     */
    public static void gc_sleep(int m) {
        if(m == 0)
            return;
        System.gc();
        sleep(m);
    }
}
