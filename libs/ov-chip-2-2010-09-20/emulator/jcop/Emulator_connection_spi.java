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
// SPI for a TerminalFactory instance connecting to the jcop emulator
// 
// $Id: Emulator_connection_spi.java,v 1.7 2009-06-17 20:14:02 tews Exp $

package ds.javacard.emulator.jcop;

import javax.smartcardio.TerminalFactorySpi;
import javax.smartcardio.CardTerminals;


public class Emulator_connection_spi extends TerminalFactorySpi {

    int verbosity = 0;

    private final static int default_port = 8015;

    private final int[] ports;

    public Emulator_connection_spi(Object o) {
        if(verbosity > 0)
            System.out.println("JC spi constructor creates " + this);
        if(o == null)
            ports = new int[]{ default_port };
        else if(o instanceof int[])
            ports = (int[])o;
        else
            throw new IllegalArgumentException
                ("jcop terminal factory requires an int array as parameter");
        return;
    }


    private static Object lock = new Object();

    private static Emulator_connection_terminals terminals = null;

    private Emulator_connection_terminals get_terminals() {
        synchronized(lock) {
            if(terminals == null) {
                if(verbosity > 0)
                    System.out.println("JC spi create new terminals instance");
                terminals = new Emulator_connection_terminals(ports, 
                                                              verbosity);
            }
            else if(verbosity > 0)
                System.out.println("JC spi reuse terminals instance");
        }

        return terminals;
    }


    protected CardTerminals engineTerminals() {
        if(verbosity > 0)
            System.out.println("JC spi request for card terminals");
        return get_terminals();
    }
}

