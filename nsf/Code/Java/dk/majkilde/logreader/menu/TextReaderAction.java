package dk.majkilde.logreader.menu;

import java.io.Serializable;

import dk.majkilde.logreader.source.TextFileList;
import dk.xpages.utils.XSPUtils;

public class TextReaderAction implements IAction, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filename = "";
	private final IMenu parent;

	private TextFileList filelist = null;

	public TextReaderAction(IMenu parent) {
		this.parent = parent;
	}

	public IMenu getParent() {
		return parent;
	}

	public String getType() {
		return "link";
	}

	public TextReaderAction setFilename(String filename) {
		this.filename = filename;
		filelist = new TextFileList("textreader", filename, null);
		return this;
	}

	public String getFilename() {
		return filename;
	}

	public void execute() {

		XSPUtils.getViewScope().put("filelist", filelist);
		XSPUtils.getViewScope().put("currentFile", filelist.getCurrent());
		XSPUtils.getViewScope().put("currentMenu", getParent());

		/*
		 * <xp:this.beforePageLoad><![CDATA[#{javascript:viewScope.filelist = configFiles.getCurrentFilelist();
		viewScope.currentFile = filelist.getCurrent();}]]></xp:this.beforePageLoad>
		<h1>
		 */

	}

	public boolean isValid() {
		try {
			return filelist.getCount() > 0;
		} catch (Exception e) {
			return false;
		}
	}

}
