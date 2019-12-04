package core;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import data_representation.DataRepresentation;
import metrics.aggregation.Aggregation;
import metrics.comparison.PairwiseComparisonStrategy;
import model.Command;

public class Camparator {
	private int threads;
	private ExecutorService threadPool;
	
	public Camparator(int threads) {
		this.threads = threads;
		threadPool = Executors.newFixedThreadPool(threads);
	}
	
	public double CompareTestCase(DataRepresentation t1, DataRepresentation t2, PairwiseComparisonStrategy strategy) throws InterruptedException, ExecutionException {
		Command c = new Command(strategy, t1, t2);
		Future<Object> result = threadPool.submit(c);
		threadPool.awaitTermination(5, TimeUnit.SECONDS);
		return (double) result.get();
	}
	
	public double CompareTestCase(DataRepresentation[] t1, DataRepresentation[] t2, PairwiseComparisonStrategy strategy, Aggregation a) {
		
		 List<Future<Object>> resultList = new ArrayList<>();
		
		for(int i = 0; i<t1.length; i++) {
			for(int y = 0; y<t2.length; y++) {
				Command c = new Command(strategy, t1[i], t2[y]);
				Future<Object> compairison = threadPool.submit(c);
				resultList.add(compairison);
			}
		}
		
		threadPool.awaitTermination(5, TimeUnit.SECONDS);
		
		Double total = a.aggregate(resultList);
		return total;
	}

}
