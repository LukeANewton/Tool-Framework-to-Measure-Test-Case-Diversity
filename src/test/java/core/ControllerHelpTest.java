package core;

import model.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ControllerHelpTest {
    private Controller c;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Config originalConfigFile;
    private FileReaderService reader;
    private FileWriterService writer;
    private Config config;
    private final String configName = "config.json";
    private final String commandHelpString = "\tcompare <filename> [<filename>] <data-representation>" + System.lineSeparator() +
            "\t\tperforms a diversity calculation within a test suite, or between test suites at the specified filename(s)" + System.lineSeparator() +
            "\t\t\t-m <metric>: set the diversity metric to use in the calculation. Available metrics can be found with 'help -m'" + System.lineSeparator() +
            "\t\t\t-a <method>: set the method to use for aggregating results. Available methods can be found with 'help -a'" + System.lineSeparator() +
            "\t\t\t-r [<format>]: set the report formats to use to display results. Available formats can be found with 'help -r'" + System.lineSeparator() +
            "\t\t\t-d <delimiter>: set the delimiter that separates test cases within the passed test suite file(s). This can be a character, string, or regular expression" + System.lineSeparator() +
            "\t\t\t-s <filename>: denote that the results of the operation should be saved to a file named <filename>" + System.lineSeparator() +
            "\t\t\t-t [<integer>]: denote that the operation should use a thread pool for concurrency, and optionally specify the number of threads" + System.lineSeparator() +
            "\tconfig <parameter> <value>" + System.lineSeparator() +
            "\t\tsets the value of a parameter read from the configuration file" + System.lineSeparator() +
            "\thelp" + System.lineSeparator() +
            "\t\tlists information on the requested topic" + System.lineSeparator() +
            "\t\t\t-m: lists the available comparison metrics in the system" + System.lineSeparator() +
            "\t\t\t-a: lists the available aggregation methods in the system" + System.lineSeparator() +
            "\t\t\t-f: lists the available data representations in the system" + System.lineSeparator() +
            "\t\t\t-r: lists the available report formats in the system" + System.lineSeparator() + System.lineSeparator();

    @Before
    public void setUp() throws IOException {
        c = Controller.getController();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        //store the config file for to later restore
        reader = new FileReaderService();
        writer = new FileWriterService();
        config = reader.readConfig(configName);
        originalConfigFile = reader.readConfig(configName);
    }

    @After
    public void tearDown() throws IOException {
        System.setOut(originalOut);
        writer.writeConfig(configName, originalConfigFile);
    }

    @Test
    /*Test for the error handling of invalid command types*/
    public void testProcessInvalidCommand() {
        String expected = "The keyword 'banana' is not recognized. Valid commands are:"  + System.lineSeparator() + commandHelpString;
        c.processCommand("banana apple orange");
        assertEquals(expected, outContent.toString());
    }

    @Test
    /*Test for processing command help*/
    public void testProcessCommandHelp() {
        compareAgainstString("help", commandHelpString);
    }

    @Test
    /*test for parsing aggregation method help*/
    public void testAggregationHelp() {
        c.processCommand("help -a");
        assertNotNull(outContent.toString());
    }

    /**
     * Test for parsing report format help
     */
    @Test
    public void testReportFormatHelp() {
        c.processCommand("help -r");
        assertNotNull(outContent.toString());
    }

    @Test
    /*test for parsing comparison metric help*/
    public void testPairwiseHelp() {
        c.processCommand("help -m");
        assertNotNull(outContent.toString());
    }

    @Test
    /*test for parsing data representation help*/
    public void testDataRepresentationHelp() {
        c.processCommand("help -f");
        assertNotNull(outContent.toString());
    }

    @Test
    /*test for parsing an invalid help with extra tokens*/
    public void testHelpExtraTokens() {
        String expected = "Unexpected additional tokens: [-m] Valid commands are:" + System.lineSeparator() + commandHelpString;
        compareAgainstString("help -f -m", expected);
    }

    @Test
    /*test for parsing an invalid help with invalid type*/
    public void testHelpInvalidType() {
        String expected = "Help type not valid: -x Valid commands are:" + System.lineSeparator() + commandHelpString;
        compareAgainstString("help -x", expected);
    }

    @Test
    /*test for a help command were there are no metrics available. One way to do this should
     * be to set the config file metric location to a non-existent directory*/
    public void testHelpNoneAvailable() throws IOException {
        String fakeDirectory = "%&$%&^#@";
        config.setPairwiseMethodLocation(fakeDirectory);
        writer.writeConfig(configName, config);
        c = Controller.getController(); // need to get a new controller so it updates its internal config file
        c.processCommand("help -m");
        assertTrue(outContent.toString().contains("None available at specified directory: " + fakeDirectory));
    }

    /**
     * helper method used to enter a system command and compare it against an expected string output
     *
     * @param command the command for the system to execute
     * @param expected the expected text output to System.out
     */
    private void compareAgainstString(String command, String expected){
        c.processCommand(command);
        assertEquals(expected, outContent.toString());
    }

}
