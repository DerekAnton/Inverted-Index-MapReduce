

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
		
		while(true)
		{
			currentStringData = buff.remove().split(" ");
			currentWord = currentStringData[0];
			lineNumber = currentStringData[1];
			fileName = currentStringData[2];
			
			if(Index.invertedIndex.contains(currentWord)){
				oldHashValue = (String) Index.invertedIndex.get(currentWord);
				newHashValue = oldHashValue + " , " + lineNumber +  "@"  + fileName;
				Index.invertedIndex.put(currentWord, newHashValue);
			}
			else
			{
				newHashValue = currentWord + " " + lineNumber + "@" + fileName;
				Index.invertedIndex.put(currentWord, newHashValue);				
			}
			
		}
		//Index.invertedIndex.put(key, value);
	}
	
	
}
