package metrics.comparison.pairwise;

import data_representation.DataRepresentation;

import java.util.HashSet;

/**
 * a pairwise metric implementing the Dice coefficient. This
 * metric is similar to JaccardIndex in that it measures similarity
 * of two sequences as the overlap of their set of elements, although
 * it is done in a slightly different way. 0 is most diverse, while 1
 * is least diverse.
 *
 * @author luke
 */
public class Dice implements PairwiseComparisonStrategy {

    @Override
    public double compare(DataRepresentation testCase1, DataRepresentation testCase2) {
        HashSet<String> set1 = new HashSet<>();
        HashSet<String> set2 = new HashSet<>();
        HashSet<String> intersection = new HashSet<>();

        while(testCase1.hasNext()) {
            String s = testCase1.next().toString();
            set1.add(s);
        }

        while(testCase2.hasNext()) {
            String s = testCase2.next().toString();
            set2.add(s);
            if (set1.contains(s))
                intersection.add(s);
        }

        return (2.0 * intersection.size()) / (set1.size() + set2.size());
    }

    @Override
    public String getDescription() {
        return "a measure of overlap between two sets, with 0 being distinct and 1 being complete equality";
    }
}
