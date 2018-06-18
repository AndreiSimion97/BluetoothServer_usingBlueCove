/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

/**
 *
 * @author asimion
 */
public class SimpleSPPServer implements Runnable {

    @Override
    public void run() {
        try {
            startServer();
        } catch (IOException ex) {
            Logger.getLogger(SimpleSPPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startServer() throws IOException {
        //create UUID for SSP
        UUID uuid = new UUID("1101", true);

        //Create the servicve url
        String connectionString = "btspp://localhost:" + uuid + ";name=SPP server";

        //open server url
        StreamConnectionNotifier streamConnNotifier = (StreamConnectionNotifier) Connector.open(connectionString);

        while (true) {
            try {
                //Wait for client connection
                System.out.println("\nServer Started. Waiting for clients to connect...");
                StreamConnection connection = streamConnNotifier.acceptAndOpen();
                //get inf. about remote device
                RemoteDevice dev = RemoteDevice.getRemoteDevice(connection);
                System.out.println("Remote device address: " + dev.getBluetoothAddress());
                System.out.println("Remote device name: " + dev.getFriendlyName(true));
                //create processThread
                Thread processThread = new Thread(new ProcessConnectionThread(connection));
                System.out.println("Message is sending...");
                //start procesThread
                processThread.start();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

        }

    }
}
