package brainard.a2;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;

/**
 * PROJECT: A1  CLASS: TestDriver.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 *
 * FILES ACCESSED: Log.txt, to display when the driver has started and finished.
 *
 *
 *
 *
 * DESCRIPTION: Deletes the outputfiles Log.txt and Backup.csv (if they exist), then runs
 * Setup, PrettyPrint, and UserApp.
 *
 *
 *
 *
 * *******************************************************************************
 */
public class TestDriver {
    //**************************** MAIN METHOD ************************
    public static void main(String[] args) throws IOException {


        deleteFile("DataStorage.bin");



        Setup.main(new String[]{"2"});
        PrettyPrint.main(new String[]{});
        UserApp.main(new String[]{"TransData2a.txt"});
        UserApp.main(new String[]{"TransData2bIdFirst.txt"});
        PrettyPrint.main(new String[]{});


    }

    private static boolean deleteFile(String fileName) {
        boolean delete = false;
        File f = new File(fileName);
        if (f.exists()) {
            delete = f.delete();
        }
        return delete;
    }
}
