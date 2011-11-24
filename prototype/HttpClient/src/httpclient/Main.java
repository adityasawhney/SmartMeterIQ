/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package httpclient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * A simple Swing application that demonstrates how to use the Jakarta
 * HttpClient API.  This application loads HTML from servers and displays the
 * content as text and as rendered HTML.
 *
 * @author Sean C. Sullivan
 * @author Ortwin Glück
 * @author Michael Becke
 */
public class Main {

    public static void main(String[] args) {
        HttpClientFrame f = new HttpClientFrame();
        f.setTitle("HttpClient demo application");
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

        private JComboBox cmbURL;
        private JTextArea taTextResponse;
        private JEditorPane htmlPane;

        public HttpClientFrame() {
            JPanel panInput = new JPanel(new FlowLayout());

            String[] aURLs = {
                "http://www.apache.org/",
                "http://www.google.com/",
                "http://www.opensource.org/",
                "http://www.anybrowser.org/",
                "http://jakarta.apache.org/",
                "http://www.w3.org/"
            };

            final JButton btnGET = new JButton("GET");
            btnGET.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        String url = (String) cmbURL.getSelectedItem();
                        if (url != null && url.length() > 0) {
                            loadPage(url);
                        }
                    }
                }
            );

            cmbURL = new JComboBox(aURLs);
            cmbURL.setToolTipText("Enter a URL");
            cmbURL.setEditable(true);
            cmbURL.setSelectedIndex(0);

            JLabel lblURL = new JLabel("URL:");

            panInput.add(lblURL);
            panInput.add(cmbURL);
            panInput.add(btnGET);

            taTextResponse = new JTextArea();
            taTextResponse.setEditable(false);
            taTextResponse.setCaretPosition(0);

            htmlPane = new JEditorPane();
            htmlPane.setContentType("text/html");
            htmlPane.setEditable(false);

            JSplitPane splitResponsePane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(taTextResponse),
                new JScrollPane(htmlPane)
            );
            splitResponsePane.setOneTouchExpandable(false);
            splitResponsePane.setDividerLocation(350);
            // it would be better to set resizeWeight, but this method does
            // not exist in JRE 1.2.2
            splitResponsePane.setResizeWeight(0.5);

            this.getContentPane().setLayout(new BorderLayout());
            this.getContentPane().add(panInput, BorderLayout.NORTH);
            //this.getContentPane().add(splitResponsePane, BorderLayout.CENTER);
            this.getContentPane().add(htmlPane, BorderLayout.CENTER);
        }

        /**
         * Sets the HTML content to be displayed.
         *
         * @param content an HTML document
         */
        private void setDocumentContent(String content) {

            HTMLDocument doc = new HTMLDocument();
            try {
                doc.remove(0, doc.getLength());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);

            try {
                htmlPane.read(new ByteArrayInputStream(content.getBytes()), doc);
            } catch (IOException e) {
                e.printStackTrace();
            }

            htmlPane.setDocument(doc);
            htmlPane.setCaretPosition(0);

            taTextResponse.setText(content);
            taTextResponse.setCaretPosition(0);
            taTextResponse.requestFocus();
        }

        /**
         * Loads the page at the given URL from a separate thread.
         * @param url
         */
        private void loadPage(final String url) {
            // create a new thread to load the URL from
            new Thread() {
                @Override
                public void run() {
                    DefaultHttpClient httpclient = new DefaultHttpClient();
                    try {
                        HttpHost target = new HttpHost("localhost", 8019, "http");
                        HttpGet req = new HttpGet("/sioclient/getsiovalue");

                        System.out.println("executing request to " + target);

                        HttpResponse rsp = httpclient.execute(target, req);
                        final HttpEntity entity = rsp.getEntity();

                        System.out.println("----------------------------------------");
                        System.out.println(rsp.getStatusLine());
                        Header[] headers = rsp.getAllHeaders();
                        for (int i = 0; i < headers.length; i++) {
                            System.out.println(headers[i]);
                        }
                        System.out.println("----------------------------------------");

                        if (entity != null) {
                            final String responseBody = EntityUtils.toString(entity);
                            System.out.println(responseBody);
                            // set the HTML on the UI thread
                            SwingUtilities.invokeLater(
                                new Runnable() {
                                    public void run() {
                                        setDocumentContent(responseBody);
                                    }
                                }
                            );
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        // When HttpClient instance is no longer needed,
                        // shut down the connection manager to ensure
                        // immediate deallocation of all system resources
                        httpclient.getConnectionManager().shutdown();
                    }
                }
            }.start();
        }

    }

}