/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.supplier.tariff;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author sawhneya
 */
public class Tarrif {
    private Hashtable<String, String> tarrif = new Hashtable<String, String>();

    public void addTarrif(String slot, String value) {
        tarrif.put(slot, value);
    }

    public Hashtable<String, String> getTarrifs() {
        return tarrif;
    }

    public String getTarrif(String timeSlot) {
        return tarrif.get(timeSlot);
    }

    public String getTimeSlots() {
        StringBuilder slots = new StringBuilder();
        Enumeration e = tarrif.keys();
        concatenate(slots, e);
        return slots.toString();
    }

    public String getTariffValues() {
        StringBuilder values = new StringBuilder();
        Enumeration e = tarrif.elements();
        concatenate(values, e);
        return values.toString();
    }

    private void concatenate(StringBuilder values, Enumeration e) {
        while (e.hasMoreElements()) {
            values.append(e.nextElement());
            if (e.hasMoreElements()) {
                values.append(",");
            }
        }
    }
}
