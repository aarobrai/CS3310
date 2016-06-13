import java.io.IOException;
import java.util.Arrays;

/**
 * PROJECT: A6
 * AUTHOR:  aaronbrainard
 * DATE:    11/23/2015
 *
 * FILES ACCESSED:
 *
 *
 *
 *
 * DESCRIPTION: uses Dijkstra’s Minimum Cost Path algorithm to find shortest path
 * thru a graph
 */

public class Route {

    private Map map;
    private Log log;

    private short[] distances;
    private boolean[] included;
    private short[] path;

    private short n;
    private short startNum;
    private short destNum;
    private short numTargets;

    /**
     * ********************* PUBLIC CONSTRUCTOR ********************************
     */
    public Route(short n) {
        this.n = n;

        distances = new short[n];
        included = new boolean[n];
        path = new short[n];
    }


    /**
     * ********************* DIJKSTRA'S ALGORITHM ********************************
     */

    public void findMinCostPath(short startNum, short destNum, short n, Map map, Log log) throws IOException {


        this.log = log;
        this.map = map;

        this.startNum = startNum;
        this.destNum = destNum;
        initialize3ScratchArrays();
        String trace = searchForPath();

        short totalDistance = distances[destNum];
        //  System.out.println("TOTAL DISTANCE TRAVELED: " + totalDistance);
        if (totalDistance == Short.MAX_VALUE) {
            log.displayThisLine("DISTANCE: ?");
            log.displayThisLine("PATH: SORRY -  can NOT get to destination city from start city");
        } else {
            log.displayThisLine("DISTANCE: " + totalDistance);
            String path = "";
            log.displayThisLine("PATH:  " + reportAnswer(path, destNum));
        }
        log.displayThisLine("TRACE OF TARGETS: " + trace);
        log.displayThisLine("# TARGETS: " + numTargets);

        // System.out.println("PATH:" + reportAnswer(trace, destNum));


    }


    //returns the trace of targets
    private String searchForPath() throws IOException {

        numTargets = 0;
        String trace = "";


        while (!included[destNum]) {
            //get next target- the edge with minimum distance
            short targetNode = getMinDistance();
            //no-edge. min distance was found to be MAX SHORT
            if (targetNode == -1) {
                return trace;
            }
            //include the current target and add to trace of targets
            trace += (map.getCityCode(targetNode) + " ");
            included[targetNode] = true;
            numTargets++;

            for (short i = 0; i < n; i++) {
                //if the node i has not yet been considered
                if (!included[i]) {
                    //If there is an edge
                    if (validDistance(targetNode, i)) {
                        //THE BIG QUESTION
                        if (distances[targetNode] + map.getRoadDistance(targetNode, i) < distances[i]) {
                            distances[i] = (short) (distances[targetNode] + map.getRoadDistance(targetNode, i));
                            path[i] = targetNode;
                        }
                    }


                }
            }
        }
        return trace;
    }


    /**
     * ********************* PRIVATE HELPER METHODS ******************************
     */

    private void initialize3ScratchArrays() throws IOException {
        //distances = new short[n];
        // = new boolean[n];
        //path = new short[n];
        for (short i = 0; i < n; i++) {
            distances[i] = map.getRoadDistance(startNum, i);
            if (distances[i] == 0 || distances[i] == Short.MAX_VALUE) {
                path[i] = -1;
            } else {
                path[i] = startNum;
            }
            included[i] = false;
        }
        included[startNum] = true;
    }


    private String reportAnswer(String pathAns, short destNum) throws IOException {


        //report the path
        if (path[destNum] == -1) {
            pathAns += map.getCityName(destNum).trim();
        } else {
            pathAns = reportAnswer(pathAns, path[destNum]);
            pathAns += (" > " + map.getCityName(destNum).trim());
        }

        return pathAns;
    }


    private boolean validDistance(short target, short i) throws IOException {
        short distance = map.getRoadDistance(target, i);

        if (distance <= 0 || distance == Short.MAX_VALUE) {
            return false;
        } else {
            return true;
        }
    }

    private short getMinDistance() {
        short min = Short.MAX_VALUE;
        short result = 0;
        for (short i = 0; i < n; i++) {
            if ((distances[i] != 0 || distances[i] != Short.MAX_VALUE) && !included[i]) {
                if (distances[i] < min) {
                    min = distances[i];
                    result = i;
                }
            }
        }
        if (min == Short.MAX_VALUE) {
            return -1;
        } else {
            return result;
        }

    }


    /**
     * **************************** CLEANUP ************************************
     */


    public void finishUp() throws IOException {
        map.finishUp();
    }

}
