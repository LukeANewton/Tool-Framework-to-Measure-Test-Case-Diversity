package metrics.aggregation;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Aggregation strategy that chooses the most frequent similarity value to represent the overall similarity of the test cases compared.
 *
 * @author crushton
 */
public class ModeValue implements AggregationStrategy {
    /**
     * Chooses the most frequent similarity value to represent the overall similarity of the test cases compared.
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    @Override
    public String aggregate(List<Double> similarities) throws NullPointerException {
        if (similarities.isEmpty()) throw new NullPointerException();
        // Count frequency of each element
        Map<Double, Long> countFrequencies = similarities.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        // Find what the max frequency is
        long maxFrequency = countFrequencies.values().stream()
                .mapToLong(count -> count)
                .max().orElse(-1);
        // Collect all items with the max frequency
        return countFrequencies.entrySet().stream()
                .filter(tuple -> tuple.getValue() == maxFrequency)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).toString();
    }

    /**
     * returns a description of the object
     *
     * @return a description of the object
     */
    @Override
    public String getDescription() {
        return "Chooses the most frequent similarity value to represent the overall similarity of the test cases compared.";
    }
}
