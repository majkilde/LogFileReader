package dk.majkilde.logreader.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.ibm.xsp.designer.context.XSPUrl;

import dk.majkilde.logreader.menu.IMenu;
import dk.majkilde.logreader.menu.MenuItem;
import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class LayoutBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private final Logger log = LogManager.getLogger();

	private final IMenu menu;

	public LayoutBean() {
		InputStream input = XSPUtils.getResourceAsStream("config.xml");
		XML configXML = new XML(input, "config");
		menu = MenuItem.createFromXML(configXML.child("menu"));
	}

	public IMenu getMenu() {
		return menu;
	}

	public List<IMenu> getTabs() {
		return menu.getChildren();
	}

	private String getSelectedTabId() {
		XSPUrl url = XSPUtils.getUrl();
		String selectedId = url.getParameter("tab");
		if (NotesStrings.isBlank(selectedId)) {
			selectedId = getTabs().get(0).getId();
		}
		return selectedId;
	}

	private String getSelectedMenuId() {
		XSPUrl url = XSPUtils.getUrl();
		String selectedId = url.getParameter("menu");
		if (NotesStrings.isBlank(selectedId)) {
			selectedId = getSelectedTab().getChildren().get(0).getId();
		}
		return selectedId;
	}

	public boolean isTabSelected(String tabid) {
		return getSelectedTabId().equals(tabid);
	}

	public boolean isMenuSelected(String menuid) {
		boolean selected = getSelectedMenuId().equals(menuid);
		return selected;
	}

	public IMenu getSelectedTab() {
		return menu.getChild(getSelectedTabId());
	}

}
