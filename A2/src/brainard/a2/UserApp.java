package brainard.a2;

import java.io.IOException;

/**
 * PROJECT: A1  CLASS: UserApp.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 *
 * FILES ACCESSED: Only INDIRECTLY thru other OOP classes
 *
 * INPUT:  TransData file. From UIinput class
 * OUTPUT: Log.txt from UIoutput class
 *
 *
 * DESCRIPTION:
 *
 * This program is a controller which uses the services of other OOP classes.
 * It uses UIinput to process requests in the TransData file, DataStorage to
 * apply the results of the transaction, and UIoutput to write the results to Log.txt
 *
 * The main algorithm used in this program is as follows:
 * loop till done with TransData file
 * {
 * input 1 TransData request
 * switch, to call the appropriate DataStorage method which handles the request
 * }
 * finish up DataStorage
 * finish up UIinput
 * finish up UIoutput
 *
 * *******************************************************************************
 */
public class UserApp {

    //**************************** PRIVATE DECLARATIONS ************************
    private static DataStorage dStorage;
    private static UIinput transReader;
    private static UIoutput log;
    private static String[] transaction;
    private static int numTransactions;

    //**************************** MAIN METHOD *********************************
    public static void main(String[] args) throws IOException {


        log = new UIoutput(false);
        log.displayThis("-->> USERAPP started");
        dStorage = new DataStorage(log);

        numTransactions = 0;

        transReader = new UIinput(args[0]);
        processTransactions();

        dStorage.finishUp();
        transReader.finishUp(numTransactions);
        log.displayThis("-->> USERAPP finished");
        log.finishUp();

    }

    private static void processTransactions() throws IOException {
        while (transReader.readTransData()) {


            transaction = transReader.getTransaction().split(" ", 2);

            switch (transaction[0]) {
                case "S":
                    dStorage.selectByCountryId(Integer.parseInt(transaction[1]));
                    numTransactions++;
                    break;
                case "A":
                    numTransactions++;
                    dStorage.showAll();
                    break;
                case "I":

                    String[] iData = transaction[1].split(",");
                    log.displayThis("I " + iData[0] + "," + iData[1] + "," + iData[2] + "," +
                            iData[3] + "," + iData[4] + "," + iData[5] + "," + iData[6]);

                    dStorage.insertCountry(iData[0], iData[1], iData[2], iData[3]
                            , iData[4], iData[5], iData[6], false);

                    numTransactions++;
                    break;
                case "D":
                    numTransactions++;
                    dStorage.delete(transaction[1]);
                    break;
            }
        }
    }
}
