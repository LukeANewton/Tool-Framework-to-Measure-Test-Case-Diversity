package model;

import java.util.concurrent.Callable;
import data_representation.DataRepresentation;
import metrics.comparison.PairwiseComparisonStrategy;

/**
 * PairwiseCommands will contain the information needed to compare two tests
 * 
 * @author Eric
 *
 */
public class PairwiseCommand implements Callable<Object>{
	private PairwiseComparisonStrategy comparison;
	private DataRepresentation Data1, Data2;
	
	public PairwiseCommand(PairwiseComparisonStrategy comparison, DataRepresentation t1, DataRepresentation t2) {
		this.comparison = comparison;
		this.Data1 = t1;
		this.Data2 = t2;
	}

	public PairwiseComparisonStrategy getComparison() {
		return comparison;
	}

	public DataRepresentation getFirst() {
		return Data1;
	}

	public DataRepresentation getSecond() {
		return Data2;
	}

	/**
	 * calculate the difference between two give tests
	 * 
	 * @return a double representing the similarity between the two tests
	 */
	public Object call() throws Exception {
		return comparison.compare(Data1, Data2);
	}

}
