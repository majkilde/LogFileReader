package dk.majkilde.logreader.files;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;

public class FileList implements Serializable {

	private final Logger log = LogManager.getLogger();

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
}
