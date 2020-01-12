package core;

import data_representation.CSV;
import data_representation.DataRepresentation;
import javafx.util.Pair;
import metrics.aggregation.AggregationStrategy;
import metrics.aggregation.AverageValue;
import metrics.comparison.CommonElements;
import metrics.comparison.PairwiseComparisonStrategy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Test suite that tests the comparison service
 */
public class ComparisonServiceTest {

    private Set<Pair<DataRepresentation, DataRepresentation>> differentTestCasePairs, sameTestCasePairs, halfSimilarPairs;
    private PairwiseComparisonStrategy strategy;
    private AggregationStrategy aggregationStrategy;
    private ComparisonService comparisonService;
    private final double TOLERANCE = 0.01;

    @Before
    public void setup() {
        differentTestCasePairs = new HashSet<>();
        sameTestCasePairs = new HashSet<>();
        halfSimilarPairs = new HashSet<>();
        int dissimilarValue;
        int value = 0;
        try {
            for (int i = 0; i < 10; i++) {
                value = i;
                differentTestCasePairs.add(new Pair<>(new CSV(value + ""), new CSV(++value + "")));
                sameTestCasePairs.add(new Pair<>(new CSV(value + ""), new CSV(value + "")));
                dissimilarValue = (i < 5)? value : 99;
                halfSimilarPairs.add(new Pair<>(new CSV(value + ""), new CSV(dissimilarValue + "")));
            }
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

        strategy = new CommonElements();
        aggregationStrategy = new AverageValue();
        comparisonService = new ComparisonService(2);

    }

    @After
    public void clean() {
        try {
            comparisonService.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that the comparison service is comparing pairs correctly when given completely similar pairs.
     */
    @Test
    public void testCompareSimilarPairs() {
        try {
            assertEquals(1.0, comparisonService.compareTestCase(sameTestCasePairs, strategy, aggregationStrategy), TOLERANCE);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that the comparison service is comparing pairs correctly when given completely dissimilar pairs.
     */
    @Test
    public void testCompareDissimilarPairs() {
        try {
            assertEquals(0.0, comparisonService.compareTestCase(differentTestCasePairs, strategy, aggregationStrategy), TOLERANCE);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test that the comparison service is comparing pairs correctly when given half similar pairs.
     * This is a 0.0 value because the current aggregation strategy finds the minimum
     */
    @Test
    public void testCompareHalfSimilarPairs() {
        try {
            assertEquals(0.5, comparisonService.compareTestCase(halfSimilarPairs, strategy, aggregationStrategy), TOLERANCE);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}