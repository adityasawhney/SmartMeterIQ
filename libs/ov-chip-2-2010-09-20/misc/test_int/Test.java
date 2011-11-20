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
// Created 16.03.09 by Hendrik
// 
// test 32 bit integers
// 
// $Id: Test.java,v 1.4 2009-03-20 15:48:46 tews Exp $

// specific package name
package test_int;

// generic imports
import javacard.framework.Applet;
import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;


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
        
        byte ins = buf[ISO7816.OFFSET_INS];
        short rounds = (short)(
               buf[ISO7816.OFFSET_P1] * 256 + buf[ISO7816.OFFSET_P2]);

        // short to_read = (short)(buf[ISO7816.OFFSET_LC] & 0xff);

        short read_count = apdu.setIncomingAndReceive();

        if(read_count != 1)
            ISOException.throwIt((short)(
                 ISO7816.SW_BYTES_REMAINING_00 |
                 (short)(read_count - 1) & 0xff));

        boolean do_operation = buf[ISO7816.OFFSET_CDATA] == 0x01;

        if(ins == 0) {
            short a = 31000;
            short b = 27000;
            short c = 0;

            for(short i = 0; i < rounds; i++) {
                if(do_operation)
                    c = (short)(a * b);
                else
                    c = 20000;
            }

            buf[0] = (byte)((c >>> 24) & 0xff);
            buf[1] = (byte)((c >>> 16) & 0xff);
            buf[2] = (byte)((c >>> 8) & 0xff);
            buf[3] = (byte)(c & 0xff);
        }
        else if(ins == 1) {

            #ifdef WITH_INT_SUPPORT
                int a = 31000;
                int b = 27000;
                int c = 0;

                for(short i = 0; i < rounds; i++) {
                    if(do_operation)
                        c = a * b;
                    else
                        c = 20000;
                }
            #else
                short c = -1;
            #endif

            buf[0] = (byte)((c >>> 24) & 0xff);
            buf[1] = (byte)((c >>> 16) & 0xff);
            buf[2] = (byte)((c >>> 8) & 0xff);
            buf[3] = (byte)(c & 0xff);
        }
        else
            ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);

        apdu.setOutgoingAndSend((short)0, (short)4);
    }
}
