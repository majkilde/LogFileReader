package dk.xpages.utils;

public class XMLException extends Exception {
	private static final long serialVersionUID = 1L;

	public XMLException(String message) {
		super(message);
	}

	public XMLException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
