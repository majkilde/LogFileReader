package dk.majkilde.logreader.menu;

import dk.majkilde.logreader.menu.actions.IAction;
import dk.majkilde.logreader.menu.actions.LinkAction;
import dk.majkilde.logreader.menu.actions.NSFAction;
import dk.majkilde.logreader.menu.actions.NullAction;
import dk.majkilde.logreader.menu.actions.TextReaderAction;
import dk.majkilde.logreader.menu.actions.XMLAction;
import dk.xpages.utils.XML;

public class ActionFactory {
	public static IAction createAction(XML menuXML, IMenu parent) {
		XML actionXML = menuXML.child("action");
		if (actionXML != null) {
			String actionType = actionXML.string("type").toLowerCase();

			if ("link".equals(actionType)) {
				return new LinkAction(actionXML, parent);
			}

			if ("textreader".equals(actionType)) {
				return new TextReaderAction(actionXML, parent);
			}

			if ("nsfreader".equals(actionType)) {
				return new NSFAction(actionXML, parent);
			}

			if ("xmlreader".equals(actionType)) {
				return new XMLAction(actionXML, parent);
			}
		}

		return new NullAction();
	}
}
