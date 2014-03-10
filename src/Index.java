
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Index 
{
	public static mapperThread[] mapperThreadHolder; //will house the mapper threads//
	public static File[] fileArray; //The loaded files from disk //
	public static BoundedBuffer[] bbuffers;
	public static int requestedMapperThreads;
	public static ConcurrentHashMap invertedIndex;
	
	public static void main(String[] args)
	{
		int requestedReducerThreads = 0;
		requestedMapperThreads = 0;

		try
		{
			requestedReducerThreads = Integer.parseInt(args[0]);
			for(int counter = 1; counter < args.length; counter++)
			{
				fileArray[counter-1] = new File(args[counter]);
				requestedMapperThreads++;
			}
			bbuffers = new BoundedBuffer[requestedReducerThreads];
			fileArray = new File[requestedMapperThreads];
			mapperThreadHolder = new mapperThread[requestedMapperThreads];
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("The parameters passed were not of the form");
			System.out.println("Index [n] [file1] [file2] ... [filek]");
			System.out.println("Please re-run the program and try again");
		}
	}	
	
	
	public void createMapThreads(int numThreads)
	{

		for(int threadNum = 0 ; threadNum < numThreads ; threadNum++ )
		{
			//Create New Thread
			mapperThreadHolder[threadNum] = new mapperThread(fileArray, threadNum);
			mapperThreadHolder[threadNum].start();
		}
	}
}

/* INPUT
 * java Index [n] [file1] [file2] ... [filen]
 * 		With n Reducer threads & k Mapper threads
 * 
 * OUTPUT
 * word1 (file@line,line... file@line,line,...)...
 * word2 (file@line,line... file@line,line,...)...
 * 
 * TODOs:
 *  1.) Each map thread will be assigned to the next unique txt file
 *  		I'm going to have to create requestedThreads number of threads and give each one 
 *  		the task to read in the next file passed as a parameter in args[] 
 * 	2.) Each map thread will hash each word and compute an int from 1-n (n being the number of threads created?)
 * 	
 * 
 */
