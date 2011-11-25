/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos;

import edu.colorado.aos.bill.BillCalculator;
import edu.colorado.aos.model.Bill;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sawhneya
 */
public class SmartMeterBillingServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        PrintWriter out = response.getWriter();
        
        try {
            Bill bill = BillCalculator.computerBill();
            out.println(bill.getTotalAmount());
            out.println(bill.getTotalRandomness());
            out.println(bill.getTimeSlots());
            out.println(bill.getCommitments());
        } finally {
            out.close();
        }
    }
}
