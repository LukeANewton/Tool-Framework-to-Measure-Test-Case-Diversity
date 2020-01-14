package data_representation;

import core.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/*
constructor with correct format
with incorrect format catching an InvalidFormatException
parse

 */

public class CSVTest {

    DataRepresentation csv;
    private final int MIN = 0;
    private final int MAX = 20;
    private final int NUM_VALUES = 10;

    @Before
    public void setUp() {
        
        StringBuilder values = new StringBuilder();
        for (int i = 0; i < NUM_VALUES; i++) {
            values.append(ThreadLocalRandom.current().nextInt(MIN, MAX + 1));
            values.append(',');
        }
        try {
            csv = new CSV(values.toString());
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = InvalidFormatException.class)
    public void testInvalidFormatConstructor() throws InvalidFormatException {
        new CSV("");
    }

    @Test
    public void hasNext() {
        assertTrue(csv.hasNext());
    }

    @Test
    public void next() {
        assertEquals("First element was not obtained","1", csv.next());
    }

    @Test
    public void parse() {
    }

    @Test
    public void checkFormat() {
    }
}