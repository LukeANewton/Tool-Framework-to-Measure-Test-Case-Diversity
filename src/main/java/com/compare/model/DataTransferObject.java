package com.compare.model;

/**
 * The abstract representation for a DataTransferObject. DataTransferObjects 
 * are used by the user interface to communicate user requests with the
 * Controller in a way that is independent of the user interface implementation.
 * 
 * @author luke
 *
 */
public abstract class DataTransferObject {
	//the type of command the transfer object represents. must be set by each subclass constructor
	protected CommandType commandType;
	
	/**
	 * returns the type of command the object represents
	 * 
	 * @return the type of command the object represents
	 */
	public CommandType getCommandType() {
		return commandType;
	}
}
