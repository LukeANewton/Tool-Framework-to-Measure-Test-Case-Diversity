package metrics.aggregation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ManhattanTest {
    private final double TOLERANCE = 0.01;
    private AggregationStrategy aggregationStrategy;

    @Before
    public void setup() {
        aggregationStrategy = new Manhattan();
    }

    /**
     * Tests that the manhattan is calculated correctly using an alternate average calculation method as the oracle.
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
            sum += Math.abs(value);
        }
        assertEquals(sum, aggregationStrategy.aggregate(similarityValues).get(0), TOLERANCE);
    }

    /**
     * Tests that the manhattan is calculated correctly using predefined values.
     * Variant: Aggregating one value
     */
    @Test
    public void testManhattanOneValue() {
        List<Double> similarityValues = new ArrayList<>();
        similarityValues.add(-3.0);
        assertEquals(3.0, aggregationStrategy.aggregate(similarityValues).get(0), TOLERANCE);
    }

    /**
     * Tests that the manhattan is calculated correctly using predefined values.
     * Variant: Result is zero
     */
    @Test
    public void testZeroManhattanValue() {
        List<Double> similarityValues = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.0, 0.0));
        assertEquals(0.0, aggregationStrategy.aggregate(similarityValues).get(0), TOLERANCE);
    }

    /**
     * Tests that the manhattan is calculated correctly using predefined values.
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
