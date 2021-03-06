package metrics.comparison.listwise;

import core.HelpTarget;
import data_representation.DataRepresentation;

import java.util.List;

/**
 * An interface for any listwise metrics to implement. listwise metrics are
 * those metrics that compare a whole list at once, rather than doing the
 * comparison in a pairwise manner
 */
public interface ListwiseComparisonStrategy extends HelpTarget {
    /**
     * compare a set of test cases according to this listwise metric
     *
     * @param testsuite the set of test cases to compare
     * @return the result of the comparison
     */
     double compare(List<DataRepresentation> testsuite);
}
