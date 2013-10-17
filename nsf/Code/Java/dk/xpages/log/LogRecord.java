package dk.xpages.log;

public class LogRecord {
	private String loggerName = "";
	private String className = "";
	private String methodName = "";
	private Level level = Level.OFF;
	private String message = "";
	private Throwable thrown = null;

	public LogRecord(String loggerName, String className, String methodName, String message, Level level, Throwable t) {
		this.loggerName = loggerName;
		this.className = className;
		this.methodName = methodName;
		this.message = message;
		this.level = level;
		this.thrown = t;
	}

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String method) {
		this.methodName = method;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getThrown() {
		return thrown;
	}

	public void setThrown(Throwable t) {
		this.thrown = t;
	}

}
