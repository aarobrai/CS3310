import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * PROJECT: A4  CLASS: CustomerServicingApp
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: Input: CustomerEvents.csv
 *                 Output: Log.txt
 *
 * DESCRIPTION: Uses a CustomerPQ object to create a Priority Queue of initial customers. Then, opens
 * the store, processes CustomerEvents file, calling appropriate methods in CustomerPQ class as events happen.
 * Then, closes the store, and CustomerPQ object serves the remaining customers until the queue is empty.
 */
public class CustomerServicingApp {

    private static BufferedReader custEvents;
    private static CustomerPQ pq;
    private static Output log;

    public static void main(String[] args) throws IOException {
        log = new Output(false);
        log.displayThis(">> Program starting. ");


        pq = new CustomerPQ("LineAt6Am.csv",log);
        pq.createCustPQ();

        //open the store
        processCustEvents("CustomerEvents.csv", pq);
        storeCloses();


        log.finishUp();
    }

    public static void processCustEvents(String fileName, CustomerPQ pq) throws IOException {
        custEvents = new BufferedReader(new FileReader(fileName));
        String currentLine;
        String event;
        log.displayThis(">> Will now process CustomerEvents data. ");
        while ((currentLine = custEvents.readLine()) != null) {

            //will make "S" transaction for CustomerServed and "A" for CustomerArrives
            event = currentLine.substring(8, 9);
            //System.out.println("Event = " + event);
            switch (event) {
                case "S":
                    pq.serveNextCustInPQ();
                    break;
                case "A":
                    pq.addCustToPQ(currentLine);

            }
        }
        log.displayThis(">> Finished processing CustomerEvents data. ");
        pq.printHeapToLog();


    }

    public static void storeCloses() throws  IOException{
        log.displayThis("STORE CLOSES");
        pq.serveRestOfCustInPQ();

    }
}
