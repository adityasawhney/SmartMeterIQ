/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.colorado.aos;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aditya
 */
public class SmartMeterPOS extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html><head><title>SmartMeterPOS</title></head>");
            out.println("<body><h1>SmartMeterPOS</h1>");
            out.println("Hello from edu.colorado.aos.SmartMeterPOS to");
            out.println(request.getParameter("name"));
            out.println("</body></html>");
        } finally {
            out.close();
        }
    }

/*
    public void run() {

        BigInteger T[] = { new BigInteger("5"), new BigInteger("7") };

        SmartMeter meter = new SmartMeter();
        PrivacyPlugin plugin = new PrivacyPlugin(meter);
        Supplier supplier = new Supplier();

        meter.setTarrif(T);
        plugin.setTarrif(T);
        supplier.setTarrif(T);

        SmartMeterBill bill = plugin.retrieveBill();
        boolean isValid = supplier.verifyBill(bill);
        System.out.print(isValid);

        plugin.toggleTamper();

        bill = plugin.retrieveBill();
        isValid = supplier.verifyBill(bill);
        System.out.print(isValid);
 }
 */
}
