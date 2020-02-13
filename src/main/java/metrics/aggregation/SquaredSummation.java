package metrics.aggregation;

import java.util.List;

/**
 * Squares all the similarities and returns the summation.
 *
 * @author crushton
 */
public class SquaredSummation implements AggregationStrategy {
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
        int exponent = 2;
        return String.valueOf(similarities.stream().mapToDouble(similarity -> Math.pow(similarity, exponent)).sum());
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Squares all the similarities and returns the sum of squares.";
    }
}