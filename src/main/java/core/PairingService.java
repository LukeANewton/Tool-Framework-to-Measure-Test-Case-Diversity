package core;

import data_representation.DataRepresentation;
import javafx.util.Pair;

import java.util.*;

/**
 * A service that creates a list of pairs that the comparing service will use for comparisons.
 * This is based on comparing test cases from a single test suite or test cases from two or more different test suites.
 *
 * @author crushton
 */
public class PairingService {

    private HashSet<Pair<DataRepresentation, DataRepresentation>> pairs;

    public PairingService() {
        pairs = new HashSet<>();
    }


    /**
     * Makes pairs to be compared from a single test suite. Each test case is compared to another test case once.
     *
     * @param testSuite the test suite of test cases
     * @return an unordered set of pairs of test cases in the form of data representations
     */
    public HashSet<Pair<DataRepresentation, DataRepresentation>> makePairs(DataRepresentation[] testSuite) {
        for (int i = 0; i < testSuite.length; i++) {
            for (int j = i + 1; j < testSuite.length; j++) {
                pairs.add(new Pair<>(testSuite[i], testSuite[j]));
            }
        }
        return pairs;
    }

    /**
     * Makes pairs to be compared from one test suite to another. Each test case is compared to another test case once.
     *
     * @param testSuites an array of test suite arrays containing DataRepresentations for pairing
     * @return an unordered set of pairs of test cases in the form of data representations
     */
    public HashSet<Pair<DataRepresentation, DataRepresentation>> makePairs(DataRepresentation[]... testSuites) {
        // Observed column(Test Suite) of DataRepresentations
        for (int i = 0; i < testSuites.length; i++) {
            // Element(Test Case) in the observed column
            for (int j = 0; j < testSuites[i].length; j++) {
                // Loop through each other column after the observed column
                for (int k = i + 1; k < testSuites.length; k++) {
                    // Loop through each element in that other column
                    for (int l = 0; l < testSuites[k].length; l++) {
                        // Pair the observed element(Test Case) in the observed column(Test Suite) with the other element(TC) in the other column(TS)
                        pairs.add(new Pair<>(testSuites[i][j], testSuites[k][l]));
                    }
                }
            }
        }
        return pairs;
    }
}
