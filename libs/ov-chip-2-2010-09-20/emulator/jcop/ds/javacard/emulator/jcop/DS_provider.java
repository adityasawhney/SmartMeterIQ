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
// Created 31.10.08 by Hendrik
// 
// provider for a TerminalFactory connecting to the jcop emulator
// 
// $Id: DS_provider.java,v 1.5 2009-02-20 15:29:21 tews Exp $

package ds.javacard.emulator.jcop;

import java.security.ProviderException;
import java.security.Provider;

import com.ibm.jc.terminal.RemoteJCTerminal;

public class DS_provider extends Provider {

    public static final long serialVersionUID = 1L;

    public DS_provider() 
        throws ProviderException
    {
        super("DS_provider", 0.1d, 
              "access to the jcop emulator through TerminalFactory");
        put("TerminalFactory.JcopEmulator", 
            "ds.javacard.emulator.jcop.Emulator_connection_spi");
        // Peek into offcard.jar to check if it is there.
        try {
            RemoteJCTerminal x = new RemoteJCTerminal();
        }
        catch(NoClassDefFoundError e) {
            throw new ProviderException("offcard.jar library missing", e);
        }
        // System.out.println("DS_provider constructor");
     }
}

