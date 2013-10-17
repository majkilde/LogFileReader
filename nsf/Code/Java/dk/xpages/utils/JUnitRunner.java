package dk.xpages.utils;

import static dk.xpages.utils.NotesStrings.messageFormat;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;

public class JUnitRunner {
	private final StringBuilder html = new StringBuilder();
	private final Logger log = LogManager.getLogger();

	public JUnitRunner() {

	}

	public String getHtml() {
		return "<div id=\"junit\">" + html.toString() + "</div>";
	}

	private void addSuccess(Result result, String classname) {
	}

	private void addFailed(Result result, String classname) {

		html.append(messageFormat("{0} test(s) failed:", result.getFailureCount()));

		html.append("<table cellpadding=0 cellspacing=0><tr><th>Method</th><th>Message</th><th>Class</th></tr>");
		for (Failure failure : result.getFailures()) {

			html.append("<tr>");
			Description description = failure.getDescription();
			html.append(messageFormat("<td>{0}</td>", description.getMethodName()));
			html.append(messageFormat("<td>{0}</td>", failure.getMessage()));
			html.append(messageFormat("<td>{0}</td>", description.getClassName()));

			html.append("</tr>");
		}
		html.append("</table>");

	}

	private void addHeader(String title, String styleclass) {
		html.append(messageFormat("<h2 class=\"{1}\">{0}</h2>", title, styleclass));
	}

	private void addStatistics(Result result) {
		html.append(messageFormat("{0} test(s) ran in {1} ms<br/>", result.getRunCount(), result.getRunTime()));

	}

	@SuppressWarnings("unchecked")
	public void runTest(String classname) {
		Class clazz;
		try {
			clazz = Class.forName(classname);
			log.info("Test class or suite: {0}", classname);
			Result result = JUnitCore.runClasses(clazz);
			if (result.wasSuccessful()) {
				addHeader("Passed: " + clazz.getName(), "success");
				addStatistics(result);
				addSuccess(result, clazz.getName());
			} else {
				addHeader("Failed: " + clazz.getName(), "failed");
				addStatistics(result);
				addFailed(result, clazz.getName());
			}

		} catch (ClassNotFoundException e) {
			log.error(e);
			addHeader("Class not found: " + classname, "failed");
			return;
		}

	}
}