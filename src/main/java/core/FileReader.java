package capstone;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Handles the system functionalities for reading test cases from files.
 * 
 * @author luke

 */
public class FileReader {
	//the expected format of the test case to read from file
	private DataRepresentation parser;

	/**Constructor*/
	public FileReader() {
		parser = null;
	}

	/**
	 * set the format to use for reading test cases by supplying a DataRepresentation
	 * 
	 * @param parser the DataRepresentation object used for formatting the input
	 */
	public void setParser(DataRepresentation parser) {
		this.parser = parser;
	}

	/**
	 * reads a file into the system which contains a single test case at the specified path 
	 * 
	 * @param path a string representing the location of the file to read
	 * @return the test case formatted as a DataRepresentation object
	 * @throws FileNotFoundException thrown when no file is found at the specified path
	 * @throws InvalidFormatException thrown when the format of the test case found does not match the expected format
	 */
	public String readTestCase(String path) throws FileNotFoundException, InvalidFormatException {
		File file = new File(path); 

		Scanner sc = new Scanner(file); 

		StringBuffer s = new StringBuffer();
		while (sc.hasNextLine()) {
			s.append(sc.nextLine());  
			if(sc.hasNextLine())
				s.append(System.lineSeparator());
		}
		sc.close();
		
		return this.parser.parse(s.toString());
	}
	
}
