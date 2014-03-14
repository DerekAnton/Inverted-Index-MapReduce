	/*
	 * Mitchell Hebert
	 * Derek Anton
	 * 
	 * OS Spring 2014
	 * Professor Sean Barker
	 * 
	 * Class for Bounded Buffer 
	 * using monitors. 
	 * 
	 */

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BBMonitor {
	
	private WordData[] buffer = new WordData[10];
	private int count;
	private Lock lock = new ReentrantLock();
	private Condition full = lock.newCondition();
	private Condition empty = lock.newCondition();
	private int appendPointer = 0;
	private int removePointer = 0;
	
	
	public BBMonitor(){
		count = 0;
		//INstantiate the buffer
		for(int i = 0 ; i < buffer.length; i++){
			buffer[i] = null;
		}
		
	}
	
	public void append(WordData word) {
		lock.lock();
		try {
			//Buffer Full Wait for slot to open
			while (count == buffer.length) {
				empty.await();
			}
			
			//Put WordData in buffer
			buffer[appendPointer] = new WordData(word.getWord(), word.getLineNumber() , word.getFileName() );
			appendPointer++;
			
			//Again Mod function can return negative numbers so I did my own
			if(appendPointer == buffer.length){
				appendPointer = 0;
			}
			//Increment the number of items in buffer
			count++;
			
			//Signal their is a new item to be removed
			full.signal();
		} catch (Exception e) {
			System.out.println("APPEND ERROR");
		}
		lock.unlock();

	}
	public WordData remove(){
		WordData returnWord  = new WordData("" , 0 , "" );
		lock.lock();
		try{
			//Buffer is empty, wait for item to remove
			while(count == 0){
				full.await();
			}
			//Remove WordData
			returnWord = buffer[removePointer];
			buffer[removePointer] = null;
			removePointer++;
			
			//Custom Mod function
			if(removePointer == buffer.length){
				removePointer = 0;
			}
			//Decrement number of items in buffer
			count = count - 1;
			
			//Signal that an item has been removed
			empty.signal();
			
			
		}catch(Exception e){
			System.out.println("REMOVE FAIL");
		}
		lock.unlock();

		return returnWord;
	}
	
	public boolean isEmpty(){
		for(WordData word : buffer){
			if(word != null){
				return false;
			}
		}
		return true;
	}
	
}
