package model;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CompareDTOTest {
    private CommandType compareCommand = CommandType.Compare;
    private String aggregationMethod = "AggregationMethod";
    private String dataRepresentation = "DataRepresentation";
    private String delimiter = ",";
    private Integer numberOfThreads = 1;
    private String outputFileName = "output.txt";
    private String pairwiseMethod = "PairwiseMethod";
    private String testCaseLocationOne = "Location/test1";
    private String testCaseLocationTwo = "Location/test2";
    private boolean useThreadPool = true;

    @Test
    /**
     * test setting all the variables in the CompareDTO and check it has properly set CommandType
     */
    public void testCompareDTO(){
        CompareDTO compare = new CompareDTO();
        compare.setAggregationMethods(new String[]{aggregationMethod});
        compare.setDataRepresentation(dataRepresentation);
        compare.setDelimiter(delimiter);
        compare.setNumberOfThreads(numberOfThreads);
        compare.setOutputFilename(outputFileName);
        compare.setComparisonMethod(pairwiseMethod);
        compare.setTestCaseLocationOne(testCaseLocationOne);compare.setTestCaseLocationTwo(testCaseLocationTwo);
        compare.setUseThreadPool(useThreadPool);

        assertEquals("CommandType should be " + compareCommand + ", but is: " + compare.getCommandType(),
                compare.getCommandType(), compareCommand);
        assertEquals("AggregationMethod should be " + aggregationMethod + ", but is: " + compare.getAggregationMethods()[0],
                compare.getAggregationMethods()[0], aggregationMethod);
        assertEquals("DataRepresentation should be " + dataRepresentation + ", but is: " + compare.getDataRepresentation(),
                compare.getDataRepresentation(), dataRepresentation);
        assertEquals("Delimiter should be " + delimiter + ", but is: " + compare.getDelimiter(),
                compare.getDelimiter(), delimiter);
        assertEquals("NumberOfThreads should be " + numberOfThreads + ", but is: " + compare.getNumberOfThreads(),
                compare.getNumberOfThreads(), numberOfThreads);
        assertEquals("OutputFileName should be " + outputFileName + ", but is: " + compare.getOutputFilename(),
                compare.getOutputFilename(), outputFileName);
        assertEquals("PairwiseMethod should be " + pairwiseMethod + ", but is: " + compare.getComparisonMethod(),
                compare.getComparisonMethod(), pairwiseMethod);
        assertEquals("TestCaseLocationOne should be " + testCaseLocationOne + ", but is: " + compare.getTestCaseLocationOne(),
                compare.getTestCaseLocationOne(), testCaseLocationOne);
        assertEquals("TestCaseLocationTwo should be " + testCaseLocationTwo + ", but is: " + compare.getTestCaseLocationTwo(),
                compare.getTestCaseLocationTwo(), testCaseLocationTwo);
        assertTrue(compare.isUseThreadPool());
    }

    @Test
    /*test setting no variables in the CompareDTO and check it has properly set CommandType*/
    public void testEmptyCompareDTO(){
        CompareDTO compareEmpty = new CompareDTO();

        assertEquals("CommandType should be " + compareCommand + ", but is: " + compareEmpty.getCommandType(), compareEmpty.getCommandType(), compareCommand);
        assertNull("AggregationMethod should be null, but is: " + Arrays.toString(compareEmpty.getAggregationMethods()), compareEmpty.getAggregationMethods());
        assertNull("DataRepresentation should be null, but is: " + compareEmpty.getDataRepresentation(), compareEmpty.getDataRepresentation());
        assertNull("Delimiter should be null, but is: " + compareEmpty.getDelimiter(), compareEmpty.getDelimiter());
        assertNull("NumberOfThreads should be null, but is: " + compareEmpty.getNumberOfThreads(), compareEmpty.getNumberOfThreads());
        assertNull("OutputFileName should be null, but is: " + compareEmpty.getOutputFilename(), compareEmpty.getOutputFilename());
        assertNull("PairwiseMethod should be null, but is: " + compareEmpty.getComparisonMethod(), compareEmpty.getComparisonMethod());
        assertNull("TestCaseLocationOne should be null, but is: " + compareEmpty.getTestCaseLocationOne(), compareEmpty.getTestCaseLocationOne());
        assertNull("TestCaseLocationTwo should be null, but is: " + compareEmpty.getTestCaseLocationTwo(), compareEmpty.getTestCaseLocationTwo());
        assertFalse(compareEmpty.isUseThreadPool());
    }
}
