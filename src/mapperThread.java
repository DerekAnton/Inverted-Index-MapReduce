import java.io.FileNotFoundException;

import java.util.Scanner;
import java.io.File;


public class mapperThread extends Thread 
{
	private File[] fileArray;
	private int fileNumber = 0;
	
	public mapperThread(File[] fileArray, int fileNumber)
	{
		this.fileArray = fileArray;
		this.fileNumber = fileNumber;
	}
	public void run() 
	{
		mapRead(fileNumber);
	}
	public void mapRead(int fileNumber)
	{
		String nextLine[];
		int hashValue;
		int lineNumber = 1;
	    try 
	    {
			Scanner scan = new Scanner(fileArray[fileNumber]);
	    	while(scan.hasNext())
	    	{
			nextLine = scan.nextLine().split(" ");
				for(String word: nextLine)
				{
					hashValue = word.hashCode() % Index.requestedMapperThreads;
					Index.bbuffers[hashValue].Producer(word);
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
