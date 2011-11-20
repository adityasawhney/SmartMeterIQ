/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ds.ov2.gui;

import java.awt.event.ItemEvent;

/**
 *
 * @author tews
 */
public class Gui_actions {

    public void start_action(Ov_demo_gui gui) {
        System.out.println("global start action");
    }

    public void toolbar_config_button(Ov_demo_gui gui) {
        System.out.println("config window");
    }

    public void select_office(Ov_demo_gui gui) {
        System.out.println("show office tab");
    }

    public void deselect_office(Ov_demo_gui gui) {
        System.out.println("hide office tab");
    }

    public void rsa_key_size_change(Ov_demo_gui gui) {
        System.out.println("rsa key size change");
    }

    public void lenstra_exponent_box_changed(ItemEvent evt, Ov_demo_gui gui) {
        System.out.println("Lenstra check box changed");
    }

    public void ptls_params_load(Ov_demo_gui gui) {
        System.out.println("load ptls parameters");
    }

    public void ptls_params_save(Ov_demo_gui gui) {
        System.out.println("save ptls parameters");
    }

    public void ptls_params_new(Ov_demo_gui gui) {
        System.out.println("generate ptls parameters");
    }

    public void ptls_office_install_all(Ov_demo_gui gui) {
        System.out.println("install and initialize applet");
    }

    public void ptls_office_card_status(Ov_demo_gui gui) {
        System.out.println("card status");
    }

    public void ptls_office_install_applet(Ov_demo_gui gui) {
        System.out.println("install applet");
    }

    public void ptls_office_personalize(Ov_demo_gui gui) {
        System.out.println("personalize applet");
    }

    public void select_automaton(Ov_demo_gui gui) {
        System.out.println("show automaton tab");
    }

    public void deselect_automaton(Ov_demo_gui gui) {
        System.out.println("hide automaton tab");
    }

    public void ptls_automaton_resign_button(Ov_demo_gui gui) {
        System.out.println("resign");
    }

    public void entry_run_button(Ov_demo_gui gui) {
        System.out.println("start entry protocol");
    }

    public void select_entry_gate(Ov_demo_gui gui) {
        System.out.println("show entry gate tab");
    }

    public void deselect_entry_gate(Ov_demo_gui gui) {
        System.out.println("hide entry gate tab");
    }

    public void applet_selection_changed(ItemEvent evt, Ov_demo_gui aThis) {
        System.out.println("applet selection changed");
    }

    public void entry_automatic_changed(Ov_demo_gui gui) {
        System.out.println("proof automatically changed");
    }
}
