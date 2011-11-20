// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2009
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
// Created 29.5.09 by Hendrik
// 
// command line option with int argument
// 
// $Id: Int_option.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


/**
 * Command line switch with an integer argument. When the option is
 * found the integer value is recorded in a reference.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Int_option extends Option {

    /**
     * 
     * The int reference to set.
     */
    private Reference<Integer> ref;

    /**
     * 
     * Construct an integer option. When found on the
     * command line, the reference {@code ref} will be set to the
     * value of the int argument.
     * 
     * @param option the literal option string as it will appear on
     * the command line
     * @param ref the int reference 
     * @param argument name of the argument for the explanation
     * @param explanation explanation for the usage information
     */
    public Int_option(String option, Reference<Integer> ref, 
                      String argument, String explanation) 
    {
        super(option, argument, explanation);
        this.ref = ref;
    }


    /**
     * 
     * Parse an int argument from the command line. Tries to convert
     * the next unprocessed command line element to an integer.
     * Displays an error message and terminates the application if
     * there is no next command line element or if it cannot be
     * converted to an integer. If successful the index {@link
     * Commandline#next_option} in {@code cl} of the next unprocessed
     * command line element is incremented.
     * 
     * @param cl command line instance
     * @return next unprocessed option converted to int if possible
     */
    public int get_int_argument(Commandline cl) {
        NumberFormatException error = null;

        String next = cl.retrieve_option();
        if(next != null) {
            try {
                return Integer.parseInt(next);
            }
            catch(NumberFormatException e) {
                error = e;
            }
        }

        // Still here? Then we have an error!
        System.out.flush();
        System.err.format("option %s requires an int argument\n",
                          option);
        if(error != null) {
            System.err.format("Parsing %s threw a NumberFormatException " +
                              "with reason: %s\n",
                              next,
                              error.getMessage());
        }
        System.exit(1);
        return -1;
    }


    /**
     * 
     * Called when the option is recognized. Converts the next command
     * line element to an integer and puts it into {@link #ref}.
     * Prints an error and exits the program on any problem.
     * 
     * @param cl command line instance
     */
    public void matched(Commandline cl) {
        ref.ref = get_int_argument(cl);
    }
}