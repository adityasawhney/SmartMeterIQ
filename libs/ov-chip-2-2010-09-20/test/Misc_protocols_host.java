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
// Created 4.9.08 by Hendrik
// 
// Host support for misc protocols
// 
// $Id: Misc_protocols_host.java,v 1.4 2009-06-02 09:56:02 tews Exp $

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;

import ds.ov2.util.Misc_host;
import ds.ov2.util.Response_apdu;
import ds.ov2.bignat.Resize;
import ds.ov2.test.Misc_protocols_stubs.Mem_size_result;
import ds.ov2.test.Misc_protocols_stubs.Status_result;


/** 
 * This class contains the host driver code for the misc protocols,
 * which are ping, set_size, mem_size and status.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.4 $
 * @commitdate $Date: 2009-06-02 09:56:02 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
class Misc_protocols_host {

    /**
     * 
     * The protocol array instance of the host to set the result sizes
     * after the resize protocol.
     */
    private Test_protocols test_protocols;


    /**
     * 
     * Card communication stubs.
     */
    private Misc_protocols_stubs stubs;


    /**
     * 
     * The channel to the card.
     */
    private CardChannel card_channel;


    /**
     * 
     * Channel for debug and progress messages.
     */
    private PrintWriter out;


    /**
     * 
     * Constructor. Initializes all fields.
     * 
     * @param test_protocols protocol array instance
     * @param card_channel channel to the applet
     * @param out print writer for debug and progress messages
     */
    public Misc_protocols_host(Test_protocols test_protocols,
                               CardChannel card_channel, PrintWriter out)
    {
        this.test_protocols = test_protocols;
        this.card_channel = card_channel;
        this.out = out;
        PrintWriter stub_out = State.verbosity.ref >= 10 ? out : null;
        stubs = new Misc_protocols_stubs(test_protocols.misc_protocols, 
                                         stub_out, 
                                         State.apduscript.ref);
    }


    /**
     * 
     * General resize method. See {@link Resize}. Resizes all
     * registered items in the applet and in the host driver to the
     * given new sizes. Reinitializes the protocol array as needed
     * afterwards.
     * 
     * @param short_bignat_size new size for registered short bignats
     * @param long_bignat_size new size for registered long bignats
     * @param double_bignat_size new size for registered double-sized bignats
     * @param cipher_size new size for registered {@link
     * ds.ov2.bignat.RSA_exponent RSA_exponent's}, if {@code
     * cipher_size == 0} the underlying RSA ciphers will not be
     * resized
     * @throws CardException on communication errors
     */
    public void set_size(int short_bignat_size, int long_bignat_size,
                         int double_bignat_size, int cipher_size) 
        throws CardException
    {
        int sleep = 900;

        Misc_host.gc_sleep(sleep); 
        stubs.set_size_call(card_channel, 
                            short_bignat_size, long_bignat_size, 
                            double_bignat_size, cipher_size);
        Resize.resize_bignats((short)short_bignat_size, 
                              (short)long_bignat_size,
                              (short)double_bignat_size,
                              (short)cipher_size);
        test_protocols.set_result_sizes();
        return;
    }


    /**
     * 
     * Special resize method for long bignats and {@link
     * ds.ov2.bignat.RSA_exponent RSA_exponent's}. Short and
     * double-sized bignats will be resized to some unspecified valid
     * value.
     * 
     * @param long_bignat_size new size for registered long bignats
     * @param cipher_size new size for registered {@link
     * ds.ov2.bignat.RSA_exponent RSA_exponent's}, if {@code
     * cipher_size == 0} the underlying RSA ciphers will not be
     * resized
     * @throws CardException on communication errors
     */
    public void set_long_cipher_size(int long_bignat_size, int cipher_size)
        throws CardException
    {
        int short_size = 
            long_bignat_size > State.short_size ?
            State.short_size : long_bignat_size;
        set_size(short_size, long_bignat_size, 
                 State.double_size, cipher_size);
        return;
    }



    /**
     * 
     * Special resize method only for long bignats. Short and
     * double-sized bignats will be resized to some unspecified valid
     * value. {@link ds.ov2.bignat.RSA_exponent RSA_exponent's} will
     * be left as is.
     * 
     * @param long_bignat_size new size for registered long bignats
     * @throws CardException on communication errors
     */
    public void set_long_size(int long_bignat_size)
        throws CardException
    {
        int short_size = 
            long_bignat_size > State.short_size ?
            State.short_size : long_bignat_size;
        set_size(short_size, long_bignat_size, 
                 State.double_size, 0);
        return;
    }


    /**
     * 
     * Run the memory-size protocol to obtain the amount of available
     * memory on the card.
     * 
     * @return record with amount of free memory
     * @throws CardException on communication errors
     */
    public Mem_size_result get_mem_size() 
        throws CardException
    {
        return stubs.mem_size_call(card_channel);
    }


    /**
     * 
     * Run the memory-size protocol and report the results on {@link
     * #out}. 
     * 
     */
    public void report_mem_size() {
        try {
            Mem_size_result msr = get_mem_size();
            out.format("Memory available on the card:\n" +
                       "  persistent           : %d (%.1f KB)\n" +
                       "  transient (reset)    : %d\n" +
                       "  transient (deselect) : %d\n",
                       msr.mem_persistent,
                       msr.mem_persistent / 1024.0,
                       msr.mem_transient_reset,
                       msr.mem_transient_deselect);
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 4)
                e.printStackTrace();
        }
        finally {
            out.flush();
        }
    }


    /**
     * 
     * Run the status protocol to retrieve the timestamp and the
     * maximal bignat sizes from the applet. This data is initialized
     * during applet initialization. The timestamp is initialized with
     * the last modification date of the applet cap file.
     * 
     * @return status record
     * @throws CardException on communication errors
     */
    public Status_result get_applet_stat() 
        throws CardException
    {
        Status_result stat = stubs.status_call(card_channel);
        assert stat.cap_creation_time.buf.length == 8;
        
        return stat;
    }


    /**
     * 
     * Ping the test applet.
     * 
     * @throws CardException on communication errors
     */
    public long ping() 
        throws CardException
    {
        return stubs.ping_call(card_channel);
    }


    /**
     * 
     * Ping the test applet with exception printing. This method is an
     * exception wrapper around {@link #ping}.
     * 
     */
    public void run_ping() {
        try {
            out.println("################# ping card start");
            ping();
            out.println("################# ping card finished");
        }
        catch(Response_apdu.Card_response_error e) {
            out.format("Protocol error %s\n", e);
            if(State.verbosity.ref > 9)
                e.printStackTrace();
        }
        catch(CardException e) {
            out.format("Card communication error: %s\n", e);
            if(State.verbosity.ref > 9)
                e.printStackTrace();
        }
        finally {
            out.flush();
        }
    }
}
