package net.metadata.auselit.lorestore.exceptions;

/**
 * This exception is thrown when there is a problem mapping an RDF 'model' to
 * ORE objects.
 * 
 * @author uqdayers
 */
public class ORETypeException extends RuntimeException {

	private static final long serialVersionUID = -4621706072521653796L;

	public ORETypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ORETypeException(String message) {
        super(message);
    }
}
