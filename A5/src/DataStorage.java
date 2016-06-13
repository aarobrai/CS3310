import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * PROJECT: A5
 * AUTHOR:  aaronbrainard
 * DATE:    11/12/2015
 *
 * FILES ACCESSED: DataStorage?.txt files
 *
 *
 *
 *
 * DESCRIPTION: Provides the services for retriving records via the code index.
 *              For A5, the records are implemented as Random Access.txt files.
 *              The services provided in this class are called by CodeIndex, when
 *              it finds the appropriate data record pointer in the m-way tree.
 */
public class DataStorage {

    /**
     * ********************** CONSTANT(S) *****************************************************
     */

    //2byte short rrn, 18-byte ASCII char name field, 4 byte int after, 2byte CR/LF
    private final int REC_SIZE = 25;

    /**
     * ********************** PRIVATE DECLARATION(S) *****************************************************
     */

    private RandomAccessFile data;

    /**
     * ********************** PUBLIC CONSTRUCTOR *****************************************************
     */

    public DataStorage(String fileNameSuffix) {
        try {
            data = new RandomAccessFile("DataStorage" + fileNameSuffix + ".txt", "r");
            System.out.println("Using DataStorage" + fileNameSuffix + " file");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: DataStorage" + fileNameSuffix + " file not found");
            e.printStackTrace();
        }
    }

    /**
     * ********************** PUBLIC SERVICE METHODS *****************************************************
     */

    public String retrieveRecord(int rrn) throws IOException {
        data.seek(calculateByteOffset(rrn));
        String record = "";
        for (int i = 0; i < (REC_SIZE - 2); i++) {
            record += (char) data.read();
        }

        return record;
    }

    /**
     * ********************** PRIVATE HELPER METHODS *****************************************************
     */

    private int calculateByteOffset(int rrn) {
        return ((rrn - 1) * REC_SIZE);
    }

    /**
     * ********************** CLEANUP *****************************************************
     */
    public void finishUp() throws IOException {
        System.out.println("Closing DataStorage file...");
        data.close();
    }
}
