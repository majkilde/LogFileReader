package dk.majkilde.logreader.source;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.View;
import dk.majkilde.logreader.files.Filters;
import dk.majkilde.logreader.files.IFile;
import dk.majkilde.logreader.files.NSFFile;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesObjects;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class NSFFileList implements IFileList, Serializable {

	private final Logger log = LogManager.getLogger();
	private IFile current = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<IFile> files = new ArrayList<IFile>();

	public NSFFileList(XML config) {
		String filename = config.child("filename").content();
		String viewname = config.child("viewname").content();
		String dateFormula = config.child("date-formula").content();
		String bodyField = config.child("body-field").content();

		Filters filters = new Filters(config.child("filters"));

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

				IFile nsffile = new NSFFile(filename, doc, filters);
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

	public String getPattern() {
		return "";
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
