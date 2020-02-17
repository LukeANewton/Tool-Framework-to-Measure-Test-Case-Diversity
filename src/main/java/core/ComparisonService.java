package core;

import data_representation.DataRepresentation;
import metrics.comparison.listwise.ListwiseComparisonStrategy;
import metrics.comparison.pairwise.PairwiseComparisonStrategy;
import model.ListwiseCommand;
import model.PairwiseCommand;
import utilities.Tuple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * the Comparator will compare two given tests or list of tests by making
 * commands to compare each test and assigns the commands to a thread. when the
 * calculations are done, it will use the given Aggregation Strategy to return a
 * final similarity measurement
 *
 * @author Eric
 *
 */
public class ComparisonService {
	private ExecutorService threadPool;
	private PropertyChangeSupport support;

	public ComparisonService() {
		support = new PropertyChangeSupport(this);
	}

	public ComparisonService(ExecutorService threadPool) {
		this.threadPool = threadPool;
		support = new PropertyChangeSupport(this);
	}

	/**
	 * Compares all the tests provided by testSuite1 with testSuite2 using the
	 * provided pairwise metric and a thread pool, then returns a final value
	 * using the provided aggregation
	 * strategy
	 *
	 * @param testCasePairs the list of test case pairs
	 * @param strategy    	the strategy to use to compare the tests
	 * @return the list of calculated similarities as Doubles
	 */
	public List<Double> pairwiseCompare(List<Tuple<DataRepresentation, DataRepresentation>> testCasePairs, PairwiseComparisonStrategy strategy,
										PropertyChangeListener pcl, boolean useThreadPool) throws Exception {
		List<Callable<Object>> tasks = new ArrayList<>();
		for (Tuple testCasePair : testCasePairs) {
			tasks.add(new PairwiseCommand(strategy, (DataRepresentation)testCasePair.getLeft(),
					(DataRepresentation)testCasePair.getRight(), pcl));
		}

		if(useThreadPool)
			return threadPoolCompareHelper(tasks, pcl);
		else
			return sequentialCompareHelper(tasks, pcl);
	}

	/**
	 * private helper method to perform comparison sequentially
	 *
	 * @param tasks the comparisons to make
	 * @return the list of calculated similarities as Doubles
	 */
	private List<Double> sequentialCompareHelper(List<Callable<Object>> tasks, PropertyChangeListener pcl) throws Exception {
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));

		ArrayList<Double> similarities = new ArrayList<>();
		for (Callable<Object> task : tasks)
			similarities.add((double)task.call());
		return similarities;
	}

	/**
	 * private helper method to perform comparison with thread pool
	 *
	 * @param tasks the comparisons to make in the thread pool
	 * @return the list of calculated similarities as Doubles
	 */

	private List<Double> threadPoolCompareHelper(List<Callable<Object>> tasks, PropertyChangeListener pcl) throws ExecutionException, InterruptedException {
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));
		List<Future<Object>> futureList = threadPool.invokeAll(tasks);

		ArrayList<Double> similarities = new ArrayList<>();
		for (Future<Object> future : futureList)
			similarities.add((Double) future.get());

		threadPool.shutdown();
		return similarities;
	}


	/**
	 * Compares all the testsuites provided with a thread pool and the
	 * provided listwise metric, then returns a final value using the
	 * provided aggregation strategy
	 *
	 * @param testsuites 	the list of test suites
	 * @param strategy    	the strategy to use to calculate testsuite diversity
	 * @return the list of calculated similarities as Doubles
	 */
	public List<Double> listwiseCompare(List<List<DataRepresentation>> testsuites, ListwiseComparisonStrategy strategy,
										PropertyChangeListener pcl, boolean useThreadPool) throws Exception {
		List<Callable<Object>> tasks = new ArrayList<>();
		for(List<DataRepresentation> testsuite: testsuites)
			tasks.add(new ListwiseCommand(strategy, testsuite, pcl));

		if(useThreadPool)
			return threadPoolCompareHelper(tasks, pcl);
		else
			return sequentialCompareHelper(tasks, pcl);
	}
}