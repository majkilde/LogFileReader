package dk.xpages.log;

import static dk.xpages.utils.NotesStrings.messageFormat;

import java.io.Serializable;
import java.util.logging.LogRecord;

import com.ibm.xsp.model.domino.DominoUtils;

import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XSPUtils;

public class Logger implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name = "";

	private int sessionid = 0;
	private String pagename = "";

	/**
	 * Creates a new named logger.
	 *
	 * @param name the logger name
	 */
	public Logger(final String name) {
		this.name = name;

		int newSessionid = getSessionId();
		String newPagename = getPagename();

		if (sessionid != newSessionid || !pagename.equals(newPagename)) {
			sessionid = newSessionid;
			pagename = newPagename;

		}
	}

	private static int getSessionId() {
		try {
			return DominoUtils.getCurrentSession().hashCode();
		} catch (Exception e) {
			return 0;
		}
	}

	private static String getPagename() {
		try {
			return NotesStrings.strRightBack(XSPUtils.getUrl().getAddress(), "/");
		} catch (Exception e) {
			return "";
		}
	}

	public void trace(String message, Object... params) {
		log(Level.TRACE, messageFormat(message, params), null);
	}

	public void debug(String message, Object... params) {
		log(Level.DEBUG, messageFormat(message, params), null);
	}

	public void info(String message) {
		log(Level.INFO, message, null);
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

	private static java.util.logging.Level getJavaLevel(Level level) {
		java.util.logging.Level logLevel = java.util.logging.Level.OFF;
		if (level.equals(Level.DEBUG)) {
			logLevel = java.util.logging.Level.FINE;
		} else if (level.equals(Level.ERROR)) {
			logLevel = java.util.logging.Level.SEVERE;
		} else if (level.equals(Level.FATAL)) {
			logLevel = java.util.logging.Level.SEVERE;
		} else if (level.equals(Level.INFO)) {
			logLevel = java.util.logging.Level.INFO;
		} else if (level.equals(Level.TRACE)) {
			logLevel = java.util.logging.Level.FINEST;
		} else if (level.equals(Level.WARN)) {
			logLevel = java.util.logging.Level.WARNING;
		} else if (level.equals(Level.ALL)) {
			logLevel = java.util.logging.Level.ALL;
		}

		return logLevel;

	}

	private static StackTraceElement getStackTraceElement(final int depth) {
		return new Throwable().getStackTrace()[depth];
	}

	private void log(Level level, String msg, Throwable thrown) {
		StackTraceElement stacktrace = getStackTraceElement(5);

		LogRecord record = new LogRecord(getJavaLevel(level), msg);
		record.setSourceClassName(stacktrace.getClassName());
		record.setSourceMethodName(stacktrace.getMethodName());
		record.setThrown(thrown);
		NotesHandler handler = new NotesHandler();
		handler.publish(record);

		//Always log to the standard java.util.logging 
		java.util.logging.Logger javalogger = java.util.logging.Logger.getLogger(name);
		//		javalogger.logp(getJavaLevel(level), stacktrace.getClassName(), stacktrace.getMethodName(), messageFormat("{0} (line: )", msg,
		//				stacktrace.getLineNumber()), thrown);
		javalogger.log(record);
	}

}
