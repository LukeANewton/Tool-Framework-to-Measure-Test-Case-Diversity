package com.compare.model;

/**
 * A data transfer object used to encapsulate the information to issue a 
 * system configuration command. Config commands are used to set defaults 
 * for values used in compare commands, such as number of threads if using 
 * a thread pool, the precision of results, and default locations for finding
 * pairwise diversity com.compare.metrics, data representations, etc.
 * 
 * @author luke
 *
 */
public class ConfigDTO extends DataTransferObject {
	private String parameterName;
	private String parameterValue;
	private boolean configSaved;
	
	/**Constructor*/
	public ConfigDTO() {
		commandType = CommandType.Configure;
		configSaved = false;
	}

	public boolean isConfigSaved() {
		return configSaved;
	}


	public void setConfigSaved(boolean saveConfiguration) {
		this.configSaved = saveConfiguration;
	}


	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
	
}
