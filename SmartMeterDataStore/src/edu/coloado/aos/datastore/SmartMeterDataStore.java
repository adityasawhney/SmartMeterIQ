/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.coloado.aos.datastore;

import java.io.IOException;
import java.io.PrintWriter;

import javacard.framework.*;
import javacardx.framework.JCSystem;
/**
 *
 * @author Aditya
 */
public class SmartMeterDataStore extends Applet {

    /**
     * Installs this applet.
     * 
     * @param bArray
     *            the array containing installation parameters
     * @param bOffset
     *            the starting offset in bArray
     * @param bLength
     *            the length in bytes of the parameter data in bArray
     */
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new SmartMeterDataStore();
    }

    /**
     * Only this class's install method should create the applet object.
     */
    protected SmartMeterDataStore() {
        register();
    }

    /**
     * Processes an incoming APDU.
     * 
     * @see APDU
     * @param apdu
     *            the incoming APDU
     */
    @Override
    public void process(APDU apdu) {
        //
    }
}
