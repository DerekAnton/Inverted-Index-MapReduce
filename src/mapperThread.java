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
	    try 
	    {
			Scanner scan = new Scanner(fileArray[fileNumber]);
			
			
		} 
	    catch (FileNotFoundException e) 
		{
			System.out.println("File #" + fileNumber + " Read Error");
			//e.printStackTrace();
		}  
		//Method for reading text and adding to bounded buffer
	}
}