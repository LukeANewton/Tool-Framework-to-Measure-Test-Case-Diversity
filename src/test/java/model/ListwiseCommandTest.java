package model;

import core.InvalidFormatException;
import data_representation.CSV;
import data_representation.DataRepresentation;
import metrics.listwise.ListwiseComparisonStrategy;
import metrics.listwise.ShannonIndex;
import org.junit.Before;
import org.junit.Test;

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
}