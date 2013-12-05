package dk.majkilde.logreader.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.ibm.xsp.designer.context.XSPUrl;

import dk.majkilde.logreader.menu.IMenu;
import dk.majkilde.logreader.menu.MenuItem;
import dk.majkilde.logreader.source.IFileList;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class LayoutBean implements Serializable {
	private static final long serialVersionUID = 1L;

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

	private IMenu getSelectedMenu() {
		//Check the viewscope
		IMenu currentMenu = (IMenu) XSPUtils.getViewScope().get("currentMenu");
		if (currentMenu != null) {
			return currentMenu;
		}

		//defaults to the first menu
		try {
			return getSelectedTab().getChildren().get(0);
		} catch (Exception e) {
			return null;
		}

	}

	private String getSelectedMenuId() {
		IMenu selectedMenu = getSelectedMenu();
		if (selectedMenu != null) {
			return selectedMenu.getId();
		}

		//Check the URL
		XSPUrl url = XSPUtils.getUrl();
		String urlId = url.getParameter("menu");
		if (!NotesStrings.isBlank(urlId)) {
			return urlId;
		}

		return "";
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

	public IFileList getCurrentFilelist() {
		getSelectedMenu().executeAction();
		return null;
	}

}
