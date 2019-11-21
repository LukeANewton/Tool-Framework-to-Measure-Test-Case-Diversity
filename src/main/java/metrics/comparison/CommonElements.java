package metrics.comparison;

import data_representation.DataRepresentation;

/**
 * A pairwise comparison metrics which counts the number of elements in a pair of test cases that have the same values and are in the same positions
 *
 * @author luke
 *
 */
public class CommonElements implements PairwiseComparisonStrategy {

	@Override
	public double compare(DataRepresentation testCase1, DataRepresentation testCase2) 
			throws TestCaseFormatMismatchException {
		int commonElements = 0;

		if(!testCase1.checkFormat(testCase2.toString()) || !testCase2.checkFormat(testCase1.toString()))
			throw new TestCaseFormatMismatchException();

		while(testCase1.hasNext() && testCase2.hasNext()) {
			String s1 = testCase1.next();
			String s2 = testCase2.next();
			if(s1.equals(s2)) 
				commonElements++;
		}

		return commonElements;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
