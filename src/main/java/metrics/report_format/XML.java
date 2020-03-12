package metrics.report_format;

import model.CompareDTO;

import java.util.List;
import java.util.Map;

/**
 * XML report format.
 * Note: If this file gets any more complicated, please think about adding an XML parser dependency.
 *
 * @author crushton
 */
public class XML implements ReportFormat {
    private final String SINGLE_INDENT = "\t";
    private final String DOUBLE_INDENT = "\t\t";
    private String indentation = "";
    private StringBuilder formattedData = new StringBuilder();

    /**
     * Formats the similarity values, aggregations of the similarity values and data from the DTO into a convenient format.
     *
     * @param dto          The data transfer object given to the controller to start comparison
     * @param similarities The similarity values found by the comparison service
     * @param aggregations The aggregated similarity values calculated by one or more aggregation strategies
     * @return A nicely formatted string
     */
    @Override
    public String format(CompareDTO dto, List<Double> similarities, List<List<Double>> aggregations) {

        append("<report>").append(System.lineSeparator());
        indentation = SINGLE_INDENT;
        append(createSingleLineXMLTag("username", getUser()));
        append(createSingleLineXMLTag("hostname", getHost()));
        append(createSingleLineXMLTag("datetime", getDateTime()));

        Map<String, String> runParamPairs = getRunParameters(dto);
        append("<parameters>").append(System.lineSeparator());
        indentation = DOUBLE_INDENT;
        for (String paramKey : runParamPairs.keySet()) {
            append(createSingleLineXMLTag(paramKey.toLowerCase(), runParamPairs.get(paramKey)));
        }
        indentation = SINGLE_INDENT;
        append("</parameters>").append(System.lineSeparator());

        if (similarities.size() < SIMILARITY_THRESHOLD) {
            append("<similarities>").append(System.lineSeparator());
            indentation = DOUBLE_INDENT;
            for (Double similarity : similarities) {
                append(createSingleLineXMLTag("similarity", similarity.toString()));
            }
            indentation = SINGLE_INDENT;
            append("</similarities>").append(System.lineSeparator());
        }

        Map<String, List<Double>> aggregationMethodValuePairs = getAggregations(dto, aggregations);
        append("<results>").append(System.lineSeparator());
        indentation = DOUBLE_INDENT;
        for (String method : aggregationMethodValuePairs.keySet()) {
            appendList(method.toLowerCase(), aggregationMethodValuePairs.get(method));
        }
        indentation = SINGLE_INDENT;
        append("</results>").append(System.lineSeparator());
        indentation = "";
        append("</report>").append(System.lineSeparator());

        return formattedData.toString();
    }

    /**
     * Provides a description of the format
     *
     * @return a brief description of the format
     */
    @Override
    public String getDescription() {
        return "Prints user info, run parameters, similarity values (up to " + SIMILARITY_THRESHOLD + "), and the results in XML format.";
    }

    /**
     * Creates a single line of xml.
     * @param tagName the tag name of the xml element that wraps the data.
     * @param data the data inside the tag
     * @return a string of formatted xml
     */
    private String createSingleLineXMLTag(String tagName, String data) {
        return "<" + tagName + "> " + data + " </" + tagName + ">" + System.lineSeparator();
    }

    /**
     * Appends data to the string builder
     * @param data the data to be appended
     * @return this object to be called again to chain append calls
     */
    private XML append(Object data) {
        formattedData.append(indentation).append(data);
        return this;
    }

    /**
     * Appends List of data to the string builder
     * @param data the data to be appended
     * @return this object to be called again to chain append calls
     */
    private XML appendList(String tagName, List<Double> data) {
        for(Double dataValue: data){
            formattedData.append(indentation).append("<" + tagName + "> ").append(dataValue).append(" </" + tagName + ">").append(System.lineSeparator());
        }
        return this;
    }
}
