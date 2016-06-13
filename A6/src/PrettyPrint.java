//PROGRAM: PrettyPrint
//AUTHOR: Jia Guo
//DESCRIPTION: DISPLAYS MapGraph.bin & CityNameList.csv files to MapDataPrintout.txt file.

import java.io.*;

public class PrettyPrint {

	public static void main(String[] args) throws IOException{
		//******************OPEN FILES AND DECLARATION*********************
		String aLine;
		short N; //header read from MapGraph.bin file
		short roadDistance; //for storing short reading from the file
		String distance; //convert roadDistance to String for "---" purpose
		//write to MapDataPrintout.txt file
		FileWriter output = new FileWriter("MapDataPrintout.txt");
		//random access MapGraph.bin file
		RandomAccessFile ranBinFile = new RandomAccessFile("MapGraph.bin", "rws");
		//random access CityNamesList.csv file, offset is 16 including <CR><LF>
		RandomAccessFile ranCsvFile = new RandomAccessFile("CityNamesList.csv", "rws");
		//start reading from the beginning of the .bin file
		ranBinFile.seek(0);
		//read the header N
		N = ranBinFile.readShort();
		output.write("Map Data: Europe - " + N + " cities\n\n");

		//**********************PRINT OUT THE MATRIX***************************
		output.write("00|\n");
		//read .bin file for N-1 times
		for(int i = 1; i < N; i++)
		{
			output.write(String.format("%02d| ", i));
			//read i times short each time
			for(int j = 1; j <= i; j++){
				roadDistance = ranBinFile.readShort();
				//For "infinity's" in the file, print "---" instead
				if(roadDistance == Short.MAX_VALUE) distance = "---";
				else
					distance = roadDistance + ""; //convert short to String
				output.write(String.format("%s ", distance));
			}
			output.write("\n");
		}

		//**********************PRINT OUT THE DIVIDER**************************
		output.write("   ---------------------------------------------------"
				+ "---------------------------------\n");

		//************PRINT OUT CITY CODE UNDER CORRESPONDING COLUMN***********
		//print city code in order in one line
		output.write("    ");
		//start from the beginning of the .csv file
		short offset = 0;
		for(int i = 0; i < N; i++)
		{
			ranCsvFile.seek(offset);
			aLine = ranCsvFile.readLine();
			output.write(aLine.split(",")[1] + " ");
			offset+=16; //each line is 16 bytes including <CR><LF>
		}
		output.write("\n");

		//************PRINT OUT COLUMN # UNDER CORRESPONDING CODE**************
		//print subscript for each city code in one line
		output.write("    ");
		for(int i = 0; i < N; i++)
		{
			output.write(String.format(" %02d ", i));
		}
		output.write("\n\n");

		//*********************PRINT OUT CITY NAME LIST************************
		//back to the beginning of the .csv file
		offset = 0;
		//print city names list (code first)
		for(int i = 0; i < N; i++)
		{
			ranCsvFile.seek(offset);
			aLine = ranCsvFile.readLine();
			output.write(String.format("%02d - %s %s", i, aLine.split(",")[1],
					aLine.split(",")[0] + "\n"));
			offset+=16;
		}

		//*************************CLOSE ALL THE FILES*************************
		output.close();
		ranBinFile.close();
		ranCsvFile.close();
	}
}