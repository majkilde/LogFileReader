package dk.majkilde.logreader.source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.View;
import dk.majkilde.logreader.files.IFile;
import dk.majkilde.logreader.files.NSFFile;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesObjects;

public class NSFFileList implements IFileList, Serializable {

	private final Logger log = LogManager.getLogger();
	private IFile current = null;
	private String title = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<IFile> files = new ArrayList<IFile>();

	public NSFFileList(String title, String filename, String viewname, String dateFormula, String bodyField) {
		this.title = title;

		Database db = null;
		View view = null;
		Document doc = null;
		Document nextdoc = null;

		try {
			db = NotesObjects.getDatabaseAsSigner(filename);

			view = NotesObjects.getView(db, viewname);
			view.setAutoUpdate(false);
			doc = view.getFirstDocument();
			while (doc != null) {

				IFile nsffile = new NSFFile(filename, doc);
				files.add(nsffile);

				nextdoc = view.getNextDocument(doc);
				NotesObjects.incinerate(doc);
				doc = nextdoc;
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			NotesObjects.incinerate(doc, view, db);
		}

		Collections.sort(files);
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
		if (files.size() == 0)
			return null;
		return files;
	}

	public int getCount() {
		return files.size();
	}

	public IFile getCurrent() {
		if (current == null) {
			current = getNewest();
		}
		return current;
	}

	public String getPath() {
		return "path";
	}

	public String getPattern() {
		return "";
	}

	public String getTitle() {
		return title;
	}

	public void setCurrent(IFile current) {
		this.current = current;

	}

}
