package dk.majkilde.logreader.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;

public class TextReader {
	private static Logger log = LogManager.getLogger();

	public static List<String> read(File file) throws IOException {
		return read(file, null);
	}

	public static List<String> read(File file, IFilter filter) throws IOException {
		List<String> result = new ArrayList<String>(100);

		StopWatch watch = new StopWatch();
		watch.start();

		BufferedReader reader = new BufferedReader(new FileReader(file));
		for (String line; (line = reader.readLine()) != null;) {
			if (filter == null) {
				result.add(line);
			} else {
				if (filter.included(line)) {
					result.add(line);
				}
			}
		}

		log.debug("File '{0}' read in {1}ms", file.getName(), watch.getTime());

		return result;
	}

}
