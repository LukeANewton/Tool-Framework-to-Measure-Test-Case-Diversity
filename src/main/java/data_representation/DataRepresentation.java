package data_representation;

import java.io.Serializable;
import java.util.Iterator;

import core.HelpTarget;
import core.InvalidFormatException;

/**
 * The interface for different test case formats to follow. This interface provides a means for a test case format
 * to be read into the system and iterated over for comparison.
 * 
 * @author luke
 */
public interface DataRepresentation extends Iterator<String>, HelpTarget, Serializable {
	/**
	 * determines if there is another element in the test case being iterated over that has not yet been seen
	 * 
	 * @return true if there is another element in the test case being iterated over
	 */
	public boolean hasNext();
	/**
	 * obtains the next element in the test case being iterated over
	 * 
	 * @return a string representation of the next element of the test case being iterated over
	 */
	public String next();
	/**
	 * parses a string into the internal DataRepresentation to later be iterated over
	 * 
	 * @param s the string test case to iterate over
	 * @throws InvalidFormatException thrown when the passed string does not conform to the test case format
	 */
	public String parse(String s) throws InvalidFormatException;
	/**
	 * determines if the passed string conforms to the test case format
	 * 
	 * @param s the test case string to format check
	 * @return true if the string matches the required format, otherwise false
	 */
	public boolean checkFormat(String s);
	/**
	 * returns the test case being iterated over
	 * 
	 * @return the test case being iterated over
	 */
	public String toString();
}