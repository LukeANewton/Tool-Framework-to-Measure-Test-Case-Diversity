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
public class JSON implements ReportFormat {
    /**
     * Formats the similarity values, aggregations of the similarity values and data from the DTO into a convenient format.
     *
     * @param dto          The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    @Override
    public String format(CompareDTO dto, List<Double> similarities, List<List<Double>> aggregations) {
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
        Map<String, List<Double>> aggregationPairs = getAggregations(dto, aggregations);
        for (String aggregationKey : aggregationPairs.keySet()) {
            List<Double> aggregationValues = aggregationPairs.get(aggregationKey);
            StringBuilder data = new StringBuilder();
            data.append("[ ");
            for(int i = 0; i < aggregationValues.size(); i++){
                if(i != 0){
                    data.append(", ");
                }
                data.append(aggregationValues.get(i));
            }
            data.append(" ]");
            aggregationsJson.addProperty(aggregationKey.toLowerCase(), data.toString());
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
