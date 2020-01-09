package core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import data_representation.DataRepresentation;
import metrics.aggregation.Aggregation;
import metrics.comparison.PairwiseComparisonStrategy;
import model.Command;

/**
 * the Comparator will compare two given tests or list of tests by making
 * commands to compare each test and assigns the commands to a thread. when the
 * calculations are done, it will use the given Aggregation Strategy to return a
 * final similarity measurement
 * 
 * @author Eric
 *
 */
public class Camparator {
	private ExecutorService threadPool;

	public Camparator(int threads) {
		threadPool = Executors.newFixedThreadPool(threads);
	}

	/**
	 * Compares the two tests provided using the given comparison strategy
	 * 
	 * @param test1 the first test too compare
	 * @param test2 the second test to compare
	 * @param strategy the strategy used to compare the tests
	 * @return a double representing the similarity between the two tests
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public double compareTestCase(DataRepresentation test1, DataRepresentation test2, PairwiseComparisonStrategy strategy)
			throws InterruptedException, ExecutionException {
		Command c = new Command(strategy, test1, test2);
		Future<Object> result = threadPool.submit(c);
		threadPool.awaitTermination(5, TimeUnit.SECONDS);
		return (double) result.get();
	}

	/**
	 * Compares all the tests provided by testList1 with testList2 using the
	 * provided strategy, then returns a final value using the provided aggregation
	 * strategy
	 * 
	 * @param testList1   the first list of tests
	 * @param testList2   the second list of tests
	 * @param strategy    the strategy to use to compare the tests
	 * @param aggregation the aggregation metric to use to assimilate all the
	 *                    comparisons
	 * @return a double representing the similarity between the two list of tests
	 */
	public double compareTestCase(DataRepresentation[] testList1, DataRepresentation[] testList2,
			PairwiseComparisonStrategy strategy, Aggregation aggregation) {
		List<Future<Object>> resultList = new ArrayList<>();
		// compare each item in t1 to each item in t2
		for (int i = 0; i < testList1.length; i++) {
			for (int y = i + 1; y < testList2.length; y++) {
				// y is set to i+1 too avoid duplicate comparisons
				Command c = new Command(strategy, testList1[i], testList2[y]);
				Future<Object> compairison = threadPool.submit(c);
				resultList.add(compairison);
			}
		}
		threadPool.shutdown();
		Double total = aggregation.aggregate(resultList);
		return total;
	}

}
