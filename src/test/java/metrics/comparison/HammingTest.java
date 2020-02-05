package metrics.comparison;

import data_representation.CSV;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HammingTest {
    private PairwiseComparisonStrategy metric;
    private static final double THRESHOLD = 0;

    @Before
    public void setUp(){
        metric = new Hamming();
    }

    @Test
    /*test for the getDescription method*/
    public void testGetDescription() {
        assertEquals("compares two test cases by the number of positions in the test cases that are different", metric.getDescription());
    }

    @Test
    /*Test the comparison metrics behaves correctly, counting the number of different elements in each position*/
    public void testHamming() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "1,3,6,4,2,7";

        double result = metric.compare(new CSV(testCase1), new CSV(testCase2));
        assertEquals(4, result, THRESHOLD); //check the value is as expected
        assertEquals(result, metric.compare(new CSV(testCase2), new CSV(testCase1)), THRESHOLD);//check the order does of operands does not matter
    }

    @Test
    /*Test the comparison metrics works as expected for cases of a different size*/
    public void testHammingUnequalSize() throws Exception {
        String testCase1 = "1,2,3,4,5,6,2,7,2";
        String testCase2 = "1,3,6,4,2,7";

        double result = metric.compare(new CSV(testCase1), new CSV(testCase2));
        assertEquals(4, result, THRESHOLD); //check the value is as expected
        assertEquals(result, metric.compare(new CSV(testCase2), new CSV(testCase1)), THRESHOLD);//check the order does of operands does not matter
    }
}
