package net.metadata.auselit.lorestore.exceptions;

/**
 * This exception is thrown if there is a problem configuring the ORE
 * server. This is a 'fatal' exception.
 * 
 */
public class LoreStoreConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 9181403149277727650L;

	public LoreStoreConfigurationException(Throwable ex) {
        super(ex);
    }

    public LoreStoreConfigurationException(String message, Throwable ex) {
        super(message, ex);
    }

    public LoreStoreConfigurationException(String message) {
        super(message);
    }
}
