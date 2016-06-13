import java.io.IOException;

/**
 * PROJECT: A3  CLASS: Hasher
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: RawData files (thru RawData class), Log.txt for output
 *
 *
 *
 *
 * DESCRIPTION:
 * Reads rawdata file and creates a hash table with different hash function, CRA, maxNHomeLoc depending
 * on what was called from the main Controller. It then calculates the average
 * search path for the hash table created and prints out the stats to the log. If using
 * 26 country test data, prints out the hash table as well.
 * When finishUp is called, overwrites the hash table array to "null" to allow reuse on the next call.
 *
 * Apparently Java doesn't like when you name a Class "HashTable". Probably because it's built in somewhere
 */
public class Hasher {

    //**************************** PRIVATE DECLARATIONS *************************
    private final int MAX_SIZE = 500;

    private Output log;
    private RawData rd;
    private CountryNode[] table;
    private int numRec;
    private int nColl;
    private int totalColl;
    private int numInHome;
    private int maxNHomeLoc;
    private int whichCRA;
    private int whichHashFunction;
    private String whichData;
    private boolean usingTestData;
    private int wCase = 1;

///////////////////////////////////////////// HASH TABLE & ITS FUNCTIONS ///////////////////////////////////////////////////
    public void createHashTable(String whichData, int whichFunction, int whichCRA, int maxNHomeLoc) throws IOException {

        log = new Output(false);
        //parameters made available to rest of class, specficially so we can print them to log in printHashTable()
        this.maxNHomeLoc = maxNHomeLoc;
        this.whichHashFunction = whichFunction;
        this.whichCRA = whichCRA;
        this.whichData = whichData;

        //resetting counters
        numInHome = 0;
        nColl = 0;
        //allocate memory for the table
        table = new CountryNode[MAX_SIZE];

        if (whichData.contains("All")) {
            numRec = 239;
            //printout needs to know this since it only prints the table when using test data
            usingTestData = false;
        } else if (whichData.contains("Test")) {
            numRec = 26;
            usingTestData = true;
        }
        this.maxNHomeLoc = maxNHomeLoc;
        rd = new RawData(whichData);
        rd.loadDataIntoMem();
        int homeLoc;
        for (int i = 0; i < numRec; i++) {
            //getting home location for country
            switch (whichFunction) {
                case 1:
                    homeLoc = hashFunction1(rd.getCode(i), maxNHomeLoc);
                    break;
                case 2:
                    homeLoc = hashFunction2(rd.getCode(i), maxNHomeLoc);
                    break;
                case 3:
                    homeLoc = hashFunction3(rd.getCode(i), maxNHomeLoc);
                    break;
                case 4:
                    homeLoc = hashFunction4(rd.getCode(i), maxNHomeLoc);
                    break;
                default:
                    System.out.println("Something went wrong, unable to get a hash function");
                    return;
            }
            //insert the country node
            if (isEmpty(homeLoc)) {
                table[homeLoc] = new CountryNode(rd.getCode(i), rd.getId(i), -1);
                //System.out.println(rd.getCode(i) + " inserted at homeLoc " + homeLoc);
                numInHome++;
                //collision resolution occurs here
            } else {
                switch (whichCRA) {
                    case 1:
                        homeLoc = linearEmbedded(homeLoc);
                        table[homeLoc] = new CountryNode(rd.getCode(i), rd.getId(i), -1);
                        nColl++;
                        break;
                    case 2:
                        homeLoc = chainingSeparate(homeLoc);
                        table[homeLoc] = new CountryNode(rd.getCode(i), rd.getId(i), -1);
                        nColl++;
                        break;
                    default:
                        System.out.println("ERROR, unable to select a CRA");
                        break;
                }
            }
        }
        printHashTable();
    }



    /****************************** HASH FUNCTIONS ***********************************************/

    //they output homeAddress
    public int hashFunction1(String countryCode, int maxNHomeLoc) {

        char[] letters = countryCode.toCharArray();
        return ((int) letters[0] * (int) letters[1] * (int) letters[2]) % maxNHomeLoc;

    }

    public int hashFunction2(String countryCode, int maxNHomeLoc) {

        char[] letters = countryCode.toCharArray();
        return (int) ((letters[0] + (int) letters[1] + (int) letters[2]) % maxNHomeLoc);
    }

    public int hashFunction3(String countryCode, int maxNHomeLoc) {

        char[] letters = countryCode.toCharArray();
        return (int) (concatenateNums((int) letters[0], (int) letters[1], (int) letters[2]) % maxNHomeLoc);
    }

    public int hashFunction4(String countryCode, int maxNHomeLoc) {

        char[] letters = countryCode.toCharArray();
        return (int) (concatenateNums((int) letters[2], (int) letters[1], (int) letters[0]) % maxNHomeLoc);
    }



    /******************************* CRAs ************************************************/

    //the CRAs output the new homeAddress of the country that collided with a non-empty location
    private int chainingSeparate(int oldHomeLoc) {


        int spotTaken = maxNHomeLoc;
        int nodeToLink = oldHomeLoc;
        while (table[nodeToLink].getLink() != -1) {
            nodeToLink = table[nodeToLink].getLink();
        }

        while (!isEmpty(spotTaken)) {
            spotTaken++;
        }

        table[nodeToLink].setLink(spotTaken);
        return spotTaken;
    }

    private int linearEmbedded(int oldHomeLoc) {

        int spotTaken = oldHomeLoc;

        while (!isEmpty(spotTaken)) {
            spotTaken++;
            if (spotTaken >= maxNHomeLoc) {
                spotTaken = 0;
            }

        }

        return spotTaken;

    }


    /****************************** AVG SEARCH PATH ***********************************************/
    private float calculateAvgSearchPath(int maxNHomeLoc) throws IOException {

        String searchQuery;
        int numComparisons = nColl;
        int currentLoc;
        float result;

        //linear-embedded average
        for (int i = 0; i < numRec; i++) {
            searchQuery = rd.getCode(i);
            //get the home location our query should be in based on hash function
            switch (whichHashFunction) {
                case 1:
                    currentLoc = hashFunction1(searchQuery, maxNHomeLoc);
                    break;
                case 2:
                    currentLoc = hashFunction2(searchQuery, maxNHomeLoc);
                    break;
                case 3:
                    currentLoc = hashFunction3(searchQuery, maxNHomeLoc);
                    break;
                case 4:
                    currentLoc = hashFunction4(searchQuery, maxNHomeLoc);
                    break;
                default:
                    System.out.println("Error in choosing hash function to calculate average search path");
                    currentLoc = 0;
                    break;
            }

            //get the search stats for linear-embedded
            if (whichCRA == 1) {
                while (!searchQuery.equals(table[currentLoc].getCode())) {
                    currentLoc++;
                    //wrap around to the front once we reach the end of the hashtable
                    if (currentLoc >= maxNHomeLoc) {
                        currentLoc = 0;
                    }
                    numComparisons++;
                }
                //get search stats for chaining-separate
            } else if (whichCRA == 2) {
                int numInSynFam = 0;
                while (!searchQuery.equals(table[currentLoc].getCode())) {
                    currentLoc = table[currentLoc].getLink();
                    numInSynFam++;
                }
                //using a modified (n(n+1))/2 to sum up the syn fams easier
                numComparisons += ((numInSynFam + 1 * (numInSynFam + 2)) / 2) - 1;
            }


        }
        //need this assignment so the printer method can see the number of comparisons
        totalColl = numComparisons;
        //finally, calculate the result
        result = (float) (numComparisons + numInHome) / (numInHome + nColl);


        return result;
    }

/////////////////////////////////////////// OTHER PRIVATE HELPER METHODS//////////////////////////////////////////////
    private int concatenateNums(int num1, int num2, int num3) {
        return Integer.valueOf(String.valueOf(num1) + String.valueOf(num2) + String.valueOf(num3));
    }

    private boolean isEmpty(int location) {
        if (table[location] == null) {
            return true;
        } else {

            return false;
        }
    }


    private void printHashTable() throws IOException {


        log.displayThis("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        log.displayThis("CASE: " + wCase);
        wCase++;
        if(usingTestData){
            log.displayThis("RAW DATA FILE:  Test");
        }else{
            log.displayThis("RAW DATA FILE:  All");
        }
        log.displayThis("HASH FUNCTION:  " + whichHashFunction + "  " +
                "(with maxNHomeLoc:  " + maxNHomeLoc + ")");
        if (whichCRA == 1) {
            log.displayThis("COL RESOL ALG:  " + whichCRA + "  (Linear, Embedded)");
        } else if (whichCRA == 2) {
            log.displayThis("COL RESOL ALG:  " + whichCRA + "  (Chaining, Separate)");
        }
        log.displayThis("N_HOME: " + numInHome + ",  N_COLL:  " + nColl + "  --> " + (numInHome + nColl));

        float avg = calculateAvgSearchPath(maxNHomeLoc);
        log.displayThis(String.format("AVE SEARCH PATH (for successful):  (%d + %d) / (%d + %d) --> %.1f",
                numInHome, totalColl, numInHome, nColl, avg));

        if (usingTestData) {
            int numToPrint;
            log.displayThis("HASH TABLE:");
            if (whichCRA == 1) {
                numToPrint = maxNHomeLoc;
                log.displayThis("LOC  CODE DRP");

            } else {
                numToPrint = maxNHomeLoc + nColl;
                log.displayThis("LOC  CODE DRP LINK ");
            }
            for (int i = 0; i < (numToPrint); i++) {
                if (isEmpty(i)) {
                    log.displayThis(String.format("%03d>", i));
                } else {
                    if(whichCRA == 1){
                        log.displayThis(String.format("%03d> %-4s %03d", i, table[i].getCode(),
                                table[i].getDataRecordPointer()));
                    }else{
                        log.displayThis(String.format("%03d> %-4s %03d %03d", i, table[i].getCode(),
                                table[i].getDataRecordPointer(), table[i].getLink()));
                    }



                }
            }
        } else {
            log.displayThis("HASH TABLE: big table --> saved paper by not printing it ");
        }
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //sets memory for table to null so we can use it again
    public void finishUp() throws IOException {
        for (int i = 0; i < table.length; i++) {
            table[i] = null;
        }
        log.finishUp();
    }

}
