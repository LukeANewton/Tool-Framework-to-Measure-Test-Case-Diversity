package metrics.listwise;

import core.InvalidFormatException;
import data_representation.CSV;
import data_representation.DataRepresentation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SimpsonDiversityTest {
    ListwiseComparisonStrategy strategy;

    @Before
    public void setUp(){
        strategy = new SimpsonDiversity();
    }

    @Test
    /*test for the compare method on a small test suite*/
    public void testCompare() throws InvalidFormatException {
        //create a test suite
        List<DataRepresentation> testsuite = new ArrayList<>();
        testsuite.add(new CSV("1,2,3,4,5,6"));
        testsuite.add(new CSV("5,4,5,2,4,7"));
        testsuite.add(new CSV("1,1,1,4,5,8"));

        double result = strategy.compare(testsuite);
        double expected = 0.83; //hand-calculated value
        assertEquals(expected, result, 0.01);
    }

    @Test
    /*test for the compare method on a test suite that should yield the
    lowest possible diversity: 0*/
    public void testNonDiverse() throws InvalidFormatException {
        //create a test suite
        List<DataRepresentation> testsuite = new ArrayList<>();
        testsuite.add(new CSV("1"));
        testsuite.add(new CSV("1,1,1"));

        double result = strategy.compare(testsuite);
        assertEquals(0, result, 0.01);
    }

    @Test
    /*test for the compare method that ensures the expected relationship holds:
     * less diverse suites should have lower values for this metric*/
    public void testCompareRelationship() throws InvalidFormatException {
        //create a test suite
        List<DataRepresentation> testsuite = new ArrayList<>();
        testsuite.add(new CSV("1,2,3,4,5,6"));
        testsuite.add(new CSV("5,4,8,5,2,4,7"));
        testsuite.add(new CSV("1,2,3,4,5,6"));

        double result1 = strategy.compare(testsuite);

        testsuite = new ArrayList<>();
        testsuite.add(new CSV("6,2,6,4,5,6"));
        testsuite.add(new CSV("6,2,6,4,5,6"));
        testsuite.add(new CSV("6,2,3,4,6,6"));
        double result2 = strategy.compare(testsuite);

        assertTrue(result1 > result2);
    }

    @Test
    /*test for the compare method on an empty test suite should yield 1*/
    public void testCompareEmpty() {
        List<DataRepresentation> testsuite = new ArrayList<>();
        double result = strategy.compare(testsuite);
        assertEquals(1, result, 0.05);
    }

    @Test
    /*test for the getDescription method*/
    public void testGetDescription() {
        assertEquals("a value between 0 and 1 indicating diversity within a set. values closer to 1 are more diverse",
                strategy.getDescription());

    }
}