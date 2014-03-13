

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
		String currentStringData[];
		String currentWord;
		String lineNumber;
		String fileName;
		String oldHashValue;
		String newHashValue;
		boolean allDone = false;
		WordData data;
		
		while(!allDone)
		{
			
			//DEREK!!!!!
			/*
			 * This Block of code below commented out is from old BoundedBuffer
			 * Class.
			 * Keeping it just incase.
			 */
				//System.out.println(Index.bbuffers[BufferNumber].isEmpty());
				// System.out.println(Index.bbuffers.length + "  " +
				// BufferNumber);
				//currentStringData = Index.bbuffers[BufferNumber].remove()
						//.split(" ");
				//for (String s : currentStringData) {
					 //System.out.println(s);
				//}
				 //System.out.println(currentStringData[0]);

				 
				// System.out.println(currentStringData[0] );
				//currentWord = currentStringData[0];
				//lineNumber = currentStringData[1];
				//fileName = currentStringData[2];
			if(!Index.buffers[BufferNumber].isEmpty()){
			data = Index.buffers[BufferNumber].remove();
			if (data != null) {
				currentWord = data.getWord();
				lineNumber = Integer.toString(data.getLineNumber());
				fileName = data.getFileName();
				//System.out.println(currentWord);

				if (Index.invertedIndex.containsKey(currentWord)) {
					oldHashValue = (String) Index.invertedIndex
							.get(currentWord);
					newHashValue = oldHashValue + "," + fileName + "@"
							+ lineNumber;

					Index.invertedIndex.put(currentWord, newHashValue);
				} else {
					newHashValue = fileName + "@"
							+ lineNumber;
					Index.invertedIndex.put(currentWord, newHashValue);
				}
			}
			
			}
			allDone = true;
			
			mapperThread.lock.lock();
			//System.out.println(Index.mappersActive);
			if(Index.mappersActive == 0){
				allDone = true;
				
			}
			mapperThread.lock.unlock();

			//Check if all the buffers are empty
			for(BBMonitor b : Index.buffers){
				if(!b.isEmpty()){
					//System.out.println("test1");

					allDone = false;
					
				}
			}
			//Check if all the Mapper threads are done adding words
			
			for(mapperThread m : Index.mapperThreadHolder){
				if(m.isAlive()){
					//System.out.println("test2");

					allDone = false;
				}
				
			//System.out.println(allDone);
			}
		
	}
		System.out.println("DEAD REDUCE");

	}
	
	
}
