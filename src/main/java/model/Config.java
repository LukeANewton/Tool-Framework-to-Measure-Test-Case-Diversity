package model;

/**
 * Default parameters to be loaded from config.json
 *
 * @author crushton
 */
public class Config {
    String comparisonMethod;
    String comparisonMethodLocation;
    String dataRepresentation;
    String getDataRepresentationLocation;
    char delimiter; // delimiter used when separating values for comparison
    String aggregationMethod;
    String aggregationMethodLocation;
    String reportFormat;
    String reportFormatLocation;
    int numThreads;
    int resultRoundingScale; // Scale used when calling setScale() on BigDecimals
    String resultRoundingMode; // Rounding mode used when calling setScale() on BigDecimals
    String outputFileName;
    String outputFileLocation;


    public String getComparisonMethod() {
        return comparisonMethod;
    }

    public void setComparisonMethod(String comparisonMethod) {
        this.comparisonMethod = comparisonMethod;
    }

    public String getComparisonMethodLocation() {
        return comparisonMethodLocation;
    }

    public void setComparisonMethodLocation(String comparisonMethodLocation) {
        this.comparisonMethodLocation = comparisonMethodLocation;
    }

    public String getDataRepresentation() {
        return dataRepresentation;
    }

    public void setDataRepresentation(String dataRepresentation) {
        this.dataRepresentation = dataRepresentation;
    }

    public String getGetDataRepresentationLocation() {
        return getDataRepresentationLocation;
    }

    public void setGetDataRepresentationLocation(String getDataRepresentationLocation) {
        this.getDataRepresentationLocation = getDataRepresentationLocation;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
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

    public String getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(String reportFormat) {
        this.reportFormat = reportFormat;
    }

    public String getReportFormatLocation() {
        return reportFormatLocation;
    }

    public void setReportFormatLocation(String reportFormatLocation) {
        this.reportFormatLocation = reportFormatLocation;
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
                "comparisonMethod='" + comparisonMethod + '\'' +
                ", comparisonMethodLocation='" + comparisonMethodLocation + '\'' +
                ", dataRepresentation='" + dataRepresentation + '\'' +
                ", getDataRepresentationLocation='" + getDataRepresentationLocation + '\'' +
                ", delimiter=" + delimiter +
                ", aggregationMethod='" + aggregationMethod + '\'' +
                ", aggregationMethodLocation='" + aggregationMethodLocation + '\'' +
                ", reportFormat='" + reportFormat + '\'' +
                ", reportFormatLocation='" + reportFormatLocation + '\'' +
                ", numThreads=" + numThreads +
                ", resultRoundingScale=" + resultRoundingScale +
                ", resultRoundingMode='" + resultRoundingMode + '\'' +
                ", outputFileName='" + outputFileName + '\'' +
                ", outputFileLocation='" + outputFileLocation + '\'' +
                '}';
    }
}
