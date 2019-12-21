package model;

public class HelpDTO extends DataTransferObject {
	private HelpType helpType;
	
	public HelpType getHelpType() {
		return helpType;
	}

	public void setHelpType(HelpType helpType) {
		this.helpType = helpType;
	}

	public HelpDTO() {
		commandType = CommandType.Help;
	}
}
