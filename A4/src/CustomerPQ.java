import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * PROJECT: A4  CLASS: CustomerPQ
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: Input: LineAt6Am.csv file (for initial queue)
 *
 *                 Output: Log.txt
 *
 * DESCRIPTION: Processes raw customer data and uses services from Heap class to create a Priority Queue of
 * customers at 6am. Once store opens, service methods here are called by CustomerServicingApp to process
 * customer events. Once store closes, serves rest of customers in queue.
 *      So, basically:
 *          1. Read LineAt6am (initial) data and assign priority values to customers
 *          2. Store in binary min-heap (Heap class)
 *          3. Methods to add/serve customers called by CustomerServicingApp
 *          4. Store closes, serve remaining customers by priority value
 */
public class CustomerPQ {

    private BufferedReader rawDat;
    private Heap mHeap;
    private String fileName;
    private Output log;
    private int count;

/********************************** PUBLIC CONSTRUCTOR ******************************************/
    public CustomerPQ(String fileName, Output log) throws IOException{
        this.fileName = fileName;
        mHeap = new Heap(log);
        this.log = log;
        log.displayThis("STORE OPENS");
    }

/*************************************** PUBLIC SERVICE METHODS ******************************************/



/************************** CREATE **************************/
    public int createCustPQ() throws IOException {


        rawDat = new BufferedReader(new FileReader(this.fileName));

        log.displayThis(">> Will now insert customers from LineAt6Am into PQ. ");
        String custRecord;
        String[] custFields;
        //start with 101... the priority value is given by count-priorityValue
        count = 101;

        while((custRecord = rawDat.readLine()) != null){
            custFields = custRecord.split(",");
            mHeap.insert(custFields[0], findPriorityValue(custFields, count), count);

            log.displayThis("ADDED:  " + custFields[0] + " ("+findPriorityValue(custFields,count)+")");

            //System.out.println("count = " + count);
            count++;
        }
        //heap-ify the data
        mHeap.create();
        log.displayThis(">> Finished putting customers from LineAt6Am into PQ. ");
        printHeapToLog();
        return count;

    }
/********************** ADD *********************************/
    public void addCustToPQ(String custRecord) throws IOException{
        String[] fields = custRecord.split(",");
        fields[0] = fields[0].substring(18);
        mHeap.insert(fields[0], findPriorityValue(fields, count), count);
        log.displayThis("ADDED:  " + fields[0] + " (" + findPriorityValue(fields, count) + ")");
        count++;
    }
/********************** SERVE NEXT **************************/
    public void serveNextCustInPQ() throws IOException{
        String name = mHeap.delete();
        log.displayThis("SERVED: " + name);
    }

/********************* SERVE REST **************************/
    public void serveRestOfCustInPQ() throws IOException{

        int n = mHeap.getN();
        log.displayThis(">> Will now automatically serve " + n + " remaining customers ");
        String name;
        while(!mHeap.isEmpty()){
            name = mHeap.delete();
            log.displayThis("SERVED: " + name);
            n--;
        }
        finishUp();
    }
/********************* PRINT OUT **************************/
public void printHeapToLog() throws IOException{
        mHeap.dump();
    }

/********************* FINISH UP **************************/
    public void finishUp() throws IOException{

        printHeapToLog();
        rawDat.close();
        log.displayThis(">> Program ending.");
        log.finishUp();
    }

/******************************************* PRIORITY VALUE CALCULATION ******************************************/
    private int findPriorityValue(String[] custFields, int count){

        int result = 0;

        if(custFields.length == 1){
            return 0;

        }

        if(custFields[1].contains("employee")){
            result += 10;
        }if(custFields[1].contains("owner")) {
            result += 50;
            //owner gets employee points too
            result += 10;
        }

        if(custFields[2].contains("vip")){
            result += 5;
        }if (custFields[2].toLowerCase().contains("supervip")){
            result += 8;
            //superVIP get vip points too
            result += 5;
        }

        if(custFields[3].contains("loyalty")){
            result += 4;
        }

        if(custFields[4].contains("child")){
            result += 2;
        }

        if(Integer.valueOf(custFields[5]) >= 65){
            result += 5;
            //over age 80 gets 65up points plus another 5
            if(Integer.valueOf(custFields[5]) >= 80){
                result += 5;
            }
        }

        //System.out.println("result is "+result);
        return (count - result);
    }
}
