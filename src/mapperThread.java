import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.io.File;


public class mapperThread extends Thread 
{
	//File assocaited with this mapper thread
	private int fileNumber = 0;
	//Name of file
	private String fileName;
	//Used to protect ActiveThread decrement
	public static Lock lock = new ReentrantLock();

	
	public mapperThread( int fileNumber , String fileName)
	{
		this.fileNumber = fileNumber;
		this.fileName = fileName;
	}
	public void run() 
	{
		try {
			//Method that reads in file
			mapRead(fileNumber);
		} catch (IOException e) {
			System.out.println("Mapper Thread Failed to Run");
		}
	}
	
	//Reads in whole file
	public void mapRead(int fileNumber) throws IOException
	{
		String nextLineData[];
		int hashValue;
		int lineNumber = 1;
		WordData next;
	   
		try 
	    {
			Scanner scan = new Scanner(Index.fileArray[fileNumber]);

			//Read all words in document
			while(scan.hasNext())
	    	{
				//Get next line of data split by space
				nextLineData = scan.nextLine().split(" ");
				
				//for each word in that line 
				for(String word: nextLineData)
				{
					//Get rid of all non-letters 
					word.replaceAll("[^A-Za-z0-9 ]", "" ).toLowerCase();
					
					//Create Hash value
					hashValue = word.hashCode() % Index.requestedReducerThreads;
					
					//Sometime hashCode returns negative numbers
					//This produces equivilant positive value
					if(hashValue < 0){
						hashValue += Index.requestedReducerThreads;
					}
		
					//Add it to correct BBMonitor
					next = new WordData(word, lineNumber, fileName, true);
					Index.buffers[hashValue].append(next);
				}
				//Whole Line Read
		    	lineNumber++;
		    	
	    	}
			//Takes care of decrementing counter
			done();
		} 
	    catch (FileNotFoundException e) 
		{
			System.out.println("File #" + fileNumber + " Read Error");
			//e.printStackTrace();
		}  
		//Method for reading text and adding to bounded buffer
	}
	//Decrements amount of remaining map values
	public static synchronized void done(){
		lock.lock();
		//System.out.println("MAP Dead");
		Index.mappersActive--;
		lock.unlock();
	}
}
