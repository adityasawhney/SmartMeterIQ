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
// skeleton for main
// 
// $Id: main-skeleton.java,v 1.5 2009-05-27 09:40:02 tews Exp $

package ds.ov2.test;


public class Test_host {

    public static final String short_application_name = "Test_host";

    public static final String long_application_name = 
        "Test applet host driver";

    //########################################################################
    // configuration section
    //

    private static int verbosity = 0;

    public static void parse_commandline(String[] args) {
        Commandline cl = new Commandline(args, short_application_name);

        while(cl.continue_parsing()) {
            if(cl.match("-h") || 
               cl.match("-help") ||
               cl.match("--help")) 
                {
                    usage();
                    System.exit(0);
                }
            else if(cl.match("-i")) {
                String input = cl.get_string_argument();
                if(State.fix_input_index == State.max_fix_inputs) {
                    System.err.format("can only accept upto %d fix "+
                                      "input arguments\n",
                                      State.max_fix_inputs);
                    System.exit(1);
                }
                State.fix_inputs[State.fix_input_index++] = input;
            }
            else if(cl.match("-hex")) {
                String input = cl.get_string_argument();
                if(State.fix_input_index == State.max_fix_inputs) {
                    System.err.format("can only accept upto %d fix "+
                                      "input arguments\n",
                                      State.max_fix_inputs);
                    System.exit(1);
                }
                String without_dot = input.replace(".", "");
                State.fix_inputs[State.fix_input_index++] = 
                    new BigInteger(without_dot, 16).toString();
            }
            else if(cl.match("-d")) {
                verbosity = 5;
            }
            else if(cl.match("-dd")) {
                verbosity = 10;
            }
            else if(cl.match("-ddd")) {
                verbosity = 15;
            }
            else {
                System.out.format("%s: unrecognized option %s\n",
                                  short_application_name, 
                                  cl.next_option());
                usage();
                System.exit(1);
            }
        }
        return;
    }


    // print this for -h or when command line parsing fails
    public static void usage() {
        System.out.println(long_application_name);
        System.out.println("Usage: java ds/ov/test/Test_host [options...]");
        System.out.println("Recognized options are:");
        Commandline.format_option_description(new String[][]{
            {"-i n",          "provide decimal n as input for the test"},
            {"-hex n",        "provide hex n as input for the test"},
            {"-h",            "print usage information"},
            {"-v",            "increase verbosity"},
            {"-d[d[d]]",      "print debug messages, " + 
                                    "the more d's the more messages"},
        });
    }


    public static void main_ex(String[] args) 
    {
        parse_commandline(args);
    }


    public static void main(String[] args) {
        //try {
            main_ex(args);
        // }
        // catch(X e) {
        // }
    }
}
