package core;

import data_representation.DataRepresentation;
import metrics.aggregation.AggregationStrategy;
import metrics.comparison.PairwiseComparisonStrategy;
import model.Command;
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

	public String compareTestCase(List<Tuple<DataRepresentation, DataRepresentation>> testCasePairs,
								  PairwiseComparisonStrategy strategy,
								  AggregationStrategy aggregation) throws ExecutionException, InterruptedException {
		return compareTestCase(testCasePairs, strategy, aggregation, null);
	}

	/**
	 * Compares all the tests provided by testSuite1 with testSuite2 using the
	 * provided strategy, then returns a final value using the provided aggregation
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
		if (pcl != null)
			support.addPropertyChangeListener(pcl);

		List<Command> tasks = new ArrayList<>();
		for (Tuple testCasePair : testCasePairs) {
			tasks.add(new Command(strategy, (DataRepresentation)testCasePair.getLeft(),
					(DataRepresentation)testCasePair.getRight(), pcl));
		}
		support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0, tasks.size()));
		List<Future<Object>> futureList = threadPool.invokeAll(tasks);

		ArrayList<Double> results = new ArrayList<>();
		for (Future<Object> future : futureList) {
			results.add((Double) future.get());
		}
		return aggregation.aggregate(results);
	}

	/**
	 * Shuts down the thread pool when we're done with the object.
	 */
	public void shutdown() throws InterruptedException {
		threadPool.shutdown();
		threadPool.awaitTermination(5000, TimeUnit.SECONDS);
	}

}
