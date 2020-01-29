package com.compare.user_interface;

import com.compare.model.DataTransferObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvalidCommandExceptionTest {

    @Test
    /**test the getErrorMessage for an InvalidCommandException*/
    public void testGetErrorMessage() {
        InputParser input = new InputParser();
        try {
            DataTransferObject dto = input.parse("help -x");
            fail();
        } catch (InvalidCommandException e) {
            assertEquals("Help type not valid: -x ", e.getErrorMessage());
        }
    }
}