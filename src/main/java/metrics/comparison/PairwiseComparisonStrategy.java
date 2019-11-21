package metrics.comparison;

import data_representation.DataRepresentation;

/**
 * The interface for all pairwise comparison metrics to implement. This interface ensures that each metric has a function 
 * to compare two test cases, and can provide a description of how the metric works.
 * 
 * @author luke
 *
 */
public interface PairwiseComparisonStrategy {
	/**
	 * provides a description of how the comparison metric works
	 * 
	 * @return a description of how the comparison metrics works
	 */
	public String getDescription();
	/**
	 * compares two test cases and provides some measure of the similarity between the two
	 * 
	 * @param testCase1 the first test case to compare
	 * @param testCase2 the second test case to compare
	 * @return a number value representing the similarity between the two passed test cases
	 * @throws TestCaseFormatMismatchException thrown when the two passed test cases are of different formats
	 */
	public double compare(DataRepresentation testCase1, DataRepresentation testCase2) throws TestCaseFormatMismatchException;
}
