package dk.majkilde.logreader.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.notes.addins.DominoServer;
import lotus.notes.addins.ServerAccess;
import dk.majkilde.logreader.menu.actions.IAction;
import dk.majkilde.logreader.menu.actions.NullAction;
import dk.xpages.utils.NotesObjects;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;

public class MenuItem implements IMenu, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Map<String, IMenu> items = new LinkedHashMap<String, IMenu>();

	private final String name;
	private final String id;
	private boolean restricted = false;

	private IAction action = new NullAction();

	private MenuItem(final String name, final String id) {
		this.name = name;
		if (NotesStrings.isBlank(id)) {
			this.id = generateId(name);
		} else {
			this.id = id;
		}
	}

	private MenuItem(final String name) {
		this(name, null);
	}

	private static String generateId(String name) {
		return name.replaceAll("\\s+", "").toLowerCase();
	}

	public static IMenu createFromXML(XML menuXML) throws Exception {
		MenuItem menu = null;
		if (NotesStrings.equalsIgnoreCase(menuXML.name(), "menu")) {
			//new root element
			menu = new MenuItem("root");
		} else {
			menu = new MenuItem(menuXML.string("name"), menuXML.string("id"));
		}

		menu.setRestricted(menuXML.string("restricted").equalsIgnoreCase("true"));

		menu.setAction(ActionFactory.createAction(menuXML, menu));

		for (XML itemXML : menuXML.children("item")) {
			IMenu sub = MenuItem.createFromXML(itemXML);
			menu.add(sub);
		}
		return menu;
	}

	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void add(IMenu menuitem) {
		if (menuitem.isVisible()) {
			items.put(menuitem.getId(), menuitem);
		}
	}

	public IMenu getChild(String id) {
		return items.get(id);
	}

	public List<IMenu> getChildren() {
		return new ArrayList<IMenu>(items.values());
	}

	public IAction getAction() {
		return action;
	}

	public void setAction(IAction action) {
		this.action = action;
	}

	public void executeAction() {
		action.execute();
	}

	public boolean isVisible() {
		try {
			if (restricted) {
				return getAction().isValid() && canReadLogs(NotesObjects.getCurrentSession());
			} else {
				return getAction().isValid();
			}
		} catch (Exception e) {
			return false;
		}
	}

	private boolean canReadLogs(final Session session) {

		boolean result = false;

		try {
			String username = session.getEffectiveUserName();

			DominoServer server = new DominoServer();

			result = server.checkServerAccess(username, ServerAccess.PROG_UNRESTRICTED);

			result = result || server.checkServerAccess(username, ServerAccess.VIEW_ONLY_ADMIN);

		} catch (NotesException ne) {

			ne.printStackTrace();

		}
		return result;

	}

}
