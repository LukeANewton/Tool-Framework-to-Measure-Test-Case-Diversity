package com.compare.core;

import com.compare.data_representation.DataRepresentation;
import com.compare.metrics.aggregation.AggregationStrategy;
import com.compare.metrics.comparison.PairwiseComparisonStrategy;
import com.compare.metrics.listwise.ListwiseComparisonStrategy;
import com.compare.model.Config;
import com.compare.model.ListwiseCommand;
import com.compare.model.PairwiseCommand;
import com.compare.utilities.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class ComparisonService {

	private ExecutorService threadPool;

	@Autowired
	public ComparisonService(Config configuration) {
		threadPool = Executors.newFixedThreadPool(configuration.getNumThreads());
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
								  PairwiseComparisonStrategy strategy, AggregationStrategy aggregation) throws ExecutionException, InterruptedException {
		List<Callable> tasks = new ArrayList<>();
		for(Tuple pair: testCasePairs)
			tasks.add(new PairwiseCommand(strategy, (DataRepresentation) pair.getLeft(),
					(DataRepresentation) pair.getRight()));
		return compareWithThreadPool(tasks, aggregation);
	}

	/**
	 * private helper method to perform comparison with thread pool
	 *
	 * @param tasks the comparisons to make in the thread pool
	 * @param aggregation the aggregation metric to use to assimilate all the
	 * 	 *                    	comparisons
	 * @return a double representing the diversity of the testsuite comparisons
	 */
	private String compareWithThreadPool(List<Callable> tasks, AggregationStrategy aggregation) throws ExecutionException, InterruptedException {
		List<Future<Object>> futureList = new ArrayList<>();
		for (Callable task : tasks) {
			Future<Object> comparison = threadPool.submit(task);
			futureList.add(comparison);
		}

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
								  ListwiseComparisonStrategy strategy, AggregationStrategy aggregation) throws ExecutionException, InterruptedException {
		List<Callable> tasks = new ArrayList<>();
		for(List<DataRepresentation> testsuite: testsuites)
			tasks.add(new ListwiseCommand(strategy, testsuite));
		return compareWithThreadPool(tasks, aggregation);
	}

	/**
	 * Shuts down the thread pool when we're done with the object.
	 */
	public void shutdown() throws InterruptedException {
		threadPool.shutdown();
		threadPool.awaitTermination(5000, TimeUnit.SECONDS);
	}

}
