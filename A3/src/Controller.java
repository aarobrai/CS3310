import java.io.IOException;

/**
 * PROJECT: A3  CLASS: Controller
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: None directly
 *
 *
 *
 *
 * DESCRIPTION: Uses a Hasher object to run test cases on different
 * hash functions, CRAs, maxNHomeLoc, and test data.
 *
 */
public class Controller {

    private static Hasher hasher;

    public static void main(String[] args) throws IOException {

        hasher = new Hasher();
        //case 1
        hasher.createHashTable("RawDataTest.csv", 1, 2, 20);
        hasher.finishUp();
        //case 2
        hasher.createHashTable("RawDataTest.csv", 2, 2, 20);
        hasher.finishUp();
        //case 3
        hasher.createHashTable("RawDataTest.csv", 3, 2, 20);
        hasher.finishUp();
        //case 4
        hasher.createHashTable("RawDataTest.csv", 4, 2, 20);
        hasher.finishUp();
        //case 5
        hasher.createHashTable("RawDataTest.csv", 1, 2, 30);
        hasher.finishUp();
        //case 6
        hasher.createHashTable("RawDataTest.csv", 1, 1, 30);
        hasher.finishUp();
        //case 7
        hasher.createHashTable("RawDataAll.csv", 1, 2, 240);
        hasher.finishUp();
        //case 8
        hasher.createHashTable("RawDataAll.csv", 1, 2, 260);
        hasher.finishUp();
        //case 9
        hasher.createHashTable("RawDataAll.csv", 1, 2, 350);
        hasher.finishUp();
        //case 10
        hasher.createHashTable("RawDataAll.csv", 1, 1, 240);
        hasher.finishUp();
        //case 11
        hasher.createHashTable("RawDataAll.csv", 1, 1, 260);
        hasher.finishUp();
    }
}
