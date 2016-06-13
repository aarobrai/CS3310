package brainard.a2;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;

/**
 * PROJECT: A1  CLASS: DataStorage.java
 * AUTHOR: Aaron Brainard
 * MODIFIED: 10/12/15
 *
 *
 * FILES ACCESSED:
 *
 * INPUT: DataStorage.bin- checks for empty records and used for reading records to report info in the log file
 * OUTPUT: DataStorage.bin- records inserted by ID number
 * Log.txt- The main output file
 *
 *
 * DESCRIPTION:
 *
 * This class contains the main data structure of this project, as well as the necessary methods to access and
 * manipulate said data structure.
 *
 * For A2 we have changed the data structure to a Binary File. Records are inserted by country ID number (rrn or
 * relative record number) at a byte offset calculated each time a record is accessed.
 *
 * *******************************************************************************
 */
public class DataStorage {

    //**************************** PRIVATE DECLARATIONS OF CONSTANTS ************************

    private final short SIZE_OF_INT = 4;
    private final short SIZE_OF_LONG = 8;
    private final short SIZE_OF_SHORT = 2;
    private final short SIZE_OF_FLOAT = 4;
    private final short SIZE_OF_DOUBLE = 8;
    private final short SIZE_OF_A_CHAR = 2;


    //these were originally shorts- java apparently wants short math to return ints
    private final short SIZE_OF_CODE = 3 * SIZE_OF_A_CHAR;
    private final short SIZE_OF_NAME = 18 * SIZE_OF_A_CHAR;
    private final short SIZE_OF_CONT = 13 * SIZE_OF_A_CHAR;
    private final short SIZE_OF_ID = SIZE_OF_SHORT;
    private final short SIZE_OF_AREA = SIZE_OF_INT;
    private final short SIZE_OF_POP = SIZE_OF_LONG;
    private final short SIZE_OF_LIFE = SIZE_OF_FLOAT;

    private final int SIZE_OF_DATA_REC = SIZE_OF_CODE + SIZE_OF_ID
            + SIZE_OF_NAME + SIZE_OF_CONT + SIZE_OF_AREA
            + SIZE_OF_POP + SIZE_OF_LIFE;

    private final short SIZE_OF_N = SIZE_OF_SHORT;
    private final short SIZE_OF_MAX = SIZE_OF_SHORT;
    private final int SIZE_OF_HEADER_REC = SIZE_OF_N + SIZE_OF_MAX;


    //**************************** OTHER PRIVATE DECLARATIONS ************************

    private UIoutput log;
    private RandomAccessFile mainData;

    //data fields for counting things up
    private int n;
    private int maxId;
    private int numErrors = 0;

    //data fields for the countries
    private int rrn;
    private char[] countryCode = new char[3];
    private char[] countryName = new char[18];
    private char[] continent = new char[13];
    private int area;
    private long population;
    private float lifeExpect;


    //**************************** PUBLIC CONSTRUCTOR(S) ************************
    public DataStorage(UIoutput log) throws IOException {
        this.log = log;
        log.displayThis("-->> DATASTORAGE started");

        //set up the storage file
        String fileName = "DataStorage.bin";
        mainData = new RandomAccessFile(fileName, "rw");
        log.displayThis("-->> OPENED " + fileName + " file");

        setHeaderRecord();
    }

    //**************************** PUBLIC SERVICE METHODS ************************

    //**************************** INSERT ************************

    public void insertCountry(String numId, String countryCode, String name, String continent, String area,
                              String population, String lifeExpect, boolean isSetup) throws IOException {

        this.rrn = Integer.parseInt(numId);

        //checking IDs outside of the 239 countries
        if (rrn <= 0 || rrn > 239) {
            log.displayThis(" >> invalid country id");
            log.displayThis("");
            numErrors++;
            return;
        }


        this.countryCode = countryCode.trim().toCharArray();

        countryName = padRight(name, 18);

        this.continent = padRight(continent, 13);

        this.area = Integer.parseInt(area);

        this.population = Long.parseLong(population);

        this.lifeExpect = Float.parseFloat(lifeExpect);
        //checking unreasonable lifeExpect- can be increased if humans start to live longer
        if (this.lifeExpect > 150) {
            log.displayThis(" >> ERROR: Failed to insert " + name +
                    ", life expectancy value too high (" + this.lifeExpect + ")");
            log.displayThis("");
            numErrors++;
            return;
        }

        if (isEmpty(rrn)) {
            writeOneRecord(rrn);
            if (!isSetup) {
                log.displayThis("   >> Ok, " + name + " inserted");
                log.displayThis("");
            }

        } else {
            //need to examine the record already in this spot so we can report what it is
            read1Rec(rrn);
            if (isSetup) {
                log.displayThis("ERROR: DUPLICATE ID " + numId + " when inserting " + name + ", " +
                        new String(this.countryName).trim() + " has id " + rrn);
            } else {
                log.displayThis("   >> invalid (duplicate) country id");
                log.displayThis("");
            }
            numErrors++;
            return;
        }
    }


    //**************************** SELECT ************************
    public void selectByCountryId(int countryId) throws IOException {

        if (countryId < 1 || countryId > maxId) {
            log.displayThis("S " + countryId + " invalid country id");
            log.displayThis("");
            numErrors++;
            return;
        }
        if (isEmpty(countryId)) {
            log.displayThis("S " + countryId + " invalid country id");
            log.displayThis("");
            numErrors++;
            return;
        } else {
            log.displayThis("S " + countryId + " >>");
            read1Rec(countryId);
            log.displayThis("CDE ID- NAME-------------- CONTINENT---- ------AREA ---POPULATION LIFE");
            log.displayThis(prettyPrintThisRecord());
            log.displayThis("");
        }


    }

    //**************************** DELETE ************************
    public void delete(String countryToDelete) throws IOException {

        int rrn = Integer.parseInt(countryToDelete);
        if (rrn < 1 || rrn > maxId) {
            log.displayThis("D " + rrn + " >> invalid country id");
            log.displayThis("");
        } else {
            if (!isEmpty(rrn)) {
                read1Rec(rrn);
                mainData.seek(calculateByteOffset(rrn));
                //write over ALL the fields to make them empty
                mainData.writeShort(0);
                for(int i = 0; i < 3; i++){
                    mainData.writeChar('\0');
                }
                for(int i = 0; i < 18; i++){
                    mainData.writeChar('\0');
                }
                for(int i = 0; i < 13; i++){
                    mainData.writeChar('\0');
                }
                mainData.writeInt(0);
                mainData.writeLong(0);
                mainData.writeFloat(0);
                n--;
                log.displayThis("D " + rrn + " >> ok, " + new String(this.countryName).trim() + " deleted");
                log.displayThis("");
            } else {
                log.displayThis("D " + rrn + " >> invalid country id");
                log.displayThis("");
            }

        }

    }

    //**************************** SHOW ALL************************

    public void showAll() throws IOException {

        log.displayThis("> A");
        int loc = 1;
        log.displayThis("CDE ID- NAME-------------- CONTINENT---- ------AREA ---POPULATION LIFE");
        for (int i = 0; i < maxId; i++, loc++) {
            read1Rec(loc);
            if (this.rrn != 0) {
                log.displayThis(prettyPrintThisRecord());
            }
        }

        log.displayThis("===========================");


    }

    //**************************** PRIVATE HELPER METHODS ************************
    private int calculateByteOffset(int rrn) {
        return SIZE_OF_HEADER_REC + ((rrn - 1) * SIZE_OF_DATA_REC);
    }


    private void writeOneRecord(int rrn) throws IOException {

        if (rrn > maxId) {
            maxId = rrn;
        }

        int byteOffset = calculateByteOffset(rrn);
        mainData.seek(byteOffset);

        //this is the country ID used for selection
        mainData.writeShort(rrn);

        for (int i = 0; i < 3; i++) {
            mainData.writeChar(countryCode[i]);
        }

        for (int i = 0; i < 18; i++) {
            mainData.writeChar(countryName[i]);
        }

        for (int i = 0; i < 13; i++) {
            mainData.writeChar(continent[i]);
        }

        mainData.writeInt(area);
        mainData.writeLong(population);
        mainData.writeFloat(lifeExpect);

        n++;

    }

    private boolean isEmpty(int rrn) throws IOException {

        mainData.seek(calculateByteOffset(rrn));
        byte[] isThisEmpty = new byte[2];
        mainData.read(isThisEmpty);
        if (isThisEmpty[0] == 0 && isThisEmpty[1] == 0) {
            return true;
        } else {
            return false;
        }


    }

    private char[] padRight(String s, int n) {

        s.trim();
        s = String.format("%1$-" + n + "s", s);
        //chop off the rest of the field if it's longer than the specified size
        s = s.substring(0, n);
        return s.toCharArray();
    }

    private char[] padLeft(String s, int n) {

        s.trim();
        s = String.format("%1$" + n + "s", s);
        //chop off the rest of the field if it's longer than the specified size
        s = s.substring(0, n);
        return s.toCharArray();
    }

    private void setHeaderRecord() throws IOException {


        mainData.seek(0);

        try {
            n = mainData.readShort();
            System.out.println("Set n to " + n);
        } catch (NumberFormatException e) {
            System.out.println("ERROR, no n found in the file");
        } catch (EOFException f) {
            System.out.println("ERROR, EOF REACHED");
        }

        try {
            mainData.seek(2);
            maxId = mainData.readShort();
        } catch (NumberFormatException e) {
            System.out.println("ERROR, no maxId found in the file");
        } catch (EOFException f) {
            System.out.println("ERROR, EOF REACHED");
        }


    }


    private void writeHeaderRecord() throws IOException {

        // System.out.println(String.format("n is %d , maxId is %d", n, maxId));

        mainData.seek(0);
        mainData.writeShort(n);
        mainData.writeShort(maxId);


    }

    private void read1Rec(int rrn) throws IOException {
        int byteOffSet = calculateByteOffset(rrn);

        mainData.seek(byteOffSet);
        this.rrn = mainData.readShort();
        try {
            for (int i = 0; i < 3; i++) {
                this.countryCode[i] = mainData.readChar();
            }
            for (int i = 0; i < 18; i++) {
                this.countryName[i] = mainData.readChar();
            }
            for (int i = 0; i < 13; i++) {
                this.continent[i] = mainData.readChar();
            }
        } catch (NullPointerException e) {
            System.out.println("Record " + this.rrn + " must be empty???");
            e.printStackTrace();
        }
        this.area = mainData.readInt();
        this.population = mainData.readLong();
        this.lifeExpect = mainData.readFloat();


    }

    private String prettyPrintThisRecord() {
        String s = String.format("%-3s %03d %-18s %-13s", new String(this.countryCode), this.rrn, new String(this.countryName), new String(this.continent)) + String.format("%11.11s %13.13s %4.4s", NumberFormat.getIntegerInstance().format(this.area), NumberFormat.getIntegerInstance().format(this.population), this.lifeExpect);
        return s;
    }


    //**************************** FINISH UP... LAST METHOD IN CLASS ************************
    public void finishUp() throws IOException {

        writeHeaderRecord();
        log.displayThis("-->> CLOSED MainData file");
        mainData.close();
        log.displayThis("-->> DATASTORAGE finished with " + numErrors + " errors");


    }
}

