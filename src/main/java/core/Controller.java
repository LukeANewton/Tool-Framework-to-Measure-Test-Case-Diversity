package core;

import data_representation.DataRepresentation;
import metrics.aggregation.AggregationStrategy;
import metrics.comparison.listwise.ListwiseComparisonStrategy;
import metrics.comparison.pairwise.PairwiseComparisonStrategy;
import model.*;
import user_interface.ConsoleOutputService;
import user_interface.InputParser;
import user_interface.InvalidCommandException;
import user_interface.OverwriteOption;
import utilities.Tuple;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The Controller is the main logic for the program. It pieces together the different services to
 * achieve each system command.
 *
 * @author luke
 */
public class Controller {
    //the name of the configuration file containing system defaults
    private static final String CONFIG_FILE = "config.json";
    private static final String DATA_REP_INTERFACE_PATH = "data_representation.DataRepresentation";
    private static final String PAIRWISE_COMPARISON_INTERFACE_PATH = "metrics.comparison.pairwise.PairwiseComparisonStrategy";
    private static final String LISTWISE_COMPARISON_INTERFACE_PATH = "metrics.comparison.listwise.ListwiseComparisonStrategy";
    private static final String AGGREGATION_INTERFACE_PATH = "metrics.aggregation.AggregationStrategy";

    //configuration object containing config file values
    private Config config;
    //console to obtain user input and display output
    private ConsoleOutputService console;
    //service to load class instances
    private ReflectionService reflectionService;
    //service to read files into system
    private FileReaderService fileReaderService;
    //service to write output files
    private FileWriterService fileWriterService;
    //service to generate pairs of test cases from test suite(s)
    private PairingService pairingService;
    //service to perform comparisons
    private ComparisonService comparisonService;
    //the service which parses commands
    private InputParser inputParser;

    /**Constructor*/
    private Controller() throws IOException {
        fileReaderService = new FileReaderService();
        console = new ConsoleOutputService();
        inputParser = new InputParser();
        reflectionService = new ReflectionService();
        fileWriterService = new FileWriterService();

        //read config file
        config = fileReaderService.readConfig(CONFIG_FILE);

        comparisonService = new ComparisonService();
    }

    public static Controller getController(){
        String errorMsg = "Failed to read from configuration file: " + CONFIG_FILE + ". Ensure the file exists in the same directory as this program.";
        try {
            return new Controller();
        } catch (Exception e) {// the configuration file does not exist
            //this determines if we are executing within the jar file or not
            if(Controller.class.getResource("Controller.class").toString().startsWith("jar")) {
                try{//try to create a jar in the proper spot from the default inside the jar
                    //read in the config file from the jar
                    InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);

                    //write the file to outside the jar
                    OutputStream outStream = new FileOutputStream(new File(CONFIG_FILE));
                    outStream.write(buffer);
                    outStream.close();

                    return new Controller();
                } catch(Exception ex){//this means we failed to create a new config file, so the Controller cannot be created
                    System.out.println(errorMsg);
                    return null;
                }
            } else{
                System.out.println(errorMsg);
                return null;
            }
        }
    }

    /**
     * the command to invoke in the Controller to process a system instruction
     *
     * @param command the command for the system to execute
     */
    public void processCommand(String command){
        //parse the command into a DTO
        DataTransferObject dto;
        try {
            dto = inputParser.parse(command);
        } catch (InvalidCommandException e) {
			/*if we have an invalid command, display an error message and list the
			valid commands through issuing a HelpDTO*/
            console.displayResults(e.getErrorMessage() + "Valid commands are:");
            HelpDTO help = new HelpDTO();
            help.setHelpType(HelpType.Command);
            dto = help;
        }

        //determine which command is being parsed
        CommandType commandType = dto.getCommandType();
        switch(commandType){
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
     * contains the logic for obtaining the DataRepresentation for a compare command
     *
     * @param dto the DataTransferObject containing information to run a compare command
     * @return an instance of the DataRepresentation specified in the dto
     */
    private DataRepresentation loadDataRepresentation(CompareDTO dto){
        String name = dto.getDataRepresentation();

        if (name == null)//nothing specified in dto, so load default from config file
            name = config.getDataRepresentation();

        //set the package to look in
        String packageName = config.getDataRepresentationLocation();

        //try to load the class
        return (DataRepresentation) loadRequiredImplementation(name, packageName,
                DATA_REP_INTERFACE_PATH, "data representation");
    }

    /**
     * contains the logic for obtaining the PairwiseComparisonStrategy for a compare command
     *
     * @param dto the DataTransferObject containing information to run a compare command
     * @return an instance of the PairwiseComparisonStrategy specified in the dto
     */
    private AggregationStrategy[] loadAggregationStrategy(CompareDTO dto) {
        String[] names = dto.getAggregationMethods();

        if (names == null)//nothing specified in dto, so load default from config file
            names = new String[]{config.getAggregationMethod()};

        //set the package to look in
        String packageName = config.getAggregationMethodLocation();

        //try to load the class
        List<AggregationStrategy> strategies = new ArrayList<>();
        for(String name: names){
            AggregationStrategy strategy = (AggregationStrategy) loadRequiredImplementation(name, packageName,
                    AGGREGATION_INTERFACE_PATH, "aggregation method");
            if(strategy == null)
                return null;
            else
                strategies.add(strategy);
        }
        return strategies.toArray(new AggregationStrategy[0]);
    }

    /**
     * loads an object of class name located at packageName, that implements the interface at interfacePath
     *
     * @param name the name of the class to instantiate
     * @param packageName the location of the class
     * @param interfacePath the location of the interface the class should implement
     * @param interfaceType the string description of the class to instantiate, to aid in error reporting
     * @return an instance of the class specified by name
     */
    private Object loadRequiredImplementation(String name, String packageName, String interfacePath, String interfaceType){
        if (!packageName.endsWith("."))
            packageName = packageName + ".";

        try {
            return reflectionService.loadClass(packageName + name, interfacePath);
        } catch (ClassNotFoundException | InvalidFormatException e) {
            console.displayResults("no " + interfaceType + " named " + name + " in " +  packageName + " found");
        }catch (ClassCastException e){
            console.displayResults(name + " does not implement the " + interfaceType + " interface");
        } catch (Exception e) {
            console.displayResults("failed to instantiate " + interfaceType + ": " + name + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * loads a comparison strategy specified by name, that should implement a specified interface
     *
     * @param name the name of the class to instantiate
     * @param defaultName the default name to use if the name in the dto is unspecified
     * @param packageName the location of the class
     * @param interfaceName the location of the interface the class should implement
     * @return an instance of the specified class
     * @throws Exception occurs when the class could not be found at the specified location, or if the class does not implement the specified interface
     */
    private Object loadComparisonStrategy(String name, String defaultName, String packageName, String interfaceName) throws Exception {
        if (name == null || name.equals(""))
            name = defaultName;
        if (!packageName.endsWith("."))
            packageName = packageName + ".";
        return reflectionService.loadClass(packageName + name, interfaceName);
    }

    /**
     * performs a "compare" operation that is characterized by the contents of the passed CompareDTO
     *
     * @param dto data transfer object containing information on the requested compare command
     */
    private void processCompareCommand(CompareDTO dto) {
        //load the data representation
        DataRepresentation dataRepresentation = loadDataRepresentation(dto);
        if (dataRepresentation == null)
            return;

        //determine whether the comparison metric is pairwise or listwise
        PairwiseComparisonStrategy pairwiseStrategy = null;
        ListwiseComparisonStrategy listwiseStrategy = null;
        String name = dto.getComparisonMethod();
        ComparisonType type;
        try {
            pairwiseStrategy = (PairwiseComparisonStrategy) loadComparisonStrategy(name, config.getPairwiseMethod(),
                    config.getPairwiseMethodLocation(), PAIRWISE_COMPARISON_INTERFACE_PATH);
        } catch (Exception ignored) {}
        try {
            listwiseStrategy = (ListwiseComparisonStrategy) loadComparisonStrategy(name, config.getListwiseMethod(),
                    config.getListwiseMethodLocation(), LISTWISE_COMPARISON_INTERFACE_PATH);
        } catch (Exception ignored) {}
        if (pairwiseStrategy == null && listwiseStrategy == null) {
            console.displayResults("The specified metric either cannot be found, or does not implement the required interface");
            return;
        } else if (pairwiseStrategy == null)
            type = ComparisonType.listwise;
        else
            type = ComparisonType.pairwise;

        //load the aggregation methods
        AggregationStrategy[] aggregationStrategies = loadAggregationStrategy(dto);
        if(aggregationStrategies == null)
            return;

        //read in the first test suite file
        String filename = dto.getTestCaseLocationOne();
        String delimiter = dto.getDelimiter();
        if (delimiter == null)
            delimiter = config.getDelimiter();
        DataRepresentation[] testSuite1 = getTestSuite(filename, delimiter, dataRepresentation);
        if(testSuite1 == null) //this triggers when an exception is thrown
            return;
        else if (testSuite1.length == 0){//if the file has not test cases, we cannot proceed with hte operation
            console.displayResults("operation failed because " + filename + " does not contain any test cases");
            return;
        }

        //if there is a second file, read it in as well
        DataRepresentation[] testSuite2 = null;
        if(dto.getTestCaseLocationTwo() != null) {
            filename = dto.getTestCaseLocationTwo();
            testSuite2 = getTestSuite(filename, delimiter, dataRepresentation);
            if (testSuite2 == null) //this triggers when an exception is thrown
                return;
            else if (testSuite2.length == 0) {//if the file has not test cases, we cannot proceed with the operation
                console.displayResults("operation failed because " + filename + " does not contain any test cases");
                return;
            }
        }

        //create thread pool for pairing and comparison
        if(dto.getNumberOfThreads() == null)
            dto.setNumberOfThreads(config.getNumThreads());
        ExecutorService threadPool = Executors.newFixedThreadPool(dto.getNumberOfThreads());

        String[] results;
        switch(type){//pairing and comparison is dependent on the type of comparison metric being used
            case pairwise:
                //generate the pairs for comparison
                pairingService = new PairingService(threadPool);
                List<Tuple<DataRepresentation, DataRepresentation>> pairs;
                console.displayResults("Pairing Test Cases...");
                try {
                    if (testSuite2 == null)
                        pairs = pairingService.makePairs(console, testSuite1);
                    else
                        pairs = pairingService.makePairs(console, testSuite1, testSuite2);
                } catch (Exception e) {
                    console.displayResults("Error during pair generation: " + e.toString());
                    return;
                }
                if (pairs.size() == 0) {//no pairs could be made from the passed test suites
                    console.displayResults("Test suite contains insufficient test cases to generate pairs");
                    return;
                }
                //now perform the actual comparison
                comparisonService = new ComparisonService(threadPool);
                try {
                    console.displayResults("Performing Comparison...");
                    results = comparisonService.pairwiseCompare(pairs, pairwiseStrategy, aggregationStrategies, console, dto.isUseThreadPool());
                } catch (Exception e) {
                    console.displayResults("Error in pairwise comparison calculation: " + e.toString());
                    return;
                }
                break;
            case listwise:
                //now perform the actual comparison
                comparisonService = new ComparisonService(threadPool);
                try {
                    console.displayResults("Performing Comparison...");
                    List<List<DataRepresentation>> suites = new ArrayList<>();
                    suites.add(Arrays.asList(testSuite1));
                    if (testSuite2 != null)
                        suites.add(Arrays.asList(testSuite2));
                    results = comparisonService.listwiseCompare(suites, listwiseStrategy,
                            aggregationStrategies, console, dto.isUseThreadPool());
                } catch (Exception e) {
                    console.displayResults("Error in pairwise comparison calculation: " + e.toString());
                    return;
                }
                break;
            default:
                results = new String[0];
        }
        threadPool.shutdown();

        if(results.length == 0) {
            console.displayResults("no results were obtained from the calculation");
        } else {
            outputResults(results, dto);
        }
    }

    private void outputResults(String[] results, CompareDTO dto){
        StringBuilder s = new StringBuilder();
        s.append(results[0]);
        for(int i = 1; i < results.length; i++){
            s.append(System.lineSeparator()).append(results[i]);
        }
        String result = s.toString();

        //output results to file, if required
        if(dto.getSave()) {
            String filename;
            if(dto.getOutputFilename() != null){
                filename = dto.getOutputFilename();
            } else {
                filename = config.getOutputFileName();
            }
            if(filename != null) {
                File output = new File(filename);
                try {
                    if (output.exists()) {
                        OverwriteOption overwriteOption = console.getOverwriteChoice(filename);
                        switch (overwriteOption) {
                            case Yes:
                                fileWriterService.write(filename, result, true, false);
                                break;
                            case No:
                                console.displayResults("file writing cancelled since file already exists");
                                break;
                            case Append: //write the results out on a new line
                                fileWriterService.write(filename, result, false, true);
                        }
                    } else
                        fileWriterService.write(filename, result, false, false);
                } catch (IOException e) {
                    console.displayResults("failed to write to " + filename + ": " + e.getMessage());
                }
            } else {
                console.displayResults("failed to save, outputFileName not given");
            }
        }

        //output results to console
        console.displayResults("Result:" + System.lineSeparator() + System.lineSeparator() + result);
    }

    /**
     * loads a test suite from a given file into the system. The test cases in the system are expected to
     * be seperated by the provided delimiter, and be formated according to the supplied data representation
     *
     * @param filename the name of the test suite file
     * @param delimiter the character/string/regex that separates each file in the test suite
     * @param format the data representation of the test cases
     * @return an array of data representations where each data representation contains a test case
     */
    private DataRepresentation[] getTestSuite(String filename, String delimiter, DataRepresentation format){
        try {
            return fileReaderService.readIntoDataRepresentation(filename, delimiter, format);
        } catch (InvalidFormatException e) {
            console.displayResults("one or more test cases in " + filename + " do not match the specified data representation: "
                    + format.getClass().getName() + ": " + e.getMessage());
        } catch (FileNotFoundException e) {
            console.displayResults("file: " + filename + " could not be found");
        } catch (Exception e) {
            console.displayResults("failed to instantiate data representation: " + e.getMessage());
        }
        return null;
    }

    /**
     * performs a "configure" operation that is characterized by the contents of the passed ConfigDTO
     *
     * @param dto data transfer object containing information on the requested configure command
     */
    private void processConfigureCommand(ConfigDTO dto) {
        //check if there is a setter for the parameter we are trying to set
        Method setter = null;
        //the value may be a String or and integer, so we need to try both
        Class[] classes = new Class[]{String.class, int.class};
        int validIndex = -1;
        for(int i = 0; i < classes.length; i++){
            try {
                setter = reflectionService.retrieveConfigSetter(config, classes[i], dto.getParameterName());
                validIndex = i;
                break;
            } catch (NoSuchMethodException ignored) {
            }
        }

        if (validIndex == -1)//if we make it here, then we know that the parameter to set does not exist
            console.displayResults("The parameter " + dto.getParameterName() + " is not valid");
        else {
            try {
                //invoke the setter
                switch(validIndex){
                    case 1: //the value should be an integer, so it must be cast as this
                        setter.invoke(config, Integer.parseInt(dto.getParameterValue()));
                        break;
                    default:
                        setter.invoke(config, dto.getParameterValue());
                }

                //as long as there is no REPL, any config command must save the value to a file, whether -s is specified or not
                fileWriterService.writeConfig(CONFIG_FILE, config);
                console.displayResults("Successfully set " + dto.getParameterName() + " to the value " + dto.getParameterValue());
            }  catch (NumberFormatException e) {
                console.displayResults("Failed to set " + dto.getParameterName() + " to " + dto.getParameterValue() + ". The value for " + dto.getParameterName() + " should be a number");
            } catch (Exception e) {
                console.displayResults("Failed to set " + dto.getParameterName() + " to " + dto.getParameterValue() + ": " + e.toString());
            }
        }
    }

    /**
     * performs a "help" operation that is characterized by the contents of the passed HelpDTO
     *
     * @param dto data transfer object containing information on the requested help command
     */
    private void processHelpCommand(HelpDTO dto) {
        HelpType helpType = dto.getHelpType();
        StringBuilder result = new StringBuilder();
        //determine which type of help is needed, each works the same way except for Command help
        switch(helpType){
            case Command:
                result.append("\tcompare <filename> [<filename>] <data-representation>").append(System.lineSeparator());
                result.append("\t\tperforms a diversity calculation within a test suite, or between test suites at the specified filename(s)").append(System.lineSeparator());
                result.append("\t\t\t-m <metric>: set the diversity metric to use in the calculation. Available metrics can be found with 'help -m'").append(System.lineSeparator());
                result.append("\t\t\t-a <method>: set the method to use for aggregating results. Available methods can be found with 'help -a'").append(System.lineSeparator());
                result.append("\t\t\t-d <delimiter>: set the delimiter that separates test cases within the passed test suite file(s). This can be a character, string, or regular expression").append(System.lineSeparator());
                result.append("\t\t\t-s <filename>: denote that the results of the operation should be saved to a file named <filename>").append(System.lineSeparator());
                result.append("\t\t\t-t [<integer>]: denote that the operation should use a thread pool for concurrency, and optionally specify the number of threads").append(System.lineSeparator());
                result.append("\tconfig <parameter> <value>").append(System.lineSeparator());
                result.append("\t\tsets the value of a parameter read from the configuration file").append(System.lineSeparator());
                result.append("\thelp").append(System.lineSeparator());
                result.append("\t\tlists information on the requested topic").append(System.lineSeparator());
                result.append("\t\t\t-m: lists the available comparison metrics in the system").append(System.lineSeparator());
                result.append("\t\t\t-a: lists the available aggregation methods in the system").append(System.lineSeparator());
                result.append("\t\t\t-f: lists the available data representations in the system").append(System.lineSeparator());
                console.displayResults(result.toString());
                return;
            case Metric:
                displayHelp(config.getPairwiseMethodLocation(),
                        PAIRWISE_COMPARISON_INTERFACE_PATH, "pairwise metric");
                displayHelp(config.getListwiseMethodLocation(),
                        LISTWISE_COMPARISON_INTERFACE_PATH, "listwise metric");
                break;
            case AggregationMethod:
                displayHelp(config.getAggregationMethodLocation(),
                        AGGREGATION_INTERFACE_PATH, "aggregation method");
                break;
            case DataRepresentation:
                displayHelp(config.getDataRepresentationLocation(),
                        DATA_REP_INTERFACE_PATH, "data representation");
                break;
        }


    }

    private void displayHelp(String packageName, String interfacePath, String helpType){
        try {
            StringBuilder result = new StringBuilder();
            Object[] objects = reflectionService.searchPackage(packageName, interfacePath);
            result.append("Available ").append(helpType).append("s are:").append(System.lineSeparator());
            if(objects == null)
                result.append("\tNone available at specified directory: ").append(packageName);
            else{
                for (Object object : objects) {//for each object, we want to print the name and description
                    HelpTarget h = (HelpTarget) object;

                    //get the class name
                    String name = h.getClass().getName();
                    //need to remove the preceding pathname from each class name
                    String[] parts = name.split("\\.");
                    name = parts[parts.length - 1];
                    result.append("\t").append(name).append(":").append(System.lineSeparator());

                    //get the class description
                    String description = h.getDescription();
                    //display an error message instead of null for missing descriptions
                    if (description == null)
                        description = "no description available";
                    result.append("\t\t").append(description).append(System.lineSeparator());
                }
            }
            console.displayResults(result.toString());
        } catch (Exception e){
            console.displayResults("failed to retrieve object descriptions: " + e.toString());
        }
    }
}
