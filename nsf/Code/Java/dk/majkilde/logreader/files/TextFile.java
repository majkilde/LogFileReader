package dk.majkilde.logreader.files;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;

public class TextFile implements IFile, Serializable {

	private final Logger log = LogManager.getLogger();
	private final File file;

	public TextFile(final File file) {
		this.file = file;
	}

	public TextFile(final String filename) {
		this.file = new File(filename);
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
			content = TextReader.read(file);
		} catch (IOException e) {
			log.error(e);
			return NotesStrings.messageFormat("Error reading file '{1}': {0}", e.getMessage(), file.getName());
		}

		return NotesStrings.join("<br/>", content);
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

}
