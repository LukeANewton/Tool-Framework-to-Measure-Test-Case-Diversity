package model;

import core.InvalidFormatException;
import data_representation.CSV;
import data_representation.DataRepresentation;
import data_representation.StateSequence;
import metrics.comparison.Dice;
import metrics.comparison.PairwiseComparisonStrategy;
import org.junit.Before;
import org.junit.Test;
import user_interface.ConsoleOutputService;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class PairwiseCommandTest {
    PairwiseComparisonStrategy strategy;
    DataRepresentation[] testsuite;
    PairwiseCommand command;

    @Before
    public void setUp() throws InvalidFormatException {
        strategy = new Dice();
        command = new PairwiseCommand(strategy, new CSV("1,2,3,4,5,6"), new CSV("5,4,8,5,2,4,7"), null);
    }

    @Test
    public void call() throws Exception {
        double result = (Double) command.call();
        double expected = 0.5454; //hand-calculated value
        assertEquals(expected, result, 0.05);
    }

    @Test(expected = TestCaseFormatMismatchException.class)
    public void callWithDifferentDataRepresentations() throws Exception {
        command = new PairwiseCommand(strategy, new CSV("1,2,3,4,5,6"),
                new StateSequence("[43] Start-1(0)-OffProtected-4-StoppedProtected-13-PlayingProtected"), null);
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
        command = new PairwiseCommand(strategy, new CSV("1,2,3,4,5,6"), new CSV("5,4,8,5,2,4,7") , c);
        command.call();
        String expected = "[==========]" + System.lineSeparator();
        String actual = outContent.toString();
        assertEquals(expected, actual.substring(actual.length() - expected.length()));
        System.setOut(originalOut);
    }
}