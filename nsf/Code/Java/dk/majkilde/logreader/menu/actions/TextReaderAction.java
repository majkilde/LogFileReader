package dk.majkilde.logreader.menu.actions;

import dk.majkilde.logreader.menu.IMenu;
import dk.majkilde.logreader.source.TextFileList;
import dk.xpages.utils.XSPUtils;

public class TextReaderAction extends Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filename = "";
	private final TextFileList filelist = null;

	public TextReaderAction(IMenu parent) {
		super(parent);
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

	@Override
	protected void executeAction() {
		XSPUtils.getViewScope().put("filelist", filelist);
		XSPUtils.getViewScope().put("currentFile", filelist.getCurrent());
	}

	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
