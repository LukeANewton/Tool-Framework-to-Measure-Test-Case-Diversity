package core;

import model.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class ControllerTest {
    private Controller c;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private final String configName = "config.json";
    private final String commandHelpString = "\tcompare <filename> [<filename>] <data-representation>\n" +
                "\t\tperforms a diversity calculation within a test suite, or between test suites at the specified filenames(s)\n"+
                "\t\t\t-m <metric>: set the diversity metric to use in the calculation. Available metrics can be found with 'help -m'\n"+
                "\t\t\t-a <method>: set the method to use for aggregating results. Available methods can be found with 'help -a'\n"+
                "\t\t\t-d <delimiter>: set the delimiter that separates test cases within the passed test suite file(s). This can be a character, string, or regular expression\n"+
                "\t\t\t-s <filename>: denote that the results of the operation should be saved to a file named <filename>\n"+
                "\t\t\t-t [<integer>]: denote that the operation should use a thread pool for concurrency, and optionally specify the number of threads\n"+
                "\tconfig <parameter> <value>\n"+
                "\t\tsets the value of a parameter read from the configuration file\n"+
                "\thelp\n"+
                "\t\tlists information on the requested topic\n"+
                "\t\t\t-m: lists the available comparison metrics in the system\n"+
                "\t\t\t-a: lists the available aggregation methods in the system\n"+
                "\t\t\t-f: lists the available data representations in the system\n\r\n";

    @Before
    public void setUp(){
        c = Controller.getController();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown(){
        System.setOut(originalOut);
    }

    @Test
    /*Test for the creation of a controller when no config file is present*/
    public void testControllerCreationWithoutConfigFile() throws IOException {
        //store the config file for to later restore
        FileReaderService reader = new FileReaderService();
        FileWriterService writer = new FileWriterService("");
        Config config = reader.readConfig(configName);

        //remove config file
        File file = new File(configName);
        assertTrue(file.delete());

        //test the creation of a controller without a config file
        c = Controller.getController();
        assertNull(c);
        String expected = "Failed to read from configuration file: config.json. Ensure the file exists in the same directory as this program.\r\n";
        assertEquals(expected, outContent.toString());

        //restore config file
        writer.writeConfig(configName, config);
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

    @Test
    /*Test for the error handling of invalid command types*/
    public void testProcessInvalidCommand() {
        String expected = "The keyword 'banana' is not recognized. Valid commands are:\r\n" + commandHelpString;
        compareAgainstString("banana apple orange", expected);
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
        FileReaderService reader = new FileReaderService();
        FileWriterService writer = new FileWriterService("");
        Config config = reader.readConfig(configName);
        String oldLocation = config.getComparisonMethodLocation();
        String fakeDirectory = "%&$%&^#@";
        config.setComparisonMethodLocation(fakeDirectory);
        writer.writeConfig(configName, config);

        String expected = "Available PairwiseMetrics are:\n" +
                "\tNone available at specified directory: " + fakeDirectory + "\r\n";
        c = Controller.getController(); // need to get a new controller so it updates its internal config file
        compareAgainstString("help -m", expected);

        config.setComparisonMethodLocation(oldLocation);
        writer.writeConfig(configName, config);
    }
}