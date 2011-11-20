// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2009, 2009
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
// Created 10.2.09 by Hendrik
// 
// An implementation of Wojciech APDUListener printing to a PrintWriter
// 
// $Id: PrintWriter_APDUListener.java,v 1.3 2009-03-26 15:51:31 tews Exp $

package ds.ov2.util;


import java.io.PrintWriter;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import cardservices.APDUListener;


/**
 * An {@link APDUListener} printing to a {@link PrintWriter}. Outputs 
 * progress messages from the global platform manager to a {@link
 * PrintWriter}. The generated output can be adjusted in verbosity. By
 * default full APDU's are printed. With {@link #print_dots
 * print_dots} one can optionally switch to printing one dot per APDU.
 * The output PrintWriter can be null, then nothing is printed.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.3 $
 * @commitdate $Date: 2009-03-26 15:51:31 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class PrintWriter_APDUListener implements APDUListener {

    /**
     * Local reference the channel we print to.
     */
    private final PrintWriter out;


    /**
     * Control wether to print full APDU's or dots only. Set by {@link
     * #print_dots print_dots}.
     */
    private boolean dots_only = false;


    /**
     * Create a new APDUListener printing on {@code out}. If {@code out} is
     * null printing will be disabled.
     *
     * @param out channel to print to, pass null to disable printing.
     */
    public PrintWriter_APDUListener(PrintWriter out) {
        this.out = out;
    }


    /**
     * Switch between printing full APDU's or dots only.
     *
     * @param dots_only if true, print full APDU's subsequently; 
     *            otherwise print dots only
     */
    public void print_dots(boolean dots_only) {
        this.dots_only = dots_only;
    }


    

    /**
     * Print a command/response APDU pair to the configured channel.
     * Printing can be switched between printing full APDU's (default, 
     * set with {@link #print_dots print_dots}{@code (false)}) 
     * and one dot per APDU only (set with {@link #print_dots 
     * print_dots}{@code (true)}).
     *
     * @param c command APDU to print
     * @param r response APDU to print
     */
    public void exchangedAPDU(CommandAPDU c, ResponseAPDU r) {
        if(dots_only) {
            out.print(".");
            out.flush();
        }
        else {
            Host_protocol.print_apdu(out, c, "send");
            Response_apdu re = new Response_apdu(r);
            re.print(out, true);
        }
    }
}
