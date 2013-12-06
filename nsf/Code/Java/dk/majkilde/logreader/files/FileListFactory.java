package dk.majkilde.logreader.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.View;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesObjects;
import dk.xpages.utils.XML;

public class FileListFactory {

	private static Logger log = LogManager.getLogger();

	static public FileList create(XML config) {

		String filetype = config.string("filetype");

		if (filetype.equalsIgnoreCase("text")) {
			return getTextFiles(config);
		}

		if (filetype.equalsIgnoreCase("xml")) {
			return getXMLFiles(config);
		}

		if (filetype.equalsIgnoreCase("log")) {
			return getLogFiles(config);
		}

		return null;
	}

	private static Filters getFilters(XML config) {
		return new Filters(config.child("filters"));
	}

	private static FileList getTextFiles(XML config) {
		String pattern = config.child("filename").content();
		List<String> filenames = Directory.getFileNames(pattern);

		String encoding = config.child("filename").string("encoding");
		Filters filters = getFilters(config);

		List<IFile> files = new ArrayList<IFile>();
		for (String filename : filenames) {
			files.add(new TextFile(filename, filters).setEncoding(encoding));
		}

		Collections.sort(files);

		return new FileList(files, pattern);
	}

	private static FileList getXMLFiles(XML config) {
		String pattern = config.child("filename").content();
		String xlsFilename = config.child("transform").content();

		List<String> filenames = Directory.getFileNames(pattern);

		String encoding = config.child("filename").string("encoding");

		List<IFile> files = new ArrayList<IFile>();
		for (String filename : filenames) {
			files.add(new XMLFile(filename, xlsFilename).setEncoding(encoding));
		}

		Collections.sort(files);

		return new FileList(files, pattern);
	}

	private static FileList getLogFiles(XML config) {
		String filename = config.child("filename").content();
		String viewname = config.child("viewname").content();

		Filters filters = getFilters(config);

		Database db = null;
		View view = null;
		Document doc = null;
		Document nextdoc = null;

		List<IFile> files = new ArrayList<IFile>();

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

		return new FileList(files, filename);
	}
}
