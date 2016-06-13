package brainard.a2;

import java.io.*;

/**
 * PROJECT: A1  CLASS: RawData.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 *
 * FILES ACCESSED:
 *
 * INPUT: RawData.csv
 * OUTPUT: Log.txt
 *
 * DESCRIPTION:
 *
 * Reads a .csv input file and processes its contents for insertion into DataStorage by Setup.
 * <
 * Algorithm:
 * loop till no more countries
 * {
 * read line
 * split line along the commas, creating an array of fields
 * insert each element of the array into its corresponding class field
 * }
 * <
 * finishup
 * <
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
        mLog.displayThis("-->>Opened RawData2.csv file");
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
        code = dataFields[1];
        id = dataFields[0];
        name = dataFields[2];
        continent = dataFields[3];
        surfaceArea = dataFields[5];
        population = dataFields[7];
        if ((lifeExpect = dataFields[8]).equals("NULL")) {
            System.out.println(name + " has a null life expectancy. Setting lifeExpect to 0.0");
            lifeExpect = "0.0";
        }

       // testPrintData();
    }

    public void finishUp() throws IOException {
        mLog.displayThis("-->> RawData finished- processed " + numRecords + " records");
        mLog.displayThis("-->> CLOSED RawData2.csv file");
        readRawData.close();


    }

    private void testPrintData() {
        System.out.println("id = " + id);
        System.out.println("code = " + code);
        System.out.println("name = " + name);
        System.out.println("continent = " + continent);
        System.out.println("area = " + surfaceArea);
        System.out.println("population = " + population);
        System.out.println("lifeExpect = " + lifeExpect);
        System.out.println("=================================");
    }

}


