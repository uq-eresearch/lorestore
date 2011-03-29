package oreservlet.exceptions;

public class OREException extends Exception {
	private static final long serialVersionUID = 8125088905683714398L;

	
	public OREException(Throwable e) {
		super(e);
	}
	
	public OREException(String message, Throwable e) {
		super(message, e);
	}
	
	public OREException(String message) {
		super(message);
	}
}
