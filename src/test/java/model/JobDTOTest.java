package model;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JobDTOTest {

    private String command = "Job";
    private String aggregationMethod = "AggregationMethod";
    private String comparisonMethod = "ComparisonMethod";
    private String dataLocation = "DataLocation";
    private String dataRepresentationFormat = "DataRepresentationFormat";
    private String reportLocation = "ReportLocation";
    private String resultFormat = "ResultFormat";
    private String toString = "JobDTO{" +
            "command='" + command + '\'' +
            ", comparisonMethod='" + comparisonMethod + '\'' +
            ", aggregationMethod='" + aggregationMethod + '\'' +
            ", dataRepresentationFormat='" + dataRepresentationFormat + '\'' +
            ", resultFormat='" + resultFormat + '\'' +
            ", dataLocation='" + dataLocation + '\'' +
            ", reportLocation='" + reportLocation + '\'' +
            '}';

    @Test
    /**
     * test setting all variables and check that CommandType is correctly set
     */
    public void testJobDTO(){
        JobDTO job = new JobDTO();
        job.setAggregationMethod(aggregationMethod);
        job.setCommand(command);
        job.setComparisonMethod(comparisonMethod);
        job.setDataLocation(dataLocation);
        job.setDataRepresentationFormat(dataRepresentationFormat);
        job.setReportLocation(reportLocation);
        job.setResultFormat(resultFormat);

        assertTrue("AggregationMethod should be "+ aggregationMethod +", but is: " + job.getAggregationMethod(), Objects.equals(job.getAggregationMethod(), aggregationMethod));
        assertTrue("Command should be "+ command +", but is: " + job.getCommand(), Objects.equals(job.getCommand(), command));
        assertTrue("ComparisonMethod should be "+ comparisonMethod +", but is: " + job.getComparisonMethod(), Objects.equals(job.getComparisonMethod(), comparisonMethod));
        assertTrue("DataLocation should be "+ dataLocation +", but is: " + job.getDataLocation(), Objects.equals(job.getDataLocation(), dataLocation));
        assertTrue("DataRepresentationFormat should be "+ dataRepresentationFormat +", but is: " + job.getDataRepresentationFormat(), Objects.equals(job.getDataRepresentationFormat(), dataRepresentationFormat));
        assertTrue("ReportLocation should be "+ reportLocation +", but is: " + job.getReportLocation(), Objects.equals(job.getReportLocation(), reportLocation));
        assertTrue("ResultFormat should be "+ resultFormat +", but is: " + job.getResultFormat(), Objects.equals(job.getResultFormat(), resultFormat));
    }

    @Test
    /**
     * test setting all variables via constructor and check that CommandType is correctly set
     */
    public void testConstructorJobDTO(){
        JobDTO job = new JobDTO(command, comparisonMethod, aggregationMethod, dataRepresentationFormat, resultFormat, dataLocation, reportLocation);

        assertTrue("AggregationMethod should be "+ aggregationMethod +", but is: " + job.getAggregationMethod(), Objects.equals(job.getAggregationMethod(), aggregationMethod));
        assertTrue("Command should be "+ command +", but is: " + job.getCommand(), Objects.equals(job.getCommand(), command));
        assertTrue("ComparisonMethod should be "+ comparisonMethod +", but is: " + job.getComparisonMethod(), Objects.equals(job.getComparisonMethod(), comparisonMethod));
        assertTrue("DataLocation should be "+ dataLocation +", but is: " + job.getDataLocation(), Objects.equals(job.getDataLocation(), dataLocation));
        assertTrue("DataRepresentationFormat should be "+ dataRepresentationFormat +", but is: " + job.getDataRepresentationFormat(), Objects.equals(job.getDataRepresentationFormat(), dataRepresentationFormat));
        assertTrue("ReportLocation should be "+ reportLocation +", but is: " + job.getReportLocation(), Objects.equals(job.getReportLocation(), reportLocation));
        assertTrue("ResultFormat should be "+ resultFormat +", but is: " + job.getResultFormat(), Objects.equals(job.getResultFormat(), resultFormat));
    }

    @Test
    /**
     * test setting no variables and check that no variables are pre-set
     */
    public void testEmptyJobDTO(){
        JobDTO job = new JobDTO();

        assertNull("AggregationMethod should be null, but is: " + job.getAggregationMethod(), job.getAggregationMethod());
        assertNull("Command should be null, but is: " + job.getCommand(), job.getCommand());
        assertNull("ComparisonMethod should be null, but is: " + job.getComparisonMethod(), job.getComparisonMethod());
        assertNull("DataLocation should be null, but is: " + job.getDataLocation(), job.getDataLocation());
        assertNull("DataRepresentationFormat should be null, but is: " + job.getDataRepresentationFormat(), job.getDataRepresentationFormat());
        assertNull("ReportLocation should be null, but is: " + job.getReportLocation(), job.getReportLocation());
        assertNull("ResultFormat should be null, but is: " + job.getResultFormat(), job.getResultFormat());
    }

    @Test
    /**
     * test toString
     */
    public void testToStringJobDTO(){
        JobDTO job = new JobDTO(command, comparisonMethod, aggregationMethod, dataRepresentationFormat, resultFormat, dataLocation, reportLocation);

        assertTrue("String should be "+ toString +", but is: " + job.toString(), Objects.equals(job.toString(), toString));
    }
}
