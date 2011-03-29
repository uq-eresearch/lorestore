package net.metadata.auselit.lorestore.exceptions;

public class NotFoundException extends OREException {
	private static final long serialVersionUID = 4134073166938605565L;

	public NotFoundException(String message) {
		super(message);
	}
	public NotFoundException(String message, Throwable e) {
		super(message, e);
	}
	public NotFoundException(Throwable e) {
		super(e);
	}
}
