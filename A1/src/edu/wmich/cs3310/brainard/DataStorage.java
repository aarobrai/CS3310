package edu.wmich.cs3310.brainard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * PROJECT: A1  CLASS: DataStorage.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 9/28/15
 *
 *
 * FILES ACCESSED:
 *
 * INPUT: Backup.csv - loaded into memory when UserApp uses the overloaded constructor to access services in this class
 * OUTPUT: Backup.csv - created when Setup finishes the initial insertion of the countries into the Binary Search Tree
 * Log.txt - the service methods called by UserApp publish their results to the log
 *
 * DESCRIPTION:
 *
 * This class contains the main data structure of this project, as well as the necessary methods to access and
 * manipulate said data structure.
 *
 * The RawData that is read by Setup is inserted into a Binary Search tree in this class.
 * The Binary Search Tree is comprised of an ArrayList of BSTnodes, a private class nested in DataStorage.
 * This class provides public service methods to insert a country, (static) delete country by its code,
 * and select a country by code. These last two use the search() method in order to find the correct
 * node for selection/deletion.
 *
 *
 * Setup writes the initial BST into the Backup.csv file during finishup(), which is loaded by UserApp in order to
 * process the TransData.
 * Two constructors are provided; the second overloaded constr. used by UserApp loads the backup file.
 * After UserApp is finished, it calls finishup() and writes the updated Binary Search Tree to the backup file.
 *
 *
 * *******************************************************************************
 */
public class DataStorage {

    //**************************** PRIVATE DECLARATIONS ************************
    private UIoutput log;
    private FileWriter backup;
    private int count = 0;
    private int rootPtr = -1;
    private int nextEmpty = 0;
    private int numVisited = 0;
    private BSTnode currentRecord;
    private ArrayList<BSTnode> BST;

    //**************************** PUBLIC CONSTRUCTOR(S) ************************
    public DataStorage() throws IOException {
        log = new UIoutput(false);
        //       log.displayThis("-->>DATASTORAGE started");
        BST = new ArrayList<BSTnode>();
    }

    //second constructor, used by userapp, using the backup already written by setup
    public DataStorage(UIoutput log) throws IOException {

        this.log = log;
        loadBackup();


    }
    //**************************** PUBLIC SERVICE METHODS ************************

    //**************************** INSERT ************************
    public void insertCountry(String numId, String countryCode, String name, String continent, String area,
                              String population, String lifeExpect) throws IOException {

        int i;
        int parentI = 0;
        numVisited = 0;

        //we will assign "TRUE" for L and "FALSE" for R
        boolean LorR = false;

        //create the node
        BSTnode newNode = new BSTnode(numId, countryCode, name, continent, area, population, lifeExpect);
        BST.add(newNode);

        if (rootPtr == -1) {
            rootPtr = 0;
            nextEmpty = 0;
        } else {
            i = rootPtr;
            while (i != -1) {
                parentI = i;
                //if less than
                if (BST.get(count).getCountryCode().compareTo(BST.get(i).getCountryCode()) < 0) {
                    i = BST.get(i).getLeftPtr();
                    numVisited++;
                    LorR = true;
                    //if greater than
                } else {
                    i = BST.get(i).getRightPtr();
                    LorR = false;
                    numVisited++;
                }
            }
            if (LorR == true) {
                BST.get(parentI).setLeftPtr(count);
            } else {
                BST.get(parentI).setRightPtr(count);
            }
        }

        log.displayThis("Ok, " + name + " inserted. " + numVisited + " nodes visited");

        nextEmpty++;
        count++;

        //snapShot();


    }

    //**************************** SELECT ************************
    public void selectByCountryCode(String selectedCountry) throws IOException {
        if (search(selectedCountry) == true) {

            log.displayThis("S " + selectedCountry + " >> " + numVisited + " nodes visited");
            log.displayThis("CDE ID- NAME-------------- CONTINENT---- ------AREA ---POPULATION LIFE ");
            log.displayThis(prettyPrintRecordToLogFile());

        } else {
            log.displayThis("S " + selectedCountry + " >> INVALID COUNTRY CODE \r\n");
        }
    }

    //**************************** DELETE ************************
    public void delete(String countryToDelete) throws IOException {

        numVisited = 0;

        if (search(countryToDelete) == true) {
            //delete country- tombstone it by setting it's id number to 0
            currentRecord.setId("0");
            log.displayThis("D " + countryToDelete + " >> ok, " + currentRecord.getName() + " deleted. " + numVisited +
                    " nodes visited");
        } else {
            log.displayThis("D " + countryToDelete + " >> INVALID COUNTRY CODE");
        }

    }

    //**************************** SHOW ALL************************
    public void showAll() throws IOException {

        log.displayThis("A >>");
        inOrderTraversal(rootPtr);

    }


    private void inOrderTraversal(int root) throws IOException {
        if (root == -1) {
            return;
        } else {

            //currentRecord = BST.get(root);
            inOrderTraversal(BST.get(root).getLeftPtr());
            //display the data of the node we're at, excluding tombstoned nodes
            currentRecord = BST.get(root);
            if (!currentRecord.getId().equals("0")) {


                System.out.println(prettyPrintRecordToLogFile());
                log.displayThis(prettyPrintRecordToLogFile());
            }
            inOrderTraversal(BST.get(root).getRightPtr());
        }
    }

    //**************************** SEARCH FUNCTION ************************
    public boolean search(String searchTarget) {

        numVisited = 0;
        int i = rootPtr;


        while (i != -1 && (BST.get(i).getCountryCode().contains(searchTarget) == false)) {

            int comparisonResult = searchTarget.compareTo(BST.get(i).getCountryCode());
            //if search target LESS than current node
            if (comparisonResult < 0) {
                i = BST.get(i).getLeftPtr();
            } else if (comparisonResult > 0) {
                //if search target GREATER than current node
                i = BST.get(i).getRightPtr();
            }

            numVisited++;
        }

        //handle search results
        if (i == -1) {
            return false;
        } else {

            //return false if the record is tombstoned as well
            if (BST.get(i).getId().equals("0")) {
                return false;
            }
            //store the corresponding data record in memory
            currentRecord = BST.get(i);
            return true;
        }
    }


    //**************************** PRIVATE HELPER METHODS ************************
    private void loadBackup() throws IOException {

        //load into the BST here
        String[] backupData;
        BufferedReader backupLoader = new BufferedReader(new FileReader("Backup.csv"));
        BST = new ArrayList<BSTnode>();


        backupLoader.readLine();

        String currentLine = backupLoader.readLine();
        while (currentLine != null) {
            backupData = currentLine.split(",");
            insertCountry(backupData[3], backupData[2], backupData[4], backupData[5], backupData[6], backupData[7],
                    backupData[8]);
            currentLine = backupLoader.readLine();
        }

        backupLoader.close();
    }

    private void createBackup() throws IOException {


        String[] currentNodeData;

        //write the header
        String headerRecord = rootPtr + "," + count + "," + nextEmpty;
        backup.write(headerRecord + "\r\n");
        int i = 0;
        while (i < BST.size()) {

            //backup.write(count + ",");
            backup.write(BST.get(i).getLeftPtr() + ",");
            backup.write(BST.get(i).getRightPtr() + ",");
            currentNodeData = BST.get(i).getData();
            for (int j = 0; j < currentNodeData.length; j++) {
                backup.write(currentNodeData[j] + ",");
            }

            backup.write("\r\n");
            i++;
        }

        backup.close();
    }

    private String prettyPrintRecordToLogFile() {


        String[] curNodeData = currentRecord.getData();
        //separate the fields of the String array
        String countryCode = curNodeData[0];
        int id = Integer.parseInt(curNodeData[1]);
        String name = curNodeData[2];
        String continent = curNodeData[3];
        Long area = Long.parseLong(curNodeData[4]);
        Long population = Long.parseLong(curNodeData[5]);
        float lifeExpect = Float.parseFloat(curNodeData[6]);

        //for(int i = 0; i < currentRecord.length; i++)
        //System.out.print(currentRecord[i]+",");
        // String formattedLine = String.format("  %s %s  %s  %s   %s   %,10d  %5s  %,13d   %.1f");


        String formattedLine = String.format("%-3s %-3d %-18s %-13s %10d %13d %.1f", countryCode, id, name, continent,
                area, population, lifeExpect);

        return formattedLine;


    }


    //**************************** FINISH UP... LAST METHOD IN CLASS ************************
    public void finishUp() throws IOException {

        backup = new FileWriter("Backup.csv");
        log.displayThis("-->>OPENED Backup.csv file");
        createBackup();

        log.displayThis("-->> CLOSED Backup.csv file");
        backup.close();
        log.displayThis("-->>DATASTORAGE finished");


    }


    //**************************** PRIVATE BST NODE CLASS *************************
    private class BSTnode {

        //**************************** PRIVATE DECLARATIONS *************************

        //fields for single BSTnodes hung onto the tree
        private int leftPtr;
        private int rightPtr;

        //countrycode is the search key
        private int id;
        private String countryCode;
        private String[] data;

        //**************************** PUBLIC CONSTRUCTOR *************************
        public BSTnode(String id, String code, String name, String continent,
                       String area, String population, String lifeExpect) {


            leftPtr = -1;
            rightPtr = -1;

            countryCode = code;

            data = new String[7];
            data[0] = code;
            data[1] = id;
            data[2] = name;
            data[3] = continent;
            data[4] = area;
            data[5] = population;
            data[6] = lifeExpect;


        }

        //**************************** GETTERS AND SETTERS *************************


        public int getLeftPtr() {
            return leftPtr;
        }

        public void setLeftPtr(int leftPtr) {
            this.leftPtr = leftPtr;
        }

        public int getRightPtr() {
            return rightPtr;
        }

        public void setRightPtr(int rightPtr) {
            this.rightPtr = rightPtr;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String[] getData() {
            return data;
        }

        public void setId(String id) {
            data[1] = id;
        }

        public String getId() {
            return data[1];
        }

        public String getName() {
            return data[2];
        }

    }

}

