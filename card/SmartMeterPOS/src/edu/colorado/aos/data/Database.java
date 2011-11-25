/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.data;

import edu.colorado.aos.model.Reading;
import java.util.Hashtable;

/**
 *
 * @author sawhneya
 */
public class Database {
    private static Hashtable<String, Reading> readings = new Hashtable<String, Reading>();
    private static Hashtable<String, String> tarrif = new Hashtable<String, String>();
    private static boolean tamper = false;

    // Reading
    public static void addReading(Reading r) {
        if (!readings.containsKey(r.getTimeSlot())) {
            readings.put(r.getTimeSlot(), r);
        }
    }

    public static Hashtable<String, Reading> getReadings() {
        return readings;
    }

    // Tarrif
    public static Hashtable<String, String> getTarrifs() {
        return tarrif;
    }

    public static void addTarrif(String timeSlot, String tarrifValue) {
        tarrif.put(timeSlot, tarrifValue);
    }

    public static String getTarrif(String timeSlot) {
        return tarrif.get(timeSlot);
    }

    public static void purgeTarrif() {
        tarrif.clear();
    }

    // Tamper
    public static void toggleTamper() {
        tamper = !tamper;
    }

    public static boolean isTamperEnabled() {
        return tamper;
    }
}
