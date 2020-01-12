package core;

import data_representation.CSV;
import data_representation.DataRepresentation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test Suite that tests the test case pairing functions for the comparison service
 */
public class PairingServiceTest {

    private PairingService pairingService = new PairingService();
    private DataRepresentation[] representations1, representations2, representations3,
            representations4, representations5, representations6, largeSuite, emptySuite;

    @Before
    public void setup() {
        try {
            representations1 = new DataRepresentation[] {new CSV("1"), new CSV("2"), new CSV("3")};
            representations2 = new DataRepresentation[] {new CSV("4"), new CSV("5"), new CSV("6")};
            representations3 = new DataRepresentation[] {new CSV("7"), new CSV("8"), new CSV("9")};

            representations4 = new DataRepresentation[] {new CSV("1")};
            representations5 = new DataRepresentation[] {new CSV("4"), new CSV("5")};
            representations6 = new DataRepresentation[] {new CSV("7"), new CSV("8"), new CSV("9")};

            largeSuite = new DataRepresentation[] {
                    new CSV("1"), new CSV("2"), new CSV("3"),
                    new CSV("4"), new CSV("5"), new CSV("6"),
                    new CSV("7"), new CSV("8"), new CSV("9")
            };
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        emptySuite = new DataRepresentation[]{};
    }

    /**
     * Tests that each element in a test suite is paired with another element in the same suite at least once that is not itself.
     */
    @Test
    public void testSingleSuite() {
        assertEquals("Failed to correctly pair test cases using a single suite.",
                3, pairingService.makePairs(representations1).size());
        assertEquals("Failed to correctly pair test cases using a single, large suite.",
                39, pairingService.makePairs(largeSuite).size());
    }

    /**
     * Tests that each element in a test suite is paired with another element in another test suite at least once.
     * There should be (number of test cases in a suite)^(number of test suites) pairs.
     * Three test cases in each suite, with three suites yields 3^3 = 27 pairs.
     */
    @Test
    public void testMultipleSuites() {
        assertEquals("Failed to correctly pair test cases with multiple test suites.",
                27, pairingService.makePairs(representations1, representations2, representations3).size());
    }

    /**
     * Tests that an empty suite will return no pairs.
     */
    @Test
    public void testEmptySuite() {
        assertEquals("Returned a pair when there are no test cases.",
                0, pairingService.makePairs(emptySuite).size());
    }

    /**
     * Tests that an empty suite will return zero pairs when we compare a populated suite with it.
     */
    @Test
    public void testMultipleSuitesWithEmptySuite() {
        assertEquals("Returned a pair when there are no test cases to make pairs from.",
                0, pairingService.makePairs(representations1, emptySuite).size());
    }

    /**
     * Tests that each element in a test suite is paired with another element in another test suite at least once.
     * Variant: Different number of test cases in each suite
     * There should be (number of test cases in a suite)^(number of test suites) pairs.
     *
     */
    @Test
    public void testMultipleSuitesGivenVariableSuiteSizes() {
        assertEquals("Failed to correctly pair test cases using suites of varied sizes on the multiple test suites function.",
                11, pairingService.makePairs(representations4, representations5, representations6).size());

    }
}
