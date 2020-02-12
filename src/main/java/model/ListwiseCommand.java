package model;

import data_representation.DataRepresentation;
import metrics.comparison.listwise.ListwiseComparisonStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * ListwiseCommands will contain the information needed to compare a test suite with a listwise metric
 *
 * @author luke
 *
 */
public class ListwiseCommand implements Callable<Object> {
    private ListwiseComparisonStrategy comparison;
    private List<DataRepresentation> testsuite;
    private PropertyChangeSupport support;

    public ListwiseCommand(ListwiseComparisonStrategy comparison, List<DataRepresentation> testsuite,
                           PropertyChangeListener pcl) {
        this.comparison = comparison;
        this.testsuite = testsuite;
        support = new PropertyChangeSupport(this);
        if (pcl != null)
            support.addPropertyChangeListener(pcl);
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
        for(DataRepresentation testcase: testsuite) {
            if (!testcase.getClass().equals(testsuite.get(0).getClass()))
                throw new TestCaseFormatMismatchException();
        }
        double result = comparison.compare(testsuite);
        support.firePropertyChange(new PropertyChangeEvent(this, "complete",
                null, 1));
        return result;
    }
}