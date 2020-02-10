package metrics.listwise;


import data_representation.DataRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This implements a listwise comparison metric called the
 * Stoddard Index. This measures the relative frequencies of
 * elements in the test cases and reports a value >=1. A higher value
 * indicates more diverse test cases.
 *
 * @author luke
 */
public class StoddardIndex implements ListwiseComparisonStrategy{
    @Override
    public double compare(List<DataRepresentation> testsuite) {
        //the result comes from the equation: 1/sum(p*p), where p is a type of element in the test suite
        double result = 0;
        //to calculate this, you need to find all the possible elements in the test suite, and compare the relative frequencies
        HashMap<String, Double> frequencies = new HashMap<>();
        int numElements = 0;

        //get the frequencies
        for(DataRepresentation testcase: testsuite){
            while(testcase.hasNext()) {
                String s = testcase.next().toString();
                numElements++;
                if(frequencies.containsKey(s))
                    frequencies.put(s, frequencies.get(s) + 1);
                else
                    frequencies.put(s, 1.0);
            }
        }

        //compute the result
        for (Map.Entry<String, Double> stringDoubleEntry : frequencies.entrySet()) {
            double p = stringDoubleEntry.getValue() / numElements;
            result += p * p;
        }

        return 1 / result;
    }

    @Override
    public String getDescription() {
        return "report the relative frequency of test case elements as a value >= 1. Larger values represent more diverse suites.";
    }
}
