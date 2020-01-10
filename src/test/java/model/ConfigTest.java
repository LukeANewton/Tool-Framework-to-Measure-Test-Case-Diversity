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

        private String configPath = "TestConfig.json";
        private String testValue = "TestCompareMethod";
        private String testJson = "{\"comparisonMethod\":\"TestCompareMethod\",\"numThreads\":0,\"resultRoundingScale\":0}";

        /**
         * Make a json file exclusively for testing purposes.
         */
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
        }

        /**
         * Clean up json file used for testing.
         */
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

        /**
         * Tests reading a config json using gson.
         */
        @Test
        public void readConfigJson() {
                FileReader reader = null;
                try {
                        reader = new FileReader(configPath);
                        JsonReader jsonReader = new JsonReader(reader);
                        Gson gson = new Gson();
                        Config config = gson.fromJson(jsonReader, Config.class);
                        assertNotNull(config.getComparisonMethod());
                        assertEquals("Failed to read proper test value.", testValue, config.getComparisonMethod());
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

        /**
         * Tests writing a config json using gson.
         */
        @Test
        public void writeConfigJson() {
                FileReader reader = null;
                FileWriter writer = null;
                Config config = new Config();
                config.setComparisonMethod(testValue);
                Gson gson = new Gson();
                try {
                        writer = new FileWriter(configPath);
                        gson.toJson(config, writer); // Write to json file
                        Objects.requireNonNull(writer).close();
                        reader = new FileReader(configPath);
                        JsonReader jsonReader = new JsonReader(reader); // set up the reader
                        Config newConfig = gson.fromJson(jsonReader, Config.class); // read written value to confirm it was written successfully
                        assertNotNull(newConfig.getComparisonMethod());
                        assertEquals("Failed to read proper test value.", testValue, newConfig.getComparisonMethod());
                } catch (IOException e) {
                        System.err.println("Failed to create dummy json file for testing Config.java.");
                        e.printStackTrace();
                } finally {
                        try {
                                Objects.requireNonNull(reader).close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }


}
