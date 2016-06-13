package brainard.a2;

/**
 *
 * @author Devlin Grasley (JesusCat)
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.text.NumberFormat;
import java.io.RandomAccessFile;

public class PrettyPrint {
    private static File logFile;
    private static BufferedWriter log;
    private static RandomAccessFile file;
    private static final int SIZE_OF_SHORT=2;
    private static final int SIZE_OF_INT=4;
    private static final int SIZE_OF_LONG=8;
    private static final int SIZE_OF_CHAR=2;
    private static final int SIZE_OF_FLOAT=4;
    private static final int SIZE_OF_CODE=3*SIZE_OF_CHAR;
    private static final int LENGTH_OF_CODE = 3;
    private static final int LENGTH_OF_NAME = 18;
    private static final int LENGTH_OF_CONTINENT = 13;
    private static final int SIZE_OF_NAME=18*SIZE_OF_CHAR;
    private static final int SIZE_OF_ID=SIZE_OF_SHORT;
    private static final int SIZE_OF_CONTINENT=13*SIZE_OF_CHAR;
    private static final int SIZE_OF_POPULATION=SIZE_OF_LONG;
    private static final int SIZE_OF_AREA=SIZE_OF_INT;
    private static final int SIZE_OF_LE=SIZE_OF_FLOAT;
    private static final int SIZE_OF_HEADER=2*SIZE_OF_SHORT;
    private static final int SIZE_OF_RECORD = SIZE_OF_ID+SIZE_OF_CODE+SIZE_OF_NAME+SIZE_OF_CONTINENT+SIZE_OF_AREA+SIZE_OF_POPULATION+SIZE_OF_LE;

    public static void main (String [] args){
        //
        //IF YOU HAVE ANY QUESTIONS OR ISSUES, CONTACT ME AT devlin.t.grasley@wmich.edu
        //
        File innerFile = new File("DataStorage.bin");
        logFile=new File("Log.txt");
        try{
            file = new RandomAccessFile(innerFile,"r");
            log = new BufferedWriter(new FileWriter (logFile,true));

            int location = 1;
            short id = 0;
            String code = "";
            String name = "";
            String continent = "";
            int area = 0;
            long population = 0;
            float life = 0.0f;
            int n = 0;
            int maxID = 0;
            int numberOfBlanks = 0;
            int numberOfRecords = 0;

            file.seek(0);
            n = file.readShort();
            maxID = file.readShort();

            out(String.format("PRETTYPRINT STARTED!%n"));
            out(String.format("N is %d Max ID is %d%n%n",n,maxID));
            out(String.format("%-3.3s> %-3.3s %-3.3s %-18.18s %-15.15s %11.11s %13.13s %4.4s%n",rightPad("Loc",3,'-'),rightPad("CDE", 3,'-'),rightPad("ID", 3,'-'),rightPad("NAME", 18,'-'),rightPad("CONTINENT", 15,'-'),leftPad("AREA", 11, '-'),leftPad("POPULATION",13,'-'),leftPad("LIFE",4,'-')));
            while(file.length()>getLoc(location)){

                id=readShort();
                code=readString(LENGTH_OF_CODE);
                name=readString(LENGTH_OF_NAME);
                continent=readString(LENGTH_OF_CONTINENT);
                area=readInt();
                population=readLong();
                life=readFloat();

                if(id == 0){
                    numberOfBlanks++;
                    out(String.format(String.format("%03d> %s %n",location, "EMPTY")));
                }else{
                    numberOfRecords++;
                    out(String.format("%03d> %-3.3s %03d %-18.18s %-15.15s ",location, code,id,name,continent)+String.format("%11.11s %13.13s %4.4s %n",NumberFormat.getIntegerInstance().format(area),NumberFormat.getIntegerInstance().format(population),life));
                }
                location++;
            }
            out(String.format("Number of records:       %d%nNumber of blank records: %d%nN correct:               %b%nMax ID correct:          %b%n",numberOfRecords,numberOfBlanks,n==numberOfRecords,(numberOfRecords+numberOfBlanks)==maxID));
            file.close();
            log.close();
        }catch (Exception e){
            System.out.println("[ERROR] Dude, something went wrong!\n"+e);
            e.printStackTrace();
        }
    }

    private static void out (String s){
        log(s);
        System.out.print(s);
    }

    private static void log (String s){
        try{
            log.write(s);
        }catch (Exception e){
            System.err.println("[ERROR] Dude, Something broke!\n"+e);
            e.printStackTrace();
        }
    }

    private static String rightPad (String s, int n, char c){
        if( s.length()<n){
            for(int i=s.length();i<n;i++){
                s+=c;
            }
        }
        return s;
    }
    private static String leftPad (String s, int n, char c){
        if( s.length()<n){
            for(int i=s.length();i<n;i++){
                s=c+s;
            }
        }
        return s;
    }


    private static int getLoc (int ID){
        //IDs start at 1
        return (SIZE_OF_RECORD*(ID-1))+SIZE_OF_HEADER;
    }

    public static short readShort(int location){
        try{
            if(location < file.length()){
                file.seek(location);
                return file.readShort();
            }else{
                return -1;
            }
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return -2;
        }
    }
    public static int readInt(){
        try{
            return file.readInt();
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return -2;
        }
    }
    public static float readFloat(){
        try{
            return file.readFloat();
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return -2;
        }
    }
    public static short readShort(){
        try{
            return file.readShort();
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return -2;
        }
    }
    public static long readLong(){
        try{
            return file.readLong();
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return -2;
        }
    }
    public static char readChar(){
        try{
            char c =file.readChar();
            return c;
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
            return ' ';
        }

    }
    public static String readString (int length){
        String s="";
        try{
        }catch (Exception e){
        }
        for(int i = 0;i<length;i++){
            s+=readChar();
        }
        try{
        }catch (Exception e){
        }
        return s;
    }

}