package com.compare.model;

import java.util.List;
import java.util.concurrent.Callable;
import com.compare.data_representation.DataRepresentation;
import com.compare.metrics.listwise.ListwiseComparisonStrategy;

/**
 * ListwiseCommands will contain the information needed to compare a test suite with a listwise metric
 *
 * @author luke
 *
 */
public class ListwiseCommand implements Callable<Object>{
    private ListwiseComparisonStrategy comparison;
    private List<DataRepresentation> testsuite;

    public ListwiseCommand(ListwiseComparisonStrategy comparison, List<DataRepresentation> testsuite) {
        this.comparison = comparison;
        this.testsuite = testsuite;
    }

    public ListwiseComparisonStrategy getComparison() {
        return comparison;
    }

    public List<DataRepresentation> getTestsuite() {
        return testsuite;
    }

    /**
     * calculate the diversity within the test suite
     *
     * @return a double representing the result of the diversity calculation
     */
    public Object call() throws Exception {
        return comparison.compare(testsuite);
    }

}
