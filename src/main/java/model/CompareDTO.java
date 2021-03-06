package model;

/**
 * a data transfer object for issuing compare commands to the Controller.
 * Compare commands are the main functionality of the system and compare 2 
 * or more test cases in a test suite.
 * 
 * @author luke
 *
 */
public class CompareDTO extends DataTransferObject {
	//the pairwise diversity metric to use to compare pairs of test cases
	private String comparisonMethod;
	//the method to aggregate all pairwise diversity values into a single value
	private String[] aggregationMethods;
	//the location of the first test case/suite
	private String testCaseLocationOne;
	//the location of the second test case/suite
	private String testCaseLocationTwo;
	//the number of threads to use in a thread pool for pairwise comparisons
	private Integer numberOfThreads;
	//the desired report formats
	private String[] reportFormats;

	//true to use a thread pool, false to handle tasks sequentially
	private boolean useThreadPool;
	//in the case of multiple test cases in a single file, specify the delimiter between them
	private String delimiter;
	//the DataRepresentation test cases are formatted to
	private String dataRepresentation;
	//the location to save results to
	private String outputFilename;
	//true to save the file
	private boolean save = false;
	
	/**Constructor*/
	public CompareDTO() {
		commandType = CommandType.Compare;
		useThreadPool = false;
	}

	public String getComparisonMethod() {
		return comparisonMethod;
	}

	public void setComparisonMethod(String comparisonMethod) {
		this.comparisonMethod = comparisonMethod;
	}

	public String[] getAggregationMethods() {
		return aggregationMethods;
	}

	public void setAggregationMethods(String[] aggregationMethods) {
		this.aggregationMethods = aggregationMethods;
	}

	public String getTestCaseLocationOne() {
		return testCaseLocationOne;
	}

	public void setTestCaseLocationOne(String testCaseLocationOne) {
		this.testCaseLocationOne = testCaseLocationOne;
	}

	public String getTestCaseLocationTwo() {
		return testCaseLocationTwo;
	}

	public void setTestCaseLocationTwo(String testCaseLocationTwo) {
		this.testCaseLocationTwo = testCaseLocationTwo;
	}

	public Integer getNumberOfThreads() {
		return numberOfThreads;
	}

	public void setNumberOfThreads(Integer numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getDataRepresentation() {
		return dataRepresentation;
	}

	public void setDataRepresentation(String dataRepresentation) {
		this.dataRepresentation = dataRepresentation;
	}

	public String getOutputFilename() {
		return outputFilename;
	}

	public void setOutputFilename(String outputFilename) {
		this.outputFilename = outputFilename;
	}

	public boolean isUseThreadPool() {
		return useThreadPool;
	}

	public void setUseThreadPool(boolean useThreadPool) {
		this.useThreadPool = useThreadPool;
	}
	
	public String[] getReportFormats() {
		return reportFormats;
	}

	public void setReportFormats(String[] reportFormats) {
		this.reportFormats = reportFormats;
	}

	public boolean getSave() { return save; }

	public void setSave(boolean save) { this.save = save; }
}
