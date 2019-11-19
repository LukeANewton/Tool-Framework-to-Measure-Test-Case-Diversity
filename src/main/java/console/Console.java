package main.java.console;

import main.java.model.JobDTO;

import java.util.Scanner;

public class Console {

    public JobDTO receiveInput() {

        Scanner input = new Scanner(System.in);

        System.out.print("Awaiting command: ");
        String command = input.nextLine();

        InputParser parser = new InputParser();

        // Do we want to notify Controller of quit and help requests (ie requests that dont trigger comparison actions)?
        return parser.parse(command);
    }

    public void printProgress() {
        // https://stackoverflow.com/questions/1001290/console-based-progress-in-java (Just use carriage return)
    }

}
