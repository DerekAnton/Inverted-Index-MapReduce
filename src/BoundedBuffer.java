import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class BoundedBuffer {

	private static String[] buffer;
	private static WordData[] wBuffer = new WordData[10];
	private static LinkedList<WordData> buff = new LinkedList<WordData>();
	private final int size;
	Semaphore mutex;
	Semaphore empty;
	Semaphore full;
	private int last = 0;
	private int count = 0;
	private int modValue;

	public BoundedBuffer() {
		this.size = 10;
		buffer = new String[size];
		mutex = new Semaphore(1);
		empty = new Semaphore(size);
		full = new Semaphore(0);

	}
	
	public  synchronized void append(String str) {
		try {
			//System.out.println(str);

			mutex.acquire();

			if (count == size) {
				full.wait();
			}
			buffer[last] = str;
			last = (last + 1) % size;
			count = count + 1;
			full.notify();
			full.release();
			mutex.release();
			
			for(String s : buffer){
			//	System.out.println(s);
			}
			
		} catch (InterruptedException e) {
			System.out.println("Failed To Add Item To Buffer");
		}
	}
	/*
	public synchronized String remove() {
		String returnString = "";
		try {
			mutex.acquire();
			if (count == 0) {
				empty.wait();
			}
			returnString = buffer[(last - count) % size];
			count = count - 1;
			empty.notify();
			mutex.release();
		} catch (Exception e) {
			System.out.println("Failed To Remove Item To Buffer");
		}
		return returnString;
	}
*/
	public void Producer(String str) {
		try {
			empty.acquire();
			mutex.acquire();
			//buff.add(str);
			buffer[last] = str;
			last = (last + 1) % size;
			count++;
			mutex.release();
			full.release();
		} catch (InterruptedException e) {
			System.out.println("Failed To Add Item To Buffer");
		}
		
	}

	public String remove() {
		String returnString = "";
		try {
			full.acquire();
			mutex.acquire();
			
			//returnString = buff.remove();
			//System.out.println("Last " + last + " Count " + count);
			//System.out.println((last-count)% size);
			
			if(((last-count)%size) < 0 ){
				modValue = ((last-count)%size) + 10;
			}else{
				modValue =((last-count)%size);
			}
			
			returnString = buffer[modValue];
			buffer[modValue] = "";
			count--;
			mutex.release();
			empty.release();

		} catch (InterruptedException e) {
			System.out.println("Failed To Remove Item From Buffer");
		}

		return returnString;
	}
	
	
	
	
	public void wProducer(WordData str) {
		try {
			empty.acquire();
			mutex.acquire();
			//buff.add(str);
			wBuffer[last] = str;
			last = (last + 1) % size;
			count++;
			mutex.release();
			full.release();
		} catch (InterruptedException e) {
			System.out.println("Failed To Add Item To Buffer");
		}
		
	}

	public synchronized WordData wRemove() {
		WordData returnWord = new WordData("" , 0 , "" , false);
		try {
			full.acquire();
			mutex.acquire();
		
			if(((last-count)%size) < 0 ){
				//modValue = ((last-count)%size) + 10;
			}else{
				modValue =((last-count)%size);
			}
			modValue =((last-count)%size);

			returnWord = wBuffer[modValue];
			wBuffer[modValue] =  new WordData("" , 0 , "" , false);
			count--;
			mutex.release();
			empty.release();

		} catch (InterruptedException e) {
			System.out.println("Failed To Remove Item From Buffer");
		}

		return returnWord;
	}
	
	
	
	public synchronized boolean isEmpty(){
		boolean empty = true;
		for(String s : buffer){
			if(s != ""){
				empty = false;
			}
		}
		empty = true;
		for(WordData data : wBuffer){
			if(data.isInitialized()){
				empty = false;
			}
		}
		
		
		return empty;
	}
	
}
