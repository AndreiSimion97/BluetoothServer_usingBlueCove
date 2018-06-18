/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btserver;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author asimion
 */
public class ProcessConnectionThread implements Runnable {

    private StreamConnection mConnection;

    public ProcessConnectionThread(StreamConnection connection) {
        mConnection = connection;
    }

    @Override
    public void run() {
        try {   
            Date date = new Date();
            String[] serverTime = date.toString().split(" ");//index 3 is for time;
            
            TextExtractor t1 = new TextExtractor("/home/asimion/Desktop/pdfDePrelucrat/61.pdf");
            TextExtractor t2 = new TextExtractor("/home/asimion/Desktop/pdfDePrelucrat/62.pdf");
            TextExtractor t3 = new TextExtractor("/home/asimion/Desktop/pdfDePrelucrat/336.pdf");
            Thread textExtractorThread1 = new Thread(t1);
            Thread textExtractorThread2 = new Thread(t2);
            Thread textExtractorThread3 = new Thread(t3);
            
            textExtractorThread1.start();
            textExtractorThread2.start();
            textExtractorThread3.start();
          
           
            textExtractorThread1.join();
            textExtractorThread2.join();
            textExtractorThread3.join();

            OutputStream outputStream = mConnection.openOutputStream();
              
            String mainStation = t1.getMainStation();
            //info get all inf about buses
            String info1 = t1.getRouteName() +"\n" + getNextBusTime(serverTime[3], t1.mMyTimeMap);
            String info2 = t2.getRouteName() + "\n" + getNextBusTime(serverTime[3], t2.mMyTimeMap);
            String info3 = t3.getRouteName() +  "\n" + getNextBusTime(serverTime[3], t3.mMyTimeMap);
            
            String info =  info1 + "\n" + info2 + "\n" + info3 + "\n" + mainStation;
            
            //send bytes array info
            outputStream.write(info.getBytes());
            //System.out.println(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getNextBusTime(String localtime, HashMap<String, ArrayList<String>> myHash) {
        String time = "";
        String[] myLocalTime = localtime.split(":");
        
        int nextHourInt = Integer.parseInt(myLocalTime[0]) + 1;
        if (nextHourInt > 23 || nextHourInt < 4) {
           time = "Linie-Inchisa";
       } else {
            String nextHourString = String.valueOf(nextHourInt);
            String nextMin = myHash.get(nextHourString).get(0);

            for (String hours : myHash.keySet()) {

                if (hours.equals(myLocalTime[0])) {
                    if (Integer.parseInt(myHash.get(hours).get(myHash.get(hours).size() - 1)) <= Integer.parseInt(myLocalTime[1])) {
                        time = nextHourString + ":" + nextMin;
                        break;
                    } else {
                        String[] aux = new String[myHash.get(hours).size()];
                        aux = myHash.get(hours).toArray(aux);
                        for (int i = 0; i < aux.length; i++) {
                            if (Integer.parseInt(aux[i]) > Integer.parseInt(myLocalTime[1])) {
                                time = hours + ":" + aux[i];
                                break;
                            } else if (Integer.parseInt(aux[i]) == Integer.parseInt(myLocalTime[1])) {
                                time = "~ <1min";
                                break;
                            }
                        }
                    }
                } else if ((myHash.get(myLocalTime[0]).get(0)).equals("-")) {//special case
                    //this case is magic :))
                    time = myHash.get(hours).get(0);
                    break;

                }
            }
         }//final else nextHour < 23
        return time;
    }

}
