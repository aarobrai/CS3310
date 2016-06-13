import java.io.File;
import java.io.IOException;

/**
 * PROJECT: A5
 * AUTHOR:  aaronbrainard
 * DATE:    11/12/2015
 *
 * FILES ACCESSED: Deletes Log.txt if it exists
 *
 *
 *
 *
 * DESCRIPTION: Driver program. Deletes Log.txt, then uns userAppMain for each set of data files.
 */
public class Controller {

    /**
     * ********************** PRIVATE DECLARATIONS *****************************************************
     */

    private static UI ui;

    /**
     * ********************** MAIN METHOD *****************************************************
     */
    public static void main(String[] args) throws IOException {

        deleteFile("Log.txt");

        for (int i = 0; i < 3; i++) {
            userAppMain(Integer.toString(i + 1));
        }


    }

    /**
     * ********************** PRIVATE HELPER METHODS *****************************************************
     */

    private static void userAppMain(String fileSuffix) throws IOException {
        ui = new UI(fileSuffix);
        ui.processTransactions();
        ui.finishUp();

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

