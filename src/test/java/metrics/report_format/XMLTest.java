package metrics.report_format;

import model.CompareDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests the XML report format.
 * Note: Using an XML parser is beneficial for the future.
 *
 * @author crushton
 */
public class XMLTest {
    ReportFormat XMLFormat;

    @Before
    public void setup() {
        XMLFormat = new XML();
    }

    /**
     * Ensures the result is printed correctly.
     */
    @Test
    public void testFormat() {
        final String COMPARISON_METHOD = "comparisonMethod";
        final String AGGREGATION_METHOD_ONE = "aggregationMethod1";
        final String AGGREGATION_METHOD_TWO = "aggregationMethod2";
        final String AGGREGATED_VALUE_ONE = "aggregatedValue1";
        final String AGGREGATED_VALUE_TWO = "aggregatedValue2";
        final Double SIMILARITY_VALUE_ONE = 1.0;
        final Double SIMILARITY_VALUE_TWO = 53.05;

        CompareDTO dto = new CompareDTO();
        dto.setComparisonMethod(COMPARISON_METHOD);
        dto.setAggregationMethods(new String[]{AGGREGATION_METHOD_ONE, AGGREGATION_METHOD_TWO});

        List<Double> similarities = new ArrayList<>();
        similarities.add(SIMILARITY_VALUE_ONE);
        similarities.add(SIMILARITY_VALUE_TWO);

        List<String> aggregations = new ArrayList<>();
        aggregations.add(AGGREGATED_VALUE_ONE);
        aggregations.add(AGGREGATED_VALUE_TWO);

        final String EXPECTED = "<report>\n" +
                "\t<username> " + XMLFormat.getUser() + " </username>\n" +
                "\t<hostname> " + XMLFormat.getHost() + " </hostname>\n" +
                "\t<datetime> " + XMLFormat.getDateTime() + " </datetime>\n" +
                "\t<parameters>\t\n" +
                "\t\t<usethreadpool> false </usethreadpool>\n" +
                "\t\t<comparisonmethod> " + COMPARISON_METHOD + " </comparisonmethod>\n" +
                "\t\t<aggregationmethods> ["+AGGREGATION_METHOD_ONE+", "+AGGREGATION_METHOD_TWO+"] </aggregationmethods>\n" +
                "\t\t<save> false </save>\n" +
                "\t</parameters>\t\n" +
                "\t<similarities>\t\n" +
                "\t\t<similarity> " + SIMILARITY_VALUE_ONE + " </similarity>\n" +
                "\t\t<similarity> " + SIMILARITY_VALUE_TWO + " </similarity>\n" +
                "\t</similarities>\t\n" +
                "\t<results>\t\n" +
                "\t\t<"+AGGREGATION_METHOD_TWO.toLowerCase()+"> " + AGGREGATED_VALUE_TWO + " </"+AGGREGATION_METHOD_TWO.toLowerCase()+">\n" +
                "\t\t<"+AGGREGATION_METHOD_ONE.toLowerCase()+"> " + AGGREGATED_VALUE_ONE + " </"+AGGREGATION_METHOD_ONE.toLowerCase()+">\n" +
                "\t</results>\t\n" +
                "</report>" + System.lineSeparator();

        String result = XMLFormat.format(dto, similarities, aggregations);
        assertTrue("Format does not contain the <username> tag.", result.contains("<username>") && result.contains("</username"));
        assertTrue("Format does not contain the <hostname> tag.", result.contains("<hostname>") && result.contains("</hostname"));
        assertTrue("Format does not contain the <datetime> tag.", result.contains("<datetime>") && result.contains("</datetime"));
        assertEquals("Result is not what was expected.", EXPECTED.replaceAll("\\n|\\r\\n", System.lineSeparator()),
                result.replaceAll("\\n|\\r\\n", System.lineSeparator()));
    }

    @Test
    public void testOverSimilarityThreshold() {
        final String COMPARISON_METHOD = "comparisonMethod";
        final String AGGREGATION_METHOD_ONE = "aggregationMethod1";
        final String AGGREGATION_METHOD_TWO = "aggregationMethod2";
        final String AGGREGATED_VALUE_ONE = "aggregatedValue1";
        final String AGGREGATED_VALUE_TWO = "aggregatedValue2";

        CompareDTO dto = new CompareDTO();
        dto.setComparisonMethod(COMPARISON_METHOD);
        dto.setAggregationMethods(new String[]{AGGREGATION_METHOD_ONE, AGGREGATION_METHOD_TWO});

        List<String> aggregations = new ArrayList<>();
        aggregations.add(AGGREGATED_VALUE_ONE);
        aggregations.add(AGGREGATED_VALUE_TWO);

        int numSimilarities = XMLFormat.SIMILARITY_THRESHOLD + 1;
        List<Double> similarities = new ArrayList<>(Collections.nCopies(numSimilarities, 0.0));
        String result = XMLFormat.format(dto, similarities, aggregations);
        assertFalse("Result contains similarities when it's over the threshold of " + XMLFormat.SIMILARITY_THRESHOLD + ".", result.contains("<similarities>"));
    }

    /**
     * Test get description.
     */
    @Test
    public void testDescription() {
        assertNotNull("Unable to get description of XML format.", XMLFormat.getDescription());
    }
}
