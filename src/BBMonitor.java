import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BBMonitor {
	
	private WordData[] buffer = new WordData[10];
	private int last;
	private int count;
	private Lock lock = new ReentrantLock();
	private Condition full = lock.newCondition();
	private Condition empty = lock.newCondition();
	private int appendPointer = 0;
	private int removePointer = 0;
	
	
	public BBMonitor(){
		last = 0;
		count = 0;
		for(int i = 0 ; i < buffer.length; i++){
			buffer[i] = new WordData("" , 0 , "" , false);
		}
		
	}
	
	public void append(WordData word) {
		lock.lock();
		try {
			while (count == buffer.length) {
				//System.out.println("I am full");
				empty.await();
			}
			buffer[appendPointer] = new WordData(word.getWord(), word.getLineNumber() , word.getFileName() , true);
			appendPointer = (appendPointer + 1);
			if(appendPointer == buffer.length){
				appendPointer = 0;
			}
			count++;
			full.signal();
		} catch (Exception e) {

		}
		lock.unlock();

	}
	public WordData remove(){
		WordData returnWord  = new WordData("" , 0 , "" , false);
		lock.lock();
		try{
			while(count == 0){
				//System.out.println("I am empty");
				full.await();
			}
			
			returnWord = buffer[removePointer];
			buffer[removePointer] = null;
			removePointer++;
			if(removePointer == buffer.length){
				//System.out.println("REMOVE TOO BIG");
				removePointer = 0;
			}
			count = count - 1;
			empty.signal();
			
			
		}catch(Exception e){
			System.out.println(e);
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
