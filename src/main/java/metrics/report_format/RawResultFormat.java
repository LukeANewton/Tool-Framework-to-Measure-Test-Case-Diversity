package metrics.report_format;

import model.CompareDTO;

import java.util.List;

/**
 * Prints only the resulting aggregated values
 *
 * @author crushton
 */
public class RawResultFormat implements ReportFormat {
    /**
     * Formats the aggregations of the similarity values into a convenient format.
     *
     * @param dto          The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    @Override
    public String format(CompareDTO dto, List<Double> similarities, List<String> aggregations) {
        StringBuilder formattedResults = new StringBuilder();
        for (String aggregation : aggregations) {
            formattedResults.append(aggregation).append(System.lineSeparator());
        }
        return formattedResults.toString();
    }

    /**
     * Provides a description of the format
     *
     * @return a brief description of the format
     */
    @Override
    public String getDescription() {
        return "Print the resulting aggregated values.";
    }
}
