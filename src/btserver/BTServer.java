/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btserver;

import java.io.IOException;
import javax.bluetooth.LocalDevice;

/**
 *
 * @author asimion
 */
public class BTServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //display local device address and name
        LocalDevice localDevice = LocalDevice.getLocalDevice();
        System.out.println("Address: " + localDevice.getBluetoothAddress());
        System.out.println("Name: " + localDevice.getFriendlyName());

        Thread sampleSPPServer = new Thread(new SimpleSPPServer());
        sampleSPPServer.start();
    }

}
