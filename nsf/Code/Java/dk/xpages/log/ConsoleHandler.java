package dk.xpages.log;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XSPUtils;

public class ConsoleHandler {

	private ConsoleHandler() {
	}

	private static ConsoleHandler instance = null;

	public static ConsoleHandler getInstance() {
		if (instance == null) {
			instance = new ConsoleHandler();
		}
		return instance;
	}

	private String getPagename() {
		return NotesStrings.strRightBack(XSPUtils.getUrl().getAddress(), "/");
	}

	public void publish(LogRecord record) {
		// ensure that this log record should be logged by this Handler
		if (!isLoggable(record))
			return;

		System.out.println(NotesStrings.messageFormat("NOTESLOG {0}: ({1}) {2} [{3}.{4}]", record.getLevel(), getPagename(), record
				.getMessage(), record.getSourceClassName(), record.getSourceMethodName()));

	}

	private boolean isLoggable(LogRecord record) {
		return record.getLevel().intValue() >= Level.INFO.intValue();
	}

}