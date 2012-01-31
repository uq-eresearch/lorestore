package net.metadata.openannotation.lorestore.exceptions;

/**
 * This exception is thrown when there is a problem mapping an RDF 'model' to
 * ORE objects.
 */
public class LoreStoreDBConnectionException extends RuntimeException {

	private static final long serialVersionUID = -2811126746931445687L;

	public LoreStoreDBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoreStoreDBConnectionException(String message) {
        super(message);
    }
}
