import java.io.FileWriter;
import java.io.IOException;

/**
 * PROJECT: A6  CLASS: Log
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: Log.txt
 *
 *
 *
 *
 * DESCRIPTION: Provides service for writing messages to Log.txt file
 */
public class Log {

    //**************************** PRIVATE DECLARATIONS **************************
    private FileWriter log;


    //**************************** PUBLIC CONSTRUCTOR(S) ************************
    public Log(boolean truncateLog) throws IOException {
        if (truncateLog) {
            log = new FileWriter("Log.txt", true);
        } else {
            log = new FileWriter("Log.txt");
        }

        // displayThis("-->> OPENED log file");

    }

    //**************************** PUBLIC SERVICE METHODS ************************
    public void displayThisLine(String output) throws IOException {
        log.write(output + "\r\n");
    }

    public void displayThis(String output) throws IOException {
        log.write(output);
    }

    public void finishUp() throws IOException {
        //displayThis("-->> CLOSED log file");
        log.close();
    }
}
