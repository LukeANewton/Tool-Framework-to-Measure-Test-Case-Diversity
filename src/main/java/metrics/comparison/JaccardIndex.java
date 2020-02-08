package metrics.comparison;

import data_representation.DataRepresentation;

import java.util.HashSet;

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
		return "provides a ratio between the intersection of two sets and the union of the sets";
	}

	@Override
	public double compare(DataRepresentation testCase1, DataRepresentation testCase2) {
		HashSet<String> set1 = new HashSet<>();
		HashSet<String> union = new HashSet<>();
		HashSet<String> intersection = new HashSet<>();
		
		while(testCase1.hasNext()) {
			String s = testCase1.next();
			set1.add(s);
			union.add(s);
		}
			
		while(testCase2.hasNext()) {
			String s = testCase2.next();
			union.add(s);
			if(set1.contains(s))
				intersection.add(s);
		}
		
		return (double)intersection.size()/union.size();
	}

}
