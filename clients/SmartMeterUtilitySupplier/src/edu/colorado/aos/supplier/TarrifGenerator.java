/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.supplier;

import edu.coloado.aos.config.DefaultConfig;
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
        while (count++ < DefaultConfig.MAX_TARIFF_SLOT) {
            BigInteger timeSlot = generateTimeSlot();
            BigInteger value = generateValue();
            tarrif.addTarrif(timeSlot.toString(), value.toString());
        }

        return tarrif;
    }

    private BigInteger generateTimeSlot() {
        curTimeSlot++;
        return new BigInteger(Integer.toString(curTimeSlot));
    }

    private BigInteger generateValue() {
        int value = random.nextInt(DefaultConfig.MAX_TARRIF_VALUE);
        return new BigInteger(Integer.toString(value));
    }
}
