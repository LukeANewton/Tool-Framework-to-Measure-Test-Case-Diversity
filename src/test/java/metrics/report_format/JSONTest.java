package metrics.report_format;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import model.CompareDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests the JSONFormat class.
 *
 * @author crushton
 */
public class JSONTest {

    private ReportFormat jsonFormat;
    final Double AGGREGATED_VALUE_ONE = 1d;
    final Double AGGREGATED_VALUE_TWO = 2d;

    @Before
    public void setup() {
        jsonFormat = new JSON();
    }

    /**
     * Test all values are present in the json when specified.
     */
    @Test
    public void testCreateJson() {
        CompareDTO dto = new CompareDTO();
        dto.setComparisonMethod("comparisonmethod");
        dto.setAggregationMethods(new String[]{"aggregationmethod1", "aggregationmethod2"});

        List<Double> similarities = new ArrayList<>();
        similarities.add(1.0);
        similarities.add(53.05);

        List<List<Double>> aggregations = new ArrayList<>();
        aggregations.add(Arrays.asList(AGGREGATED_VALUE_ONE));
        aggregations.add(Arrays.asList(AGGREGATED_VALUE_TWO));

        String result = jsonFormat.format(dto, similarities, aggregations);
        JsonElement json = JsonParser.parseString(result);
        assertTrue("Result is not a readable JSON.", json.isJsonObject());
        assertTrue("No 'username' parameter found.", json.getAsJsonObject().has("username"));
        assertTrue("No 'hostname' parameter found.", json.getAsJsonObject().has("hostname"));
        assertTrue("No 'datetime' parameter found.", json.getAsJsonObject().has("datetime"));
        assertTrue("No 'parameters' parameter found.", json.getAsJsonObject().has("parameters"));
        assertTrue("No 'comparisonmethod' parameter found in 'parameters'.", json.getAsJsonObject().getAsJsonObject("parameters").has("comparisonmethod"));
        assertEquals("No comparison method value found in 'parameters'.", "comparisonmethod", json.getAsJsonObject().getAsJsonObject("parameters").get("comparisonmethod").getAsString());
        assertEquals("No aggregation method values found in 'parameters'.", "[aggregationmethod1, aggregationmethod2]", json.getAsJsonObject().getAsJsonObject("parameters").get("aggregationmethods").getAsString());
        assertTrue("No 'similarities' parameter found.", json.getAsJsonObject().has("similarities"));
        assertTrue("No 'results' parameter found.", json.getAsJsonObject().has("results"));
        assertTrue("No 'aggregationmethod1' parameter found in results.", json.getAsJsonObject().getAsJsonObject("results").has("aggregationmethod1"));
        assertEquals("No first aggregated value found in results.", "[" + AGGREGATED_VALUE_ONE + "]", json.getAsJsonObject().getAsJsonObject("results").get("aggregationmethod1").getAsString());
        assertTrue("No 'aggregationmethod2' parameter found in results.", json.getAsJsonObject().getAsJsonObject("results").has("aggregationmethod2"));
        assertEquals("No second aggregated value found in results.", "[" + AGGREGATED_VALUE_TWO + "]", json.getAsJsonObject().getAsJsonObject("results").get("aggregationmethod2").getAsString());
    }

    /**
     * Test that similarity values are only printed when they are lower than the threshold.
     */
    @Test
    public void testCreateJsonWithoutSimilarities() {
        CompareDTO dto = new CompareDTO();
        int numSimilarities = jsonFormat.SIMILARITY_THRESHOLD + 1;
        List<Double> similarities = new ArrayList<>(Collections.nCopies(numSimilarities, 0.0));
        List<List<Double>> aggregations = new ArrayList<>();

        String result = jsonFormat.format(dto, similarities, aggregations);
        JsonElement json = JsonParser.parseString(result);
        assertTrue("Result is not a readable JSON.", json.isJsonObject());
        assertFalse("'similarities' parameter found when we exceed threshold of " + jsonFormat.SIMILARITY_THRESHOLD + " similarities .", json.getAsJsonObject().has("similarities"));
    }

    /**
     * Test that the object returns a description.
     */
    @Test
    public void testDescription() {
        assertNotNull("Unable to get description of JSON format.", jsonFormat.getDescription());
    }
}
