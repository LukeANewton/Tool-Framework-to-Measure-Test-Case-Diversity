package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import data_representation.DataRepresentation;
import model.Config;

/**
 * Handles the system functionality for reading test cases from files.
 * 
 * @author luke

 */
public class FileReaderService {
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

		StringBuilder s = new StringBuilder();
		while (sc.hasNextLine()) {
			s.append(sc.nextLine());
			if(sc.hasNextLine())
				s.append(System.lineSeparator());
		}
		sc.close();
		return s.toString();
	}

	/**
	 * reads a test suite file located at filename into the passed DataRepresentation for later iteration over
	 *
	 * @param filename the test suite file containing test cases
	 * @param delimiter the character(s)/pattern that separates each test case in the file
	 * @param format the data representation that the test cases are read into
	 * @return the test cases formatted as DataRepresentation objects
	 */
	public DataRepresentation[] readIntoDataRepresentation(String filename, String delimiter, DataRepresentation format) throws InvalidFormatException, FileNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		if (format == null)
			throw new InvalidFormatException();

		String s = readFile(filename);

		if(s.equals("")) //the file is empty
			return new DataRepresentation[]{};

		//at this point, we have all the file read in, so we can split up the test cases
		String[] testCases;
		if (delimiter == null){
			testCases = new String[] {s};
		} else{
			testCases = s.split(delimiter);
		}

		//all the file contents is in the system, now we need to read them into data representations
		ArrayList<DataRepresentation> list = new ArrayList<>();
		for (String testCase : testCases) {
			DataRepresentation d = format.getClass().getConstructor().newInstance();
			d.parse(testCase);
			list.add(d);
		}
		return list.toArray(new DataRepresentation[0]);
	}

	/**
	 * reads a configuration file from the passed filename
	 * @param filename the name of the configuration file
	 * @throws FileNotFoundException thrown when passed filename can not be found
	 * @return a Config object containing all the information from the configuration file
	 */
	public Config readConfig(String filename) throws IOException {
		JsonReader jsonReader = new JsonReader(new FileReader(filename));
		Gson gson = new Gson();
		Config result = gson.fromJson(jsonReader, Config.class);
		jsonReader.close();
		return result;
	}

}
