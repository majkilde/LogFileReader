package dk.majkilde.logreader.menu;

import dk.xpages.utils.XML;

public class ActionFactory {
	public static IAction createAction(XML menuXML, IMenu parent) {
		XML actionXML = menuXML.child("action");
		if (actionXML != null) {
			String actionType = actionXML.string("type").toLowerCase();

			if ("link".equals(actionType)) {
				return new LinkAction(parent).setUrl(actionXML.string("url"));
			}

			if ("textreader".equals(actionType)) {
				return new TextReaderAction(parent).setFilename(actionXML.child("filename").content());
			}
		}

		return new NullAction();
	}
}
