package model;

import core.InvalidFormatException;
import data_representation.CSV;
import data_representation.DataRepresentation;
import org.junit.Before;
import org.junit.Test;
import user_interface.ConsoleOutputService;
import utilities.Tuple;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.junit.Assert.*;

public class PairingCommandTest {
    String[] testsuite;
    PairingCommand command;

    @Before
    public void setUp() {
        testsuite = new String[]{"1,2,3,4,5,6", "5,4,8,5,2,4,7", "1,1,1,4,5,8"};
        command = new PairingCommand(null, "1,6,7,9,2", testsuite, new CSV());
    }

    @Test
    public void call() throws InvocationTargetException, NoSuchMethodException, InvalidFormatException, InstantiationException, IllegalAccessException {
        List<Tuple<DataRepresentation, DataRepresentation>> result = command.call();
        assertEquals(3, result.size());
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
                null, 3));
        command = new PairingCommand(c, "1,6,7,9,2", testsuite, new CSV());
        command.call();
        String expected = "[==========]" + System.lineSeparator();
        String actual = outContent.toString();
        assertEquals(expected, actual.substring(actual.length() - expected.length()));
        System.setOut(originalOut);
    }
}