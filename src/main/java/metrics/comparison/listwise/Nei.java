package metrics.comparison.listwise;

import data_representation.DataRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an implementation of a listwise metric called nei's measure.
 * Nei's measure looks at each index of the set of test cases and
 * calculates the simpson diversity across that position in all test cases.
 * The final result is the average of this calculation across all indices.
 *
 * @author luke
 */
public class Nei implements ListwiseComparisonStrategy{
    @Override
    public double compare(List<DataRepresentation> testsuite) {
        double totalResult = 0;
        int longestTestCaseLength = 0;
        double locusResult;
        HashMap<String, Double> frequencies;
        boolean allCasesFullyTraversed;
        int numElements;

        if (testsuite == null || testsuite.isEmpty())
            return 0;

        do {
            frequencies = new HashMap<>();
            numElements = 0;
            locusResult = 0;
            allCasesFullyTraversed = true;
            for (DataRepresentation testcase : testsuite) {
                if (testcase.hasNext()) {
                    allCasesFullyTraversed = false;
                    String s = testcase.next().toString();
                    numElements++;
                    if (frequencies.containsKey(s))
                        frequencies.put(s, frequencies.get(s) + 1);
                    else
                        frequencies.put(s, 1.0);
                }
            }
            for (Map.Entry<String, Double> stringDoubleEntry : frequencies.entrySet()) {
                double p = stringDoubleEntry.getValue() / numElements;
                locusResult += p * p;
            }
            if(!allCasesFullyTraversed) {
                longestTestCaseLength++;
                totalResult += 1 - locusResult;
            }
        }while(!allCasesFullyTraversed);

        return (1.0/longestTestCaseLength) * totalResult;
    }

    @Override
    public String getDescription() {
        return "yields the average of the simpson diversity calculated across each position in the test case sequences.";
    }
}
