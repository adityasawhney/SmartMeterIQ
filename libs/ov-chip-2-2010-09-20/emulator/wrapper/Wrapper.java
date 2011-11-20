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
// Created 6.10.08 by Hendrik
// 
// Wrapper for continously running cref/jcwde emulators
// 
// $Id: Wrapper.java,v 1.3 2009-02-20 15:29:22 tews Exp $

package ds.javacard.emulator.wrapper;

import java.lang.InterruptedException;
import java.lang.Throwable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.ProtocolException;


public class Wrapper {

    public static final String short_application_name = "Wrapper";

    public static final String long_application_name = 
        "wrapper for continuously running cref/jcwde emulators";

    static enum Emulator {
        CREF, JCWDE
    }


    //########################################################################
    // configuration section
    //

    private final static int emulator_port_default = 9010;
    private static int emulator_port = emulator_port_default;

    private final static int server_port_default = 9025;
    private static int server_port = server_port_default;

    private final static Emulator default_emulator = Emulator.JCWDE;
    private static Emulator emulator = default_emulator;

    private static String emulator_class_path = null;
    private static String jcwde_config = null;

    private static String input_eeprom = null;
    private static String output_eeprom = null;

    private static int verbosity = 0;

    public static void parse_commandline(String[] args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-h") || 
               args[i].equals("-help") ||
               args[i].equals("--help")) {
                usage();
                System.exit(0);
            }
            else if(args[i].equals("-jcwde")) {
                emulator = Emulator.JCWDE;
            }
            else if(args[i].equals("-cref")) {
                emulator = Emulator.CREF;
            }
            else if(args[i].equals("-e")) {
                emulator_port = get_int_argument(i, args);
                i++;
            }
            else if(args[i].equals("-s")) {
                server_port = get_int_argument(i, args);
                i++;
            }
            else if(args[i].equals("-i")) {
                input_eeprom = get_string_argument(i, args);
                i++;
            }
            else if(args[i].equals("-o")) {
                output_eeprom = get_string_argument(i, args);
                i++;
            }
            else if(args[i].equals("-cp")) {
                emulator_class_path = get_string_argument(i, args);
                i++;
            }
            else if(args[i].equals("-v")) {
                verbosity = 1;
            }
            else if(jcwde_config == null) {
                jcwde_config = args[i];
            }
            else {
                System.out.format("%s: unrecognized argument %s\n",
                                  short_application_name, args[i]);
                usage();
                System.exit(1);
            }
        }

        // Check consistency. For jcwde there must be a config file.
        if(emulator == Emulator.JCWDE && jcwde_config == null) {
            System.err.println
                ("Error: The jcwde emulator needs a config file.\n");
            usage();
            System.exit(1);
        }

        return;
    }


    // print this for -h or when command line parsing fails
    public static void usage() {
        System.out.println(long_application_name);
        System.out.println
            ("Usage: java ds/ov/test/Test_host [options...] [jcwde.config]");
        System.out.println("Recognized options are:");
        format_option_description(new String[][]{
            {"-h",            "print usage information"},
            {"-jcwde",        "use jcwde emulator" +
                              (default_emulator == Emulator.JCWDE ? 
                               " [default]" : "")},
            {"-cref",         "use cref emulator" +
                               (default_emulator == Emulator.CREF ? 
                                " [default]" : "")},
            {"-e p",          String.format("set emulator port [default %d]",
                                            emulator_port_default)},
            {"-s p",          String.format("set server port [default %d]",
                                            server_port_default)},
            {"-i eeprom",     "set input eeprom file for cref"},
            {"-o eeprom",     "set output eeprom file for cref"},
            {"-cp classpath", "set CLASSPATH environment variable " +
                              "for emulator"},
            {"-v",            "increase verbosity"},
        });
    }


    public static void format_option_description(String[][] opt) {
        int longest_left = 0;
        for(int i = 0; i < opt.length; i++) {
            if(opt[i][0].length() > longest_left)
                longest_left = opt[i][0].length();
        }
        for(int i = 0; i < opt.length; i++) {
            String[] o = opt[i];
            System.out.print("  ");
            System.out.print(o[0]);
            for(int j = o[0].length(); j < longest_left; j++)
                System.out.print(' ');
            System.out.print("  ");
            System.out.println(o[1]);
        }
    }


    public static int get_int_argument(int i, String[] args) {
        if(i + 1  < args.length)
            return Integer.parseInt(args[i + 1]);
        else {
            System.out.format("%s: option %s requires an int argument",
                              short_application_name,
                              args[i]);
            System.exit(1);
        }
        return -1;
    }

    public static String get_string_argument(int i, String[] args) {
        if(i + 1  < args.length)
            return args[i + 1];
        else {
            System.out.format("%s: option %s requires a string argument",
                              short_application_name,
                              args[i]);
            System.exit(1);
        }
        return "";
    }


    private static void print_bytes(String msg, byte[] buf, int len) {
        assert len <= buf.length;
        synchronized(System.out) {
            System.out.format("%s got %d bytes:", msg, len);
            for(int i = 0; i < len; i++)
                System.out.format(" %02X", buf[i]);
            System.out.print("\n");
        }
    }


    public static class Copy_stream implements Callable<Object> {
        String id;
        InputStream in;
        OutputStream out;
        boolean close_in;
        boolean close_out;
        boolean print_data;
        private static final int buf_len = 4096;
        byte[] buf = new byte[buf_len];

        Copy_stream(String id_arg, InputStream i, OutputStream o,
                    boolean ci, boolean co,
                    boolean pd) 
        {
            id = id_arg;
            in = i;
            close_in = ci;
            close_out = co;
            out = o;
            print_data = pd;
        }

        private Object call_ex() 
            throws IOException
        {
            System.out.format("WRAPPER: start %s copy stream\n", id);
            while(true) {
                int len = in.read(buf);
                if(len == -1)
                    break;
                if(print_data) {
                    print_bytes(id, buf, len);
                }
                out.write(buf, 0, len);
                out.flush();
                // System.out.format("%s piped %d bytes\n", id, len);
            }
            System.out.format("%s EOF\n", id);
            return null;
        }
        

        public Object call()
            throws IOException, Exception
        {
            try {
                return call_ex();
            }
            catch(Throwable e) {
                System.out.format("Exception escaping copy stream %s\n", id);
                e.printStackTrace();
                System.out.format("%s End\n", id);
                throw new Exception(e);
            }
            finally {
                if(close_in) {
                    System.out.format("%s close in\n", id);
                    in.close();
                }
                if(close_out) {
                    System.out.format("%s close out\n", id);
                    out.close();
                }
            }
        }
    }


    static Process emulator_process;
    static byte[] atr;
    static OutputStream emulator_in;
    static InputStream emulator_out;
    static Socket emulator_socket;
    static Future<Object> emulator_stdout, emulator_stderr;
    static Future<Object> connection_in, connection_out;

    public static class Server implements Callable<Object> {
        private final int port;
        private final ExecutorService ex;
        private final AtomicBoolean working_connection;
        
        public Server(int p, ExecutorService e, AtomicBoolean wc) {
            port = p;
            ex = e;
            working_connection = wc;
            wc.set(false);
            return;
        }

        public void treat_power_up(InputStream in, OutputStream out) 
            throws ProtocolException, IOException
        {
            // power up command over T=1 connection is F0 00 00 F0
            byte[] buf = new byte[4];
            if(in.read(buf) != 4)
                throw new ProtocolException
                    ("powerup missing in new connection");
            if((buf[0] & 0xff) != 0xF0 || 
               (buf[1] & 0xff) != 0x00 || 
               (buf[2] & 0xff) != 0x00 || 
               (buf[3] & 0xff) != 0xF0)
                throw new ProtocolException
                    ("powerup missing in new connection");
            // OK, received powerup, send ATR back
            assert atr != null;
            out.write(atr);
            System.out.println("WRAPPER: received powerup, send ATR reply");
        }


        private Object call_ex() 
            throws IOException, InterruptedException, ExecutionException
        {
            System.out.format("WRAPPER: starting server at port %d\n", port);
            ServerSocket socket = new ServerSocket(port);
            int counter = 0;
            while(true) {
                Socket connection = socket.accept();
                System.out.println("WRAPPER: new connection");
                counter += 1;

                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();
                treat_power_up(in, out);
                synchronized(working_connection) {
                    connection_in = 
                        ex.submit(new Copy_stream(String.format("IN %d", 
                                                                counter), 
                                                  in, emulator_in, 
                                                  true, false,
                                                  true));
                    connection_out =
                        ex.submit(new Copy_stream(String.format("OUT %d",
                                                                counter), 
                                                  emulator_out, out, 
                                                  false, true,
                                                  true));
                    working_connection.set(true);
                }
                connection_in.get();
                if(connection_out.cancel(true)) 
                    System.out.println("WRAPPER: canceled OUT");
                else
                    System.out.println("WRAPPER: cancel OUT failed");
                try {
                    connection_out.get();
                }
                catch(CancellationException e) {
                }

                System.out.println("WRAPPER: connection finished");
                synchronized(working_connection) {
                    working_connection.set(false);
                }
            }
        }


        public Object call()
            throws IOException, InterruptedException, ExecutionException,
                   Exception
        {
            try {
                return call_ex();
            }
            catch(Throwable e) {
                System.out.println("Exception escaping Server thread");
                e.printStackTrace();
                System.out.println("WRAPPER: Server terminated");
                System.exit(1);
                return null;
            }
        }
    }


    private static String[] make_emulator_line() {
        String jckit_bin_dir = 
            JCKIT_Path.path +
            (JCKIT_Path.path.charAt(JCKIT_Path.path.length() -1) == '/' 
             ? "" : "/") +
            "bin/";
        int args, i;
        String line[];
        switch(emulator) {
        case CREF:
            args = 3;
            if(input_eeprom != null) args += 2;
            if(output_eeprom != null) args += 2;
            line = new String[args];
            i = 0;
            line[i++] = jckit_bin_dir + "cref";
            line[i++] = "-p";
            line[i++] = Integer.toString(emulator_port);
            if(input_eeprom != null) {
                line[i++] = "-i";
                line[i++] = input_eeprom;
            }
            if(output_eeprom != null) {
                line[i++] = "-o";
                line[i++] = output_eeprom;
            }
            assert i == line.length;
            break;
        case JCWDE:
            assert jcwde_config != null;
            args = 4;
            line = new String[args];
            i = 0;
            line[i++] = jckit_bin_dir + "jcwde";
            line[i++] = "-p";
            line[i++] = Integer.toString(emulator_port);
            line[i++] = jcwde_config;
            assert i == line.length;
            break;
        default:
            assert false;
            // still here??
            return new String[0];
        }
        return line;
    }


    private static void change_environment(Map<String, String> env) {
        switch(emulator) {
        case CREF:
            break;
        case JCWDE:
            if(emulator_class_path != null) {
                System.out.format("WRAPPER: add CLASSPATH=%s\n",
                                  emulator_class_path);
                env.put("CLASSPATH", emulator_class_path);
            }
            break;
        default:
            assert false;
        }
    }
            
            
    private static void do_power_up(OutputStream out, InputStream in) 
        throws IOException
    {
        byte[] obuf = new byte[]{(byte)0xF0, (byte)0x00, 
                                 (byte)0x00, (byte)0xF0};
        System.out.println("WRAPPER: send powerup to emulator");
        out.write(obuf);
        int ibuf_len = 200;
        byte[] ibuf = new byte[ibuf_len];
        int len = in.read(ibuf);
        // In my tests the encoded ATR from cref and jcwde was 10 bytes long.
        if(len == ibuf_len) {
            System.err.format("Received %d bytes as answer to powerup. " +
                              "Something is wrong\n", len);
            System.exit(1);
        }
        print_bytes("WRAPPER: powerup atr", ibuf, len);
        if(atr == null) {
            atr = Arrays.copyOf(ibuf, len);
        }
        else {
            if(!Arrays.equals(atr, Arrays.copyOf(ibuf, len))) {
                System.err.println ("Received different ATR's from different " +
                                    "emulator instances.");
                System.exit(1);
            }
        }
    }


    public static void start_emulator(ExecutorService ex) 
        throws IOException, InterruptedException
    {
        String[] emulator_line = make_emulator_line();
        ProcessBuilder pb = new ProcessBuilder(emulator_line);
        change_environment(pb.environment());

        System.out.print("WRAPPER: start emulator:");
        for(int i = 0; i < emulator_line.length; i++)
            System.out.print(" " + emulator_line[i]);
        System.out.print("\n");
        emulator_process = pb.start();

        emulator_process.getOutputStream().close();
        emulator_stdout = 
            ex.submit(new Copy_stream("STDOUT",
                                      emulator_process.getInputStream(),
                                      System.out,
                                      true,
                                      false,
                                      false));
        emulator_stderr =
            ex.submit(new Copy_stream("STDERR",
                                      emulator_process.getErrorStream(),
                                      System.err,
                                      true,
                                      false,
                                      false));
        // Admit jcwde some startup time: 0.1 sec.
        Thread.sleep(200);
        
        System.out.format("WRAPPER: connect to emulator at port %d\n", 
                          emulator_port);
        emulator_socket = new Socket("localhost", emulator_port);
        emulator_out = emulator_socket.getInputStream();
        emulator_in = emulator_socket.getOutputStream();
        do_power_up(emulator_in, emulator_out);
    }


    private static void keep_emulator(ExecutorService ex, 
                                      AtomicBoolean working_connection) 
        throws IOException, InterruptedException
    {
        while(true) {
            if(emulator_process != null) {
                System.out.println
                    ("WRAPPER: waiting for emulator to terminate");
                emulator_process.waitFor();
                System.out.println("WRAPPER: Emulator terminated");
                emulator_stdout.cancel(true);
                emulator_stdout = null;
                emulator_stderr.cancel(true);
                emulator_stderr = null;
                emulator_socket.close();
                System.out.println("WRAPPER: cleaned up emulator socket");

                // sleep to make the port free again
                Thread.sleep(500);
            }

            System.out.println("WRAPPER: start new emulator");
            synchronized(working_connection) {
                if(working_connection.get()) {
                    System.out.println("WRAPPER: kill old client connection");
                    connection_in.cancel(true);
                    connection_out.cancel(true);
                    working_connection.set(false);
                }
                start_emulator(ex);
            }
        }
    }


    public static void main_ex(String[] args) 
        throws IOException, InterruptedException
    {
        parse_commandline(args);
        ExecutorService ex = Executors.newFixedThreadPool(5);
        AtomicBoolean working_connection = new AtomicBoolean(false);
        try {
            ex.submit(new Server(server_port, ex, working_connection));
            keep_emulator(ex, working_connection);
        }
        finally {
            ex.shutdown();
        }
    }


    public static void main(String[] args) 
        throws IOException, InterruptedException
    {
        try {
            main_ex(args);
        }
        catch(Throwable e) {
            e.printStackTrace();
            System.out.println("Escaping exception in main thread, terminate.");
            System.exit(1);
        }
    }
}

