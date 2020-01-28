package model;

import data_representation.DataRepresentation;
import metrics.comparison.PairwiseComparisonStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * PairwiseCommands will contain the information needed to compare two tests
 *
 * @author Eric
 *
 */
public class PairwiseCommand implements Command{
	private PairwiseComparisonStrategy comparison;
	private DataRepresentation Data1, Data2;
	private PropertyChangeSupport support;

	public PairwiseCommand(PairwiseComparisonStrategy comparison, DataRepresentation t1, DataRepresentation t2,
						   PropertyChangeListener pcl) {
		this.comparison = comparison;
		this.Data1 = t1;
		this.Data2 = t2;
		support = new PropertyChangeSupport(this);
		if (pcl != null)
			support.addPropertyChangeListener(pcl);
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
		double result = comparison.compare(Data1, Data2);
		support.firePropertyChange(new PropertyChangeEvent(this, "complete",
				null, null));
		return result;
	}
}