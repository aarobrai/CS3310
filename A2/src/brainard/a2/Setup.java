package brainard.a2;

import java.io.IOException;

/**
 * PROJECT: A1  CLASS: Setup.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 10/12/15
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
        DataStorage dStorage = new DataStorage(log);

        int numInserted = 0;

        while (rawInput.hasMoreCountries()) {
            rawInput.readNextRecord();


            dStorage.insertCountry(rawInput.getId(), rawInput.getCode(), rawInput.getName(),
                    rawInput.getContinent(), rawInput.getSurfaceArea(), rawInput.getPopulation(),
                    rawInput.getLifeExpect(),true);

            numInserted++;
        }


        dStorage.finishUp();
        rawInput.finishUp();
        log.displayThis("-->> SETUP completed. " + numInserted + " countries inserted into DataStorage");
        log.finishUp();


    }
}
