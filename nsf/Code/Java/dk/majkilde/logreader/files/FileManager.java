package dk.majkilde.logreader.files;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class FileManager {
	private final Map<String, FileList> filelists = new HashMap<String, FileList>();

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
}
