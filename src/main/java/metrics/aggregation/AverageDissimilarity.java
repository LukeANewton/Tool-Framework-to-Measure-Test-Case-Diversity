package metrics.aggregation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Averages the similarity values and divides the average by the squared number of similarities.
 *
 * @author crushton
 */
public class AverageDissimilarity implements AggregationStrategy {
    /**
     * Compiles all the similarity measure provided into a final similarity
     * measurement for the whole list
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    @Override
    public List<Double> aggregate(List<Double> similarities) throws NullPointerException {
        if (similarities.isEmpty()) throw new NullPointerException();
        int exponent = 2;
        return Arrays.asList(similarities.stream().mapToDouble(Double::valueOf).sum() / Math.pow(similarities.size(), exponent));
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Sums the similarity values and divides the sum by the squared number of similarities.";
    }
}
