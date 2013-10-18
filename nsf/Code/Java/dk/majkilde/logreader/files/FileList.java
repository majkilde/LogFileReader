package dk.majkilde.logreader.files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dk.xpages.utils.NotesStrings;

public class FileList {
	private String pattern = "";
	private String title = "";

	private final List<IFile> files = new ArrayList<IFile>();

	public FileList(final String title, final String pattern) {
		this.title = title;
		this.pattern = pattern;

		List<String> filenames = Directory.getFileNames(pattern);
		for (String filename : filenames) {
			files.add(new TextFile(filename));
		}

		Collections.sort(files);
	}

	public IFile getNewest() {
		if (files.size() > 0) {
			return files.get(0);
		} else {
			return null;
		}
	}

	public String getTitle() {
		return NotesStrings.messageFormat("{0} ({1})", title.toUpperCase(), pattern.toLowerCase());
	}
}
