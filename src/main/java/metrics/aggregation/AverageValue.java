package metrics.aggregation;

import java.util.Arrays;
import java.util.List;

/**
 * Aggregation strategy that averages the similarity values to represent the overall similarity of the test cases compared.
 *
 * @author crushton
 */
public class AverageValue implements AggregationStrategy {

    public AverageValue() {
    }

    /**
     * Chooses the average similarity value to represent the overall similarity of the test cases compared.
     *
     * @param similarities List<Double> the similarities
     * @return the average similarity value
     * @throws NullPointerException when there is no final similarity value caused by the similarities array being empty
     */
    @Override
    public List<Double> aggregate(List<Double> similarities) throws NullPointerException {
        return Arrays.asList(similarities.stream().mapToDouble(Double::valueOf).average().orElseThrow(NullPointerException::new));
    }

    /**
     * Provides a description of how the aggregation strategy works
     *
     * @return a description of how the aggregation strategy works
     */
    @Override
    public String getDescription() {
        return "Averages the similarities to represent the overall similarity of the test cases.";
    }
}
