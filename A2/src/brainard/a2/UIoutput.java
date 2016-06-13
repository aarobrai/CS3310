package brainard.a2;

import java.io.FileWriter;
import java.io.IOException;

/**
 * PROJECT: A1  CLASS: UIoutput.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 *
 * FILES ACCESSED:
 *
 * OUTPUT: Log.txt
 *
 *
 * DESCRIPTION:
 *
 * This class provides the services to write to the Log, which is the main output file.
 * displayThis() writes a given String to the log along with a carriage return and newline.
 *
 * Used by all other classes in the project except PrettyPrint.
 *
 *
 *
 * *******************************************************************************
 */
public class UIoutput {

    //**************************** PRIVATE DECLARATIONS **************************
    private FileWriter log;


    //**************************** PUBLIC CONSTRUCTOR(S) ************************
    public UIoutput(boolean truncateLog) throws IOException {
        if (truncateLog) {
            log = new FileWriter("Log.txt");
        } else {

            log = new FileWriter("Log.txt", true);
        }

        displayThis("-->> OPENED log file");

    }

    //**************************** PUBLIC SERVICE METHODS ************************
    public void displayThis(String output) throws IOException {
        log.write(output + "\r\n");
    }

    public void finishUp() throws IOException {
        displayThis("-->> CLOSED log file");
        log.close();
    }
}
