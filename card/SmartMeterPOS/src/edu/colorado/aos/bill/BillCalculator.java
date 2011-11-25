/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.bill;

import edu.colorado.aos.data.Database;
import edu.colorado.aos.model.Bill;
import edu.colorado.aos.model.Reading;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author sawhneya
 */
public class BillCalculator {
    public static int MAX_TARIFF_SLOT = 10;

    public static Bill computerBill() {
        Hashtable<String, Reading> readings = Database.getReadings();
        Hashtable<String, String> slotCommitments = new Hashtable<String, String>();

        int Pt = 0;
        int Rt = 0;

        for (Enumeration e = readings.elements() ; e.hasMoreElements() ;) {
            Reading r = (Reading)e.nextElement();
            int value = Integer.parseInt(r.getValue());
            int random = Integer.parseInt(r.getRandomness());
            int tariff = getTarrif(r.getTimeSlot());

            Pt += (value * tariff);
            Rt += (random * tariff);
            
            slotCommitments.put(r.getTimeSlot(), r.getCommitment());
        }

        if (Database.isTamperEnabled()) {
            Pt -= 10;
        }

        return new Bill(Integer.toString(Pt), 
                        Integer.toString(Rt),
                        slotCommitments);
    }

    private static int getTarrif(String timeSlot) {
        int ts = Integer.parseInt(timeSlot);
        int tariffSlot = (ts % MAX_TARIFF_SLOT);
        String tarrif = Database.getTarrif(Integer.toString(tariffSlot));
        return Integer.parseInt(tarrif);
    }
}
