package model;

/**
 * Data transfer object used by the terminal to transfer request data to core.
 */

public class JobDTO {
    private String comparisonMethod;
    private String aggregationMethod;
    private String dataRepresentationFormat;
    private String resultFormat;
    private String dataLocation;
    private String reportLocation;

    public JobDTO() {
    }

    public JobDTO(String comparisonMethod, String aggregationMethod, String dataRepresentationFormat,
                  String resultFormat, String dataLocation, String reportLocation) {
        this.comparisonMethod = comparisonMethod;
        this.aggregationMethod = aggregationMethod;
        this.dataRepresentationFormat = dataRepresentationFormat;
        this.resultFormat = resultFormat;
        this.dataLocation = dataLocation;
        this.reportLocation = reportLocation;
    }

    public String getComparisonMethod() {
        return comparisonMethod;
    }

    public void setComparisonMethod(String comparisonMethod) {
        this.comparisonMethod = comparisonMethod;
    }

    public String getAggregationMethod() {
        return aggregationMethod;
    }

    public void setAggregationMethod(String aggregationMethod) {
        this.aggregationMethod = aggregationMethod;
    }

    public String getDataRepresentationFormat() {
        return dataRepresentationFormat;
    }

    public void setDataRepresentationFormat(String dataRepresentationFormat) {
        this.dataRepresentationFormat = dataRepresentationFormat;
    }

    public String getResultFormat() {
        return resultFormat;
    }

    public void setResultFormat(String resultFormat) {
        this.resultFormat = resultFormat;
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public void setDataLocation(String dataLocation) {
        this.dataLocation = dataLocation;
    }

    public String getReportLocation() {
        return reportLocation;
    }

    public void setReportLocation(String reportLocation) {
        this.reportLocation = reportLocation;
    }
}
