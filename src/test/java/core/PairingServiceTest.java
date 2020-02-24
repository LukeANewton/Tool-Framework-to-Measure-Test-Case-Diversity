package core;

import data_representation.CSV;
import data_representation.DataRepresentation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user_interface.ConsoleOutputService;
import utilities.Tuple;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/*Test Suite that tests the test case pairing functions for the pairing service*/
public class PairingServiceTest {
    private PairingService pairingService = new PairingService(Executors.newFixedThreadPool(1));
    private String[] representations1, representations2, representations3,
            representations4, representations5, representations6, largeSuite, emptySuite;
    private PrintStream originalOut;
    private ByteArrayOutputStream outContent;
    private ConsoleOutputService c;

    @Before
    public void setup() {
        representations1 = new String[] {"1", "2", "3"};
        representations2 = new String[] {"4", "5", "6"};
        representations3 = new String[] {"7", "8", "9"};

        representations4 = new String[] {"1"};
        representations5 = new String[] {"4", "5"};
        representations6 = new String[] {"7", "8", "9"};

        largeSuite = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
        emptySuite = new String[]{};
        originalOut = System.out;
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        c = new ConsoleOutputService();
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    /*Tests that each element in a test suite is paired with another element in the same suite at least once that is not itself.*/
    public void testSingleSuite() throws Exception {
        assertEquals("Failed to correctly pair test cases using a single suite.",
                3, pairingService.makePairsWithin(null, new CSV(), representations1).size());
    }

    @Test
    /*Tests that each element in a large test suite is paired with another element in the same suite at least once that is not itself.*/
    public void testLargeSingleSuite() throws Exception {
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = pairingService.makePairsWithin(null, new CSV(), largeSuite);
        assertEquals("Failed to correctly pair test cases using a single, large suite. Pairs: " + Arrays.toString(pairs.toArray()),
                36, pairs.size());
    }

    @Test
    /*Tests that each element in a test suite is paired with another element in another test suite at least once.
     * There should be (number of test cases in a suite)^(number of test suites) pairs.
     * Three test cases in each suite, with three suites yields 3^3 = 27 pairs.*/
    public void testMultipleSuites() throws Exception {
        assertEquals("Failed to correctly pair test cases with multiple test suites.",
                27, pairingService.makePairsBetween(null, new CSV(),
                        representations1, representations2, representations3).size());
    }

    @Test
    /*Tests that an empty suite will return no pairs.*/
    public void testEmptySuite() throws Exception {
        assertEquals("Returned a pair when there are no test cases.",
                0, pairingService.makePairsWithin(null, new CSV(), emptySuite).size());
    }

    @Test
    /*Tests that an empty suite will return zero pairs when we compare a populated suite with it.*/
    public void testMultipleSuitesWithEmptySuite() throws Exception {
        assertEquals("Returned a pair when there are no test cases to make pairs from.",
                0, pairingService.makePairsBetween(null, new CSV(), representations1, emptySuite).size());
    }

    @Test
    /*Tests that each element in a test suite is paired with another element in another test suite at least once.
     * Variant: Different number of test cases in each suite
     * There should be (number of test cases in a suite)^(number of test suites) pairs. */
    public void testMultipleSuitesGivenVariableSuiteSizes() throws Exception {
        assertEquals("Failed to correctly pair test cases using suites of varied sizes on the multiple test suites function.",
                11, pairingService.makePairsBetween(null, new CSV(),
                        representations4, representations5, representations6).size());
    }

    /*helper method for checking that the progress bar successfully completed in an operation*/
    private void checkProgressHelper(String[]... testSuites) throws Exception {
        if(testSuites.length == 1)
            pairingService.makePairsWithin(c, new CSV(), testSuites[0]);
        else
            pairingService.makePairsBetween(c, new CSV(), testSuites);
        String expected = "[==========]" + System.lineSeparator();
        String actual = outContent.toString();
        assertEquals(expected, actual.substring(actual.length() - expected.length()));
    }

    @Test
    /*test to ensure that the progress ar works for single list pairing*/
    public void testProgressBarSingleList() throws Exception {
        checkProgressHelper(representations1);
    }

    @Test
    /*test to ensure that the progress ar works for multi list pairing*/
    public void testProgressBarMultipleList() throws Exception {
        checkProgressHelper(representations1, representations2, representations3, representations4);
    }
}
