package metrics.report_format;

import data_representation.DataRepresentation;
import model.CompareDTO;
import utilities.Tuple;

import java.util.List;
import java.util.Map;

/**
 * Prints data in a human readable format
 */
public class DefaultFormat implements ReportFormat {
    /**
     * Formats the similarity values, aggregations of the similarity values and data from the DTO into a convenient  format.
     *
     * @param dto          The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    @Override
    public String format(CompareDTO dto, List<Tuple<DataRepresentation, DataRepresentation>> pairs, List<Double> similarities, String[] aggregations) {

        StringBuilder formattedData = new StringBuilder();
        formattedData.append(getReportHeader()).append(System.lineSeparator());
        Map<String, String> runParamPairs = getRunParameters(dto);

        for (String paramKey : runParamPairs.keySet()) {
            if (runParamPairs.get(paramKey).equals(DEFAULT_VALUE)) continue;
            formattedData.append(paramKey).append(": ").append(runParamPairs.get(paramKey)).append(System.lineSeparator());
        }

        //TODO: This could be bad... What if I choose default thinking that I'll get this info but then it doesn't print it? Re-run? Ask user if they would like this data printed separately?
        if (similarities.size() < SIMILARITIES_SIZE_THRESHOLD) {
            for (int i = 0; i < similarities.size(); i++) {
                formattedData.append("[").append(i).append("]").append(System.lineSeparator());
                formattedData.append("Operand 1: ").append(pairs.get(i).getLeft().toString()).append(System.lineSeparator());
                formattedData.append("Operand 2: ").append(pairs.get(i).getRight().toString()).append(System.lineSeparator());
                formattedData.append("Similarity: ").append(similarities.get(i)).append(System.lineSeparator());
            }
        }

        Map<String, String> aggregationMethodValuePairs = getAggregations(dto, aggregations);
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
        return null;
    }
}
