package dk.majkilde.logreader.files;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;

public class FileList implements Serializable {

	private final Logger log = LogManager.getLogger();
	private IFile current = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pattern = "";
	private String title = "";

	private final List<IFile> files = new ArrayList<IFile>();

	public FileList(final String title, final String pattern) {
		this.title = title;
		this.pattern = pattern;

		List<String> filenames = Directory.getFileNames(pattern);
		for (String filename : filenames) {
			files.add(new TextFile(filename));
		}

		Collections.sort(files);
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

	public String getTitle() {
		return title;
	}

	public int getCount() {
		return files.size();
	}

	public void setCurrent(IFile current) {
		this.current = current;
	}

	public IFile getCurrent() {
		if (current == null) {
			current = getNewest();
		}
		return current;
	}
}
