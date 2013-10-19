package dk.majkilde.logreader.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import dk.majkilde.logreader.files.FileManager;
import dk.majkilde.logreader.source.IFileList;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XSPUtils;

public class ConfigFilesBean implements Serializable {
	private final Logger log = LogManager.getLogger();
	private final FileManager manager = new FileManager("configfiles");

	//	private IFile currentFile = null;

	public ConfigFilesBean() {
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

	public IFileList getFilelist(String listname) {
		return manager.getFilelist(listname);
	}

	public static String readableFileSize(long size) {
		return NotesStrings.readableFileSize(size);
	}

	public Collection<IFileList> getFilelists() {
		return manager.getFilelists();
	}

	public IFileList getFirstFilelist() {
		return getFilelists().iterator().next();
	}

	public IFileList getCurrentFilelist() {
		String menu = XSPUtils.getUrlParameter("menu");
		IFileList result = manager.getFilelist(menu);

		if (result == null) {
			return getFirstFilelist();
		} else {
			return result;
		}
	}

}
