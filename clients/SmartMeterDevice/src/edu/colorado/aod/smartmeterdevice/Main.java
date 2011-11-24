/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aod.smartmeterdevice;

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

public class Main {

    public static void main(String[] args) {
        HttpClientFrame f = new HttpClientFrame();
        f.setTitle("SmartMeter Simulator");
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

    
    public static class HttpClientFrame extends JFrame implements Simulator.Callback {
        private Simulator simulator = new Simulator();
        private JTextArea taReadings;

        public HttpClientFrame() {
            JPanel panInput = new JPanel(new FlowLayout());

            final JButton btnSTART = new JButton("START");
            btnSTART.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        simulator.setDispatchReading(true);
                    }
                }
            );

            final JButton btnSTOP = new JButton("STOP");
            btnSTOP.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        simulator.setDispatchReading(false);
                    }
                }
            );

            panInput.add(btnSTART);
            panInput.add(btnSTOP);

            taReadings = new JTextArea();
            taReadings.setEditable(false);
            taReadings.setCaretPosition(0);

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(panInput, BorderLayout.NORTH);
            this.getContentPane().add(taReadings, BorderLayout.CENTER);

            this.simulator.setCallback(this);
            this.simulator.start();
        }

        public void handle(Reading reading) {
            StringBuilder data = new StringBuilder();
            data.append(reading.getTimeSlot());
            data.append("\t");
            data.append(reading.getValue());
            data.append("\t");
            data.append(reading.getCommitment());
            data.append("\t");
            data.append(reading.getRandomness());
            data.append("\n");
            taReadings.append(data.toString());
        }
    }
}