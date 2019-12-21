package model;

/**
 * a data transfer object used to issue update commands to the controller.
 * Update commands reboot the system. This would be used when new metrics or 
 * data representation class files are added to the src directory while the 
 * system is running. The user could quit and restart themselves instead of 
 * using this command, and it would achieve the same functionality.
 * 
 * @author luke
 *
 */
public class UpdateDTO extends DataTransferObject {
	//the class name of the newly added class
	private String location;
	
	/**Constructor*/
	public UpdateDTO() {
		commandType = CommandType.Update;
	}
	
	/**
	 * returns the name of the newly added class
	 * @return the name of the newly added class
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * sets the name of the newly added class
	 * 
	 * @param location the name of the newly added class
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	
}
