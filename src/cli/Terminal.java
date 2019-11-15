package cli;

//import core.Controller;

import core.Controller;
import model.JobDTO;

import java.util.Scanner;

public class Terminal {

    public void run(Controller controller) {

        Scanner input = new Scanner(System.in);
        System.out.print("Awaiting command: ");
        String command = input.nextLine();

        InputParser parser = new InputParser();
        JobDTO job = parser.parse(command);
        controller.executeAction(job);

    }

    public void printProgress() {
        // https://stackoverflow.com/questions/1001290/console-based-progress-in-java (Just use carriage return)
    }



}
