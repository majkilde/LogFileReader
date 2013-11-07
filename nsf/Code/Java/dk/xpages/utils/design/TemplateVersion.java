package dk.xpages.utils.design;

import java.util.Date;

import lotus.domino.Database;
import lotus.domino.DateTime;
import lotus.domino.Document;
import lotus.domino.NotesException;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesObjects;

public class TemplateVersion {
	private Document note = null;

	private final Logger log = LogManager.getLogger();

	public TemplateVersion() {
		this(NotesObjects.getCurrentDatabase());
	}

	public TemplateVersion(Database db) {
		NotesDesign design = new NotesDesign(db);
		try {
			note = design.getDesignElementByName(DesignElementType.SharedFields, "$TemplateBuild");
		} catch (Exception e) {
			log.error(e);
		}

	}

	public String getVersion() {
		try {
			return note.getItemValueString("$TemplateBuild");
		} catch (NotesException e) {
			return "";
		}
	}

	public Date getDate() {
		try {
			DateTime dt = (DateTime) note.getItemValueDateTimeArray("$TemplateBuildDate").get(0);
			return dt.toJavaDate();
		} catch (NotesException e) {
			return null;
		}
	}

	public String getName() {
		try {
			return note.getItemValueString("$TemplateBuildName");
		} catch (NotesException e) {
			return "";
		}
	}

}
