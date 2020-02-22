package model;

import core.InvalidFormatException;
import data_representation.DataRepresentation;
import utilities.Tuple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.InvocationTargetException;
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
    private String[] testSuite;
    private String testCase;
    private DataRepresentation format;

    /**constructor*/
    public PairingCommand(PropertyChangeListener pcl, String testCase, String[] testSuite, DataRepresentation format) {
        this.testSuite = testSuite;
        this.testCase = testCase;
        this.format = format;
        support = new PropertyChangeSupport(this);
        if (pcl != null)
            support.addPropertyChangeListener(pcl);
    }

    @Override
    public List<Tuple<DataRepresentation, DataRepresentation>> call() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InvalidFormatException {
        int completedTasks = 0;
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        for (String t : testSuite) {
            DataRepresentation d1 = format.getClass().getConstructor().newInstance();
            d1.parse(testCase);
            DataRepresentation d2 = format.getClass().getConstructor().newInstance();
            d2.parse(t);
            pairs.add(new Tuple<>(d1, d2));
            completedTasks++;
        }
        support.firePropertyChange(new PropertyChangeEvent(this, "completed", 0, completedTasks));
        return pairs;
    }
}
