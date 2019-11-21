package metrics.comparison;

import java.util.HashSet;

import data_representation.DataRepresentation;

/**
 * A pairwise comparison metric which provides a ratio of the size of the intersection of sets of 
 * elements in each sequence against the size of the union of the element sets.
 * 
 * @author luke
 *
 */
public class JaccardIndex implements PairwiseComparisonStrategy {

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double compare(DataRepresentation testCase1, DataRepresentation testCase2)
			throws TestCaseFormatMismatchException {
		HashSet<String> set1 = new HashSet<>();
		HashSet<String> set2 = new HashSet<>();
		HashSet<String> union = new HashSet<>();
		HashSet<String> intersection = new HashSet<>();
		
		while(testCase1.hasNext()) {
			String s = testCase1.next();
			set1.add(s);
			union.add(s);
		}
			
		while(testCase2.hasNext()) {
			String s = testCase2.next();
			set2.add(s);
			union.add(s);
			if(set1.contains(s))
				intersection.add(s);
		}
		
		return (double)intersection.size()/union.size();
	}

}
