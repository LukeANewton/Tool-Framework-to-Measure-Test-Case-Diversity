package core;

import model.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ControllerCompareTest {
    private Controller c;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Config originalConfigFile;
    private FileReaderService reader;
    private FileWriterService writer;
    private Config config;
    private final String configName = "config.json";
    private final String commandHelpString = "\tcompare <filename> [<filename>] <data-representation>\n" +
                "\t\tperforms a diversity calculation within a test suite, or between test suites at the specified filename(s)\n"+
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
        String expected = "The keyword 'banana' is not recognized. Valid commands are:\r\n" + commandHelpString;
        compareAgainstString("banana apple orange", expected);
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

    public void testCompareSinglePair() throws IOException {
        String testOutputName = "test-out";
        String dataFileName = "test-data";
        String delimiter = "\r\n";
        String contents = "1,2,3,4,5,6" + delimiter + "1,2,3,5,4,6";
        writer.write(dataFileName, contents, false, false);

        c.processCommand("compare " + dataFileName + " CSV -m CommonElements -a AverageValue -s " + testOutputName);
        assertTrue(new File(testOutputName).exists());

        new File(testOutputName).delete();
        new File(dataFileName).delete();
    }
}