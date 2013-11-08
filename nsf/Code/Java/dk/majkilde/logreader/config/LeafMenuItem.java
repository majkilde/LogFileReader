package dk.majkilde.logreader.config;

import static org.apache.commons.lang3.StringUtils.isBlank;
import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;

public class LeafMenuItem {
	private final String name;
	private final String id;
	private final String url;

	private LeafMenuItem(final String name, final String id, String url) {
		if (isBlank(name) && isBlank(id)) {
			throw new IllegalArgumentException("You must provide a name or an id");
		}
		if (isBlank(url)) {
			throw new IllegalArgumentException("You must provide an url");
		}

		if (isBlank(name)) {
			this.name = id;
		} else {
			this.name = name;
		}

		if (isBlank(id)) {
			this.id = name.replaceAll("\\s+", "").toLowerCase();
		} else {
			this.id = id;
		}

		this.url = NotesStrings.messageFormat(url, this.id);

	}

	private static String getUrl(final XML tabXML) {
		if (!isBlank(tabXML.string("link"))) {
			return tabXML.string("link");
		}
		if (!isBlank(tabXML.string("page"))) {
			return "/" + tabXML.string("page") + "?tab={0}";
		}
		return "";
	}

	public static LeafMenuItem createTabFromXML(final XML tabXML) {
		return new LeafMenuItem(tabXML.string("name"), tabXML.string("id"), getUrl(tabXML));
	}

	public static LeafMenuItem createTab(String name, String id, String url) {
		return new LeafMenuItem(name, id, url);
	}

	public static LeafMenuItem createTab(String name, String id) {
		return new LeafMenuItem(name, id, "#");
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getUrl() {
		return url;
	}

	public String getTarget() {
		if (NotesStrings.containsIgnoreCase(url, "://")) {
			return "_blank";
		}
		return null;
	}

}
