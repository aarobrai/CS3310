package edu.wmich.cs3310.brainard;

import java.io.IOException;

/**
 * PROJECT: A1  CLASS: Setup.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 *
 * FILES ACCESSED:
 *
 * INPUT: Rawdata.csv, indirectly thru RawData class
 * OUTPUT: Log.txt file
 *
 * DESCRIPTION:
 * This class processes the RawData file and inserts its contents into DataStorage.
 * EVERYTHING to do with the RawData file is handled here.
 *
 * The raw data input algorithm is as follows:
 * loop til done with RawData file
 * {
 * read one record
 * completely deal with record and insert into DataStorage
 * }
 *
 * finish up DataStorage
 * finish up RawData
 *
 * *******************************************************************************
 */
public class Setup {
    //**************************** MAIN METHOD ************************
    public static void main(String[] args) throws IOException {


        UIoutput log = new UIoutput(true);

        log.displayThis("-->> SETUP started");

        String fileNameSuffix = args[0];

        RawData rawInput = new RawData("RawData" + fileNameSuffix + ".csv", log);
        DataStorage dStorage = new DataStorage();

        while (rawInput.hasMoreCountries()) {
            rawInput.readNextRecord();

            dStorage.insertCountry(rawInput.getCode(), rawInput.getId(), rawInput.getName(), rawInput.getContinent(),
                    rawInput.getSurfaceArea(), rawInput.getPopulation(), rawInput.getLifeExpect());
        }


        dStorage.finishUp();
        rawInput.finishUp();
        log.displayThis("-->> SETUP completed");
        log.finishUp();


    }
}
