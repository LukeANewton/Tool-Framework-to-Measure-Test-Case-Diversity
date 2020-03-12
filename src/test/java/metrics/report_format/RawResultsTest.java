package metrics.report_format;

import model.CompareDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the RawResults report format type.
 *
 * @author crushton
 */
public class RawResultsTest {

    private RawResults rawFormat;

    @Before
    public void setup() {
        rawFormat = new RawResults();
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

        List<Double> similarities = new ArrayList<>();
        similarities.add(1.0);

        String result = rawFormat.format(dto, similarities, aggregations);

        assertEquals("Format is not what was expected.",
                "[" + AGGREGATED_VALUE_ONE + "]" + System.lineSeparator() + "[" + AGGREGATED_VALUE_TWO + "]" + System.lineSeparator(),
                result);
    }

    /**
     * Test get description.
     */
    @Test
    public void testDescription() {
        assertNotNull("Unable to get description or Raw format.", rawFormat.getDescription());
    }
}
