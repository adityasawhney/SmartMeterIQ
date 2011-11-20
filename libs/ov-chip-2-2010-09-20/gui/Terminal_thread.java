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
// Created 5.1.09 by Hendrik
// 
// The background thread controlling the card terminal
// 
// $Id: Terminal_thread.java,v 1.16 2010-03-01 21:20:16 tews Exp $

package ds.ov2.gui;


import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.math.BigInteger;
import javax.smartcardio.CardException;
import javax.swing.SwingWorker;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;

import cardservices.AIDRegistry;
import cardservices.AIDRegistryEntry;
import cardservices.AIDRegistryEntry.Kind;
import cardservices.GlobalPlatformException;
import cardservices.GlobalPlatformInstallForLoadException;
import cardservices.GlobalPlatformLoadException;

import ds.ov2.util.Action;
import ds.ov2.util.Card_terminal.Applet_selection_exception;
import ds.ov2.front.Applet_type;


/** 
 * Worker thread that performs all card related tasks. The GUI can
 * only be controlled from one thread, the GUI thread or, in SUN Java
 * speak, the event dispatch thread. Because the GUI thread should not
 * contain long running methods all the real work must be done in a
 * separate thread, the terminal thread or, in SUN Java speak, the
 * worker thread. The terminal thread is called terminal thread,
 * because most of the code it executes talks to a Java Card terminal.
 * The code of this class is executed on two threads: the GUI thread
 * and the terminal thread. Many methods can only be executed on one
 * thread. The method documentation indicates to which thread each
 * method belongs. The GUI starts the terminal thread by calling the
 * inherited method {@link #execute execute}, which then executes
 * {@link #doInBackground} in a different thread. <P>
 *
 * The GUI and the terminal thread communicate in the following way. The
 * GUI sets the {@link #next_action} that the terminal thread should
 * execute and, if necessary, interrupts the terminal thread (via
 * {@link #set_next_action set_next_action}). The terminal thread
 * performs an endless loop that always executes the action that is
 * found in {@link #next_action}. To simplify the logic here, each
 * role of the GUI has its own waiting action, which runs when the GUI
 * is waiting for the user to press some buttons. This waiting action
 * is started when the corresponding role is selected. The waiting
 * action then enables and disables all the buttons in the GUI as
 * cards are inserted and removed in an endless loop. The waiting
 * action only terminates if the terminal thread gets interrupted
 * because the GUI sets a new action. <P>
 *
 * The terminal thread {@link #publish publish'es} the actions to be
 * executed in the GUI. They are then somewhere queued by the swing
 * framework and get executed in the GUI in the {@link #process
 * process} method. <P>
 *
 * For simplicity all the actions that are communicated between the
 * GUI and the terminal thread are simply {@link Runnable Runnable's}.
 * The action to be executed in the other thread is captured in the
 * {@link Runnable#run run} method. The thread that creates an action
 * has to create a suitable anonymous class instance. This instance is
 * then communicated to the other thread (either via {@link
 * #set_next_action set_next_action} or via {@link #publish publish}).
 * The other thread then executes the action by calling the {@link
 * Runnable#run run} method. For creating anonymous classes and objects of
 * the {@link Runnable} interface the {@link Action} class is used.
 * <P>
 *
 * The {@link Runnable#run} method does not permit any checked
 * exceptions, therefore all {@link CardException CardException's} are
 * wrapped in a {@link Runtime_card_exception Runtime_card_exception}.
 * If this class were properly written it would use a version of
 * {@link Runnable} that permits {@link CardException} in its {@link
 * Runnable#run run} method...
 *
 * @author Hendrik Tews
 * @version $Revision: 1.16 $
 * @commitdate $Date: 2010-03-01 21:20:16 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 * @todo the {@link #messages} field should be moved to {@link Card_protocols}
 */
class Terminal_thread extends SwingWorker<Void, Runnable> {

    /**
     * 
     * The GUI object. Needed for accessing and changing the GUI.
     */
    private final Ov_demo_gui gui;


    /**
     * 
     * GUI actions for the GUI thread.
     */
    private final Gui_actions gui_actions;


    /**
     * 
     * Instance for printing progess messages in the GUI.
     * @todo this field should be copied to {@link Card_protocols}
     */
    private final Gui_protocol_messages messages;


    /**
     * 
     * Card protocol instance.
     */
    private final Card_protocols card_protocols;


    /**
     * 
     * The terminal thread. Whenever the user suddenly changes his mind
     * the GUI must interrupt the terminal thread, see {@link
     * #set_next_action set_next_action}. 
     */
    private Thread thread = null;


    /**
     * 
     * Constructor. Record the arguments into private state.
     * <P>
     *
     * Can be executed on any thread.
     * 
     * @param gui the GUI object
     * @param gui_actions the GUI actions
     */
    public Terminal_thread(Ov_demo_gui gui, Gui_actions gui_actions,
                           Card_protocols card_protocols) 
        {
            this.gui = gui;
            this.gui_actions = gui_actions;
            this.card_protocols = card_protocols;
            messages = new Gui_protocol_messages(this);
        }


    /**
     * 
     * Process actions to change the GUI in the GUI thread (event
     * dispatch thread). For simplicity the actions are encapsulated
     * in the {@link Runnable#run run} method of a {@link Runnable}.
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param actions actions that have accumulated since the last
     * call to this method.
     */
    public void process(List<Runnable> actions) {
        // System.out.format("EDT: received %d actions\n", actions.size());
        for(Runnable a : actions) 
            a.run();
    }



    //########################################################################
    // 
    // Predefined GUI actions
    // 

    /**
     * 
     * GUI Action to display an arbitrary message in the status line.
     * 
     * @param message the message to display
     */
    private Action gui_status_line_message(final String message) {
        return new Action() {
            public void run() {
                gui.status_line.setText(message);
            }
        };
    }


    /**
     * 
     * GUI Action to clear the status line in the GUI.
     */
    private Action gui_clear_message = gui_status_line_message(" ");


    /**
     * 
     * GUI Action to display the line "Waiting for a card ..." in the
     * status line of the GUI.
     */
    /* package */ Action gui_wait_for_card = 
        gui_status_line_message("Waiting for a card ...");


    /**
     * 
     * GUI Action to display "Please remove card" in the status line of
     * the GUI.
     */
    private Action gui_wait_for_card_removal = 
        gui_status_line_message("Please remove card");


    /**
     * 
     * GUI Action to display "Card inserted" in the status line of the
     * GUI.
     */
    /* package */ Action gui_card_inserted =
        gui_status_line_message("Card inserted");


    /**
     * 
     * GUI Action to display "Proof finished. Please remove card." in
     * the status line of the GUI.
     */
    private Action gui_proof_finished =
        gui_status_line_message("Gate protocol finished. Please remove card.");


    /**
     * 
     * GUI Action to display an error to the user on the fatal event that
     * the terminal thread dies. When the user acknowledges the
     * application is terminated.
     * <P>
     *
     * Should be executed on the Terminal thread.
     */
    private Action gui_background_died(Throwable e) {
        final String msg = 
            String.format("Card/Terminal interaction died with exception\n" +
                          "%s\nExit now.",
                          e);
        return new Action() {
                public void run() {
                    JOptionPane.showMessageDialog(gui, msg,
                                                  "tab selection error",
                                                  JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            };
    }


    /**
     * 
     * GUI action to display a message in a message text pane. When the
     * {@code bold} flag is true the message is displayed in the
     * "bold" style, which therefore should exist on the text pane
     * {@code pane}. 
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param pane the text pane to write to
     * @param message the message
     * @param bold display in bold if true
     */
    public void gui_message(final JTextPane pane, 
                            final String message, 
                            final boolean bold) {
        publish(new Action() {
                public void run() {
                    Document doc = pane.getDocument();
                    try {
                        doc.insertString(doc.getLength(), message, 
                                         bold ? pane.getStyle("bold") : null);
                    }
                    catch(BadLocationException e) {
                        assert false;
                    }
                }
            });
    }


    /**
     * 
     * GUI action to display a progress message. The message can be
     * displayed in bold.
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param message the message
     * @param bold display in bold if true
     */
    public void gui_progress_message(String message, boolean bold) {
        gui_message(gui_actions.progress_window.progress_text_pane, 
                    message, bold);
    }


    /**
     * 
     * GUI action to display a card appearence message.
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param blinded_a the blinded attribute expression of the card
     * @param message the message
     */
    public void gui_card_message(BigInteger blinded_a, String message) {
        String card_id = blinded_a.toString().substring(0, 10);
        gui_message(gui.card_log_text_pane,
                    "card " + card_id, 
                    true);
        gui_message(gui.card_log_text_pane,
                    " " + message + "\n", 
                    false);
    }


    /**
     * 
     * Gui action to display a warning dialog if the card is invalid
     * for some reason.
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param message more detailed message 
     * @return GUI action to display the warning dialog
     */
    private Action invalid_card_warning(final String message) {
        return new Action() {
            public void run() {
                gui_actions.gui_warning(gui,
                                        "Invalid Card: " + message,
                                        "Invalid Card");
            }
        };
    }


    /**
     * 
     * Gui action to display a warning if card communication fails for
     * some reason.
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param message some more detailed reason
     * @return GUI action to display the warning dialog
     */
    private Action card_communication_error_warning(final String message) {
        return new Action() {
            public void run() {
                gui_actions.gui_warning(gui, message,
                                        "Card communication error");
            }
        };
    }


    //########################################################################
    //########################################################################
    // 
    // Code for the background thread.
    // 
    // General card thread stuff
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Lock for {@link #next_action}. All accesses to {@link
     * #next_action} must be guarded by locking this object.
     */
    private Object action_lock = new Object();


    /**
     * 
     * Next action to execute in the terminal thread. Or null, if the
     * terminal thread currently executes an action and the next action
     * has not been set yet. All accesses to this field must be
     * guarded by locking the object in {@link #action_lock}.
     * There are three methods that should be used instead of
     * accessing this field: {@link #get_next_action}, {@link
     * #set_next_action set_next_action} and {@link
     * #set_successor_action set_successor_action}.
     */
    private Action next_action = null;


    /**
     * 
     * Return the next action for the terminal thread and clear the
     * {@link #next_action} field.
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @return next action to execute on the terminal thread
     */
    private Action get_next_action() {
        synchronized(action_lock) {
            Action res = next_action;
            next_action = null;
            return res;
        }
    }


    /**
     * 
     * Set the next action to be executed in the terminal thread and
     * interrupt the action that currently executes on the terminal
     * thread. This method is for use in the GUI, for instance, when
     * the user presses some button. Note that the interrupts in Java
     * are not really interrupts, so it might take some time until the
     * terminal thread reacts.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param next_action next action for the terminal thread
     */
    public void set_next_action(Action next_action) {
        synchronized(action_lock) {
            this.next_action = next_action;
            if(thread != null && thread != Thread.currentThread()) {
                Gui_state.out.println("set next action interrupt");
                thread.interrupt();
            }
            else {
                Gui_state.out.println("set next action no interrupt");
            }
        }
    }


    /**
     * 
     * Set a successor action for the currently executing action. This
     * method is meant for chaining actions in the terminal thread.
     * Setting the successor action takes only effect if the GUI has
     * not itself set an action. 
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param successor_action the successor action
     */
    protected void set_successor_action(Action successor_action) {
        synchronized(action_lock) {
            if(next_action == null) {
                next_action = successor_action;
                Gui_state.out.println("set successor action");
            }
            else {
                Gui_state.out.println("skip successor action");
            }
        }
    }


    /**
     * 
     * Runtime version of {@link CardException}. Needed for exceptions
     * that might escape an action in the terminal thread. 
     */
    private static class Runtime_card_exception extends RuntimeException 
    {
        /** serialVersionUID for the Serializable interface */
        public static final long serialVersionUID = 1L;

        /**
         * 
         * Constructs a new Runtime_card_exception exception with the
         * specified cause and a detail message of {@code (cause==null ? null
         * : cause.toString())} (which typically contains the class and
         * detail message of cause).
         * 
         * @param cause cause, typically a {@link CardException}
         */
        public Runtime_card_exception(Throwable cause) {
            super(cause);
        }
    }


    /**
     * 
     * Remembers whether we believe that there is currently a card
     * present in the configured card reader.
     */
    public boolean card_is_present = false;


    /**
     * 
     * Wait until the card is removed or the terminal thread is
     * interrupted. Because {@link javax.smartcardio.CardTerminal}
     * does not abort timeouts on interrupts this method performs
     * busy-waiting and checks every 100 milliseconds whether the
     * terminal thread has been interrupted. <P>
     *
     * Must be executed on the Terminal thread.
     * 
     * @throws InterruptedException in case this thread was
     * interrupted before a card removal has been detected
     * @throws Runtime_card_exception in case of problems with
     * the card reader
     */
    public void wait_until_card_is_removed()
        throws InterruptedException, Runtime_card_exception
    {
        Gui_state.out.println("wait until card is removed");
        try {
            while(Gui_state.card_terminal.isCardPresent()) {
                try {
                    Gui_state.card_terminal.waitForCardAbsent(100);
                }
                catch(CardException e) {
                    if(e.getMessage().equals("wait mismatch")) {
                        Gui_state.out.println("wait mismatch exception");
                    }
                }
                if(Thread.interrupted()) {
                    Gui_state.out.println("Interrupted!");
                    throw new InterruptedException();
                }
            }
        }
        catch(CardException e) {
            throw new Runtime_card_exception(e);
        }

        card_is_present = false;
        if(card_protocols.valid_parameters())
            publish(gui_wait_for_card);
    }


    /**
     * 
     * Wait until a card is inserted or the terminal thread is
     * interrupted. An {@link InterruptedException} is generated if an
     * interrupt is detected before a card insertion. Because {@link
     * javax.smartcardio.CardTerminal} does not react to interrupts,
     * this method performs busy waiting and checks every 100
     * milliseconds whether an interrupt occurred. <P>
     *
     * Must be executed on the Terminal thread.
     * 
     * @throws InterruptedException if an interrupt is detected
     * @throws Runtime_card_exception for card reader problems
     */
    public void wait_for_card() 
        throws InterruptedException, Runtime_card_exception
    {
        if(card_protocols.valid_parameters())
            publish(gui_wait_for_card);

        try {
            while(!Gui_state.card_terminal.isCardPresent()) {
                Gui_state.card_terminal.waitForCardPresent(100);
                if(Thread.interrupted())
                    throw new InterruptedException();
            }
        }
        catch(CardException e) {
            throw new Runtime_card_exception(e);
        }

        card_is_present = true;
        if(card_protocols.valid_parameters())
            publish(gui_card_inserted);
    }


    //########################################################################
    //########################################################################
    // 
    // PTLS office role
    // 
    // Wait action
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * GUI Action to enable all the card maintenance buttons in the GUI.
     */
    private Action gui_enable_ptls_actions =
        new Action() {
            public void run() {
                gui_actions.enable_card_maintenance(gui);
            }
        };


    /**
     * 
     * GUI Action to disable all the card maintenance buttons in the GUI.
     */
    private Action gui_disable_ptls_actions =
        new Action() {
            public void run() {
                gui_actions.disable_card_maintenance(gui);
            }
        };


    /**
     * 
     * Terminal thread wait action for the PTLS office. This action
     * does not finish if the card is removed. Instead it switches the
     * PTLS buttons on and off as cards are inserted and removed until
     * it is interrupted because of a new action.
     */
    private Action wait_in_ptls_office =
        new Action() {
            public void run() {
                Gui_state.out.println("start waiting in PTLS office");

                try {
                    while(true) {
                        wait_for_card();
                        publish(gui_enable_ptls_actions);
                        wait_until_card_is_removed();
                        publish(gui_disable_ptls_actions);
                    }
                }
                catch(InterruptedException e) {}

                Gui_state.out.println("finish waiting in PTLS office");
            }
        };


    /**
     * 
     * Set the PTLS waiting action. 
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     */
    public void wait_in_ptls_office() {
        set_next_action(wait_in_ptls_office);
    }
    

    //########################################################################
    //########################################################################
    // 
    // Card status
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Return the applet list window or throw an assertion if no such
     * window is registered.
     * <P>
     *
     * Not bound to any thread.
     * 
     * @return the applet list window instance
     */
    private Applet_list_window get_applet_list_window() {
        return gui_actions.get_applet_list_window();
    }


    /**
     * 
     * GUI action to clear the applet list in the applet list dialog.
     */
    private Action clear_applet_list =
        new Action() {
            public void run() {
                DefaultTableModel tm = (DefaultTableModel)
                    (get_applet_list_window().
                     applet_list_applet_table.getModel());
                tm.setRowCount(0);
            }
        };


    /**
     * 
     * GUI action for adding an applet/package line to the applet list
     * dialog.
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param kind kind of the entry
     * @param name name of the entry
     * @return GUI action for adding the specified line
     */
    private Action applet_table_line(final String kind, 
                                     final Printable_aid name) 
    {
        return new Action() {
            public void run() {
                DefaultTableModel tm = (DefaultTableModel)
                    (get_applet_list_window().
                     applet_list_applet_table.getModel());

                tm.addRow(new Object[]{ kind, name, false });
            }
        };
    }


    /**
     * 
     * The AID of the package that seems to be on any card. Will be
     * filtered out from the applet list dialog (if everything is not
     * checked). 
     */
    private final byte[] default_applet_name = 
        new byte[]{(byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x00, 
                   (byte)0x03, (byte)0x53, (byte)0x50};


    /**
     * 
     * Filter the interesting applets/packages to display in the
     * applet list dialog. The default applet packages are filtered
     * out. 
     * <P>
     *
     * Not bound to any thread.
     * 
     * @param e registry entry describing one applet/package
     * @return true if {@code e} should be displayed without the
     * everything check box checked
     */
    private boolean status_entry_filter(AIDRegistryEntry e) {
        if(e.getKind() == Kind.IssuerSecurityDomain)
            return false;
        if(Arrays.equals(e.getAID().getBytes(), default_applet_name))
            return false;
        return true;
    }


    /**
     * 
     * Display all applets/packages of one registry in the applet list
     * dialog. 
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param r the registry to display
     * @param everything if true display all entries, otherwise filter
     * default entries out
     * @return true if nothing has been displayed
     */
    private boolean publish_registry(AIDRegistry r, boolean everything) {
        boolean empty = true;

        publish(clear_applet_list);

        for(AIDRegistryEntry e : r) {
            if(everything || status_entry_filter(e)) {
                publish(applet_table_line(e.getKind().toShortString(),
                                          new Printable_aid(e.getAID())));
                empty = false;
            }
        }

        return empty;
    }


    /**
     * 
     * Terminal thread action for displaying installed
     * applets/packages in the applet list display. Querys the status
     * of the card, updates the display and set the ptls office
     * waiting action {@link #wait_in_ptls_office} as successor
     * action. 
     * <P>
     *
     * The action can throw a {@link Runtime_card_exception} for a
     * low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param everything if true display all card entries, otherwise
     * filter default entries out
     * @return the encapsolated action
     */
    private Action card_status(final boolean everything) 
    {
        return new Action() {
            public void run() {
                Gui_state.out.println("start card status");
                gui_progress_message("query card status\n", false);

                AIDRegistry r;
                try {
                    r = card_protocols.get_card_status();
                }
                catch(CardException e) {
                    gui_progress_message("got CardException\n\n", true);
                    throw new Runtime_card_exception(e);
                }

                publish_registry(r, everything);

                Gui_state.out.println("end card status");
                gui_progress_message("card status complete\n\n", true);

                set_successor_action(wait_in_ptls_office);
            }
        };
    }


    /**
     * 
     * Set card status action. Only to be called from within the GUI
     * thread. 
     * <P>
     *
     * Must be executed on the GUI thread.
     *
     * @param everything if true display all card entries in the
     * applet list dialog, otherwise filter default entries out
     */
    public void start_card_status_action(boolean everything) {
        Gui_state.out.println("set card status action");
        set_next_action(card_status(everything));
    }



    //########################################################################
    //########################################################################
    // 
    // Install applet
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Gui action to display a warning dialog for applet installation
     * errors. 
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param e Exception from the global platform service
     * @return GUI action to display the warning dialog
     */
    private Action applet_install_failed_warning
                                        (final GlobalPlatformException e) {
        return new Action() {
            public void run() {
                gui_actions.gui_warning(gui,
                                        e.toString(),
                                        "Applet installation failed");
            }
        };
    }


    /**
     * 
     * Gui action to display a warning if the applet file cannot be
     * opened. 
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param e IOException from the global platform service
     * @return GUI action to display the warning dialog
     */
    private Action applet_cap_file_warning(final IOException e) {
        return new Action() {
            public void run() {
                gui_actions.gui_warning(gui,
                                        e.toString(),
                                        "Applet Cap File Error");
            }
        };
    }


    /**
     * 
     * Terminal thread action for installing one of the applets. 
     * <P>
     *
     * The action might throw a {@link Runtime_card_exception} for
     * low-level communication problems.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param applet_type the applet to install
     * @return the action performing the task
     * @throws Runtime_card_exception if the operation is
     * terminated because of a {@link CardException}
     */
    private Action install_applet_action(final Applet_type applet_type) 
    {
        return new Action() {
            public void run() {
                Gui_state.out.println("start install applet " + applet_type);
                gui_progress_message("install applet " + applet_type + "\n", 
                                   false);

                try {
                    card_protocols.install_applet(applet_type, messages);
                    Gui_state.out.println("Install finished");
                    gui_progress_message("Applet installation finished\n\n", 
                                       true);
                }
                catch(GlobalPlatformInstallForLoadException e) {
                    publish(applet_install_failed_warning(e));
                }
                catch(GlobalPlatformLoadException e) {
                    publish(applet_install_failed_warning(e));
                }
                catch(IOException e) {
                    publish(applet_cap_file_warning(e));
                }
                catch(CardException e) {
                    throw new Runtime_card_exception(e);
                }

                Gui_state.out.println("end install applet");

                set_successor_action(wait_in_ptls_office);
            }
        };
    }


    /**
     * 
     * Set the applet install action for the terminal thread. Only to
     * be called from the GUI thread.
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param type the applet to install
     */
    public void start_install_applet(Applet_type type) {
        Gui_state.out.println("set install applet action");
        set_next_action(install_applet_action(type));
    }


    //########################################################################
    //########################################################################
    // 
    // Delete applets
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Terminal thread action to delete some applets/packages and
     * update the applet list dialog afterwards.
     * <P>
     *
     * The action may throw a {@link Runtime_card_exception} because
     * of a low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param aids applets/packages to delete
     * @param everything if true display all card entries in the
     * applet list dialog, otherwise filter default entries out
     * @return Action to perform the deletion
     */
    private Action delete_applets(final Printable_aid[] aids, 
                                  final boolean everything) 
    {
        return new Action() {
            public void run() {
                Gui_state.out.println(String.format("Delete %d applets", 
                                                    aids.length));

                try {
                    card_protocols.delete_applets(aids, messages);
                }
                catch(CardException e) {
                    throw new Runtime_card_exception(e);
                }

                Gui_state.out.println("applet deletion finished");

                set_successor_action(card_status(everything));
            }
        };
    }


    /**
     * 
     * Set applet/package deletion as next action in the terminal
     * thread. Only to be called from the GUI thread.
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param aids applets/packages to delete
     * @param everything if true display all card entries in the
     * applet list dialog, otherwise filter default entries out
     */
    public void start_delete_applets(Printable_aid[] aids, 
                                     boolean everything) 
    {
        Gui_state.out.println("set applet delete action");
        set_next_action(delete_applets(aids, everything));
    }


    //########################################################################
    //########################################################################
    // 
    // Personalize applets & Reinstall/personalize & Reset
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Terminal thread action to personalize an applet.
     * <P>
     *
     * The action might throw a {@link Runtime_card_exception} for a
     * low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param applet_type the applet to personalize
     * @return the action that will run the personalization in the
     * terminal thread
     */
    private Action personalize_action(final Applet_type applet_type) 
    {
        return new Action() {
            public void run() {
                try {
                    card_protocols.personalize(applet_type, messages);
                    gui_progress_message("Personalization finished\n\n", true);
                }
                catch(Applet_selection_exception e) {
                    publish(invalid_card_warning("Applet selection failed"));
                }
                catch(CardException e) {
                    throw new Runtime_card_exception(e);
                }
                set_successor_action(wait_in_ptls_office);
            }
        };
    }
    

    /**
     * 
     * Set personalization as next action of the terminal thread.
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param applet_type the applet to personalize
     */
    public void start_personalize(Applet_type applet_type) {
        Gui_state.out.println("start personalize");
        set_next_action(personalize_action(applet_type));
    }


    /**
     * 
     * Terminal thread action for reinstalling the applet with
     * subsequent personalization. 
     * <P>
     *
     * The action might throw a {@link Runtime_card_exception} for a
     * low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param applet_type the applet to reinstall and personlize
     * @return the action that performs these tasks in the terminal
     * thread
     */
    private Action reinstall_personalize(final Applet_type applet_type) 
    {
        return new Action() {
            public void run() {
                try {
                    card_protocols.reinstall_personalize(applet_type, 
                                                         messages);
                    gui_progress_message
                        ("Installation & Personalization finished\n\n", true);
                }
                catch(GlobalPlatformInstallForLoadException e) {
                    publish(applet_install_failed_warning(e));
                }
                catch(GlobalPlatformLoadException e) {
                    publish(applet_install_failed_warning(e));
                }
                catch(IOException e) {
                    publish(applet_cap_file_warning(e));
                }
                catch(CardException e) {
                    throw new Runtime_card_exception(e);
                }
                set_successor_action(wait_in_ptls_office);
            }
        };
    }


    /**
     * 
     * Set reinstall + personalize as next action of the terminal
     * thread. 
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param applet_type the applet to reinstall and personalize
     */
    public void start_reinstall_personalize(Applet_type applet_type) {
        Gui_state.out.println("start reinstall personalize");
        set_next_action(reinstall_personalize(applet_type));
    }


    /**
     * 
     * Terminal thread action to reset an applet.
     * <P>
     *
     * The action might throw a {@link Runtime_card_exception} for a
     * low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param applet_type the applet to reset
     */
    private Action reset_action(final Applet_type applet_type) {
        return new Action() {
            public void run() {
                try {
                    card_protocols.reset_applet(applet_type);
                    gui_progress_message("Applet reset.\n\n", true);
                }
                catch(Applet_selection_exception e) {
                    publish(invalid_card_warning("Applet selection failed"));
                }
                catch(CardException e) {
                    throw new Runtime_card_exception(e);
                }
                set_successor_action(wait_in_ptls_office);
            }
        };
    }
    

    /**
     * 
     * Set personalization as next action of the terminal thread.
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param applet_type the applet to reset
     */
    public void start_reset_applet(Applet_type applet_type) {
        Gui_state.out.println("start reset");
        set_next_action(reset_action(applet_type));
    }


    //########################################################################
    //########################################################################
    // 
    // PTLS Automaton role
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * GUI action to enable or disable the obtain-new-signature button
     * in the PTLS automaton tab.
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param b if true enable the button, otherwise disable it
     * @return Action that enables/disables the button in the GUI thread
     */
    private Action gui_set_ptls_automaton_button(final boolean b) {
        return new Action() {
            public void run() {
                gui.ptls_run_button.setEnabled(b);
            }
        };
    }


    /**
     * 
     * Terminal thread action for waiting in the PTLS automaton tab.
     * The action runs endlessly until interrupted and switches the
     * PTLS automaton button on and off as cards are inserted and
     * removed. 
     * 
     */
    private Action wait_in_ptls_automaton =
        new Action() {
            public void run() {
                Gui_state.out.println("waiting in ptls automaton");
                
                try {
                    while(true) {
                        wait_for_card();
                        publish(gui_set_ptls_automaton_button(true));
                        wait_until_card_is_removed();
                        publish(gui_set_ptls_automaton_button(false));
                    }
                }
                catch(InterruptedException e) {}

                Gui_state.out.println("leaving ptls automaton");
            }
        };


    /**
     * 
     * Set the waiting action of the PTLS automaton as next action.
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     */
    public void wait_in_ptls_automaton() {
        set_next_action(wait_in_ptls_automaton);
    }
    

    /**
     * 
     * GUI action to display a warning when resigning fails.
     */
    private Action resign_failed_warning =
        new Action() {
            public void run() {
                gui_actions.gui_warning(gui, "Resigning failed", 
                                        "PTLS Automaton");
            }
        };


    /**
     * 
     * GUI action for enabling the PTLS automaton run button and
     * display a finished message in the status line.
     */
    private Action resign_finished_action =
        new Action() {
            public void run() {
                gui.ptls_run_button.setEnabled(true);
                gui.status_line.setText("Resign finished.");
            }
        };


    /**
     * 
     * Terminal thread action for resigning. 
     * <P>
     *
     * The action might throw a {@link Runtime_card_exception} for a
     * low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param applet_type the applet that should be selected for
     * resigning 
     * @param updates user selected attribute updates or null for
     * using random attribute updates
     * @return the action for doing the resign in the terminal thread
     */
    private Action resign_action(final Applet_type applet_type,
                                 final BigInteger[] updates) 
    {
        return new Action() {
            public void run() {
                try {
                    if(!card_protocols.resign_with_update(applet_type,
                                                          updates,
                                                          messages))
                        {
                            publish(resign_failed_warning);
                        }
                }
                catch(Applet_selection_exception e) {
                    publish(invalid_card_warning("Applet selection failed"));
                }
                catch(CardException e) {
                    throw new Runtime_card_exception(e);
                }

                publish(resign_finished_action);
                gui_progress_message("Resign finished. " +
                                       "You can now remove the card.\n\n",
                                       true);

                set_successor_action(wait_in_ptls_automaton);
            }
        };
    }


    /**
     * 
     * Set the resign action as next action to execute in the terminal
     * thread. 
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param applet_type the applet to select for resigning
     * @param updates user selected attribute updates or null for
     * using random attribute updates
     */
    public void start_resign(Applet_type applet_type, BigInteger[] updates) {
        set_next_action(resign_action(applet_type, updates));
    }


    //########################################################################
    //########################################################################
    // 
    // Entry gate role
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * GUI action to display a warning when the entry gate check
     * failed. 
     */
    private Action proof_failed_warning =
        new Action() {
            public void run() {
                gui_actions.gui_warning(gui, "Entry gate check failed", 
                                        "Entry gate ");
            }
        };


    /**
     * 
     * Run the entry-gate proof-protocol on the card. 
     * <P>
     *
     * Must be executed on the Terminal thread.
     * 
     * @param applet_type the applet to select and to run the protocol
     * with 
     * @throws CardException on low-level communication errors
     */
    private void proof(Applet_type applet_type) 
        throws CardException
    {
        if(card_protocols.entry_gate_check(applet_type, messages)) {
            gui_progress_message("Gate check succeeded. Open gate.\n\n", true);
        }
        else {
            gui_progress_message("Check failed. Gates stay closed.\n\n", true);
            publish(proof_failed_warning);
        }
        publish(gui_proof_finished);
    }


    /**
     * 
     * Exception wrapper for {@link #proof proof}. Runs the entry-gate
     * protocol and turn various exceptions into gui visible warnings.
     * <P>
     *
     * Must be executed on the Terminal thread.
     * 
     * @param applet_type the applet to select and to run the protocol
     * with 
     * @throws Runtime_card_exception in case of unhandled
     * communication errors
     */
    private void proof_ex(Applet_type applet_type)
        throws Runtime_card_exception
    {
        try {
            proof(applet_type);
        }
        catch(Applet_selection_exception e) {
            publish(invalid_card_warning("Applet selection failed"));
        }
        catch(CardException e) {
            String err_msg = null;
            String ex_msg = e.getMessage();

            final String card_not_transacted = "SCARD_E_NOT_TRANSACTED";
            int start = ex_msg.length() - card_not_transacted.length();
            start = start >= 0 ? start : 0;
            if(ex_msg.substring(start).equals(card_not_transacted)) {
                err_msg = 
                    "Card communication failed with SCARD_E_NOT_TRANSACTED.";
            }

            final String card_removed = "SCARD_W_REMOVED_CARD";
            start = ex_msg.length() - card_removed.length();
            start = start >= 0 ? start : 0;
            if(ex_msg.substring(start).equals(card_removed)) {
                err_msg = 
                    "Card communication failed with SCARD_W_REMOVED_CARD.";
            }

            if(ex_msg.equals("connect() failed")) {
                err_msg = "Connect to card failed.";
            }

            if(err_msg != null) {
                Gui_state.out.println("card communication error: " + err_msg);
                publish(card_communication_error_warning(err_msg));
            }
            else {
                throw new Runtime_card_exception(e);
            }
        }
    }


    /**
     * 
     * GUI action to enable/disable the run button on the entry gate
     * tab. 
     * <P>
     *
     * Should be executed on the Terminal thread.
     * 
     * @param b enable the button if true, otherwise disable the button
     * @return GUI action for chaning the button
     */
    private Action gui_set_entry_run_button(final boolean b) {
        return new Action() {
            public void run() {
                gui.entry_run_button.setEnabled(b);
            }
        };
    }


    /**
     * 
     * Terminal thread action for waiting in the entry gate tab. The
     * action runs endlessly until it is interrupted and enables and
     * disables the entry gate controls as cards are inserted and
     * removed. 
     * <P>
     *
     * The action might throw a {@link Runtime_card_exception} for a
     * low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread. The returned actions
     * sould be executed in the terminal thread.
     * 
     * @param automatic_proof if true start the proof protocol
     * automatically when a card is inserted
     * @param applet_type applet to select and to run the proof
     * protocol on
     * @return Terminal thread action for performing these tasks
     */
    private Action wait_in_entry_gate_action(final boolean automatic_proof,
                                             final Applet_type applet_type)
    {
        return new Action() {
            public void run() {
                Gui_state.out.println("waiting in entry gate, automatic " + 
                                      automatic_proof);
                try {
                    while(true) {
                        wait_for_card();
                        if(automatic_proof) {
                            proof_ex(applet_type);
                        }
                        else {
                            publish(gui_set_entry_run_button(true));
                        }
                        wait_until_card_is_removed();
                        publish(gui_set_entry_run_button(false));
                    }
                }
                catch(InterruptedException e) {}

                Gui_state.out.println("finish entry gate");
            }
        };
    }


    /**
     * 
     * Set the entry-gate waiting-action as next action of the
     * terminal thread.
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param automatic_proof if true start the proof protocol
     * automatically when a card is inserted
     * @param applet_type applet to select and to run the proof
     * protocol on
     */
    public void wait_in_entry_gate(Applet_type applet_type,
                                   boolean automatic_proof) 
    {
        set_next_action(wait_in_entry_gate_action(automatic_proof, 
                                                  applet_type));
    }


    /**
     * 
     * Terminal thread action to run the entry-gate proof-protocol.
     * <P>
     *
     * The action might throw a {@link Runtime_card_exception} for a
     * low-level communication problem.
     * <P>
     *
     * Should be executed on the GUI thread.
     * 
     * @param applet_type the applet to select and to run the proof
     * protocol on
     * @return terminal thread action of running the proof protocol
     */
    private Action proof_action(final Applet_type applet_type) 
    {
        return new Action() {
            public void run() {
                proof_ex(applet_type);
                
                set_successor_action(wait_in_entry_gate_action
                           (gui.gate_run_automatically_check_box.isSelected(),
                            applet_type));
            }
        };
    }


    /**
     * 
     * Set the proof-protocol action as next action of the terminal
     * thread. 
     * <P>
     *
     * Must be executed on the GUI thread.
     * 
     * @param applet_type the applet to select and to run the proof
     * protocol on
     */
    public void start_proof(Applet_type applet_type) {
        set_next_action(proof_action(applet_type));
    }

    //########################################################################
    //########################################################################
    // 
    // Main loop of the background terminal thread
    // 
    //########################################################################
    //########################################################################

    /**
     * 
     * Main loop of the terminal thread. Started by the {@link
     * SwingWorker} machinery in a thread different from the GUI
     * thread. The main loop continuously executes the actions it
     * finds in {@link #next_action}. Such actions can be set from the
     * GUI or by actions themselves. 
     * 
     * @return nothing
     */
    public Void doInBackground() {
        thread = Thread.currentThread();
        Action next_action;

        try {
            while(true) {
                Gui_state.out.println("dequeue next action");
                next_action = get_next_action();
                if(next_action == null) {
                    Gui_state.out.println("next action is null!");
                    // Wait until the GUI has settled and set a new 
                    // action.
                    try {
                        Thread.sleep(200);
                    }
                    catch(InterruptedException e) {}
                    next_action = get_next_action();
                    if(next_action == null) {
                        // Still no action? There is something wrong!
                        Gui_state.out.println("next action still null!!!");
                        throw new Exception("no action defined");
                    }
                }
                Gui_state.out.println("run next action");
                next_action.run();
            }
        }
        catch(Throwable e) {
            Throwable t = e;
            while(t != null) {
                t.printStackTrace(Gui_state.out);
                t = t.getCause();
            }
            publish(gui_background_died(e));
        }
        return null;
    }
}
