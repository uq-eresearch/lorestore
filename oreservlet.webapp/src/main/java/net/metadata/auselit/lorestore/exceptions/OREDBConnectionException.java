package net.metadata.auselit.lorestore.exceptions;

/**
 * This exception is thrown when there is a problem mapping an RDF 'model' to
 * ORE objects.
 * 
 * @author uqdayers
 */
public class OREDBConnectionException extends RuntimeException {

	private static final long serialVersionUID = -2811126746931445687L;

	public OREDBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public OREDBConnectionException(String message) {
        super(message);
    }
}
