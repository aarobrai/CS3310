package edu.wmich.cs3310.brainard;

import java.io.*;

/**
 * PROJECT: A1  CLASS: RawData.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 * <p>
 * FILES ACCESSED:
 * <p>
 * INPUT: RawData.csv
 * OUTPUT: Log.txt
 * <p>
 * DESCRIPTION:
 * <p>
 * Reads a .csv input file and processes its contents for insertion into DataStorage by Setup.
 * <p>
 * Algorithm:
 * loop till no more countries
 * {
 * read line
 * split line along the commas, creating an array of fields
 * insert each element of the array into its corresponding class field
 * }
 * <p>
 * finishup
 * <p>
 * *******************************************************************************
 */
public class RawData {

    //**************************** PRIVATE DECLARATIONS *************************
    private String currentLine;
    private String code;
    private String id;
    private String name;
    private String continent;
    private String surfaceArea;
    private String population;
    private String lifeExpect;
    private BufferedReader readRawData;
    private UIoutput mLog;
    private int numRecords;

    //**************************** PUBLIC CONSTRUCTOR(S) ************************
    public RawData(String fileLocation, UIoutput log) throws IOException {
        numRecords = 0;
        mLog = log;
//        mLog.displayThis("-->>RAWDATA started");
        readRawData = new BufferedReader(new FileReader(fileLocation));
        mLog.displayThis("-->>Opened RawDataAZ.csv file");
    }

    //**************************** PUBLIC GET METHODS ************************
    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContinent() {
        return continent;
    }

    public String getSurfaceArea() {
        return surfaceArea;
    }

    public String getPopulation() {
        return population;
    }

    public String getLifeExpect() {
        return lifeExpect;
    }

    //************************* PUBLIC SERVICE METHODS ************************

    public boolean hasMoreCountries() throws IOException {
        if ((currentLine = readRawData.readLine()) == null) {
            return false;
        } else {
            return true;
        }
    }

    public void readNextRecord() throws IOException {
        numRecords++;
        String[] dataFields = currentLine.split(",");


        //put each data field into its appropriate variable
        code = dataFields[0];
        id = dataFields[1];
        name = dataFields[2];
        continent = dataFields[3];
        surfaceArea = dataFields[5];
        population = dataFields[7];
        if ((lifeExpect = dataFields[8]).equals("NULL")) {
            System.out.println(name + " has a null life expectancy. Setting lifeExpect to 0.0");
            lifeExpect = "0.0";
        }


    }

    public void finishUp() throws IOException {
        mLog.displayThis("-->>RawData finished- processed " + numRecords + " records");
        mLog.displayThis("-->>CLOSED RawDataAZ.csv file");
        readRawData.close();


    }

}


