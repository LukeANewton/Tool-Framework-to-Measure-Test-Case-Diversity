package metrics.comparison;

import data_representation.CSV;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CompressionDistanceTest {
    private PairwiseComparisonStrategy metric;

    @Before
    public void setUp() {
        metric = new CompressionDistance();
    }

    @Test
    public void compare() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "1,2,6,4,2,7";
        assertTrue(metric.compare(new CSV(testCase1), new CSV(testCase2)) >
                metric.compare(new CSV(testCase1), new CSV(testCase1)));
    }

    @Test
    public void getDescription() {
        assertEquals("calculates the similarity of two test cases by the ratio of size between " +
                "the individual test cases compressed, and the compression of the test cases concatenated",
                metric.getDescription());
    }
}