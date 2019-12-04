package model;

import java.util.concurrent.Callable;
import data_representation.DataRepresentation;
import metrics.comparison.PairwiseComparisonStrategy;

public class Command implements Callable<Object>{
	
	private PairwiseComparisonStrategy compairison;
	private DataRepresentation Data1, Data2;
	
	public Command(PairwiseComparisonStrategy compairison, DataRepresentation t1, DataRepresentation t2) {
		this.compairison = compairison;
		this.Data1 = t1;
		this.Data2 = t2;
	}

	public PairwiseComparisonStrategy getCompairison() {
		return compairison;
	}

	public DataRepresentation getFirst() {
		return Data1;
	}

	public DataRepresentation getSecond() {
		return Data2;
	}

	@Override
	public Object call() throws Exception {
		return compairison.compare(Data1, Data2);
	}

}
