package metrics.comparison;

import data_representation.DataRepresentation;

/**
 * a pairwise distance metric that implements a longest common substring
 * comparison. This metric returns the length of the longest string of
 * contiguous elements between two sequences. The larger the resulting value,
 * the more similar the test cases are.
 *
 * @author luke
 */
public class LongestCommonSubstring implements PairwiseComparisonStrategy {
    @Override
    public double compare(DataRepresentation testCase1, DataRepresentation testCase2) {
        int length = 0;
        int LCS = 0;

        while(testCase1.hasNext() && testCase2.hasNext()) {
            if(testCase1.next().equals(testCase2.next()))
                length++;
            else{
                if(length > LCS)
                    LCS = length;
                length = 0;
            }
        }

        return Math.max(length, LCS);
    }

    @Override
    public String getDescription() {
        return "returns a value indicating the longest common sequence of contiguous elements in a pair of test cases";
    }
}
