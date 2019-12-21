package model;

public class UpdateDTO extends DataTransferObject {
	private String location;
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public UpdateDTO() {
		commandType = CommandType.Update;
	}
}
