/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.supplier;

import edu.coloado.aos.config.DefaultConfig;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
                    }
                }
            );

            final JButton btnVerifyBill = new JButton("Verify Bill");
            btnVerifyBill.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                    }
                }
            );

            panInput.add(btnSendTarrif);
            panInput.add(btnGetBill);
            panInput.add(btnVerifyBill);

            taDisplay.setEditable(false);

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(panInput, BorderLayout.NORTH);
            this.getContentPane().add(taDisplay, BorderLayout.CENTER);
        }

        private void sendTarrif() {
            TarrifGenerator generator = new TarrifGenerator();
            TarrifDispatcher dispatcher = new TarrifDispatcher(
                    DefaultConfig.HOST_NAME,
                    DefaultConfig.PORT);

            dispatcher.dispatch(generator.generate());
            taDisplay.setText("");
            taDisplay.setText("Tarrif Data Sent!!");
        }
    }
}
