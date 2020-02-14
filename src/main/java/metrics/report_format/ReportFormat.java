package metrics.report_format;

import data_representation.DataRepresentation;
import model.CompareDTO;
import model.DataTransferObject;
import utilities.Tuple;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Interface for all report formats
 * @author crushton
 */
public interface ReportFormat {

    String DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
    // The recommended maximum size to nicely print in the report format
    int SIMILARITIES_SIZE_THRESHOLD = 100;
    // The default value of any null or undefined attributes
    String DEFAULT_VALUE = "Undefined";

    /**
     * Formats the similarity values, aggregations of the similarity values and data from the DTO into a convenient  format.
     * @param dto The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    String format(CompareDTO dto, List<Tuple<DataRepresentation, DataRepresentation>> pairs, List<Double> similarities, String[] aggregations);

    /**
     * Provides a description of the format
     * @return a brief description of the format
     */
    String getDescription();

    default String getUser() {
        return System.getProperty("user.name");
    }

    default String getHost() {
        String localMachine = "Unknown";
        try {
            localMachine = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignore) {}
        return localMachine;
    }

    default String getDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    default Map<String, String> getRunParameters(DataTransferObject dto) {
        Map<String, String> parameterValuePairs = new HashMap<>();
        try {
            Arrays.stream(Introspector.getBeanInfo(dto.getClass(), DataTransferObject.class)
                    .getPropertyDescriptors())
                    // Filter out properties that only have setters
                    .filter(pd -> Objects.nonNull(pd.getReadMethod()))
                    .forEach(pd -> { // Invoke the method to get the value
                        try {
                            Object value = pd.getReadMethod().invoke(dto);
                            if (value != null) {
                                parameterValuePairs.put(pd.getName(), value.toString());
                            } else { // Give the caller as much data as possible without making them work too hard
                                parameterValuePairs.put(pd.getName(), DEFAULT_VALUE);
                            }
                        } catch (Exception ignore) {}
                    });
            return parameterValuePairs;
        } catch (IntrospectionException ignore) {
            return Collections.emptyMap();
        }
    }

    default Map<String, String> getAggregations(CompareDTO dto, String[] aggregations) {
        Map<String, String> methodValuePair = new HashMap<>();
        String[] methods = dto.getAggregationMethods();

        // Assuming methods.length == aggregations.length
        for (int i = 0; i < aggregations.length; i++) {
            methodValuePair.put(methods[i], aggregations[i]);
        }
        return methodValuePair;
    }

    default String getReportHeader() {
        return "/******************" + System.lineSeparator() +
                " * Comparison Report" + System.lineSeparator() +
                " * User: " + getUser() + System.lineSeparator() +
                " * Host: " + getHost() + System.lineSeparator() +
                " * Date (" + DATE_FORMAT + "): " + getDateTime() +
                " ******************" + System.lineSeparator();
    }
}
