package user_interface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ConsoleOutputServiceTest {
    private ConsoleOutputService consoleOutputService;
    private  ByteArrayOutputStream outContent;
    private  PrintStream originalOut;
    private InputStream originalIn;

    @Before
    public void setUp(){
        consoleOutputService = new ConsoleOutputService();
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        originalIn = System.in;
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test(expected = IllegalStateException.class)
    /*test to ensure closeScanner properly closes the scanner.
     * test this by closing the scanner and then attempting to get input.
     * this should throw an IllegalStateException
     */
    public void closeScanner() {
        consoleOutputService.closeScanner();
        consoleOutputService.getOverwriteChoice("filename");
    }

    @Test
    /*Test for display results to check that if properly prints to the standard output*/
    public void displayResults() {
        String s = "results";
        consoleOutputService.displayResults(s);
        assertEquals(s + System.lineSeparator(), outContent.toString());
    }

    private void setInputForTest(String input){
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        consoleOutputService = new ConsoleOutputService(); //the constructor opens a scanner on System.in, so we need to redo that
    }

    @Test
    /*test for getOverwriteChoice with a yes option*/
    public void getOverwriteChoiceYes() {
        setInputForTest("y");
        OverwriteOption opt = consoleOutputService.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.Yes, opt);
    }

    @Test
    /*test for getOverwriteChoice with a no option*/
    public void getOverwriteChoiceNo() {
        setInputForTest("N");
        OverwriteOption opt = consoleOutputService.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.No, opt);
    }

    @Test
    /*test for getOverwriteChoice with an append option*/
    public void getOverwriteChoiceAppend() {
        setInputForTest("A");
        OverwriteOption opt = consoleOutputService.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.Append, opt);
    }

    @Test
    /*test for getOverwriteChoice with an invalid option*/
    public void getOverwriteChoiceInvalid() {
        //the system should try to get input twice, first failing, then getting a Yes choice
        setInputForTest("banana" + System.lineSeparator() +"y");
        OverwriteOption opt = consoleOutputService.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.Yes, opt);

        /*the expected output is a prompt for a choice twice, but there is no new line
        because that would normally be entered by the user*/
        String expected = "The file: filename already exists." + System.lineSeparator() +
                "Do you wish to overwrite it?([y]es/[n]o/[a]ppend):Invalid choice, options are ([y]es/[n]o/[a]ppend): ";
        assertEquals(expected, outContent.toString());
    }

    @Test
    /*test for the progress bar works as intended*/
    public void testPropertyChange(){
        //setup the observer for the console
        PropertyChangeSupport support = new PropertyChangeSupport(this);
        support.addPropertyChangeListener(consoleOutputService);

        //set the number of tasks to expect
        int numTasks = 10;
        support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks", 0 , numTasks));

        //fire an event to simulate a task being completed, this should create the progress bar at 10% complete
        support.firePropertyChange(new PropertyChangeEvent(this, "completedTask", 0, 1));
        String expected = "[=         ]\r";
        String actual = outContent.toString();
        assertEquals(expected, actual.substring(actual.length() - expected.length()));

        //fire 9 more events to complete all 10 tasks. then we should have a full progress bar
        for(int i = 0; i < 9; i++)
            support.firePropertyChange(new PropertyChangeEvent(this, "completedTask", 0, 1));
        expected = "[==========]"+System.lineSeparator();
        /*unfortunately, unlike the terminal, output streams do not overwrite the contents
        when you use a /r, so we only check the end of the output stream to see what would
        be printed in the end*/
        assertEquals(expected, outContent.toString().substring(outContent.toString().length() - 14));
    }
}