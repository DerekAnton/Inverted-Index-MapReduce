
public class WordData {

	private String word;
	private int lineNumber;
	private String fileName;
	private boolean initialized = false;
	
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

	public WordData(String word, int lineNumber, String fileName , boolean initialized){
		this.word = word;
		this.lineNumber = lineNumber;
		this.fileName = fileName;
		this.initialized = initialized;
	}

	public boolean isInitialized() {
		return initialized;
	}
	
	
	
}
