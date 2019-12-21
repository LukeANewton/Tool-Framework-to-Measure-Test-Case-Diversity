package model;

/**
 * A data transfer object used for issuing an exit command to the controller.
 * Exit commands shut down the system.
 * 
 * @author luke
 *
 */
public class ExitDTO extends DataTransferObject {
	/**constructor*/
	public ExitDTO() {
		commandType = CommandType.Exit;
	}
}
