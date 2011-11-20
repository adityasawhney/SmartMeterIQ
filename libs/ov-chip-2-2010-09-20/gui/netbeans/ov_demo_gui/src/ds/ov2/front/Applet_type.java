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
// Created 22.4.09 by Hendrik
// 
// Partial copy of front/Applet_type
// 
// $Id: Applet_type.java,v 1.1 2009-05-11 08:27:19 tews Exp $

package ds.ov2.front;


/**
 * 
 * Copy the Applet_type enum from the real sources here, to satisfy
 * the netbeans IDE.
 * @author tews
 *
 * @author Hendrik Tews
 * @version $Revision: 1.1 $
 * @commitdate $Date: 2009-05-11 08:27:19 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public enum Applet_type {

    /**
     * 
     * ID for the plain RSA applet. The plain RSA applet computes
     * exponents with the RSA cipher and communicates with the
     * host by exchanging the plain, unmontgomerized numbers.
     * Corresponds to {@link RSA_data#PLAIN_RSA_APPLET}.
     */
    PLAIN_RSA_APPLET,

    /**
     * 
     * ID for the Montgomerizing RSA applet. The Montgomerizing
     * RSA applet computes everything on the JCVM, without the RSA
     * cipher. It communicates with the host by exchanging
     * montgomerized numbers.
     */
    MONT_RSA_APPLET;
}