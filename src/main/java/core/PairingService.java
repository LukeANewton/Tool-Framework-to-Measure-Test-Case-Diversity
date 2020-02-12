package core;

import data_representation.DataRepresentation;
import org.apache.commons.lang3.SerializationUtils;
import utilities.Tuple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A service that creates a list of pairs that the comparing service will use for comparisons.
 * This is based on comparing test cases from a single test suite or test cases from two or more different test suites.
 *
 * @author crushton
 */
public class PairingService {
    private PropertyChangeSupport support;

    /**constructor*/
    public PairingService() {
        support = new PropertyChangeSupport(this);
    }

    /**
     * Makes pairs to be compared from a single test suite. Each test case is compared to another test case once.
     *
     * @param testSuite the test suite of test cases
     * @return a list of pairs of test cases in the form of data representations
     */
    public List<Tuple<DataRepresentation, DataRepresentation>> makePairs(PropertyChangeListener pcl,
                                                                         DataRepresentation[] testSuite) {
        setListener(pcl, testSuite.length);
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        for (int i = 0; i < testSuite.length; i++) {
            int completedTasks = 0;
            for (int j = i + 1; j < testSuite.length; j++) {
               pairs.add(new Tuple<>(SerializationUtils.clone(testSuite[i]),
                        SerializationUtils.clone(testSuite[j])));
                completedTasks++;
            }
            support.firePropertyChange(new PropertyChangeEvent(this, "completed", 0, completedTasks));
        }
        return pairs;
    }

    /**
     * Makes pairs to be compared from one test suite to another. Each test case is compared to another test case once.
     *
     * @param testSuites an array of test suite arrays containing DataRepresentations for pairing
     * @return a list of pairs of test cases in the form of data representations
     */
    public List<Tuple<DataRepresentation, DataRepresentation>> makePairs(PropertyChangeListener pcl,
                                                                         DataRepresentation[]... testSuites) {
        setListener(pcl, Arrays.stream(testSuites).mapToInt(e->e.length).toArray());
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        // Observed column(Test Suite) of DataRepresentations
        for (int i = 0; i < testSuites.length; i++) {
            // Element(Test Case) in the observed column
            for (int j = 0; j < testSuites[i].length; j++) {
                // Loop through each other column after the observed column
                for (int k = i + 1; k < testSuites.length; k++) {
                    int completedTasks = 0;
                    // Loop through each element in that other column
                    for (int m = 0; m < testSuites[k].length; m++) {
                        // Pair the observed element(Test Case) in the observed column(Test Suite) with the other element(TC) in the other column(TS)
                        pairs.add(new Tuple<>( SerializationUtils.clone(testSuites[i][j]),
                                SerializationUtils.clone(testSuites[k][m])));
                        completedTasks++;
                    }
                    support.firePropertyChange(new PropertyChangeEvent(this, "completed", 0, completedTasks));
                }
            }
        }
        return pairs;
    }

    /**
     * finds the number of pairs that would be calculated within one list of size k
     *
     * @param k the number of items in a list to pair
     * @return the number of pairs that would be generated from the set of lists
     */
    private int calculateNumberOfPairsInList(int k){
        return k * (k - 1) / 2;
    }

    /**
     * finds the number of pairs that would be generated between several lists
     * of various lengths
     *
     * @param lengths the length of each list in the pairing operation
     * @return the number of pairs that would be generated between the lists
     */
    private int calculateNumberOfPairsBetweenLists(int... lengths){
        int tasks = 0;
        for(int i = 0; i < lengths.length; i++){//for each list
            for(int j = i + 1; j < lengths.length; j++){
                tasks += lengths[i] * lengths[j];
            }
        }
        return tasks;
    }

    /**
     * set up a listener for the pairing service and send it the number of pairs to expect
     *  @param pcl the listener for the pairing service
     * @param lengths the length of the list to pair
     */
    private void setListener(PropertyChangeListener pcl, int... lengths){
        if (pcl != null)
            support.addPropertyChangeListener(pcl);
        int tasks;
        if(lengths.length == 1)
            tasks = calculateNumberOfPairsInList(lengths[0]);
        else
            tasks = calculateNumberOfPairsBetweenLists(lengths);
        support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks));
        support.firePropertyChange(new PropertyChangeEvent(this, "completed", 0, 0));
    }
}
