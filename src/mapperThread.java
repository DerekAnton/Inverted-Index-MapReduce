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
		WordData next;
	    try 
	    {
			Scanner scan = new Scanner(Index.fileArray[fileNumber]);

			while(scan.hasNext())
	    	{
				nextLineData = scan.nextLine().split(" ");
				for(String word: nextLineData)
				{
					word.replaceAll("[^A-Za-z0-9 ]", "" ).toLowerCase();	
					hashValue = word.hashCode() % Index.requestedReducerThreads;
					data = word + " " + lineNumber + " " + fileName;
					
					//From old BoundedBuffer
					//Index.bbuffers[hashValue].Producer(data);
					
					
					next = new WordData(word, lineNumber, fileName, true);
					Index.buffers[hashValue].append(next);
					//System.out.println(next.getWord());
				}
		    	lineNumber++;
		    	
	    	}
			System.out.println("MAP DIE");
		} 
	    catch (FileNotFoundException e) 
		{
			System.out.println("File #" + fileNumber + " Read Error");
			//e.printStackTrace();
		}  
		//Method for reading text and adding to bounded buffer
	}
}
