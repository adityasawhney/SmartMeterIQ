/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos;

import edu.colorado.aos.data.Database;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sawhneya
 */
public class SmartMeterTarrifServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String timeSlot = request.getParameter("timeslots");
        String value = request.getParameter("values");
        StringTokenizer ts = new StringTokenizer(timeSlot, ",");
        StringTokenizer vs = new StringTokenizer(value, ",");

        Database.purgeTarrif();

        while (ts.hasMoreTokens()) {
             Database.addTarrif(ts.nextToken(), vs.nextToken());
         }
    }
}
