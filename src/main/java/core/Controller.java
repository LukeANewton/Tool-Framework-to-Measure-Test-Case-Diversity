package core;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import model.*;
import user_interface.Console;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * The Controller is the main logic for the program. It pieces together the different services to
 * achieve each system command.
 *
 * @author luke
 */
public class Controller {
    //the name of the configuration file containing system defaults
    private static final String CONFIG_FILE = "config.json";
    //configuration object containing config file values
    private Config config;
    //console to obtain user input and display output
    private Console console;
    //service to load class instances
    private ReflectionService reflectionService;
    //service to read files into system
    private FileReaderService fileReaderService;
    //service to generate pairs of test cases from test suite(s)
    private PairingService pairingService;
    //service to perform comparisons
    private ComparisonService comparisonService;

    /**Constructor*/
    public Controller() {
        //read config file
        try {
            config = readConfig(CONFIG_FILE);
        } catch (FileNotFoundException e) {
            System.err.println("Config file: " + CONFIG_FILE + " not found.");
            e.printStackTrace();
            System.exit(-1);
        }

        console = new Console();
        reflectionService = new ReflectionService();
        fileReaderService = new FileReaderService();
        pairingService = new PairingService();
        comparisonService = new ComparisonService(config.getNumThreads());
    }

    /**
     * reads a configuration file from the passed filename
     * @param filename the name of the configuration file
     * @throws FileNotFoundException thrown when passed filename can not be found
     * @return a Config object containing all the information from the configuration file
     */
    private Config readConfig(String filename) throws FileNotFoundException {
        JsonReader jsonReader = new JsonReader(new FileReader(filename));
        Gson gson = new Gson();
        return config = gson.fromJson(jsonReader, Config.class);
    }

    /**
     * writes a Config object to a file
     *
     * @param filename the name of the file to write to
     * @param config the Config object to write to file
     * @throws IOException when there is an error with the FileWriter
     */
    private void writeConfig(String filename, Config config) throws IOException {
        FileWriter writer = null;
        Gson gson = new Gson();
        writer = new FileWriter(filename);
        gson.toJson(config, writer); // Write to json file
        Objects.requireNonNull(writer).close();
    }

    public void processCommand(String command){
        DataTransferObject dto = console.processInstruction(command);
        CommandType commandType = dto.getCommandType();
        switch(commandType){
            case Exit:
            case Update:
                console.displayResults("This command remains unimplemented while there is no REPL");
                break;
            case Help:
                processHelpCommand((HelpDTO)dto);
                break;
            case Configure:
                processConfigureCommand((ConfigDTO)dto);
                break;
            case Compare:
                processCompareCommand((CompareDTO)dto);
                break;
        }
    }

    /**
     * performs a "compare" operation that is characterized by the contents of the passed CompareDTO
     *
     * @param dto data transfer object containing information on the requested compare command
     */
    private void processCompareCommand(CompareDTO dto) {
    }

    /**
     * performs a "configure" operation that is characterized by the contents of the passed ConfigDTO
     *
     * @param dto data transfer object containing information on the requested configure command
     */
    private void processConfigureCommand(ConfigDTO dto) {
    }

    /**
     * performs a "help" operation that is characterized by the contents of the passed HelpDTO
     *
     * @param dto data transfer object containing information on the requested help command
     */
    private void processHelpCommand(HelpDTO dto) {
        HelpType helpType = dto.getHelpType();
        StringBuilder result = new StringBuilder();

        String packageName = null;
        String interfaceName = null;
        switch(helpType){//determine which type of help is needed, each works the same way except for Command
            case Command:
                result.append("\tcompare\n");
                result.append("\tperforms a diversity measurement within a test suite, or between test suites\n\n");
                result.append("\tconfig\n");
                result.append("\tsets the value of parameters read from the configuration file\n\n");
                result.append("\thelp\n");
                result.append("\tlists information on the requested topic\n\n");
                result.append("\tupdate\n");
                result.append("\tnot implemented\n\n");
                result.append("\texit\n");
                result.append("\tnot implemented");
                console.displayResults(result.toString());
                return;
            case PairwiseMetric:
                packageName = "metrics.comparison";
                interfaceName = "PairwiseComparisonStrategy";
                break;
            case AggregationMethod:
                packageName = "metrics.aggregation";
                interfaceName = "AggregationStrategy";
                break;
            case DataRepresentation:
                packageName = "data_representation";
                interfaceName = "DataRepresentation";
                break;
        }

        try {
            Object[] objects = reflectionService.searchPackage(packageName, interfaceName);
            result.append("Available " + helpType + "s are:\n");
            for(int i = 0; i <objects.length; i++){//for each object, we want to print the name and description
                HelpTarget h = (HelpTarget) objects[i];

                //get the class name
                String name = h.getClass().getName();
                //need to remove the preceding pathname from each class name
                String[] parts = name.split("\\.");
                name = parts[parts.length-1];
                result.append("\t" + name + ":\n");

                //get the class description
                String description = h.getDescription();
                //display an error message instead of null for missing descriptions
                if (description == null)
                    description = "no description available";
                result.append("\t\t" + description + "\n");
            }
            console.displayResults(result.toString());
        } catch (IllegalAccessException e) {
            console.displayResults("failed to fetch information due to IllegalAccess of " + packageName);
        } catch (InvocationTargetException e) {
            console.displayResults("failed to fetch information due to IllegalAccess of " + packageName);
        } catch (InstantiationException e) {
            console.displayResults("class " + e.getCause() + " could not be instantiated to obtain description");
        } catch (NoSuchMethodException e) {
            console.displayResults("method " + e.getCause() + " could not be found to obtain description");
        } catch (ClassNotFoundException e) {
            console.displayResults("class " + e.getCause() + " could not be found to obtain description");
            e.printStackTrace();
        }
    }

    /**
     * main function for the system
     *
     * @param args command line arguments
     */
    public static void main(String[] args){
        Controller controller = new Controller();

        /*in order to reuse the code from the Console, for now args must be
          concatenated, even though it is then tokenized again in the near future
         */
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < args.length; i++)
            stringBuilder.append(args[i]);

        controller.processCommand(stringBuilder.toString());
    }

}
