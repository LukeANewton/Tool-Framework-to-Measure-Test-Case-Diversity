import core.Controller;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class ComparisonApp {

    private void run(String[] args) {
        boolean exist = false;
        final String APP_NAME = this.getClass().getName();
        Preferences prefs = Preferences.userRoot().node(APP_NAME);

        try {
            exist = Preferences.userRoot().nodeExists(APP_NAME);
        } catch (BackingStoreException e) {
            System.err.println("Failed to contact storage of OS's preference manager. User preferences will be not be used.");
        }
        if (!exist) {
//            prefs.put(Settings.COMPARISON_METHOD.name(), "CommonElements");
//            prefs.put(Settings.COMPARISON_METHOD_LOCATION.name(), "metrics.comparison.");
//            prefs.put(Settings.DATA_REPRESENTATION.name(), "CSV");
//            prefs.put(Settings.DATA_REPRESENTATION_LOCATION.name(), "data_representation.");
//            prefs.put(Settings.DELIMITER.name(), "\n");
//            prefs.put(Settings.AGGREGATION_METHOD.name(), "AverageValue");
//            prefs.put(Settings.AGGREGATION_METHOD_LOCATION.name(), "metrics.aggregation.");
//            prefs.put(Settings.THREAD_COUNT.name(), "3");
//            prefs.put(Settings.RESULT_ROUNDING_SCALE.name(), "2");
//            prefs.put(Settings.RESULT_ROUNDING_MODE.name(), "HALF_UP");
//            prefs.put(Settings.OUTPUT_FILE_NAME.name(), "comparison_result");
//            prefs.put(Settings.OUTPUT_FILE_LOCATION.name(), "/");
            System.out.println("Preferences created.");
        }

        Controller controller = new Controller(prefs);
        /*in order to reuse the code from the Console, for now args must be
          concatenated, even though it is then tokenized again in the near future
         */
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) stringBuilder.append(arg).append(" ");

        controller.processCommand(stringBuilder.toString());
        System.exit(0);
    }

    /**
     * main function for the system
     *
     * @param args command line arguments
     */
    public static void main(String[] args){
        ComparisonApp app = new ComparisonApp();
        app.run(args);
    }
}
