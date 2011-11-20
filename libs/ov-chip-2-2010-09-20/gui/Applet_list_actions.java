// 
// OV-chip 2.0 project
// 
// Digital Security (DS) group at Radboud Universiteit Nijmegen
// 
// Copyright (C) 2009
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
// Created 4.5.09 by Hendrik
// 
// Action methods for the Applet list dialog
// 
// $Id: Applet_list_actions.java,v 1.2 2009-05-11 15:23:10 tews Exp $

package ds.ov2.gui;


import java.awt.Component;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/**
 * 
 * GUI actions for the applet list dialog.
 *
 * @author Hendrik Tews
 * @version $Revision: 1.2 $
 * @commitdate $Date: 2009-05-11 15:23:10 $ by $Author: tews $
 * @environment host
 * @CPP no cpp preprocessing needed
 *
 */
public class Applet_list_actions {

    /**
     * 
     * The GUI actions instance.
     */
    private final Gui_actions gui_actions;


    /**
     * 
     * Constructor. Store the GUI actions instance internally to use
     * it later.
     * 
     * @param gui_actions the GUI actions instance
     */
    public Applet_list_actions(Gui_actions gui_actions) {
        this.gui_actions = gui_actions;
    }


    /**
     * 
     * Swing dialog window instance or null if the dialog is currently
     * not open. Initialized in the constructor of {@link
     * Applet_list_window}. Resetting this to null lies in the
     * responsibility of those who control the dialog.
     */
    private Applet_list_window applet_list_window = null;


    /**
     * 
     * Register a new applet list window.
     * 
     * @param applet_list_window the new dialog window
     */
    void register_applet_list_window(Applet_list_window applet_list_window) {
        this.applet_list_window = applet_list_window;
    }


    /**
     * 
     * Reset the {@link #applet_list_window} to null. Must be called,
     * when the dialog is closed.
     * 
     */
    void reset_applet_list_window() {
        applet_list_window = null;
    }


    /**
     * 
     * Return the applet list window or throw an assertion if no such
     * window is registered.
     * 
     * @return the applet list window instance
     */
    Applet_list_window get_applet_list_window() {
        assert applet_list_window != null;
        return applet_list_window;
    }



    /**
     * 
     * Action for the "Delete applets" button. Tell the background
     * thread to delete the selected applets/packages from the card.
     * 
     */
    public void delete_selected_applets(Applet_list_window alw) {
        System.out.println("delete selected applets");

        DefaultTableModel tm = 
            (DefaultTableModel)(alw.applet_list_applet_table.getModel());
        int rows = tm.getRowCount();
        int selection_count = 0;
        for(int i = 0; i < rows; i++)
            if((Boolean)(tm.getValueAt(i, 2)))
                selection_count++;

        if(selection_count == 0) {
            gui_actions.gui_warning(alw, "No applets selected for deletion!", 
                                    "Nothing to delte");
            return;
        }

        Printable_aid[] aids = new Printable_aid[selection_count];
        int aid_index = 0;
        for(int i = 0; i < rows; i++) {
            if((Boolean)(tm.getValueAt(i, 2))) {
                aids[aid_index++] = (Printable_aid)(tm.getValueAt(i, 1));
            }
        }

        gui_actions.terminal_thread.
            start_delete_applets(aids,
                                 alw.applet_list_show_all_check_box
                                 .isSelected());
    }


    /**
     * 
     * Close the applet list dialog.
     * 
     * @param alw the applet list dialog
     */
    public void close(Applet_list_window alw) {
        System.out.println("applet_list close");
        alw.dispose();
    }
    

    /**
     * 
     * Reread the status of the card.
     * 
     * @param alw the applet list dialog
     */
    void refresh(Applet_list_window alw) {
        System.out.println("refresh");

        gui_actions.terminal_thread.start_card_status_action
            (alw.applet_list_show_all_check_box.isSelected());
    }


    /**
     * 
     * Select or deselect all applets/packages for deletion.
     * 
     * @param alw the applet list dialog
     * @param select if true select, if false deselect
     */
    void select_all(Applet_list_window alw, boolean select) {
        System.out.println("select all");
        DefaultTableModel tm = 
            (DefaultTableModel)(alw.applet_list_applet_table.getModel());
        int rows = tm.getRowCount();
        for(int i = 0; i < rows; i++)
            tm.setValueAt(select, i, 2);
    }

}