package metrics.comparison.pairwise;

import data_representation.CSV;
import metrics.comparison.pairwise.LongestCommonSubstring;
import metrics.comparison.pairwise.PairwiseComparisonStrategy;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LongestCommonSubstringTest {
    private PairwiseComparisonStrategy metric;
    private final double THRESHOLD = 0;

    @Before
    public void setUp() {
        metric = new LongestCommonSubstring();
    }

    @Test
    public void testCompareStart() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "1,2,6,4,2,7";
        assertEquals(2,  metric.compare(new CSV(testCase1), new CSV(testCase2)), THRESHOLD); //check the value is as expected
    }

    @Test
    public void testCompareOperandOrder() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "1,2,6,4,2,7";

        assertEquals( metric.compare(new CSV(testCase1), new CSV(testCase2))
                ,metric.compare(new CSV(testCase2), new CSV(testCase1)), THRESHOLD);
    }

    @Test
    public void testCompareEnd() throws Exception {
        String testCase1 = "1,2,3,4,5,6,4,5,6,7";
        String testCase2 = "1,2,6,4,2,7,4,5,6,7";
        assertEquals(4,  metric.compare(new CSV(testCase1), new CSV(testCase2)), THRESHOLD); //check the value is as expected
    }

    @Test
    public void testCompareMiddle() throws Exception {
        String testCase1 = "1,2,3,4,87,34,6,2,5,6,4,5,6,7";
        String testCase2 = "1,2,6,4,87,34,6,2,7,4,5,6,7";
        assertEquals(5,  metric.compare(new CSV(testCase1), new CSV(testCase2)), THRESHOLD); //check the value is as expected
    }

    @Test
    public void testGetDescription() {
        assertEquals("returns a value indicating the longest common sequence of contiguous elements in a pair of test cases",
                metric.getDescription());
    }
}