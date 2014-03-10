import java.util.concurrent.Semaphore;

public class BoundedBuffer {

	private static String[] buffer;
	private final int size;
	Semaphore mutex;
	Semaphore empty;
	Semaphore full;
	private int last = 0;
	private int count = 0;

	public BoundedBuffer() {
		this.size = 10;
		buffer = new String[size];
		mutex = new Semaphore(1);
		empty = new Semaphore(size);
		full = new Semaphore(1);

	}

	public void Producer(String str) {
		try {
			empty.acquire();
			mutex.acquire();
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
			returnString = buffer[(last - count) % size];
			buffer[(last - count) % size] = "";
			count--;
			mutex.release();
			empty.release();

		} catch (InterruptedException e) {
			System.out.println("Failed To Remove Item From Buffer");
		}

		return returnString;
	}
	public boolean isEmpty(){
		boolean empty = true;
		for(String s : buffer){
			if(s != ""){
				empty = false;
			}
		}
		return empty;
	}
	
}
