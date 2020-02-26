package core;

import model.Config;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ControllerConfigTest {
    private Controller c;
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut;
    private Config originalConfigFile;
    private FileReaderService reader;
    private FileWriterService writer;
    private Config config;
    private final String configName = "config.json";

    @Before
    public void setUp() throws IOException {
        c = Controller.getController();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        //store the config file for to later restore
        reader = new FileReaderService();
        writer = new FileWriterService();
        config = reader.readConfig(configName);
        originalConfigFile = reader.readConfig(configName);
    }

    @After
    public void tearDown() throws IOException {
        System.setOut(originalOut);
        writer.writeConfig(configName, originalConfigFile);
    }

    @Test
    /*Test for the creation of a controller when no config file is present*/
    public void testControllerCreationWithoutConfigFile() throws IOException {
        //remove config file
        File file = new File(configName);
        assertTrue(file.delete());

        //test the creation of a controller without a config file
        c = Controller.getController();
        assertNull(c);
        String expected = "Failed to read from configuration file: config.json. Ensure the file exists in the same directory as this program." + System.lineSeparator();
        assertEquals(expected, outContent.toString());

        //restore config file
        writer.writeConfig(configName, config);
    }

    @Test
    /*use a config command to set the string for the delimiter property*/
    public void testProcessStringCommandConfig() throws IOException {
        config.setDelimiter("banana");
        writer.writeConfig(configName, config);
        config = reader.readConfig(configName);
        assertEquals("Error setting config values for the test","banana", config.getDelimiter());

        c.processCommand("config delimiter apple");
        assertEquals("Successfully set delimiter to the value apple" + System.lineSeparator(), outContent.toString());

        config = reader.readConfig(configName);
        assertEquals("Delimiter should be set to apple but is: " + config.getDelimiter(),"apple", config.getDelimiter());
    }

    @Test
    /*Use a config command to set the int for the numThreads property*/
    public void testProcessIntCommandConfig() throws IOException {
        config.setNumThreads(0);
        writer.writeConfig(configName, config);
        config = reader.readConfig(configName);
        assertEquals("Error setting config values for the test",0, config.getNumThreads());

        c.processCommand("config numThreads 5");
        assertEquals("Successfully set numThreads to the value 5" + System.lineSeparator(), outContent.toString());

        config = reader.readConfig(configName);
        assertEquals("NumThreads should be set to 5 but is: " + config.getNumThreads(),5, config.getNumThreads());
    }

    @Test
    /*Use a config command to set the int for the numThreads property too a string*/
    public void testProcessStringToIntParamCommandConfig() {
        c.processCommand("config numThreads apple");
        assertEquals("Failed to set numThreads to apple. The value for numThreads should be a number" + System.lineSeparator(), outContent.toString());
    }

    @Test
    /*Use a config command with an invalid property*/
    public void testProcessInvalidCommandConfig() {
        c.processCommand("config banana banana");
        assertEquals("The parameter banana is not valid" + System.lineSeparator(), outContent.toString());
    }

}
