/*
 * Mitchell Hebert
 * Derek Anton
 * 
 * OS Spring 2014
 * Professor Sean Barker
 * 
 * Thread code for removing items from
 * buffer and computing inverted index
 * 
 */

public class reducerThread extends Thread {

	private int BufferNumber;

	public reducerThread(int BufferNumber) {
		this.BufferNumber = BufferNumber;
	}

	public void run() {
		reduce();
	}

	public void reduce() {
		String currentWord;
		String lineNumber;
		String fileName;
		String oldHashValue;
		String newHashValue;
		boolean allDone = false;
		WordData data;

		// While there is data left to reduce
		while (!allDone) {
			//If there is nothing to grab, don't try
			if (!Index.buffers[BufferNumber].isEmpty()) {
				//Remove item from bounded buffer
				data = Index.buffers[BufferNumber].remove();
				//Make sure you didn't remove null data
				if (data != null) {
					
					//Get information from word object
					currentWord = data.getWord();
					lineNumber = Integer.toString(data.getLineNumber());
					fileName = data.getFileName();

					//If word already exists in inverted index
					if (Index.invertedIndex.containsKey(currentWord)) {
						oldHashValue = (String) Index.invertedIndex
								.get(currentWord);
						newHashValue = oldHashValue + "," + fileName + "@"
								+ lineNumber;

						Index.invertedIndex.put(currentWord, newHashValue);
					
					//If it is a new word
					} else {
						newHashValue = fileName + "@" + lineNumber;
						Index.invertedIndex.put(currentWord, newHashValue);
					}
				}

			}else{
			
			}
			
			//Check to see if all the threads are done
			allDone = true;
			mapperThread.lock.lock();
			if (Index.mappersActive == 0) {
				allDone = true;

			}
			mapperThread.lock.unlock();
			//System.out.println("test");
			// Check if all the buffers are empty
			for (BBMonitor b : Index.buffers) {
				if (!b.isEmpty()) {
					allDone = false;
				}
			}
			// Check if all the Mapper threads are done adding words
			for (mapperThread m : Index.mapperThreadHolder) {
				if (m.isAlive()) {
					//System.out.println("Alive");
					allDone = false;
				}
			}
			//System.out.println(allDone);
		}
	//System.out.println("Dead");
	}

}
