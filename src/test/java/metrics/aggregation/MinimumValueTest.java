package metrics.aggregation;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * test for the minimum value aggregation method
 *
 * @author luke
 */
public class MinimumValueTest {
    AggregationStrategy aggregationStrategy;

    @Before
    public void setUp() throws Exception {
        aggregationStrategy = new MinimumValue();
    }

    @Test
    /**
     * test the aggregation method. It should return the smallest number in a list.
     */
    public void aggregate() {
        Double smallest = 1.11111;

        List<Double> list = new ArrayList<>();
        list.add(5.0);
        list.add(6.7);
        list.add(10.0);
        list.add(smallest);
        list.add(3.14);
        list.add(2.72);
        list.add(42.0);

        assertEquals(aggregationStrategy.aggregate(list), smallest, 0.01);

    }

    @Test
    /**
     * test that the getDescription method does return a description of the method
     */
    public void getDescription() {
        assertEquals(aggregationStrategy.getDescription(),
                "Chooses the lowest similarity value to represent the overall similarity of the test cases compared.");
    }
}