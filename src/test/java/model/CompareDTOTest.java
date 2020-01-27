package model;

import org.junit.Test;
import java.util.Objects;
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

    @Test
    /**
     * test setting all the variables in the CompareDTO and check it has properly set CommandType
     */
    public void testCompareDTO(){
        CompareDTO compare = new CompareDTO();
        compare.setAggregationMethod(aggregationMethod);
        compare.setDataRepresentation(dataRepresentation);
        compare.setDelimiter(delimiter);
        compare.setNumberOfThreads(numberOfThreads);
        compare.setOutputFilename(outputFileName);
        compare.setPairwiseMethod(pairwiseMethod);
        compare.setTestCaseLocationOne(testCaseLocationOne);compare.setTestCaseLocationTwo(testCaseLocationTwo);

        assertTrue("CommandType should be "+ compareCommand +", but is: " + compare.getCommandType(),Objects.equals(compare.getCommandType(), compareCommand));
        assertTrue("AggregationMethod should be " + aggregationMethod + ", but is: " + compare.getAggregationMethod(), Objects.equals(compare.getAggregationMethod(), aggregationMethod));
        assertTrue("DataRepresentation should be " + dataRepresentation + ", but is: " + compare.getDataRepresentation(), Objects.equals(compare.getDataRepresentation(), dataRepresentation));
        assertTrue("Delimiter should be " + delimiter + ", but is: " + compare.getDelimiter(), Objects.equals(compare.getDelimiter(), delimiter));
        assertEquals("NumberOfThreads should be " + numberOfThreads + ", but is: " + compare.getNumberOfThreads(),compare.getNumberOfThreads(), numberOfThreads);
        assertTrue("OutputFileName should be " + outputFileName + ", but is: " + compare.getOutputFilename(), Objects.equals(compare.getOutputFilename(), outputFileName));
        assertTrue("PairwiseMethod should be " + pairwiseMethod + ", but is: " + compare.getPairwiseMethod(), Objects.equals(compare.getPairwiseMethod(), pairwiseMethod));
        assertTrue("TestCaseLocationOne should be " + testCaseLocationOne + ", but is: " + compare.getTestCaseLocationOne(), Objects.equals(compare.getTestCaseLocationOne(), testCaseLocationOne));
        assertTrue("TestCaseLocationTwo should be " + testCaseLocationTwo + ", but is: " + compare.getTestCaseLocationTwo(), Objects.equals(compare.getTestCaseLocationTwo(), testCaseLocationTwo));
    }

    @Test
    /**
     * test setting no variables in the CompareDTO and check it has properly set CommandType
     */
    public void testEmptyCompareDTO(){
        CompareDTO compareEmpty = new CompareDTO();

        assertTrue("CommandType should be "+ compareCommand +", but is: " + compareEmpty.getCommandType(),Objects.equals(compareEmpty.getCommandType(), compareCommand));
        assertNull("AggregationMethod should be null, but is: " + compareEmpty.getAggregationMethod(), compareEmpty.getAggregationMethod());
        assertNull("DataRepresentation should be null, but is: " + compareEmpty.getDataRepresentation(), compareEmpty.getDataRepresentation());
        assertNull("Delimiter should be null, but is: " + compareEmpty.getDelimiter(), compareEmpty.getDelimiter());
        assertNull("NumberOfThreads should be null, but is: " + compareEmpty.getNumberOfThreads(), compareEmpty.getNumberOfThreads());
        assertNull("OutputFileName should be null, but is: " + compareEmpty.getOutputFilename(), compareEmpty.getOutputFilename());
        assertNull("PairwiseMethod should be null, but is: " + compareEmpty.getPairwiseMethod(), compareEmpty.getPairwiseMethod());
        assertNull("TestCaseLocationOne should be null, but is: " + compareEmpty.getTestCaseLocationOne(), compareEmpty.getTestCaseLocationOne());
        assertNull("TestCaseLocationTwo should be null, but is: " + compareEmpty.getTestCaseLocationTwo(), compareEmpty.getTestCaseLocationTwo());
    }
}
