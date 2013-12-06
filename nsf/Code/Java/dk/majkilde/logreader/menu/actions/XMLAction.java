package dk.majkilde.logreader.menu.actions;

import java.io.Serializable;

import dk.majkilde.logreader.menu.IMenu;
import dk.majkilde.logreader.source.XMLFileList;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class XMLAction implements IAction, Serializable {
	private static final long serialVersionUID = 1L;
	private final String filename = "";
	private final IMenu parent;

	private XMLFileList filelist = null;

	public XMLAction(XML config, IMenu parent) {
		this.parent = parent;
		filelist = new XMLFileList(config);
	}

	public IMenu getParent() {
		return parent;
	}

	public String getType() {
		return "link";
	}

	public String getFilename() {
		return filename;
	}

	public void execute() {
		XSPUtils.getViewScope().put("filelist", filelist);
		XSPUtils.getViewScope().put("currentFile", filelist.getCurrent());
		XSPUtils.getViewScope().put("currentMenu", getParent());
	}

	public boolean isValid() {
		try {
			return filelist.getCount() > 0;
		} catch (Exception e) {
			return false;
		}
	}

}
