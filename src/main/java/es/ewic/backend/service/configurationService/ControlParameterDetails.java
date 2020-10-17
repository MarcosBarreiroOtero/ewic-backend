package es.ewic.backend.service.configurationService;

public class ControlParameterDetails {

	private String name;
	private String value;

	public ControlParameterDetails(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

}
