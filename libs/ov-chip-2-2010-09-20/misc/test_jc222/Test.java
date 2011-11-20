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
// test applet for testing various things
// 
// $Id: Test.java,v 1.4 2009-02-20 15:29:27 tews Exp $

// specific package name
package test_jc222;

// generic imports
import javacard.framework.Applet;
import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

// specific imports
//import javacardx.framework.math.BigNumber;


//########################################################################
// 
// generic part
// 
//########################################################################

// public for jcwde
public class Test extends Applet 
                          // implements javacardx.apdu.ExtendedLength
{

    // save init parameters for later debugging
    // private byte[] init_array;
    // private short init_start;
    // private short init_len;

    public static void install(byte[] bytes, short start, byte len) {
        new Test(bytes, start, len);
        return;
    }


    Test(byte[] bytes, short start, byte len) {
        
        // save init parameters for later debugging
        // init_array = new byte[bytes.length];
        // Misc.array_copy(bytes, (short)0, init_array, (short)0, 
        //              (short)bytes.length);
        // init_start = start;
        // init_len = len;

        // overwrite init parameters for debugging
        // bignat_size.value = (short)10;
        // max_vector_length.value = (short)5;

        register();
        return;
    }


    public boolean select() {
        return true;
    }


    public void process(APDU apdu) {
        // Return 9000 on SELECT
        if (selectingApplet()) {
            return;
        }

        // init parameters debugging
        // ASSERT_TAG(false, init_array[19]);

        byte[] buf = apdu.getBuffer();

        if(buf[ISO7816.OFFSET_CLA] != 0)
            ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);

        process_apdu(apdu, buf);
    }


//########################################################################
// 
// specific part
// 
//########################################################################

    public static void process_apdu(APDU apdu, byte[] buf) {
        
        //short len = 5;
        short len = javacardx.framework.math.BigNumber.getMaxBytesSupported();
        buf[0] = (byte)((len >> 8) & 0xff);
        buf[1] = (byte)(len & 0xff);
        apdu.setOutgoingAndSend((short)0, (short)2);
    }
}
