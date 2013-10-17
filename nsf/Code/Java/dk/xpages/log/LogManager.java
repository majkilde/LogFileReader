package dk.xpages.log;

import java.util.HashMap;
import java.util.Map;

public class LogManager {
	private static Map<String, Logger> loggers = new HashMap<String, Logger>();
	private static final NotesHandler handler = new NotesHandler();

	/**
	 * Gets the class name of the caller in the current stack at the given {@code depth}.
	 *
	 * @param depth a 0-based index in the current stack.
	 * @return a class name
	 */

	private static String getClassName(final int depth) {
		return new Throwable().getStackTrace()[depth].getClassName();
	}

	/**
	 * Returns a Logger with the name of the calling class.
	 * @return The Logger for the calling class.
	 */
	public static Logger getLogger() {
		return getLogger(getClassName(2));
	}

	/**
	 * Returns a Logger using the fully qualified name of the Class as the Logger name.
	 * @param clazz The Class whose name should be used as the Logger name. If null it will default to the calling
	 *              class.
	 * @return The Logger.
	 */
	public static Logger getLogger(final Class<?> clazz) {
		return getLogger(clazz != null ? clazz.getName() : getClassName(2));
	}

	/**
	 * Returns a Logger with the specified name.
	 *
	 * @param name The logger name. If null the name of the calling class will be used.
	 * @return The Logger.
	 */
	public static Logger getLogger(final String name) {
		final String actualName = name != null ? name : getClassName(2);

		if (loggers.containsKey(actualName)) {
			return loggers.get(actualName);
		} else {
			Logger logger = new Logger(actualName);
			logger.setHandler(handler);
			loggers.put(actualName, logger);
			return logger;
		}
	}
}
