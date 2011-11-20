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
// $Id: Test.java,v 1.1 2009-04-22 14:36:07 tews Exp $

// specific package name
package test_mem;

// generic imports
import javacard.framework.Applet;
import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

// specific imports
import javacard.framework.JCSystem;


//########################################################################
// 
// generic part
// 
//########################################################################

// public for jcwde
public class Test extends Applet 
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

    private short store_short(byte[] buf, short index, short val) {
        buf[index] = (byte)((val >> 8) & 0xff);
        buf[(short)(index + 1)] = (byte)(val & 0xff);
        return 2;
    }

    private short store_boolean(byte[] buf, short index, boolean val) {
        if(val)
            buf[index] = (byte)1;
        else
            buf[index] = (byte)0;
        return 1;
    }

    private void process_apdu(APDU apdu, byte[] buf) {
        
        short mem_persistent = 
            JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_PERSISTENT);

        short mem_transient_reset = 
            JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_TRANSIENT_RESET);

        short mem_transient_deselect = 
            JCSystem.getAvailableMemory(JCSystem.MEMORY_TYPE_TRANSIENT_DESELECT);

        short version = JCSystem.getVersion();

        boolean gc = JCSystem.isObjectDeletionSupported();

        short index = 0;
        index += store_short(buf, index, (short)0x95A0);
        index += store_short(buf, index, mem_persistent);
        index += store_short(buf, index, mem_transient_reset);
        index += store_short(buf, index, mem_transient_deselect);
        index += store_short(buf, index, version);
        index += store_short(buf, index, (short)buf.length);
        index += store_boolean(buf, index, gc);
        buf[index] = APDU.getProtocol();
        index += 1;     

        // send back the result
        apdu.setOutgoing();
        apdu.setOutgoingLength(index);
        apdu.sendBytesLong(buf, (short)0, index);
    }
}
