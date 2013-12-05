package dk.majkilde.logreader.menu;

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
		}

		return new NullAction();
	}
}
