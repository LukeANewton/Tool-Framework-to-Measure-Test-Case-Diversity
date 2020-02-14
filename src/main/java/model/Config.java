package model;

/**
 * Default parameters to be loaded from config.json
 *
 * @author crushton
 */
public class Config {
    private String listwiseMethod;
    private String listwiseMethodLocation;
    private String pairwiseMethod;
    private String pairwiseMethodLocation;
    private String dataRepresentation;
    private String dataRepresentationLocation;
    private String delimiter; // delimiter used when separating values for comparison
    private String aggregationMethod;
    private String aggregationMethodLocation;
    private int numThreads;
    private int resultRoundingScale; // Scale used when calling setScale() on BigDecimals
    private String resultRoundingMode; // Rounding mode used when calling setScale() on BigDecimals
    private String outputFileName;
    private String outputFileLocation;

    public Config() {
    }

    public String getListwiseMethod() {
        return listwiseMethod;
}

    public void setListwiseMethod(String listwiseMethod) {
        this.listwiseMethod = listwiseMethod;
    }

    public String getListwiseMethodLocation() {
        return listwiseMethodLocation;
    }

    public void setListwiseMethodLocation(String listwiseMethodLocation) {
        this.listwiseMethodLocation = listwiseMethodLocation;
    }

    public String getPairwiseMethod() {
        return pairwiseMethod;
    }

    public void setPairwiseMethod(String pairwiseMethod) {
        this.pairwiseMethod = pairwiseMethod;
    }

    public String getPairwiseMethodLocation() {
        return pairwiseMethodLocation;
    }

    public void setPairwiseMethodLocation(String pairwiseMethodLocation) {
        this.pairwiseMethodLocation = pairwiseMethodLocation;
    }

    public String getDataRepresentation() {
        return dataRepresentation;
    }

    public void setDataRepresentation(String dataRepresentation) {
        this.dataRepresentation = dataRepresentation;
    }

    public String getDataRepresentationLocation() {
        return dataRepresentationLocation;
    }

    public void setDataRepresentationLocation(String getDataRepresentationLocation) {
        this.dataRepresentationLocation = getDataRepresentationLocation;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getAggregationMethod() {
        return aggregationMethod;
    }

    public void setAggregationMethod(String aggregationMethod) {
        this.aggregationMethod = aggregationMethod;
    }

    public String getAggregationMethodLocation() {
        return aggregationMethodLocation;
    }

    public void setAggregationMethodLocation(String aggregationMethodLocation) {
        this.aggregationMethodLocation = aggregationMethodLocation;
    }

    public int getNumThreads() {
        return numThreads;
    }

    public void setNumThreads(int numThreads) {
        this.numThreads = numThreads;
    }

    public int getResultRoundingScale() {
        return resultRoundingScale;
    }

    public void setResultRoundingScale(int resultRoundingScale) {
        this.resultRoundingScale = resultRoundingScale;
    }

    public String getResultRoundingMode() {
        return resultRoundingMode;
    }

    public void setResultRoundingMode(String resultRoundingMode) {
        this.resultRoundingMode = resultRoundingMode;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFileLocation() {
        return outputFileLocation;
    }

    public void setOutputFileLocation(String outputFileLocation) {
        this.outputFileLocation = outputFileLocation;
    }

    @Override
    public String toString() {
        return "Config{" +
                "listwiseMethod='" + listwiseMethod + '\'' +
                ", listwiseMethodLocation='" + listwiseMethodLocation + '\'' +
                ", pairwiseMethod='" + pairwiseMethod + '\'' +
                ", pairwiseMethodLocation='" + pairwiseMethodLocation + '\'' +
                ", dataRepresentation='" + dataRepresentation + '\'' +
                ", dataRepresentationLocation='" + dataRepresentationLocation + '\'' +
                ", delimiter=" + delimiter +
                ", aggregationMethod='" + aggregationMethod + '\'' +
                ", aggregationMethodLocation='" + aggregationMethodLocation + '\'' +
                ", numThreads=" + numThreads +
                ", resultRoundingScale=" + resultRoundingScale +
                ", resultRoundingMode='" + resultRoundingMode + '\'' +
                ", outputFileName='" + outputFileName + '\'' +
                ", outputFileLocation='" + outputFileLocation + '\'' +
                '}';
    }
}
