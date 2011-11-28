package net.metadata.auselit.lorestore.exceptions;

public class LoreStoreException extends Exception {
	private static final long serialVersionUID = 8125088905683714398L;

	
	public LoreStoreException(Throwable e) {
		super(e);
	}
	
	public LoreStoreException(String message, Throwable e) {
		super(message, e);
	}
	
	public LoreStoreException(String message) {
		super(message);
	}
}
