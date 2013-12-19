package dk.majkilde.logreader.beans;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import com.ibm.xsp.designer.context.XSPUrl;

import dk.majkilde.logreader.files.FileList;
import dk.majkilde.logreader.menu.IMenu;
import dk.majkilde.logreader.menu.MenuItem;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class LayoutBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private IMenu menu = null;

	public LayoutBean() {
		try {
			InputStream input = XSPUtils.getResourceAsStream("config.xml");
			if (input == null) {
				XSPUtils.addFatal("File nor found: config.xml");
			} else {
				XML configXML = new XML(input, "config");
				if (configXML.hasChild("menu")) {
					menu = MenuItem.createFromXML(configXML.child("menu"));
				} else {
					XSPUtils.addFatal("config.xml: No <menu>");
				}
			}
		} catch (Exception e) {
			XSPUtils.addFatal(e);
		}
	}

	public IMenu getMenu() {
		return menu;
	}

	public List<IMenu> getTabs() {
		try {
			return menu.getChildren();
		} catch (Exception e) {
			XSPUtils.addFatal(e);
			return null;
		}
	}

	private String getSelectedTabId() {
		String selectedId = "";
		try {
			XSPUrl url = XSPUtils.getUrl();
			selectedId = url.getParameter("tab");
			if (NotesStrings.isBlank(selectedId)) {
				selectedId = getTabs().get(0).getId();
			}
		} catch (Exception e) {
			XSPUtils.addFatal(e);
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
			XSPUtils.addFatal(e);
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
		try {
			return menu.getChild(getSelectedTabId());
		} catch (Exception e) {
			XSPUtils.addFatal(e);
			return null;
		}
	}

	public FileList getCurrentFilelist() {
		try {
			getSelectedMenu().executeAction();
		} catch (Exception e) {
			XSPUtils.addFatal(e);
		}

		return null;
	}

}
