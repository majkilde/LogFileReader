package dk.xpages.utils;

import java.util.Date;

import lotus.domino.Base;
import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.View;

import com.ibm.domino.xsp.module.nsf.NotesContext;
import com.ibm.xsp.model.domino.DominoUtils;

public class NotesObjects extends DominoUtils {

	public static Session getSessionAsSigner() {
		NotesContext nc = NotesContext.getCurrentUnchecked();
		return (null != nc) ? nc.getSessionAsSignerFullAdmin() : null;
	}

	private static String getCurrentServer() {
		Session session = getCurrentSession();
		try {
			return session.getServerName();
		} catch (NotesException e) {
			return "";
		}
	}

	private static String getServer(String server) {
		if (NotesStrings.isBlank(server) || server.equals("*")) {
			return getCurrentServer();
		} else {
			return server;
		}
	}

	/*
	 * servername!!filepath		open the file on the specified server
	 * *!!filepath				open on current server
	 * filepath					open on current server
	 * *						currentDatabase
	 * servername::replicaid	open the file on the specified server
	 * ::replicaid				open on current server
	 */
	public static Database getDatabase(Session session, String reference) {
		String server = "";
		String filepath = "";
		try {
			if (reference.indexOf("!!") >= 0) {
				server = getServer(NotesStrings.strToken(reference, "!!", 0));
				filepath = NotesStrings.strToken(reference, "!!", 1);
				return session.getDatabase(server, filepath);
			} else if (reference.indexOf("::") >= 0) {
				server = getServer(NotesStrings.strToken(reference, "::", 0));
				filepath = NotesStrings.strToken(reference, "::", 1);
				return getDatabase(session, server + "!!" + DominoUtils.getDbNameByReplicaID(server, filepath));
			} else {
				if (reference.equals("*")) {
					return getCurrentDatabase();
				} else {
					return session.getDatabase(null, reference);
				}
			}
		} catch (NotesException e) {
			return null;
		}
	}

	public static Database getDatabase(String reference) {
		Session session = getCurrentSession();
		return getDatabase(session, reference);
	}

	public static Database getDatabaseAsSigner(String reference) {
		Session session = getSessionAsSigner();
		return getDatabase(session, reference);
	}

	public static Database getCurrentDatabaseAsSigner() {
		Session session = getSessionAsSigner();
		return getDatabase(session, "*");
	}

	public static View getView(Database db, String viewname) {
		assert db != null;

		try {
			return db.getView(viewname);
		} catch (NotesException e) {
			return null;
		}
	}

	public static Document getDocument(Database db, String unidOrId) {
		assert db != null;

		if (NotesStrings.isBlank(unidOrId)) {
			return null;
		}

		try {
			if (unidOrId.length() == 32) {
				return db.getDocumentByUNID(unidOrId);
			} else {
				return db.getDocumentByID(unidOrId);
			}
		} catch (NotesException e) {
			return null;
		}
	}

	public static Document getDocument(View view, String key, Boolean exactMatch) {
		assert view != null;

		try {
			return view.getDocumentByKey(key, exactMatch);
		} catch (NotesException e) {
			return null;
		}
	}

	public static void incinerate(Object... dominoObjects) {
		for (Object dominoObject : dominoObjects) {
			if (null != dominoObject) {
				if (dominoObject instanceof Base) {
					try {
						((Base) dominoObject).recycle();
					} catch (NotesException recycleSucks) {
						System.out.println("Can't recycle object");
					}
				}
			}
		}
	}

	public static DateTime getNow() {
		DateTime result = null;
		Date date = new Date();
		Session session = getCurrentSession();
		try {
			result = session.createDateTime(date);
		} catch (NotesException e) {
			e.printStackTrace();
		}

		incinerate(session);
		return result;
	}
}
