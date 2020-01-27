package core;

import data_representation.DataRepresentation;
import metrics.aggregation.AggregationStrategy;
import metrics.comparison.PairwiseComparisonStrategy;
import metrics.listwise.ListwiseComparisonStrategy;
import model.Command;
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

	public ComparisonService(int threads) {
		threadPool = Executors.newFixedThreadPool(threads);
		support = new PropertyChangeSupport(this);
	}

	/**
	 * Compares all the tests provided by testSuite1 with testSuite2 using the
	 * provided pairwise metric, then returns a final value using the provided aggregation
	 * strategy
	 *
	 * @param testCasePairs the list of test case pairs
	 * @param strategy    	the strategy to use to compare the tests
	 * @param aggregation 	the aggregation metric to use to assimilate all the
	 *                    	comparisons
	 * @return a double representing the similarity between the two list of tests
	 */
	public String compareTestCase(List<Tuple<DataRepresentation, DataRepresentation>> testCasePairs,
								  PairwiseComparisonStrategy strategy, AggregationStrategy aggregation,
								  PropertyChangeListener pcl) throws ExecutionException, InterruptedException {
		List<Command> tasks = new ArrayList<>();
		for (Tuple testCasePair : testCasePairs) {
			tasks.add(new PairwiseCommand(strategy, (DataRepresentation)testCasePair.getLeft(),
											(DataRepresentation)testCasePair.getRight(), pcl));
		}

		return compareWithThreadPool(tasks, aggregation, pcl);
		}

	/**
	 * private helper method to perform comparison with thread pool
	 *
	 * @param tasks the comparisons to make in the thread pool
	 * @param aggregation the aggregation metric to use to assimilate all the
	 * 	 *                    	comparisons
	 * @return a double representing the diversity of the testsuite comparisons
	 */
	private String compareWithThreadPool(List<Command> tasks, AggregationStrategy aggregation,
										 PropertyChangeListener pcl) throws ExecutionException, InterruptedException {
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));
		List<Future<Object>> futureList = threadPool.invokeAll(tasks);

		ArrayList<Double> results = new ArrayList<>();
		for (Future<Object> future : futureList)
			results.add((Double) future.get());

		return aggregation.aggregate(results);
	}


	/**
	 * Compares all the testsuites provided with the provided listwise metric,
	 * then returns a final value using the provided aggregation strategy
	 *
	 * @param testsuites 	the list of test suites
	 * @param strategy    	the strategy to use to calculate testsuite diversity
	 * @param aggregation 	the aggregation metric to use to assimilate all the
	 *                    	comparisons
	 * @return a double representing the diversity of the testsuite comparisons
	 */
	public String compareTestCase(List<List<DataRepresentation>> testsuites,
								  ListwiseComparisonStrategy strategy, AggregationStrategy aggregation,
								  PropertyChangeListener pcl) throws ExecutionException, InterruptedException {
		List<Command> tasks = new ArrayList<>();
		for(List<DataRepresentation> testsuite: testsuites)
			tasks.add(new ListwiseCommand(strategy, testsuite, pcl));
		return compareWithThreadPool(tasks, aggregation, pcl);
	}

	/**
	 * Shuts down the thread pool when we're done with the object.
	 */
	public void shutdown() throws InterruptedException {
		threadPool.shutdown();
		threadPool.awaitTermination(5000, TimeUnit.SECONDS);
	}

}
