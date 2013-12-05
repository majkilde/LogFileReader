package dk.majkilde.logreader.menu;

import java.io.Serializable;

import dk.majkilde.logreader.source.TextFileList;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class TextReaderAction implements IAction, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String filename = "";
	private final IMenu parent;

	private TextFileList filelist = null;

	public TextReaderAction(XML config, IMenu parent) {
		this.parent = parent;
		filelist = new TextFileList(config);
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
