package es.ewic.backend.modelutil.exceptions;

@SuppressWarnings("serial")
public class MaxCapacityException extends InstanceException {

	public MaxCapacityException(Object key, String className) {
		super("Max capacity reached", key, className);
	}

}
