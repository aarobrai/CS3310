import java.io.*;

/**
 * PROJECT: A6
 * AUTHOR:  aaronbrainard
 * DATE:    11/23/2015
 *
 * FILES ACCESSED: Input: MapGraph.bin
 * CityNameList.csv
 *
 *
 *
 *
 * DESCRIPTION: handles everything to do with MapGraph.bin &
 * CityNameList.csv files. Provides services used by DrivingApp
 * to access the data in these files
 */
public class Map {

    /**
     * ********************* PRIVATE DECLARATIONS ******************************
     */

    private final int CITY_BYTE_OFFSET = 16;

    private short n;
    private RandomAccessFile cityNamesList;
    private RandomAccessFile mapData;

    /**
     * ************************************** PUBLIC CONSTRUCTOR ***********************************
     */
    //opens MapGraph and CityNameList files, and reads n from MapGraph file
    public Map() throws IOException {
        try {
            mapData = new RandomAccessFile("MapGraph.bin", "r");
            mapData.seek(0);
            n = mapData.readShort();
            System.out.println("MAP class has n as " + n);
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Map Data File Not Found");
            e.printStackTrace();
        }

        //now open the CityNameList file. Fixed length records from Setup, so it's direct address
        try {
            cityNamesList = new RandomAccessFile("CityNamesList.csv", "r");
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: CityNameList File Not Found");
            e.printStackTrace();
        }
    }

    /**
     * ********************* PUBLIC SERVICE METHODS ***********************************
     */

    public String getCityName(short cityNumber) throws IOException {
        cityNamesList.seek(cityNumber * CITY_BYTE_OFFSET);
        return cityNamesList.readLine().split(",")[0];
    }

    public String getCityCode(short cityNumber) throws IOException {
        cityNamesList.seek(cityNumber * CITY_BYTE_OFFSET);
        return cityNamesList.readLine().split(",")[1];
    }

    public short getCityNumber(String cityName) throws IOException {
        cityNamesList.seek(0);
        //capitalize the first letter of the input string
        String name = cityName.substring(0, 1).toUpperCase() + cityName.substring(1);
        short i = 0;
        String s;
        while (i < n) {
            s = cityNamesList.readLine().split(" ")[0].trim();
            if (name.contains(s)) {
                return i;
            }
            i++;
        }
        return -1;

    }

    /*
     * NOTE: row > column for the lower-left triangle, so row & column
     *              could be EITHER: city1 & city2 OR city2 & city1
     */
    public short getRoadDistance(short cityNum1, short cityNum2) throws IOException {
        if (cityNum1 == cityNum2) {
            return 0;
        } else if (cityNum1 < cityNum2) {
            mapData.seek(calculateByteOffset(cityNum2, cityNum1));
        } else {
            mapData.seek(calculateByteOffset(cityNum1, cityNum2));
        }
        short distance = mapData.readShort();
        return distance;

    }

    public short getN() {
        return n;
    }

    /**
     * ********************* PRIVATE HELPER METHODS ***********************************
     */

    private long calculateByteOffset(int row, int col) {
        long result = (row * (row - 1)) + (col * 2) + 2;
        //System.out.println("seeking to " + result);
        return result;
    }

    /**
     * ********************* CLEANUP **************************************************
     */
    public void finishUp() throws IOException {
        mapData.close();
        cityNamesList.close();
    }
}
