package dk.xpages.log;

import static dk.xpages.utils.NotesStrings.messageFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import org.apache.commons.lang3.exception.ExceptionUtils;

import dk.xpages.utils.NotesStrings;

public class NotesFormatter extends Formatter {
	private final Clock watch = new Clock();

	private String getPhaseListener(LogRecord record) {
		if (record.getSourceMethodName().contains("before")) {
			watch.start();
			return messageFormat("<fieldset><legend>PHASE: {0}</legend>\n", record.getMessage());
		} else {
			if (watch.hasWatch()) {
				long ms = watch.getWatch().getTime();
				watch.reset();
				return messageFormat("<br/><i>Time: {0,number} ms</i></fieldset>\n", ms);
			} else {
				return "</fieldset>\n";
			}
		}
	}

	private boolean isPhaseListener(LogRecord record) {
		return (record.getSourceClassName().equals("dk.xpages.log.PhaseListener"));
	}

	// This method is called for every log records
	@Override
	public String format(LogRecord record) {
		StringBuilder html = new StringBuilder();

		if (isPhaseListener(record)) {
			html.append(getPhaseListener(record));
		} else {
			String style = record.getLevel().getName();
			html.append(messageFormat("<div class=\"row {0}\">", style));

			//Not using this at the moment
			if (record.getThrown() != null) {
				String exception = ExceptionUtils.getStackTrace(record.getThrown());
				exception = NotesStrings.replace(exception, "\n", "<br/>");
				html.append(messageFormat("<div class=\"Exception\">{0}</div>", exception));
			}

			html.append(messageFormat("<span class=\"column col1\">{0}&nbsp;</span>", record.getSourceClassName()));
			html.append(messageFormat("<span class=\"column col2\">{0}&nbsp;</span>", record.getSourceMethodName()));
			html.append(messageFormat("<span class=\"column col3\">{0}&nbsp;</span>", record.getMessage()));

			html.append("</div>\n");
		}
		return html.toString();
	}

	@SuppressWarnings("unused")
	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("dd. MMM yyyy HH:mm");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}

}
