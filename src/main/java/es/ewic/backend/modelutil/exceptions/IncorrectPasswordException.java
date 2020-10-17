package es.ewic.backend.modelutil.exceptions;

@SuppressWarnings("serial")
public class IncorrectPasswordException extends InstanceException {

	public IncorrectPasswordException(Object key, String className) {
		super("Incorrect password", key, className);
	}

}
