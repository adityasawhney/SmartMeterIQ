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
// Created 26.8.08 by Hendrik
// 
// pointers to void methods
// 
// $Id: Void_method.java,v 1.9 2009-03-26 15:51:32 tews Exp $

#include <config>

#ifdef PACKAGE
  package PACKAGE;
#else
  package ds.ov2.util;
#endif


// import javacard.framework.APDU;


/** 
 * Method pointer class. Very similar in spirit to 
 * {@link java.lang.Runnable Runnable}. We prefer to keep our own 
 * version, because then we are free to change the interface, for
 * instance for passing the APDU buffer into the method (which
 * actually had been the case once).
 *
 * @author Hendrik Tews
 * @version $Revision: 1.9 $
 * @commitdate $Date: 2009-03-26 15:51:32 $ by $Author: tews $
 * @environment card
 * @CPP This interface uses the following cpp defines:
 *   <a href="../../../overview-summary.html#PACKAGE">PACKAGE</a>,
 *   <a href="../../../overview-summary.html#PUBLIC">PUBLIC<a>
 *
 */
PUBLIC interface Void_method {

    // Pass APDU and APDU buffer, in case the waitExtension must be called.
    // public void method(APDU apdu, byte[] buf);

    // waitExtension doesn't seem to have any effect. Save some trouble.

    /**
     * Run the action captured in this class.
     */
    public void method();

}
