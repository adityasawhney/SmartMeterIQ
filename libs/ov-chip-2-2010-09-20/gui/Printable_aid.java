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
// Created 1.2.09 by Hendrik
// 
// change the toString method of cardservices.AID
// 
// $Id: Printable_aid.java,v 1.2 2009-05-11 21:44:30 tews Exp $

package ds.ov2.gui;

import java.nio.charset.Charset;

import cardservices.AID;


/**
 * Equip cardservices.AID with a toString method for the applet list
 * dialog. If the applet ID consists only of printable characters, as
 * it is the case for all applets in this repository, the result of
 * {@link #toString} is just the readable applet name. If there is one
 * non-printable charakter, {@link #toString} will return the applet
 * ID as byte array in hex.
 * <P>
 *
 * The result of this toString method is only computed once in the
 * constructor.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.2 $
 * @commitdate $Date: 2009-05-11 21:44:30 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */

public class Printable_aid {

    /**
     * 
     * US-ASCII character set for converting strings into byte arrays.
     */
    public final Charset char_set_us_ascii = Charset.forName("US-ASCII");


    /**
     * 
     * The applet identifier this instance wraps.
     */
    public final AID aid;


    /**
     * 
     * The string to be returned by {@link #toString}.
     */
    public final String printable_name;


    /**
     * 
     * Constructor. Computes the prinable representation in {@link
     * #printable_name}. 
     * 
     * @param aid the applet ID to wrap
     */
    public Printable_aid(AID aid) {
        this.aid = aid;

        byte[] bytes = aid.getBytes();

        boolean printable = true;
        for(int i = 0; i < bytes.length; i++) {
            if(!(bytes[i] >= 0x20 && bytes[i] <= 0x7F)) {
                printable = false;
                break;
            }
        }

        if(printable) {
            printable_name = new String(bytes, char_set_us_ascii);
            return;
        }

        // Not printable. Convert into hex.
        String result = String.format("%02X", (bytes[0] & 0xff));
        for(int i = 1; i < bytes.length; i++)
            result = result + String.format(" %02X", (bytes[i] & 0xff));
        printable_name = result;
    }


    /**
     * 
     * Return a more readable representation of the applet ID.
     * 
     */
    public String toString() {
        return printable_name;
    }
}