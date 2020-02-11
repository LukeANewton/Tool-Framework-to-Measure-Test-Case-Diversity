package model;

import data_representation.DataRepresentation;
import metrics.comparison.pairwise.PairwiseComparisonStrategy;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Callable;

/**
 * PairwiseCommands will contain the information needed to compare two tests
 *
 * @author Eric
 *
 */
public class PairwiseCommand implements Callable<Object> {
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

	/**
	 * calculate the difference between two give tests
	 *
	 * @return a double representing the similarity between the two tests
	 */
	public Object call() throws Exception {
		if(!Data1.getClass().equals(Data2.getClass()))
			throw new TestCaseFormatMismatchException();

		double result = comparison.compare(Data1, Data2);
		support.firePropertyChange(new PropertyChangeEvent(this, "complete",
				null, null));
		return result;
	}
}