package com.compare.model;

/**
 * A data transfer object used to issue help commands. Help commands request
 * information about system functionalities
 * 
 * @author luke
 *
 */
public class HelpDTO extends DataTransferObject {
	//the type of information requested in the command
	private HelpType helpType;
	
	/**Constructor*/
	public HelpDTO() {
		commandType = CommandType.Help;
	}
	
	/**
	 * returns the type of information requested for this help command
	 * 
	 * @return type of information requested for this help command
	 */
	public HelpType getHelpType() {
		return helpType;
	}

	/**
	 * set the type of information requested for this help command
	 * 
	 * @param helpType a member of the HelpType enumeration that denotes what information is requested
	 */
	public void setHelpType(HelpType helpType) {
		this.helpType = helpType;
	}

	
}
