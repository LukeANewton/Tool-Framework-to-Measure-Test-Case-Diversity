package metrics.aggregation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MaximumValueTest {
    private AggregationStrategy aggregationStrategy;

    @Before
    public void setUp() {
        aggregationStrategy = new MaximumValue();
    }

    /**
     * Test the aggregation method. It should return the largest number in a list.
     */
    @Test
    public void aggregate() {
        double largest = 17.11111;

        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(6.7);
        list.add(10.0);
        list.add(largest);
        list.add(3.14);
        list.add(2.72);
        list.add(4.0);

        assertEquals(largest, aggregationStrategy.aggregate(list).get(0), 0.01);
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
