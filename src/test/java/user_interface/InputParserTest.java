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
    /**test for parsing unrecognized command*/
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
            case AggregationMethod:
                command.append(" -a");
                break;
            case DataRepresentation:
                command.append(" -f");
                break;
            case PairwiseMetric:
                command.append(" -m");
                break;
            case Command:
                break;
        }

        DataTransferObject dto = input.parse(command.toString());
        assertEquals(dto.getCommandType(), CommandType.Help);
        assertEquals(((HelpDTO) dto).getHelpType(), helpType);
    }

    @Test
    /**test for parsing command help*/
    public void testCommandHelp() throws InvalidCommandException {
        helpHelper(HelpType.Command);
    }

    @Test
    /**test for parsing aggregation method help*/
    public void testAggregationHelp() throws InvalidCommandException {
        helpHelper(HelpType.AggregationMethod);
    }

    @Test
    /**test for parsing comparison metric help*/
    public void testPairwiseHelp() throws InvalidCommandException {
        helpHelper(HelpType.PairwiseMetric);
    }

    @Test
    /**test for parsing data representation help*/
    public void testDataRepresentationHelp() throws InvalidCommandException {
        helpHelper(HelpType.DataRepresentation);
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing an invalid help with extra tokens*/
    public void testHelpExtraTokens() throws InvalidCommandException {
        input.parse("help -f -m");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing an invalid help with invalid type*/
    public void testHelpInvalidType() throws InvalidCommandException {
        input.parse("help -x");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with no parameter name or value specified*/
    public void testConfigNoSpecification() throws InvalidCommandException {
        input.parse("config");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with no parameter value specified*/
    public void testConfigNoValueSet() throws InvalidCommandException {
        input.parse("config banana");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with extra tokens*/
    public void testConfigExtraTokensShort() throws InvalidCommandException {
        input.parse("config numThreads 4 banana");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with extra tokens*/
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
    /**test for parsing a valid config command without save option*/
    public void testConfigValidSyntax() throws InvalidCommandException {
        String name = "numThreads";
        String value = "4";
        configHelper(name, value,false);
    }

    @Test
    /**test for parsing a syntactically valid config command that is semantically invalid.
     * the parser should not know anything about what a valid name/value is, so this should
     * not throw an InvalidCommandException*/
    public void testConfigValidSyntaxInvalidSemantics() throws InvalidCommandException {
        String name = "banana";
        String value = "orange";
        configHelper(name, value,false);
    }

    @Test
    /**test for parsing a valid config command with save option*/
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
     * @throws InvalidCommandException when the parameters specified result in an invalid command
     */
    private void compareHelper(String file1, String file2, String dataRepresentation, String comparisonMethod,
                               String aggregationMethod, String delimiter, String outputFilename,
                               Integer numThreads) throws InvalidCommandException {
        StringBuilder command = new StringBuilder();
        command.append("compare").append(" ").append(file1).append(" ").append(file2).append(" ").append(dataRepresentation);
        if(comparisonMethod != null)
            command.append(" -m ").append(comparisonMethod);
        if(aggregationMethod != null)
            command.append(" -a ").append(aggregationMethod);
        if(delimiter != null)
            command.append(" -d ").append(delimiter);
        if(outputFilename != null)
            command.append(" -s ").append(outputFilename);
        if(numThreads != null)
            command.append(" -t ").append(numThreads);

        DataTransferObject dto = input.parse(command.toString());
        assertEquals(dto.getCommandType(), CommandType.Compare);
        assertEquals(file1, ((CompareDTO)dto).getTestCaseLocationOne());
        assertEquals(file2, ((CompareDTO)dto).getTestCaseLocationTwo());
        assertEquals(dataRepresentation, ((CompareDTO)dto).getDataRepresentation());
        assertEquals(comparisonMethod, ((CompareDTO)dto).getPairwiseMethod());
        assertEquals(aggregationMethod, ((CompareDTO)dto).getAggregationMethod());
        assertEquals(delimiter, ((CompareDTO)dto).getDelimiter());
        assertEquals(outputFilename, ((CompareDTO)dto).getOutputFilename());
        assertEquals(numThreads, ((CompareDTO)dto).getNumberOfThreads());
    }

    @Test
    /**test for parsing a valid compare command with no flags*/
    public void testCompareNoFlags() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, null);
    }

    @Test
    /**test for parsing a valid compare command with save flag*/
    public void testCompareSaveFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String output = "out";
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, output, null);
    }

    @Test
    /**test for parsing a compare command with save flag but no filename*/
    public void testCompareSaveFlagNoValueEndOfCommand(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -s");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No filename specified after save flag.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a compare command with save flag but no filename*/
    public void testCompareSaveFlagNoValue(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -s -m metricname");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No filename specified after save flag.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a valid compare command with comparison metric flag*/
    public void testCompareMetricFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String metric = "out";
        compareHelper(file1, file2, dataRepresentation, metric, null,
                null, null, null);
    }

    @Test
    /**test for parsing a compare command with metric flag but no provided metric*/
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
    /**test for parsing a compare command with metric flag but no provided metric*/
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
    /**test for parsing a valid compare command with aggregation metric flag*/
    public void testCompareAggregationFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String metric = "out";
        compareHelper(file1, file2, dataRepresentation, null, metric,
                null, null, null);
    }

    @Test
    /**test for parsing a compare command with aggregation flag but no provided method*/
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
    /**test for parsing a compare command with aggregation flag but no provided method*/
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

    @Test
    /**test for parsing a valid compare command with delimiter flag*/
    public void testCompareDelimiterFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        compareHelper(file1, file2, dataRepresentation, null, null,
                delimiter, null, null);
    }

    @Test
    /**test for parsing a compare command with delimiter flag but no provided delimiter*/
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
    /**test for parsing a compare command with delimiter flag but no provided delimiter*/
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
    /**test for parsing a valid compare command with numThreads flag*/
    public void testCompareNumThreadFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        Integer numThreads = 4;
        compareHelper(file1, file2, dataRepresentation, null, null,
                null, null, numThreads);
    }

    @Test
    /**test for parsing a compare command with numThread flag but no provided value*/
    public void testCompareNumThreadFlagNoValueEndOfCommand(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -m metricname -t");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No value specified after flag.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a compare command with numThread flag but no provided value*/
    public void testCompareNumThreadFlagNoValue(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -t -m metricname");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No value specified after flag.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a compare command with numThread flag but the provided value is not a number*/
    public void testCompareNumThreadFlagNotNumber(){
        String file1 = "file1";
        String dataRepresentation = "CSV";

        try {
            input.parse("compare " + file1 + " " + dataRepresentation + " -t twelve -m metricname");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Value specified after number of threads flag is not a number.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a valid compare command with many flags*/
    public void testCompareSeveralFlags() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        String metric = "out";
        Integer numThreads = 4;
        compareHelper(file1, file2, dataRepresentation, metric, null,
                delimiter, null, numThreads);
    }

    @Test
    /**test for parsing a valid compare command with every flag specified*/
    public void testCompareEveryFlag() throws InvalidCommandException {
        String file1 = "file1";
        String file2 = "file2";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        String metric = "out";
        Integer numThreads = 4;
        String outputFile = "out.txt";
        compareHelper(file1, file2, dataRepresentation, metric, metric,
                delimiter, outputFile, numThreads);
    }

    @Test
    /**test for parsing a valid compare command with every flag specified and some extra bits*/
    public void testCompareTooLong() {
        try {
            input.parse("compare file1 file2 CSV -m metricname -a aggregation -d *** -t 67 -s out.txt banana republic");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Unexpected additional tokens: [banana, republic]", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a valid compare command with many flags, reordered to ensure that only the helper flag ordering works*/
    public void testCompareSeveralFlagsReordered() throws InvalidCommandException {
        String file1 = "file1";
        String dataRepresentation = "CSV";
        String delimiter = "out";
        String metric = "out";
        Integer numThreads = 4;

        String command = "compare " + file1 + " " + dataRepresentation + " -t " +
                numThreads + " -m " + metric + " -d " + delimiter;

        DataTransferObject dto = input.parse(command);
        assertEquals(dto.getCommandType(), CommandType.Compare);
        assertEquals(file1, ((CompareDTO)dto).getTestCaseLocationOne());
        assertEquals(null, ((CompareDTO)dto).getTestCaseLocationTwo());
        assertEquals(dataRepresentation, ((CompareDTO)dto).getDataRepresentation());
        assertEquals(metric, ((CompareDTO)dto).getPairwiseMethod());
        assertEquals(null, ((CompareDTO)dto).getAggregationMethod());
        assertEquals(delimiter, ((CompareDTO)dto).getDelimiter());
        assertEquals(null, ((CompareDTO)dto).getOutputFilename());
        assertEquals(numThreads, ((CompareDTO)dto).getNumberOfThreads());
    }

    @Test
    /**test for parsing a compare command that is missing a data representation*/
    public void testCompareTooShort() {
        try {
            input.parse("compare file1");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No test case(s) or data representation specified.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a compare command that is missing filenames and data representation*/
    public void testCompareWayTooShort() {
        try {
            input.parse("compare");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("No test case(s) or data representation specified.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a compare command with an invalid flag*/
    public void testCompareInvalidFlag(){
        try {
            input.parse("compare file1 file2 CSV -t 12 -x banana -m metricname");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Unrecognized token '-x'.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a compare command with an invalid token in the middle of flags*/
    public void testCompareInvalidTokenAfterFlags(){
        try {
            input.parse("compare file1 file2 CSV -t 12 -m metric name -d ***");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Unrecognized token 'name'.", e.getErrorMessage());
        }
    }

    @Test
    /**test for parsing a compare command with too many filenames listed*/
    public void testCompareTooManyTokensBeforeFlags(){
        try {
            input.parse("compare file1 file2 file3 CSV -t 12 -m metric -d ***");
            fail();
        }catch(InvalidCommandException e){
            assertEquals("Wrong number of filenames or data representation set.", e.getErrorMessage());
        }
    }
}