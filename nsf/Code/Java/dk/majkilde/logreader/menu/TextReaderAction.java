package dk.majkilde.logreader.menu;

import dk.majkilde.logreader.source.TextFileList;
import dk.xpages.utils.XSPUtils;

public class TextReaderAction implements IAction {
	private String filename = "";
	private final IMenu parent;

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
		return this;
	}

	public String getFilename() {
		return filename;
	}

	public void execute() {

		TextFileList filelist = new TextFileList("textreader", getFilename(), null);
		XSPUtils.getViewScope().put("filelist", filelist);
		XSPUtils.getViewScope().put("currentFile", filelist.getCurrent());
		XSPUtils.getViewScope().put("currentMenu", getParent());

		/*
		 * <xp:this.beforePageLoad><![CDATA[#{javascript:viewScope.filelist = configFiles.getCurrentFilelist();
		viewScope.currentFile = filelist.getCurrent();}]]></xp:this.beforePageLoad>
		<h1>
		 */

	}

}
