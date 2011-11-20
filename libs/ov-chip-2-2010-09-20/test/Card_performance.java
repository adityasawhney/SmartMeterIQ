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
// Created 31.8.08 by Hendrik
// 
// Measure the performance of the card
// 
// $Id: Card_performance.java,v 1.16 2009-06-02 13:27:56 tews Exp $

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.*;


/** 
 * Performance measurements for the protocol layer.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.16 $
 * @commitdate $Date: 2009-06-02 13:27:56 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
class Card_performance {

    /**
     * 
     * Channel for writing progress and debug messages.
     */
    private PrintWriter out;

    /**
     * 
     * Host instance for the miscellaneous protocols. Needed for
     * accessing the ping protocol.
     */
    private Misc_protocols_host misc_host;

    /**
     * 
     * Host driver instance for the data protocol.
     */
    private Data_protocol_host data_host;

    /**
     * 
     * Instance for data transmission checks.
     */
    private Check_data_transmission check_transmission;

    /**
     * 
     * Stubs for the data protocol.
     */
    private Data_protocol_stubs stubs;

    /**
     * 
     * Channel to the applet.
     */
    private CardChannel card_channel;


    /**
     * 
     * Constructor. Initialize all fields.
     * 
     * @param test_protocols protocol array of the test host driver
     * @param card_channel channel to the applet
     */
    public Card_performance(Test_protocols test_protocols,
                            CardChannel card_channel) {
        this.card_channel = card_channel;
        out = new PrintWriter(System.out, true);
        misc_host = new Misc_protocols_host(test_protocols, card_channel, out);
        check_transmission = 
            new Check_data_transmission(test_protocols.data_protocol, 
                                        card_channel);

        PrintWriter o = State.verbosity.ref >= 5 ? out : null;
        data_host = Data_protocol_host.makeit(test_protocols.data_protocol, 
                                              card_channel, o);
        stubs = data_host.get_stubs();
    }


    //########################################################################
    // measure ping
    // 


    /**
     * 
     * Measure 10 rounds of the ping protocol and print maximal,
     * minimal and everage timings.
     * 
     * @throws CardChannel on communication errors
     */
    public void measure_ping() 
        throws CardException
    {
        long duration, sum, max, min;

        out.println("###### 10 x ping");
        max = 0;
        min = Long.MAX_VALUE;
        sum = 0;
        for(int i = 0; i < 10; i++) {
            duration = misc_host.ping();
            if(duration > max)
                max = duration;
            if(duration < min)
                min = duration;
            sum += duration;
        }
        out.format("### average: %.3f ms max %.3f ms min %.3f ms\n",
                   new Long(sum).doubleValue() / 10 / 1E6,
                   new Long(max).doubleValue() / 1E6,
                   new Long(min).doubleValue() / 1E6);
        return;
    }


    //########################################################################
    // measure sending to the card
    // 

    /**
     * 
     * Measure sending data to the card. Measures 10 times sending 2KB
     * (2048 Bytes) and prints the timings. 
     * <P>
     *
     * The measurement is slightly biased, because the data is written
     * to EEPROM on the card.
     * 
     * @throws CardException on communication errors
     * @todo Measure transmission time to RAM as well
     */
    public void measure_send() 
        throws CardException
    {
        long duration, sum, max, min;

        data_host.set_size(2048, 2048, 2048, 2048, 2048, 
                           2048, 2048, 2048, 2048, 2048, 
                           true);

        Resizable_buffer a = new Resizable_buffer((short)2048);
        PrintWriter hp_out = State.verbosity.ref >= 5 ? out : null;

        out.println("###### 10 x send 2K start");
        max = 0;
        min = Long.MAX_VALUE;
        sum = 0;
        for(int i = 0; i < 10; i++) {
            duration = stubs.data_performance_receive_call(card_channel, a);
            if(duration > max)
                max = duration;
            if(duration < min)
                min = duration;
            sum += duration;
        }
        
        out.format("### average: %.3f ms max %.3f ms min %.3f ms %.3f K/s\n",
                   new Long(sum).doubleValue() / 10 / 1E6,
                   new Long(max).doubleValue() / 1E6,
                   new Long(min).doubleValue() / 1E6,
                   2 / (new Long(sum).doubleValue() / 10 / 1E9)
                   );
        return;
    }


    //########################################################################
    // measure receiving from the card
    // 


    /**
     * 
     * Measure receiving data from the card. Measures 10 times
     * receiving 2KB (2048 Bytes) from the card and prints the
     * timings. 
     * <P>
     *
     * The measurement is slightly biased, because the data is written
     * to EEPROM on the card.
     * 
     * @throws CardException on communication errors
     * @todo Measure transmission time to RAM as well
     */
    public void measure_receive()
        throws CardException
    {
        long duration, sum, max, min;

        out.println("###### 10 x receive 2K start");

        data_host.set_size(2048, 2048, 2048, 2048, 2048, 
                           2048, 2048, 2048, 2048, 2048, 
                           true);

        PrintWriter hp_out = State.verbosity.ref >= 5 ? out : null;

        max = 0;
        min = Long.MAX_VALUE;
        sum = 0;
        for(int i = 0; i < 10; i++) {
            duration = stubs.data_performance_send_call(card_channel).duration;
            if(duration > max)
                max = duration;
            if(duration < min)
                min = duration;
            sum += duration;
        }
        
        out.format("### average: %.3f ms max %.3f ms min %.3f ms %.3f K/s\n",
                   new Long(sum).doubleValue() / 10 / 1E6,
                   new Long(max).doubleValue() / 1E6,
                   new Long(min).doubleValue() / 1E6,
                   2 / (new Long(sum).doubleValue() / 10 / 1E9)
                   );
        return;
    }


    //########################################################################
    // mesure 20 K send and receive
    // 

    /**
     * 
     * Measure sending 10KB and receiving 10KB and print the timings.
     * <P>
     *
     * The measurement is slightly biased, because the data is written
     * to EEPROM on the card.
     * 
     * @throws Data_protocol_host.Data_error never thrown, because the
     * transmission is done with performance flag set to true; only
     * declared to satisfy the compiler
     * @throws CardException on communication errors
     * @todo Measure transmission time to RAM as well
     */
    public void measure_big_send_and_receive() 
        throws CardException, Data_protocol_host.Data_error
    {
        long duration;
        duration = check_transmission.check_with_sizes
            ("size 2048", 2048,2048,2048,2048,2048,
             2048,2048,2048,2048,2048, true);


        out.format("### Transferring 10K to the card and back " +
                   "without checks: %.3f sec (%.3f K/s)\n",
                   new Long(duration).doubleValue() / 1E9,
                   20 / 
                   (new Long(duration).doubleValue() / 1E9)
                   );

        return;
    }


    //########################################################################
    // mesure proof attributes communication
    // 

    /**
     * 
     * Measure data transmission with sizes as we expect for the proof
     * protocol for an RSA key size of 2048 bits. 
     * <P>
     *
     * The size used in this method represent the expectations from
     * August 2008, long before the proof protocol has been
     * implemented.
     * <P>
     *
     * The measurement is slightly biased, because the data is written
     * to EEPROM on the card.
     * 
     * @throws CardException on communication errors
     * @todo Measure transmission time to RAM as well
     */
    public void measure_proof() 
        throws CardException
    {
        long start, duration, sum, max, min;

        out.println("###### 10 x proof immitation start");
        PrintWriter hp_out = State.verbosity.ref >= 5 ? out : null;

        int gate_meta_size = 20;
        int card_meta_size = 10;
        int large_bigint_size = 2048 / 8;
        int small_bigint_size = 200 / 8;

        Resizable_buffer gate_meta = 
            new Resizable_buffer((short)gate_meta_size);
        Resizable_buffer challenge = 
            new Resizable_buffer((short)small_bigint_size);

        // buf's usage for proof immitation
        // buf_0         gate meta data
        // buf_1         card meta data
        // buf_2         A', a large bigint
        // buf_3         w, another large bigint
        // buf_4         gamma, a small bigint
        // buf_5..buf_8  r_i, small bigints  
        // buf_9         s, a large bigint

        data_host.set_size(gate_meta_size, card_meta_size,
                           large_bigint_size, large_bigint_size,
                           small_bigint_size,
                           small_bigint_size, small_bigint_size,
                           small_bigint_size, small_bigint_size,
                           large_bigint_size,
                           true);

        max = 0;
        min = Long.MAX_VALUE;
        sum = 0;
        for(int i = 0; i < 10; i++) {
            start = System.nanoTime();

            stubs.data_perf_proof_commit_call(card_channel, gate_meta);
            stubs.data_perf_answer_to_challenge_call(card_channel, challenge);

            duration = System.nanoTime() - start;
            if(duration > max)
                max = duration;
            if(duration < min)
                min = duration;
            sum += duration;
        }
        
        out.format("### average: %.3f ms max %.3f ms min %.3f ms\n",
                   new Long(sum).doubleValue() / 10 / 1E6,
                   new Long(max).doubleValue() / 1E6,
                   new Long(min).doubleValue() / 1E6);
        return;
    }



    //########################################################################
    // run all tests/measurements
    // 


    /**
     * 
     * Run all data transmission performance measurements.
     * 
     * @throws Data_protocol_host.Data_error never thrown, because the
     * transmission is done with performance flag set to true; only
     * declared to satisfy the compiler
     * @throws CardException on communication errors
     */
    public void run_ex() 
        throws CardException, Data_protocol_host.Data_error
    {
        out.println("################# performance test start");

        measure_ping();

        measure_send();

        measure_receive();

        measure_big_send_and_receive();

        measure_proof();

        out.println("\n" +
                    "######################################################\n" +
                    "#################\n" +
                    "################# performance test finished\n" +
                    "#################\n" +
                    "######################################################\n");

        return;
    }


    /**
     * 
     * Exception wrapper for {@link #run_ex}. Runs all the data
     * transmission performance measurements and prints escaping
     * exceptions. 
     * 
     */
    public void run() {
        try {
            run_ex();
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
        catch(Data_protocol_host.Data_error e) {
            out.format("Data error at position %d\n", e.position);
            if(State.verbosity.ref > 9)
                e.printStackTrace();
        }
    }
}
