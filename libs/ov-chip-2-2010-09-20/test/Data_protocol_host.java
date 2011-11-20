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
// Created 29.8.08 by Hendrik
// 
// data protocol: host driver
// 
// $Id: Data_protocol_host.java,v 1.17 2009-06-02 13:27:57 tews Exp $

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.*;

import ds.ov2.test.Data_protocol_stubs.Check_data_result;

/** 
 * Host driver for the data protocols that check/measure the protocol
 * layer. An instance of this class owns an instance of the
 * description class of the data protocols. Because of the size
 * changes of the {@link Resizable_buffer Resizable_buffer's} therein
 * this description instance must only exist once (more precisely one
 * instance in the host driver and one instance inside the applet).
 * Therefore this class is an enforced singleton class.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.17 $
 * @commitdate $Date: 2009-06-02 13:27:57 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
class Data_protocol_host {
    // This is a singleton class.
    // This is even enforced in the constructor.

    //########################################################################
    // Exception classes thrown here
    // 

    /**
     * 
     * Exception to indicate that an error in the protocol layer has
     * been detected when checking the results from the applet.
     */
    static class Data_error extends Exception {
        
        /** Field to disable the serialVersionUID warning. */
        public static final long serialVersionUID = 1L;

        /**
         * 
         * Byte index at which the error occured (result local).
         */
        public final int position;


        /**
         * 
         * Construct a new Data_error exception for an error that has
         * been detected at index {@code p}.
         * 
         * @param p index at which the error occured
         */
        Data_error(int p) {
            super(String.format("Data error at position %d", p));
            position = p;
        }
    }


    //########################################################################
    // Data that is shared between most of the methods
    // 

    /**
     * 
     * Channel to the applet.
     */
    private CardChannel card_channel;

    /**
     * 
     * Description instance of the data protocols.
     */
    private Data_protocol_description description;

    /**
     * 
     * Channel for debug and progress messages.
     */
    private PrintWriter out;

    /**
     * 
     * Stubs for the data protocols.
     */
    private Data_protocol_stubs stubs;


    /**
     * 
     * Accessor for the stubs instance.
     * 
     * @return the stubs for the data protocols
     */
    public Data_protocol_stubs get_stubs() {
        return stubs;
    }


    //########################################################################
    // Constructor
    // 

    /**
     * 
     * Reference holding the single instance of this class.
     */
    private static Data_protocol_host singleton = null;


    /**
     * 
     * Constructor. Initialize all fields and the singleton reference
     * {@link #singleton}. 
     * <P>
     *
     * Asserts that {@link #singleton} is null before assigning to it.
     * 
     * @param data_protocol description instance of the data protocols
     * @param card_channel channel to the applet
     * @param out progress/debug message channel
     */
    protected Data_protocol_host(Data_protocol_description data_protocol,
                                 CardChannel card_channel, PrintWriter out) {
        assert singleton == null;
        singleton = this;

        this.card_channel = card_channel;
        this.out = out;
        description = data_protocol;
        PrintWriter stubs_out = State.verbosity.ref >= 10 ? out : null;
        stubs = new Data_protocol_stubs(description,
                                        stubs_out, State.apduscript.ref);
    }


    /**
     * 
     * Return the signleton instance of this class. If no instance
     * exists yet, it is created. If it does exist, it is checked
     * (via an assertion) wheter its card channel is identical to the
     * {@code card_channel} argument of this method.
     * 
     * @param data_protocol description instance of the data protocols
     * @return card_channel channel to the applet
     * @param out progress/debug message channel
     */
    public static Data_protocol_host makeit(Data_protocol_description 
                                                               data_protocol,
                                            CardChannel card_channel, 
                                            PrintWriter out) 
    {
        if(singleton != null) {
            assert (card_channel == singleton.card_channel);
            return singleton;
        }
        return new Data_protocol_host(data_protocol, card_channel, out);
    }


    //########################################################################
    // check data call
    // 


    /**
     * 
     * Enumerate the bytes in a {@link Resizable_buffer}. Start with
     * {@code count}. Return the count for the following {@link
     * Resizable_buffer}. 
     * 
     * @param b buffer to fill
     * @param count starting counter
     * @return count for the following {@link Resizable_buffer}
     */
    private byte fill_buffer(Resizable_buffer b, byte count) {
        for(int i = 0; i < b.size(); i++) {
            b.buf[i] = count++;
        }
        return count;
    }


    /**
     * 
     * Check the enumberation of resulting {@link Resizable_buffer}
     * that we received from the card. Throws {@link Data_error} if
     * an error is detected.
     * 
     * @param b the buffer to check
     * @param count the starting count for the first byte
     * @return count for the next result
     * @throws Data_error for any detected enumeration error
     */
    private byte check_buffer(Resizable_buffer b, byte count) 
        throws Data_error
    {
        for(int i = 0; i < b.size(); i++) {
            if(b.buf[i] != count) {
                if(out != null)
                    out.format("DATA check error at pos %d found %02X " +
                               "expected %02X\n",
                               i, b.buf[i] & 0xff, count & 0xff);
                throw new Data_error(i);
            }
            count++;
        }
        return count;
    }
                

    /**
     * 
     * Run the data protocol once for testing the protocol layer.
     * Sends 5 arguments to the card and receives 5 results. Their
     * sizes must have been configured before with one of the set_size
     * methods ({@link #set_size(int[], boolean) set_size} or {@link
     * #set_size(int, int, int, int, int, int, int, int, int, int,
     * boolean) set_size}). This method uses the sizes of the buffers
     * in the description instance of the data protocols.
     * 
     * @param performance_test if true do neither enumerate arguments
     * nor check results
     * @return duration of the data transmission (including the
     * protocol layer overhead) in nanoseconds
     * @throws Data_error for any detected error in the protocol layer
     * @throws CardException for communication errors
     */
    public long check_data(boolean performance_test) 
        throws CardException, Data_error
    {
        if(out != null)
            out.println("### check data start");
        byte count = 0;
        Resizable_buffer a1 = new Resizable_buffer(description.buf_0.size());
        Resizable_buffer a2 = new Resizable_buffer(description.buf_1.size());
        Resizable_buffer a3 = new Resizable_buffer(description.buf_2.size());
        Resizable_buffer a4 = new Resizable_buffer(description.buf_3.size());
        Resizable_buffer a5 = new Resizable_buffer(description.buf_4.size());

        if(!performance_test) {
            count = fill_buffer(a1, count);
            count = fill_buffer(a2, count);
            count = fill_buffer(a3, count);
            count = fill_buffer(a4, count);
            count = fill_buffer(a5, count);
        }

        Check_data_result r = stubs.check_data_call(card_channel, 
                                                    a1, a2, a3, a4, a5);

        if(!performance_test) {
            count = 0;
            count = check_buffer(r.buf_5, count);
            count = check_buffer(r.buf_6, count);
            count = check_buffer(r.buf_7, count);
            count = check_buffer(r.buf_8, count);
            count = check_buffer(r.buf_9, count);
        }

        if(out != null)
            out.format("### check data finished (%.3f ms)\n",
                       new Long(r.duration).doubleValue() / 1E6
                       );
        return r.duration;
    }


    //########################################################################
    // set_size call
    // 


    /**
     * 
     * Set the sizes of the {@link Resizable_buffer
     * Resizable_buffer's} that are used as arguments and results in
     * the data protocol. The maximal possible size for all buffers in
     * the applet is given by a compile time constant. If bigger sizes
     * are requested the applet ignores the size request. The
     * resulting buffer sizes are returned in any case and this method
     * uses them to set the buffers in the description instance of the
     * host driver to the same size as on the card. A message is
     * printed to {@link #out} if the requested size was not set on
     * the applet.
     * 
     * @param new_sizes array of 10 positive shorts with the requested
     * sizes 
     * @param performance_test performance test flag to set or clear
     * in the applet
     * @throws CardException on communication errors
     */
    public void set_size(int[] new_sizes, boolean performance_test) 
        throws CardException
    {
        if(out != null)
            out.println("### set_size start");

        // new_sizes must be an array of 10 elements
        // of positive shorts.
        assert new_sizes != null && new_sizes.length == 10;
        for(int i = 0; i < 10; i++) {
            assert 0 <= new_sizes[i] && new_sizes[i] <= Short.MAX_VALUE;
        }

        // Set sizes in the card. 
        int actual_sizes[] = stubs.set_size_call(card_channel, 
                                                 new_sizes, performance_test);

        // The card limits the sizes it sets to a compilation time
        // constant (Data_protocol_description.check_data_max_size).
        // The card might therefore set different sizes. The sizes it 
        // has set are now in actual_sizes.

        // Set the same sizes on our copy of Data_protocol_description.
        description.get_buf_sizes().copy(actual_sizes);
        description.set_size();
        int[] osizes = description.get_buf_sizes().get_int_array();
        // check that osizes has not been changed by our copy of
        // Data_protocol_description.
        assert actual_sizes.length == osizes.length;
        for(int i = 0; i < actual_sizes.length; i++)
            assert actual_sizes[i] == osizes[i];

        for(int i = 0; i < 10; i++) {
            if(new_sizes[i] != actual_sizes[i]) {
                if(out != null)
                    out.format("set different size for buf %d: " +
                               "requested %d but set %d\n",
                               i, new_sizes[i], actual_sizes[i]);
            }
        }

        if(out != null)
            out.println("### set_size finished");
        return;
    }


    /**
     * 
     * Same as {@link #set_size(int[], boolean)} but where the 10
     * sizes are passed as separate arguments.
     * 
     * @param a1 requested size for the first argument
     * @param a2 requested size for the second argument
     * @param a3 requested size for the third argument
     * @param a4 requested size for the fourth argument
     * @param a5 requested size for the fifth argument
     * @param r1 requested size for the first result
     * @param r2 requested size for the second result
     * @param r3 requested size for the third result
     * @param r4 requested size for the fourth result
     * @param r5 requested size for the fifth result
     * @param performance_test performance test flag to set or clear
     * in the applet
     * @throws CardException for communication errors
     */
    public void set_size(int a1, int a2, int a3, int a4, int a5,
                          int r1, int r2, int r3, int r4, int r5,
                          boolean performance_test)
        throws CardException
    {
        set_size(new int[]{a1, a2, a3, a4, a5, r1, r2, r3, r4, r5},
                 performance_test);
        return;
    }

}
