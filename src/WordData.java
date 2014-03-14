	/*
	 * Mitchell Hebert
	 * Derek Anton
	 * 
	 * OS Spring 2014
	 * Professor Sean Barker
	 * 
	 * WordData Object is used to pass data through 
	 * BBMonitors. Includes getter and setter methods
	 * for all appropriate pieces of data
	 */


public class WordData {

	private String word;
	private int lineNumber;
	private String fileName;
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public  String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public WordData(String word, int lineNumber, String fileName){
		this.word = word;
		this.lineNumber = lineNumber;
		this.fileName = fileName;
	}
	
	
}
