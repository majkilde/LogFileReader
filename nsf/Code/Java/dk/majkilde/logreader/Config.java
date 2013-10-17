package dk.majkilde.logreader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.xpages.utils.NotesEnvironment;
import dk.xpages.utils.NotesStrings;

public class Config {

	private String cleanupPattern(final String filePattern) {
		String pattern = filePattern;

		if (NotesStrings.startsWithIgnoreCase(pattern, "[program]")) {
			pattern = NotesEnvironment.getProgramFolder() + pattern.substring("[program]".length() + 1);
		}
		if (NotesStrings.startsWithIgnoreCase(pattern, "[data]")) {
			pattern = NotesEnvironment.getDataFolder() + pattern.substring("[data]".length() + 1);
		}
		if (NotesStrings.startsWithIgnoreCase(pattern, "[config]")) {
			pattern = NotesEnvironment.getConfigFile().getParent() + File.separator + pattern.substring("[config]".length() + 1);
		}
		pattern = NotesStrings.replace(pattern, "\\", File.separator);
		pattern = NotesStrings.replace(pattern, "/", File.separator);

		return pattern;
	}

	public List<String> getFiles(final String filePattern) {

		String pattern = cleanupPattern(filePattern);
		String folder = NotesStrings.strLeftBack(pattern, File.separator) + File.separator;
		String filename = NotesStrings.strRightBack(pattern, File.separator);

		return getFiles(folder, filename);
	}

	/**
	 * 
	 * @param folder
	 * @param patterns 	null will return all files in the folder. Otherwise use a pattern, like *.txt to return all text files
	 * @return
	 */
	private ArrayList<String> getFiles(String folder, String pattern) {
		File dir = new File(folder);
		String[] entries = dir.list(); // get the folder content

		// convert to ArrayList and remove all directories from the list
		ArrayList<String> files = new ArrayList<String>();
		for (int i = 0; i < entries.length; i++) {
			File d = new File(folder + entries[i]);
			if (d.isFile()) {
				if (pattern == null) {
					files.add(entries[i]);
				} else {
					if (wildCardMatch(entries[i], pattern)) {
						files.add(entries[i]);
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
