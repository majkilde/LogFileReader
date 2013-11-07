package dk.xpages.log;

import static dk.xpages.utils.NotesObjects.incinerate;

import java.util.Vector;
import java.util.logging.LogRecord;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;
import lotus.domino.ViewNavigator;

import com.ibm.xsp.designer.context.XSPUrl;
import com.ibm.xsp.model.domino.DominoUtils;

import dk.xpages.utils.NotesObjects;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XSPUtils;

public class NotesHandler {
	private String unid = null;
	private int sessionId = 0;
	private final Clock watch = new Clock();

	public NotesHandler() {
	}

	private boolean isTruncated(Document doc) throws NotesException {
		return doc.getItemValueString("truncated") == "1";
	}

	private void setTruncated(Document doc) throws NotesException {
		doc.replaceItemValue("truncated", "1").recycle();
	}

	private void writeRecord(Document doc, LogRecord record) {
		try {
			Item item = doc.getFirstItem("log");
			if (item == null) {
				item = doc.replaceItemValue("log", "");
			}

			//Add the record
			if (!isTruncated(doc)) {
				String message = getFormatter().format(record);

				final int MAX_ITEM_SIZE_IN_NOTES = 32000;
				int messageSize = item.getValueLength() + message.length();
				if (messageSize > MAX_ITEM_SIZE_IN_NOTES) {
					setTruncated(doc);
					message = NotesStrings.left(message, MAX_ITEM_SIZE_IN_NOTES - item.getValueLength());
				}

				item.appendToTextList(message);
			}

			//Update control fields
			doc.replaceItemValue("elapsed", watch.getWatch("TOTAL").getTime()).recycle();

			if (!doc.hasItem("level") || doc.getItemValueInteger("level") < record.getLevel().intValue()) {
				//new higher level
				doc.replaceItemValue("message", NotesStrings.messageFormat("{0} [{1}]", record.getMessage(), record.getSourceMethodName()))
						.recycle();
				doc.replaceItemValue("level", record.getLevel().intValue()).recycle();
				doc.replaceItemValue("levelText", record.getLevel().getLocalizedName()).recycle();

			}
			incinerate(item);
		} catch (NotesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private NotesFormatter getFormatter() {
		return new NotesFormatter();
	}

	private static final int NUMBER_OF_LOGS_TO_KEEP = 20;

	@SuppressWarnings("unchecked")
	private Document getReusableDocument(Database db) {
		Document doc = null;
		View view = NotesObjects.getView(db, "$logs");
		ViewNavigator nav = null;

		String category = getPagename();
		try {

			if (view != null) {
				nav = view.createViewNavFromCategory(category);

				if (nav != null && nav.getCount() >= NUMBER_OF_LOGS_TO_KEEP) {
					doc = nav.getLastDocument().getDocument();

					//Remove all items from old document
					Vector<Item> items = doc.getItems();
					for (int i = 0; i < items.size(); i++) {
						Item item = items.get(i);
						doc.removeItem(item.getName());
						incinerate(item);
					}

					//Create new default values
					createDocument(doc);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		incinerate(nav, view);

		if (doc == null) {
			doc = createDocument(db);
		}
		return doc;
	}

	private String getPagename() {
		return NotesStrings.strRightBack(XSPUtils.getUrl().getAddress(), "/");
	}

	private Document createDocument(Document doc) {
		try {
			//References
			doc.replaceItemValue("Form", "log").recycle();
			doc.replaceItemValue("sessionId", sessionId).recycle(); //not used for anything ...
			doc.replaceItemValue("$created", NotesObjects.getNow()).recycle();
			doc.replaceItemValue("page", getPagename()).recycle();
			doc.replaceItemValue("systemDocument", 1).recycle();

			//URL
			XSPUrl url = XSPUtils.getUrl();
			doc.replaceItemValue("url", url.getAddress()).recycle();
			doc.replaceItemValue("queryString", url.getQueryString()).recycle();

			//User
			Session session = XSPUtils.getCurrentSession();
			Item item = doc.replaceItemValue("username", session.getEffectiveUserName());
			item.setNames(true);
			incinerate(item);

			//Memory
			//doc.replaceItemValue("usedMemoryBefore", NotesLog.getUsedMemory()).recycle();
			//doc.replaceItemValue("heapBefore", ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()).recycle();
			//doc.replaceItemValue("nonHeapBefore", ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed()).recycle();

			//Timers
			watch.resetAll();
			watch.start("TOTAL");

			unid = doc.getUniversalID();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;

	}

	private Document createDocument(Database db) {
		try {
			return createDocument(db.createDocument());
		} catch (NotesException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void publish(LogRecord record) {
		// ensure that this log record should be logged by this Handler
		if (!isLoggable(record))
			return;

		Database db = NotesObjects.getCurrentDatabaseAsSigner();
		Document doc = null;

		//reuse the current document (if any)
		int currentSession = getSessionId();
		if (currentSession == sessionId) {
			doc = NotesObjects.getDocument(db, unid);
		}
		sessionId = currentSession;

		//create
		if (doc == null) {
			doc = getReusableDocument(db);
		}

		//add the message
		writeRecord(doc, record);
		//save
		try {
			doc.save();
		} catch (Exception e) {
			e.printStackTrace();
		}

		incinerate(doc, db);

	}

	private boolean isLoggable(LogRecord record) {
		return true;
	}

	private int getSessionId() {
		return DominoUtils.getCurrentSession().hashCode();
	}

}
