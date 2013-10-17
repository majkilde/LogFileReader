package test.dk.majkilde.logreader;

import java.io.InputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import dk.majkilde.logreader.Config;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class ConfigTest {

	private final Logger log = LogManager.getLogger();

	@Test
	public void readConfigFile() throws Exception {
		//		File notesini = NotesEnvironment.getConfigFile();
		//		List<String> content = TextReader.read(notesini);

		try {
			Config config = new Config();

			InputStream input = XSPUtils.getResourceAsStream("config.xml");
			XML xml = new XML(input, "config");
			XML setupfiles = xml.child("setupfiles");
			for (XML file : setupfiles.children("file")) {
				String type = file.string("type");
				String title = file.child("title").content();
				String filename = file.child("filename").content();

				log.info("CONFIG FILE: {0}: {1} | {2}", type, title, filename);

				List<String> files = config.getFiles(filename);
				log.info("Files: {0}", files);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		}

		Assert.assertTrue(true);
	}
}
