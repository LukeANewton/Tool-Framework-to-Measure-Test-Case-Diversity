package model;

/**
 * Data transfer object used by a user interface to transfer request data to test.java.core.
 */

public class JobDTO {
    private String command;
    private String comparisonMethod;
    private String aggregationMethod;
    private String dataRepresentationFormat;
    private String resultFormat;
    private String dataLocation;
    private String reportLocation;

    public JobDTO() {
    }

    public JobDTO(String command, String comparisonMethod, String aggregationMethod, String dataRepresentationFormat,
                  String resultFormat, String dataLocation, String reportLocation) {
        this.command = command;
        this.comparisonMethod = comparisonMethod;
        this.aggregationMethod = aggregationMethod;
        this.dataRepresentationFormat = dataRepresentationFormat;
        this.resultFormat = resultFormat;
        this.dataLocation = dataLocation;
        this.reportLocation = reportLocation;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
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

    @Override
    public String toString() {
        return "JobDTO{" +
                "command='" + command + '\'' +
                ", comparisonMethod='" + comparisonMethod + '\'' +
                ", aggregationMethod='" + aggregationMethod + '\'' +
                ", dataRepresentationFormat='" + dataRepresentationFormat + '\'' +
                ", resultFormat='" + resultFormat + '\'' +
                ", dataLocation='" + dataLocation + '\'' +
                ", reportLocation='" + reportLocation + '\'' +
                '}';
    }
}
