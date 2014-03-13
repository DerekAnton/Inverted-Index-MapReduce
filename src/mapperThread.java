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
	private File[] fileArray;
	private int fileNumber = 0;
	private String fileName;
	public static Lock lock = new ReentrantLock();

	
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
					
					if(hashValue < 0){
						hashValue += Index.requestedReducerThreads;
					}
					//From old BoundedBuffer
					//Index.bbuffers[hashValue].Producer(data);
					
					
					next = new WordData(word, lineNumber, fileName, true);
					Index.buffers[hashValue].append(next);
					//System.out.println(next.getWord());
				}
		    	lineNumber++;
		    	
	    	}
			done();
		} 
	    catch (FileNotFoundException e) 
		{
			System.out.println("File #" + fileNumber + " Read Error");
			//e.printStackTrace();
		}  
		//Method for reading text and adding to bounded buffer
	}
	
	public static synchronized void done(){
		lock.lock();
		System.out.println("MAP DIE");
		Index.mappersActive--;
		lock.unlock();
	}
}
