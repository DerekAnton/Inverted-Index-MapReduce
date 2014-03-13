
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Index 
{
	public static mapperThread[] mapperThreadHolder; //will house the mapper threads//
	public static reducerThread[] reducerThreadHolder;
	private static String[] fileNames = new String[50];
	public static File[] fileArray = new File[20]; //The loaded files from disk //
	public static BoundedBuffer[] bbuffers;
	public static BBMonitor[] buffers;
	public static int requestedMapperThreads;
	public static int requestedReducerThreads;
	public static ConcurrentHashMap<String, String> invertedIndex = new ConcurrentHashMap<String, String>();
	public static int mappersActive;
	
	
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
			buffers = new BBMonitor[requestedReducerThreads];
			
			//Initiate Buffers
			for(int i = 0 ; i < requestedReducerThreads; i++){
				bbuffers[i] = new BoundedBuffer();
			}
			for(int i = 0 ; i < requestedReducerThreads; i++){
				buffers[i] = new BBMonitor();
			}
			
			mapperThreadHolder = new mapperThread[requestedMapperThreads];
			mappersActive = requestedMapperThreads;
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
			//System.out.println("loop1");

		}
		for(int threadNum = 0 ; threadNum < reduceNum ; threadNum++ )
		{
			reducerThreadHolder[threadNum].join();
			//System.out.println("loop2");

		}
		
	}
	
	
	public static void printMap(){
		//System.out.println("print");
		TreeMap<String, String> printMap= new TreeMap();
		printMap.putAll(invertedIndex);
		try {
			PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
	

		Iterator it = printMap.keySet().iterator();
		Object printS;
		while(it.hasNext()){
			printS = it.next();
			System.out.println(printS + " "  + printMap.get(printS));
		}
		writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
