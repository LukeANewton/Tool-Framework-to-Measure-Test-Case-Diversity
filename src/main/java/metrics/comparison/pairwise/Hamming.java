package metrics.comparison.pairwise;

import data_representation.DataRepresentation;

/**
 * a pairwise metric that implements a hamming distance. That is, a
 * comparison of two sequences that counts the number of elements that
 * are unequal at each position. Usually this requires both sequences be
 * the same length, this has not been implemented here and instead the
 * algorithm simply stops when it reaches the eng of one sequence. The
 * larger the value, the more dissimilar the test cases are.
 *
 * @author luke
 */
public class Hamming implements PairwiseComparisonStrategy {
    @Override
    public double compare(DataRepresentation testCase1, DataRepresentation testCase2) {
        int diff = 0;

        while(testCase1.hasNext() && testCase2.hasNext()) {
            if(!testCase1.next().equals(testCase2.next()))
                diff++;
        }

        return diff;
    }

    @Override
    public String getDescription() {
        return "compares two test cases by the number of positions in the test cases that are different";
    }
}
