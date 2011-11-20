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
// SPI for a TerminalFactory instance connecting to cref/jcwde emulators
// 
// $Id: Emulator_connection_spi.java,v 1.5 2009-02-20 15:29:22 tews Exp $

package ds.javacard.emulator.smartcardio;

import java.lang.IllegalArgumentException;
import javax.smartcardio.TerminalFactorySpi;
import javax.smartcardio.CardTerminals;


public class Emulator_connection_spi extends TerminalFactorySpi {

    int verbosity = 0;
    int[] emulator_ports;

    public Emulator_connection_spi(Object o) {
        if(o == null || o instanceof int[]) {
            emulator_ports = (int[])o;
            if(verbosity > 0)
                System.out.println("EC spi constructor");
        }
        else {
            if(verbosity > 0)
                System.out.println("EC spi constructor with wrong argument\n");
            throw new IllegalArgumentException(
               "Emulator TerminalFactory expects null or an int array " +
               "(of ports) as argument");           
        }
    }


    protected CardTerminals engineTerminals() {
        if(verbosity > 0)
            System.out.println("EC spi request for card terminals");
        return new Emulator_connection_terminals(emulator_ports, verbosity);
    }
}

