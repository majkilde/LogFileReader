package dk.majkilde.logreader.files;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.Download;
import dk.xpages.utils.NotesObjects;
import dk.xpages.utils.NotesStrings;

public class NSFFile implements IFile, Serializable {

	private final Logger log = LogManager.getLogger();
	private final String noteid;
	private Date date = null;
	private String filename = "";
	private long size = 0;

	ArrayList<String> includes = null;

	public NSFFile(final String filename, final Document doc) throws NotesException {
		this.noteid = doc.getNoteID();
		this.filename = filename;

		if (doc != null) {
			date = doc.getCreated().toJavaDate();
			size = doc.getSize();
		}
	}

	private static final long serialVersionUID = 1L;

	public Date getDate() {
		return date;
	}

	public String getFilename() {
		return noteid + ".txt";
	}

	@SuppressWarnings("unchecked")
	private List<String> getContent() {
		Database db = null;
		Document doc = null;
		Item item = null;

		try {
			db = NotesObjects.getDatabaseAsSigner(filename);
			doc = NotesObjects.getDocumentById(db, noteid);

			if (doc != null) {
				item = doc.getFirstItem("EventList");
				return new ArrayList<String>(item.getValues());
			}
			return null;

		} catch (NotesException e) {
			log.error(e);
			return null;
		} finally {
			NotesObjects.incinerate(item, doc, db);
		}

	}

	public String getHtml() {
		return NotesStrings.join("<br/>", getContent());
	}

	public long getSize() {
		return size;
	}

	public int compareTo(IFile compareFile) {
		return (-1) * this.getDate().compareTo(compareFile.getDate());
	}

	public String getPath() {
		return filename;
	}

	public void download() {
		Download download = new Download();
		try {
			byte[] data = NotesStrings.join("\n", getContent()).getBytes();

			download.save(data, getFilename());
		} catch (IOException e) {
			log.error(e);
		}

	}
}
