package metrics.report_format;

import model.CompareDTO;

import java.util.List;
import java.util.Map;

/**
 * Prints data in a human readable format
 *
 * @author crushton
 */
public class Pretty implements ReportFormat {

    /**
     * Formats the aggregations of the similarity values and data from the DTO into a convenient format.
     *
     * @param dto          The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    @Override
    public String format(CompareDTO dto, List<Double> similarities, List<List<Double>> aggregations) {

        StringBuilder formattedData = new StringBuilder();
        formattedData.append(getReportHeader()).append(System.lineSeparator());
        Map<String, String> runParamPairs = getRunParameters(dto);

        formattedData.append("Run parameters:").append(System.lineSeparator());
        for (String paramKey : runParamPairs.keySet()) {
            formattedData.append(paramKey).append(": ").append(runParamPairs.get(paramKey)).append(System.lineSeparator());
        }

        formattedData.append(System.lineSeparator()).append("Results:").append(System.lineSeparator());
        Map<String, List<Double>> aggregationMethodValuePairs = getAggregations(dto, aggregations);
        for (String method : aggregationMethodValuePairs.keySet()) {
            formattedData.append(method).append(": ").append(aggregationMethodValuePairs.get(method)).append(System.lineSeparator());
        }

        return formattedData.toString();
    }

    /**
     * Provides a description of the format
     *
     * @return a brief description of the format
     */
    @Override
    public String getDescription() {
        return "Prints data in a human readable format.";
    }
}
