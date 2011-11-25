/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.supplier;

import edu.colorado.aos.supplier.tariff.TarrifDispatcher;
import edu.colorado.aos.supplier.tariff.TarrifGenerator;
import edu.coloado.aos.config.DefaultConfig;
import edu.colorado.aos.supplier.bill.Bill;
import edu.colorado.aos.supplier.bill.BillRetriever;
import edu.colorado.aos.supplier.bill.BillVerifier;
import edu.colorado.aos.supplier.tariff.Tarrif;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Aditya
 */
public class Main {

    public static void main(String[] args) {
        HttpClientFrame f = new HttpClientFrame();
        f.setTitle("Utility Company Simulator");
        f.setSize(700, 500);
        f.addWindowListener(
            new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            }
        );
        f.setVisible(true);
    }


    public static class HttpClientFrame extends JFrame {
       private JTextArea taDisplay = new JTextArea();
       private TarrifDispatcher dispatcher = new TarrifDispatcher(
                DefaultConfig.HOST_NAME,
                DefaultConfig.PORT);
       private BillRetriever billRetirever = new BillRetriever(
               DefaultConfig.HOST_NAME,
               DefaultConfig.PORT);
       private Tarrif currentTariff = null;
       private Bill currentBill = null;

       public HttpClientFrame() {
            JPanel panInput = new JPanel(new FlowLayout());

            final JButton btnSendTarrif = new JButton("Send Tarrif");
            btnSendTarrif.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        sendTarrif();
                    }
                }
            );

            final JButton btnGetBill = new JButton("Get Bill");
            btnGetBill.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        getBill();
                    }
                }
            );

            final JButton btnVerifyBill = new JButton("Verify Bill");
            btnVerifyBill.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        verifyBill();
                    }
                }
            );

            panInput.add(btnSendTarrif);
            panInput.add(btnGetBill);
            panInput.add(btnVerifyBill);

            taDisplay.setEditable(false);
            taDisplay.setAlignmentX(CENTER_ALIGNMENT);
            taDisplay.setAlignmentY(CENTER_ALIGNMENT);
            taDisplay.setFont(new Font("Verdana", Font.PLAIN, 14));

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(panInput, BorderLayout.NORTH);
            this.getContentPane().add(taDisplay, BorderLayout.CENTER);
        }

        private void sendTarrif() {
            TarrifGenerator generator = new TarrifGenerator();

            currentTariff = generator.generate();
            dispatcher.dispatch(currentTariff);

            taDisplay.setText("");
            taDisplay.setText("Tarrif Data Sent!!");
        }

        private void getBill() {
            currentBill = billRetirever.retrieve();

            // display
            taDisplay.setText("");
            if (currentBill != null) {
                taDisplay.setText(currentBill.toString());
            }
            else {
                taDisplay.setText("Failed to retrieve bill!!");
            }
        }

        private void verifyBill() {
            BillVerifier billVerifier = new BillVerifier();
            boolean isBillValid = billVerifier.verify(currentBill, currentTariff);

            // display
            taDisplay.setText("");
            if (isBillValid) {
                taDisplay.setText("Awesome...the bill is VALID!!");
            }
            else {
                taDisplay.setText("Bummer...the bill is NOT VALID!!");
            }
        }
    }
}
