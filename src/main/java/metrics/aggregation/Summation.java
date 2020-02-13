package metrics.aggregation;

import java.util.List;

/**
 * Sums all signed similarities.
 * Example:
 * 4.0 + (-3.0) + 0.3 + 1.2 = 2.5
 * @author crushton
 */
public class Summation implements AggregationStrategy {
    /**
     * Compiles all the similarity measure provided into a final similarity
     * measurement for the whole list
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    @Override
    public String aggregate(List<Double> similarities) throws NullPointerException {
        if (similarities.isEmpty()) throw new NullPointerException();
        return String.valueOf(similarities.stream().mapToDouble(Double::valueOf).sum());
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Sums all signed similarities.";
    }
}
