/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos;

import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sawhneya
 */
public class SmartMeterReadingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String timeSlot = request.getParameter("timeslot");
        String value = request.getParameter("value");
        String commitment = request.getParameter("commitment");
        String randomness = request.getParameter("randomness");
        Reading reading = new Reading(
                timeSlot,
                value,
                commitment,
                randomness);
        Database.addReading(reading);
    }
}
