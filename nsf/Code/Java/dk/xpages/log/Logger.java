package dk.xpages.log;

import static dk.xpages.utils.NotesStrings.messageFormat;

import java.io.Serializable;

public class Logger implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private NotesHandler handler = null;

	/**
	 * Creates a new named logger.
	 *
	 * @param name the logger name
	 */
	public Logger(final String name) {
		this.name = name;
	}

	public void setHandler(NotesHandler handler) {
		this.handler = handler;
	}

	public void trace(String message, Object... params) {
		log(Level.TRACE, messageFormat(message, params), null);
	}

	public void debug(String message, Object... params) {
		log(Level.DEBUG, messageFormat(message, params), null);
	}

	public void info(String message, Object... params) {
		log(Level.INFO, messageFormat(message, params), null);
	}

	public void warning(String message, Object... params) {
		log(Level.WARN, messageFormat(message, params), null);
	}

	public void error(String message, Object... params) {
		log(Level.ERROR, messageFormat(message, params), null);
	}

	public void error(Throwable t) {
		log(Level.ERROR, messageFormat("{0}", t.getMessage()), null);
	}

	private void log(Level level, String msg, Throwable t) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		String classname = stackTraceElements[4].getClassName();
		String methodname = stackTraceElements[4].getMethodName();

		LogRecord record = new LogRecord(name, classname, methodname, msg, level, t);
		handler.publish(record);
	}

}
