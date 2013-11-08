package dk.majkilde.logreader.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.ibm.xsp.designer.context.XSPUrl;

import dk.majkilde.logreader.config.CompositeMenuItem;
import dk.majkilde.logreader.config.LeafMenuItem;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class LayoutBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Logger log = LogManager.getLogger();

	private final CompositeMenuItem tabs;

	public LayoutBean() {
		InputStream input = XSPUtils.getResourceAsStream("config.xml");
		XML configXML = new XML(input, "config");
		tabs = CompositeMenuItem.createTabsFromXML(configXML.child("menu"));
	}

	public CompositeMenuItem getTabs() {
		return tabs;
	}

	public List<LeafMenuItem> getTabList() {
		return tabs.getList();
	}

	public boolean isTabSelected(String tabid) {
		XSPUrl url = XSPUtils.getUrl();
		String selected = url.getParameter("tab");
		if (NotesStrings.isBlank(selected)) {
			selected = tabs.getFirst().getId();
		}

		return selected.equals(tabid);
	}

}
