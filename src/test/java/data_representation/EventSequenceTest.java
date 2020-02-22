package data_representation;

import core.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventSequenceTest {
    private DataRepresentation d;
    // Number of events in the testcase
    private final int NUM_VALUES = 5;
    private final String testcase = "[43] Start-1(0)-OffProtected-4-StoppedProtected-13-PlayingProtected-27-PausedProtected-56-xxxPlayingProtected";
    private final String[] eventsFromTestCase = new String[]{"1(0)", "4", "13", "27", "56"};

    @Before
    public void setUp() throws InvalidFormatException {
        d = new EventSequence(testcase);
    }

    @Test
    /*Test that the constructor instantiates an object when given a correctly formatted string*/
    public void testArgsConstructor() throws InvalidFormatException {
        new EventSequence(testcase);
    }

    @Test
    /*Test that the constructor instantiates an object correctly with no arguments*/
    public void testNoArgsConstructor()  {
        EventSequence e = new EventSequence();
        assertNull(e.next());
    }

    @Test
    /*test that a sequence with or without an identifier is stored the same*/
    public void testOptionalIdentifier() throws InvalidFormatException {
        EventSequence withID = new EventSequence(testcase);
        EventSequence withoutID = new EventSequence(testcase.substring(5));
        assertEquals(withID.toString(), "[43] " + withoutID.toString());
    }

    /*Test that the constructor throws an InvalidFormatException when given an incorrectly formatted string*/
    @Test(expected = InvalidFormatException.class)
    public void testInvalidFormatConstructor() throws InvalidFormatException {
        new EventSequence("");
    }

    @Test
    /*Test that the values are separated correctly and returned by next()*/
    public void testNext() {
        for (int i = 0; i < NUM_VALUES; i++) {
            assertTrue(d.hasNext());
            assertEquals(d.next().toString(), eventsFromTestCase[i]);
        }

        assertFalse(d.hasNext());
        assertNull(d.next());
    }

    @Test
    /*Test that parse() completes successfully without throwing an exception*/
    public void testParse() throws InvalidFormatException {
        d.parse(testcase);
    }

    @Test
    /*Test that parse() completes successfully without throwing an exception*/
    public void testShortParse() throws InvalidFormatException {
        d.parse("Start-6");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseOnlyIdentifier() throws InvalidFormatException {
        d.parse("[7]");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseNoStartState() throws InvalidFormatException {
        d.parse("6-7-8-9");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseNoStates() throws InvalidFormatException {
        d.parse("Start-");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseNull() throws InvalidFormatException {
        d.parse(null);
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseWithBadIdentifier() throws InvalidFormatException {
        String badID = "[] Start-1(0)-OffProtected-4-StoppedProtected-13-PlayingProtected-27-PausedProtected-56-xxxPlayingProtected";
        d.parse(badID);
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseWithNonNumberIdentifier() throws InvalidFormatException {
        String badID = "[b] Start-1(0)-OffProtected-4-StoppedProtected-13-PlayingProtected-27-PausedProtected-56-xxxPlayingProtected";
        d.parse(badID);
    }

    @Test
    /*test that the getDescription method does return a description of the method*/
    public void testGetDescription() {
        assertEquals(d.getDescription(),
                "reads in a dash-separated list of alternating events and states, but only stores the events");
    }
}