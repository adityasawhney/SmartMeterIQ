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
// Created 2.10.08 by Hendrik
// 
// to be included in Data_protocol_description
// 
// $Id: Data_protocol_description_include.java,v 1.5 2009-04-09 21:07:27 tews Exp $


/**
 * 
 * The maximal size of the ten {@link Resizable_buffer
 * Resizable_buffer's} used for the protocol data checks. Has value
 * {@value}. 
 */
public static final short check_data_max_size = 2100;


/**
 * 
 * Change the sizes of the {@link Resizable_buffer
 * Resizable_buffer's}. The sizes are taken from {@link #buf_sizes}.
 * The new actual sizes of the buffers are afterwards written back
 * into {@link #buf_sizes}. This method does also call {@link
 * Test_protocols#set_result_sizes} to update the result sizes of all
 * registered protocols.
 */
public void set_size() {
    buf_0.set_size(buf_sizes.get((short)0));
    buf_1.set_size(buf_sizes.get((short)1));
    buf_2.set_size(buf_sizes.get((short)2));
    buf_3.set_size(buf_sizes.get((short)3));
    buf_4.set_size(buf_sizes.get((short)4));
    buf_5.set_size(buf_sizes.get((short)5));
    buf_6.set_size(buf_sizes.get((short)6));
    buf_7.set_size(buf_sizes.get((short)7));
    buf_8.set_size(buf_sizes.get((short)8));
    buf_9.set_size(buf_sizes.get((short)9));

    // Reinitialize the result size cache.
    test_protocols.set_result_sizes();

    // set_size is ignored if the requested size is too big.
    // Report now the actual sizes.
    buf_sizes.set((short)0, buf_0.size());
    buf_sizes.set((short)1, buf_1.size());
    buf_sizes.set((short)2, buf_2.size());
    buf_sizes.set((short)3, buf_3.size());
    buf_sizes.set((short)4, buf_4.size());
    buf_sizes.set((short)5, buf_5.size());
    buf_sizes.set((short)6, buf_6.size());
    buf_sizes.set((short)7, buf_7.size());
    buf_sizes.set((short)8, buf_8.size());
    buf_sizes.set((short)9, buf_9.size());

    return;
}


/**
 * 
 * Return the {@link #buf_sizes} array.
 * 
 * @return the {@link #buf_sizes} array
 */
public APDU_short_array get_buf_sizes() {
    return buf_sizes;
}

