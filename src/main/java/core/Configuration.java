package core;

/**
 * A list of possible keys and values to use in the user preferences object if we want it to be persisted.
 *
 * Normally, the program will attempt to read a parameter specified by the user. If it's not given, the program will
 * check the user's preference file stored on their machine. If that file doesn't have the value, we use the backup value here.
 */
public enum Configuration {
    COMPARISON_METHOD ("CommonElements"),
    COMPARISON_METHOD_LOCATION ("metrics.comparison."),
    DATA_REPRESENTATION ("CSV"),
    DATA_REPRESENTATION_LOCATION ("data_representation."),
    DELIMITER ("\n"),
    AGGREGATION_METHOD ("AverageValue"),
    AGGREGATION_METHOD_LOCATION ("metrics.aggregation."),
    THREAD_COUNT ("3"),
    RESULT_ROUNDING_SCALE ("2"),
    RESULT_ROUNDING_MODE ("HALF_UP"),
    OUTPUT_FILE_NAME ("comparison_result"),
    OUTPUT_FILE_LOCATION ("/");

    private final String backupValue;

    Configuration(String s) {
        backupValue = s;
    }

    // A copy of the keys so that we can know what the valid configurations are.
    // We know we'll never mutate this, so we can keep a local copy.
    private static final Configuration[] copyOfValues = values();

    public static Configuration has(String name) {
        name = name.toUpperCase();
        for (Configuration value : copyOfValues) {
            if (value.name().equals(name)) {
                return value;
            }
        }
        return null;
    }

    public String getBackupValue() {
        return backupValue;
    }

}
