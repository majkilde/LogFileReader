package dk.majkilde.logreader.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import dk.xpages.utils.XML;

public class CompositeMenuItem {
	private final Map<String, LeafMenuItem> map = new LinkedHashMap<String, LeafMenuItem>();

	public Map<String, LeafMenuItem> getMap() {
		return map;
	}

	public static CompositeMenuItem createTabsFromXML(XML tabsXML) {
		CompositeMenuItem tabs = new CompositeMenuItem();
		for (XML tabXML : tabsXML.children("item")) {
			tabs.add(LeafMenuItem.createTabFromXML(tabXML));
		}
		return tabs;
	}

	public void add(LeafMenuItem tab) {
		map.put(tab.getId(), tab);
	}

	public LeafMenuItem get(String id) {
		return map.get(id);
	}

	public LeafMenuItem getFirst() {
		return getList().get(0);
	}

	public List<LeafMenuItem> getList() {
		return new ArrayList<LeafMenuItem>(map.values());
	}
}
