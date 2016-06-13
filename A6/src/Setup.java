import java.io.*;

/**
 * PROJECT: A6
 * AUTHOR:  aaronbrainard
 * DATE:    11/23/2015
 *
 * FILES ACCESSED: Input: EuropeMapData.csv
 * Output: MapGraph.bin
 * <
 * DESCRIPTION: BUILDS MapGraph.bin & CityNameList.csv files
 * from the raw data file, EuropeMapData.csv.
 */
public class Setup {

    private static RandomAccessFile mapGraph;
    private static BufferedReader europeMapData;
    private static short[] rows;
    private static short[] cols;
    private static short[] distances;

    public static void main(String[] args) throws FileNotFoundException, IOException {


        europeMapData = new BufferedReader(new FileReader("EuropeMapData.csv"));
        //DataOutputStream mapGraph = new DataOutputStream(new FileOutputStream("MapGraph.bin"));
        mapGraph = new RandomAccessFile("MapGraph.bin", "rw");
        mapGraph.seek(0);

        //throw out succeeding "%" comment lines
        String currentLine = "";
        for (int i = 0; i < 7; i++) {
            currentLine = europeMapData.readLine();
        }

        /**********************
         * Read n and write it as header record
         **********************/
        String[] fields = currentLine.split(",");

        System.out.println("Writing n: " + fields[1]);
        short n = Short.valueOf(fields[1]);
        mapGraph.writeShort(Short.valueOf(fields[1]));


        //throw out succeeding "%" comment lines
        for (int i = 0; i < 5; i++) {
            currentLine = europeMapData.readLine();
        }

        /**********************
         * Build CityNameList file
         **********************/
        FileWriter cityPairs = new FileWriter("CityNamesList.csv");
        for (int i = 0; i < n; i++) {
            fields = currentLine.split(",");
            cityPairs.write(String.format("%-10s,%3s\r\n", fields[0], fields[1]));
            currentLine = europeMapData.readLine();
        }

        //throw out succeeding "%" comment lines
        for (int i = 0; i < 4; i++) {
            currentLine = europeMapData.readLine();
        }

        System.out.println(currentLine);
        /*******************build the adjacency matrix ********************/


        /******************
         * Initialize all spots to MAX_SHORT
         ******************/
        for (int i = 0; i < n; i++) {
            //System.out.println("I is "+i);
            //currentLine = europeMapData.readLine();
            for (int j = 0; j < i; j++) {
                //System.out.println("J is "+j);

                mapGraph.seek(calculateByteOffset(i, j));
                mapGraph.writeShort(Short.MAX_VALUE);


            }
        }

        rows = new short[50];
        cols = new short[50];
        distances = new short[50];

        /*******************
         * Store each city1,city2,distance in parallel arrays
         *******************/
        int i = 0;
        while ((currentLine = europeMapData.readLine()) != null) {
            fields = currentLine.split(",");

            rows[i] = Short.valueOf(fields[0]);
            cols[i] = Short.valueOf(fields[1]);
            distances[i] = Short.valueOf(fields[2]);

            /*
            System.out.println(row + "," + col + "," + distance);
            mapGraph.seek(calculateByteOffset(col, row));

            //mapGraph.writeShort(row);
            // mapGraph.writeShort(col);
            mapGraph.writeShort(distance);


            numEdges++;
            */
            i++;
        }

        /**********
         * Write each city pair's distance to the matrix in column-major form
         **********/
        for (int x = 0; x < i; x++) {
            //System.out.println("I is "+i);
            //currentLine = europeMapData.readLine();

            if (rows[x] < cols[x]) {
                mapGraph.seek(calculateByteOffset(cols[x], rows[x]));
            } else {
                mapGraph.seek(calculateByteOffset(rows[x], cols[x]));
            }

            mapGraph.writeShort(distances[x]);


        }


/******************* FINISH UP WITH ALL FILES ******************************/
        cityPairs.close();
        europeMapData.close();
        mapGraph.close();

    }

    private static long calculateByteOffset(int row, int col) {
        long result = (row * (row - 1)) + (col * 2) + 2;
        //System.out.println("seeking to " + result);
        return result;
    }


}


