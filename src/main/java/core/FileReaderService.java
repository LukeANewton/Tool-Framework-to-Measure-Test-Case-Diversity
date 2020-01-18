package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

import data_representation.DataRepresentation;

/**
 * Handles the system functionality for reading test cases from files.
 * 
 * @author luke

 */
public class FileReaderService {
	/**
	 * reads a file into the system which contains a single test case at the specified path 
	 * 
	 * @param filename a string representing the location of the file to read
	 * @param format the data representation of the test case
	 * @return the test case formatted as a DataRepresentation object
	 * @throws FileNotFoundException thrown when no file is found at the specified path
	 * @throws InvalidFormatException thrown when the format of the test case found does not match the expected format
	 */
	public String read(String filename, DataRepresentation format) throws FileNotFoundException, InvalidFormatException {
		if (format == null)
			throw new InvalidFormatException();

		String s = readFile(filename);
		return format.parse(s.toString());
	}

	/**
	 * reads a file's contents into a single string
	 *
	 * @param filename the location of the file
	 * @return a String containing the entire contents of the file
	 * @throws FileNotFoundException thrown when no file with the specified name is found
	 */
	private String readFile(String filename) throws FileNotFoundException {
		if(filename.equals(""))
			throw new FileNotFoundException();

		File file = new File(filename);

		Scanner sc = new Scanner(file);

		StringBuffer s = new StringBuffer();
		while (sc.hasNextLine()) {
			s.append(sc.nextLine());
			if(sc.hasNextLine())
				s.append(System.lineSeparator());
		}
		sc.close();
		return s.toString();
	}

	/**
	 * reads a file into the system which contains a multiple test cases in one file at the specified path
	 *
	 * @param filename a string representing the location of the file to read
	 * @param delimiter the sequence of characters used to separate test cases in a single file
	 * @param format the data representation of the test case
	 * @return the test case formatted as a DataRepresentation object
	 * @throws FileNotFoundException thrown when no file is found at the specified path
	 * @throws InvalidFormatException thrown when the format of the test case found does not match the expected format
	 */
	public String[] read(String filename, String delimiter, DataRepresentation format) throws FileNotFoundException, InvalidFormatException {
		if (format == null)
			throw new InvalidFormatException();

		String s = readFile(filename);

		//at this point, we have all the file read in, so we can split up the test cases
		String[] testCases;
		if (delimiter == null){
			testCases = new String[] {s};
		} else{
			testCases = s.split(delimiter);
		}

		for(int i = 0; i < testCases.length; i++){
			testCases[i] = format.parse(testCases[i]);
		}

		return testCases;
	}

	/**
	 * reads a test suite file located at filename into the passed DataRepresentation for later iteration over
	 *
	 * @param filename the test suite file containing test cases
	 * @param delimiter the character(s)/pattern that separates each test case in the file
	 * @param format the data representation that the test cases are read into
	 * @return
	 */
	public DataRepresentation[] readIntoDataRepresentation(String filename, String delimiter, DataRepresentation format) throws InvalidFormatException, FileNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		if (format == null)
			throw new InvalidFormatException();

		String s = readFile(filename);

		//at this point, we have all the file read in, so we can split up the test cases
		String[] testCases;
		if (delimiter == null){
			testCases = new String[] {s};
		} else{
			testCases = s.split(delimiter);
		}

		//all the file contents is in the system, now we need to read them into data representations
		ArrayList<DataRepresentation> list = new ArrayList<>();
		for(int i = 0; i < testCases.length; i++){
			DataRepresentation d = format.getClass().getConstructor().newInstance();
			d.parse(testCases[i]);
			list.add(d);
		}
		return list.toArray(new DataRepresentation[0]);
	}

}
