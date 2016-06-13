import java.io.*;

/**
 * PROJECT: A5
 * AUTHOR:  aaronbrainard
 * DATE:    11/12/2015
 *
 * FILES ACCESSED: Input: CodeIndex?.bin files
 * Output: Log.txt
 *
 *
 *
 *
 * DESCRIPTION: Provides the services to Select keyValues from the CodeIndex.
 * Reads and searches nodes from the code index (implemented as an m-way tree),
 * then uses DataStorage to access specific data records as given by the
 * code index.
 */
public class CodeIndex {

    /**
     * ********************** CONSTANTS *****************************************************
     */

    final private int SIZE_OF_SHORT = 2;
    final private int SIZE_OF_KEYVAL = 3;

    final private int SIZE_OF_HEADER_REC = 3 * SIZE_OF_SHORT;
    final private int SIZE_OF_TRIPLE = SIZE_OF_SHORT + SIZE_OF_KEYVAL + SIZE_OF_SHORT;

    /**
     * ********************** PRIVATE DECLARATIONS ******************************************
     */

    //header record contents
    private short m; //M-way tree
    private short rootPtr;
    private short n;

    //calculated in constructor after m is read
    private int dataRecSize;

    //storage for a single BTreeNode
    private short[] treePtrs;
    private String[] keyVals;
    private short[] dataRecPtrs;

    //file and class services objects
    private RandomAccessFile inFile;
    private DataStorage dataStorage;
    private Output log;

    /**
     * ********************** PUBLIC CONSTRUCTOR *****************************************************
     */

    public CodeIndex(String fileNameSuffix, Output log) throws IOException {

        this.log = log;

        try {
            inFile = new RandomAccessFile("CodeIndex" + fileNameSuffix + ".bin", "r");
            readHeaderRec();
            dataRecSize = calculateRecSize(m);

            //initialize storage for a node with respect to m
            treePtrs = new short[m];
            keyVals = new String[m];
            keyVals[m - 1] = "]]]";
            dataRecPtrs = new short[m - 1];

            System.out.println("***Data record size for " + m + " way tree is " + dataRecSize + " bytes***");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: CodeIndex" + fileNameSuffix + " file not found");
            e.printStackTrace();
        }

        dataStorage = new DataStorage(fileNameSuffix);

    }

    /**
     * ********************** PUBLIC SERVICE METHOD(S) *****************************************************
     */

    public void selectByCode(String keyValue) throws IOException {

        short nodesRead = 0;
        //start with the root node
        read1Node(rootPtr);
        nodesRead++;
        int[] result = search1Node(keyValue);


        //     haven't gotten a DRP and haven't gotten a treePtr of -1, which indicates unsuccessful search
        //                                         (don't fall off the edge!)
        while (result[0] != 0 && result[1] != -1 && nodesRead < m) {
            //read the node at the treePointer returned from search
            read1Node(result[1]);
            nodesRead++;
            result = search1Node(keyValue);
        }

        //handle search results

        //successful
        if (result[1] > 0) {
            //direct address to the DataStorage record at its DRP
            log.displayThis("S " + keyValue + " >> " + dataStorage.retrieveRecord(result[1]) +
                    " [# nodes read:  " + nodesRead + "]");
        //unsuccessful (result is a treePtr of -1 meaning we've searched all the way to leaf level
        } else {
            log.displayThis("S " + keyValue + " >> ERROR - code not found  [# nodes read:  " +
                    nodesRead + "]");
        }

    }

    /**
     * ********************** PRIVATE HELPER METHODS *****************************************************
     */

    private void read1Node(int rrn) throws IOException {
        inFile.seek(calculateByteOffSet(rrn));
        System.out.println("Reading node at " + rrn + " ...");
        //reading in a SINGLE node's fields
        for (int i = 0; i < (m - 1); i++) {
            String curKeyVal = "";
            treePtrs[i] = inFile.readShort();
            for (int x = 0; x < 3; x++) {
                curKeyVal += (char) inFile.read();
            }
            keyVals[i] = curKeyVal;
            dataRecPtrs[i] = inFile.readShort();

        }
        treePtrs[(m - 1)] = inFile.readShort();
    }

    /**
     * Searches the current node in memory for the keyvalue
     *
     * @return a DRP if keyValue is found, else a TP if not found.
     * This is done by returning an int array of size 2. If retVal[0] == 0, it's a DRP.
     * Else if retVal[0] == 1, then it's a TP.
     */
    private int[] search1Node(String target) {

        int[] aPointer = new int[2];
        int i = 0;
        //Linear search WITHIN THE CURRENT NODE ONLY
        int comparison;
        while ((comparison = target.compareTo(keyVals[i])) >= 0 && i < (m - 1)) {
            //match case
            if (comparison == 0) {
                //indicate this is a DRP
                aPointer[0] = 0;
                aPointer[1] = dataRecPtrs[i];
                return aPointer;
            }
            i++;
        }
        //'else' return treePointer to the next node to search
        aPointer[0] = 1;
        aPointer[1] = treePtrs[i];
        System.out.println(aPointer[0] + " Will go to subtree node at " + aPointer[1]);
        return aPointer;
    }


    private void readHeaderRec() throws IOException {
        inFile.seek(0);
        m = inFile.readShort();
        rootPtr = inFile.readShort();
        System.out.println("m is " + m + " rootPtr is " + rootPtr);
        //don't really need n for A5 but I'm keeping it for my own purposes
        n = inFile.readShort();
        System.out.println("n is " + n);
    }

    private int calculateRecSize(int m) {
        /*
         * There are m-1 triples in each record. Each containing 2byte treePointer,
         * 3byte keyValue, and 2byte dataRecPointer. Then there is a final 2byte
         * treePointer at the end of a node.
         */
        return ((m - 1) * SIZE_OF_TRIPLE) + 2;
    }

    private long calculateByteOffSet(int rrn) {
        return SIZE_OF_HEADER_REC + ((rrn - 1) * dataRecSize);
    }

    private void printCurrentNodeInMem() {
        for (int k = 0; k < (m - 1); k++) {
            System.out.print(treePtrs[k] + " ");
            System.out.print(keyVals[k] + " ");
            System.out.print(" ");
            System.out.print(String.format("%03d ", dataRecPtrs[k]) + " ");
        }
        System.out.print(treePtrs[m - 1]);
        System.out.println();
    }

    /**
     * ********************** CLEANUP *****************************************************
     */

    public void finishUp() throws IOException {
        inFile.close();
        dataStorage.finishUp();
    }
}
