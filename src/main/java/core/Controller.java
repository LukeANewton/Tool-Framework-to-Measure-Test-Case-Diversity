package core;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import model.Config;
import model.DataTransferObject;
import user_interface.Console;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Controller {

    final String CONFIG_FILE = "config.json";

    public Controller() {
    }

    public String getStatus() {
        return "status is: executing.";
    }

    public void run() {
        // Start the user_interface
        Console console = new Console();

        // Load config.json
        JsonReader jsonReader = null;
        try {
            jsonReader = new JsonReader(new FileReader(CONFIG_FILE));
        } catch (FileNotFoundException e) {
            //console.displayResults("Unable to load file '" + CONFIG_FILE + "'.");
            e.printStackTrace();
        }
        Gson gson = new Gson();
        if (jsonReader != null) {
            Config config = gson.fromJson(jsonReader, Config.class);
        }

        DataTransferObject job = console.receiveInput();
        while (!job.getCommandType().equals("quit")) {
            //console.displayResults("Input received: " + job.toString());
            // Start processing input here
            job = console.receiveInput();
        }

    }

}
