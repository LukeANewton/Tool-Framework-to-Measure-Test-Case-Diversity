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
	private String pairwiseMethod;
	//the method to aggregate all pairwise diversity values into a single value
	private String aggregationMethod;
	//the location of the first test case/suite
	private String testCaseLocationOne;
	//the location of the second test case/suite
	private String testCaseLocationTwo;
	//the number of threads to use in a thread pool for pairwise comparisons
	private Integer numberOfThreads;

	//true to use a thread pool, false to handle tasks sequentially
	private boolean useThreadPool;
	//in the case of multiple test cases in a single file, specify the delimiter between them
	private String delimiter;
	//the DataRepresentation test cases are formatted to
	private String dataRepresentation;
	//the location to save results to
	private String outputFilename;
	
	/**Constructor*/
	public CompareDTO() {
		commandType = CommandType.Compare;
		useThreadPool = false;
	}

	public String getPairwiseMethod() {
		return pairwiseMethod;
	}

	public void setPairwiseMethod(String pairwiseMethod) {
		this.pairwiseMethod = pairwiseMethod;
	}

	public String getAggregationMethod() {
		return aggregationMethod;
	}

	public void setAggregationMethod(String aggregationMethod) {
		this.aggregationMethod = aggregationMethod;
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
}
