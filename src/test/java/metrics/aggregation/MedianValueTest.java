package metrics.aggregation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MedianValueTest {
    private AggregationStrategy aggregationStrategy;

    @Before
    public void setUp() {
        aggregationStrategy = new MedianValue();
    }

    /**
     * Test the aggregation method. It should return the median number in a list after the strategy sorts it.
     */
    @Test
    public void aggregate() {
        Double median = 4.5;

        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(median);
        list.add(6.7);
        list.add(10.0);
        list.add(3.14);
        list.add(2.72);
        list.add(4.0);

        assertEquals(median, aggregationStrategy.aggregate(list).get(0));
    }

    /**
     * Test the aggregation method. It should return the median number in a list after the strategy sorts it.
     */
    @Test
    public void aggregateMultipleMedians() {
        double median1 = 4.5;
        double median2 = 4.82;

        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(median1);
        list.add(6.7);
        list.add(10.0);
        list.add(3.14);
        list.add(2.72);
        list.add(4.0);
        list.add(median2);

        List<Double> aggregation = aggregationStrategy.aggregate(list);

        assertEquals(2, aggregation.size());
        assert(aggregation.contains(median1));
        assert(aggregation.contains(median2));
    }

    /**
     * Test the aggregation method with one value. It should return the value.
     */
    @Test
    public void aggregateOneValue() {
        Double median = 4.5;

        List<Double> list = new ArrayList<>();
        list.add(median);

        List<Double> aggregation = aggregationStrategy.aggregate(list);

        assertEquals(1, aggregation.size());
        assertEquals(median, aggregation.get(0));
    }

    /**
     * Test the aggregation method with two value. It should return the value.
     */
    @Test
    public void aggregateTwoValue() {
        double median1 = 4.5;
        double median2 = 4.6;

        List<Double> list = new ArrayList<>();
        list.add(median1);
        list.add(median2);

        List<Double> aggregation = aggregationStrategy.aggregate(list);

        assertEquals(2, aggregation.size());
        assert(aggregation.contains(median1));
        assert(aggregation.contains(median2));
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
