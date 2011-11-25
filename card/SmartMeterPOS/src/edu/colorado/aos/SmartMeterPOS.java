/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.colorado.aos;

import edu.colorado.aos.bill.BillCalculator;
import edu.colorado.aos.model.Reading;
import edu.colorado.aos.data.Database;
import edu.colorado.aos.model.Bill;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aditya
 */
public class SmartMeterPOS extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String op = request.getParameter("op");
        StringBuilder result = new StringBuilder();

        // Handle operation
        if (op != null) {
            if (op.equals("Bill")) {
                generateBill(result);
            }
            else if (op.equals("Reading")) {
                generateReading(result);
            }
            else if (op.equals("Tarrif")) {
                generateTarrif(result);
            }
            else if (op.equals("Tamper")) {
                Database.toggleTamper();
            }
        }

        // Compile response
        addResultHeader(result);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        RequestDispatcher dispatcher = null;

        dispatcher = request.getRequestDispatcher("/WEB-INF/header.i");
        dispatcher.include(request, response);

        dispatcher = request.getRequestDispatcher("/WEB-INF/index1.i");
        dispatcher.include(request, response);

        out.println(result);

        dispatcher = request.getRequestDispatcher("/WEB-INF/index2.i");
        dispatcher.include(request, response);

        dispatcher = request.getRequestDispatcher("/WEB-INF/footer.i");
        dispatcher.include(request, response);
    }

    private void addResultHeader(StringBuilder result) {
        StringBuilder header = new StringBuilder();
        header.append("Tamper is ");
        header.append(Database.isTamperEnabled() ? "ENABLED" : "DISABLED");
        header.append("\n");

        result.insert(0, header.toString());
    }

    private void generateBill(StringBuilder result) {
        Bill bill = BillCalculator.computerBill();
        result.append("\n");
        result.append("Total Amount:");
        result.append("\t\t");
        result.append(bill.getTotalAmount());

        result.append("\n");
        result.append("Total Randomness:");
        result.append("\t");
        result.append(bill.getTotalRandomness());

        result.append("\n");
        result.append("Time Slots:");
        result.append("\t\t");
        result.append(bill.getTimeSlots());

        result.append("\n");
        result.append("Commitments:");
        result.append("\t\t");
        result.append(bill.getCommitments());
    }

    private void generateReading(StringBuilder result) {
        // Header
        result.append("\n");
        result.append("TimeSlot");
        result.append("\t");
        result.append("Reading Value");
        result.append("\t");
        result.append("Commitment");
        result.append("\t");
        result.append("Randomness");
        result.append("\n");

        for (Enumeration e = Database.getReadings().elements() ; e.hasMoreElements() ;) {
            Reading r = (Reading)e.nextElement();
            result.append(r.getTimeSlot());
            result.append("\t\t");
            result.append(r.getValue());
            result.append("\t\t");
            result.append(r.getCommitment());
            result.append("\t\t");
            result.append(r.getRandomness());
            result.append("\n");
        }
    }

    private void generateTarrif(StringBuilder result) {
        // Header
        result.append("\n");
        result.append("TimeSlot");
        result.append("\t");
        result.append("Tarrif");
        result.append("\n");

        for (Enumeration e = Database.getTarrifs().keys() ; e.hasMoreElements() ;) {
            String slot = (String)e.nextElement();
            result.append(slot);
            result.append("\t\t");
            result.append(Database.getTarrif(slot));
            result.append("\t\t");
            result.append("\n");
        }
    }
}
