import core.Controller;

import java.util.prefs.Preferences;

public class ComparisonApp {

    private void run(String[] args) {
        final String APP_NAME = this.getClass().getName();
        Preferences prefs = Preferences.userRoot().node(APP_NAME);

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
