package metrics.aggregation;

import java.util.List;

/**
 * Sums the absolute value of all similarities.
 *
 * @author crushton
 */
public class Manhattan implements AggregationStrategy {
    /**
     * Compiles all the similarity measure provided into a final similarity
     * measurement for the whole list
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    @Override
    public Double aggregate(List<Double> similarities) throws NullPointerException {
        if (similarities.isEmpty()) throw new NullPointerException();
        return similarities.stream().mapToDouble(Math::abs).sum();
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Sums the absolute value of all similarities.";
    }
}
