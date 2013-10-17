package dk.xpages.log;

public enum Level {

	/**
	 * No events will be logged.
	 */
	OFF(0),

	/**
	 * A severe error that will prevent the application from continuing.
	 */
	FATAL(1),

	/**
	 * An error in the application, possibly recoverable.
	 */
	ERROR(2),

	/**
	 * An event that might possible lead to an error.
	 */
	WARN(3),

	/**
	 * An event for informational purposes.
	 */
	INFO(4),

	/**
	 * A general debugging event.
	 */
	DEBUG(5),

	/**
	 * A fine-grained debug message, typically capturing the flow through the application.
	 */
	TRACE(6),

	/**
	 * All events should be logged.
	 */
	ALL(Integer.MAX_VALUE);

	private final int intLevel;

	private Level(final int val) {
		intLevel = val;
	}

	/**
	 * Compares the specified Level against this one.
	 * @param level The level to check.
	 * @return True if the passed Level is more specific or the same as this Level.
	 */
	public boolean lessOrEqual(final Level level) {
		return intLevel <= level.intLevel;
	}

	/**
	 * Compares the specified Level against this one.
	 * @param level The level to check.
	 * @return True if the passed Level is more specific or the same as this Level.
	 */
	public boolean lessOrEqual(final int level) {
		return intLevel <= level;
	}

	/**
	 * Returns the integer value of the Level.
	 * @return the integer value of the Level.
	 */
	public int intLevel() {
		return intLevel;
	}
}
