/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos;


/**
 *
 * @author sawhneya
 */
public class Reading {
    private String timeSlot;
    private String value;
    private String commitment;
    private String randomness;

    public Reading(
            String timeSlot,
            String value,
            String commitment,
            String randomness) {
        this.timeSlot = timeSlot;
        this.value = value;
        this.commitment = commitment;
        this.randomness = randomness;
    }

    public String getCommitment() {
        return commitment;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getValue() {
        return value;
    }

    public String getRandomness() {
        return randomness;
    }
}
