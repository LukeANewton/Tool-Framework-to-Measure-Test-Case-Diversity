package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HelpDTOTest {

    private CommandType helpCommand = CommandType.Help;
    private HelpType helpType = HelpType.COMMAND;

    @Test
    public void testHelpDTO(){
        HelpDTO help = new HelpDTO();
        help.setHelpType(HelpType.COMMAND);

        assertEquals("CommandType should be " + helpCommand + ", but is: " + help.getCommandType(), help.getCommandType(), helpCommand);
        assertEquals("HelpType should be " + helpType + ", but is: " + help.getHelpType(), help.getHelpType(), helpType);
    }

    @Test
    public void testEmptyHelpDTO(){
        HelpDTO help = new HelpDTO();

        assertEquals("CommandType should be " + helpCommand + ", but is: " + help.getCommandType(), help.getCommandType(), helpCommand);
        assertNull("HelpType should be null, but is: " + help.getHelpType(), help.getHelpType());
    }
}
