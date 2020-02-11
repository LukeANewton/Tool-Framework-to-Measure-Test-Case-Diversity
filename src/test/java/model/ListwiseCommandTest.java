package model;

import core.InvalidFormatException;
import data_representation.CSV;
import data_representation.DataRepresentation;
import data_representation.StateSequence;
import metrics.listwise.ListwiseComparisonStrategy;
import metrics.listwise.ShannonIndex;
import org.junit.Before;
import org.junit.Test;
import user_interface.ConsoleOutputService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ListwiseCommandTest {
    ListwiseComparisonStrategy strategy;
    List<DataRepresentation> testsuite;
    ListwiseCommand command;

    @Before
    public void setUp() throws InvalidFormatException {
        strategy = new ShannonIndex();
        testsuite = new ArrayList<>();
        testsuite.add(new CSV("1,2,3,4,5,6"));
        testsuite.add(new CSV("5,4,8,5,2,4,7"));
        testsuite.add(new CSV("1,1,1,4,5,8"));
        command = new ListwiseCommand(strategy, testsuite, null);
    }

    @Test
    public void getComparison() {
        assertEquals(strategy, command.getComparison());
    }

    @Test
    public void getTestsuite() {
        //assertEquals for two arrays is deprecated, so this is the fix
        List<DataRepresentation> ts = command.getTestsuite();
        for(int i = 0 ; i < ts.size(); i++)
            assertEquals(testsuite.get(i), ts.get(i));
    }

    @Test
    public void call() throws Exception {
        double result = (Double) command.call();
        double expected = 1.92; //hand-calculated value
        assertEquals(expected, result, 0.05);
    }

    @Test(expected = TestCaseFormatMismatchException.class)
    public void callWithDifferentDataRepresentations() throws Exception {
        testsuite = new ArrayList<>();
        testsuite.add(new CSV("1,2,3,4,5,6"));
        testsuite.add(new StateSequence("[43] Start-1(0)-OffProtected-4-StoppedProtected-13-PlayingProtected"));
        command = new ListwiseCommand(strategy, testsuite, null);
        command.call();
    }

    @Test
    public void callWithListener() throws Exception {
        /*set up a console to display a progress bar for one task, if the progress bar
        is fully displayed, then the listener works*/
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ConsoleOutputService c = new ConsoleOutputService();
        PropertyChangeSupport support = new PropertyChangeSupport(this);
        support.addPropertyChangeListener(c);
        support.firePropertyChange(new PropertyChangeEvent(this, "numberTasks",
                null, 1));
        command = new ListwiseCommand(strategy, testsuite, c);
        command.call();
        String expected = "[==========]" + System.lineSeparator();
        String actual = outContent.toString();
        assertEquals(expected, actual.substring(actual.length() - expected.length()));
        System.setOut(originalOut);
    }
}