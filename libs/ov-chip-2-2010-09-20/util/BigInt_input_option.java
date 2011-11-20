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
// BigInteger input
// 
// $Id: BigInt_input_option.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


import java.math.BigInteger;



/**
 * Command line option with a {@link BigInteger} argument which is to
 * be recoreded in a {@link BigInteger_inputs} instance.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class BigInt_input_option extends Option {

    /**
     * 
     * The vector with the BigInteger inputs.
     */
    private final BigInteger_inputs inputs;

    /**
     * 
     * Construct an BigInteger input option. When found on the
     * command line, the next command line element will be taken as a
     * base 10 {@link BigInteger} and added to {@code inputs}.
     * 
     * @param option the literal option string as it will appear on
     * the command line
     * @param inputs the input collection
     * @param argument name of the argument for the explanation
     * @param explanation explanation for the usage information
     */
    public BigInt_input_option(String option, BigInteger_inputs inputs,
                               String argument, String explanation) 
    {
        super(option, argument, explanation);
        this.inputs = inputs;
    }


    /**
     * 
     * Parse a {@link BigInteger} argument from the command line.
     * Tries to convert the next unprocessed command line element to a
     * BigInteger. Displays an error message and terminates the
     * application if there is no next command line element or if it
     * cannot be converted.
     * 
     * @param cl command line instance
     * @return next unprocessed option converted to {@link BigInteger}
     * if possible
     */
    public BigInteger get_bigint_argument(Commandline cl) {
        NumberFormatException error = null;

        String next = cl.retrieve_option();
        if(next != null) {
            try {
                return new BigInteger(next);
            }
            catch(NumberFormatException e) {
                error = e;
            }
        }

        // Still here? Then we have an error!
        System.out.flush();
        System.err.format("option %s requires a decimal BigInteger argument\n",
                          option);
        if(error != null) {
            System.err.format("Parsing %s threw a NumberFormatException " +
                              "with reason: %s\n",
                              next,
                              error.getMessage());
        }
        System.exit(1);
        return BigInteger.ONE;
    }


    /**
     * 
     * Called when this option has been recognized on the command
     * line. Retrives the next command line argument, interprets it as
     * a base 10 BigInteger and stores it in
     * the vector of BigInteger inputs {@link #inputs}. 
     * 
     * @param cl command line instance
     */
    public void matched(Commandline cl) {
        inputs.add(get_bigint_argument(cl));
    }
}