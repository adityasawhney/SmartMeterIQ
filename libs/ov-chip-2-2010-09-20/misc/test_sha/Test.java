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
// $Id: Test.java,v 1.3 2009-02-20 15:29:28 tews Exp $

// specific package name
package test_sha;

// generic imports
import javacard.framework.Applet;
import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

// specific imports
import javacard.security.MessageDigest;
import javacard.security.CryptoException;
import javacard.framework.CardRuntimeException;
import java.lang.ArrayIndexOutOfBoundsException;

//########################################################################
// 
// generic part
// 
//########################################################################

// public for jcwde
public class Test extends Applet {

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

    static MessageDigest digest;

    static byte[] input;
    static byte[] output;

    public static void process_apdu(APDU apdu, byte[] buf) {
        
        // get sha digest
        try {
            digest = MessageDigest.getInstance(MessageDigest.ALG_SHA, false);
        }
        catch(CryptoException e) {
            ISOException.throwIt((short)((short)0x9F00 | e.getReason()));
        }
        
        if(input == null) {
            input = new byte[]{
                (byte)(0x00), (byte)(0x01), (byte)(0x02), (byte)(0x03),
                (byte)(0x04), (byte)(0x05), (byte)(0x06), (byte)(0x07),
                (byte)(0x08), (byte)(0x09), (byte)(0x10), (byte)(0x11),
                (byte)(0x12), (byte)(0x13), (byte)(0x14), (byte)(0x15),
                (byte)(0x16), (byte)(0x17), (byte)(0x18), (byte)(0x19), 
                (byte)(0x20), (byte)(0x21), (byte)(0x22), (byte)(0x23),
                (byte)(0x24), (byte)(0x25), (byte)(0x26), (byte)(0x27),
                (byte)(0x28), (byte)(0x29), (byte)(0x30), (byte)(0x31),
                (byte)(0x32), (byte)(0x33), (byte)(0x34), (byte)(0x35),
                (byte)(0x36), (byte)(0x37), (byte)(0x38), (byte)(0x39), 
                (byte)(0x50), (byte)(0x51), (byte)(0x52), (byte)(0x53),
                (byte)(0x54), (byte)(0x55), (byte)(0x56), (byte)(0x57),
                (byte)(0x58), (byte)(0x59), (byte)(0x60), (byte)(0x61),
                (byte)(0x62), (byte)(0x63), (byte)(0x64), (byte)(0x65),
                (byte)(0x66), (byte)(0x67), (byte)(0x68), (byte)(0x69)
            };
            output = new byte[22];
        }

        short res = 0;

        try {
            res = digest.doFinal(input, (short)0, (short)input.length, 
                                 output, (short)2);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            ISOException.throwIt((short)0x9E00);
            // ISOException.throwIt((short)((short)0x9E00 | e.getReason()));
        }

        output[0] = (byte)(res >> 8);
        output[1] = (byte)res;

        apdu.setOutgoing();
        apdu.setOutgoingLength((short)output.length);
        apdu.sendBytesLong(output, (short)0, (short)output.length);
    }
}
