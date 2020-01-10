package metrics.aggregation;

import java.util.List;

/**
 * Aggregation strategy that averages the similarity values to represent the overall similarity of the test cases compared.
 *
 * @author crushton
 */
public class AverageValue implements Aggregation {

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
    public double aggregate(List<Double> similarities) throws NullPointerException {
        return similarities.stream().mapToDouble(Double::valueOf).average().orElseThrow(NullPointerException::new);
    }
}
