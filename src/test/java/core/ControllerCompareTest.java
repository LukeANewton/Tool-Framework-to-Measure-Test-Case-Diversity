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
    private InputStream originalIn;
    private PrintStream originalOut;
    private Config originalConfigFile;
    private FileReaderService reader;
    private FileWriterService writer;
    private Config config;
    private final double TOLERANCE = 0.001;
    private final String CONFIG_NAME = "config.json";
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
        originalIn = System.in;


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
        System.setIn(originalIn);
        writer.writeConfig(CONFIG_NAME, originalConfigFile);
        deleteFiles(singlePairTestSuiteName, singleCaseTestSuiteName, sampleTestSuiteA, sampleTestSuiteB);
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
        int character;
        StringBuilder fileContent = new StringBuilder();
        while((character = r.read()) != -1)
            fileContent.append((char) character);
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
                              String delimiter, boolean save, String saveFileName, String numThreads){
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
        if(save){
            s.append(" -s");
            if(saveFileName != null)
                s.append(" ").append(saveFileName);
        }
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
        return "[==========]"+System.lineSeparator()+"Result:" + System.lineSeparator() +
                System.lineSeparator() + result + System.lineSeparator();
    }

    /**
     * helper method to simulate input from the user for a test
     *
     * @param input the inpt to simulate doming from the user
     */
    private void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    /*test for the compare command that checks that files are correctly created*/
    public void testCompareSaveFileCreated() throws IOException {
        String testOutputName = "test-out";

        doComparison(singlePairTestSuiteName, null, "CommonElements", "AverageValue",
                null, true, testOutputName, null);

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
                null, true, outputName1 ,null);
        doComparison(singlePairTestSuiteName, null, "CommonElements", "MinimumValue",
                null, true, outputName2, null);

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
                delimiter, true, outputName1, null);

        delimiter = System.lineSeparator();
        contents = "1,2,3,4,5,6" + delimiter + "1,2,3,5,4,6";
        writeFile(inputName2, contents);
        doComparison(inputName2, null, "CommonElements", "MinimumValue",
                null, true, outputName2, null);

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
        config.setComparisonMethodLocation("metrics.comparison.pairwise");
        config.setAggregationMethod("AverageValue");
        config.setAggregationMethodLocation("metrics.aggregation");
        config.setOutputFileName("default");
        config.setDelimiter(System.lineSeparator());
        writer.writeConfig(CONFIG_NAME, config);

        c = Controller.getController(); //getting a new controller is required because the config file is read in during creation
        doComparison(singlePairTestSuiteName, null, null, null,
                null, false,null, null);

        //check the value of the resulting comparison. The singlePairTestSuite JaccardIndex should be 1
        assertEquals(buildSinglePairConsoleOutput("1.0"), outContent.toString());
    }

    @Test
    /*test for the compare command with an invalid pairwise metric name*/
    public void testCompareInvalidMetric() {
        doComparison(singlePairTestSuiteName, null, "banana", null,
                null, false,null, null);
        assertEquals("no pairwise metric named banana in metrics.comparison.pairwise. found" +
                System.lineSeparator(), outContent.toString());
    }

    @Test
    /*test for the compare command with an invalid aggregation method name*/
    public void testCompareInvalidAggregation() {
        doComparison(singlePairTestSuiteName, null, null, "apple",
                null, false, null, null);
        assertEquals("no aggregation method named apple in metrics.aggregation. found" +
                System.lineSeparator(), outContent.toString());
    }

    @Test
    /*test for the compare command with an invalid data representation name*/
    public void testCompareInvalidDataRepresentation() {
        c.processCommand("compare " + singlePairTestSuiteName + " banana");
        assertEquals("no data representation named banana in data_representation. found"
                + System.lineSeparator(), outContent.toString());
    }

    @Test
    /*test for the compare command with a test suite filename that does not correspond to a file*/
    public void testCompareNonExistentTestSuite() {
        String filename = "not-a-file";
        c.processCommand("compare " + filename + " CSV");
        assertEquals("file: " + filename + " could not be found" +
                System.lineSeparator(), outContent.toString());
    }

    @Test
    /*test for the compare command with a test suite containing test cases that do not match the specified representation*/
    public void testCompareTestSuiteContentsAndDataRepresentationMismatch() {
        //to ensure the comparison fails, we will use one of the files formatted as CSV with the EventSequence representation
        c.processCommand("compare " + singlePairTestSuiteName + " EventSequence");
        assertEquals("one or more test cases in " + singlePairTestSuiteName +
                " do not match the specified data representation: data_representation.EventSequence" +
                ": sequence does not begin with Start state" + System.lineSeparator(), outContent.toString());
    }

    @Test
    /*test for the compare command with a comparison metric that cannot  be instantiated*/
    public void testCompareMetricCannotBeInstantiated() {
        doComparison(singlePairTestSuiteName, null, "PairwiseComparisonStrategy", null,
                null, false, null, null);
        assertEquals("failed to instantiate pairwise metric: PairwiseComparisonStrategy: PairwiseComparisonStrategy " +
                "is a metrics.comparison.pairwise.PairwiseComparisonStrategy. Expected a class." +
                System.lineSeparator(), outContent.toString());
    }

    @Test
    /*test for the compare command with an empty file*/
    public void testCompareEmptyTestSuite() throws IOException {
        String emptyTestSuiteName = "empty-test-suite";
        new File(emptyTestSuiteName).createNewFile();
        doComparison(emptyTestSuiteName, null, null, null,
                null, false, null, null);
        assertEquals("operation failed because " + emptyTestSuiteName +
                " does not contain any test cases"+System.lineSeparator(), outContent.toString());
        deleteFiles(emptyTestSuiteName);
    }

    @Test
    /*test for the compare command with a valid test suite and one invalid test suite*/
    public void testCompareSecondTestSuiteContentsAndDataRepresentationMismatch() throws IOException {
        String testSuiteName = "test-test-suite";
        String delimiter = "-1";
        String contents = "Start-a-b-c" + delimiter + "Start-d-e-f";
        writeFile(testSuiteName, contents);
        c.processCommand("compare " + testSuiteName + " " + singlePairTestSuiteName + " EventSequence -d -1");
        assertEquals("one or more test cases in " + singlePairTestSuiteName +
                " do not match the specified data representation: data_representation.EventSequence" +
                        ": sequence does not begin with Start state" + System.lineSeparator(),
                outContent.toString());
        deleteFiles(testSuiteName);
    }

    @Test
    /*test for the compare command with one empty file and a non-empty file*/
    public void testCompareSecondTestSuiteEmpty() throws IOException {
        String emptyTestSuiteName = "empty-test-suite";
        new File(emptyTestSuiteName).createNewFile();
        doComparison(singlePairTestSuiteName, emptyTestSuiteName, null, null,
                null, false,null, null);
        assertEquals("operation failed because " + emptyTestSuiteName + " does not contain any test cases"
                        +System.lineSeparator(), outContent.toString());
        deleteFiles(emptyTestSuiteName);
    }

    @Test
    /*test for the compare command that checks that test case pairs can be generated from
    two test suites that contain a single test case each*/
    public void testGeneratePairsForTwoSmallSuites(){
        doComparison(singleCaseTestSuiteName, singleCaseTestSuiteName, "JaccardIndex", null,
                null, false,null, null);
        assertEquals(buildSinglePairConsoleOutput("1.0"), outContent.toString());
    }

    @Test
    /*test for the compare command that checks the proper error is displayed when a test suite is too small to generate pairs*/
    public void testCompareFailTooFewTestCases(){
        doComparison(singleCaseTestSuiteName, null, "JaccardIndex", null, null, false,
                null, null);
        assertEquals("Test suite contains insufficient test cases to generate pairs\r\n", outContent.toString());
    }

    @Test
    /*test for the compare command that checks the number of threads, or choice or threads/sequential
     does not change the calculation result*/
    public void testCompareNumThreadsDoesNotChangeResult() throws IOException {
        String output1 = "out-1";
        String output2 = "out-2";
        String output3 = "out-3";
        String output4 = "out-4";

        doComparison(sampleTestSuiteA, null, "CommonElements", null, null,
                true, output1, "7");
        doComparison(sampleTestSuiteA, null, "CommonElements", null, null,
                true, output2, "3");
        doComparison(sampleTestSuiteA, null, "CommonElements", null, null,
                true, output3, null);
        c.processCommand("compare " + sampleTestSuiteA + " CSV -m CommonElements -t -s " + output4);

        assertEquals(readFile(output1), readFile(output2));
        assertEquals(readFile(output1), readFile(output3));
        assertEquals(readFile(output1), readFile(output4));
        /*hand calculated value:
        sampleTestSuiteA generates 10 pairs
        with common elements: [4 4 4 4 5 9 9 5 5 12]*/
        assertEquals(6.1, Double.parseDouble(readFile(output1)), TOLERANCE);

        deleteFiles(output1, output2, output3, output4);
    }

    @Test
    /*test for the compare command that checks results can be correctly appended to a file*/
    public void testCompareFileExistsAppend() throws IOException {
        String filename = "out";
        prepareFileExistsIssue(filename, "a");
        assertEquals("6.1"+System.lineSeparator()+"6.1" ,readFile(filename));
        deleteFiles(filename);
    }

    @Test
    /*test for the compare command that checks results can correctly overwrite a file*/
    public void testCompareFileExistsOverwrite() throws IOException {
        String filename = "out";
        prepareFileExistsIssue(filename, "y");
        assertEquals("6.1" ,readFile(filename));
        deleteFiles(filename);
    }

    @Test
    /*test for the compare command that checks results can cancel a write if needed*/
    public void testCompareFileExistsCancelOverwrite() throws IOException {
        String filename = "out";
        prepareFileExistsIssue(filename, "n");
        assertEquals("6.1" ,readFile(filename));
        assertTrue(outContent.toString().contains("file writing cancelled since file already exists"));
        deleteFiles(filename);
    }

    @Test
    /*test for the compare command that provides a save token but no file name in dto*/
    public void testCompareSaveFileNameNotInDTO() throws IOException {
        String filename = "defaultName";
        config.setOutputFileName(filename);
        writer.writeConfig(CONFIG_NAME, config);

        //getting a new controller is required because the config file is read in during creation
        c = Controller.getController();

        doComparison(sampleTestSuiteA, null, "CommonElements", null, null,
                true, null, null);

        //prepareFileExistsIssue(filename, "y");
        assertEquals("6.1" ,readFile(filename));
        deleteFiles(filename);
    }

    @Test
    /*test for the compare command that provides a save token but no file name in dto or config*/
    public void testCompareSaveFileNameNotInDTOAndConfig() throws IOException {
        config.setOutputFileName(null);
        writer.writeConfig(CONFIG_NAME, config);

        //getting a new controller is required because the config file is read in during creation
        c = Controller.getController();

        doComparison(sampleTestSuiteA, null, "CommonElements", null, null,
                true, null, null);

        assertTrue(outContent.toString(), outContent.toString().contains("failed to save, outputFileName not given"));
    }

    /**
     * helper method for tests that check an overwrite option
     *
     * @param filename the name of the file to cause an overwrite issue on requiring user input
     * @param choice the user input choice for dealing with the issue
     */
    private void prepareFileExistsIssue(String filename, String choice) {
        doComparison(sampleTestSuiteA, null, "CommonElements", null, null,
                true, filename, null);
        provideInput(choice);
        c = Controller.getController();
        doComparison(sampleTestSuiteA, null, "CommonElements", null, null,
                true, filename, null);
    }
}