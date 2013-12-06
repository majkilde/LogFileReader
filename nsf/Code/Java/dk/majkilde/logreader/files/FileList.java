package dk.majkilde.logreader.files;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XSPUtils;

public class FileList implements Serializable {
	private static final long serialVersionUID = 1L;
	private final Logger log = LogManager.getLogger();

	private IFile current = null;

	private final List<IFile> files;
	private final String pattern;

	public FileList(List<IFile> files, String pattern) {
		this.files = files;
		this.pattern = pattern;
	}

	public String getPattern() {
		return Directory.getCleanPattern(pattern);
	}

	public String getPath() {
		return NotesStrings.strLeftBack(getPattern(), File.separator);
	}

	public IFile getNewest() {
		if (files.size() > 0) {
			return files.get(0);
		} else {
			return null;
		}
	}

	public List<IFile> getFiles() {
		log.trace("Trace: getFiles({0})", pattern);
		if (files.size() == 0)
			return null;
		return files;
	}

	public int getCount() {
		return files.size();
	}

	public void setCurrent(IFile current) {
		this.current = current;
		XSPUtils.getViewScope().put("currentFile", current);
	}

	public IFile getCurrent() {
		if (current == null) {
			current = getNewest();
		}
		return current;
	}

}
