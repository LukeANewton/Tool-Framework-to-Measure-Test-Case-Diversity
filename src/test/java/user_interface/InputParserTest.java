package user_interface;

import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class InputParserTest {
    private InputParser input;

    @Before
    public void setUp(){
        input = new InputParser();
    }

    @Test(expected = InvalidCommandException.class)
    /*test for parsing unrecognized command*/
    public void testInvalidCommand() throws InvalidCommandException {
        input.parse("banana");
        fail();
    }

    /**
     * helper method for testing valid help commands
     *
     * @param helpType the type of help expected in the DTO
     * @throws InvalidCommandException when the passed parameters so not make a valid command
     */
    private void helpHelper(HelpType helpType) throws InvalidCommandException {
        StringBuilder command = new StringBuilder();
        command.append("help");
        switch(helpType){
            case AGGREGATION_METHOD:
                command.append(" -a");
                break;
            case DATA_REPRESENTATION:
                command.append(" -f");
                break;
            case METRIC:
                command.append(" -m");
                break;
            case REPORT_FORMAT:
                command.append(" -r");
                break;
            case COMMAND:
                break;
        }

        DataTransferObject dto = input.parse(command.toString());
        assertEquals(dto.getCommandType(), CommandType.Help);
        assertEquals(((HelpDTO) dto).getHelpType(), helpType);
    }

    @Test
    /*test for parsing command help*/
    public void testCommandHelp() throws InvalidCommandException {
        helpHelper(HelpType.COMMAND);
    }

    @Test
    /*test for parsing aggregation method help*/
    public void testAggregationHelp() throws InvalidCommandException {
        helpHelper(HelpType.AGGREGATION_METHOD);
    }

    /**
     * Test for parsing report format method help
     * @throws InvalidCommandException on invalid command construction.
     */
    @Test
    public void testReportFormatHelp() throws InvalidCommandException {
        helpHelper(HelpType.REPORT_FORMAT);
    }

    @Test
    /*test for parsing comparison metric help*/
    public void testPairwiseHelp() throws InvalidCommandException {
        helpHelper(HelpType.METRIC);
    }

    @Test
    /*test for parsing data representation help*/
    public void testDataRepresentationHelp() throws InvalidCommandException {
        helpHelper(HelpType.DATA_REPRESENTATION);
    }

    @Test(expected = InvalidCommandException.class)
    /*test for parsing an invalid help with extra tokens*/
    public void testHelpExtraTokens() throws InvalidCommandException {
        input.parse("help -f -m");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /*test for parsing an invalid help with invalid type*/
    public void testHelpInvalidType() throws InvalidCommandException {
        input.parse("help -x");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /*test for parsing config command with no parameter name or value specified*/
    public void testConfigNoSpecification() throws InvalidCommandException {
        input.parse("config");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /*test for parsing config command with no parameter value specified*/
    public void testConfigNoValueSet() throws InvalidCommandException {
        input.parse("config banana");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /*test for parsing config command with extra tokens*/
    public void testConfigExtraTokensShort() throws InvalidCommandException {
        input.parse("config numThreads 4 banana");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /*test for parsing config command with extra tokens*/
    public void testConfigExtraTokensLong() throws InvalidCommandException {
        input.parse("config numThreads 4 banana orange apple");
        fail();
    }

    /**
     * helper method for testing valid config commands
     *
     * @param name the expected parameter name to find in the DTO
     * @param value the expected parameter value to find in the DTO
     * @param isSaved the expected save choice to find in the DTO
     * @throws InvalidCommandException when passed parameters generate an invalid command
     */
    private void configHelper(String name, String value, boolean isSaved) throws InvalidCommandException {
        StringBuilder command = new StringBuilder();
        command.append("config").append(" ").append(name).append(" ").append(value);
        if(isSaved)
            command.append(" -s");

        DataTransferObject dto = input.parse(command.toString());

        assertEquals(CommandType.Configure, dto.getCommandType());
        assertEquals(name, ((ConfigDTO)dto).getParameterName());
        assertEquals(value, ((ConfigDTO)dto).getParameterValue());
        assertEquals(isSaved, ((ConfigDTO)dto).isConfigSaved());
    }

    @Test
    /*test for parsing a valid config command without save option*/
    public void testConfigValidSyntax() throws InvalidCommandException {
        String name = "numThreads";
        String value = "4";
        configHelper(name, value,false);
    }

    @Test
    /*test for parsing a syntactically valid config command that is semantically invalid.
     * the parser should not know anything about what a valid name/value is, so this should
     * not throw an InvalidCommandException*/
    public void testConfigValidSyntaxInvalidSemantics() throws InvalidCommandException {
        String name = "banana";
        String value = "orange";
        configHelper(name, value,false);
    }

    @Test
    /*test for parsing a valid config command with save option*/
    public void testConfigValidSyntaxWithSave() throws InvalidCommandException {
        String name = "numThreads";
        String value = "4";
        configHelper(name, value,true);
    }

    /**
     * helper method for testing valid compare commands
     *
     * @param file1 the expected first filename to find in the DTO
     * @param file2 the expected second filename value to find in the DTO
     * @param dataRepresentation the expected data representation to find in the DTO
     * @param reportFormats the name of the report format class.
     * @throws InvalidCommandException when the parameters specified result in an invalid command
     */
    private void compareHelper(String file1, String file2, String dataRepresentation, String comparisonMethod,
                               String[] aggregationMethods, String delimiter, String outputFilename,
                               boolean save, Integer numThreads, String[] reportFormats) throws InvalidCommandException {
        StringBuilder command = new StringBuilder();
        command.append("compare").append(" ").append(file1).append(" ").append(file2).append(" ").append(dataRepresentation);
        if(comparisonMethod != null)
            command.append(" -m ").append(comparisonMethod);
        if(aggregationMethods != null) {
            command.append(" -a");
            for(String aggregationMethod : aggregationMethods)
                command.append(" ").append(aggregationMethod);
        }
        if (reportFormats != null) {
            command.append(" -r");
            for (String format : reportFormats) {
                command.append(" ").append(format);
            }
        }
        if(delimiter != null)
            command.append(" -d ").append(delimiter);
        if(save){
            command.append(" -s");
            if(outputFilename != null)
                command.append(" ").append(outputFilename);
        }
        if(numThreads != null)
            command.append(" -t ").append(numThreads);


        DataTransferObject dto = input.parse(command.toString());
        assertEquals(dto.getCommandType(), CommandType.Compare);
        assertEquals(file1, ((CompareDTO)dto).getTestCaseLocationOne());
        assertEquals(file2, ((CompareDTO)dto).getTestCaseLocationTwo());
        assertEquals(dataRepresentation, ((CompareDTO)dto).getDataRepresentation());
        assertEquals(comparisonMethod, ((CompareDTO)dto).getComparisonMethod());
        assertArrayEquals(aggregationMethods, ((CompareDTO) dto).getAggregationMethods());
        assertArrayEquals(reportFormats, ((CompareDTO) dto).getReportFormats());
        assertEquals(delimiter, ((CompareDTO)dto).getDelimiter());
        assertEquals(outputFilename, ((CompareDTO)dto).getOutputFilename());
        assertEquals(numThreads, ((CompareDTO)dto).getNumberOfThreads());
        assertEquals(save, ((CompareDTO) dto).getSave());
        assertEquals(numThreads!=null, ((CompareDTO)dto).isUseThreadPool());
    }

    @Test
    /*test for parsing a valid compare command with no flags*/
    public void testCompareNoFlags() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, false, null, null);
    }

    @Test
    /*test for parsing a valid compare command with save flag*/
    public void testCompareSaveFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String output = "out";
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, output, true, null, null);
    }

    @Test
    /*test for parsing a compare command with save flag but no filename*/
    public void testCompareSaveFlagNoValueEndOfCommand() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";

        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, true, null, null);
    }

    @Test
    /*test for parsing a compare command with save flag but no filename and followed by another token*/
    public void testCompareSaveFlagNoValue() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        int numThreads = 2;

        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, true, numThreads, null);
    }

    @Test
    /*test for parsing a valid compare command with comparison metric flag*/
    public void testCompareMetricFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String metric = "out";

        compareHelper(file1, file2, dataRepresentation, metric, null,
                null, null, true, null, null);
    }

    @Test
    /*test for parsing a compare command with metric flag but no provided metric*/
    public void testCompareMetricFlagNoValueEndOfCommand(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -m");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No metric specified after pairwise flag.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a compare command with metric flag but no provided metric*/
    public void testCompareMetricFlagNoValue(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -m -s out.txt");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No metric specified after pairwise flag.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a valid compare command with aggregation metric flag*/
    public void testCompareAggregationFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String metric = "out";
        compareHelper(file1, file2, dataRepresentation, null, new String[]{metric},
                null, null, false, null, null);
    }

    @Test
    /*test for parsing a valid compare command with aggregation metric flag*/
    public void testCompareMultipleAggregationsFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String metric = "out";
        compareHelper(file1, file2, dataRepresentation, null, new String[]{metric, metric, metric},
                null, null, false, null, null);
    }

    @Test
    /*test for parsing a compare command with aggregation flag but no provided method*/
    public void testCompareAggregationFlagNoValueEndOfCommand(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -m metricname -a");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No method specified after aggregation flag.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a compare command with aggregation flag but no provided method*/
    public void testCompareAggregationFlagNoValue(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -a -s out.txt");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No method specified after aggregation flag.", e.getErrorMessage());
        }
    }

    /* *******************
     * Report Formatting *
     ********************/

    /**
     * Test for parsing a valid compare command with report format flag.
     * @throws InvalidCommandException when the command is constructed incorrectly.
     */
    @Test
    public void testCompareReportFormatFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, false, null, new String[]{"RawResults"});
    }

    /**
     * Test for parsing a valid compare command with report format flag and bad formats.
     * @throws InvalidCommandException when the command is constructed incorrectly.
     */
    @Test
    public void testCompareInvalidReportFormat() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, false, null, new String[]{"notAFormat"});
    }

    /**
     * Test for parsing a valid compare command with report format flag
     * @throws InvalidCommandException when the command is constructed incorrectly.
     */
    @Test
    public void testCompareMultipleReportFormatsFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, false, null, new String[]{"JSON", "XML", "Pretty", "RawResults"});
    }

    /**
     * Test for parsing a compare command with report format flag but no provided method.
     */
    @Test
    public void testCompareReportFormatFlagNoValueEndOfCommand(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -m metricname -r");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No method specified after report format flag.", e.getErrorMessage());
        }
    }

    /**
     * Test for parsing a compare command with report format flag but no provided method.
     */
    @Test
    public void testCompareReportFormatFlagNoValue(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -r -s out.txt");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No method specified after report format flag.", e.getErrorMessage());
        }
    }

    /* ************
     * Delimiters *
    **************/

    @Test
    /*test for parsing a valid compare command with delimiter flag*/
    public void testCompareDelimiterFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        compareHelper(file1, file2, dataRepresentation, null, null,
                delimiter, null, false, null, null);
    }

    @Test
    /*test for parsing a compare command with delimiter flag but no provided delimiter*/
    public void testCompareDelimiterFlagNoValueEndOfCommand(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -m metricname -d");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No delimiter specified after flag.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a compare command with delimiter flag but no provided delimiter*/
    public void testCompareDelimiterFlagNoValue(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -d -m metricname");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No delimiter specified after flag.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a valid compare command with numThreads flag*/
    public void testCompareNumThreadFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        Integer numThreads = 4;
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, false, numThreads, null);
    }

    @Test
    /*test for parsing a compare command with numThread flag but no provided value*/
    public void testCompareNumThreadFlagNoValueEndOfCommand() throws InvalidCommandException {
        String file1 = "file1";
        String dataRepresentation = "CSV";

        DataTransferObject dto = input.parse("compare " + file1 + " " + dataRepresentation + " -m metricname -t");
        assertTrue(((CompareDTO)dto).isUseThreadPool());
        assertNull(((CompareDTO)dto).getNumberOfThreads());
    }

    @Test
    /*test for parsing a compare command with numThread flag but no provided value */
    public void testCompareNumThreadFlagNoValue() throws InvalidCommandException {
        String file1 = "file1";
        String dataRepresentation = "CSV";

        DataTransferObject dto = input.parse("compare " + file1 + " " + dataRepresentation + " -t -m metricname");
        assertTrue(((CompareDTO)dto).isUseThreadPool());
        assertNull(((CompareDTO)dto).getNumberOfThreads());
        assertEquals(((CompareDTO)dto).getComparisonMethod(), "metricname");
    }

    @Test
    /*test for parsing a compare command with numThread flag but the provided value is not a number*/
    public void testCompareNumThreadFlagNotNumber(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -t twelve -m metricname");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Value specified after number of threads flag is not a number or flag.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a valid compare command with many flags*/
    public void testCompareSeveralFlags() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        String metric = "out";
        Integer numThreads = 4;
        compareHelper(file1, file2, dataRepresentation, metric, null,
                delimiter, null, false, numThreads, null);
    }

    @Test
    /*test for parsing a valid compare command with every flag specified*/
    public void testCompareEveryFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        String metric = "out";
        Integer numThreads = 4;
        String outputFile = "out.txt";
        compareHelper(file1, file2, dataRepresentation, metric, new String[]{metric},
                delimiter, outputFile, true, numThreads, null);
    }

    @Test
    /*test for parsing a valid compare command with many flags, reordered to ensure that only the helper flag ordering works*/
    public void testCompareSeveralFlagsReordered() throws InvalidCommandException {
        String file1 = "file1";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        String metric = "out";
        Integer numThreads = 4;

        String command = "compare " + file1 + " " + dataRepresentation + " -t " +
                numThreads + " -m " + metric + " -s -d " + delimiter;

        DataTransferObject dto = input.parse(command);
        assertEquals(dto.getCommandType(), CommandType.Compare);
        assertEquals(file1, ((CompareDTO)dto).getTestCaseLocationOne());
        assertNull(((CompareDTO) dto).getTestCaseLocationTwo());
        assertEquals(dataRepresentation, ((CompareDTO)dto).getDataRepresentation());
        assertEquals(metric, ((CompareDTO)dto).getComparisonMethod());
        assertNull(((CompareDTO) dto).getAggregationMethods());
        assertEquals(delimiter, ((CompareDTO)dto).getDelimiter());
        assertNull(((CompareDTO) dto).getOutputFilename());
        assertEquals(numThreads, ((CompareDTO)dto).getNumberOfThreads());
        assertTrue(((CompareDTO) dto).getSave());
    }

    @Test
    /*test for parsing a compare command that is missing a data representation*/
    public void testCompareTooShort() {
        try {
            input.parse("compare file1");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No test case(s) or data representation specified.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a compare command that is missing filenames and data representation*/
    public void testCompareWayTooShort() {
        try {
            input.parse("compare");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No test case(s) or data representation specified.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a compare command with an invalid flag*/
    public void testCompareInvalidFlag(){
        try {
            input.parse("compare file1 file2 CSV -t 12 -x banana -m metricname");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Unrecognized token '-x'.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a compare command with an invalid token in the middle of flags*/
    public void testCompareInvalidTokenAfterFlags(){
        try {
            input.parse("compare file1 file2 CSV -t 12 -m metric name -d ***");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Unrecognized token 'name'.", e.getErrorMessage());
        }
    }

    @Test
    /*test for parsing a compare command with too many filenames listed*/
    public void testCompareTooManyTokensBeforeFlags(){
        try {
            input.parse("compare file1 file2 file3 CSV -t 12 -m metric -d ***");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Wrong number of filenames or data representation set.", e.getErrorMessage());
        }
    }
}
