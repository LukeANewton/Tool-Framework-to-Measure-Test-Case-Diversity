import core.Controller;

public class ComparisonApp {
    private void run(String[] args) {
        Controller controller = Controller.getController();

        if (controller != null) {
            /*in order to reuse the code from the Console, for now args must be
            concatenated, even though it is then tokenized again in the near future
            */
            StringBuilder stringBuilder = new StringBuilder();
            for (String arg : args) stringBuilder.append(arg).append(" ");

            controller.processCommand(stringBuilder.toString());
        }
    }

    /**
     * main function for the system
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        ComparisonApp app = new ComparisonApp();
        app.run(args);
        System.exit(0);
    }
}
