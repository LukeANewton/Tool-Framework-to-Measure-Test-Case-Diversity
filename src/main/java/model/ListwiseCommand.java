package model;

import data_representation.DataRepresentation;
import metrics.listwise.ListwiseComparisonStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * ListwiseCommands will contain the information needed to compare a test suite with a listwise metric
 *
 * @author luke
 *
 */
public class ListwiseCommand implements Command{
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
        double result = comparison.compare(testsuite);
        support.firePropertyChange(new PropertyChangeEvent(this, "complete",
                null, null));
        return result;
    }
}