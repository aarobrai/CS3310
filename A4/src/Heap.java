import java.io.IOException;

/**
 * PROJECT: A4  CLASS: Heap
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: Input: None
 *                 Output: Log.txt file
 *
 * DESCRIPTION: This is the implementation of a binary MIN heap, used to create a priority queue
 * in CustomerPQ class. It also has an internal class, HeapNode, where each HeapNode contains a
 * Name and PriorityValue. The minHeap consists of an array of these nodes of MAX_SIZE.
 *
 * There are special rules for ties on priority values:
 *          Walkup: If parent = child, DO NOT swap
 *          Walkdown: If parent = child, then DO swap
 *          Swap: If leftChild and rightChild are equal, use leftChild
 */

public class Heap {

/************************* PRIVATE DECLARATIONS *****************************/
    private final int MAX_SIZE = 200;
    private HeapNode[] nodeArr;
    private Output log;
    private int n;
/************************* PUBLIC CONSTRUCTOR(S) ***************************/
    public Heap(Output log) {
        this.log = log;
        nodeArr = new HeapNode[MAX_SIZE];
        n = 0;
    }
/************************************ SERVICE METHODS *****************************************/


/******************** CREATE ********************/
    public void create() {
        for (int i = parI(n - 1); i > 0; i--) {
            walkDown(i);
        }
    }
/******************** INSERT ********************/
    public void insert(String name, int priority, int count) {

        //System.out.println(String.format("%d %-27s %d", count, name, priority));
        //create node
        if(!isHeapFull()){
            nodeArr[n] = new HeapNode(name, priority);
            n++;
            walkUp(n - 1);
        }else{
            try{
                log.displayThis(">> ERROR, heap is full! Please increase MAX_SIZE ("+MAX_SIZE+")");
            }catch(IOException e){
                e.printStackTrace();
            }

        }


    }

/******************* DELETE *******************/
    public String delete() {
        HeapNode min = nodeArr[0];
        n--;
        nodeArr[0] = nodeArr[n];

        walkDown(0);
        return (min.getName()+ " ("+min.getPV()+")");
    }
/******************** WALK UP ******************************/
    private void walkUp(int start) {
        int i = start;
        //strictly less than- will NOT swap if i and parent are equal
        while (i > 0 && nodeArr[i].getPV() < nodeArr[parI(i)].getPV()) {

            swap(i, (parI(i)));

            i = parI(i);
        }
    }
/****************** WALK DOWN ***************************/
    private void walkDown(int start) {
        int i = start;
        int smCh = subOfSmCh(i);
        //greater OR EQUAL. Will swap if parent and child are equal
        while ((2 * i + 1) <= (n - 1) && nodeArr[i].getPV() >= nodeArr[smCh].getPV()) {
            swap(i, smCh);
            // System.out.println("Swapping "+i+" and "+smCh);
            i = smCh;
            smCh = subOfSmCh(i);
        }

    }


/************ LEFTCHILD, RIGHTCHILD, PARENT *********************/
    private int getLChild(int i){
        return 2 * i + 1;
    }

    private int getRChild(int i){
        return 2 * i + 2;
    }

    private int parI(int i) {
        return (i - 1) / 2;
    }

/*************** SUBSCRIPT OF SMALLEST CHILD ********************/
    private int subOfSmCh(int i) {
        int result;
        if ((2 * i + 2) > (n - 1) || (nodeArr[2 * i + 1].getPV() <= nodeArr[2 * i + 2].getPV())) {
            result = 2 * i + 1;
        } else {
            result = 2 * i + 2;
        }
        return result;
    }


/********************* SWAP *************************************/
    private void swap(int first, int second) {
        HeapNode temp = nodeArr[first];
        nodeArr[first] = nodeArr[second];
        nodeArr[second] = temp;
        //System.out.println("Swapped "+nodeArr[first].getName()+" and "+nodeArr[second].getName());
        // System.out.println("PVs are "+nodeArr[first].getPV() + " and "+nodeArr[second].getPV());
    }
/********************************* OTHER PUBLIC HELPER METHODS *************************************/
    public boolean isHeapFull() {
        if (n == MAX_SIZE) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        if (n == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int getN(){
        return n;
    }

    public void dump() throws IOException, NullPointerException {

        if(n == 0){
            log.displayThis(">> Heap is now empty - "+n+" nodes");
            return;
        }

        log.displayThis(">> Dump of current heap (array) - "+n+" nodes:");
        log.displayThis(">> SUB PV   NAME ");
        for (int i = 0; i < n; i++) {
            log.displayThis(String.format(">> %02d  %03d  %-27s ", i, nodeArr[i].getPV(), nodeArr[i].getName()));
        }
    }

/*************************** INTERNAL HEAPNODE CLASS **************************************************/
    private class HeapNode {

        private String name;
        private int priority;

        public HeapNode(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        public String getName() {
            return name;
        }

        public int getPV() {
            return priority;
        }
    }


}
