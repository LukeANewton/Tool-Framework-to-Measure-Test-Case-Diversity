package model;

import org.junit.Test;
import java.util.Objects;
import static org.junit.Assert.*;

public class UpdateDTOTest {

    private CommandType updateCommand = CommandType.Update;
    private String location = "location";

    @Test
    /**
     * test setting all variables and check that CommandType is correctly set
     */
    public void testUpdateDTO(){
        UpdateDTO update = new UpdateDTO();
        update.setLocation(location);

        assertTrue("CommandType should be "+ updateCommand +", but is: " + update.getCommandType(), Objects.equals(update.getCommandType(), updateCommand));
        assertTrue("Location should be "+ location +", but is: " + update.getLocation(), Objects.equals(update.getLocation(), location));
    }

    @Test
    /**
     * test setting no variables and check that CommandType is correctly set
     */
    public void testEmptyUpdateDTO(){
        UpdateDTO update = new UpdateDTO();

        assertTrue("CommandType should be "+ updateCommand +", but is: " + update.getCommandType(), Objects.equals(update.getCommandType(), updateCommand));
        assertNull("Location should be null, but is: " + update.getLocation(), update.getLocation());
    }
}
