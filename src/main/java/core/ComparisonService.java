package core;

import data_representation.DataRepresentation;
import metrics.aggregation.AggregationStrategy;
import metrics.comparison.pairwise.PairwiseComparisonStrategy;
import metrics.comparison.listwise.ListwiseComparisonStrategy;
import model.ListwiseCommand;
import model.PairwiseCommand;
import utilities.Tuple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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
	 * @param aggregation 	the aggregation metric to use to assimilate all the
	 *                    	comparisons
	 * @return a double representing the similarity between the two list of tests
	 */
	public String pairwiseCompare(List<Tuple<DataRepresentation, DataRepresentation>> testCasePairs,
								  PairwiseComparisonStrategy strategy, AggregationStrategy aggregation,
								  PropertyChangeListener pcl, boolean useThreadPool) throws Exception {
		List<Callable<Object>> tasks = new ArrayList<>();
		for (Tuple testCasePair : testCasePairs) {
			tasks.add(new PairwiseCommand(strategy, (DataRepresentation)testCasePair.getLeft(),
					(DataRepresentation)testCasePair.getRight(), pcl));
		}

		if(useThreadPool)
			return threadPoolCompareHelper(tasks, aggregation, pcl);
		else
			return sequentialCompareHelper(tasks, aggregation, pcl);
	}

	/**
	 * private helper method to perform comparison sequentially
	 *
	 * @param tasks the comparisons to make
	 * @param aggregation the aggregation metric to use to assimilate all the
	 * 	 *                    	comparisons
	 * @return a double representing the diversity of the testsuite comparisons
	 */
	private String sequentialCompareHelper(List<Callable<Object>> tasks, AggregationStrategy aggregation,
										   PropertyChangeListener pcl) throws Exception {
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));

		ArrayList<Double> results = new ArrayList<>();
		for (Callable<Object> task : tasks)
			results.add((double)task.call());

		return aggregation.aggregate(results);
	}

	/**
	 * private helper method to perform comparison with thread pool
	 *
	 * @param tasks the comparisons to make in the thread pool
	 * @param aggregation the aggregation metric to use to assimilate all the
	 * 	 *                    	comparisons
	 * @return a double representing the diversity of the testsuite comparisons
	 */
	private String threadPoolCompareHelper(List<Callable<Object>> tasks, AggregationStrategy aggregation,
										   PropertyChangeListener pcl) throws ExecutionException, InterruptedException {
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));
		List<Future<Object>> futureList = threadPool.invokeAll(tasks);

		ArrayList<Double> results = new ArrayList<>();
		for (Future<Object> future : futureList)
			results.add((Double) future.get());

		threadPool.shutdown();

		return aggregation.aggregate(results);
	}


	/**
	 * Compares all the testsuites provided with a thread pool and the
	 * provided listwise metric, then returns a final value using the
	 * provided aggregation strategy
	 *
	 * @param testsuites 	the list of test suites
	 * @param strategy    	the strategy to use to calculate testsuite diversity
	 * @param aggregation 	the aggregation metric to use to assimilate all the
	 *                    	comparisons
	 * @return a double representing the diversity of the testsuite comparisons
	 */
	public String listwiseCompare(List<List<DataRepresentation>> testsuites,
								  ListwiseComparisonStrategy strategy, AggregationStrategy aggregation,
								  PropertyChangeListener pcl, boolean useThreadPool) throws Exception {
		List<Callable<Object>> tasks = new ArrayList<>();
		for(List<DataRepresentation> testsuite: testsuites)
			tasks.add(new ListwiseCommand(strategy, testsuite, pcl));

		if(useThreadPool)
			return threadPoolCompareHelper(tasks, aggregation, pcl);
		else
			return sequentialCompareHelper(tasks, aggregation, pcl);
	}
}