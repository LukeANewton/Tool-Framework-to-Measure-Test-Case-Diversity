package com.compare.core;

/**
 * An exception thrown when a test case in a file does not conform to the format specified in the DataRepresentation being used.
 * 
 * @author luke
 */
public class InvalidFormatException extends Exception {

    public InvalidFormatException() {
        super();
    }

    public InvalidFormatException(String s) {
        super(s);
    }
}
