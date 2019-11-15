package core;

import cli.Terminal;
import model.JobDTO;

public class Controller {

    public Controller() {
    }

    public void executeAction(JobDTO job) {
        System.out.println("Executing job");
    }

    public String getStatus() {
        return "status is: executing.";
    }

    public void run() {
        Terminal gui = new Terminal();
        gui.run(this);
    }

}
