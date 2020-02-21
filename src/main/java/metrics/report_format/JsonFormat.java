package metrics.report_format;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.CompareDTO;

import java.util.List;
import java.util.Map;

/**
 * Json report format.
 *
 * @author crushton
 */
public class JsonFormat implements ReportFormat {
    /**
     * Formats the similarity values, aggregations of the similarity values and data from the DTO into a convenient format.
     *
     * @param dto          The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    @Override
    public String format(CompareDTO dto, List<Double> similarities, List<String> aggregations) {
        JsonObject json = new JsonObject();
        json.addProperty("username", getUser());
        json.addProperty("hostname", getHost());
        json.addProperty("datetime", getDateTime());

        JsonObject runParamsJson = new JsonObject();
        Map<String, String> runParamPairs = getRunParameters(dto);
        for (String paramKey : runParamPairs.keySet()) {
            runParamsJson.addProperty(paramKey.toLowerCase(), runParamPairs.get(paramKey));
        }
        json.add("parameters", runParamsJson);

        if (similarities.size() < SIMILARITY_THRESHOLD) {
            JsonArray similaritiesArray = new JsonArray();
            for (Double similarity : similarities) {
                similaritiesArray.add(similarity.toString());
            }
            json.add("similarities", similaritiesArray);
        }

        JsonObject aggregationsJson = new JsonObject();
        Map<String, String> aggregationPairs = getAggregations(dto, aggregations);
        for (String aggregationKey : aggregationPairs.keySet()) {
            aggregationsJson.addProperty(aggregationKey.toLowerCase(), runParamPairs.get(aggregationKey));
        }
        json.add("results", aggregationsJson);

        return json.toString();
    }

    /**
     * Provides a description of the format
     *
     * @return a brief description of the format
     */
    @Override
    public String getDescription() {
        return "Prints user info, run parameters, similarity values (up to " + SIMILARITY_THRESHOLD + "), and the results in JSON format.";
    }
}
