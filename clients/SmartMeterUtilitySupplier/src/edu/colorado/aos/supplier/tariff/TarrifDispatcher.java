/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aos.supplier.tariff;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author sawhneya
 */
public class TarrifDispatcher {
    private HttpHost targetHost;

    public TarrifDispatcher(String hostName, int port) {
        this.targetHost = new HttpHost(hostName, port, "http");
    }
    
    public void dispatch(Tarrif tarrif) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpRequest request = composeRequest(tarrif);
            httpclient.execute(targetHost, request);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
    }

    private HttpRequest composeRequest(Tarrif tarrif) {
        StringBuilder readingUri = new StringBuilder();
        readingUri.append("/smartmeterpos/tarrif?");
        readingUri.append("timeslots=");
        readingUri.append(tarrif.getTimeSlots());
        readingUri.append("&values=");
        readingUri.append(tarrif.getTariffValues());
        return new HttpGet(readingUri.toString());
    }
}
