package metrics.aggregation;

import core.HelpTarget;

import java.util.List;

/**
 * Interface for all aggregation strategies that will be implemented. It ensures
 * that each aggregation method will have a method to summarize the values given
 * to it.
 *
 * @author Eric
 *
 */
public interface AggregationStrategy extends HelpTarget {

    /**
     * Compiles all the similarity measure provided into a final similarity
     * measurement for the whole list
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     * @throws NullPointerException when there is no resulting aggregate value created
     */
    Double aggregate(List<Double> similarities) throws NullPointerException;
}
