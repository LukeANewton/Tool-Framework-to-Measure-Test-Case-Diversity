package model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.Assert.*;

public class ConfigTest {
        private Config config;
        private String configPath = "TestConfig.json";
        private String testJson = "{\n" +
                "  \"listwiseMethod\": \"ShannonIndex\",\n" +
                "  \"listwiseMethodLocation\": \"metrics.comparison.listwise\",\n" +
                "  \"pairwiseMethod\": \"CommonElements\",\n" +
                "  \"pairwiseMethodLocation\": \"metrics.comparison.pairwise\",\n" +
                "  \"dataRepresentation\": \"CSV\",\n" +
                "  \"dataRepresentationLocation\": \"data_representation\",\n" +
                "  \"delimiter\": \"\n\",\n" +
                "  \"aggregationMethod\": \"\",\n" +
                "  \"aggregationMethodLocation\": \"metrics.aggregation.\",\n" +
                "  \"numThreads\":  3,\n" +
                "  \"resultRoundingScale\": 2,\n" +
                "  \"resultRoundingMode\": \"HALF_UP\",\n" +
                "  \"outputFileName\": \"comparison_result\",\n" +
                "  \"outputFileLocation\": \"\"\n" +
                "}";

        /*Make a json file exclusively for testing purposes.*/
        @Before
        public void setup() {
                FileWriter writer = null;

                try {
                        writer = new FileWriter(configPath);
                        writer.write(testJson);
                } catch (IOException e) {
                        System.err.println("Failed to create dummy json file for testing Config.java.");
                        e.printStackTrace();
                } finally {
                        try {
                                Objects.requireNonNull(writer).close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                FileReader reader = null;
                try {
                        reader = new FileReader(configPath);
                        JsonReader jsonReader = new JsonReader(reader);
                        Gson gson = new Gson();
                        config = gson.fromJson(jsonReader, Config.class);
                } catch (FileNotFoundException e) {
                        System.out.println("Unable to load file '" + configPath + "'.");
                        e.printStackTrace();
                } finally {
                        try {
                                Objects.requireNonNull(reader).close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        /*Clean up json file used for testing.*/
        @After
        public void teardown() {
                File file = new File(configPath);
                boolean result;
                try {
                        result = Files.deleteIfExists(file.toPath());
                        if (!result) System.err.println("File doesn't exist.");
                } catch (IOException e) {
                        System.err.println("Failed to delete dummy json file for testing Config.java.");
                        e.printStackTrace();
                }
        }

        @Test
        /*test the getter for default pairwise method*/
        public void getPairwiseMethod() {
                assertEquals(config.getPairwiseMethod(), "CommonElements");
        }

        @Test
        /*test the setter for default pairwise method*/
        public void setPairwiseMethod() {
                String newMethod = "JaccardIndex";
                config.setPairwiseMethod(newMethod);
                assertEquals(config.getPairwiseMethod(), newMethod);
        }

        @Test
        /*test the getter for location of pairwise metrics*/
        public void getPairwiseMethodLocation() {
                assertEquals(config.getPairwiseMethodLocation(), "metrics.comparison.pairwise");
        }

        @Test
        /*test the setter for location of pairwise metrics*/
        public void setPairwiseMethodLocation() {
                String newLocation = "java.util";
                config.setPairwiseMethodLocation(newLocation);
                assertEquals(config.getPairwiseMethodLocation(), newLocation);
        }

        @Test
        /*test the getter for default listwise method*/
        public void getListwiseMethod() {
                assertEquals(config.getListwiseMethod(), "ShannonIndex");
        }

        @Test
        /*test the setter for default listwise method*/
        public void setListwiseMethod() {
                String newMethod = "JaccardIndex";
                config.setListwiseMethod(newMethod);
                assertEquals(config.getListwiseMethod(), newMethod);
        }

        @Test
        /*test the getter for location of listwise metrics*/
        public void getListwiseMethodLocation() {
                assertEquals(config.getListwiseMethodLocation(), "metrics.comparison.listwise");
        }

        @Test
        /*test the setter for location of listwise metrics*/
        public void setListwiseMethodLocation() {
                String newLocation = "java.util";
                config.setListwiseMethodLocation(newLocation);
                assertEquals(config.getListwiseMethodLocation(), newLocation);
        }

        @Test
        /*test the getter for default data representation*/
        public void getDataRepresentation() {
                assertEquals(config.getDataRepresentation(), "CSV");
        }
        
        @Test
        /*test the setter for default data representation*/
        public void setDataRepresentation() {
                String newDR = "AdjacencyGraph";
                config.setDataRepresentation(newDR);
                assertEquals(config.getDataRepresentation(), newDR);
        }

        @Test
        /*test the getter for location of data representations*/
        public void getGetDataRepresentationLocation() {
                assertEquals(config.getDataRepresentationLocation(), "data_representation");
        }

        @Test
        /*test the setter for location of data representations*/
        public void setGetDataRepresentationLocation() {
                String newLocation = "java.util";
                config.setDataRepresentationLocation(newLocation);
                assertEquals(config.getDataRepresentationLocation(), newLocation);
        }

        @Test
        /*test the getter for default delimiter between test cases*/
        public void getDelimiter() {
                assertEquals(config.getDelimiter(), "\n");
        }

        @Test
        /*test the setter for default delimiter between test cases*/
        public void setDelimiter() {
                String newDelimiter = "\n**********\n";
                config.setDelimiter(newDelimiter);
                assertEquals(config.getDelimiter(), newDelimiter);
        }

        @Test
        /*set the getter for the default aggregation method*/
        public void getAggregationMethod() {
                assertEquals(config.getAggregationMethod(), "");
        }

        @Test
        /*set the getter for the default aggregation method*/
        public void setAggregationMethod() {
                String newAM = "Average";
                config.setAggregationMethod(newAM);
                assertEquals(config.getAggregationMethod(), newAM);
        }

        @Test
        /*test the getter for location of aggregation methods*/
        public void getAggregationMethodLocation() {
                assertEquals(config.getAggregationMethodLocation(), "metrics.aggregation.");
        }

        @Test
        /*test the setter for location of aggregation methods*/
        public void setAggregationMethodLocation() {
                String newLocation = "java.util";
                config.setAggregationMethodLocation(newLocation);
                assertEquals(config.getAggregationMethodLocation(), newLocation);
        }

        @Test
        /*test the getter for the default number of threads to use in a thread pool*/
        public void getNumThreads() {
                assertEquals(config.getNumThreads(), 3);
        }

        @Test
        /*test the getter for the default number of threads to use in a thread pool*/
        public void setNumThreads() {
                int newNumThreads = 5;
                config.setNumThreads(newNumThreads);
                assertEquals(config.getNumThreads(), newNumThreads);
        }

        @Test
        /*test the getter for the number of decimal places to round results*/
        public void getResultRoundingScale() {
                assertEquals(config.getResultRoundingScale(), 2);
        }

        @Test
        /*test the getter for the number of decimal places to round results*/
        public void setResultRoundingScale() {
                int newRoundingScale = 5;
                config.setResultRoundingScale(newRoundingScale);
                assertEquals(config.getResultRoundingScale(), newRoundingScale);
        }

        @Test
        /*test the getter for the rounding mode to use*/
        public void getResultRoundingMode() {
                assertEquals(config.getResultRoundingMode(), "HALF_UP");
        }

        @Test
        /*test the setter for the rounding mode to use*/
        public void setResultRoundingMode() {
                String newRoundingMode = "HALF_DOWN";
                config.setResultRoundingMode(newRoundingMode);
                assertEquals(config.getResultRoundingMode(), newRoundingMode);
        }

        @Test
        /*test the getter for the default name of output file*/
        public void getOutputFileName() {
                assertEquals(config.getOutputFileName(), "comparison_result");
        }

        @Test
        /*test the getter for the default name of output file*/
        public void setOutputFileName() {
                String newOutputName = "out";
                config.setOutputFileName(newOutputName);
                assertEquals(config.getOutputFileName(), newOutputName);
        }

        @Test
        /*test the getter for the default location to save output files*/
        public void getOutputFileLocation() {
                assertEquals(config.getOutputFileLocation(), "");
        }

        @Test
        /*test the setter for the default location to save output files*/
        public void setOutputFileLocation() {
                String newLocation = "C://";
                config.setOutputFileLocation(newLocation);
                assertEquals(config.getOutputFileLocation(), newLocation);
        }

        @Test
        /*test the to string method*/
        public void testToString() {
               assertEquals(config.toString(), "Config{" +
                       "listwiseMethod=\'ShannonIndex\'," +
                       " listwiseMethodLocation=\'metrics.comparison.listwise\'," +
                       " pairwiseMethod=\'CommonElements\'," +
                       " pairwiseMethodLocation=\'metrics.comparison.pairwise\'," +
                       " dataRepresentation=\'CSV\'," +
                       " dataRepresentationLocation=\'data_representation\'," +
                       " delimiter=\n," +
                       " aggregationMethod=\'\'," +
                       " aggregationMethodLocation=\'metrics.aggregation.\'," +
                       " numThreads=3," +
                       " resultRoundingScale=2," +
                       " resultRoundingMode=\'HALF_UP\'," +
                       " outputFileName=\'comparison_result\'," +
                       " outputFileLocation=\'\'" +
                       "}");
        }
}
