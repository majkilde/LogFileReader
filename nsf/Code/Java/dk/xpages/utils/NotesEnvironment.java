package dk.xpages.utils;

import java.io.File;

import lotus.domino.NotesException;
import lotus.domino.Session;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;

public class NotesEnvironment {

	private static Logger log = LogManager.getLogger();

	/**
	 * @return the value of the notes.ini parameter
	 */
	public static String getEnvironmentString(String paramname, Boolean system) {
		Session session = XSPUtils.getCurrentSession();
		String value = "";
		try {
			value = session.getEnvironmentString(paramname, system);
		} catch (NotesException e) {
			log.error(e);
		}
		return value;
	}

	public static String getDataFolder() {
		String filename = "";
		filename = getEnvironmentString("Directory", true);
		filename = filename.replace("\\", "/");
		if (!filename.endsWith("/")) {
			filename += "/";
		}
		return filename;
	}

	public static String getProgramFolder() {
		String filename = "";
		filename = getEnvironmentString("NotesProgram", true);
		filename = filename.replace("\\", "/");
		if (!filename.endsWith("/"))
			filename += "/";
		return filename;
	}

	/**
	 * 
	 * @return the path & filename for the notes.ini file. As @ConfigFile()
	 */
	public static String getConfigFilename() {
		return NotesUtils.evaluateString("@ConfigFile");
	}

	/**
	 * 
	 * @return The default temporary-file directory, typically c:\temp on windows
	 */
	public static String getTempFolder() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 
	 * @return a File hande to notes.ini
	 */
	public static File getConfigFile() {
		String filename = getConfigFilename();
		log.debug("Config file: {0}", filename);
		File file = new File(filename);
		return file;

	}
}
