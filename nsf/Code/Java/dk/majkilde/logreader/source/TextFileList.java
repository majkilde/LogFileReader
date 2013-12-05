package dk.majkilde.logreader.source;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.majkilde.logreader.files.Directory;
import dk.majkilde.logreader.files.Filters;
import dk.majkilde.logreader.files.IFile;
import dk.majkilde.logreader.files.TextFile;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class TextFileList implements Serializable, IFileList {

	private final Logger log = LogManager.getLogger();
	private IFile current = null;
	//	private InputStream xlsTransformer = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pattern = "";

	private final List<IFile> files = new ArrayList<IFile>();

	public TextFileList(XML config) {
		this.pattern = config.child("filename").content();

		Filters filters = new Filters(config.child("filters"));

		List<String> filenames = Directory.getFileNames(pattern);
		for (String filename : filenames) {
			files.add(new TextFile(filename, filters));
		}

		Collections.sort(files);

	}

	/* (non-Javadoc)
	 * @see dk.majkilde.logreader.files.IFileList#getPattern()
	 */
	public String getPattern() {
		return Directory.getCleanPattern(pattern);
	}

	/* (non-Javadoc)
	 * @see dk.majkilde.logreader.files.IFileList#getPath()
	 */
	public String getPath() {
		return NotesStrings.strLeftBack(getPattern(), File.separator);
	}

	/* (non-Javadoc)
	 * @see dk.majkilde.logreader.files.IFileList#getNewest()
	 */
	public IFile getNewest() {
		if (files.size() > 0) {
			return files.get(0);
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see dk.majkilde.logreader.files.IFileList#getFiles()
	 */
	public List<IFile> getFiles() {
		log.trace("Trace: getFiles({0})", pattern);
		if (files.size() == 0)
			return null;
		return files;
	}

	/* (non-Javadoc)
	 * @see dk.majkilde.logreader.files.IFileList#getCount()
	 */
	public int getCount() {
		return files.size();
	}

	/* (non-Javadoc)
	 * @see dk.majkilde.logreader.files.IFileList#setCurrent(dk.majkilde.logreader.files.IFile)
	 */
	public void setCurrent(IFile current) {
		this.current = current;
		XSPUtils.getViewScope().put("currentFile", current);
	}

	/* (non-Javadoc)
	 * @see dk.majkilde.logreader.files.IFileList#getCurrent()
	 */
	public IFile getCurrent() {
		if (current == null) {
			current = getNewest();
		}
		return current;
	}

}
