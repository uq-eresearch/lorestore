package net.metadata.openannotation.lorestore.exceptions;

/**
 * This exception is thrown when there is a problem mapping an RDF 'model' to
 * ORE objects.
 */
public class LoreStoreTypeException extends RuntimeException {

	private static final long serialVersionUID = -4621706072521653796L;

	public LoreStoreTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoreStoreTypeException(String message) {
        super(message);
    }
}
