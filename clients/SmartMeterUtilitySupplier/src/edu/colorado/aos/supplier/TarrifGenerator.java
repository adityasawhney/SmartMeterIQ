/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.supplier;

import edu.colorado.aos.crypto.DefaultConfig;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author sawhneya
 */
public class TarrifGenerator {
    private Random random = null;
    private int curTimeSlot = 0;

    public TarrifGenerator() {
        this.random = new Random();
    }

    public Tarrif generate() {
        Tarrif tarrif = new Tarrif();

        int count = 0;
        while (count++ <= DefaultConfig.MAX_SLOT) {
            BigInteger timeSlot = generateTimeSlot();
            BigInteger value = generateValue();
            tarrif.addTarrif(timeSlot.toString(), value.toString());
        }

        return tarrif;
    }

    private BigInteger generateTimeSlot() {
        //int timeSlot = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        curTimeSlot = (curTimeSlot + 1) % DefaultConfig.MAX_SLOT;
        return new BigInteger(Integer.toString(curTimeSlot));
    }

    private BigInteger generateValue() {
        int value = random.nextInt(DefaultConfig.MAX_TARRIF_VALUE);
        return new BigInteger(Integer.toString(value));
    }
}
