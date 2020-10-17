package es.ewic.backend.modelutil.exceptions;

@SuppressWarnings("serial")
public class NoAuthorizedException extends InstanceException {

	public NoAuthorizedException(Object key, String className) {
		super("Operation no authorized", key, className);
	}
}
