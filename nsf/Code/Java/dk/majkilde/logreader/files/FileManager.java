package dk.majkilde.logreader.files;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class FileManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Map<String, FileList> filelists = new TreeMap<String, FileList>();

	private final Logger log = LogManager.getLogger();

	public FileManager(final String configSection) {
		loadConfig(configSection);
	}

	/**
	 * 
	 * @param configSection	logfiles or setupfiles
	 */
	public void loadConfig(final String configSection) {

		try {
			//	Config config = new Config();

			InputStream input = XSPUtils.getResourceAsStream("config.xml");
			XML xml = new XML(input, "config");
			XML setupfiles = xml.child(configSection);
			for (XML file : setupfiles.children("file")) {
				//	String type = file.string("type");
				String title = file.child("title").content();
				String filename = file.child("filename").content();

				FileList filelist = new FileList(title, filename);
				filelists.put(title, filelist);

			}

		} catch (Exception e) {
			log.error(e);
		}

	}

	public List<String> getListNames() {
		return new ArrayList<String>(filelists.keySet());
	}

	public FileList getFilelist(String listname) {
		if (filelists.containsKey(listname)) {
			return filelists.get(listname);
		} else {
			return null;
		}
	}

	public Collection<FileList> getFilelists() {
		return filelists.values();
	}
}
