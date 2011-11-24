/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos;

import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author sawhneya
 */
public class Database {
    private static Vector<Reading> items = new Vector<Reading>();
    private static Hashtable<String, String> tarrif = new Hashtable<String, String>();
    private static boolean tamper = false;

    // Reading
    public static void addReading(Reading item) {
        if (!items.contains(item)) {
            items.addElement(item);
        }
    }

    public static Vector<Reading> getReadings() {
        return items;
    }

    public static boolean deleteReading(Reading item) {
        return items.removeElement(item);
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
