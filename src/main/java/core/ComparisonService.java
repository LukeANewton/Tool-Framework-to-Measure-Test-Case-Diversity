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
	 * @param aggregations	the aggregation metrics to use to assimilate all the
	 *                    	comparisons
	 * @return an array of strings, where each string is the result of an aggregation strategy
	 */
	public String[] pairwiseCompare(List<Tuple<DataRepresentation, DataRepresentation>> testCasePairs,
										  PairwiseComparisonStrategy strategy, AggregationStrategy[] aggregations,
										  PropertyChangeListener pcl, boolean useThreadPool) throws Exception {
		List<Callable<Object>> tasks = new ArrayList<>();
		for (Tuple testCasePair : testCasePairs) {
			tasks.add(new PairwiseCommand(strategy, (DataRepresentation)testCasePair.getLeft(),
					(DataRepresentation)testCasePair.getRight(), pcl));
		}

		if(useThreadPool)
			return threadPoolCompareHelper(tasks, aggregations, pcl);
		else
			return sequentialCompareHelper(tasks, aggregations, pcl);
	}

	/**
	 * private helper method to perform comparison sequentially
	 *
	 * @param tasks the comparisons to make
	 * @param aggregations the aggregation metric to use to assimilate all the
	 * 	 *                    	comparisons
	 * @return an array of strings, where each string is the result of an aggregation strategy
	 */
	private String[] sequentialCompareHelper(List<Callable<Object>> tasks, AggregationStrategy[] aggregations,
										   PropertyChangeListener pcl) throws Exception {
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));

		ArrayList<Double> results = new ArrayList<>();
		for (Callable<Object> task : tasks)
			results.add((double)task.call());

		List<String> aggregateResults = new ArrayList<>();
		for(AggregationStrategy aggregation: aggregations)
			aggregateResults.add(aggregation.aggregate(results));
		return aggregateResults.toArray(new String[0]);
	}

	/**
	 * private helper method to perform comparison with thread pool
	 *
	 * @param tasks the comparisons to make in the thread pool
	 * @param aggregations the aggregation metric to use to assimilate all the
	 * 	 *                    	comparisons
	 * @return an array of strings, where each string is the result of an aggregation strategy
	 */

	private String[] threadPoolCompareHelper(List<Callable<Object>> tasks, AggregationStrategy[] aggregations,
								   PropertyChangeListener pcl) throws ExecutionException, InterruptedException {
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));
		List<Future<Object>> futureList = threadPool.invokeAll(tasks);

		ArrayList<Double> results = new ArrayList<>();
		for (Future<Object> future : futureList)
			results.add((Double) future.get());

		List<String> aggregateResults = new ArrayList<>();
		for(AggregationStrategy aggregation: aggregations)
			aggregateResults.add(aggregation.aggregate(results));
		return aggregateResults.toArray(new String[0]);
	}


	/**
	 * Compares all the testsuites provided with a thread pool and the
	 * provided listwise metric, then returns a final value using the
	 * provided aggregation strategy
	 *
	 * @param testsuites 	the list of test suites
	 * @param strategy    	the strategy to use to calculate testsuite diversity
	 * @param aggregations 	the aggregation metrics to use to assimilate all the
	 *                    	comparisons
	 * @return an array of strings, where each string is the result of an aggregation strategy
	 */
	public String[] listwiseCompare(List<List<DataRepresentation>> testsuites,
								  ListwiseComparisonStrategy strategy, AggregationStrategy[] aggregations,
								  PropertyChangeListener pcl, boolean useThreadPool) throws Exception {
		List<Callable<Object>> tasks = new ArrayList<>();
		for(List<DataRepresentation> testsuite: testsuites)
			tasks.add(new ListwiseCommand(strategy, testsuite, pcl));

		if(useThreadPool)
			return threadPoolCompareHelper(tasks, aggregations, pcl);
		else
			return sequentialCompareHelper(tasks, aggregations, pcl);
	}
}