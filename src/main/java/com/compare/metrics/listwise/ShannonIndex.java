package com.compare.metrics.listwise;

import com.compare.data_representation.DataRepresentation;
import com.compare.metrics.comparison.TestCaseFormatMismatchException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ShannonIndex implements ListwiseComparisonStrategy{
    DataRepresentation[] testSuite;

    @Override
    public double compare(List<DataRepresentation> testsuite) throws TestCaseFormatMismatchException {
        //the result comes from the equation: -sum(p*ln(p)), where p is a type of element in the test suite
        double result = 0;
        //to calculate this, you need to find all the possible elements in the test suite, and compare the relative frequencies
        HashMap<String, Double> frequencies = new HashMap<String, Double>();

        //get the frequencies
        for(DataRepresentation testcase: testsuite){
            if(!testcase.getClass().equals(testsuite.get(0).getClass()))
                throw new TestCaseFormatMismatchException();

            while(testcase.hasNext()) {
                String s = testcase.next();
                if(frequencies.containsKey(s))
                    frequencies.put(s, frequencies.get(s) + 1);
                 else
                    frequencies.put(s, 1.0);
            }
        }

        //compute the entropy
        int totalElements = frequencies.size();
        Iterator it = frequencies.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            double p = ((Double) pair.getValue()) / totalElements;
            result += p * Math.log(p);
        }

        return -result;
    }

    @Override
    public String getDescription() {
        return "A measure of the entropy for sets of sequences. " +
                "Commonly used to measure diversity within a population in life sciences";
    }
}
