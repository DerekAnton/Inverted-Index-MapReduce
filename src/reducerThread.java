

public class reducerThread extends Thread {

	
	private BoundedBuffer buff;
	private int BufferNumber;
	
	public reducerThread(int BufferNumber)
	{
		this.BufferNumber = BufferNumber;
	}
	
	public void run() 
	{
		reduce();
	}
	
	public void reduce(){
		buff = Index.bbuffers[BufferNumber];
		
		//Index.invertedIndex.put(key, value);
	}
	
	
}
