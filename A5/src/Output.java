import java.io.FileWriter;
import java.io.IOException;

/**
 * PROJECT: A5  CLASS: Output
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
public class Output {

    //**************************** PRIVATE DECLARATIONS **************************
    private FileWriter log;


    //**************************** PUBLIC CONSTRUCTOR(S) ************************
    public Output(boolean truncateLog) throws IOException {
        if (truncateLog) {
            log = new FileWriter("Log.txt", true);
        } else {
            log = new FileWriter("Log.txt");
        }

        // displayThis("-->> OPENED log file");

    }

    //**************************** PUBLIC SERVICE METHODS ************************
    public void displayThis(String output) throws IOException {
        log.write(output + "\r\n");
    }

    public void finishUp() throws IOException {
        //displayThis("-->> CLOSED log file");
        log.close();
    }
}
