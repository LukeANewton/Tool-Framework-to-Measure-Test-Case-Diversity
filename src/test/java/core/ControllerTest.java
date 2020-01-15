package core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void processCommand() {
        String command = "compare data data2 CSV -a AverageValue -m JaccardIndex -s out.txt";

        Controller controller = new Controller();
        controller.processCommand(command);

    }
}