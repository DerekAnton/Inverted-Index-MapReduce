
public class WordData {

	public static String word;
	public static int lineNumber;
	public static String fileName;
	public static boolean initialized = false;
	
	public static String getWord() {
		return word;
	}

	public static void setWord(String word) {
		WordData.word = word;
	}

	public static int getLineNumber() {
		return lineNumber;
	}

	public static void setLineNumber(int lineNumber) {
		WordData.lineNumber = lineNumber;
	}

	public static String getFileName() {
		return fileName;
	}

	public static void setFileName(String fileName) {
		WordData.fileName = fileName;
	}

	public WordData(String word, int lineNumber, String fileName , boolean initialized){
		this.word = word;
		this.lineNumber = lineNumber;
		this.fileName = fileName;
		this.initialized = initialized;
	}

	public static boolean isInitialized() {
		return initialized;
	}
	
	
	
}
