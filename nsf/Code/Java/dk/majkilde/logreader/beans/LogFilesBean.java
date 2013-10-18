package dk.majkilde.logreader.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import dk.majkilde.logreader.files.FileList;
import dk.majkilde.logreader.files.FileManager;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XSPUtils;

public class LogFilesBean implements Serializable {
	private final Logger log = LogManager.getLogger();
	private final FileManager manager = new FileManager("logfiles");

	//	private IFile currentFile = null;

	public LogFilesBean() {
		log.info("Bean initialized: {0}", LogFilesBean.class.getName());
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List<String> getListNames() {
		log.trace("Trace: GetListNames()");
		return manager.getListNames();
	}

	public FileList getFilelist(String listname) {
		return manager.getFilelist(listname);
	}

	public static String readableFileSize(long size) {
		return NotesStrings.readableFileSize(size);
	}

	public Collection<FileList> getFilelists() {
		return manager.getFilelists();
	}

	public FileList getFirstFilelist() {
		return getFilelists().iterator().next();
	}

	public FileList getCurrentFilelist() {
		String menu = XSPUtils.getUrlParameter("menu");
		FileList result = manager.getFilelist(menu);

		if (result == null) {
			return getFirstFilelist();
		} else {
			return result;
		}
	}

	//	public void setCurrentFile(IFile file) {
	//		this.currentFile = file;
	//	}
	//
	//	public IFile getCurrentFile() {
	//		if (currentFile == null) {
	//			currentFile = getCurrentFilelist().getNewest();
	//		}
	//		return currentFile;
	//	}
}
