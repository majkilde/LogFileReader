package dk.majkilde.logreader.files;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesEnvironment;
import dk.xpages.utils.NotesStrings;

public class Directory {
	public static final String NOTES_PROGRAM = "[program]";
	public static final String NOTES_DATA = "[data]";
	public static final String NOTES_CONFIG = "[config]";

	private static final Logger log = LogManager.getLogger();

	public static List<String> getFileNames(final String filePattern) {

		String pattern = getCleanPattern(filePattern);
		String folder = NotesStrings.strLeftBack(pattern, File.separator) + File.separator;
		String filename = NotesStrings.strRightBack(pattern, File.separator);

		return getFiles(folder, filename);
	}

	public static String getCleanPattern(final String filePattern) {
		String pattern = filePattern;

		if (NotesStrings.startsWithIgnoreCase(pattern, NOTES_PROGRAM)) {
			pattern = NotesEnvironment.getProgramFolder() + pattern.substring(NOTES_PROGRAM.length() + 1);
		}
		if (NotesStrings.startsWithIgnoreCase(pattern, NOTES_DATA)) {
			pattern = NotesEnvironment.getDataFolder() + pattern.substring(NOTES_DATA.length() + 1);
		}
		if (NotesStrings.startsWithIgnoreCase(pattern, NOTES_CONFIG)) {
			pattern = NotesEnvironment.getConfigFile().getParent() + File.separator + pattern.substring(NOTES_CONFIG.length() + 1);
		}
		pattern = NotesStrings.replace(pattern, "\\", File.separator);
		pattern = NotesStrings.replace(pattern, "/", File.separator);

		return pattern;
	}

	/**
	 * 
	 * @param folder
	 * @param patterns 	null will return all files in the folder. Otherwise use a pattern, like *.txt to return all text files
	 * @return
	 */
	private static ArrayList<String> getFiles(String folder, String pattern) {
		ArrayList<String> files = new ArrayList<String>();

		File dir = new File(folder);
		String[] entries = dir.list(); // get the folder content
		if (entries == null) {
			log.warning("Not a folder: {0}", folder);
			return files;
		}
		// convert to ArrayList and remove all directories from the list

		for (int i = 0; i < entries.length; i++) {
			File d = new File(folder + entries[i]);
			if (d.isFile()) {
				if (pattern == null) {
					files.add(folder + entries[i]);
				} else {
					if (wildCardMatch(entries[i], pattern)) {
						files.add(folder + entries[i]);
					}
				}
			}
		}

		// sort the list
		Collections.sort(files);

		return files;
	}

	/**
	 * Performs a wildcard matching for the text and pattern provided.
	 * 
	 * @param text
	 *            the text to be tested for matches.
	 * 
	 * @param pattern
	 *            the pattern to be matched for. This can contain the wildcard
	 *            character '*' (asterisk).
	 * 
	 * @return <tt>true</tt> if a match is found, <tt>false</tt> otherwise.
	 * 
	 * @see http://www.adarshr.com/papers/wildcard
	 */
	public static boolean wildCardMatch(String text, String pattern) {
		// Create the cards by splitting using a RegEx. If more speed
		// is desired, a simpler character based splitting can be done.
		if (pattern == null)
			return true;

		String[] cards = pattern.split("\\*");

		// Iterate over the cards.
		for (String card : cards) {
			int idx = text.indexOf(card);

			// Card not detected in the text.
			if (idx == -1) {
				return false;
			}

			// Move ahead, towards the right of the text.
			text = text.substring(idx + card.length());
		}

		return true;
	}

}
