package main.java.core;

import main.java.console.Console;
import main.java.model.JobDTO;

public class Controller {

    public Controller() {
    }

    public String getStatus() {
        return "status is: executing.";
    }

    public void run() {
        // Start the console to receive input
        Console gui = new Console();
        JobDTO job = gui.receiveInput();
        while (!job.getCommand().equals("quit")) {
            System.out.println("Input received: " + job.toString());
            // Start processing input here
            job = gui.receiveInput();
        }

    }

}
