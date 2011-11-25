/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.model;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author sawhneya
 */
public class Bill {
    private String totalAmount;
    private String totalRandomness;
    private Hashtable<String, String> slotCommitments;

    public Bill(
            String totalAmount,
            String totalRandomness,
            Hashtable<String, String> slotCommitments) {
        this.totalAmount = totalAmount;
        this.totalRandomness = totalRandomness;
        this.slotCommitments = slotCommitments;
    }

    public Hashtable<String, String> getSlotCommitments() {
        return slotCommitments;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getTotalRandomness() {
        return totalRandomness;
    }

    public String getTimeSlots() {
        StringBuilder slots = new StringBuilder();
        Enumeration e = slotCommitments.keys();
        concatenate(slots, e);
        return slots.toString();
    }

    public String getCommitments() {
        StringBuilder values = new StringBuilder();
        Enumeration e = slotCommitments.elements();
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
