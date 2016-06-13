package brainard.a2;

import java.io.*;

/**
 * PROJECT: A1  CLASS: UIinput.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 *
 * FILES ACCESSED:
 *
 * INPUT: TransData file
 * OUTPUT: Log.txt, via UIoutput class
 *
 * DESCRIPTION:
 *
 * Provides the TransData reading service for UserApp.
 * Each request is read from the file, and is then accessed and processed
 * by UserApp.
 *
 * The TransData file is opened, completely dealt with, and closed in this class.
 *
 *
 * *******************************************************************************
 */
public class UIinput {

    //**************************** PRIVATE DECLARATIONS *************************
    private UIoutput log;
    private BufferedReader transReader;
    private String currentLine;
    private String transaction;


    //**************************** PUBLIC CONSTRUCTOR(S) ************************
    public UIinput(String fileName) throws IOException {


        log = new UIoutput(false);
        transReader = new BufferedReader(new FileReader(fileName));
        log.displayThis("-->> OPENED "+fileName);

    }

    //**************************** PUBLIC SERVICE METHODS ************************
    public boolean readTransData() throws IOException {

        if ((currentLine = transReader.readLine()) == null) {
            return false;
        } else {
            transaction = currentLine.trim();
            return true;
        }
    }

    public void finishUp(int numTransactions) throws IOException {
        log.displayThis("-->> TransData processing finished- " + numTransactions + " transactions processed");
        log.displayThis("-->> CLOSED TransData file");
        transReader.close();
        //log.finishUp();
    }

    //**************************** PUBLIC GET METHODS ************************


    public String getTransaction() {
        return transaction;
    }

}
