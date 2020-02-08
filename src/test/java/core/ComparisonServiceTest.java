package core;

import data_representation.CSV;
import data_representation.DataRepresentation;
import metrics.aggregation.AggregationStrategy;
import metrics.aggregation.AverageValue;
import metrics.comparison.CommonElements;
import metrics.comparison.PairwiseComparisonStrategy;
import metrics.listwise.ShannonIndex;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.Tuple;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite that tests the comparison service
 */
public class ComparisonServiceTest {

    private List<Tuple<DataRepresentation, DataRepresentation>> differentTestCasePairs, sameTestCasePairs, halfSimilarPairs;
    private PairwiseComparisonStrategy strategy;
    private AggregationStrategy aggregationStrategy;
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
                dissimilarValue = (i < 5)? value : 99;
                halfSimilarPairs.add(new Tuple<>(new CSV(value + ""), new CSV(dissimilarValue + "")));
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        strategy = new CommonElements();
        aggregationStrategy = new AverageValue();
        comparisonService = new ComparisonService();
        comparisonService.setUpThreadPool(2);
    }

    @After
    public void clean() {
        try {
            comparisonService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Test that the comparison service is comparing pairs correctly when given completely similar pairs.
     */
    @Test
    public void testCompareSimilarPairs() throws Exception {
        assertEquals(1.0,
                Double.parseDouble(comparisonService.pairwiseCompare(sameTestCasePairs, strategy, aggregationStrategy, null, false)),
                TOLERANCE);
    }

    /*
     * Test that the comparison service is comparing pairs correctly when given completely dissimilar pairs.
     */
    @Test
    public void testCompareDissimilarPairs() throws Exception {
        assertEquals(0.0,
                Double.parseDouble(comparisonService.pairwiseCompare(differentTestCasePairs, strategy, aggregationStrategy, null, false)),
                TOLERANCE);
    }

    /*
     * Test that the comparison service is comparing pairs correctly when given half similar pairs.
     * This is a 0.0 value because the current aggregation strategy finds the minimum
     */
    @Test
    public void testCompareHalfSimilarPairs() throws Exception {
        assertEquals(0.5,
                Double.parseDouble(comparisonService.pairwiseCompare(halfSimilarPairs, strategy, aggregationStrategy, null, false)),
                TOLERANCE);
    }

    @Test
    /*Test for the Comparison service with a listwise comparison that uses the thread pool*/
    public void testListwiseComparison() throws Exception {
        List<List<DataRepresentation>> testsuites = new ArrayList<>();
        List<DataRepresentation> testsuite = new ArrayList<>();
        testsuite.add(new CSV("1,2,3,4,5,6"));
        testsuite.add(new CSV("5,4,8,5,2,4,7"));
        testsuite.add(new CSV("1,1,1,4,5,8"));
        testsuites.add(testsuite);

        assertEquals(2.512,
                Double.parseDouble(comparisonService.listwiseCompare(testsuites, new ShannonIndex(), aggregationStrategy, null, false)),
                TOLERANCE);
    }

    private  List<DataRepresentation> buildTestSuite() throws InvalidFormatException {
        List<DataRepresentation> testsuite = new ArrayList<>();
        testsuite.add(new CSV("1,2,3,4,5,6"));
        testsuite.add(new CSV("5,4,8,5,2,4,7"));
        testsuite.add(new CSV("1,1,1,4,5,8"));
        return testsuite;
    }

    @Test
    /*Test for the Comparison service with a listwise comparison that uses the thread pool*/
    public void testPairwiseThreadPoolDoesNotChangeResult() throws Exception {
        PairingService p = new PairingService();
        assertEquals(Double.parseDouble(comparisonService.pairwiseCompare(p.makePairs(buildTestSuite().toArray(new DataRepresentation[3])),
                new CommonElements(), aggregationStrategy, null, true)),
                Double.parseDouble(comparisonService.pairwiseCompare(p.makePairs(buildTestSuite().toArray(new DataRepresentation[3])),
                        new CommonElements(), aggregationStrategy, null, false)),
                TOLERANCE);
    }

    @Test
    /*Test for the Comparison service with a listwise comparison that uses the thread pool*/
    public void testListwiseThreadPoolDoesNotChangeResult() throws Exception {
        List<List<DataRepresentation>> testsuites1 = new ArrayList<>();
        List<List<DataRepresentation>> testsuites2 = new ArrayList<>();
        testsuites1.add(buildTestSuite());
        testsuites2.add(buildTestSuite());

        assertEquals(Double.parseDouble(comparisonService.listwiseCompare(testsuites1, new ShannonIndex(), aggregationStrategy, null, true)),
                Double.parseDouble(comparisonService.listwiseCompare(testsuites2, new ShannonIndex(), aggregationStrategy, null, false)),
        assertEquals(1.92,
                Double.parseDouble(comparisonService.compareTestCase(testsuites, new ShannonIndex(), aggregationStrategy, null)),
                TOLERANCE);
    }
}