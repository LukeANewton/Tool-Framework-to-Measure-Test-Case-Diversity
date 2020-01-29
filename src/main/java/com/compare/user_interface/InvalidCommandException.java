package com.compare.user_interface;

/**
 * An exception thrown when the input entered in the system is invalid. Contains
 * an error message describing why the command was rejected.
 * 
 * @author luke
 *
 */
public class InvalidCommandException extends Exception {
	private static final long serialVersionUID = -8095044455374094735L;

	//a short description of why the command was determined invalid
	private String errorMessage;

	/**
	 * Constructor
	 * 
	 * @param errorMsg a short description of why the command was determined invalid
	 */
	public InvalidCommandException(String errorMsg) {
		errorMessage = errorMsg;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
}
