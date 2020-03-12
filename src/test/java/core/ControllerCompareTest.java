package core;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    private final String SINGLE_PAIR_TEST_SUITE_FILE_NAME = "single-pair-suite";
    private final String SINGLE_CASE_TEST_SUITE_FILE_NAME = "single-test-case";
    private final String SAMPLE_TEST_SUITE_A_FILE_NAME = "sample-suite-A";
    private final String SAMPLE_TEST_SUITE_B_FILE_NAME = "sample-suite-b";
    private final String RESULT_FILE_NAME = "result";

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
        String contents = "1,2,3,4,5,6"+System.lineSeparator()+"1,2,3,5,4,6";
        writeFile(SINGLE_PAIR_TEST_SUITE_FILE_NAME, contents);
        contents = "1,2,3,4,5,6";
        writeFile(SINGLE_CASE_TEST_SUITE_FILE_NAME, contents);
        contents = "1,2,3,4,5" + System.lineSeparator() +
                "1,2,3,4,6,7,3,4,6,7,3,4,5" + System.lineSeparator() +
                "1,2,3,4,6,8,9,3,4,6,8,9,3,4,5" + System.lineSeparator() +
                "1,2,3,4,6,7,3,4,6,8,9,3,4,5" + System.lineSeparator() +
                "1,2,3,4,6,7,3,4,6,8,9,3,10";
        writeFile(SAMPLE_TEST_SUITE_A_FILE_NAME, contents);
        contents = "4,7,9,36,4,6,6,6,6" + System.lineSeparator() +
                "1,2,3,4,6,3,8,26,0,2,7,8,6,66" + System.lineSeparator() +
                "1,2,3,4,4,6,3,7,2,6,9" + System.lineSeparator() +
                "4,6,7,3,4,6,8,9,3,4,5";
        writeFile(SAMPLE_TEST_SUITE_B_FILE_NAME, contents);
    }

    @After
    public void tearDown() throws IOException {
        System.setOut(originalOut);
        System.setIn(originalIn);
        writer.writeConfig(CONFIG_NAME, originalConfigFile);
        deleteFiles(SINGLE_PAIR_TEST_SUITE_FILE_NAME, SINGLE_CASE_TEST_SUITE_FILE_NAME, SAMPLE_TEST_SUITE_A_FILE_NAME,
                SAMPLE_TEST_SUITE_B_FILE_NAME, RESULT_FILE_NAME);
    }

    /**
     * Test for controller creation where there is no config file
     */
    @Test
    public void testControllerCreationWithoutConfigFile(){
        deleteFiles(CONFIG_NAME);
        Controller.getController();
        assertEquals("Failed to read from configuration file: " + CONFIG_NAME + ". Ensure the file exists in the same directory as this program." + System.lineSeparator(), outContent.toString());
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
     * @param reportFormats the names of the report formats to use
     */
    private void doComparison(String filename1, String filename2, String metric, String aggregation, String delimiter,
                              boolean save, String saveFileName, String numThreads, String[] reportFormats) {
        StringBuilder s = new StringBuilder();
        s.append("compare ").append(filename1);
        if (filename2 != null)
            s.append(" ").append(filename2);
        s.append(" CSV");
        if (metric != null)
            s.append(" -m ").append(metric);
        if (aggregation != null)
            s.append(" -a ").append(aggregation);
        if (delimiter != null)
            s.append(" -d ").append(delimiter);
        if(save){
            s.append(" -s");
            if(saveFileName != null)
                s.append(" ").append(saveFileName);
        }
        if(numThreads != null)
            s.append(" -t ").append(numThreads);
        if (reportFormats != null) {
            s.append(" -r");
            for (String format : reportFormats) {
                s.append(" ").append(format);
            }
        }
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
     * helper method to simulate input from the user for a test
     *
     * @param input the input to simulate doming from the user
     */
    private void provideInput(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    /**
     * Test for the compare command that checks that files are correctly created.
     */
    @Test
    public void testCompareSaveFileCreated() throws IOException {
        String testOutputName = "test-out";

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, true, testOutputName, null, null);

        //check that the file was correctly created
        assertTrue(new File(testOutputName).exists());

        //check that the file contents matches what is displayed to the console
        String expected = readFile(testOutputName) + System.lineSeparator();
        String actual = outContent.toString();
        assertTrue("'"+ actual +"' doesn't contain '" + expected + "'.", actual.contains(expected));
        //check the actual value of the comparison (hand calculated)
        assertEquals(Double.parseDouble(expected.substring(1, expected.length()-5)), 4.0, TOLERANCE);

        //clean up files created
        deleteFiles(testOutputName);
    }

    /**
     * Test for the compare command that performs two pairwise metrics on a single pair of tests
     * with different aggregation methods. The output for each should be the same since there is
     * only one pair.
     */
    @Test
    public void testCompareSinglePair() throws IOException {
        String outputName1 = "test-out1";
        String outputName2 = "test-out2";

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, true, outputName1, null, null);
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "MinimumValue",
                null, true, outputName2, null, null);

        //check that the results are the same for each file
        assertEquals(readFile(outputName1), readFile(outputName2));

        //clean up files created
        deleteFiles(outputName1, outputName2);
    }

    /**
     * Test for the compare command with a non-default delimiter, the results
     * should not change with different delimiters.
     */
    @Test
    public void testDifferentDelimiters() throws IOException {
        String outputName1 = "test-out1";
        String outputName2 = "test-out2";
        String inputName1 = "test-data1";
        String inputName2 = "test-data2";

        String delimiter = "banana";
        String contents = "1,2,3,4,5,6" + delimiter + "1,2,3,5,4,6";
        writeFile(inputName1, contents);
        doComparison(inputName1, null, "CommonElements", "AverageValue",
                delimiter, true, outputName1, null, null);

        delimiter = System.lineSeparator();
        contents = "1,2,3,4,5,6" + delimiter + "1,2,3,5,4,6";
        writeFile(inputName2, contents);
        doComparison(inputName2, null, "CommonElements", "MinimumValue",
                null, true, outputName2, null, null);

        //check that the results are the same for each file
        assertEquals(readFile(outputName1), readFile(outputName2));

        //clean up files created
        deleteFiles(inputName1, inputName2, outputName1, outputName2);
    }

    /**
     * Test for the compare command using all default values.
     */
    @Test
    public void testCompareAllDefaults() throws IOException {
        //set up a config file here that includes defaults
        config.setDataRepresentationLocation("data_representation");
        config.setPairwiseMethod("JaccardIndex");
        config.setPairwiseMethodLocation("metrics.comparison.pairwise");
        config.setAggregationMethod("AverageValue");
        config.setAggregationMethodLocation("metrics.aggregation");
        config.setOutputFileName("default");
        config.setDelimiter(System.lineSeparator());
        writer.writeConfig(CONFIG_NAME, config);

        c = Controller.getController(); //getting a new controller is required because the config file is read in during creation
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, null, null,
                null, false, null, null, null);

        //check the value of the resulting comparison. The singlePairTestSuite JaccardIndex should be 1
        String expected = "[1.0]" + System.lineSeparator();
        String actual = outContent.toString();
        assertTrue("'" + actual + "' does not contain '" + expected + "'", actual.contains(expected));
    }

    /**
     * Test for the compare command with an invalid pairwise metric name.
     */
    @Test
    public void testCompareInvalidMetric() {
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "banana", null,
                null, false, null, null, null);
        assertEquals("The specified metric either cannot be found, or does not implement the required interface" +
                System.lineSeparator(), outContent.toString());
    }

    /**
     * Test for the compare command with an invalid aggregation method name.
     */
    @Test
    public void testCompareInvalidAggregation() {
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, null, "apple",
                null, false, null, null, null);
        assertEquals("no aggregation method named apple in metrics.aggregation. found" +
                System.lineSeparator(), outContent.toString());
    }

    /**
     * Test for the compare command with an invalid data representation name.
     */
    @Test
    public void testCompareInvalidDataRepresentation() {
        c.processCommand("compare " + SINGLE_PAIR_TEST_SUITE_FILE_NAME + " banana");
        assertEquals("no data representation named banana in data_representation. found"
                + System.lineSeparator(), outContent.toString());
    }

    /**
     * Test for the compare command with a test suite filename that does not correspond to a file.
     */
    @Test
    public void testCompareNonExistentTestSuite() {
        String filename = "not-a-file";
        c.processCommand("compare " + filename + " CSV");
        assertEquals("file: " + filename + " could not be found" +
                System.lineSeparator(), outContent.toString());
    }

    /**
     * Test for the compare command with a test suite containing test cases that do not match the specified representation.
     */
    @Test
    public void testCompareTestSuiteContentsAndDataRepresentationMismatch() {
        //to ensure the comparison fails, we will use one of the files formatted as CSV with the EventSequence representation
        c.processCommand("compare " + SINGLE_PAIR_TEST_SUITE_FILE_NAME + " EventSequence");
        assertEquals("one or more test cases in " + SINGLE_PAIR_TEST_SUITE_FILE_NAME +
                " do not match the specified data representation: data_representation.EventSequence" +
                ": sequence does not begin with Start state" + System.lineSeparator(), outContent.toString());
    }

    /**
     * Test for the compare command with a comparison metric that cannot be instantiated.
     */
    @Test
    public void testCompareMetricCannotBeInstantiated() {
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "PairwiseComparisonStrategy", null,
                null, false, null, null, null);
        assertEquals("The specified metric either cannot be found, or does not implement the required interface" +
                System.lineSeparator(), outContent.toString());
    }

    /**
     * Test for the compare command with a data representation that cannot be instantiated.
     */
    @Test
    public void testCompareRepresentationCannotBeInstantiated() {
        c.processCommand("compare " + SINGLE_PAIR_TEST_SUITE_FILE_NAME + " DataRepresentation");
        assertEquals("failed to instantiate data representation: DataRepresentation: DataRepresentation is a " +
                "data_representation.DataRepresentation. Expected a class." +
                System.lineSeparator(), outContent.toString());
    }

    /**
     * Test for the compare command with an empty file.
     */
    @Test
    public void testCompareEmptyTestSuite() throws IOException {
        String emptyTestSuiteName = "empty-test-suite";
        new File(emptyTestSuiteName).createNewFile();
        doComparison(emptyTestSuiteName, null, null, null,
                null, false, null, null, null);
        assertEquals("operation failed because " + emptyTestSuiteName +
                " does not contain any test cases"+System.lineSeparator(), outContent.toString());
        deleteFiles(emptyTestSuiteName);
    }

    /**
     * Test for the compare command with a valid test suite and one invalid test suite.
     */
    @Test
    public void testCompareSecondTestSuiteContentsAndDataRepresentationMismatch() throws IOException {
        String testSuiteName = "test-test-suite";
        String delimiter = "-1";
        String contents = "Start-a-b-c" + delimiter + "Start-d-e-f";
        writeFile(testSuiteName, contents);
        c.processCommand("compare " + testSuiteName + " " + SINGLE_PAIR_TEST_SUITE_FILE_NAME + " EventSequence -d -1");
        assertEquals("one or more test cases in " + SINGLE_PAIR_TEST_SUITE_FILE_NAME +
                " do not match the specified data representation: data_representation.EventSequence" +
                        ": sequence does not begin with Start state" + System.lineSeparator(),
                outContent.toString());
        deleteFiles(testSuiteName);
    }

    /**
     * Test for the compare command with one empty file and a non-empty file.
     */
    @Test
    public void testCompareSecondTestSuiteEmpty() throws IOException {
        String emptyTestSuiteName = "empty-test-suite";
        new File(emptyTestSuiteName).createNewFile();
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, emptyTestSuiteName, null, null,
                null, false, null, null, null);
        assertEquals("operation failed because " + emptyTestSuiteName + " does not contain any test cases"
                        +System.lineSeparator(), outContent.toString());
        deleteFiles(emptyTestSuiteName);
    }

    /**
     * Test for the compare command that checks that test case pairs can be generated from
     * two test suites that contain a single test case each.
     */
    @Test
    public void testGeneratePairsForTwoSmallSuites(){
        doComparison(SINGLE_CASE_TEST_SUITE_FILE_NAME, SINGLE_CASE_TEST_SUITE_FILE_NAME, "JaccardIndex", null,
                null, false, null, null, null);

        String expected = "[1.0]" + System.lineSeparator();
        String actual = outContent.toString();
        assertTrue("'" + actual + "' does not contain '" + expected + "'", actual.contains(expected));
    }

    /**
     * Test for the compare command that checks the proper error is displayed when a test suite is too small to generate pairs*/
    @Test
    public void testCompareFailTooFewTestCases(){
        doComparison(SINGLE_CASE_TEST_SUITE_FILE_NAME, null, "JaccardIndex", null, null,
                false, null, null, null);
        String expected = "Test suite contains insufficient test cases to generate pairs" + System.lineSeparator();
        String actual = outContent.toString();
        assertEquals(expected, actual.substring(actual.length() - expected.length()));
    }

    /**
     * Test for the compare command that checks the number of threads, or choice or threads/sequential
     * does not change the calculation result.
     */
    @Test
    public void testCompareNumThreadsDoesNotChangeResult() throws IOException {
        String output1 = RESULT_FILE_NAME + "1";
        String output2 = RESULT_FILE_NAME + "2";
        String output3 = RESULT_FILE_NAME + "3";
        String output4 = RESULT_FILE_NAME + "4";

        doComparison(SAMPLE_TEST_SUITE_A_FILE_NAME, null, "CommonElements", null, null,
                true, output1, "7", null);
        doComparison(SAMPLE_TEST_SUITE_A_FILE_NAME, null, "CommonElements", null, null,
                true, output2, "3", null);
        doComparison(SAMPLE_TEST_SUITE_A_FILE_NAME, null, "CommonElements", null, null,
                true, output3, null, null);
        c.processCommand("compare " + SAMPLE_TEST_SUITE_A_FILE_NAME + " CSV -m CommonElements -t -s " + output4);

        assertEquals(readFile(output1), readFile(output2));
        assertEquals(readFile(output1), readFile(output3));
        assertEquals(readFile(output1), readFile(output4));
        /*hand calculated value:
        sampleTestSuiteA generates 10 pairs
        with common elements: [4 4 4 4 5 9 9 5 5 12]*/
        String result = readFile(output1);
        assertEquals(6.1, Double.parseDouble(result.substring(1, result.length()-3)), TOLERANCE);

        deleteFiles(output1, output2, output3, output4);
    }

    /**
     * Test for the compare command that checks the number of threads, or choice or threads/sequential
     * does not change the calculation result.
     */
    @Test
    public void testCompareMultipleAggregations() throws IOException {
        doComparison(SAMPLE_TEST_SUITE_A_FILE_NAME, null, "CommonElements", "AverageValue MinimumValue", null,
                true, RESULT_FILE_NAME, "7", null);
        /*hand calculated value:
        sampleTestSuiteA generates 10 pairs
        with common elements: [4 4 4 4 5 9 9 5 5 12]*/
        String contents = readFile(RESULT_FILE_NAME);
        String[] results = contents.split(System.lineSeparator());

        String firstResult = results[0];
        String secondResult = results[1];

        assertEquals(6.1, Double.parseDouble(firstResult.substring(1, firstResult.length()-1)), TOLERANCE);
        assertEquals(4, Double.parseDouble(secondResult.substring(1, firstResult.length()-1)), TOLERANCE);

        deleteFiles(RESULT_FILE_NAME);
    }

    /**
     * Test for the compare command that checks results can be correctly appended to a file.
     */
    @Test
    public void testCompareFileExistsAppend() throws IOException {
        prepareFileExistsIssue("a");
        assertEquals("[6.1]"+System.lineSeparator()+System.lineSeparator()+"[6.1]"+System.lineSeparator(), readFile(RESULT_FILE_NAME));
        deleteFiles(RESULT_FILE_NAME);
    }

    /**
     * Test for the compare command that checks results can correctly overwrite a file.
     */
    @Test
    public void testCompareFileExistsOverwrite() throws IOException {
        prepareFileExistsIssue("y");
        assertTrue(readFile(RESULT_FILE_NAME).contains("6.1"));
        deleteFiles(RESULT_FILE_NAME);
    }

    /**
     * Test for the compare command that checks results can cancel a write if needed.
     */
    @Test
    public void testCompareFileExistsCancelOverwrite() throws IOException {
        prepareFileExistsIssue("n");
        assertTrue(readFile(RESULT_FILE_NAME).contains("6.1"));
        assertTrue(outContent.toString().contains("file writing cancelled since file already exists"));
        deleteFiles(RESULT_FILE_NAME);
    }

    /**
     * Test for the compare command that provides a save token but no file name in dto.
     */
    @Test
    public void testCompareSaveFileNameNotInDTO() throws IOException {
        String filename = "defaultName";
        config.setOutputFileName(filename);
        writer.writeConfig(CONFIG_NAME, config);

        //getting a new controller is required because the config file is read in during creation
        c = Controller.getController();

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", null, null,
                true, null, null, null);

        assertEquals("[4.0]"+System.lineSeparator(), readFile(filename));
        deleteFiles(filename);
    }

    @Test
    /*test for the compare command that provides a save token but no file name in dto or config*/
    public void testCompareSaveFileNameNotInDTOAndConfig() throws IOException {
        config.setOutputFileName(null);
        writer.writeConfig(CONFIG_NAME, config);

        //getting a new controller is required because the config file is read in during creation
        c = Controller.getController();

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", null, null,
                true, null, null, null);

        assertTrue(outContent.toString(), outContent.toString().contains("failed to save, outputFileName not given"));
    }

    /**
     * Helper method for tests that check an overwrite option.
     *
     * @param choice the user input choice for dealing with the issue
     */
    private void prepareFileExistsIssue(String choice) {
        doComparison(SAMPLE_TEST_SUITE_A_FILE_NAME, null, "CommonElements", null, null,
                true, RESULT_FILE_NAME, null, null);
        provideInput(choice);
        c = Controller.getController();
        doComparison(SAMPLE_TEST_SUITE_A_FILE_NAME, null, "CommonElements", null, null,
                true, RESULT_FILE_NAME, null, null);
    }

    /**
     * Test the compare command for a listwise command.
     */
    @Test
    public void testListwiseCompare() throws IOException {
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "ShannonIndex", null, null,
                true, RESULT_FILE_NAME, null, null);
        String result = readFile(RESULT_FILE_NAME);
        assertEquals(1.79 ,Double.parseDouble(result.substring(1,result.length()-3)), 0.01);
        deleteFiles(RESULT_FILE_NAME);
    }

    /**
     * Test the compare command for a listwise command.
     */
    @Test
    public void testListwiseCompareMultipleFiles() throws IOException {
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, SAMPLE_TEST_SUITE_A_FILE_NAME, "ShannonIndex", "MinimumValue", null,
                true, RESULT_FILE_NAME, null, null);
        String result = readFile(RESULT_FILE_NAME);
        assertEquals(1.79 ,Double.parseDouble(result.substring(1, result.length()-3)), 0.01);
        deleteFiles(RESULT_FILE_NAME);
    }

    /**
     * Test for compare that ensures the config locations work with or without a dot at the end.
     */
    @Test
    public void testCompareMetricLocationEndsInDot() throws IOException {
        String outputName1 = "test-out1";
        String outputName2 = "test-out2";

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, true, outputName1, null, null);
        config.setPairwiseMethodLocation(config.getPairwiseMethodLocation()+".");
        config.setAggregationMethodLocation(config.getAggregationMethodLocation()+".");
        writer.writeConfig(CONFIG_NAME, config);
        c = Controller.getController();
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, true, outputName2, null, null);

        //check that the results are the same for each file
        assertEquals(readFile(outputName1), readFile(outputName2));

        //clean up files created
        deleteFiles(outputName1, outputName2);
    }

    /**
     * Test that output isn't saved to a file when save = false and an file name is specified.
     * This test confirms that the save flag overrides the assumption of saving when a file name is entered or read from config.
     */
    @Test
    public void testSaveFalseGivenFileName() {
        String fileName = "ControllerCompareTest_testSaveFalseGivenFileName";
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, false, fileName + "", null, new String[]{"JSON"});
        boolean fileExists = new File(fileName + "JSON").exists();
        assertFalse("Output file created when save flag set to false.", fileExists);
        if (fileExists) {
            deleteFiles(fileName + "JSON");
        }
    }

    /**
     * Test multiple specified report formats print results to file and console.
     */
    @Test
    public void testMultipleReportFormatsPrintResults() throws IOException {
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, true, RESULT_FILE_NAME, null, new String[]{"RawResults", "JSON"});

        c = Controller.getController();

        // Check files
        assertFalse(readFile(RESULT_FILE_NAME + "RawResults").isEmpty());
        assertTrue(readFile(RESULT_FILE_NAME + "RawResults").contains("4.0"));

        Reader reader = new FileReader(RESULT_FILE_NAME + "JSON");
        JsonElement jsonResult = JsonParser.parseReader(reader);
        reader.close();

        assertTrue(jsonResult.isJsonObject());
        assertEquals("[4.0]", jsonResult.getAsJsonObject().get("results").getAsJsonObject().get("averagevalue").getAsString());

        deleteFiles(RESULT_FILE_NAME + "RawResults", RESULT_FILE_NAME + "JSON");

        // Check console
        assertTrue("Correct result wasn't printed to console.", outContent.toString().contains("[4.0]"));
        assertTrue("Expected JSON format wasn't printed to console.", outContent.toString().contains("\"results\":{"));
    }

    /**
     * Test specified report format prints results to console.
     */
    @Test
    public void testReportFormatPrintsResultsToConsole() {
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, false, RESULT_FILE_NAME, null, new String[]{"RawResults"});
        c = Controller.getController();
        // By adding the line separator, we can safely assume that the format is either Pretty or Raw.
        assertTrue("Correct result wasn't printed to console. " + outContent.toString(), outContent.toString().contains("[4.0]"+System.lineSeparator()));
        // This ensures that the format has to be Raw, which is what we asked for.
        assertFalse("Incorrect (pretty?) format printed to console when we expected the raw format.", outContent.toString().contains("Results:"));
    }

    /**
     * Test that a bad report format will print an error message to the console.
     */
    @Test
    public void testBadReportFormat() {
        final String INVALID_FORMAT = "NotAFormat872iqwert";
        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "CommonElements", "AverageValue",
                null, false, RESULT_FILE_NAME, null, new String[]{INVALID_FORMAT});
        c = Controller.getController();
        assertTrue("No error printed to console when given an invalid report format name.", outContent.toString().contains("report format named " + INVALID_FORMAT));
    }

    /**
     * Test rounding one aggregation result
     */
    @Test
    public void testRoundOneAggregation() throws IOException {
        config.setResultRoundingScale(5);
        writer.writeConfig(CONFIG_NAME, config);
        c = Controller.getController();

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "ShannonIndex", null, null,
                false, null, null, null);
        assertTrue("Correct result wasn't printed to console. " + outContent.toString(), outContent.toString().contains("[1.79176]"+System.lineSeparator()));
    }

    /**
     * Test rounding multiple aggregation methods
     */
    @Test
    public void testRoundMultipleAggregation() throws IOException {
        config.setResultRoundingScale(5);
        writer.writeConfig(CONFIG_NAME, config);
        c = Controller.getController();

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "ShannonIndex", "AverageValue MinimumValue Summation", null,
                false, null, null, null);
        assertTrue("Correct result wasn't printed to console. " + outContent.toString(), outContent.toString().contains("[1.79176]"+System.lineSeparator()+"[1.79176]"+System.lineSeparator()+"[1.79176]"+System.lineSeparator()));
    }

    /**
     * Test error while rounding
     */
    @Test
    public void testErrorWhileRounding() throws IOException {
        config.setResultRoundingMode("UNNECESSARY");
        writer.writeConfig(CONFIG_NAME, config);
        c = Controller.getController();

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "ShannonIndex", "AverageValue MinimumValue Summation", null,
                false, null, null, null);
        assertTrue("Error wasn't printed to console. " + outContent.toString(), outContent.toString().contains("Rounding Error: Could not round aggregation result"+System.lineSeparator()));
        assertTrue("Correct result wasn't printed to console. " + outContent.toString(), outContent.toString().contains("[1.7917594692280547]"+System.lineSeparator()));
    }

    /**
     * Test incorrect rounding mode
     */
    @Test
    public void testIncorrectRoundingMode() throws IOException {
        config.setResultRoundingMode("banana");
        writer.writeConfig(CONFIG_NAME, config);
        c = Controller.getController();

        doComparison(SINGLE_PAIR_TEST_SUITE_FILE_NAME, null, "ShannonIndex", "AverageValue MinimumValue Summation", null,
                false, null, null, null);
        assertTrue("Error wasn't printed to console. " + outContent.toString(), outContent.toString().contains("Rounding Error: banana is not a valid rounding mode"+System.lineSeparator()));
        assertTrue("Correct result wasn't printed to console. " + outContent.toString(), outContent.toString().contains("[1.7917594692280547]"+System.lineSeparator()));
    }
}
