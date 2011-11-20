/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ds.ov2.gui;

/**
 *
 * @author tews
 */
public class Applet_list_actions {
    private Applet_list_window applet_list_window = null;

    Applet_list_actions(Object /* Gui_actions */ gui_actions) {
    }

    public void delete_selected_applets(Applet_list_window alw) {
            System.out.println("delete selected applets");
    }

    public void close(Applet_list_window alw) {
            System.out.println("close");
    }

    void refresh(Applet_list_window aThis) {
        System.out.println("refresh");
    }

    void register_applet_list_window(Applet_list_window applet_list_window) {
        this.applet_list_window = applet_list_window;
    }

    void select_all(Applet_list_window alw, boolean select) {
        System.out.println("select all");
    }

}
