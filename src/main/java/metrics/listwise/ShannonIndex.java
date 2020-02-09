package metrics.listwise;

import data_representation.DataRepresentation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**implements a listwise diversity metric called the Shannon
 * Index. This is a measure of the relative frequencies of elements
 * appearing in the test suite. the larger the number, the more
 * evenly distributed the frequencies of elements in the test cases are
 *
 * @author luke
 */
public class ShannonIndex implements ListwiseComparisonStrategy{
    @Override
    public double compare(List<DataRepresentation> testsuite) {
        //the result comes from the equation: -sum(p*ln(p)), where p is a type of element in the test suite
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

        //compute the entropy
        for (Map.Entry<String, Double> stringDoubleEntry : frequencies.entrySet()) {
            double p =  stringDoubleEntry.getValue() / numElements;
            result += p * Math.log(p);
        }

        return -result;
    }

    @Override
    public String getDescription() {
        return "A measure of the entropy for sets. " +
                "Commonly used to measure diversity within a population in life sciences";
    }
}
