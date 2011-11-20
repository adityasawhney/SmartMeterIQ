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
// command line option setting a certain value
// 
// $Id: Value_option.java,v 1.1 2009-06-02 09:56:03 tews Exp $

package ds.ov2.util;


/**
 * Command line switch which sets a reference to a certain value.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-06-02 09:56:03 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Value_option<E> extends Option {

    /**
     * 
     * The reference to set.
     */
    private final Reference<E> ref;

    /**
     * 
     * The value to store in {@link #ref} when the option is found.
     */
    private final E value;


    /**
     * 
     * Construct a value option. When found on the
     * command line, the reference {@code ref} will be set to the
     * specified value.
     * 
     * @param option the literal option string as it will appear on
     * the command line
     * @param ref the reference 
     * @param value the value to set
     * @param explanation explanation for the usage information
     */
    public Value_option(String option, Reference<E> ref, E value,
                        String explanation) 
    {
        super(option, null, explanation);
        this.ref = ref;
        this.value = value;
    }


    /**
     * 
     * Called when the option is recognized. Sets the reference {@link
     * #ref} to the specified value {@link #value}.
     * 
     * @param cl command line instance
     */
    public void matched(Commandline cl) {
        ref.ref = value;
    }
}