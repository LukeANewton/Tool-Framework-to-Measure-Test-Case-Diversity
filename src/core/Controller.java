package core;

import cli.Terminal;

public class Controller {
    
    public Controller() {
    }

    public void executeAction(String action) {
        System.out.println("Executing action '" + action + "'.");
    }

    public String getStatus() {
        return "status is: executing.";
    }

    public void run() {
        Terminal gui = new Terminal();
        gui.run();
    }

}
