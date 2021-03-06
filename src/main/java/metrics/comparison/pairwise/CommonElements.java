package metrics.comparison.pairwise;

import data_representation.DataRepresentation;

/**
 * A pairwise comparison metrics which counts the number of elements in a pair of test cases that have the same values and are in the same positions
 *
 * @author luke
 *
 */
public class CommonElements implements PairwiseComparisonStrategy {

	@Override
	public double compare(DataRepresentation testCase1, DataRepresentation testCase2) {
		int commonElements = 0;

		while(testCase1.hasNext() && testCase2.hasNext()) {
			if(testCase1.next().equals(testCase2.next()))
				commonElements++;
		}

		return commonElements;
	}

	@Override
	public String getDescription() {
		return "counts the number of elements that appear at the same index in both sequences";
	}

}
