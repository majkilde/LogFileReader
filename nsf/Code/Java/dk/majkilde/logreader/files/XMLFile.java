package dk.majkilde.logreader.files;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Serializable;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;

public class XMLFile extends TextFile implements Serializable {
	private final Logger log = LogManager.getLogger();
	private InputStream xlsFile = null;
	private static final long serialVersionUID = 1L;

	public XMLFile(final File file, final InputStream xlsFile) {
		super(file);
		this.xlsFile = xlsFile;
	}

	public XMLFile(final String filename, final InputStream xlsFile) {
		super(filename);
		this.xlsFile = xlsFile;
	}

	@Override
	public String getHtml() {

		if (xlsFile == null) {
			//no XSL file specified - just return the content as a plain text file
			return super.getHtml();
		}

		// XSL transform
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			// Input XML
			StreamSource inputStream = new StreamSource(new BufferedReader(new FileReader(file)));

			// Transformer XLS 
			transformer = tFactory.newTransformer(new StreamSource(xlsFile));

			// Output stream
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(output);

			// Do the transformation
			transformer.transform(inputStream, result);

			return output.toString("utf-8");
		} catch (Exception e) {
			log.error(e);
		}

		return null;
	}

}
