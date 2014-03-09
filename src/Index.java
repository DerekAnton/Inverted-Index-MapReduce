
import java.util.concurrent.Semaphore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Index 
{
	private static File[] fileArray;
	public static void main(String[] args)
	{
		int requestedThreads = 0;
		fileArray = new File[20];
		
		try
		{
			requestedThreads = Integer.parseInt(args[0]);
			for(int counter = 1; counter < args.length; counter++)
			{
				fileArray[counter-1] = new File(args[counter]);
			}
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("The parameters passed were not of the form");
			System.out.println("Index [n] [file1] [file2] ... [filek]");
			System.out.println("Please re-run the program and try again");
		}
	}	
	
	
	public void createMapThreads(int numThreads){
		Runnable thread;
		for(int threadNum = 0 ; threadNum < numThreads ; threadNum++ ){
			//Create New Thread
			thread = new Runnable(){
				public void run() {
					mapRead(1);
					//Calls method that reads in text files
				}
			};
			new Thread(thread).start();
		}
		
	}
	
	public void mapRead(int fileNumber){
	    try {
			Scanner scan = new Scanner(fileArray[fileNumber]);
			
			
		} catch (FileNotFoundException e) {
			System.out.println("File #" + fileNumber + " Read Error");
			//e.printStackTrace();
		}  

		//Method for reading text and adding to bounded buffer
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
