import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * PROJECT: A6
 * AUTHOR:  aaronbrainard
 * DATE:    11/23/2015
 *
 * FILES ACCESSED: Input: CityPairs.csv from Setup
 * MapGraph.bin & CityNameList.csv (handled by Map class)
 * Output: Log.txt
 *
 * DESCRIPTION: Uses services in other OOP classes to find the min cost path between a city pair
 *
 * Declare 3 objects: map, route, log
 * Open CityPairs file
 * Loop til no more city pairs
 * {
 * read in a city pair: 1 start city NAME & 1 destination city NAME
 * ask Map’s method to getCityNumber for the 2 cities’ names (separately)
 * ask Route’s method to findMinCostPath (given the above 2 city NUMBERS)
 * [it’ll need to access map and log objects]
 * which will find the answer (path & distance) & the traceOfTargets
 * and provide these to log object for writing to the Log file
 * }
 * Close CityPairs file
 * FinishUp with the 3 objects
 */
public class DrivingApp {

    /**
     * ********************* PRIVATE DECLARATIONS ******************************
     */

    private static Map map;
    private static Route route;
    private static Log log;
    private static BufferedReader cityPairs;

    /**
     * ********************* CONSTRUCTOR ******************************
     */
    public DrivingApp() throws IOException {

        try {
            map = new Map();
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: Map Data file not found");
            e.printStackTrace();
        }

        route = new Route(map.getN());
        log = new Log(true);
        cityPairs = new BufferedReader(new FileReader("CityPairs.csv"));
    }

    /**
     * ********************* MAIN METHOD ******************************
     */

    public static void main(String[] args) throws IOException {
        new DrivingApp();

        String transaction;

        short start;
        short dest;
        short n = map.getN();

        //read and process the city pair transactions
        while ((transaction = cityPairs.readLine()) != null) {

            if (!transaction.contains("%")) {
                String[] nums = transaction.split(",");
                start = map.getCityNumber(nums[0]);
                dest = map.getCityNumber(nums[1]);

                log.displayThisLine("#   #   #   #   #   #   #   #   #   #   #   #");
                log.displayThisLine(nums[0] + " (" + start + ") TO " + nums[1] + " (" + dest + ")");
                if (start < 0 || dest < 0 || start >= n || dest >= n) {
                    log.displayThisLine("ERROR - one city is NOT on this map");
                } else {
                    route.findMinCostPath(start, dest, n, map, log);
                }


            }
        }

        cityPairs.close();

        map.finishUp();
        route.finishUp();
        log.finishUp();

    }
}
