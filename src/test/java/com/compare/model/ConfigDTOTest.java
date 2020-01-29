package com.compare.model;

import org.junit.Test;
import java.util.Objects;
import static org.junit.Assert.*;

public class ConfigDTOTest {

    private CommandType configCommand = CommandType.Configure;
    private String parameterName = "Name";
    private String parameterValue = "Value";

    @Test
    /**
     * test setting all variables and check that CommandType is correctly set
     */
    public void testConfigDTO(){
        ConfigDTO config = new ConfigDTO();
        config.setParameterName(parameterName);
        config.setParameterValue(parameterValue);
        config.setConfigSaved(true);

        assertTrue("CommandType should be "+ configCommand +", but is: " + config.getCommandType(), Objects.equals(config.getCommandType(), configCommand));
        assertTrue("parameterName should be "+ parameterName +", but is: " + config.getParameterName(), Objects.equals(config.getParameterName(), parameterName));
        assertTrue("CommandType should be "+ parameterValue +", but is: " + config.getParameterValue(), Objects.equals(config.getParameterValue(), parameterValue));
        assertTrue("ConfigSaved should be true, but is: " + config.isConfigSaved(), config.isConfigSaved());
    }

    @Test
    /**
     * test setting no variables and check that CommandType and ConfigSaved are correctly set
     */
    public void testEmptyConfigDTO(){
        ConfigDTO config = new ConfigDTO();

        assertTrue("CommandType should be "+ configCommand +", but is: " + config.getCommandType(), Objects.equals(config.getCommandType(), configCommand));
        assertNull("parameterName should be null, but is: " + config.getParameterName(), config.getParameterName());
        assertNull("CommandType should be null, but is: " + config.getParameterValue(), config.getParameterValue());
        assertFalse("ConfigSaved should be false, but is: " + config.isConfigSaved(), config.isConfigSaved());
    }
}
