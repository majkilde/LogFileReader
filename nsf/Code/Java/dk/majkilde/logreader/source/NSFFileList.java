package dk.majkilde.logreader.source;

import java.io.Serializable;
import java.util.List;

import dk.majkilde.logreader.files.IFile;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;

public class NSFFileList implements IFileList, Serializable {

	private final Logger log = LogManager.getLogger();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NSFFileList(String filename) {

	}

	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public IFile getCurrent() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IFile> getFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public IFile getNewest() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPattern() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setCurrent(IFile current) {
		// TODO Auto-generated method stub

	}

}
