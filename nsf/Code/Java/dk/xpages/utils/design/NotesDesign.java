package dk.xpages.utils.design;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.NoteCollection;
import lotus.domino.NotesException;
import dk.xpages.utils.NotesObjects;
import dk.xpages.utils.NotesStrings;

public class NotesDesign {
	private final Database db;

	public NotesDesign() {
		this(NotesObjects.getCurrentDatabase());
	}

	public NotesDesign(final Database db) {
		this.db = db;
	}

	private NoteCollection getNoteCollection(boolean selectAll) throws NotesException {
		return db.createNoteCollection(selectAll);
	}

	public NoteCollection getAllDesignElements() throws NotesException {
		NoteCollection coll = getNoteCollection(false);
		coll.selectAllDesignElements(true);
		coll.buildCollection();
		return coll;
	}

	public NoteCollection getDesignElements(DesignElementType type) throws NotesException {
		return getDesignElements(type, null);
	}

	public NoteCollection getDesignElements(DesignElementType type, String elementname) throws NotesException {
		NoteCollection coll = getNoteCollection(false);
		coll.setSelectAcl(type == DesignElementType.ACL);
		coll.setSelectActions(type == DesignElementType.Actions);
		coll.setSelectAgents(type == DesignElementType.Agents);
		coll.setSelectDatabaseScript(type == DesignElementType.DatabaseScript);
		coll.setSelectDataConnections(type == DesignElementType.DataConnections);
		coll.setSelectDocuments(type == DesignElementType.Documents);
		coll.setSelectFolders(type == DesignElementType.Folders);
		coll.setSelectForms(type == DesignElementType.Forms);
		coll.setSelectFramesets(type == DesignElementType.Framesets);
		coll.setSelectHelpAbout(type == DesignElementType.HelpAbout);
		coll.setSelectHelpIndex(type == DesignElementType.HelpIndex);
		coll.setSelectHelpUsing(type == DesignElementType.HelpUsing);
		coll.setSelectIcon(type == DesignElementType.Icon);
		coll.setSelectImageResources(type == DesignElementType.ImageResources);

		coll.setSelectJavaResources(type == DesignElementType.JavaResources);
		coll.setSelectMiscCodeElements(type == DesignElementType.MiscCodeElements);
		coll.setSelectMiscFormatElements(type == DesignElementType.MiscFormatElements);
		coll.setSelectMiscIndexElements(type == DesignElementType.MiscIndexElements);
		coll.setSelectNavigators(type == DesignElementType.Navigators);
		coll.setSelectOutlines(type == DesignElementType.Outlines);
		coll.setSelectProfiles(type == DesignElementType.Profiles);
		coll.setSelectScriptLibraries(type == DesignElementType.ScriptLibraries);
		coll.setSelectSharedFields(type == DesignElementType.SharedFields);
		coll.setSelectStylesheetResources(type == DesignElementType.Stylesheets);
		coll.setSelectSubforms(type == DesignElementType.Subforms);
		coll.setSelectViews(type == DesignElementType.Views);

		if (elementname != null) {
			coll.setSelectionFormula(NotesStrings.messageFormat("$TITLE=\"{0}\"", elementname));
		}

		coll.buildCollection();

		return coll;
	}

	public Document getDesignElementByName(DesignElementType type, String elementname) throws NotesException {
		NoteCollection coll = getDesignElements(type, elementname);
		Document note = null;
		if (coll.getCount() == 1) {
			note = NotesObjects.getDocumentById(db, coll.getFirstNoteID());
		}
		return note;
	}

}
