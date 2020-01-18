package metrics.listwise;

import core.FileWriterService;
import core.InvalidFormatException;
import data_representation.CSV;
import data_representation.DataRepresentation;
import metrics.comparison.TestCaseFormatMismatchException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShannonIndexTest {
    ListwiseComparisonStrategy strategy;

    @Before
    public void setUp(){
        strategy = new ShannonIndex();
    }

    @Test
    /**test for the compare method on a small test suite*/
    public void testCompare() throws InvalidFormatException, TestCaseFormatMismatchException {
        //create a test suite
        DataRepresentation[] testsuite = {new CSV("1,2,3,4,5,6"),
                                          new CSV("5,4,8,5,2,4,7"),
                                          new CSV("1,1,1,4,5,8")};

        double result = strategy.compare(testsuite);
        double expected = 2.512; //hand-calculated value
        assertEquals(expected, result, 0.05);
    }

    @Test
    /**test for the comapre method on an empty test suite should yield 0*/
    public void testCompareEmpty() throws TestCaseFormatMismatchException {
        DataRepresentation[] testsuite = {};
        double result = strategy.compare(testsuite);
        assertEquals(0, result, 0.05);
    }

    @Test
    /**test for the getDescription method*/
    public void testGetDescription() {
        assertEquals("A measure of the entropy for sets of sequences. " +
                "Commonly used to measure diversity within a population in life sciences",
                strategy.getDescription());

    }
}