package metrics.comparison.pairwise;

import data_representation.CSV;
import metrics.comparison.pairwise.Levenshtein;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LevenshteinTest {
    private Levenshtein metric;
    private static final double THRESHOLD = 0.01;

    @Before
    public void setUp(){
        metric = new Levenshtein();
    }

    @Test
    /**test for the getDescription method*/
    public void testGetDescription() {
        assertEquals(metric.getDescription(),
                "Calculates the minimum number of insertion/deletion/modification operations to transform one test case into another");
    }

    @Test
    /**Test the levenshtein distance when both test cases are equal
     * The resulting value should zero
     */
    public void testComparisonEqual() {
        String testCase = "1,2,3,4,5,6,7,8";
        try {
            assertEquals(0, metric.compare(new CSV(testCase), new CSV(testCase)), THRESHOLD);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**Test the levenshtein distance when test cases are different*/
    public void testComparisonNotEqual() {
        String testCase1 = "8,7,6,5,4,3,2,1";
        String testCase2 = "1,2,3,4,5,6,7,8";
        try {
            assertEquals(7, metric.compare(new CSV(testCase1), new CSV(testCase2)), THRESHOLD);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**Test for levenshtein distance that check the the order of operands does not matter*/
    public void testComparisonSwitchOperands() {
        String testCase1 = "8,7,6,5,4,3,2,1";
        String testCase2 = "1,2,3,4,5,6,7,8";
        try {
            assertEquals(metric.compare(new CSV(testCase2), new CSV(testCase1)), metric.compare(new CSV(testCase1), new CSV(testCase2)), THRESHOLD);
        } catch (Exception e) {
            fail();
        }
    }
}