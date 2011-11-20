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
// $Id: Test.java,v 1.3 2009-02-20 15:29:27 tews Exp $

// specific package name
package test_rsa;

// generic imports
import javacard.framework.Applet;
import javacard.framework.APDU;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;

// specific imports
import javacard.security.RSAPublicKey;
import javacard.security.KeyBuilder;
import javacard.security.CryptoException;
import javacardx.crypto.Cipher;


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


    // key size in bits, must be divisible by 8
    // The cards that I tested support all key sizes between 512 and 1952 bits
    // with 32 bit steps. That is, the next possible size is 544.
    private static final short key_size = 512; 
    private static final short length = key_size / 8;

    // initialized in init
    private static byte[] base;
    private static byte[] modulus;
    private static byte[] exponent;
    private static byte[] result;

    private static Cipher cipher;
    private static RSAPublicKey key;

    
    private static void init() {
        base = new byte[length];
        modulus = new byte[length];
        exponent = new byte[length];
        result = new byte[length];

        // zero initialize, maybe not necessary, but anyway
        for(short i = 0; i < length; i++) {
            base[i] = 0;
            modulus[i] = 0;
            exponent[i] = 0;
            result[i] = 0;
        }

        // base can be an arbitrary number
        // format is big-endian, most significant byte/digit at index 0
        base[length -1] = 5;

        // exponent can be arbitrary
        exponent[length -1] = 5;

        // in the modulus the first byte must be non-zero
        modulus[0] = 1;
    }


    public static void process_apdu(APDU apdu, byte[] buf) {
        
        // get cipher
        try {
            cipher = Cipher.getInstance(Cipher.ALG_RSA_NOPAD, false);
        }
        catch(CryptoException e) {
            ISOException.throwIt((short)((short)0x9F00 | e.getReason()));
        }
        

        // build a RSA public key
        try {
            key = (RSAPublicKey)
                (KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PUBLIC,
                                     key_size, false));
        }
        catch (CryptoException e) {
            ISOException.throwIt((short)((short)0x9E00 | e.getReason()));
        }


        // initialize base, modulus and exponent, if not done yet
        if(base == null) 
            init();


        // set modulus and exponent
        try {
            key.setModulus(modulus, (short)0, length);
            key.setExponent(exponent, (short)0, length);
        }
        catch(CryptoException e) {
            ISOException.throwIt((short)((short)0x9D00 | e.getReason()));
        }


        // initialize the cipher with the key
        cipher.init(key, Cipher.MODE_ENCRYPT);
        cipher.doFinal(base, (short)0, length, result, (short)0);


        // send back the result
        apdu.setOutgoing();
        apdu.setOutgoingLength((short)result.length);
        apdu.sendBytesLong(result, (short)0, (short)result.length);
    }
}
