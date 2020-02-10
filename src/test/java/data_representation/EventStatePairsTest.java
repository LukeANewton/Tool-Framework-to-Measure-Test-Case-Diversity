package data_representation;

import core.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventStatePairsTest {
    private DataRepresentation d;
    // Number of events in the testcase
    private final int NUM_VALUES = 5;
    private final String testcase = "[43] Start-1(0)-OffProtected-4-StoppedProtected-13-PlayingProtected-27-PausedProtected-56-xxxPlayingProtected";
    private final String[] eventsFromTestCase = new String[]{"(1(0),OffProtected)", "(4,StoppedProtected)", "(13,PlayingProtected)",
            "(27,PausedProtected)", "(56,xxxPlayingProtected)"};

    @Before
    public void setUp() throws InvalidFormatException {
        d = new EventStatePairs(testcase);
    }

    @Test
    /*Test that the constructor instantiates an object when given a correctly formatted string*/
    public void testArgsConstructor() throws InvalidFormatException {
        new EventStatePairs(testcase);
    }

    @Test
    /*Test that the constructor instantiates an object correctly with no arguments*/
    public void testNoArgsConstructor()  {
        EventStatePairs e = new EventStatePairs();
        assertNull(e.next());
    }

    @Test
    /*test that a sequence with or without an identifier is stored the same*/
    public void testOptionalIdentifier() throws InvalidFormatException {
        EventStatePairs withID = new EventStatePairs(testcase);
        EventStatePairs withoutID = new EventStatePairs(testcase.substring(5));
        assertTrue(d.checkFormat(testcase.substring(5)));
        assertEquals(withID.toString(), "[43] " + withoutID.toString());
    }

    /*Test that the constructor throws an InvalidFormatException when given an incorrectly formatted string*/
    @Test(expected = InvalidFormatException.class)
    public void testInvalidFormatConstructor() throws InvalidFormatException {
        new EventStatePairs("");
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
        assertTrue(d.checkFormat("Start-6-7"));
        d.parse("Start-6-7");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseOnlyIdentifier() throws InvalidFormatException {
        assertFalse(d.checkFormat("[7]"));
        d.parse("[7]");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseNoStartState() throws InvalidFormatException {
        assertFalse(d.checkFormat("6-7-8-9"));
        d.parse("6-7-8-9");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseNoStates() throws InvalidFormatException {
        assertFalse(d.checkFormat("Start-"));
        d.parse("Start-");
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseNoFollowingState() throws InvalidFormatException {
        assertFalse(d.checkFormat("Start-6"));
        d.parse("Start-6");
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
        assertFalse(d.checkFormat(badID));
        d.parse(badID);
    }

    @Test(expected=InvalidFormatException.class)
    /*Tests that the parse() correctly throws an InvalidFormatException when given an invalid string*/
    public void testParseWithNonNumberIdentifier() throws InvalidFormatException {
        String badID = "[b] Start-1(0)-OffProtected-4-StoppedProtected-13-PlayingProtected-27-PausedProtected-56-xxxPlayingProtected";
        assertFalse(d.checkFormat(badID));
        d.parse(badID);
    }

    @Test
    /*Test that the format comes back correct*/
    public void testCheckCorrectFormat() {
        assertTrue(d.checkFormat(testcase));
    }

    @Test
    /*Test that the format comes back incorrect*/
    public void testCheckIncorrectFormat() {
        assertFalse(d.checkFormat(""));
        assertFalse(d.checkFormat(null));
    }

    @Test
    /*test that the getDescription method does return a description of the method*/
    public void testGetDescription() {
        assertEquals(d.getDescription(),
                "reads in a dash-separated list of alternating events and states, storing each event and resulting state as a tuple");
    }
}