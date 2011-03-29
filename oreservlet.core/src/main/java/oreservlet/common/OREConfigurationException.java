package oreservlet.common;

/**
 * This exception is thrown if there is a problem configuring the ORE
 * server. This is a 'fatal' exception.
 * 
 * @author uqdayers
 */
public class OREConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 9181403149277727650L;

	public OREConfigurationException(Throwable ex) {
        super(ex);
    }

    public OREConfigurationException(String message, Throwable ex) {
        super(message, ex);
    }

    public OREConfigurationException(String message) {
        super(message);
    }
}
