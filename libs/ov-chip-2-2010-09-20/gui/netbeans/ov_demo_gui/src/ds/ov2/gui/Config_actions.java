/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ds.ov2.gui;

import javax.swing.JTextField;

/**
 *
 * @author tews
 */
public class Config_actions {

    public Object[] get_card_readers() {
        return new Object[0];
    }

    public void ok_button(Config_window cw) {
        System.out.println("config ok");
    }

    public void dismiss_button(Config_window cw) {
        System.out.println("config dismiss");
    }

    void applet_browse_button(Config_window cw, JTextField applet_name_text_field) {
        System.out.println("browsing for applet");
    }
}
