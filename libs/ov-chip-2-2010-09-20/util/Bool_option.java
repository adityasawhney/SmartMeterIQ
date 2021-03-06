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
// boolean command line switch
// 
// $Id: Bool_option.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


/**
 * Boolean command line switch that sets a boolean {@link Reference} to
 * true if found on the command line.
 *
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Bool_option extends Option {

    /**
     * 
     * Reference to set to true when the option occured.
     */
    private Reference<Boolean> ref;

    /**
     * 
     * Construct a simple command line switch. When found on the
     * command line, the reference {@code ref} will be set to
     * true. 
     * 
     * @param option the literal option string as it will appear on
     * the command line
     * @param ref the reference to be switched to true
     * @param explanation explanation for the usage information
     */
    public Bool_option(String option, Reference<Boolean> ref, 
                       String explanation) 
    {
        super(option, null, explanation);
        this.ref = ref;
    }


    /**
     * 
     * Called when the option is recognized. Sets the boolean
     * reference {@link #ref} to true.
     * 
     * @param cl command line instance
     */
    public void matched(Commandline cl) {
        ref.ref = true;
    }
}