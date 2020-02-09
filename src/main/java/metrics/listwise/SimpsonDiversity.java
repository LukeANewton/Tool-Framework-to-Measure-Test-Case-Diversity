package metrics.listwise;

import data_representation.DataRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this implements a listwise comparison metric called the
 * Simpson Diversity. This is a measure of the diversity of a
 * test suite on a scale of 0 to 1. the higher the number, the
 * more evenly distributed the frequencies of elements in the
 * test cases are.
 *
 * This number can be expressed as a probability. If we consider
 * all the different elements in all the test cases and select two
 * at random, this is the probability that the elements are not equal.
 *
 * @author luke
 */
public class SimpsonDiversity implements ListwiseComparisonStrategy {
    @Override
    public double compare(List<DataRepresentation> testsuite) {
        //the result comes from the equation: 1-sum(p*p), where p is a type of element in the test suite
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

        return 1 - result;
    }

    @Override
    public String getDescription() {
        return "a value between 0 and 1 indicating diversity within a set. values closer to 1 are more diverse";
    }
}
