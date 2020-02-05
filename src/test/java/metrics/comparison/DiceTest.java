package metrics.comparison;

import data_representation.CSV;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DiceTest {
    private PairwiseComparisonStrategy metric;
    private final double THRESHOLD = 0;

    @Before
    public void setUp() {
        metric = new Dice();
    }

    @Test
    public void testCompareSame() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        assertEquals(1,  metric.compare(new CSV(testCase1), new CSV(testCase1)), THRESHOLD); //check the value is as expected
    }

    @Test
    public void testCompareSimilar() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "4,56,6,2,4";
        assertEquals(0.6,  metric.compare(new CSV(testCase1), new CSV(testCase2)), THRESHOLD); //check the value is as expected
    }

    @Test
    public void testCompareDistinct() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "7,8,9";
        assertEquals(0,  metric.compare(new CSV(testCase1), new CSV(testCase2)), THRESHOLD); //check the value is as expected
    }

    @Test
    public void testCompareOperandOrder() throws Exception {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "14,2,6,8,2,7";

        assertEquals( metric.compare(new CSV(testCase1), new CSV(testCase2)) ,metric.compare(new CSV(testCase2), new CSV(testCase1)), THRESHOLD);
    }

    @Test
    public void getDescription() {
        assertEquals("a measure of overlap between two sets, with 0 being distinct and 1 being complete equality",
                metric.getDescription());
    }
}