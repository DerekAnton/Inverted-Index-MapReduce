
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Index 
{
	public static mapperThread[] mapperThreadHolder; //will house the mapper threads//
	public static reducerThread[] reducerThreadHolder;
	private static String[] fileNames = new String[50];
	public static File[] fileArray = new File[20]; //The loaded files from disk //
	public static BoundedBuffer[] bbuffers;
	public static int requestedMapperThreads;
	public static int requestedReducerThreads;
	public static ConcurrentHashMap invertedIndex = new ConcurrentHashMap();
	
	public static void main(String[] args) throws InterruptedException
	{
		requestedReducerThreads = 0;
		requestedMapperThreads = 0;

		try
		{
			requestedReducerThreads = Integer.parseInt(args[0]);
			for(int counter = 1; counter < args.length; counter++)
			{
				fileArray[counter-1] = new File(args[counter]);
				fileNames[counter-1] = args[counter];
				requestedMapperThreads++;
			}
			bbuffers = new BoundedBuffer[requestedReducerThreads];

			//Initiate Buffers
			for(int i = 0 ; i < requestedReducerThreads; i++){
				bbuffers[i] = new BoundedBuffer();
			}
			
			mapperThreadHolder = new mapperThread[requestedMapperThreads];
			reducerThreadHolder = new reducerThread[requestedReducerThreads];
			
			
			createMapThreads(requestedMapperThreads);
			createReducerThreads(requestedReducerThreads);
			isDone(requestedMapperThreads, requestedReducerThreads);
			printMap();
		
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("The parameters passed were not of the form");
			System.out.println("Index [n] [file1] [file2] ... [filek]");
			System.out.println("Please re-run the program and try again");
		}
	}	
	
	
	public static void createMapThreads(int numThreads) throws InterruptedException
	{
		for(int threadNum = 0 ; threadNum < numThreads ; threadNum++ )
		{
			//Create New Thread
			mapperThreadHolder[threadNum] = new mapperThread(fileArray, threadNum , fileNames[threadNum] );
			mapperThreadHolder[threadNum].start();

		}
	}
	
	public static void createReducerThreads(int numThreads) throws InterruptedException
	{
		for(int threadNum = 0 ; threadNum < numThreads ; threadNum++ )
		{
			//Create New Thread
			reducerThreadHolder[threadNum] = new reducerThread(threadNum);
			reducerThreadHolder[threadNum].start();

		}		
	}

	public static void isDone(int mapNum , int reduceNum) throws InterruptedException{
		
		for(int threadNum = 0 ; threadNum < mapNum ; threadNum++ )
		{
			mapperThreadHolder[threadNum].join();

		}
		for(int threadNum = 0 ; threadNum < reduceNum ; threadNum++ )
		{
			reducerThreadHolder[threadNum].join();

		}
		
	}
	
	
	public static void printMap(){
		Iterator it = invertedIndex.keySet().iterator();
		while(it.hasNext()){
			System.out.println(invertedIndex.get(it.next()));
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
