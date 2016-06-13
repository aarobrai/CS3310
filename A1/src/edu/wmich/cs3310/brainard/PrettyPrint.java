package edu.wmich.cs3310.brainard;
//PROGRAM: PrettyPrint
//AUTHOR: J.G.
//DESCRIPTION:This program is a developer's utility which reads/prints the Backup file,
//		showing it (nicely) in the Log file. NOTE: It does NOT display the internal BST, per se!
//		The program has no idea that the data in the Backup file is actually a BST - the program
//		just thinks it's a series of data records.
//		The program just opens, reads/writes and closes Backup.csv and Log.txt files itself
//		since it's PP not OOP.

//Since lifeExpectancy is stored as double, please let your rawData convert any NULL to 0.0
//Otherwise, please use the comments of line 34, 76 and 80 to replace their previous statement. Thanks!
//Regarding any other issues, please feel free to contact me via email: jia.guo@wmich.edu


import java.io.*;
import java.util.*;

public class PrettyPrint {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String inFileName = "Backup.csv";
        String outFileName = "Log.txt";


        int rootPtr, n, nextEmptyPtr; //root pointer, left child and right child
        //the number of countries
        //next empty pointer
        int loc = 0; //subscribe value of the record
        int lch, rch; //subscribe values of left child and right child
        String code, name, continent;
        int id, area;
        long population;
        double life;
        //String life;

        //Deal with the extended ASCII Char's
        FileReader input = new FileReader(inFileName);
        BufferedReader inFile = new BufferedReader(input);
        Formatter output = new Formatter(new BufferedWriter(new FileWriter(outFileName, true)));//append mode
        output.format("-->> PRETTYPRINT started\n");

        //Reads the header of the file
        String theLine = inFile.readLine();
        String[] header = theLine.split(",");
        rootPtr = Integer.parseInt(header[0]);
        n = Integer.parseInt(header[1]);
        nextEmptyPtr = Integer.parseInt(header[1]);

        //The information of BST pointer and number
        System.out.printf("RootPtr is %02d, N is %02d, NextEmptyPtr is %02d\n\n", rootPtr, n, nextEmptyPtr);
        output.format("RootPtr is %02d, N is %02d, NextEmptyPtr is %02d\n\n", rootPtr, n, nextEmptyPtr);


        //Prints the header and writes to Log.txt
        System.out.printf("LOC> LCH RCH CDE ID- NAME-------------------- CONTINENT---- ------AREA ---POPULATION LIFE\n");
        output.format("LOC> LCH RCH CDE ID- NAME-------------------- CONTINENT---- ------AREA ---POPULATION LIFE\n");

        //Reads other lines of the file till EOF
        while ((theLine = inFile.readLine()) != null) {
            String[] field = theLine.split(",");
            lch = Integer.parseInt(field[0]);
            rch = Integer.parseInt(field[1]);
            code = field[2];
            id = Integer.parseInt(field[3]);
            name = field[4];
            continent = field[5];
            area = Integer.parseInt(field[6]);
            population = Long.parseLong(field[7]);
            life = Double.parseDouble(field[8]);
            //life = field[8];

            //prints in console and writes to Log.txt with specific format
            System.out.printf("%03d> %03d %03d %s %03d %-24s %-13s %,10d %,13d %4.1f\n",
                    loc, lch, rch, code, id, name, continent, area, population, life);
            //System.out.printf("%03d> %03d %03d %s %03d %-24s %-13s %,10d %,13d %4s\n", loc, lch, rch, code, id, name, continent, area, population, life);

            output.format("%03d> %03d %03d %s %03d %-24s %-13s %,10d %,13d %4.1f\n",
                    loc, lch, rch, code, id, name, continent, area, population, life);
            //output.format("%03d> %03d %03d %s %03d %-24s %-13s %,10d %,13d %4s\n", loc, lch, rch, code, id, name, continent, area, population, life);
            loc++;
        }

        //Indicates the end of the file
        System.out.printf("===========================\n");
        output.format("===========================\n");
        output.format("-->> PRETTY PRINT finished\n");
        input.close();
        inFile.close();
        output.close();
    }
}