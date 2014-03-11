import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;
import java.io.File;


public class mapperThread extends Thread 
{
	private File[] fileArray;
	private int fileNumber = 0;
	private String fileName;
	
	public mapperThread(File[] fileArray, int fileNumber , String fileName)
	{
		this.fileArray = fileArray;
		this.fileNumber = fileNumber;
		this.fileName = fileName;
	}
	public void run() 
	{
		try {
			mapRead(fileNumber);
		} catch (IOException e) {
			System.out.println("Mapper Thread Failed to Run");
		}
	}
	public void mapRead(int fileNumber) throws IOException
	{
		String nextLineData[];
		String data;
		int hashValue;
		int lineNumber = 1;
	    try 
	    {
			Scanner scan = new Scanner(Index.fileArray[fileNumber]);

			while(scan.hasNext())
	    	{
				nextLineData = scan.nextLine().split(" ");
				for(String word: nextLineData)
				{
					hashValue = word.hashCode() % Index.requestedMapperThreads;
					data = word + " " + lineNumber + " " + fileName;
					Index.bbuffers[hashValue].Producer(data);
				}
	    	}
	    	lineNumber++;
			
		} 
	    catch (FileNotFoundException e) 
		{
			System.out.println("File #" + fileNumber + " Read Error");
			//e.printStackTrace();
		}  
		//Method for reading text and adding to bounded buffer
	}
}
