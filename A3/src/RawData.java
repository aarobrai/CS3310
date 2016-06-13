

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * PROJECT: A3  CLASS: RawData
 * AUTHOR: aaronbrainard
 *
 *
 * FILES ACCESSED: RawDataAll or RawDataTest depending on args passed into constructor
 *
 *
 *
 * <
 * DESCRIPTION: Reads the raw data file line by line and gets rid of
 * everything but Code and ID, which are stored in parallel arrays to
 * build the hash table in Hasher class
 */
public class RawData {

    //**************************** PRIVATE DECLARATIONS *************************

    private final int MAX_SIZE = 300;
    private String currentLine;
    private String[] code;
    private String[] id;
    private BufferedReader readRawData;


    public RawData(String fileLocation) throws IOException {

        readRawData = new BufferedReader(new FileReader(fileLocation));

        code = new String[MAX_SIZE];
        id = new String[MAX_SIZE];

    }

    public void loadDataIntoMem() throws IOException{

        CountryNode aCountry;

        String[] splitLine;
        int i = 0;
        while((currentLine = readRawData.readLine()) != null){
            //get country codes and id's
            splitLine = currentLine.substring(30).split(",");
            code[i] = splitLine[0].replace("'","");
            id[i] = splitLine[1];
            i++;
        }

    }

    public String getCode(int i){

        try{
            return code[i];
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Invalid Code array index selected: "+i);
            return null;
        }

    }

    public int getId(int i){
        try{
            return Integer.parseInt(id[i]);
        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Invalid ID array index selected: "+i);
            return -999;
        }

    }
























    public void finishUp() throws IOException{
        //mLog.finishUp();
    }
}
