package metrics.aggregation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the aggregation strategy that chooses the most frequent similarity value to represent the overall similarity of the test cases.
 *
 * @author crushton
 */
public class ModeValueTest {
    private AggregationStrategy aggregationStrategy;

    @Before
    public void setUp() {
        aggregationStrategy = new ModeValue();
    }

    /**
     * Test the aggregation method. It should return the most frequent number in a list.
     */
    @Test
    public void aggregate() {
        double mode = 17.11;

        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(mode);
        list.add(10.0);
        list.add(mode);
        list.add(3.14);
        list.add(2.72);
        list.add(4.0);

        assertEquals(Arrays.toString(new double[]{mode}), aggregationStrategy.aggregate(list));
    }

    /**
     * Test the aggregation method. It should return the most frequent numbers in a list.
     */
    @Test
    public void aggregateMultipleModes() {
        double mode1 = 17.11;
        double mode2 = 1.0;

        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(mode1);
        list.add(10.0);
        list.add(mode1);
        list.add(3.14);
        list.add(2.72);
        list.add(4.0);
        list.add(mode2);
        list.add(mode2);

        assertEquals(Arrays.toString(new double[]{mode2, mode1}), aggregationStrategy.aggregate(list));
    }

    /**
     * Test the aggregation method when passed an empty list. It should throw a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void aggregateEmptyList() {
        List<Double> list = new ArrayList<>();
        aggregationStrategy.aggregate(list);
    }

    /**
     * test that the getDescription method does return a description of the method
     */
    @Test
    public void getDescription() {
        assertNotNull(aggregationStrategy.getDescription());
    }
}
