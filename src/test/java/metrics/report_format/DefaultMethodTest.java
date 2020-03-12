package metrics.report_format;

import model.CompareDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test the default methods specified in the ReportFormat interface.
 *
 * @author crushton
 */
public class DefaultMethodTest {

    private ReportFormat format = new RawResults();

    /**
     * Tests that the host name is returned.
     */
    @Test
    public void testGetHostName() {
        assertNotNull("Host name not returned from default method.", format.getHost());
    }

    /**
     * Tests that the user name is returned.
     */
    @Test
    public void testGetUserName() {
        assertNotNull("User name not returned from default method.", format.getUser());
    }

    /**
     * Tests that the date and time is returned.
     */
    @Test
    public void testGetDateTime() {
        assertNotNull("Date/time not returned from default method.", format.getDateTime());
    }

    /**
     * Ensure that all run parameters are returned when given.
     */
    @Test
    public void testGetAllRunParameters() {
        CompareDTO dto = new CompareDTO();
        dto.setComparisonMethod("comparisonMethodValue");
        dto.setAggregationMethods(new String[]{"method1"});
        dto.setSave(false);
        dto.setOutputFilename("output");
        dto.setTestCaseLocationOne("tcl1");
        dto.setTestCaseLocationTwo("tcl2");
        dto.setReportFormats(new String[]{"reportformat1"});
        dto.setNumberOfThreads(2);
        dto.setDataRepresentation("dr");
        dto.setUseThreadPool(true);
        dto.setDelimiter("delim");
        Map<String, String> runParams = format.getRunParameters(dto);
        assertEquals("Correct 'comparisonMethod' run parameter not returned from default method.", "comparisonMethodValue", runParams.get("comparisonMethod"));
        assertEquals("Correct 'aggregationMethods' run parameter not returned from default method.", "[method1]", runParams.get("aggregationMethods"));
        assertEquals("Correct 'save' run parameter not returned from default method.", "false", runParams.get("save"));
        assertEquals("Correct 'outputFilename' run parameter not returned from default method.", "output", runParams.get("outputFilename"));
        assertEquals("Correct 'testCaseLocationOne' run parameter not returned from default method.", "tcl1", runParams.get("testCaseLocationOne"));
        assertEquals("Correct 'testCaseLocationTwo' run parameter not returned from default method.", "tcl2", runParams.get("testCaseLocationTwo"));
        assertEquals("Correct 'reportFormats' run parameter not returned from default method.", "[reportformat1]", runParams.get("reportFormats"));
        assertEquals("Correct 'numberOfThreads' run parameter not returned from default method.", "2", runParams.get("numberOfThreads"));
        assertEquals("Correct 'dataRepresentation' run parameter not returned from default method.", "dr", runParams.get("dataRepresentation"));
        assertEquals("Correct 'useThreadPool' run parameter not returned from default method.", "true", runParams.get("useThreadPool"));
        assertEquals("Correct 'delimiter' run parameter not returned from default method.", "delim", runParams.get("delimiter"));
    }

    /**
     * Ensure some run parameters are not returned when not given.
     */
    @Test
    public void testMissingSomeRunParameters() {
        CompareDTO dto = new CompareDTO();
        dto.setComparisonMethod("comparisonMethodValue");
        // Missing aggregation methods
        dto.setSave(false);
        dto.setOutputFilename("output");
        dto.setTestCaseLocationOne("tcl1");
        // Missing test case location two
        dto.setReportFormats(new String[]{"reportformat1"});
        dto.setNumberOfThreads(2);
        dto.setDataRepresentation("dr");
        dto.setUseThreadPool(true);
        dto.setDelimiter("delim");
        Map<String, String> runParams = format.getRunParameters(dto);
        assertEquals("Correct 'comparisonMethod' run parameter not returned from default method.", "comparisonMethodValue", runParams.get("comparisonMethod"));
        assertNull("'aggregationMethods' run parameter returned from default method when not defined.", runParams.get("aggregationMethods"));
        assertEquals("Correct 'save' run parameter not returned from default method.", "false", runParams.get("save"));
        assertEquals("Correct 'outputFilename' run parameter not returned from default method.", "output", runParams.get("outputFilename"));
        assertEquals("Correct 'testCaseLocationOne' run parameter not returned from default method.", "tcl1", runParams.get("testCaseLocationOne"));
        assertNull("'testCaseLocationTwo' run parameter returned from default method when not defined.", runParams.get("testCaseLocationTwo"));
        assertEquals("Correct 'reportFormats' run parameter not returned from default method.", "[reportformat1]", runParams.get("reportFormats"));
        assertEquals("Correct 'numberOfThreads' run parameter not returned from default method.", "2", runParams.get("numberOfThreads"));
        assertEquals("Correct 'dataRepresentation' run parameter not returned from default method.", "dr", runParams.get("dataRepresentation"));
        assertEquals("Correct 'useThreadPool' run parameter not returned from default method.", "true", runParams.get("useThreadPool"));
        assertEquals("Correct 'delimiter' run parameter not returned from default method.", "delim", runParams.get("delimiter"));
    }

    /**
     * Ensure that thread data isn't printed when not in use.
     */
    @Test
    public void testNoThreadDataWhenNotInUse() {
        CompareDTO dto = new CompareDTO();
        dto.setNumberOfThreads(2);
        dto.setUseThreadPool(false);

        Map<String, String> runParams = format.getRunParameters(dto);
        assertNull("'numberOfThreads' run parameter returned from default method when usethreadpool=false.", runParams.get("numberOfThreads"));
        assertEquals("'useThreadPool' run parameter not returned from default method.", "false", runParams.get("useThreadPool"));
    }

    /**
     * Test that we get the aggregated values paired with the right aggregation methods.
     */
    @Test
    public void testGetAggregations() {
        CompareDTO dto = new CompareDTO();
        dto.setAggregationMethods(new String[]{"method1", "method2"});

        List<Double> aggregations1 = new ArrayList<>();
        aggregations1.add(1d);
        List<Double> aggregations2 = new ArrayList<>();
        aggregations2.add(2d);
        List<List<Double>> aggregations = new ArrayList<>();
        aggregations.add(aggregations1);
        aggregations.add(aggregations2);

        Map<String, List<Double>> methodAggregationMap = format.getAggregations(dto, aggregations);
        assertEquals("Aggregation value one doesn't correspond to aggregation method one.", Arrays.asList(1d), methodAggregationMap.get("method1"));
        assertEquals("Aggregation value two doesn't correspond to aggregation method two.", Arrays.asList(2d), methodAggregationMap.get("method2"));
    }

    /**
     * Tests that the report header is returned.
     */
    @Test
    public void testGetReportHeader() {
        assertNotNull("Report header was not returned.", format.getReportHeader());
    }

}
