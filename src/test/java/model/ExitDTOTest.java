package model;

import org.junit.Test;
import java.util.Objects;
import static org.junit.Assert.*;

public class ExitDTOTest {

    private CommandType exitCommand = CommandType.Exit;

    @Test
    /**
     * test that commandType is correctly set
     */
    public void testExitDTO(){
        ExitDTO exit = new ExitDTO();

        assertTrue("CommandType should be "+ exitCommand +", but is: " + exit.getCommandType(), Objects.equals(exit.getCommandType(), exitCommand));
    }
}
