package core;

import data_representation.DataRepresentation;
import model.PairingCommand;
import utilities.Tuple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A service that creates a list of pairs that the comparing service will use for comparisons.
 * This is based on comparing test cases from a single test suite or test cases from two or more different test suites.
 *
 * @author crushton
 */
public class PairingService {
    private ExecutorService threadPool;
    private PropertyChangeSupport support;

    /**constructor*/
    public PairingService(ExecutorService threadPool) {
        this.threadPool = threadPool;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Makes pairs to be compared from a single test suite. Each test case is compared to another test case once.
     *
     * @param testSuite the test suite of test cases
     * @return a list of pairs of test cases in the form of data representations
     */
    public List<Tuple<DataRepresentation, DataRepresentation>> makePairsWithin(
            PropertyChangeListener pcl, DataRepresentation format, String[] testSuite) throws Exception {
        setListener(pcl, testSuite.length);
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        List<Future<Object>> futureList = new ArrayList<>();
        for (int i = 0; i < testSuite.length; i++)//for each test case, generate a command that makes pairs with the remainder of the suite
            futureList.add(threadPool.submit(new PairingCommand(pcl, testSuite[i],
                    Arrays.copyOfRange(testSuite, i+1, testSuite.length), format)));

        for (Future<Object> future : futureList) {
            List<Tuple<DataRepresentation, DataRepresentation>> result =
                    (List<Tuple<DataRepresentation, DataRepresentation>>) future.get();
            pairs.addAll(result);
        }
        return pairs;
    }

    /**
     * Makes pairs to be compared from one test suite to another. Each test case is compared to another test case once.
     *
     * @param testSuites an array of test suite arrays containing DataRepresentations for pairing
     * @return a list of pairs of test cases in the form of data representations
     */
    public List<Tuple<DataRepresentation, DataRepresentation>> makePairsBetween(
            PropertyChangeListener pcl, DataRepresentation format, String[]... testSuites) throws Exception {
        setListener(pcl, Arrays.stream(testSuites).mapToInt(e->e.length).toArray());
        List<Tuple<DataRepresentation, DataRepresentation>> pairs = new ArrayList<>();
        List<Future<Object>> futureList = new ArrayList<>();
        for (int i = 0; i < testSuites.length; i++) {//for each test suite
            for (int j = 0; j < testSuites[i].length; j++) //for each test case, create a command that generates pairs on the rest of the suites
                futureList.add(threadPool.submit(new PairingCommand(pcl, testSuites[i][j],
                        flatten(Arrays.copyOfRange(testSuites, i+1, testSuites.length)),
                        format
                       )));
        }
        for (Future<Object> future : futureList) {
            List<Tuple<DataRepresentation, DataRepresentation>> result =
                    (List<Tuple<DataRepresentation, DataRepresentation>>) future.get();
            pairs.addAll(result);
        }
        return pairs;
    }

    /**
     * flattens a 2D DataRepresentation array in a 1D array
     *
     * @param testSuites the 2D array
     * @return the input array flattened
     */
    private String[] flatten(String[][] testSuites){
        return Arrays.stream(testSuites)
                .flatMap(Arrays::stream).toArray(String[]::new);
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
            for(int j = i + 1; j < lengths.length; j++)
                tasks += lengths[i] * lengths[j];
        }
        return tasks;
    }

    /**
     * set up a listener for the pairing service and send it the number of pairs to expect
     * @param pcl the listener for the pairing service
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
    }
}
