

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
		String currentStringData[];
		String currentWord;
		String lineNumber;
		String fileName;
		String oldHashValue;
		String newHashValue;
		boolean allDone = false;

		
		while(!allDone)
		{
			
			if (!Index.bbuffers[BufferNumber].isEmpty()) {
				//System.out.println(Index.bbuffers[BufferNumber].isEmpty());
				// System.out.println(Index.bbuffers.length + "  " +
				// BufferNumber);
				currentStringData = Index.bbuffers[BufferNumber].remove()
						.split(" ");
				for (String s : currentStringData) {
					// System.out.println(s);
				}
				// System.out.println(currentStringData[0] );
				currentWord = currentStringData[0];
				lineNumber = currentStringData[1];
				fileName = currentStringData[2];

				if (Index.invertedIndex.containsKey(currentWord)) {
					oldHashValue = (String) Index.invertedIndex.get(currentWord);
					//System.out.println(oldHashValue);
					newHashValue = oldHashValue + " , " + lineNumber + "@" + fileName;
					//System.out.println(newHashValue);

					Index.invertedIndex.put(currentWord, newHashValue);
				} else {
					newHashValue = currentWord + " " + lineNumber + "@"
							+ fileName;
					Index.invertedIndex.put(currentWord, newHashValue);
				}
			}
			allDone = true;
			for(BoundedBuffer b : Index.bbuffers){
				if(!b.isEmpty()){
					allDone = false;
				}
			}
		
	}

		//Index.invertedIndex.put(key, value);
	}
	
	
}
