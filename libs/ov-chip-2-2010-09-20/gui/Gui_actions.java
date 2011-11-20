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
// Created 18.12.08 by Hendrik
// 
// actions from the GUI
// 
// $Id: Gui_actions.java,v 1.29 2010-03-12 15:40:21 tews Exp $

package ds.ov2.gui;

import java.util.concurrent.atomic.AtomicBoolean;
import java.math.BigInteger;
import java.text.ParseException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidParameterException;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.BadLocationException;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;


import ds.ov2.util.BigIntUtil;
import ds.ov2.util.Security_parameter;
import ds.ov2.util.Card_terminal;
import ds.ov2.front.PTLS_rsa_parameters;
import ds.ov2.front.PTLS_rsa_parameters.PTLS_io_exception;
import ds.ov2.front.Applet_type;
import ds.ov2.gui.Ov_demo_gui.Applet_type_item;


/** 
 * Actions for the main window of the graphical demonstrator.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.29 $
 * @commitdate $Date: 2010-03-12 15:40:21 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 */
public class Gui_actions {

    /**
     * 
     * Card protocol instance.
     */
    private final Card_protocols card_protocols;

    /**
     * 
     * Actions for the config dialog.
     */
    private final Config_actions config_actions = new Config_actions();

    /**
     * 
     * Actions for the base selection dialog.
     */
    private final Bases_actions bases_actions = new Bases_actions();


    /**
     * 
     * Actions for the applet list dialog.
     */
    private final Applet_list_actions applet_list_actions = 
        new Applet_list_actions(this);


    /**
     * 
     * Terminal thread instance.
     */
    public Terminal_thread terminal_thread = null;


    /**
     * 
     * The progress window. Initialized from the outside from
     * {@link Ov_demo}, because this class and {@link
     * Progress_messages} are mutually dependent and an instance of
     * this class is created first.
     */
    public Progress_messages progress_window = null;


    /**
     * 
     * Constructor. Remembers the card protocols instance locally.
     * Does not start the terminal thread, because the GUI instance is
     * not available here, see {@link #select_office select_office}.
     * 
     * @param card_protocols instance with card and terminal actions
     */
    public Gui_actions(Card_protocols card_protocols) {
        this.card_protocols = card_protocols;
    }


    //########################################################################
    //########################################################################
    // 
    // general top level actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * GUI initialization order seems to depend in some parts on the
     * order in which the GUI elements have been created in the
     * netbeans GUI. Further it works bottom-up, that is the
     * applet-type combobox starts event processing before the
     * PTLS-office-tabulator a component-shown event. The following
     * boolean is used to disable some actions in some event
     * processing routines that would otherwise run before GUI
     * initialization is complete, see {@link #default_initialization
     * default_initialization}. 
     */
    private boolean initialization_finished = false;


    /**
     * 
     * Default initialization of the internal state. Runs when the
     * main window is first displayed. Some GUI elements might trigger
     * event processing code before that point. Such code is therefore
     * disabled via {@link #initialization_finished}. This method sets
     * {@link Gui_state#card_terminal}, starts the terminal thread and
     * sets its first action and initializes the key size spinners
     * acording to the default applet type. At the very end
     * appropriate actions are executed to satisfy the command line
     * options.
     * 
     * @param gui the GUI window
     */
    void default_initialization(Ov_demo_gui gui) {
        // set default card terminal
        if(Gui_state.card_terminal == null)
            Gui_state.card_terminal = config_actions.get_default_reader(gui);

        // Start the card reader background thread if not yet running.
        if(terminal_thread == null) {
            terminal_thread = new Terminal_thread(gui, this, card_protocols);
            terminal_thread.execute();
        }

        // gui.role_tabbed_pane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        gui.tool_bar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("typed q"), "quit action");
        gui.tool_bar.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke("control Q"), "quit action");
        AbstractAction quit_action = new AbstractAction("quit action") {
                /* Field for the serial version UID warning. */
                public static final long serialVersionUID = 1L;
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            };
        gui.tool_bar.getActionMap().put("quit action", quit_action);


        System.out.println("Initialization finished");
        initialization_finished = true;

        if(gui.role_tabbed_pane.getSelectedIndex() != 0)
            gui.role_tabbed_pane.setSelectedComponent(gui.ptls_office_tab);
        else
            select_office(gui);

        // Set key size spinner for the default applet type.
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                    getSelectedItem())).type;
        gui.key_size_spinner.setValue(applet_type.minimal_base_size());

        if(Gui_state.ptls_params_file.ref != null)
            ptls_params_load_from_file(gui, Gui_state.ptls_params_file.ref);
    }


    /**
     * 
     * Run on application startup. Organizes default initialization as
     * far as necessary.
     * 
     * @param gui the GUI window
     */
    void start_action(Ov_demo_gui gui) {
        Gui_state.out.println("START APPLICATION");

        default_initialization(gui);
    }


    /**
     * 
     * Show a warning message to the user and wait until he
     * acknowledges. 
     * 
     * @param parent_window the parent window of the warning
     * @param message warning to show
     * @param title title of the warning dialog
     */
    public void gui_warning(Component parent_window, String message,
                            String title) {
        JOptionPane.showMessageDialog(parent_window,
                                      message,
                                      title,
                                      JOptionPane.WARNING_MESSAGE);
    }


    /**
     * 
     * Show an error message to the user and wait until he
     * acknoledges.
     * 
     * @param parent_window the parent window of the warning
     * @param message warning to show
     * @param title title of the warning dialog
     */
    public void gui_error(Component parent_window, String message,
                          String title) {
        JOptionPane.showMessageDialog(parent_window,
                                      message,
                                      title,
                                      JOptionPane.ERROR_MESSAGE);
    }


    /**
     * 
     * Clear a message pane.
     * 
     * @param pane the text pane to clear
     */
    public void clear_message_pane(JTextPane pane) {
        Document d = pane.getDocument();
        try {
            d.remove(0, d.getLength());
        }
        catch(BadLocationException e) {
            assert false;
        }
    }


    //########################################################################
    //########################################################################
    // 
    // toolbar actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Change our state to match to the new configuration. Called when
     * the config dialog is closed with the OK button.
     * 
     * @param cw the just closed config window.
     */
    void apply_config(Config_window cw) {
        Gui_state.out.println("apply new config");

        Object item = cw.card_terminal_combo_box.getSelectedItem();
        assert item instanceof CardTerminal;
        Gui_state.card_terminal = (CardTerminal)item;

        Gui_state.square_4_applet_file_name = 
            cw.config_square_4_applet_name_text_field.getText();
        Gui_state.square_2_applet_file_name = 
            cw.config_square_2_applet_name_text_field.getText();
        Gui_state.plain_applet_file_name = 
            cw.config_plain_applet_name_text_field.getText();
        Gui_state.mont_applet_file_name = 
            cw.config_mont_applet_name_text_field.getText();
        Gui_state.warn_about_invalid_key_sizes =
            cw.config_invalid_key_size_warning_check_box.isSelected();
        Gui_state.warn_about_long_key_sizes =
            cw.config_long_key_size_warning_check_box.isSelected();

        Gui_state.out.format("set terminal %s\n" +
                             "set square 4 applet %s\n" +
                             "set square 2 applet %s\n" +
                             "set plain applet %s\n" +
                             "set mont applet %s\n" +
                             "warn about invalid key sizes %s\n" +
                             "warn about long key sizes %s\n",
                             Gui_state.card_terminal,
                             Gui_state.square_4_applet_file_name,
                             Gui_state.square_2_applet_file_name,
                             Gui_state.plain_applet_file_name,
                             Gui_state.mont_applet_file_name,
                             Gui_state.warn_about_invalid_key_sizes,
                             Gui_state.warn_about_long_key_sizes);
    }


    /**
     * 
     * Method to run when the config button in the toolbar is pressed.
     * Creates and displays the config dialog and changes the program
     * state when the config window is closed.
     * 
     * @param gui the GUI window
     */
    void toolbar_config_button(Ov_demo_gui gui) {
        // Create the config window.
        final Config_window cw = new Config_window(gui, true, config_actions);

        // Copy current configuration into config window.

        DefaultComboBoxModel reader_selection_model =
            (DefaultComboBoxModel)cw.card_terminal_combo_box.getModel();
        for(int i = 0; i < reader_selection_model.getSize(); i++) {
            if(Gui_state.card_terminal.equals
               (reader_selection_model.getElementAt(i))) 
                {
                    reader_selection_model.setSelectedItem
                        (Gui_state.card_terminal);
                    break;
                }
        }
                

        cw.config_square_4_applet_name_text_field.
            setText(Gui_state.square_4_applet_file_name);
        cw.config_square_2_applet_name_text_field.
            setText(Gui_state.square_2_applet_file_name);
        cw.config_plain_applet_name_text_field.
            setText(Gui_state.plain_applet_file_name);
        cw.config_mont_applet_name_text_field.
            setText(Gui_state.mont_applet_file_name);
        cw.config_invalid_key_size_warning_check_box.setSelected
            (Gui_state.warn_about_invalid_key_sizes);
        cw.config_long_key_size_warning_check_box.setSelected
            (Gui_state.warn_about_long_key_sizes);

        // Set the close action.
        cw.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    cw.dispose();
                }
            });

        // Make the window visible. This waits until the user finishes the 
        // config dialog.
        cw.setVisible(true);

        // Apply the new configuration, if user left via the OK button.
        if(cw.finished_ok)
            apply_config(cw);
    }


    //########################################################################
    //########################################################################
    // 
    // Progress window actions
    // 
    //########################################################################
    //########################################################################

    /**
     * 
     * Action for the progress log button in the toolbar. Makes the
     * progress window visible.
     * 
     */
   public void toolbar_progress_button() {
        progress_window.setVisible(true);
    }
 


    /**
     * 
     * Action for the dismiss button of the progress window. Makes the
     * progress window invisible.
     * 
     */
    public void progress_dismiss_button() {
        progress_window.setVisible(false);
    }




    //########################################################################
    //########################################################################
    // 
    // Tab selection actions
    // 
    //########################################################################
    //########################################################################

    // The tabs represent different applications, which, in principle, 
    // should run in different locations. When a tab is selected we
    // have to change our role.

    /**
     * 
     * Method to be executed when the PTLS office tab is selected.
     * Interrupts the terminal thread to change accordingly.
     * 
     * @param gui the GUI window
     */
    void select_office(Ov_demo_gui gui) {
        // Gui_state.role = Gui_state.PTLS_OFFICE;
        Gui_state.out.println("show office tab");

        if(!initialization_finished)
            return;

        terminal_thread.wait_in_ptls_office();
    }


    /**
     * 
     * Method to be called when the user tries to select a different
     * tab without having configured a set of PTLS parameters.
     * Displays an error message and changes back to the PTLS office
     * tab. 
     * 
     * @param gui the GUI window
     */
    void ptls_parameters_missing(Ov_demo_gui gui) {
        gui_error(gui,
                  "Cannot change role without valid system parameters!\n" +
                  "Load or create them in the PTLS Office tab.",
                  "tab selection error");
        gui.role_tabbed_pane.setSelectedComponent(gui.ptls_office_tab);
    }


    /**
     * 
     * Method to be called when the PTLS automaton tab is selected.
     * Interrupts the terminal thread to change accordingly.
     * 
     * @param gui the GUI window
     */
    void select_automaton(Ov_demo_gui gui) {
        Gui_state.out.println("show automaton tab");

        if(!card_protocols.valid_parameters()) {
            ptls_parameters_missing(gui);
            return;
        }

        terminal_thread.wait_in_ptls_automaton();
    }


    /**
     * 
     * Method to be called when the entry gate tab is selected.
     * Interrupts the terminal thread to change accordingly.
     * 
     * @param gui the GUI window
     */
    void select_entry_gate(Ov_demo_gui gui) {
        Gui_state.out.println("show entry gate tab");

        if(!card_protocols.valid_parameters()) {
            ptls_parameters_missing(gui);
            return;
        }

        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;

        if(terminal_thread.card_is_present)
            gui.gate_run_automatically_check_box.setSelected(false);

        terminal_thread.wait_in_entry_gate
            (applet_type,
             gui.gate_run_automatically_check_box.isSelected());
    }


    //########################################################################
    //########################################################################
    // 
    // System parameter actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Called when the applet type is changed in the applet type
     * combobox. Checks and, if necessary, adjusts the key sizes and
     * invalidates the PTLS parameters. The last point is necessary
     * because the PTLS parameters contain data structures that depend
     * on the number of Montgomery digits and therefore on the applet
     * type. 
     * 
     * @param e item event of the combo box
     * @param gui the GUI window
     */
    public void applet_selection_changed(ItemEvent e, Ov_demo_gui gui) {
        if(e.getStateChange() == ItemEvent.SELECTED) {
            int key_size = int_of_number_spinner(gui.key_size_spinner);

            int adjusted_val = check_key_size(gui, key_size);
            if(key_size != adjusted_val) {
                gui.key_size_spinner.setValue(adjusted_val);
            }

            ptls_params_invalidate_action(gui);
        }
    }


    /**
     * 
     * Method to be called when the automatic exponent size check box
     * in the PTLS office tab is selected. Adjusts the exponent size
     * to match the security level of the RSA key size, using A.
     * Lenstra's estimations.
     * 
     * @param e event from the check box, telling us whether it got
     * selected 
     * @param gui the GUI window
     */
    public void lenstra_exponent_box_changed(ItemEvent e, 
                                             Ov_demo_gui gui) {
        if(e.getStateChange() == ItemEvent.SELECTED)
            rsa_key_size_change(gui);
    }


    /**
     * 
     * Read out the value of a spinner. We have spinners holding int's
     * and {@link BigInteger BigInteger's}. Therefore this method
     * returns some super class, which must be downcast appropriately.
     * 
     * @param spinner the spinner to read out
     */
    public Number number_of_number_spinner(JSpinner spinner) {
        assert spinner.getModel() instanceof SpinnerNumberModel;
        return ((SpinnerNumberModel)(spinner.getModel())).getNumber();
    }


    /**
     * 
     * Read the value of a spinner holding an integer.
     * 
     * @param spinner the spinner to read out
     */
    public int int_of_number_spinner(JSpinner spinner) {
        return
            number_of_number_spinner(spinner).intValue();
    }


    /**
     * 
     * Read the value of a spinner holding a {@link BigInteger}.
     * 
     * @param spinner the spinner to read out
     */
    public BigInteger bigint_of_number_spinner(JSpinner spinner) {
        Number n = number_of_number_spinner(spinner);
        assert n instanceof BigInteger;
        return (BigInteger)n;
    }


    /**
     * 
     * Check whether the user selected {@code user_key_size} makes
     * sense for the current applet type. If not warnings are
     * displayed and, at the users options, the key size is adjusted. 
     * 
     * @param gui the GUI window
     * @param user_key_size the key size the user selected
     * @return adjusted key size or user_key_size if no adjustments
     * were made
     */
    public int check_key_size(Ov_demo_gui gui, int user_key_size) {
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                    getSelectedItem())).type;

        // For those applets that use the RSA cipher the length of the
        // RSA modulus in bytes must be divisible by 4 (and the
        // modulus must not have a leading zero byte). For the
        // squaring applet the highest bit of the modulus must be zero
        // otherwise addition could overflow somewhere in the applet.
        int mod = user_key_size % 32;

        switch(applet_type) {
        case PLAIN_RSA_APPLET:
            if(Gui_state.warn_about_invalid_key_sizes &&
               (user_key_size < 505 || user_key_size > 1952 || 
                (mod != 0 && mod < 25)))
                {
                    int res = JOptionPane.showConfirmDialog
                        (gui, 
                         "For the plain coprocessor applet the key size " +
                         "must be between 505 and 1952 bits.\n" +
                         "Further, the remainder when dividing by 32 " +
                         "must be either 0 or grater than 24.\n" +
                         "Shall I adopt the key size?",
                         "Invalid key size", 
                         JOptionPane.YES_NO_OPTION,
                         JOptionPane.WARNING_MESSAGE);
                    // res == 0 for yes   res == 1 for no
                    if(res == 0) {
                        if(user_key_size < 505)
                            user_key_size = 505;
                        else if(user_key_size > 1952)
                            user_key_size = 1952;
                        else if(mod > 12)
                            user_key_size += 25 - mod;
                        else 
                            user_key_size -= mod;

                        // Now we changed the size and this method will 
                        // therefore be called again (with the changed
                        // size). Therefore leave out all following checks
                        // and warnings, because they are otherwise
                        // possibly displayed twice.
                        return user_key_size;
                    }
                }
            if(Gui_state.warn_about_long_key_sizes && user_key_size > 576) {
                int res = JOptionPane.showConfirmDialog
                    (gui, 
                     "With the selected key size the transaction time will " +
                     "be rather long (> 2 minutes).\n" +
                     "Shall I adjust the key size such that the transaction " +
                     "times are below 2 minutes?", 
                     "Long transaction times", 
                     JOptionPane.YES_NO_OPTION,
                     JOptionPane.WARNING_MESSAGE);
                // res == 0 for yes   res == 1 for no
                if(res == 0)
                    user_key_size = 576;
            }
            return user_key_size;

        case SQUARED_RSA_APPLET:
            if(Gui_state.warn_about_invalid_key_sizes && 
               (user_key_size < 505 || user_key_size > 1951 || mod < 25)) {
                int res = JOptionPane.showConfirmDialog
                    (gui, 
                     "For the plain coprocessor applet the key size must be " +
                     "between 505 and 1951 bits.\n" +
                     "Further, the remainder when dividing by 32 " +
                     "must be grater than 24.\n" +
                     "Shall I adopt the key size?",
                     "Invalid key size", 
                     JOptionPane.YES_NO_OPTION,
                     JOptionPane.WARNING_MESSAGE);
                // res == 0 for yes   res == 1 for no
                if(res == 0) {
                    if(user_key_size < 505)
                        user_key_size = 505;
                    else if(user_key_size > 1951)
                        user_key_size = 1951;
                    else if(mod > 12)
                        user_key_size += 25 - mod;
                    else 
                        user_key_size -= mod + 1;

                    // Now we changed the size and this method will 
                    // therefore be called again (with the changed
                    // size). Therefore leave out all following checks
                    // and warnings, because they are otherwise
                    // possibly displayed twice.
                    return user_key_size;
                }
            }
            return user_key_size;

        case SQUARED4_RSA_APPLET:
            if(Gui_state.warn_about_invalid_key_sizes && 
               (user_key_size < 505 || user_key_size > 1950 || 
                mod < 25 || mod > 30)) {
                int res = JOptionPane.showConfirmDialog
                    (gui, 
                     "For the plain coprocessor applet the key size must be " +
                     "between 505 and 1950 bits.\n" +
                     "Further, the remainder when dividing by 32 " +
                     "must be grater than 24 and lesser than 31.\n" +
                     "Shall I adopt the key size?",
                     "Invalid key size", 
                     JOptionPane.YES_NO_OPTION,
                     JOptionPane.WARNING_MESSAGE);
                // res == 0 for yes   res == 1 for no
                if(res == 0) {
                    if(user_key_size < 505)
                        user_key_size = 505;
                    else if(user_key_size > 1950)
                        user_key_size = 1950;
                    else if(mod == 31)
                        user_key_size -= 1;
                    else if(mod > 12)
                        user_key_size += 25 - mod;
                    else 
                        user_key_size -= mod + 2;

                    // Now we changed the size and this method will 
                    // therefore be called again (with the changed
                    // size). Therefore leave out all following checks
                    // and warnings, because they are otherwise
                    // possibly displayed twice.
                    return user_key_size;
                }
            }
            return user_key_size;

        case MONT_RSA_APPLET:
            if(Gui_state.warn_about_long_key_sizes && user_key_size > 128) {
                int res = JOptionPane.showConfirmDialog
                    (gui, 
                     "With the selected key size the transaction time will " +
                     "be rather long (> 2 minutes).\n" +
                     "Shall I adjust the key size such that the transaction " +
                     "times are below 2 minutes?", 
                     "Long transaction times", 
                     JOptionPane.YES_NO_OPTION,
                     JOptionPane.WARNING_MESSAGE);
                // res == 0 for yes   res == 1 for no
                if(res == 0)
                    user_key_size = 128;
            }
            return user_key_size;

        default:
            assert false;
            return user_key_size;
        }
    }


    /**
     * 
     * Method to be called when the exponent length must be adjusted to
     * the base langth. This happens when (1) the RSA key size is
     * changed and (2) the automatic exponent size box is checked. If
     * there is an invalid value in the RSA key size spinner it is
     * reverted to last known valid one.
     * 
     * @param gui the GUI window
     */
    public void rsa_key_size_change(Ov_demo_gui gui) {
        try {
            gui.key_size_spinner.commitEdit();
        }           
        catch (ParseException pe) {
            // AFAICS the spinner reverts itself to some valid value.
        }
        assert gui.key_size_spinner.getModel() instanceof SpinnerNumberModel;
        int new_val = int_of_number_spinner(gui.key_size_spinner);

        int adjusted_val = check_key_size(gui, new_val);
        if(new_val != adjusted_val) {
            gui.key_size_spinner.setValue(adjusted_val);
        }

        // Adjust the exponent length only if the check box is ticked.
        if(!gui.lenstra_exponent.isSelected())
            return;

        int new_exp_length = Security_parameter.
            exponent_length_for_modulus_length_full_byte(2009, adjusted_val, 
                                                         null);

        gui.exp_size_spinner.setValue(new Integer(new_exp_length));
        return;
    }


    /**
     * 
     * Method to be called when we obtained some PTLS parameters.
     * 
     * @param gui the GUI window
     */
    public void ptls_params_ready_action(Ov_demo_gui gui) {
        gui.ptls_params_save_button.setEnabled(true);
        if(terminal_thread.card_is_present) {
            gui.ptls_office_install_all_button.setEnabled(true);
            gui.ptls_office_personalize_button.setEnabled(true);
            terminal_thread.gui_card_inserted.run();
        }
        else {
            terminal_thread.gui_wait_for_card.run();
        }

    }


    /**
     * 
     * Method to be called when we lost our PTLS parameters.
     * 
     * @param gui the GUI window
     */
    public void ptls_params_invalidate_action(Ov_demo_gui gui) {
        card_protocols.clear_parameters();

        gui.ptls_params_save_button.setEnabled(false);

        gui.status_line.setText("No system parameters configured.");


        gui.ptls_office_install_all_button.setEnabled(false);
        gui.ptls_office_personalize_button.setEnabled(false);
    }


    /**
     * 
     * Read PTLS parameters from file {@code file}. Adapt the state of
     * the GUI apropriately if successful. Displays an error message
     * if reading fails.
     * 
     * @param gui the GUI window
     * @param file_name the file to read
     */
    public void ptls_params_load_from_file(Ov_demo_gui gui, 
                                           String file_name) {
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;
        boolean read_was_ok = true;
        int sizes[] = new int[0];
        try {
            sizes = card_protocols.read_ptls_parameters(file_name,
                                                        applet_type);
        }
        catch(PTLS_io_exception e) {
            read_was_ok = false;
            gui_error(gui, 
                      String.format("Reading parameters failed: %s",
                                    e.getMessage()),
                      "PTLS Parameter");
        }

        if(read_was_ok) {
            gui.attribute_number_spinner.setValue(new Integer(sizes[0]));
            gui.key_size_spinner.setValue(new Integer(sizes[1]));
            gui.exp_size_spinner.setValue(new Integer(sizes[2]));
            ptls_params_ready_action(gui);
        }
    }


    /**
     * 
     * Method to be called when the Load-from-file button for loading
     * PTLS parameters is pressed. Displays a file chooser and diverts
     * to {@link #ptls_params_load_from_file
     * ptls_params_load_from_file} for the real work.
     * 
     * @param gui the GUI window
     */
    public void ptls_params_load(Ov_demo_gui gui) {
        Gui_state.out.println("load ptls parameters");

        final JFileChooser fc = new JFileChooser(new File("."));
        FileNameExtensionFilter filter =
            new FileNameExtensionFilter("PTLS parameter files", "ptls");
        fc.setFileFilter(filter);

        int ret = fc.showOpenDialog(gui);

        if(ret == JFileChooser.APPROVE_OPTION) {
            String file_name = fc.getSelectedFile().getPath();
            Gui_state.out.format("load from %s\n", file_name);

            ptls_params_load_from_file(gui, file_name);
        }
    }


    /**
     * 
     * Method to be called when Save-to-file button for saving the
     * current PTLS parameters is pressed. Displays a file chooser,
     * saves the parameters and displays an error if anything goes
     * wrong. 
     * 
     * @param gui the GUI window
     */
    public void ptls_params_save(Ov_demo_gui gui) {
        Gui_state.out.println("save ptls parameters");

        final JFileChooser fc = new JFileChooser(new File("."));
        FileNameExtensionFilter filter =
            new FileNameExtensionFilter("PTLS parameter files", "ptls");
        fc.setFileFilter(filter);

        int ret = fc.showOpenDialog(gui);

        if(ret == JFileChooser.APPROVE_OPTION) {
            String file_name = fc.getSelectedFile().getPath();
            Gui_state.out.format("save in %s\n", file_name);

            try {
                card_protocols.save_ptls_parameters(file_name);
            }
            catch(PTLS_io_exception e) {
                gui_error(gui,
                          String.format("Writing parameters failed: %s",
                                        e.getMessage()),
                          "PTLS Parameter");
            }
        }
    }


    /**
     * 
     * Exception for when the base selection dialog is aborted.
     */
    public class Bases_selection_abort_exception extends Exception 
    {
        /**
         * 
         * Field for the serial version UID warning.
         */
        public static final long serialVersionUID = 1L;

        /**
         * 
         * Constructs a new Bases_selection_abort_exception with null
         * as its detail message.
         * 
         */
        public Bases_selection_abort_exception() {
            super();
        }


        /**
         * 
         * Constructs a new Bases_selection_abort_exception with the
         * specified detail message.
         * 
         * @param s the detail message
         */
        public Bases_selection_abort_exception(String s) {
            super(s);
        }


        /**
         * 
         * Constructs a new Bases_selection_abort_exception with the
         * specified detail message and cause.
         * 
         * @param s the detail message
         * @param cause the cause
         */
        public Bases_selection_abort_exception(String s, Throwable cause) {
            super(s, cause);
        }


        /**
         * 
         * Constructs a new Bases_selection_abort_exception with the
         * specified cause and a detail message of {@code (cause==null
         * ? null : cause.toString())} (which typically contains the
         * class and detail message of cause).
         * 
         * @param cause cause
         */
        public Bases_selection_abort_exception(Throwable cause) {
            super(cause);
        }
    }


    /**
     * 
     * Method to be called at the end of PTLS parameter generation
     * when the choose-bases-randomly box is not checked. Displays the
     * bases selection dialog and changes the parameters acordingly
     * afterwards. 
     * 
     * @param gui the GUI window
     * @throws Bases_selection_abort_exception if the base selection
     * dialog is aborted
     * @todo fix the various bugs in this method that make the base
     * selection dialog unusable
     */
    // If the random bases checkbox is not enabled, then set_bases is
    // called after parameter generation to overwrite the bases.
    @SuppressWarnings({"fallthrough"})
    public void set_bases(Ov_demo_gui gui) 
        throws Bases_selection_abort_exception
    {

        // Create the base selection window.
        final Base_selection bs = new Base_selection(gui, true, bases_actions);

        // Throw an exception if the user wants to destroy the window.
        bs.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    bs.dispose();
                }
            });

        // Compute the maximal base value.
        BigInteger base_max = card_protocols.get_max_base();

        BigInteger[] base = card_protocols.get_bases();

        // How many bases do we have?
        int bases = int_of_number_spinner(gui.attribute_number_spinner);
        assert bases == base.length;

        // Disable the spinners for unused bases.
        if(bases <= 4) {
            bs.base_4_label.setEnabled(false);
            bs.base_4_spinner.setEnabled(false);
        }
        else {
            // Spinner 4 is used, set a model then.
            SpinnerNumberModel m = 
                new SpinnerNumberModel(base[4],
                                       BigInteger.ONE,
                                       base_max,
                                       BigInteger.ONE);
            bs.base_4_spinner.setModel(m);
        }
                                       
        if(bases <= 3) {
            bs.base_3_label.setEnabled(false);
            bs.base_3_spinner.setEnabled(false);
        }
        else {
            // Spinner 3 is used, set a model then.
            SpinnerNumberModel m = 
                new SpinnerNumberModel(base[3],
                                       BigInteger.ONE,
                                       base_max,
                                       BigInteger.ONE);
            bs.base_3_spinner.setModel(m);
        }

        if(bases <= 2) {
            bs.base_2_label.setEnabled(false);
            bs.base_2_spinner.setEnabled(false);
        }
        else {
            // Spinner 2 is used, set a model then.
            SpinnerNumberModel m = 
                new SpinnerNumberModel(base[2],
                                       BigInteger.ONE,
                                       base_max,
                                       BigInteger.ONE);
            bs.base_2_spinner.setModel(m);
        }

        if(bases <= 1) {
            bs.base_1_label.setEnabled(false);
            bs.base_1_spinner.setEnabled(false);
        }
        else {
            // Spinner 1 is used, set a model then.
            SpinnerNumberModel m = 
                new SpinnerNumberModel(base[1],
                                       BigInteger.ONE,
                                       base_max,
                                       BigInteger.ONE);
            bs.base_1_spinner.setModel(m);
        }

        // Spinner 0 is always used, set a model then.
        SpinnerNumberModel m = 
            new SpinnerNumberModel(base[0],
                                   BigInteger.ONE,
                                   base_max,
                                   BigInteger.ONE);
        bs.base_0_spinner.setModel(m);

        // Make the base selection frame visible and let the user fill it.
        // If it returns normally, the bases are all OK and the user has 
        // pressed the OK button. The dismiss button throws an execption.
        bs.setVisible(true);
        if(!bs.finished_ok)
            throw new Bases_selection_abort_exception("dialog aborted");

        Gui_state.out.println("bases selection finished");

        // Propagate the user selection back into the PTLS parameters.
        switch(bases) {
        case 5:
            base[4] = bigint_of_number_spinner(bs.base_4_spinner);
        case 4:
            base[3] = bigint_of_number_spinner(bs.base_3_spinner);
        case 3:
            base[2] = bigint_of_number_spinner(bs.base_2_spinner);
        case 2:
            base[1] = bigint_of_number_spinner(bs.base_1_spinner);
        case 1:
            base[0] = bigint_of_number_spinner(bs.base_0_spinner);
        }

        BigIntUtil.print_array(Gui_state.out, "selected %d bases\n", 
                               "base", base);
        card_protocols.update_bases(base);
    }



    /**
     * 
     * Method to be called when the create-new button to generate new
     * PTLS parameters is pressed. This method contains the
     * functionality, exceptions are caught in the exception wrapper
     * {@link #ptls_params_new ptls_params_new} method. 
     * 
     * @param gui the GUI window
     * @throws NoSuchAlgorithmException if no provider for RSA key
     * generation is present
     * @throws Bases_selection_abort_exception if the base selection
     * dialog is aborted. 
     */
    public void ptls_params_new_ex(Ov_demo_gui gui) 
        throws NoSuchAlgorithmException, 
               Bases_selection_abort_exception
    {
        Gui_state.out.println("generate ptls parameters");

        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;

        card_protocols.generate_ptls_parameters(
                int_of_number_spinner(gui.attribute_number_spinner),
                int_of_number_spinner(gui.key_size_spinner),
                int_of_number_spinner(gui.exp_size_spinner),
                applet_type);

        if(!gui.ptls_office_random_bases_box.isSelected())
            set_bases(gui);

        ptls_params_ready_action(gui);
    }


    /**
     * 
     * Possibly report an escaping exception as error message. To be
     * called with {@code ex} being a null reference, if there is no
     * exception. 
     * 
     * @param parent_window parent window for the error dialog
     * @param title title of the error dialog
     * @param ex the escaping exception or null
     * @param ex_name the exception name
     */
    public void report_exception_maybe(Component parent_window,
                                       String title, Exception ex,
                                       String ex_name) {
        if(ex != null) {
            gui_error(parent_window,
                      String.format("Exception %s: %s",
                                    ex_name,
                                    ex.getMessage()),
                      title);
        }
    }


    /**
     * 
     * Method to be called when the create-new button to generate new
     * PTLS parameters is pressed. Exception wrapper of {@link
     * #ptls_params_new_ex ptls_params_new_ex}, which implements the
     * functionality. This method only catches all possible exceptions
     * and displays appropriate error messages.
     * 
     * @param gui the GUI window
     */
    public void ptls_params_new(Ov_demo_gui gui) {
        Exception ex = null;
        String ex_name = null;
        try {
            ptls_params_new_ex(gui);
        }
        catch(NoSuchAlgorithmException e) {
            ex_name = "NoSuchAlgorithm";
            ex = e;
        }
        catch(InvalidParameterException e) {
            ex_name = "InvalidParameter";
            ex = e;
        }
        catch(Bases_selection_abort_exception e) {
            // The user aborted the selection dialog, do nothing.
        }
        report_exception_maybe(gui, "PTLS Parameter Generation Error", 
                               ex, ex_name);
    }



    //########################################################################
    //########################################################################
    // 
    // Card maintenance actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Method to be called when a card is inserted and the PTLS office
     * tab is selected. Enables all the buttons in the PTLS office
     * tab. 
     * 
     * @param gui the GUI window
     */
    public void enable_card_maintenance(Ov_demo_gui gui) {
        gui.ptls_office_card_status_delete_button.setEnabled(true);
        gui.ptls_office_install_button.setEnabled(true);
        gui.ptls_office_reset_button.setEnabled(true);
        if(card_protocols.valid_parameters()) {
            gui.ptls_office_install_all_button.setEnabled(true);
            gui.ptls_office_personalize_button.setEnabled(true);
        }
    }


    /**
     * 
     * Method to be called when a card is removed and the PTLS office
     * tab is selected. Disables all the buttons in the PTLS office
     * tab. 
     * 
     * @param gui the GUI window
     */
    public void disable_card_maintenance(Ov_demo_gui gui) {
        gui.ptls_office_install_all_button.setEnabled(false);
        gui.ptls_office_card_status_delete_button.setEnabled(false);
        gui.ptls_office_install_button.setEnabled(false);
        gui.ptls_office_reset_button.setEnabled(false);
        gui.ptls_office_personalize_button.setEnabled(false);
    }


    /**
     * 
     * Return the applet list window or throw an assertion if no such
     * window is registered.
     * 
     * @return the applet list window instance
     */
    public Applet_list_window get_applet_list_window() {
        return applet_list_actions.get_applet_list_window();
    }


    /**
     * 
     * Method to be called when the status-&-delete button in the PTLS
     * office tab is pressed. Create a new applet list dialog register
     * it with {@link #applet_list_actions} and display it. When
     * finished deregister the window with {@link
     * #applet_list_actions}. 
     * 
     * @param gui the GUI window
     */
    public void ptls_office_card_status(Ov_demo_gui gui) {
        // Create applet deletion window.
        final Applet_list_window alw = 
            new Applet_list_window(gui, true, applet_list_actions);

        // Set the close action.
        alw.addWindowListener(new java.awt.event.WindowAdapter() {
                public void windowClosing(java.awt.event.WindowEvent e) {
                    alw.dispose();
                }
            });

        assert terminal_thread != null;
        terminal_thread.start_card_status_action
            (alw.applet_list_show_all_check_box.isSelected());

        // Make the window visible and wait until the user leaves this dialog.
        alw.setVisible(true);

        // Reset the window field in the actions instance.
        applet_list_actions.reset_applet_list_window();
    }


    /**
     * 
     * Method to be called when the install button is pressed. Tell
     * the terminal window to install the selected applet.
     * 
     * @param gui the GUI window
     */
    public void ptls_office_install_applet(Ov_demo_gui gui) {
        Gui_state.out.println("install applet");

        Applet_type type = ((Applet_type_item)
            gui.applet_choice_combo_model.getSelectedItem()).type;
        
        terminal_thread.start_install_applet(type);
    }


    /**
     * 
     * Method to be called when the applet reset button is pressed.
     * Tell the terminal window to reset the applet.
     * 
     * @param gui the GUI window
     */
    public void ptls_office_reset_applet(Ov_demo_gui gui) {
        Gui_state.out.println("reset applet");

        Applet_type type = ((Applet_type_item)
            gui.applet_choice_combo_model.getSelectedItem()).type;
        
        terminal_thread.start_reset_applet(type);
    }


    /**
     * 
     * Action method for the PTLS office "Personalize" button. Starts
     * the {@link Terminal_thread#personalize_action
     * personalize_action} in the {@link Terminal_thread}, which does
     * the job.
     * 
     */
    public void ptls_office_personalize(Ov_demo_gui gui) {
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;
        terminal_thread.start_personalize(applet_type);
    }
            

    /**
     * 
     * Method to be called when the reinstall-and-personalize button
     * is pressed. Tell the terminal thread to reinstall and
     * personalize. 
     * 
     * @param gui the GUI window
     */
    public void ptls_office_install_all(Ov_demo_gui gui) {
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;
        terminal_thread.start_reinstall_personalize(applet_type);
    }


    //########################################################################
    //########################################################################
    // 
    // PTLS Automaton actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Method to be called when the obtain-new-signature button is
     * pressed in the PTLS automaton tab. Tell the terminal thread
     * about it. 
     * 
     * @param gui the GUI window
     */
    public void ptls_automaton_resign_button(Ov_demo_gui gui) {
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;
        BigInteger[] updates;
        if(gui.ptls_automaton_random_attributes_radio_button.isSelected())
            updates = null;
        else if(gui.ptls_automaton_constant_attributes_radio_button
                .isSelected())
            {
                updates = 
                    new BigInteger[card_protocols.get_attribute_number()];
                for(int i = 0; i < updates.length; i++)
                    updates[i] = BigInteger.ZERO;
            }
        else {
            // The checks above are radio buttons in the same button
            // group, there is always precisely one selected.
            assert false;
            updates = null;
        }
        terminal_thread.start_resign(applet_type, updates);
    }



    //########################################################################
    //########################################################################
    // 
    // Gate actions
    // 
    //########################################################################
    //########################################################################


    /**
     * 
     * Method to be called when the run-entry-protocol button is
     * pressed in the entry gate tab. Tell the terminal thread about
     * it. 
     * 
     * @param gui the GUI window
     */
    public void entry_run_button(Ov_demo_gui gui) {
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;
        terminal_thread.start_proof(applet_type);
    }


    /**
     * 
     * Method to be called when the run-automatically check box
     * changes state in the entry gate tab. Restart the entry gate
     * waiting action of the terminal thread with the new setting. 
     * 
     * @param gui the GUI window
     */
    public void entry_automatic_changed(Ov_demo_gui gui) {
        Applet_type applet_type = 
            ((Applet_type_item)(gui.ptls_params_applet_combobox.
                                getSelectedItem())).type;
        boolean b = gui.gate_run_automatically_check_box.isSelected();

        gui.entry_run_button.setEnabled(!b);

        terminal_thread.wait_in_entry_gate(applet_type, b);
    }

}
