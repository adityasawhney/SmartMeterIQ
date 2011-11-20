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
// Created 4.10.08 by Hendrik
// 
// provider for a TerminalFactory connecting to cref/jcwde emulators
// 
// $Id: DS_provider.java,v 1.6 2009-02-20 15:29:22 tews Exp $

package ds.javacard.emulator.smartcardio;


import java.security.Provider;
import java.security.ProviderException;

import com.sun.javacard.apduio.Apdu;


public class DS_provider extends Provider {

    public static final long serialVersionUID = 1L;

    public DS_provider() {
        super("DS_provider", 0.1d, 
              "access to cref/jcwde emulators through TerminalFactory");
        put("TerminalFactory.SunEmulator", 
            "ds.javacard.emulator.smartcardio.Emulator_connection_spi");
        // Peek into apduio.jar to check if it is there.
        try {
            Apdu x = new Apdu();
        }
        catch(NoClassDefFoundError e) {
            throw new ProviderException("apduio.jar library missing", e);
        }
        // System.out.println("DS_provider constructor");
     }
}

