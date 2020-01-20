package user_interface;

import model.CommandType;
import model.DataTransferObject;
import model.HelpDTO;
import model.HelpType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InputParserTest {
    private InputParser input;

    @Before
    public void setUp(){
        input = new InputParser();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing unrecognized command*/
    public void testInvalidCommand() throws InvalidCommandException {
        DataTransferObject dto = input.parse("banana");
    }

    @Test
    /**test for parsing command help*/
    public void testCommandHelp() throws InvalidCommandException {
        DataTransferObject dto = input.parse("help");
        assertEquals(dto.getCommandType(), CommandType.Help);
        assertEquals(((HelpDTO) dto).getHelpType(), HelpType.Command);
    }

    @Test
    /**test for parsing aggregation method help*/
    public void testAggregationHelp() throws InvalidCommandException {
        DataTransferObject dto = input.parse("help -a");
        assertEquals(dto.getCommandType(), CommandType.Help);
        assertEquals(((HelpDTO) dto).getHelpType(), HelpType.AggregationMethod);
    }

    @Test
    /**test for parsing comparison metric help*/
    public void testPairwiseHelp() throws InvalidCommandException {
        DataTransferObject dto = input.parse("help -m");
        assertEquals(dto.getCommandType(), CommandType.Help);
        assertEquals(((HelpDTO) dto).getHelpType(), HelpType.PairwiseMetric);
    }

    @Test
    /**test for parsing data representation help*/
    public void testDataRepresentationHelp() throws InvalidCommandException {
        DataTransferObject dto = input.parse("help -f");
        assertEquals(dto.getCommandType(), CommandType.Help);
        assertEquals(((HelpDTO) dto).getHelpType(), HelpType.DataRepresentation);
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing an invalid help with extra tokens*/
    public void testHelpExtraTokens() throws InvalidCommandException {
        DataTransferObject dto = input.parse("help -f -m");
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing an invalid help with invalid type*/
    public void testHelpInvalidType() throws InvalidCommandException {
        DataTransferObject dto = input.parse("help -x");
    }
}