package metrics.aggregation;

import java.util.List;

/**
 * Aggregation strategy that chooses the lowest similarity value to represent the overall similarity of the test cases compared.
 *
 * @author crushton
 */
public class MinimumValueDouble implements Aggregation {

    public MinimumValueDouble() {
    }

    /**
     * Chooses the lowest similarity value to represent the overall similarity of the test cases compared.
     *
     * @param similarities List<Double> the similarities
     * @return the lowest similarity value
     */
    @Override
    public Double aggregate(List<Double> similarities) {
        return similarities.stream().min(Double::compare).orElse(null);
    }
}
