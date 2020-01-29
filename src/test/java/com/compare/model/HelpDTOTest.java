package com.compare.model;

import org.junit.Test;
import java.util.Objects;
import static org.junit.Assert.*;

public class HelpDTOTest {

    private CommandType helpCommand = CommandType.Help;
    private HelpType helpType = HelpType.Command;

    @Test
    public void testHelpDTO(){
        HelpDTO help = new HelpDTO();
        help.setHelpType(HelpType.Command);

        assertTrue("CommandType should be "+ helpCommand +", but is: " + help.getCommandType(), Objects.equals(help.getCommandType(), helpCommand));
        assertTrue("HelpType should be "+ helpType +", but is: " + help.getHelpType(), Objects.equals(help.getHelpType(), helpType));
    }

    @Test
    public void testEmptyHelpDTO(){
        HelpDTO help = new HelpDTO();

        assertTrue("CommandType should be "+ helpCommand +", but is: " + help.getCommandType(), Objects.equals(help.getCommandType(), helpCommand));
        assertNull("HelpType should be null, but is: " + help.getHelpType(), help.getHelpType());
    }
}
