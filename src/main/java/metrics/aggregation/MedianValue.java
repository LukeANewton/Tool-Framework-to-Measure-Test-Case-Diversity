package metrics.aggregation;

import java.util.Collections;
import java.util.List;

/**
 * Aggregation strategy that chooses the median similarity value to represent the overall similarity of the test cases compared.
 *
 * @author crushton
 */
public class MedianValue implements AggregationStrategy {
    /**
     * Chooses the median similarity value to represent the overall similarity of the test cases compared.
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    @Override
    public Double aggregate(List<Double> similarities) throws NullPointerException {
        if (similarities.isEmpty()) throw new NullPointerException();
        // For many similarities, consider an approach that uses pivot points. This would be O(n) while the current implementation is O(nlogn)
        Collections.sort(similarities);
        Double[] similarities_array = similarities.toArray(new Double[similarities.size()]);
        int middle = similarities_array.length / 2;
        if (similarities.size() % 2 == 0) {
            return (similarities_array[middle - 1] + similarities_array[middle])/2;
        }
        return similarities_array[middle];
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Chooses the median similarity to represent the overall similarity of the test cases.";
    }
}
