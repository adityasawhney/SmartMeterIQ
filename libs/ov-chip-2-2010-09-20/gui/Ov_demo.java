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
// Created 1.1.09 by Hendrik
// 
// root class for application logic
// 
// $Id: Ov_demo.java,v 1.10 2009-06-22 21:09:39 tews Exp $

package ds.ov2.gui;


import ds.ov2.util.Commandline;
import ds.ov2.util.Option;
import ds.ov2.util.String_option;
import ds.ov2.util.Int_option;
import ds.ov2.util.Bool_option;
import ds.ov2.util.Parse_commandline;


/** 
 * Root class for the application logic of the graphical demonstrator.
 * Having such a root class was a nice idea, which however got only
 * little support. So only very little is done here: The constructor
 * parses the command line and creates the interlocked instances of
 * {@link Card_protocols}, {@link Gui_actions} and {@link
 * Progress_messages}.
 * <P>
 *
 * Apart from the standard swing accelerator keys the following key
 * strokes are defined.
 * <DL>
 * <DT><STRONG>q and CTRL-Q</STRONG>
 * <DD>immediately quit the application
 * </DL>
 * <P>
 *
 * The OV-chip graphical demonstrator recognizes the following
 * options.
 * <DL>
 * <DT><STRONG>-load-ptls-params-file file</STRONG>
 * <DD>Load a set of PTLS parameters from file on startup.
 * <DT><STRONG>-jcop-port p</STRONG>
 * <DD>Set the port number(s) for the jcop emulator. The option can be
 * given multiple times to configure more than one emulator. Default
 * is one emulator at port 8015.
 * </DL>
 *
 * @author Hendrik Tews
 * @version $Revision: 1.10 $
 * @commitdate $Date: 2009-06-22 21:09:39 $ by $Author: tews $
 * @environment host
 */
public class Ov_demo {

    /**
     * 
     * Short application name for error messages during option
     * parsing. 
     */
    private final String short_application_name = "OV-chip demonstrator";

    /**
     * 
     * Card protocols instance of the graphical demonstrator.
     */
    private Card_protocols card_protocols = new Card_protocols();

    /**
     * 
     * Gui actions instance of the graphical demonstrator.
     */
    public final Gui_actions gui_actions;

    /**
     * 
     * Progress window. The window does always exist and the
     * application permanentely writes to the progress text pane
     * therein. The relevant buttons only change the visibility of the
     * progress window. They do not create or destroy it.
     */
    public final Progress_messages progress_window;

    /**
     * 
     * Array of recognized command line options.
     */
    private final Option[] options = new Option[] {
        new String_option("-load-ptls-params", Gui_state.ptls_params_file,
                          "file", "load PTLS parameters from file"),
        new Int_option("-r", Gui_state.default_reader_number, "n",
                       "use card reader number n as default"),
        new Bool_option("-jcop", Gui_state.default_jcop,
                           "use jcop emulator as default terminal"),
        new Int_option("-jcop-port", null, "p",
                       "add jcop emulator at port p") {
            public void matched(Commandline cl) {
                Gui_state.jcop_ports.add(get_int_argument(cl));
            }
        }
    };
    

    /**
     * 
     * Constructor. Parse the command line and create some of the
     * necessary instances for the GUI.
     * 
     * @param args the command line arguments, they are ignored
     * @param gui the GUI instance, currently also ignored
     */
    public Ov_demo(String[] args, Ov_demo_gui gui) {

        new Parse_commandline(options, short_application_name).parse(args);

        if(Gui_state.jcop_ports.size() == 0)
            Gui_state.jcop_ports.add(8015);

        gui_actions = new Gui_actions(card_protocols);
        progress_window = new Progress_messages(gui, gui_actions);
        // Set the close action for the progress window.
        progress_window.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    progress_window.setVisible(false);
                }
            });
        gui_actions.progress_window = progress_window;
    }
}
