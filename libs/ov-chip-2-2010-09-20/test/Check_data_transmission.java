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
// Created 27.8.08 by Hendrik
// 
// check correctness of the data transmission to the card
// 
// $Id: Check_data_transmission.java,v 1.14 2009-06-02 13:27:57 tews Exp $

package ds.ov2.test;

import java.io.PrintWriter;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import ds.ov2.util.*;


/** 
 * Test the protocol layer by sending arguments and receiving results
 * of varying sizes. The bytes sent and received are enumerated to
 * detect bugs. Checks various corner cases, for instance total
 * argument length of 509, 510 and 511 bytes. 
 *
 * @author Hendrik Tews
 * @version $Revision: 1.14 $
 * @commitdate $Date: 2009-06-02 13:27:57 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
class Check_data_transmission {

    /**
     * 
     * Output channel for progress messages.
     */
    private PrintWriter out;

    /**
     * 
     * Instance for running the data transmission protocol.
     */
    private Data_protocol_host data;


    /**
     * 
     * Constructor. Initialize all fields.
     * 
     * @param data_protocol data protocol description instance
     * @param cc channel to the applet
     */
    public Check_data_transmission(Data_protocol_description data_protocol,
                                   CardChannel cc) {
        out = new PrintWriter(System.out, true);
        PrintWriter o = State.verbosity.ref >= 5 ? out : null;
        data = Data_protocol_host.makeit(data_protocol, cc, o);
    }


    /**
     * 
     * Test the protocol layer by sending 5 arguments and receiving 5
     * results of the specified size. Runs first the data size
     * protocol to set the size of the {@link Resizable_buffer
     * Resizable_buffer's} that are used as arguments and results. If
     * {@code performance_test} is false the bytes in the transmission
     * are enumerated to catch reordering bugs. 
     * 
     * @param msg identifier for progress messages
     * @param a1 size of the first argument
     * @param a2 size of the second argument
     * @param a3 size of the third argument
     * @param a4 size of the fourth argument
     * @param a5 size of the fifth argument
     * @param r1 size of the first result
     * @param r2 size of the second result
     * @param r3 size of the third result
     * @param r4 size of the fourth result
     * @param r5 size of the fifth result
     * @param performance_test if true do not enumerate the bytes sent
     * to obtain pure transmission times
     * @return duration of the data transmission (without setting the
     * sizes or allocating the buffers but with the protocol layer
     * overhead) in nanoseconds
     * @throws Data_protocol_host.Data_error if an error in the
     * protocol layer is detected
     * @throws CardException on communication problems
     */
    // Return duration in nanoseconds for data transmission (without set_size).
    public long check_with_sizes(String msg, 
                                 int a1, int a2, int a3, int a4, int a5,
                                 int r1, int r2, int r3, int r4, int r5,
                                 boolean performance_test) 
        throws CardException, Data_protocol_host.Data_error
    {
        out.println("######## check " + msg + " start");

        data.set_size(a1, a2, a3, a4, a5, r1, r2, r3, r4, r5,
                      performance_test);

        return data.check_data(performance_test);
    }


    /**
     * 
     * Run protocol layer checks. Check with a number of different
     * argument and result sizes.
     * 
     * @throws Data_protocol_host.Data_error if an error in the
     * protocol layer is detected
     * @throws CardException on communication problems
     */
    public void run_ex() 
        throws CardException, Data_protocol_host.Data_error
    {
        out.println("################# check data start");

        check_with_sizes("size 1", 1,1,1,1,1,1,1,1,1,1, false);

        check_with_sizes("size 509", 199,222,48,39,1,199,222,48,39,1, false);

        check_with_sizes("size 510", 200,222,48,39,1,200,222,48,39,1, false);

        check_with_sizes("size 511", 201,222,48,39,1,201,222,48,39,1, false);

        check_with_sizes("size 5 x 254", 
                         254,254,254,254,254,254,254,254,254,254, 
                         false);

        check_with_sizes("size 5 x 255", 
                         255,255,255,255,255,255,255,255,255,255, 
                         false);

        check_with_sizes("size 5 x 256", 
                         256,256,256,256,256,256,256,256,256,256, 
                         false);

        check_with_sizes("size 5 x 2100", 2100,2100,2100,2100,2100,
                         2100,2100,2100,2100,2100, 
                         false);

        out.println("\n" +
                    "######################################################\n" +
                    "#################\n" +
                    "################# check data successfully finished\n" +
                    "#################\n" +
                    "######################################################\n");
    }


    /**
     * 
     * Exception wrapper for {@link #run_ex}. Run the protocol layer
     * checks and print escaping exceptions.
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
