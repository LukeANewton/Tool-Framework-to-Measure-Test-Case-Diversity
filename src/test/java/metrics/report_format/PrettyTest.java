package metrics.report_format;

import model.CompareDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test the Pretty report format.
 *
 * @author crushton
 */
public class PrettyTest {

    ReportFormat prettyFormat;

    @Before
    public void setup() {
        prettyFormat = new Pretty();
    }

    /**
     * Ensures the result is printed correctly.
     */
    @Test
    public void testFormat() {
        final String COMPARISON_METHOD = "comparisonMethod";
        final String AGGREGATION_METHOD_ONE = "aggregationMethod1";
        final String AGGREGATION_METHOD_TWO = "aggregationMethod2";
        final Double AGGREGATED_VALUE_ONE = 1d;
        final Double AGGREGATED_VALUE_TWO = 2d;

        CompareDTO dto = new CompareDTO();
        dto.setComparisonMethod(COMPARISON_METHOD);
        dto.setAggregationMethods(new String[]{AGGREGATION_METHOD_ONE, AGGREGATION_METHOD_TWO});

        List<List<Double>> aggregations = new ArrayList<>();
        aggregations.add(Arrays.asList(AGGREGATED_VALUE_ONE));
        aggregations.add(Arrays.asList(AGGREGATED_VALUE_TWO));

        // Pretty format doesn't use similarities (they are not very pretty).
        String result = prettyFormat.format(dto, null, aggregations);

        assertTrue("Format does not contain the expected header.", result.contains("/*********"));
        assertTrue("Format does not contain the run parameters declaration.", result.contains("Run parameters:"));
        assertTrue("Format does not contain a run parameter.", result.contains(COMPARISON_METHOD));
        assertTrue("Format does not contain a run parameter.", result.contains(AGGREGATION_METHOD_ONE));
        assertTrue("Format does not contain a run parameter.", result.contains(AGGREGATION_METHOD_TWO));
        assertTrue("Format does not contain the results declaration.", result.contains("Results:"));
        assertTrue("Format does not contain a correct result pair.", result.contains(AGGREGATION_METHOD_ONE + ": " + Arrays.asList(AGGREGATED_VALUE_ONE)));
        assertTrue("Format does not contain a correct result pair.", result.contains(AGGREGATION_METHOD_TWO + ": " + Arrays.asList(AGGREGATED_VALUE_TWO)));
    }

    /**
     * Test get description.
     */
    @Test
    public void testDescription() {
        assertNotNull("Unable to get description or Pretty format.", prettyFormat.getDescription());
    }
}
