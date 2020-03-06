package metrics.report_format;

import core.HelpTarget;
import model.CompareDTO;
import model.DataTransferObject;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author crushton
 */
public interface ReportFormat extends HelpTarget {

    String DATE_FORMAT = "MM/dd/yyyy HH:mm";
    // The recommended maximum size to nicely print in the report format
    int SIMILARITY_THRESHOLD = 100;

    /**
     * Formats the similarity values, aggregations of the similarity values and data from the DTO into a convenient format.
     * @param dto The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    String format(CompareDTO dto, List<Double> similarities, List<Double> aggregations);

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

    default Map<String, String> getRunParameters(CompareDTO dto) {
        // Don't display the number of threads if we don't use a thread pool
        if (!dto.isUseThreadPool()) {
            dto.setNumberOfThreads(null);
        }

        Map<String, String> parameterValuePairs = new HashMap<>();
        try {
            Arrays.stream(Introspector.getBeanInfo(dto.getClass(), DataTransferObject.class)
                    .getPropertyDescriptors())
                    // Filter out properties that only have setters or are null
                    .filter(pd -> Objects.nonNull(pd.getReadMethod()))
                    .forEach(pd -> { // Invoke the method to get the value
                        try {
                            Object value = pd.getReadMethod().invoke(dto);
                            if (value != null) {
                                parameterValuePairs.put(pd.getName(), value.getClass().isArray()? Arrays.toString((String[]) value) : value.toString());
                            }
                        } catch (Exception ignore) {}
                    });
            return parameterValuePairs;
        } catch (IntrospectionException ignore) {
            return Collections.emptyMap();
        }
    }

    default Map<String, String> getAggregations(CompareDTO dto, List<String> aggregations) {
        Map<String, String> methodValuePair = new HashMap<>();
        String[] methods = dto.getAggregationMethods();

        // Assuming methods.length == aggregations.length
        for (int i = 0; i < aggregations.size(); i++) {
            methodValuePair.put(methods[i], aggregations.get(i));
        }
        return methodValuePair;
    }

    default String getReportHeader() {
        return "/******************" + System.lineSeparator() +
                " * Comparison Report" + System.lineSeparator() +
                " * User: " + getUser() + System.lineSeparator() +
                " * Host: " + getHost() + System.lineSeparator() +
                " * Date: " + getDateTime() + " (" + DATE_FORMAT + ")" + System.lineSeparator() +
                " ******************" + System.lineSeparator();
    }
}
