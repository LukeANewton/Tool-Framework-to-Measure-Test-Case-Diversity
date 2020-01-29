package com.compare.metrics.comparison;

import com.compare.data_representation.CSV;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CommonElementsTest {
    private CommonElements metric;
    private static final double THRESHOLD = 0;

    @Before
    public void setUp(){
        metric = new CommonElements();
    }

    @Test
    /**test for the getDescription method, currently null*/
    public void testCommonElementsGetDescription() {
        assertNull("Description should be null but is: " + metric.getDescription(), metric.getDescription());
    }

    @Test
    /**Test the common elements when both test cases are equal*/
    public void testCommonElementsEqual() {
        String testCase = "1,2,3,4,5,6,7,8,9";
        try {
            double result = metric.compare(new CSV(testCase), new CSV(testCase));
            assertEquals("Result should be 9 but is: " + result,9, result,THRESHOLD);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**Test the common elements when test cases are completely different*/
    public void testCommonElementsNotEqual() {
        String testCase1 = "1,2,3,4,5";
        String testCase2 = "6,7,8,9,10";
        try {
            double result = metric.compare(new CSV(testCase1), new CSV(testCase2));
            assertEquals("Result should be 0 but is: " + result,0,result,THRESHOLD);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**Test the common elements between two lists with the same values in different order*/
    public void testCommonElements() {
        String testCase1 = "1,2,3,4,5,6";
        String testCase2 = "6,5,4,3,2,1";
        try {
            double result = metric.compare(new CSV(testCase1), new CSV(testCase2));
            assertEquals("Result should be 0 but is: " + result,0,result,THRESHOLD);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    /**Test the common elements between two lists with one list longer than the other*/
    public void testComparisonSwitchOperands() {
        String testCase1 = "1,2,3";
        String testCase2 = "1,2,3,4,5,6";
        try {
            double result = metric.compare(new CSV(testCase1), new CSV(testCase2));
            assertEquals("Result should be 3 but is: " + result,3,result,THRESHOLD);
        } catch (Exception e) {
            fail();
        }
    }
}
