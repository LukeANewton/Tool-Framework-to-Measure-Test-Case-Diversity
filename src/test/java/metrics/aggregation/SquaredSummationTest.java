package metrics.aggregation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SquaredSummationTest {
    private final double TOLERANCE = 0.01;
    private AggregationStrategy aggregationStrategy;

    @Before
    public void setup() {
        aggregationStrategy = new SquaredSummation();
    }

    /**
     * Tests that the aggregate is calculated correctly using an alternate average calculation method as the oracle.
     */
    @Test
    public void testAggregateValue() {
        List<Double> similarityValues = new ArrayList<>();
        // Populate array with random values
        Random r = new Random(123456);
        final int NUM_SIMILARITY_VALUES = 10;
        final double MIN_RANGE = -3.00;
        final double MAX_RANGE = 3.00;

        for (int i = 0; i < NUM_SIMILARITY_VALUES; i++) {
            similarityValues.add(MIN_RANGE + (MAX_RANGE - MIN_RANGE) * r.nextDouble());
        }
        // Attempt to calculate the value by other means. This will be our oracle.
        double sum = 0;
        for (Double value : similarityValues) {
            sum += Math.pow(value, 2);
        }
        assertEquals(sum, Double.parseDouble(aggregationStrategy.aggregate(similarityValues)), TOLERANCE);
    }

    /**
     * Tests that the aggregate is calculated correctly using predefined values.
     * Variant: Aggregating one value
     */
    @Test
    public void testAggregateOneValue() {
        List<Double> similarityValues = new ArrayList<>();
        similarityValues.add(-3.0);
        assertEquals(9.0, Double.parseDouble(aggregationStrategy.aggregate(similarityValues)), TOLERANCE);
    }

    /**
     * Tests that the aggregate is calculated correctly using predefined values.
     * Variant: Result is zero
     */
    @Test
    public void testZeroAggregateValue() {
        List<Double> similarityValues = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0));
        assertEquals(0.0, Double.parseDouble(aggregationStrategy.aggregate(similarityValues)), TOLERANCE);
    }

    /**
     * Tests that the aggregate is calculated correctly using predefined values.
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
