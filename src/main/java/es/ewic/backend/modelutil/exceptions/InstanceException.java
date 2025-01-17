package es.ewic.backend.modelutil.exceptions;

@SuppressWarnings("serial")
public class InstanceException extends Exception {

	private final Object key;
	private final String className;

	protected InstanceException(String specificMessage, Object key, String className) {

		super(specificMessage + " (key = '" + key + "' - className = '" + className + "')");
		this.key = key;
		this.className = className;

	}

	public Object getKey() {
		return key;
	}

	public String getClassName() {
		return className;
	}

}
