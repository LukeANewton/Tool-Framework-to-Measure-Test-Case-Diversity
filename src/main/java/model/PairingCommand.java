package model;

import data_representation.DataRepresentation;
import org.apache.commons.lang3.SerializationUtils;
import utilities.Tuple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * This encapsulates a portion of the pairing functionality for concurrent execution in a thread pool.
 * Each command is given a subset of the total pairs to generate as a way to speed up this operation as
 * it quickly explodes (eg. 1400 list items gives 980 000 unordered pairs!)
 *
 * @author luke
 */
public class PairingCommand implements Callable<Object> {
    private PropertyChangeSupport support;
    private DataRepresentation[] testSuite;
    private DataRepresentation testCase;

    /**constructor*/
    public PairingCommand(PropertyChangeListener pcl, DataRepresentation testCase, DataRepresentation[] testSuite) {
        this.testSuite = testSuite;
        this.testCase = testCase;
        support = new PropertyChangeSupport(this);
        if (pcl != null)
            support.addPropertyChangeListener(pcl);
    }

    @Override
    public List<Tuple<DataRepresentation, DataRepresentation>> call() {
        int completedTasks = 0;
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        for (DataRepresentation dataRepresentation : testSuite) {
            pairs.add(new Tuple<>(SerializationUtils.clone(testCase),
                    SerializationUtils.clone(dataRepresentation)));
            completedTasks++;
        }
        support.firePropertyChange(new PropertyChangeEvent(this, "completed", 0, completedTasks));
        return pairs;
    }
}
