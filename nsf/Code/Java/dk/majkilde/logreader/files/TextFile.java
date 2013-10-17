package dk.majkilde.logreader.files;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class TextFile implements IFile, Serializable {
	private Date date;

	private final File file;

	public TextFile(final File file) {
		this.file = file;
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
		// TODO Auto-generated method stub
		return null;
	}

	public long getSize() {
		return file.length();
	}

}
