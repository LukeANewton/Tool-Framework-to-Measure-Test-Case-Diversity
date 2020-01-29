package com.compare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.compare.core", "com.compare.metrics",
        "com.compare.data_representation", "com.compare.user_interface"})
public class ComparisonApp {

    private static final Logger log = LoggerFactory.getLogger(ComparisonApp.class);

    private void run(String[] args) {
//        Controller controller = Controller.getController();
//
//        if (controller != null) {
//            /*in order to reuse the code from the Console, for now args must be
//            concatenated, even though it is then tokenized again in the near future
//            */
//            StringBuilder stringBuilder = new StringBuilder();
//            for (String arg : args) {
//                stringBuilder.append(arg).append(" ");
//            }
//
////            controller.processCommand(stringBuilder.toString());
//        }

    }

    /**
     * main function for the system
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
//        com.compare.ComparisonApp app = new com.compare.ComparisonApp();
//        app.run(args);
//        System.exit(0);
        SpringApplication.run(ComparisonApp.class, args);
    }
}
