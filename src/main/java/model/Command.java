package model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.Callable;
import data_representation.DataRepresentation;
import metrics.comparison.PairwiseComparisonStrategy;

/**
 * Commands will contain the information needed to compare two tests
 * 
 * @author Eric
 *
 */
public class Command implements Callable<Object>{
	private PairwiseComparisonStrategy compairison;
	private DataRepresentation Data1, Data2;
	private PropertyChangeSupport support;
	
	public Command(PairwiseComparisonStrategy compairison, DataRepresentation t1,
				   DataRepresentation t2, PropertyChangeListener pcl) {
		this.compairison = compairison;
		this.Data1 = t1;
		this.Data2 = t2;
		support = new PropertyChangeSupport(this);
		if (pcl != null)
			support.addPropertyChangeListener(pcl);
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

	/**
	 * calculate the difference between two give tests
	 * 
	 * @return a double representing the similarity between the two tests
	 */
	public Object call() throws Exception {
		double result = compairison.compare(Data1, Data2);
		Thread.sleep(500);
		support.firePropertyChange(new PropertyChangeEvent(this, "complete",
				null, null));
		return result;
	}

}
