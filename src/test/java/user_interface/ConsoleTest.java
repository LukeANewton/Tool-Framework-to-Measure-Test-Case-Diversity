package user_interface;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class ConsoleTest {
    private Console console;
    private  ByteArrayOutputStream outContent;
    private  PrintStream originalOut;
    private InputStream originalIn;

    @Before
    public void setUp(){
        console = new Console();
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
    /**test to ensure closeScanner properly closes the scanner.
     * test this by closing the scanner and then attempting to get input.
     * this should throw an IllegalStateException
     */
    public void closeScanner() {
        console.closeScanner();
        console.getOverwriteChoice("filename");
    }

    @Test
    /**Test for display results to check that if properly prints to the standard output*/
    public void displayResults() {
        String s = "results";
        console.displayResults(s);
        assertEquals(s + "\r\n", outContent.toString());
    }

    private void setInputForTest(String input){
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        console = new Console(); //the constructor opens a scanner on System.in, so we need to redo that
    }

    @Test
    /**test for getOverwriteChoice with a yes option*/
    public void getOverwriteChoiceYes() {
        setInputForTest("y");
        OverwriteOption opt = console.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.Yes, opt);
    }

    @Test
    /**test for getOverwriteChoice with a no option*/
    public void getOverwriteChoiceNo() {
        setInputForTest("N");
        OverwriteOption opt = console.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.No, opt);
    }

    @Test
    /**test for getOverwriteChoice with an append option*/
    public void getOverwriteChoiceAppend() {
        setInputForTest("A");
        OverwriteOption opt = console.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.Append, opt);
    }

    @Test
    /**test for getOverwriteChoice with an invalid option*/
    public void getOverwriteChoiceInvalid() {
        //the system should try to get input twice, frst failing, then getting a Yes choice
        setInputForTest("banana\ny");
        OverwriteOption opt = console.getOverwriteChoice("filename");
        assertEquals(OverwriteOption.Yes, opt);

        /*the expected output is a prompt for a choice twice, but there is no new line
        because that would normally be entered by the user*/
        String expected = "The file: filename already exists.\n" +
                "Do you wish to overwrite it?([y]es/[n]o/[a]ppend):Invalid choice, options are ([y]es/[n]o/[a]ppend): ";
        assertEquals(expected, outContent.toString());
    }
}