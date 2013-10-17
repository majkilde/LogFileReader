package majkilde.log;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections; //xml/xsl transform
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import lotus.domino.NotesException;
import lotus.domino.Session;
import lotus.domino.Database;
import lotus.domino.View;
import lotus.domino.Document;

import com.debug.*; // XPages Debug toolbar
import com.ibm.domino.xsp.module.nsf.NotesContext;


public class Reader implements Serializable {
	private String version="1.2";
	private static final long serialVersionUID = 1L;
	private DebugToolbar debug = null;

	// Constructor
	public Reader() {
		debug = new DebugToolbar(); // initialize the debug toolbar
	}

	// Often used functions
	private Session getCurrentSession() {
		NotesContext nc = NotesContext.getCurrentUnchecked();
		// return (null != nc) ? nc.getCurrentSession() : null;
		return (null != nc) ? nc.getSessionAsSignerFullAdmin() : null;
		

	}

	@SuppressWarnings("unchecked")
	public static Map getCurrentSessionScope() {
		FacesContext context = FacesContext.getCurrentInstance();
		return (Map) context.getApplication().getVariableResolver().resolveVariable(context, "sessionScope");
		}

	private String getNotesini(String paramname, Boolean system) {
		Session session = getCurrentSession();
		String value = "";
		try {
			value = session.getEnvironmentString(paramname, system);
		} catch (NotesException e) {
			debug.error(e.toString());
		}
		return value;
	}

	public String getDataFolder() {
		String filename = "";
		filename = getNotesini("Directory", true);
		filename = filename.replace("\\", "/");
		if (!filename.endsWith("/")) filename+="/";
		return filename;
	}

	public String getProgramFolder() {
		String filename = "";
		filename = getNotesini("NotesProgram", true);
		filename = filename.replace("\\", "/");
		if (!filename.endsWith("/")) filename+="/";
		return filename;
	}

	public String getFolder(String section) {
		String folder = "";
		if (section.equals("tech")) {
			folder = getDataFolder() + "IBM_TECHNICAL_SUPPORT/";
		} else if (section.equals("ini")) {
			folder = getProgramFolder();
		} else if (section.equals("jvm")) {
			folder = getProgramFolder() + "framework/rcp/deploy/";
		} else if (section.equals("javapolicy")) {
			folder = getProgramFolder() + "jvm/lib/security/";
		} else if (section.equals("rcp")) {
			folder = getDataFolder() + "domino/workspace/.config/";
		} else if (section.equals("xml")) {
			folder = getDataFolder() + "domino/workspace/logs/";
		}
		return folder;
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
		if (pattern==null) return true;
		
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
	
	public static String readableFileSize(long size) {
	    if(size <= 0) return "0 bytes";
	    final String[] units = new String[] { "bytes", "Kbytes", "Mb", "Gb", "Tb" };
	    int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
	    return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
	}
	
	public static String readableDate(long date) {
		String DATE_FORMAT = "dd. MM yyyy";
	    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format( date );
	}
	

	/*
	 * Retrieves a list of files in the specified folder - matching the optional
	 * filter
	 */
	public ArrayList<String> getFiles(String section, String filter) {
		String folder = getFolder(section);
		File dir = new File(folder);
		String[] entries = dir.list(); // get the folder content

		// convert to ArrayList and remove all directories from the list
		ArrayList<String> files = new ArrayList<String>();
		for (int i = 0; i < entries.length; i++) {
			File d = new File(folder + entries[i]);
			if (d.isFile()) {
				if (filter == null) {
					files.add(entries[i]);
				} else {
					if (wildCardMatch(entries[i], filter)) {
						files.add(entries[i]);
					}
				}
			}
		}

		// sort the list
		Collections.sort(files);

		return files;
	}

	public ArrayList<String> getFiles(String section) {
		return getFiles(section, null);
	}

	public String readFileFast(String filename, String filter) {
		StringBuilder result = new StringBuilder();
		debug.info( "Reading from file: " + filename + " (filter: "+filter+")");
		File file = new File(filename);
		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);

			String line = null;
			while ((line = reader.readLine()) != null) {
				if (filter == null) {
					result.append(line);
					result.append("\n");
				} else {
					if (line.indexOf(filter, 0) >= 0) {
						result.append(line);
						result.append("\n");
					}
				}
			}
		} catch (Exception e) {
			debug.error(e.toString());
		}

		return result.toString();
	}

	public String readFileFast(String filename) {
		return readFileFast(filename, null);
	}

	@SuppressWarnings("unchecked")
	public String readLog(String filter, String unid) {
		String result = "";
		if (filter == null)
			filter = "HTTP";
		Session session = getCurrentSession();
		try {
			Database db = session.getDatabase("", "log.nsf");
			View view = db.getView("MiscEvents");
			Document doc = null;
			if (unid == null || unid=="") {
				doc = view.getLastDocument();
			} else {
				doc = db.getDocumentByUNID(unid);
			}
			
			debug.info( "Current document:" + doc.getUniversalID());
			
			//update sessionScope with links to prev/next log document
			try {
				Document prev = view.getPrevDocument(doc);
				Document next = view.getNextDocument(doc);
			
				Map sessionScope = getCurrentSessionScope();
				String tmpUnid = (next==null) ? "" : next.getUniversalID();
				sessionScope.put("nextUnid", tmpUnid);
				debug.info( "Next document:" + tmpUnid);
				
				tmpUnid = (prev==null) ? "" : prev.getUniversalID();
				sessionScope.put("prevUnid", tmpUnid);
				debug.info( "Prev document:" + tmpUnid);
				
			} catch (Exception e) {
				debug.error( e.toString());
			}
			ArrayList<String> list = new ArrayList<String>(doc.getItemValue("EventList"));
			StringBuilder sb = new StringBuilder();

			for (String s : list) {
				if (filter == null) {
					sb.append(s).append("\n");
				} else {
					if (s.indexOf(filter, 0) >= 0) {
						sb.append(s).append("\n");
					}
				}
			}
			result = sb.toString();
		} catch (Exception e) {
			debug.error(e.toString());
		}
		return result;
	}

	public String readLog(String filter) {
		return readLog(filter, null);
	}

	public String readConsole(String filename, String filter) {
		String html = "";
		if (filename == null)
			return "Please select a file";
		if (filter == null)
			filter = "HTTP";
		filename = getFolder("tech") + filename;
		html = readFileFast(filename, filter);
		return html;
	}

	public String readXPages(String filename) {
		String html = "";
		if (filename == null)
			return "Please select a file";
		filename = getFolder("tech") + filename;
		html = readFileFast(filename);
		return html;
	}

	public String readStartup() {
		String html = "";
		String filename = getFolder("xml") + "startup.log";
		html = readFileFast(filename);
		return html;
	}

	public String readNotesini() {
		String html = "";
		String filename = getFolder("ini") + "notes.ini";
		html = readFileFast(filename);
		return html;
	}
	
	public String readJVM() {
		String html = "";
		String filename = getFolder("jvm") + "jvm.properties";
		html = readFileFast(filename);
		return html;
	}
	
	public String readJavaPolicy() {
		String html = "";
		String filename = getFolder("javapolicy") + "java.policy";
		html = readFileFast(filename);
		return html;
	}
	
	public String readRCP() {
		String html = "";
		String filename = getFolder("rcp") + "rcpinstall.properties";
		html = readFileFast(filename);
		return html;
	}

	/**
	 * @param type
	 *            error or trace filename: must be one of the error-log-*.xml or
	 *            trace-log-*.xml files
	 */
	public String readXML(String type, String filename) {

		// default file
		if (filename == null) {
			return "Please select a file";
		}
		
		String path = getFolder("xml");
		String xsl = getFullyQualifiedDatabaseURL()+ "/transform.xsl";
		
		debug.info("Reading: " + getFolder("xml") + filename);
		debug.info("Tranform with: " + xsl);
		
		// Get the number of bytes in the file
		File file = new File(getFolder("xml") + filename);
		if ( file.length()==0) {
			debug.info( "File is empty - return a blank page ...");
			return "";
		}
		
		// XSL transform
		if (true) {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer;
			try {
				transformer = tFactory.newTransformer(new StreamSource(xsl));

				// prepare the output/result
				// TODO Use an String instead of creating temp file on disk
				String tmpFile = path + "log.html";
				StreamResult result = new StreamResult(new FileOutputStream(
						tmpFile));

				// read the log file
				StreamSource inputStream = new StreamSource(path + filename);

				// do the transformation
				transformer.transform(inputStream, result);
				return readFileFast(tmpFile);
			} catch (Exception e) {
				debug.error(e.toString());
			}
		}
		// If transformation fails - just display the xml file
		String html = "";
		filename = getFolder("xml") + filename;
		html = readFileFast(filename);
		return html;

	}

	public String readHtml(String section, String filename) {
		String html = "";
		if (section == null) { /* If the String is null... */
		} else if (section.equals("tech")) {
			if (filename == null)
				return "Please select a file";
			filename = getFolder(section) + filename;
			html = readFileFast(filename, null);
		} else if (section.equals("xml")) {
			if (filename == null)
				filename = "startup.log";
			filename = getFolder(section) + filename;
			html = readFileFast(filename, null);
		} else if (section.equals("ini")) {
			filename = getFolder(section) + filename;
			html = readFileFast(filename);
		} else if (section.equals("log")) {
			html = "log";
		} else { /* DEFAULT Operation */
		}
		return html;
	}
	
	private static String getFullyQualifiedDatabaseURL(){
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
		
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();
		
		return scheme+"://"+serverName+ ((serverPort==80) ? "" : ":"+serverPort) +contextPath;
	}

	

	public String getVersion() {
		return version;
	}

}
