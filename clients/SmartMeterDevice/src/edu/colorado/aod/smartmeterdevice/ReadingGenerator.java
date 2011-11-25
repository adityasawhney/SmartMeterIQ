/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aod.smartmeterdevice;

import edu.coloado.aos.config.DefaultConfig;
import edu.colorado.aos.crypto.ZpPedersen;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author sawhneya
 */
public class ReadingGenerator {
    private ZpPedersen perdersen = null;
    private Random random = null;
    private int curTimeSlot = 0;

    public ReadingGenerator() {
        this.perdersen = DefaultConfig.createPedersen();
        this.random = new Random();
    }

    public Reading generate() {
        BigInteger timeSlot = generateTimeSlot();
        BigInteger value = generateValue();
        BigInteger randomness = this.perdersen.getRandom(random);
        BigInteger commitment = this.perdersen.commit(value, randomness);
        return new Reading(timeSlot, value, commitment, randomness);
    }

    private BigInteger generateTimeSlot() {
        //int timeSlot = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        curTimeSlot = (curTimeSlot + 1) % DefaultConfig.MAX_SLOT;
        return new BigInteger(Integer.toString(curTimeSlot));
    }

    private BigInteger generateValue() {
        int value = random.nextInt(DefaultConfig.MAX_READING_VALUE);
        return new BigInteger(Integer.toString(value));
    }
}
