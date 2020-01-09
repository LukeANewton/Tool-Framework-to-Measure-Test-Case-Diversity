package core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
public class Comparator {
	private ExecutorService threadPool;

	public Comparator(int threads) {
		threadPool = Executors.newFixedThreadPool(threads);
	}

	/**
	 * Compares the two tests provided using the given comparison strategy
	 * 
	 * @param test1    the first test too compare
	 * @param test2    the second test to compare
	 * @param strategy the strategy used to compare the tests
	 * @return a double representing the similarity between the two tests
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public double compareTestCase(DataRepresentation test1, DataRepresentation test2,
			PairwiseComparisonStrategy strategy) throws InterruptedException, ExecutionException {
		Command command = new Command(strategy, test1, test2);
		Future<Object> result = threadPool.submit(command);
		threadPool.shutdown();
		return (Double) result.get();
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
		List<Future<Object>> futureList = new ArrayList<>();
		for (int i = 0; i < testList1.length; i++) {
			for (int y = 0; y < testList2.length; y++) {
				Command c = new Command(strategy, testList1[i], testList2[y]);
				Future<Object> comparison = threadPool.submit(c);
				futureList.add(comparison);
			}
		}
		threadPool.shutdown();
		ArrayList<Double> results = new ArrayList<Double>();
		for (Future<Object> future : futureList) {
			try {
				results.add((Double) future.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO decide how to throw exceptions
				e.printStackTrace();
			}
		}
		Double total = aggregation.aggregate(results);
		return total;
	}

}
