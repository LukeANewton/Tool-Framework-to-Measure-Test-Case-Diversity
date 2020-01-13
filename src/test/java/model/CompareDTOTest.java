package model;

import metrics.aggregation.AggregationStrategy;
import metrics.aggregation.AverageValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;

public class CompareDTOTest {

    private CompareDTO compare;
    private String aggregationMethod = "AggregationMethod";
    private String dataRepresentation = "DataRepresentation";
    private String delimiter = ",";
    private int NumberOfThreads = 1;
    private String outputFileName = "output.txt";
    private String pairwiseMethod = "PairwiseMethod";
    private String testCaseLocationOne = "Location/test1";
    private String testCaseLocationTwo = "Location/test2";

    @Before
    public void setup(){
        compare = new CompareDTO();
        compare.setAggregationMethod(aggregationMethod);
        compare.setDataRepresentation(dataRepresentation);
        compare.setDelimiter(delimiter);
        compare.setNumberOfThreads(NumberOfThreads);
        compare.setOutputFilename(outputFileName);
        compare.setPairwiseMethod(pairwiseMethod);
        compare.setTestCaseLocationOne(testCaseLocationOne);
        compare.setTestCaseLocationTwo(testCaseLocationTwo);
    }

    @Test
    public void testCompareDTO(){
        assertTrue("The Command type should be Compare",Objects.equals(compare.getCommandType(), CommandType.Compare));
        assertTrue("The AggregationMethod should be " + aggregationMethod, Objects.equals(compare.getAggregationMethod(), aggregationMethod));
        assertTrue("The DataRepresentation should be " + dataRepresentation, Objects.equals(compare.getDataRepresentation(), dataRepresentation));
    }
}
