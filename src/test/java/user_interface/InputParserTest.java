package user_interface;

import model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

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
        fail();
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
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing an invalid help with invalid type*/
    public void testHelpInvalidType() throws InvalidCommandException {
        DataTransferObject dto = input.parse("help -x");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with no parameter name or value specified*/
    public void testConfigNoSpecification() throws InvalidCommandException {
        DataTransferObject dto = input.parse("config");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with no parameter value specified*/
    public void testConfigNoValueSet() throws InvalidCommandException {
        DataTransferObject dto = input.parse("config banana");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with extra tokens*/
    public void testConfigExtraTokensShort() throws InvalidCommandException {
        DataTransferObject dto = input.parse("config numThreads 4 banana");
        fail();
    }

    @Test(expected = InvalidCommandException.class)
    /**test for parsing config command with extra tokens*/
    public void testConfigExtraTokensLong() throws InvalidCommandException {
        DataTransferObject dto = input.parse("config numThreads 4 banana orange apple");
        fail();
    }

    @Test
    /**test for parsing a valid config command without save option*/
    public void testConfigValidSyntax() throws InvalidCommandException {
        String name = "numThreads";
        String value = "4";
        DataTransferObject dto = input.parse("config " + name + " " + value);
        assertEquals(CommandType.Configure, dto.getCommandType());
        assertEquals(name, ((ConfigDTO)dto).getParameterName());
        assertEquals(value, ((ConfigDTO)dto).getParameterValue());
        assertEquals(false, ((ConfigDTO)dto).isConfigSaved());
    }

    @Test
    /**test for parsing a syntactically valid config command that is semantically invalid.
     * the parser should not know anything about what a valid name/value is, so this should
     * not throw an InvalidCommandException*/
    public void testConfigValidSyntaxInvalidSemantics() throws InvalidCommandException {
        String name = "banana";
        String value = "orange";
        DataTransferObject dto = input.parse("config " + name + " " + value);
        assertEquals(CommandType.Configure, dto.getCommandType());
        assertEquals(name, ((ConfigDTO)dto).getParameterName());
        assertEquals(value, ((ConfigDTO)dto).getParameterValue());
        assertEquals(false, ((ConfigDTO)dto).isConfigSaved());
    }

    @Test
    /**test for parsing a valid config command with save option*/
    public void testConfigValidSyntaxWithSave() throws InvalidCommandException {
        String name = "numThreads";
        String value = "4";
        DataTransferObject dto = input.parse("config " + name + " " + value + " -s");
        assertEquals(CommandType.Configure, dto.getCommandType());
        assertEquals(name, ((ConfigDTO)dto).getParameterName());
        assertEquals(value, ((ConfigDTO)dto).getParameterValue());
        assertEquals(true, ((ConfigDTO)dto).isConfigSaved());
    }
}