package dk.majkilde.logreader.files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.XSPUtils;

public class XMLFile extends TextFile implements Serializable {
	private final String ENCODING = "utf-8";
	private final Logger log = LogManager.getLogger();
	private String xlsFilename = null;
	private static final long serialVersionUID = 1L;

	public XMLFile(final File file, final String xlsFilename) {
		super(file);
		this.xlsFilename = xlsFilename;
	}

	public XMLFile(final String filename, final String xlsFilename) {
		super(filename);
		this.xlsFilename = xlsFilename;
	}

	@Override
	public String getHtml() {

		if (xlsFilename == null) {
			//no XSL file specified - just return the content as a plain text file
			return super.getHtml();
		}

		if (file.length() == 0) {
			return "Empty file (size=0)";
		}

		// XSL transform
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			// Input XML
			//			StreamSource inputStream = new StreamSource(new BufferedReader(new FileReader(file)));
			StreamSource inputStream = new StreamSource(file);

			// Transformer XLS 
			transformer = tFactory.newTransformer(new StreamSource(XSPUtils.getResourceAsStream(xlsFilename)));

			// Output stream
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(output);

			// Do the transformation
			transformer.transform(inputStream, result);

			return output.toString(ENCODING);
		} catch (Exception e) {
			log.error(e);
		}

		return null;
	}

}
