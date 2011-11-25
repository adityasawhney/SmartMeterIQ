/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.colorado.aod.smartmeterdevice;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author sawhneya
 */
public class ReadingDispatcher {
    private HttpHost targetHost;

    public ReadingDispatcher(String hostName, int port) {
        this.targetHost = new HttpHost(hostName, port, "http");
    }
    
    public void dispatch(Reading reading) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpRequest request = composeRequest(reading);
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

    private HttpRequest composeRequest(Reading reading) {
        StringBuilder readingUri = new StringBuilder();
        readingUri.append("/smartmeterpos/reading?");
        readingUri.append("timeslot=");
        readingUri.append(reading.getTimeSlot());
        readingUri.append("&value=");
        readingUri.append(reading.getValue());
        readingUri.append("&commitment=");
        readingUri.append(reading.getCommitment());
        readingUri.append("&randomness=");
        readingUri.append(reading.getRandomness());
        return new HttpGet(readingUri.toString());
    }
}
