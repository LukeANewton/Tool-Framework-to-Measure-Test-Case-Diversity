package model;

/**
 * An enumeration of the possible types of help offered by the system, used in the HelpDTO.
 * AggregationMethod would return a list of the existing aggregation methods in the system
 * DataRepresentation would return a list of the existing data representations in the system
 * PairwiseMetric would return a list of the valid pairwise diversity metrics in the system
 * Command would return a list of the valid commands the system has
 * 
 * @author luke
 *
 */
public enum HelpType {
	DATA_REPRESENTATION ("data representation"),
	AGGREGATION_METHOD ("aggregation method"),
	REPORT_FORMAT ("report format"),
	METRIC ("pairwise metric", "listwise metric"),
	COMMAND ("command");

	private final String[] values;
	public String[] getNames() {
		return values;
	}
	public String getName() {
		return values[0];
	}
	HelpType(String... values) {
		this.values = values;
	}
}
