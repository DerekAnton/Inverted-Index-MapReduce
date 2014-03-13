
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
	public static reducerThread[] reducerThreadHolder; //will house the reducer threads
	private static String[] fileNames; //holds files names
	public static File[] fileArray; //The loaded files from disk //
	public static BBMonitor[] buffers; //Bounded Buffers
	public static int requestedMapperThreads;
	public static int requestedReducerThreads;
	public static ConcurrentHashMap<String, String> invertedIndex = new ConcurrentHashMap<String, String>(); //InvertedIndex
	public static int mappersActive; //Active mappper threads = n
	
	
	public static void main(String[] args) throws InterruptedException
	{
		requestedReducerThreads = 0;
		requestedMapperThreads = 0;

		try
		{
			requestedReducerThreads = Integer.parseInt(args[0]);
			
			//Instantiate Arrays
			buffers = new BBMonitor[requestedReducerThreads];
			fileNames = new String[args.length-1];
			fileArray = new File[args.length-1];
			
			//Load Files into file array
			for(int counter = 1; counter < args.length; counter++)
			{
				fileArray[counter-1] = new File(args[counter]);
				fileNames[counter-1] = args[counter];
				requestedMapperThreads++;
			}
			
		
			
			//Initiate Buffers
			for(int i = 0 ; i < requestedReducerThreads; i++){
				buffers[i] = new BBMonitor();
			}
			
			//Instantiate thread holders
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
	
	//Create mapper threads
	public static void createMapThreads(int numThreads) throws InterruptedException
	{
		for(int threadNum = 0 ; threadNum < numThreads ; threadNum++ )
		{
			//Create New Thread
			mapperThreadHolder[threadNum] = new mapperThread( threadNum , fileNames[threadNum] );
			mapperThreadHolder[threadNum].start();

		}
	}
	
	//Create all the reducer threads
	public static void createReducerThreads(int numThreads) throws InterruptedException
	{
		for(int threadNum = 0 ; threadNum < numThreads ; threadNum++ )
		{
			//Create New Thread
			reducerThreadHolder[threadNum] = new reducerThread(threadNum);
			reducerThreadHolder[threadNum].start();

		}		
	}

	//Check to see if all threads are dead before printing
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

		//Makes HashMap Alphabetical
		TreeMap<String, String> printMap= new TreeMap();
		printMap.putAll(invertedIndex);
		
		try {

			PrintWriter writer = new PrintWriter("output.txt", "UTF-8");

			// Used To Print Out map
			Iterator it = printMap.keySet().iterator();
			Object printS;
			while (it.hasNext()) {
				printS = it.next();
				// Print To Console
				System.out.println(printS + " " + printMap.get(printS));

				// Print To File
				// writer.println(printS + " " + printMap.get(printS));
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
