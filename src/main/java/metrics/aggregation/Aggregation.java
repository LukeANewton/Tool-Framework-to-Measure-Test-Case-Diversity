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
public interface Aggregation {

    /**
     * compiles all the similarity measure provided into a final similarity
     * measurement for the whole list
     *
     * @param similarities List<Double> the similarities
     * @return the aggregated similarity
     */
    double aggregate(List<Double> similarities) throws NullPointerException;

}
