package metrics.aggregation;

import java.util.Arrays;
import java.util.List;

/**
 * Aggregation strategy that chooses the highest similarity value to represent the overall similarity of the test cases compared.
 *
 * @author crushton
 */
public class MaximumValue implements AggregationStrategy {
    /**
     * Chooses the highest similarity value to represent the overall similarity of the test cases compared.
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    @Override
    public List<Double> aggregate(List<Double> similarities) throws NullPointerException {
        return Arrays.asList(similarities.stream().max(Double::compare).orElseThrow(NullPointerException::new));
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Chooses the largest similarity to represent the overall similarity of the test cases.";
    }
}
