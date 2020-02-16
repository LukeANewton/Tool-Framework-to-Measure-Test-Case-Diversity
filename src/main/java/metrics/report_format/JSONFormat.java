package metrics.report_format;

import model.CompareDTO;

import java.util.List;

public class JSONFormat implements ReportFormat {
    /**
     * Formats the similarity values, aggregations of the similarity values and data from the DTO into a convenient format.
     *
     * @param dto          The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    @Override
    public String format(CompareDTO dto, List<Double> similarities, String[] aggregations) {
        return null;
    }

    /**
     * Provides a description of the format
     *
     * @return a brief description of the format
     */
    @Override
    public String getDescription() {
        return null;
    }
}
