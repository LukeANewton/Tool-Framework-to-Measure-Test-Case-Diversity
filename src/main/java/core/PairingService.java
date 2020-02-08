package core;

import data_representation.DataRepresentation;
import org.apache.commons.lang3.SerializationUtils;
import utilities.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * A service that creates a list of pairs that the comparing service will use for comparisons.
 * This is based on comparing test cases from a single test suite or test cases from two or more different test suites.
 *
 * @author crushton
 */
public class PairingService {
    /**
     * Makes pairs to be compared from a single test suite. Each test case is compared to another test case once.
     *
     * @param testSuite the test suite of test cases
     * @return a list of pairs of test cases in the form of data representations
     */
    public List<Tuple<DataRepresentation, DataRepresentation>> makePairs(DataRepresentation[] testSuite) {
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        for (int i = 0; i < testSuite.length; i++) {
            for (int j = i + 1; j < testSuite.length; j++) {
                pairs.add(new Tuple<>(SerializationUtils.clone(testSuite[i]),
                        SerializationUtils.clone(testSuite[j])));
            }
        }
        return pairs;
    }

    /**
     * Makes pairs to be compared from one test suite to another. Each test case is compared to another test case once.
     *
     * @param testSuites an array of test suite arrays containing DataRepresentations for pairing
     * @return a list of pairs of test cases in the form of data representations
     */
    public List<Tuple<DataRepresentation, DataRepresentation>> makePairs(DataRepresentation[]... testSuites) {
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        // Observed column(Test Suite) of DataRepresentations
        for (int i = 0; i < testSuites.length; i++) {
            // Element(Test Case) in the observed column
            for (int j = 0; j < testSuites[i].length; j++) {
                // Loop through each other column after the observed column
                for (int k = i + 1; k < testSuites.length; k++) {
                    // Loop through each element in that other column
                    for (int m = 0; m < testSuites[k].length; m++) {
                        // Pair the observed element(Test Case) in the observed column(Test Suite) with the other element(TC) in the other column(TS)
                        pairs.add(new Tuple<>( SerializationUtils.clone(testSuites[i][j]),
                                SerializationUtils.clone(testSuites[k][m])));
                    }
                }
            }
        }
        return pairs;
    }
}
