/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aod.smartmeterdevice;

import java.math.BigInteger;

/**
 *
 * @author sawhneya
 */
public class Reading {
    private BigInteger timeSlot;
    private BigInteger value;
    private BigInteger commitment;
    private BigInteger randomness;

    public Reading(
            BigInteger timeSlot,
            BigInteger value,
            BigInteger commitment,
            BigInteger randomness) {
        this.timeSlot = timeSlot;
        this.value = value;
        this.commitment = commitment;
        this.randomness = randomness;
    }

    public BigInteger getCommitment() {
        return commitment;
    }

    public BigInteger getTimeSlot() {
        return timeSlot;
    }

    public BigInteger getValue() {
        return value;
    }

    public BigInteger getRandomness() {
        return randomness;
    }
}
