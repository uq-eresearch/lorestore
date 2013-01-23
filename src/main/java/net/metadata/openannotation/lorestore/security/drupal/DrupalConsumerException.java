package net.metadata.openannotation.lorestore.security.drupal;

public class DrupalConsumerException extends Exception {
    private static final long serialVersionUID = 1L;

	public DrupalConsumerException(String message) {
        super(message);
    }

    public DrupalConsumerException(String message, Throwable t) {
        super(message, t);
    }
}
