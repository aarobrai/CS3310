import java.io.File;
import java.io.IOException;

/**
 * PROJECT: A6
 * AUTHOR:  aaronbrainard
 * DATE:    11/30/2015
 *
 * FILES ACCESSED: MapGraph.bin (to delete)
 *
 *
 *
 *
 * DESCRIPTION: Deletes MapGraph.bin since it gets built in setup
 * Runs setup, prettyprint, then drivingapp
 */
public class Driver {

    public static void main(String[] args) throws IOException {
        deleteFile("MapGraph.bin");
        Setup.main(new String[]{});
        PrettyPrint.main(new String[]{});
        DrivingApp.main(new String[]{});

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
