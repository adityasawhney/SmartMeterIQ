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
// $Id: Test_applet.java,v 1.24 2009-05-20 12:04:09 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.test;
#endif


#ifdef JAVADOC
  import ds.ov2.util.*;
  import ds.ov2.bignat.*;
#endif

/** 
 * Main test applet class. Root class/object of the test applet.
 * Extends {@link Protocol_applet} with applet specific code. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.24 $
 * @commitdate $Date: 2009-05-20 12:04:09 $ by $Author: tews $
 * @environment card
 * @CPP This class uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC</a>
 *
 */
PUBLIC class Test_applet extends Protocol_applet {

    // save init parameters for later debugging
    // private byte[] init_array;
    // private short init_start;
    // private short init_len;


    /**
     * 
     * Applet install method. Expects three installation arguments:
     * the size of short (exponent) bignats, the size of long (base)
     * bignats and the (base) vector length. See also {@link
     * Test_host#make_installation_arguments}. 
     * 
     * @param bytes array with installation arguments
     * @param start start offset of the installation arguments
     * @param len of installation arguments
     */
    public static void install(byte[] bytes, short start, byte len) {
        new Test_applet(bytes, start, len);
        return;
    }


    /**
     * 
     * APDU container for the first installation argument. 
     * Only used during installation.
     */
    private APDU_short short_bignat_size;

    /**
     * 
     * APDU container for the second installation argument.
     * Only used during installation.
     */
    private APDU_short long_bignat_size;

    /**
     * 
     * APDU container for the third installation argument. Only used
     * during installation.
     */
    private APDU_short double_bignat_size;

    /**
     * 
     * APDU container for the fourth installation argument.
     * Only used during installation.
     */
    private APDU_short max_vector_length;

    /**
     * 
     * APDU container for the fifth installation argument, the
     * cap-file creation time as long as returned by {@link
     * java.io.File#lastModified}. Used in the status protocol.
     */
    private APDU_byte_array cap_creation_time;

    /**
     * 
     * Array for all installation APDU containers.
     * Only used during installation.
     */
    private APDU_Serializable[] install_arguments;


    /**
     * 
     * Allocate/initialize everything for the test applet. The
     * arguments are the same as for {@link #install install}. They
     * should contain four installation arguments: the size of short
     * (exponent) bignats, the size of long (base) bignats, the size
     * of double-sized bignates and the (base) vector length. See also
     * {@link Test_host#make_installation_arguments}.
     * 
     * @param bytes array with installation arguments
     * @param start start offset of the installation arguments
     * @param len of installation arguments
     */
    Test_applet(byte[] bytes, short start, byte len) {
        
        short_bignat_size = new APDU_short();
        long_bignat_size = new APDU_short();
        double_bignat_size = new APDU_short();
        max_vector_length = new APDU_short();
        cap_creation_time = new APDU_byte_array((short)8);
        install_arguments = new APDU_Serializable[]{
            short_bignat_size,
            long_bignat_size,
            double_bignat_size,
            max_vector_length,
            cap_creation_time
        };
        Installation_arguments.decode(bytes, start, len,
                                      install_arguments);

        // save init parameters for later debugging
        // init_array = new byte[bytes.length];
        // Misc.array_copy(bytes, (short)0, init_array, (short)0, 
        //              (short)bytes.length);
        // init_start = start;
        // init_len = len;

        // overwrite init parameters for debugging
        // short_bignat_size.value = (short)10;
        // long_bignat_size.value = (short)20;
        // max_vector_length.value = (short)5;
        

        Resize.init();

        Test_protocols test_protocols = 
            new Test_protocols(short_bignat_size,
                               long_bignat_size,
                               double_bignat_size,
                               max_vector_length,
                               cap_creation_time);
        set_registered_protocols(test_protocols.registered_protocols);

        register();
        return;
    }
}

