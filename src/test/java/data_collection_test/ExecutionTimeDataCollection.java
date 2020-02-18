package data_collection_test;

import core.Controller;
import core.FileReaderService;
import core.FileWriterService;
import model.Config;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This file contains a script to run a command several times, each on varying sized test cases and
 * different numbers of threads, and stores the results to plot
 *
 * @author luke
 */
public class ExecutionTimeDataCollection {
    private static final String FILENAME = "examples/VCRTPath.txt";
    private static final  String DELIMITER = System.lineSeparator() + System.lineSeparator();
    private static final  int NUMBER_POINTS_TO_COLLECT = 30;
    private static final  String CONFIG_NAME = "config.json";

    @Test
    public void collectExecutionTimeInformation() throws IOException {
        //store the config file for to later restore
        FileWriterService fw = new FileWriterService();
        Config originalConfigFile = new FileReaderService().readConfig(CONFIG_NAME);

        String contents = readFile(FILENAME);
        String[] testSuite = contents.split(DELIMITER);
        int numberTestCases = testSuite.length;

        //determine the sizes of test suites to use in the measurements
        Integer[] suiteSizes = getTestSuiteSizes(numberTestCases, NUMBER_POINTS_TO_COLLECT);

        //get data for a pariwise comparison
        doComparisons(testSuite, suiteSizes, "Levenshtein");
        //get data for a listwise comparison
        doComparisons(testSuite, suiteSizes, "Nei");

        //restore original config file
        fw.writeConfig(CONFIG_NAME, originalConfigFile);
    }

    private void doComparisons(String[] testSuite, Integer[] sizes, String metric) throws IOException {
        //collect data for a sequential only system
        doComparisons(1, testSuite, sizes, metric);
        //collect data for a threaded systems
        doComparisons(2, testSuite, sizes, metric);
        doComparisons(5, testSuite, sizes, metric);
        doComparisons(10, testSuite, sizes, metric);
        doComparisons(20, testSuite, sizes, metric);
    }

    /**
     * perform comparisons for a set number of threads
     *
     * @param n the number of threads to use in each comparison
     * @param testSuite the test suite to draw a subset from
     * @param sizes the size of each subset to use in comparisons
     * @throws IOException file reading/writing fails
     */
    private void doComparisons(int n, String[] testSuite, Integer[] sizes, String metric) throws IOException {
        List<String> executionTimes = new ArrayList<>();
        Controller c = getControllerWithSpecifiedThreadCount(n);
        for(Integer size : sizes){
            //write subset to a file
            String[] subset = getRandomSubset(testSuite, size);
            File file = File.createTempFile("performance-measure", "");
            String filename = file.getAbsolutePath();
            write(file, subset);

            //do comparison
            long startTime = System.nanoTime();
            String command = "compare " + filename + " EventStatePairs -m " + metric + " -a AverageValue";
            if(n > 1)
                command += " -t " + n;
            c.processCommand(command);
            //calculate seconds, with three decimal places
            executionTimes.add(String.valueOf((double)Math.round(1000d *
                    (((double) System.nanoTime() - startTime) / 1000000000)) / 1000d));
            file.delete();
        }
        write(new File("results-" + n + "-thread-" + metric), executionTimes.toArray(new String[0]));
    }

    /**
     * calculate a list of test sizes to use based of the total size of the suite and number of steps
     *
     * @param totalSize the total size of the test suite
     * @param numberOfMeasurements the number of steps to increment
     * @return a list of sizes
     */
    private Integer[] getTestSuiteSizes(int totalSize, int numberOfMeasurements){
        int stepSize = totalSize / numberOfMeasurements;
        List<Integer> sizes = new ArrayList<>();
        for(int i = 0; i < numberOfMeasurements; i++)
            sizes.add(stepSize * (i+1));
        return sizes.toArray(new Integer[0]);
    }

    /**
     * writes specified contents to a file
     *
     * @param file the file object to write to
     * @param contents the contents to append to the file
     * @throws IOException occurs when write is unsuccessful
     */
    private void write(File file, String[] contents) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        for(int i = 0; i < contents.length; i++) {
            if(i + 1 < contents.length)
                bw.write(contents[i] + System.lineSeparator());
            else
                bw.write(contents[i]);
        }
        bw.flush();
        bw.close();
    }

    /**
     * updates the config file's thread count, then gets a new controller to
     * refresh the systems internal config representation
     *
     * @param n the number of threads to use in the comparisons
     * @return a Controller instance that uses the new thread count
     * @throws IOException occurs when the config file could not be updated
     */
    private Controller getControllerWithSpecifiedThreadCount(int n) throws IOException {
        Config config = new FileReaderService().readConfig(CONFIG_NAME);
        config.setNumThreads(n);
        new FileWriterService().writeConfig(CONFIG_NAME, config);
        return Controller.getController();
    }

    /**
     * samples the testSuite n times to get a new testSuite of size n. This allows the same test case to be
     * sampled several times, but since this script only cares about the execution time, and not results of any
     * particular operation, this is okay.
     *
     * @param testSuite the test suite to sample from
     * @param n the number of samples to take from the specified test suite
     * @return a new test suite of n samples of the original suite
     */
    private String[] getRandomSubset(String[] testSuite, int n)  {
        List<String> result = new ArrayList<>();
        int numberTestCases = testSuite.length;
        for(int i = 0; i < n; i++)
            result.add(testSuite[(int) (Math.random() * numberTestCases)]);
        return result.toArray(new String[0]);
    }

    /**
     * reads a file's contents into a single string
     *
     * @param filename the location of the file
     * @return a String containing the entire contents of the file
     * @throws FileNotFoundException thrown when no file with the specified name is found
     */
    private String readFile(String filename) throws FileNotFoundException {
        if(filename.equals(""))
            throw new FileNotFoundException();

        File file = new File(filename);

        Scanner sc = new Scanner(file);

        StringBuilder s = new StringBuilder();
        while (sc.hasNextLine()) {
            s.append(sc.nextLine());
            if(sc.hasNextLine())
                s.append(System.lineSeparator());
        }
        sc.close();
        String result = s.toString();
        return result.substring(0, result.length() - System.lineSeparator().length());
    }
}
