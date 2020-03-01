package core;

import data_representation.CSV;
import data_representation.DataRepresentation;
import metrics.comparison.listwise.ShannonIndex;
import metrics.comparison.pairwise.CommonElements;
import metrics.comparison.pairwise.PairwiseComparisonStrategy;
import org.junit.Before;
import org.junit.Test;
import utilities.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite that tests the comparison service
 */
public class ComparisonServiceTest {

    private List<Tuple<DataRepresentation, DataRepresentation>> differentTestCasePairs, sameTestCasePairs, halfSimilarPairs;
    private PairwiseComparisonStrategy strategy;
    private ComparisonService comparisonService;
    private final double TOLERANCE = 0.01;

    @Before
    public void setup() {
        differentTestCasePairs = new ArrayList<>();
        sameTestCasePairs = new ArrayList<>();
        halfSimilarPairs = new ArrayList<>();
        int dissimilarValue;
        int value;
        try {
            for (int i = 0; i < 10; i++) {
                value = i;
                differentTestCasePairs.add(new Tuple<>(new CSV(value + ""), new CSV(++value + "")));
                sameTestCasePairs.add(new Tuple<>(new CSV(value + ""), new CSV(value + "")));
                dissimilarValue = (i < 5) ? value : 99;
                halfSimilarPairs.add(new Tuple<>(new CSV(value + ""), new CSV(dissimilarValue + "")));
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        strategy = new CommonElements();
        comparisonService = new ComparisonService(Executors.newFixedThreadPool(2));
    }

    /**
     * Test that the comparison service is comparing pairs correctly when given completely similar pairs.
     */
    @Test
    public void testCompareSimilarPairs() throws Exception {
        List<Double> results = comparisonService.pairwiseCompare(sameTestCasePairs, strategy, null, false);
        assertEquals(10, results.size());
        assertTrue(results.stream().allMatch(results.get(0)::equals));
    }

    /**
     * Test that the comparison service is comparing pairs correctly when given completely dissimilar pairs.
     */
    @Test
    public void testCompareDissimilarPairs() throws Exception {
        List<Double> results = comparisonService.pairwiseCompare(differentTestCasePairs, strategy, null, false);
        assertEquals(10, results.size());
        assertTrue(results.stream().allMatch(results.get(0)::equals));
    }

    /**
     * Test that the comparison service is comparing pairs correctly when given half similar pairs.
     * This is a 0.0 value because the current aggregation strategy finds the minimum
     */
    @Test
    public void testCompareHalfSimilarPairs() throws Exception {
        List<Double> results = comparisonService.pairwiseCompare(halfSimilarPairs, strategy, null, false);
        assertEquals(10, results.size());
    }


    /** TODO: This appears to be broken; shouldn't the result be 3 similarities?
     * Test for the comparison service with a listwise comparison.
     * @throws Exception when unable to make comparisons
     */
    @Test
    public void testListwiseComparison() throws Exception {
        List<List<DataRepresentation>> testsuites = new ArrayList<>();
        List<DataRepresentation> testsuite = new ArrayList<>();
        testsuite.add(new CSV("1,2,3,4,5,6"));
        testsuite.add(new CSV("5,4,8,5,2,4,7"));
        testsuite.add(new CSV("1,1,1,4,5,8"));
        testsuites.add(testsuite);

        List<Double> results = comparisonService.listwiseCompare(testsuites, new ShannonIndex(), null, false);
        assertEquals(1, results.size());
    }

    private  List<String> getTestSuiteStrings() {
        List<String> testsuite = new ArrayList<>();
        testsuite.add("1,2,3,4,5,6");
        testsuite.add("5,4,8,5,2,4,7");
        testsuite.add("1,1,1,4,5,8");
        return testsuite;
    }

    /**
     * Builds a test suite of data representations given a list of test cases.
     *
     * @param testCases a pattern on elements distinguished by a delimiter.
     * @return a list of data representations that allow iteration over each element in a test case.
     * @throws InvalidFormatException when we're unable to parse the test cases.
     */
    private List<DataRepresentation> buildTestSuite(List<String> testCases) throws InvalidFormatException {
        List<DataRepresentation> testsuite = new ArrayList<>();
        for (String s : testCases) {
            DataRepresentation d = new CSV();
            d.parse(s);
            testsuite.add(d);
        }
        return testsuite;
    }

    /**
     * Test for the Comparison service with a listwise comparison that uses the thread pool.
     */
    @Test
    public void testPairwiseThreadPoolDoesNotChangeResult() throws Exception {
        PairingService p = new PairingService(Executors.newFixedThreadPool(1));
        assertEquals(comparisonService.pairwiseCompare(p.makePairsWithin(
                null, new CSV(), getTestSuiteStrings().toArray(new String[3])), new CommonElements(), null, true).get(0),
                comparisonService.pairwiseCompare(p.makePairsWithin(
                        null, new CSV(), getTestSuiteStrings().toArray(new String[3])), new CommonElements(), null, false).get(0),
                TOLERANCE);
    }

    /**
     * Test for the Comparison service with a listwise comparison that uses the thread pool.
     */
    @Test
    public void testListwiseThreadPoolDoesNotChangeResult() throws Exception {
        List<List<DataRepresentation>> testsuites1 = new ArrayList<>();
        List<List<DataRepresentation>> testsuites2 = new ArrayList<>();
        testsuites1.add(buildTestSuite(getTestSuiteStrings()));
        testsuites2.add(buildTestSuite(getTestSuiteStrings()));

        assertEquals(comparisonService.listwiseCompare(testsuites1, new ShannonIndex(), null, true).get(0),
                comparisonService.listwiseCompare(testsuites2, new ShannonIndex(), null, false).get(0),
                TOLERANCE);
    }
}