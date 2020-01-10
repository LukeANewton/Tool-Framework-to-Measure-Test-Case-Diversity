package metrics.aggregation;

import java.util.List;

/**
 * Aggregation strategy that chooses the lowest similarity value to represent the overall similarity of the test cases compared.
 *
 * @author crushton
 */
public class MinimumValue implements Aggregation {

    public MinimumValue() {
    }

    /**
     * Chooses the lowest similarity value to represent the overall similarity of the test cases compared.
     *
     * @param similarities List<Double> the similarities
     * @return the lowest similarity value
     * @throws NullPointerException when there is no final similarity value caused by the similarities array being empty
     */
    @Override
    public double aggregate(List<Double> similarities) throws NullPointerException {
        return similarities.stream().min(Double::compare).orElseThrow(NullPointerException::new);
    }
}
