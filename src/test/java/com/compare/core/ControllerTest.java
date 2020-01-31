package com.compare.core;

import com.compare.model.Config;
import com.compare.user_interface.Console;
import com.compare.user_interface.InputParser;
import com.compare.user_interface.InvalidCommandException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ControllerTest {
    // Dont mock the controller because this is what we want to use.
    private Controller c;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Config originalConfigFile;
    @Mock
    private FileReaderService reader;
    @Mock
    private FileWriterService writer;
    @Mock
    private Config config;
    @Mock
    private Console console;
    @Mock
    private InputParser parser;
    @Mock
    private PairingService pairingService;
    @Mock
    private ComparisonService comparisonService;


    private final String configName = "config.json";
    private final String commandHelpString = "\tcompare <filename> [<filename>] <data-representation>\n" +
                "\t\tperforms a diversity calculation within a test suite, or between test suites at the specified filename(s)\n"+
                "\t\t\t-m <metric>: set the diversity metric to use in the calculation. Available com.compare.metrics can be found with 'help -m'\n"+
                "\t\t\t-a <method>: set the method to use for aggregating results. Available methods can be found with 'help -a'\n"+
                "\t\t\t-d <delimiter>: set the delimiter that separates test cases within the passed test suite file(s). This can be a character, string, or regular expression\n"+
                "\t\t\t-s <filename>: denote that the results of the operation should be saved to a file named <filename>\n"+
                "\t\t\t-t [<integer>]: denote that the operation should use a thread pool for concurrency, and optionally specify the number of threads\n"+
                "\tconfig <parameter> <value>\n"+
                "\t\tsets the value of a parameter read from the configuration file\n"+
                "\thelp\n"+
                "\t\tlists information on the requested topic\n"+
                "\t\t\t-m: lists the available comparison com.compare.metrics in the system\n"+
                "\t\t\t-a: lists the available aggregation methods in the system\n"+
                "\t\t\t-f: lists the available data representations in the system\n\r\n";

    public ControllerTest() {
    }

    @Before
    public void setUp() {
        c = new Controller(console, parser, writer, reader, pairingService, comparisonService, config);
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        //store the config file for to later restore
//        config = reader.readConfig(configName);
//        originalConfigFile = reader.readConfig(configName);
    }

    @After
    public void tearDown() throws IOException {
        System.setOut(originalOut);
//        writer.writeConfig(configName, originalConfigFile);
    }

    @Test
    /*Test for the error handling of invalid command types*/
    public void testProcessInvalidCommand() throws InvalidCommandException {
        String command = "banana apple orange";
        String expected = "The keyword 'banana' is not recognized. Valid commands are:\r\n" + commandHelpString;
        when(parser.parse(command)).thenThrow(InvalidCommandException.class);
        compareAgainstString(command, expected);
    }

    @Test
    /*Test for processing command help*/
    public void testProcessCommandHelp() {
        compareAgainstString("help", commandHelpString);
    }

    @Test
    /*test for parsing aggregation method help*/
    public void testAggregationHelp() {
        String expected = "Available AggregationMethods are:\n" +
                "\tAverageValue:\n" +
                "\t\tChooses the average similarity value to represent the overall similarity of the test cases compared.\n" +
                "\tMinimumValue:\n" +
                "\t\tChooses the lowest similarity value to represent the overall similarity of the test cases compared.\n" +
                "\r\n";
        compareAgainstString("help -a", expected);
    }

    @Test
    /*test for parsing comparison metric help*/
    public void testPairwiseHelp() {
        String expected = "Available PairwiseMetrics are:\n" +
                "\tCommonElements:\n" +
                "\t\tno description available\n" +
                "\tJaccardIndex:\n" +
                "\t\tno description available\n" +
                "\tLevenshtein:\n" +
                "\t\tCalculates the minimum number of insertion/deletion/modification operations to transform one test case into another\n" +
                "\r\n";
        compareAgainstString("help -m", expected);
    }

    @Test
    /*test for parsing data representation help*/
    public void testDataRepresentationHelp() {
        String expected = "Available DataRepresentations are:\n" +
                "\tCSV:\n" +
                "\t\tcomma separated value\n" +
                "\r\n";
        compareAgainstString("help -f", expected);
    }

    @Test
    /*test for parsing an invalid help with extra tokens*/
    public void testHelpExtraTokens() {
        String expected = "Unexpected additional tokens: [-m] Valid commands are:\r\n" + commandHelpString;
        compareAgainstString("help -f -m", expected);
    }

    @Test
    /*test for parsing an invalid help with invalid type*/
    public void testHelpInvalidType() {
        String expected = "Help type not valid: -x Valid commands are:\r\n" + commandHelpString;
        compareAgainstString("help -x", expected);
    }

    @Test
    /*test for a help command were there are no metrics available. One way to do this should
    * be to set the config file metric location to a non-existent directory*/
    public void testHelpNoneAvailable() throws IOException {
        String fakeDirectory = "%&$%&^#@";
        config.setComparisonMethodLocation(fakeDirectory);
        writer.writeConfig(configName, config);

        String expected = "Available PairwiseMetrics are:\n" +
                "\tNone available at specified directory: " + fakeDirectory + "\r\n";
        compareAgainstString("help -m", expected);
    }

    @Test
    /*use a config command to set the string for the delimiter property*/
    public void testProcessStringCommandConfig() throws IOException {
        config.setDelimiter("banana");
        writer.writeConfig(configName, config);
        config = reader.readConfig(configName);
        assertEquals("Error setting config values for the test","banana", config.getDelimiter());

        c.processCommand("config delimiter apple");
        assertEquals("Successfully set delimiter to the value apple\r\n", outContent.toString());

        config = reader.readConfig(configName);
        assertEquals("Delimiter should be set to apple but is: " + config.getDelimiter(),"apple", config.getDelimiter());
    }

    @Test
    /*Use a config command to set the int for the numThreads property*/
    public void testProcessIntCommandConfig() throws IOException {
        config.setNumThreads(0);
        writer.writeConfig(configName, config);
        config = reader.readConfig(configName);
        assertEquals("Error setting config values for the test",0, config.getNumThreads());

        c.processCommand("config numThreads 5");
        assertEquals("Successfully set numThreads to the value 5\r\n", outContent.toString());

        config = reader.readConfig(configName);
        assertEquals("NumThreads should be set to 5 but is: " + config.getNumThreads(),5, config.getNumThreads());
    }

    @Test
    /*Use a config command to set the int for the numThreads property too a string*/
    public void testProcessStringToIntParamCommandConfig() {
        c.processCommand("config numThreads apple");
        assertEquals("Failed to set numThreads to apple. The value for numThreads should be a number\r\n", outContent.toString());
    }

    @Test
    /*Use a config command with an invalid property*/
    public void testProcessInvalidCommandConfig() {
        c.processCommand("config banana banana");
        assertEquals("The parameter banana is not valid\r\n", outContent.toString());
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