package dk.majkilde.logreader.files;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.Download;
import dk.xpages.utils.NotesStrings;

public class TextFile implements IFile, Serializable {
	private static final String DEFAULT_ENCODING = "utf-8";
	private String encoding = DEFAULT_ENCODING;
	private final Logger log = LogManager.getLogger();
	protected final File file;
	private Filters filters;

	public TextFile(final File file) {
		this.file = file;
	}

	protected TextFile(final String filename) {
		this(filename, new Filters(null));
	}

	public TextFile(final String filename, final Filters filters) {
		this.file = new File(filename);
		this.filters = filters;
	}

	public TextFile setEncoding(String encoding) {
		if (NotesStrings.isBlank(encoding)) {
			this.encoding = DEFAULT_ENCODING;
		} else {
			this.encoding = encoding;
		}

		return this;
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
			content = getStringList();
		} catch (IOException e) {
			log.error(e);
			return NotesStrings.messageFormat("Error reading file '{1}': {0}", e.getMessage(), file.getName());
		}

		filters.apply(content);

		return NotesStrings.join("", content);
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
		return FileUtils.readLines(file, encoding);
	}

	public void download() {
		Download download = new Download();
		download.setCharset(encoding);
		try {
			download.save(getByteArray(), getFilename());
		} catch (IOException e) {
			log.error(e);
		}

	}

	public Filters getFilters() {
		return filters;
	}
}
