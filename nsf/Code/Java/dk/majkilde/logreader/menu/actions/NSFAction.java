package dk.majkilde.logreader.menu.actions;

import java.io.Serializable;

import lotus.domino.Database;
import dk.majkilde.logreader.menu.IMenu;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesObjects;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class NSFAction implements IAction, Serializable {

	private final Logger log = LogManager.getLogger();
	private static final long serialVersionUID = 1L;

	private final String filename;
	private final String viewname;

	private final IMenu parent;

	public NSFAction(XML config, IMenu parent) {
		this.parent = parent;
		this.filename = config.child("filename").content();
		this.viewname = config.child("viewname").content();

	}

	public IMenu getParent() {
		return parent;
	}

	public void execute() {
		XSPUtils.getViewScope().put("filename", filename);
		XSPUtils.getViewScope().put("viewname", viewname);
	}

	public boolean isValid() {
		Database db = NotesObjects.getDatabase("*!!" + filename);
		boolean isopen = false;
		try {
			db.open();
			isopen = db.isOpen();
		} catch (Exception e) {
			log.error(e);
		}

		return isopen;
	}
}
