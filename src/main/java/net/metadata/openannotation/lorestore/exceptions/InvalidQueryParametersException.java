package net.metadata.openannotation.lorestore.exceptions;

public class InvalidQueryParametersException extends LoreStoreException {
	private static final long serialVersionUID = -1712694785413468981L;

	public InvalidQueryParametersException(String message) {
		super(message);
	}
	public InvalidQueryParametersException(String message, Throwable e) {
		super(message, e);
	}
	public InvalidQueryParametersException(Throwable e) {
		super(e);
	}
}
