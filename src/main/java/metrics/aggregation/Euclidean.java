package metrics.aggregation;

import java.util.List;

/**
 * Squares all the similarities and returns the square root of the sum of squares.
 *
 * @author crushton
 */
public class Euclidean implements AggregationStrategy {
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
        int exponent = 2;
        return Math.sqrt(similarities.stream().mapToDouble(similarity -> similarity = Math.pow(similarity, exponent)).sum());
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Squares all the similarities and returns the square root of the sum of squares";
    }
}
