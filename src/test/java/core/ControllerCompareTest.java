package core;

import model.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class ControllerCompareTest {
    private Controller c;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Config originalConfigFile;
    private FileReaderService reader;
    private FileWriterService writer;
    private Config config;
    private final double TOLERANCE = 0.001;
    private final String CONFIG_NAME = "config.json";
    private final String COMMAND_HELP_STRING = "\tcompare <filename> [<filename>] <data-representation>\n" +
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
    private final String singlePairTestSuiteName = "single-pair-suite";
    private final String singleCaseTestSuiteName = "single-test-case";
    private final String sampleTestSuiteA = "sample-suite-A";
    private final String sampleTestSuiteB = "sample-suite-b";

    @Before
    public void setUp() throws IOException {
        c = Controller.getController();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        //store the config file for to later restore
        reader = new FileReaderService();
        writer = new FileWriterService();
        config = reader.readConfig(CONFIG_NAME);
        originalConfigFile = reader.readConfig(CONFIG_NAME);

        //create test suite files for use in tests
        String contents = "1,2,3,4,5,6\r\n1,2,3,5,4,6";
        writeFile(singlePairTestSuiteName, contents);
        contents = "1,2,3,4,5,6";
        writeFile(singleCaseTestSuiteName, contents);
        contents = "1,2,3,4,5\r\n" +
                "1,2,3,4,6,7,3,4,6,7,3,4,5\r\n" +
                "1,2,3,4,6,8,9,3,4,6,8,9,3,4,5\r\n" +
                "1,2,3,4,6,7,3,4,6,8,9,3,4,5\r\n" +
                "1,2,3,4,6,7,3,4,6,8,9,3,10";
        writeFile(sampleTestSuiteA, contents);
        contents = "4,7,9,36,4,6,6,6,6\r\n" +
                "1,2,3,4,6,3,8,26,0,2,7,8,6,66\r\n" +
                "1,2,3,4,4,6,3,7,2,6,9\r\n" +
                "4,6,7,3,4,6,8,9,3,4,5";
        writeFile(sampleTestSuiteB, contents);
    }

    @After
    public void tearDown() throws IOException {
        System.setOut(originalOut);
        writer.writeConfig(CONFIG_NAME, originalConfigFile);
        deleteFiles(singlePairTestSuiteName, singleCaseTestSuiteName, sampleTestSuiteA, sampleTestSuiteB);
    }

    @Test
    /*Test for the error handling of invalid command types*/
    public void testProcessInvalidCommand() {
        String expected = "The keyword 'banana' is not recognized. Valid commands are:\r\n" + COMMAND_HELP_STRING;
        c.processCommand("banana apple orange");
        assertEquals(expected, outContent.toString());
    }

    @Test
    /*test for controller creation where there is no config file*/
    public void testControllerCreationWithoutConfigFile(){
        deleteFiles(CONFIG_NAME);
        Controller.getController();
        assertEquals("Failed to read from configuration file: " + CONFIG_NAME + ". Ensure the file exists in the same directory as this program.\r\n", outContent.toString());
    }

    /**
     * helper method to read a file into a string
     * @param filename the name of a file to read
     * @return the contents of the file
     */
    private String readFile(String filename) throws IOException {
        BufferedReader r = new BufferedReader(new FileReader(filename));
        String line;
        StringBuilder fileContent = new StringBuilder();
        while((line = r.readLine()) != null)
            fileContent.append(line);
        r.close();
        return fileContent.toString();
    }

    /**
     * helper method to write to a file
     * @param contents the contents of the file to write
     */
    private void writeFile(String filename, String contents) throws IOException {
        writer.write(filename, contents, false, false);
    }

    /**
     * helper function to run a command in the system
     * @param filename1 the name of the first test suite
     * @param filename2 the name of the optional second test suite
     * @param metric the name of the optional diversity metric
     * @param aggregation the name of the optional aggregation method
     * @param delimiter the delimiter between test cases
     * @param saveFileName the name of a file to save output to
     */
    private void doComparison(String filename1, String filename2, String metric, String aggregation,
                              String delimiter, String saveFileName, String numThreads){
        StringBuilder s = new StringBuilder();
        s.append("compare ").append(filename1);
        if(filename2 != null)
            s.append(" ").append(filename2);
        s.append(" CSV");
        if(metric != null)
            s.append(" -m ").append(metric);
        if(aggregation != null)
            s.append(" -a ").append(aggregation);
        if(delimiter != null)
            s.append(" -d ").append(delimiter);
        if(saveFileName != null)
            s.append(" -s ").append(saveFileName);
        if(numThreads != null)
            s.append(" -t ").append(numThreads);
        c.processCommand(s.toString());
    }

    /**
     * helper function to delete several files created during a test
     *
     * @param filenames the list of files to delete
     */
    private void deleteFiles(String ... filenames){
        for(String filename:filenames)
            new File(filename).delete();
    }

    /**
     * helper function to format an expected result in the way it is displayed on the console
     *
     * @param result the comparison result to format
     * @return the result formated to match console display
     */
    private String buildSinglePairConsoleOutput(String result){
        return "[==========]\r\nResult:\n\n" + result + "\r\n";
    }

    @Test
    /*test for the compare command that checks that files are correctly created*/
    public void testCompareSaveFileCreated() throws IOException {
        String testOutputName = "test-out";

        doComparison(singlePairTestSuiteName, null, "CommonElements", "AverageValue",
                null, testOutputName, null);

        //check that the file was correctly created
        assertTrue(new File(testOutputName).exists());

        //check that the file contents matches what is displayed to the console
        String result = readFile(testOutputName);
        assertEquals(outContent.toString(), buildSinglePairConsoleOutput(result));

        //check the actual value of the comparison (hand calculated)
        assertEquals(Double.parseDouble(result), 4.0, TOLERANCE);

        //clean up files created
        deleteFiles(testOutputName);
    }

    @Test
    /*test for the compare command that performs two pairwise metrics on a single pair of tests
    * with different aggregation methods. The output for each should be the same since there is
    * only one pair*/
    public void testCompareSinglePair() throws IOException {
        String outputName1 = "test-out1";
        String outputName2 = "test-out2";

        doComparison(singlePairTestSuiteName, null, "CommonElements", "AverageValue",
                null, outputName1, null);
        doComparison(singlePairTestSuiteName, null, "CommonElements", "MinimumValue",
                null, outputName2, null);

        //check that the results are the same for each file
        assertEquals(readFile(outputName1), readFile(outputName2));

        //clean up files created
        deleteFiles(outputName1, outputName2);
    }

    @Test
    /*test for the compare command with a non-default delimiter, the results
    should not change with different delimiters*/
    public void testDifferentDelimiters() throws IOException {
        String outputName1 = "test-out1";
        String outputName2 = "test-out2";
        String inputName1 = "test-data1";
        String inputName2 = "test-data2";

        String delimiter = "banana";
        String contents = "1,2,3,4,5,6" + delimiter + "1,2,3,5,4,6";
        writeFile(inputName1, contents);
        doComparison(inputName1, null, "CommonElements", "AverageValue",
                delimiter, outputName1, null);

        delimiter = "\r\n";
        contents = "1,2,3,4,5,6" + delimiter + "1,2,3,5,4,6";
        writeFile(inputName2, contents);
        doComparison(inputName2, null, "CommonElements", "MinimumValue",
                null, outputName2, null);

        //check that the results are the same for each file
        assertEquals(readFile(outputName1), readFile(outputName2));

        //clean up files created
        deleteFiles(inputName1, inputName2, outputName1, outputName2);
    }

    @Test
    /*test for the compare command using all default values*/
    public void testCompareAllDefaults() throws IOException {
        //set up a config file here that includes defaults
        config.setDataRepresentationLocation("data_representation");
        config.setComparisonMethod("JaccardIndex");
        config.setComparisonMethodLocation("metrics.comparison");
        config.setAggregationMethod("AverageValue");
        config.setAggregationMethodLocation("metrics.aggregation");
        config.setDelimiter("\r\n");
        writer.writeConfig(CONFIG_NAME, config);

        c = Controller.getController(); //getting a new controller is required because the config file is read in during creation
        doComparison(singlePairTestSuiteName, null, null, null,
                null, null, null);

        //check the value of the resulting comparison. The singlePairTestSuite JaccardIndex should be 1
        assertEquals(buildSinglePairConsoleOutput("1.0"), outContent.toString());
    }

    @Test
    /*test for the compare command with an invalid pairwise metric name*/
    public void testCompareInvalidMetric() {
        doComparison(singlePairTestSuiteName, null, "banana", null,
                null, null, null);
        assertEquals("no pairwise metric named banana in metrics.comparison. found\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command with an invalid aggregation method name*/
    public void testCompareInvalidAggregation() {
        doComparison(singlePairTestSuiteName, null, null, "apple",
                null, null, null);
        assertEquals("no aggregation method named apple in metrics.aggregation. found\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command with an invalid data representation name*/
    public void testCompareInvalidDataRepresentation() {
        c.processCommand("compare " + singlePairTestSuiteName + " banana");
        assertEquals("no data representation named banana in data_representation. found\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command with a test suite filename that does not correspond to a file*/
    public void testCompareNonExistentTestSuite() {
        String filename = "not-a-file";
        c.processCommand("compare " + filename + " CSV");
        assertEquals("file: " + filename + " could not be found\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command with a test suite containing test cases that do not match the specified representation*/
    public void testCompareTestSuiteContentsAndDataRepresentationMismatch() {
        /*we can just use on of the predetermined test suites made with newline delimiters,
        but change the delimiter in the command to something else. This would cause the
        system to read the whole thing as one test case, and CSVs throw a format mismatch
        exception if the the CSV contains newlines*/
        doComparison(singlePairTestSuiteName, null, null, null,
                "-1", null, null);
        assertEquals("one or more test cases in " + singlePairTestSuiteName + " do not match the specified data representation: data_representation.CSV\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command with a comparison metric that cannot  be instantiated*/
    public void testCompareMetricCannotBeInstantiated() {
        doComparison(singlePairTestSuiteName, null, "PairwiseComparisonStrategy", null,
                null, null, null);
        assertEquals("failed to instantiate pairwise metric: PairwiseComparisonStrategy: PairwiseComparisonStrategy " +
                "is a metrics.comparison.PairwiseComparisonStrategy. Expected a class.\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command with an empty file*/
    public void testCompareEmptyTestSuite() throws IOException {
        String emptyTestSuiteName = "empty-test-suite";
        new File(emptyTestSuiteName).createNewFile();
        doComparison(emptyTestSuiteName, null, null, null,
                null, null, null);
        assertEquals("operation failed because " + emptyTestSuiteName + " does not contain any test cases\r\n", outContent.toString());
        deleteFiles(emptyTestSuiteName);
    }

    @Test
    /*test for the compare command with a valid test suite and one invalid test suite*/
    public void testCompareSecondTestSuiteContentsAndDataRepresentationMismatch() throws IOException {
        String testSuiteName = "test-test-suite";
        String delimiter = "-1";
        String contents = "1,2,3,4,5,6" + delimiter + "1,2,3,5,4,6";
        writeFile(testSuiteName, contents);

        doComparison(testSuiteName, singlePairTestSuiteName, null, null,
                "-1", null, null);
        assertEquals("one or more test cases in " + singlePairTestSuiteName + " do not match the specified data representation: data_representation.CSV\r\n", outContent.toString());
    deleteFiles(testSuiteName);
    }

    @Test
    /*test for the compare command with one empty file and a non-empty file*/
    public void testCompareSecondTestSuiteEmpty() throws IOException {
        String emptyTestSuiteName = "empty-test-suite";
        new File(emptyTestSuiteName).createNewFile();
        doComparison(singlePairTestSuiteName, emptyTestSuiteName, null, null,
                null, null, null);
        assertEquals("operation failed because " + emptyTestSuiteName + " does not contain any test cases\r\n", outContent.toString());
        deleteFiles(emptyTestSuiteName);
    }

    @Test
    /*test for the compare command that checks that test case pairs can be generated from
    two test suites that contain a single test case each*/
    public void testGeneratePairsForTwoSmallSuites(){
        doComparison(singleCaseTestSuiteName, singleCaseTestSuiteName, "JaccardIndex", null,
                null, null, null);
        assertEquals(buildSinglePairConsoleOutput("1.0"), outContent.toString());
    }

    @Test
    /*test for the compare command that checks the proper error is displayed when a test suite is too small to generate pairs*/
    public void testCompareFailTooFewTestCases(){
        doComparison(singleCaseTestSuiteName, null, "JaccardIndex", null, null,
                null, null);
        assertEquals("Test suite contains insufficient test cases to generate pairs\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command that checks the numeber of threads does not change the calculation result*/
    public void testCompareNumThreadsDoesNotChangeResult() throws IOException {
        String output1 = "out-1";
        String output2 = "out-2";
        String output3 = "out-3";

        doComparison(sampleTestSuiteA, null, "JaccardIndex", null, null,
                output1, "7");
        doComparison(sampleTestSuiteA, null, "JaccardIndex", null, null,
                output2, "3");
        doComparison(sampleTestSuiteA, null, "JaccardIndex", null, null,
                output3, null);

        assertEquals(readFile(output1), readFile(output2));
        assertEquals(readFile(output1), readFile(output3));

        deleteFiles(output1, output2, output3);
    }
}