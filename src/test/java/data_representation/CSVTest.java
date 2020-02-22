package data_representation;

import core.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * Test suite for the CSV DataRepresentation
 *
 * @author crushton
 */
public class CSVTest {

    private DataRepresentation csv;
    // The string of values given to the csv object simulating how it would be read from a file
    private StringBuilder values;
    // A list representation of the comma separated values used as an oracle
    private List<Integer> valuesList;
    // Range of values that can be populated in the list of values
    private final int MIN = 0;
    private final int MAX = 20;
    // Number of values in the list of comma separated values
    private final int NUM_VALUES = 10;

    /**
     * Set up the CSV object with a list of random values as a string.
     * @throws InvalidFormatException when the format of the list of values is incorrect.
     */
    @Before
    public void setUp() throws InvalidFormatException {
        // Values list that simulates a test case from a file
        values = new StringBuilder();
        // This is our oracle
        valuesList = new ArrayList<>();

        int randomValue;
        for (int i = 0; i < NUM_VALUES; i++) {
            randomValue = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
            valuesList.add(randomValue);
            values.append(randomValue);
            values.append(',');
        }
        csv = new CSV(values.toString());
    }

    /**
     * Test that the constructor instantiates an object when given a correctly formatted string.
     * @throws InvalidFormatException when given an incorrectly formatted string.
     */
    @Test
    public void testArgsConstructor() throws InvalidFormatException {
        new CSV(values.toString());
    }

    /**
     * Test that the constructor throws an InvalidFormatException when given an incorrectly formatted string.
     * @throws InvalidFormatException when given an incorrectly formatted string of comma separated values.
     */
    @Test(expected = InvalidFormatException.class)
    public void testInvalidFormatConstructor() throws InvalidFormatException {
        new CSV("");
    }

    /**
     * Test that the next value has been obtained.
     */
    @Test
    public void testHasNext() {
        assertTrue(csv.hasNext());
    }

    /**
     * Test that the values are separated correctly and returned by next() using random values and an oracle created at setup.
     */
    @Test
    public void testNext() {
        // Using all values except for the last because testHasNext() can occur after this test.
        int allValuesButLast = NUM_VALUES-1;
        for (int i = 0; i < allValuesButLast; i++) {
            assertEquals("First " + allValuesButLast + " not obtained", valuesList.get(i).toString(), csv.next());
        }
    }

    /**
     * Test that parse() completes successfully without throwing an exception.
     * @throws InvalidFormatException when the string format is not correct.
     */
    @Test
    public void testParse() throws InvalidFormatException {
        // Parse returns a string, but checking this value isn't required as there is no manipulation of the string
        csv.parse(values.toString());
    }

    /**
     * Tests that the parse() correctly throws an InvalidFormatException when given an invalid string.
     * @throws InvalidFormatException when the string format is not correct.
     */
    @Test(expected=InvalidFormatException.class)
    public void testParseWithException() throws InvalidFormatException {
        csv.parse("");
    }

    /**
     * test that the getDescription method does return a description of the method
     */
    @Test
    public void testGetDescription() {
        assertEquals(csv.getDescription(),
                "comma separated value");
    }
}