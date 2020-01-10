package metrics.aggregation;

import java.util.List;

/**
 * Interface for all aggregation strategies that will be implemented. It ensures
 * that each aggregation method will have a method to summarize the values given
 * to it.
 *
 * @author Eric
 *
 */
public interface AggregationStrategy {

    /**
     * Compiles all the similarity measure provided into a final similarity
     * measurement for the whole list
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    double aggregate(List<Double> similarities) throws NullPointerException;

    /**
     * Provides a description of how the aggregation strategy works
     *
     * @return a description of how the aggregation strategy works
     */
    String getDescription();
}
