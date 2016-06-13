import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * PROJECT: A5
 * AUTHOR:  aaronbrainard
 * DATE:    11/12/2015
 *
 * FILES ACCESSED: INPUT: A5TransData(1,2,3) files
 *                 OUTPUT: Log.txt
 *
 * DESCRIPTION: Reads TransData file and calls CodeIndex select method
 *
 *              loop till no more transactions
 *              {
 *                  call appropriate CodeIndex method ('S' only for A5)
 *              }
 *              close TransData file and finish up
 */
public class UI {

    private BufferedReader transReader;
    private int count;
    private CodeIndex codeIndex;
    private Output log;

    /**
     * ********************* PUBLIC CONSTRUCTOR *************************************************
     */
    public UI(String fileNameSuffix) throws IOException {
        log = new Output(true);
        try {
            transReader = new BufferedReader(new FileReader("A5TransData" + fileNameSuffix + ".txt"));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: A5TransData" + fileNameSuffix + " file not found");
            e.printStackTrace();
        }
        log.displayThis("+ + + + + + + + + + + + + + ");
        log.displayThis("PROCESSING A5TransData" + fileNameSuffix + ".txt");
        log.displayThis("");
        codeIndex = new CodeIndex(fileNameSuffix, log);
    }

    /**
     * ********************** PUBLIC METHOD(S) *****************************************************
     */

    public void processTransactions() throws IOException {

        String currentLine;
        //sub0 is transaction type, sub1 is the keyVal
        String[] transaction;

        while ((currentLine = transReader.readLine()) != null) {
            transaction = currentLine.split(" ");
            switch (transaction[0]) {
                case "S":
                    //log.displayThis(transaction[0] + " " + transaction[1]);
                    codeIndex.selectByCode(transaction[1]);
                    count++;
                    break;
            }

        }
        System.out.println("Closing TransData file...");
        transReader.close();
    }


    /**
     * ********************** CLEANUP *****************************************************
     */
    public int finishUp() throws IOException {
        System.out.println("UI finished... processed " + count + " transactions");
        codeIndex.finishUp();
        log.finishUp();
        return count;
    }


}
