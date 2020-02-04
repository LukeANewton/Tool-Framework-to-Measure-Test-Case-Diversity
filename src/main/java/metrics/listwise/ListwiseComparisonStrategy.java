package metrics.listwise;

import core.HelpTarget;
import data_representation.DataRepresentation;
import model.TestCaseFormatMismatchException;

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
     * @throws TestCaseFormatMismatchException thrown when there are multiple different data representations in the test suite
     */
     public double compare(List<DataRepresentation> testsuite) throws TestCaseFormatMismatchException;
}
