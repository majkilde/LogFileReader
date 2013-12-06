package dk.majkilde.logreader.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.io.FileUtils;

import dk.majkilde.logreader.files.Directory;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.Download;

public class ExtBean implements Serializable {

	private static Logger log = LogManager.getLogger();

	private static final long serialVersionUID = 1L;

	public ExtBean() {
	}

	public List<File> getFileList() {
		return Directory.getFiles("[config]\\jvm\\lib\\ext\\*.jar");
	}

	private byte[] getByteArray(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}

	public void download(File file) {
		Download download = new Download();
		try {
			download.save(getByteArray(file), file.getAbsolutePath());
		} catch (IOException e) {
			log.error(e);
		}

	}

}
