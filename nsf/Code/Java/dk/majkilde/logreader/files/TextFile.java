package dk.majkilde.logreader.files;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.Download;
import dk.xpages.utils.NotesStrings;

public class TextFile implements IFile, Serializable {
	private final String ENCODING = "utf-8";
	private final Logger log = LogManager.getLogger();
	protected final File file;
	ArrayList<String> includes = new ArrayList<String>();

	public TextFile(final File file) {
		this.file = file;
	}

	protected TextFile(final String filename) {
		this.file = new File(filename);
	}

	public TextFile(final String filename, final ArrayList<String> includes) {
		this.file = new File(filename);
		this.includes = includes;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Date getDate() {
		return new Date(file.lastModified());
	}

	public String getFilename() {
		return file.getName();
	}

	public String getHtml() {
		List<String> content;
		try {
			content = getStringList(); // TextReader.read(file);
			filterContent(content);
		} catch (IOException e) {
			log.error(e);
			return NotesStrings.messageFormat("Error reading file '{1}': {0}", e.getMessage(), file.getName());
		}

		return NotesStrings.join("<br/>", content);
	}

	private boolean isLineIncluded(String line) {
		//check every entry in the filter
		for (String entry : includes) {
			if (NotesStrings.containsIgnoreCase(line, entry)) {
				return true;
			}
		}
		return false;
	}

	private void filterContent(List<String> content) {
		if (includes == null || includes.size() == 0) {
			return; //no filters
		}

		//check every line in the file
		for (Iterator<String> itr = content.iterator(); itr.hasNext();) {
			String line = itr.next();

			if (!isLineIncluded(line)) {
				itr.remove();
			}
		}
	}

	public long getSize() {
		return file.length();
	}

	public int compareTo(IFile compareFile) {
		return (-1) * this.getDate().compareTo(compareFile.getDate());
	}

	public String getPath() {
		return file.getPath();
	}

	private byte[] getByteArray() throws IOException {
		return FileUtils.readFileToByteArray(file);
	}

	private List<String> getStringList() throws IOException {
		return FileUtils.readLines(file, ENCODING);
	}

	public void download() {
		Download download = new Download();
		download.setCharset(ENCODING);
		try {
			download.save(getByteArray(), getFilename());
		} catch (IOException e) {
			log.error(e);
		}

	}
}
