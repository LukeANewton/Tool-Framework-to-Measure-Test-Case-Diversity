package metrics.aggregation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the average value aggregation strategy
 */
public class AverageValueTest {

    private final double TOLERANCE = 0.01;
    private AggregationStrategy aggregationStrategy;

    @Before
    public void setup() {
        aggregationStrategy = new AverageValue();
    }

    /**
     * Tests that the average is calculated correctly using an alternate average calculation method as the oracle.
     */
    @Test
    public void testAverageValue() {
        List<Double> similarityValues = new ArrayList<>();
        // Populate array with random values
        Random r = new Random(123456);
        final int SIMILARITY_VALUES = 10;
        final double MIN_RANGE = 0.00;
        final double MAX_RANGE = 1.00;
        for (int i = 0; i < SIMILARITY_VALUES; i++) {
            similarityValues.add(MIN_RANGE + (MAX_RANGE - MIN_RANGE) * r.nextDouble());
        }

        // Attempt to calculate the average value by other means. This will be our oracle.
        double sum = 0;
        for (Double value : similarityValues) {
            sum += value;
        }
        double average = sum/ SIMILARITY_VALUES;
        assertEquals(average, aggregationStrategy.aggregate(similarityValues).get(0), TOLERANCE);
    }

    /**
     * Tests that the average is calculated correctly using predefined values.
     * Variant: Result is 0.5
     */
    @Test
    public void testHalfAverageValue() {
        List<Double> similarityValues = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.25, 0.5, 0.75, 1.0, 1.0));
        assertEquals(0.5, aggregationStrategy.aggregate(similarityValues).get(0), TOLERANCE);
    }

    /**
     * Tests that the average is calculated correctly using predefined values.
     * Variant: Result is negative
     */
    @Test
    public void testNegativeAverageValue() {
        List<Double> similarityValues = new ArrayList<>(Arrays.asList(0.0, 0.0, 1.0, -1.0, -1.0));
        assertEquals(-0.2, aggregationStrategy.aggregate(similarityValues).get(0), TOLERANCE);
    }

    /**
     * Tests that the average is calculated correctly using predefined values.
     * Variant: List is empty
     */
    @Test(expected = NullPointerException.class)
    public void testEmptyList() {
        List<Double> similarityValues = new ArrayList<>();
        aggregationStrategy.aggregate(similarityValues);
    }

    /**
     * test that the getDescription method does return a description of the method
     */
    @Test
    public void testGetDescription() {
        assertNotNull(aggregationStrategy.getDescription());
    }
}
