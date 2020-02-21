package model;

/**
 * Types of interfaces for use in logging
 *
 * @author crushton
 */
public enum InterfaceType {
    DATA_REPRESENTATION ("data representation"),
    AGGREGATION_STRATEGY ("aggregation strategy"),
    REPORT_FORMAT ("report format");

    private final String value;
    public String getName() {
        return value;
    }
    InterfaceType(String value) {
        this.value = value;
    }
}
