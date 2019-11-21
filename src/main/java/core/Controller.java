package core;

import model.JobDTO;
import user_interface.Console;

public class Controller {

    public Controller() {
    }

    public String getStatus() {
        return "status is: executing.";
    }

    public void run() {
        // Start the user_interface to receive input
        Console gui = new Console();
        JobDTO job = gui.receiveInput();
        while (!job.getCommand().equals("quit")) {
            System.out.println("Input received: " + job.toString());
            // Start processing input here
            job = gui.receiveInput();
        }

    }

}
