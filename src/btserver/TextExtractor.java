/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package btserver;

import btserver.PDFManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asimion
 */
public class TextExtractor implements Runnable {

    //public ArrayList<String> mStations;
    public HashMap<String, ArrayList<String>> mMyTimeMap;
    private String mPdfPath;
    private String mStation;
    private String mRouteName;

    public TextExtractor(String path) {
        //mStations = new ArrayList();
        mMyTimeMap = new HashMap();
        mPdfPath = path;
    }

    @Override
    public void run() {
        try {
            ExtractData();
        } catch (IOException ex) {
            Logger.getLogger(TextExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ExtractData() throws IOException {
        PDFManager pdfManager = new PDFManager(mPdfPath);
        String text = pdfManager.ToText();

        String[] dataTextArray = text.split("\n");
        
        //set mStation and mRounteName
        setMainStationAndRouteName(dataTextArray);
        //add stations
        /* int i = 2;
        while(!dataTextArray[i].equals("ORE ZILE LUCRATOARE")){
               mStations.add(dataTextArray[i]);
            i++;
        }*/
        int startIndex = getIndexWhereStartTimetableBus(dataTextArray);
        //add hours in myTimeMap
        for (int j = startIndex; j < dataTextArray.length; j++) {
            String[] line = dataTextArray[j].split(" ");
            String hour = line[0];
            ArrayList<String> min = new ArrayList();
            for (int k = 1; k < line.length; k++) {
                min.add(line[k]);
            }
            mMyTimeMap.put(hour, min);
        }
    }

    private int getIndexWhereStartTimetableBus(String[] arr) {
        int index = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("ORE ZILE LUCRATOARE")) {
                index = i + 1;
                break;
            }
        }
        return index;
    }

    public String getMainStation() {
        return mStation;
    }
    
    public String getRouteName(){
        return mRouteName;
    }
    
    private void setMainStationAndRouteName(String []dataArr){
        for (String line : dataArr) {
            if (line.startsWith("TRASEU")) {
                mRouteName = line;
            }else if(line.startsWith("STATIA")){
                mStation = line;
            }
        }
    }
}
