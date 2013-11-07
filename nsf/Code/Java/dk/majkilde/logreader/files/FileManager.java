package dk.majkilde.logreader.files;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lotus.domino.NotesException;
import lotus.notes.addins.DominoServer;
import lotus.notes.addins.ServerAccess;
import dk.majkilde.logreader.source.IFileList;
import dk.majkilde.logreader.source.NSFFileList;
import dk.majkilde.logreader.source.TextFileList;
import dk.majkilde.logreader.source.XMLFileList;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class FileManager implements Serializable {
	/**
	 *  
	 */

	private static final long serialVersionUID = 1L;

	private final Map<String, IFileList> filelists = new LinkedHashMap<String, IFileList>();

	private static final Logger log = LogManager.getLogger();

	public FileManager(final String configSection) {
		loadConfig(configSection);
	}

	public static boolean canReadLogs(final lotus.domino.Session session) {
		boolean result = false;
		try {
			String username = session.getEffectiveUserName();
			DominoServer server = new DominoServer();
			result = server.checkServerAccess(username, ServerAccess.PROG_UNRESTRICTED);
			result = result || server.checkServerAccess(username, ServerAccess.VIEW_ONLY_ADMIN);
		} catch (NotesException ne) {
			log.error(ne);
		}
		return result;
	}

	/**
	 * 
	 * @param configSection	logfiles or setupfiles
	 */
	public void loadConfig(final String configSection) {

		try {
			//	Config config = new Config();
			log.info("Loading config: {0}", configSection);
			InputStream input = XSPUtils.getResourceAsStream("config.xml");
			XML xml = new XML(input, "config");
			XML setupfiles = xml.child(configSection);
			for (XML file : setupfiles.children("file")) {

				String type = file.string("type");
				String title = file.child("title").content();
				if (NotesStrings.equalsIgnoreCase(type, "TXT")) {
					String filename = file.child("filename").content();

					XML filters = file.child("filters");
					ArrayList<String> includes = new ArrayList<String>();
					if (filters != null) {
						for (XML include : filters.children("include")) {
							includes.add(include.content());
						}
					}

					TextFileList textfilelist = new TextFileList(title, filename, includes);

					filelists.put(title, textfilelist);

				} else if (NotesStrings.equalsIgnoreCase(type, "XML")) {
					String filename = file.child("filename").content();
					String transformer = file.child("transform").content();

					XMLFileList xmlfilelist = new XMLFileList(title, filename, transformer);

					filelists.put(title, xmlfilelist);

				} else if (NotesStrings.equalsIgnoreCase(type, "NSF")) {
					String filename = file.child("filename").content();
					String viewname = file.child("viewname").content();
					String dateFormula = file.child("date-formula").content();
					String bodyField = file.child("body-field").content();

					NSFFileList nsffilelist = new NSFFileList(title, filename, viewname, dateFormula, bodyField);

					filelists.put(title, nsffilelist);

				} else {
					log.warning("Unsupported file type: {0}", type);
				}
			}

		} catch (Exception e) {
			log.error(e);
		}

	}

	public List<String> getListNames() {
		return new ArrayList<String>(filelists.keySet());
	}

	public IFileList getFilelist(String listname) {
		if (filelists.containsKey(listname)) {
			return filelists.get(listname);
		} else {
			return null;
		}
	}

	public Collection<IFileList> getFilelists() {
		return filelists.values();
	}
}
