package dk.majkilde.logreader.files;

import java.io.Serializable;

import dk.xpages.utils.NotesStrings;
import dk.xpages.utils.XML;

/**
 * 
 * Used with the file readers. Each line will be processed by the filter to determin if it should be included in the final report
 *
 */
public class Filter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String color = null;
	private String background = null;
	private String styleclass = null;
	private String name = null;
	private boolean visible = true;
	private boolean bold = false;
	private String regex = null;
	private Scope scope = Scope.SELECTION;

	public static enum Scope {
		LINE, SELECTION
	}

	public Filter(String name) {
		this.name = name;
	};

	public Filter(XML config) {
		if (config.string("applyto").equalsIgnoreCase("line")) {
			scope = Scope.LINE;
		} else {
			scope = Scope.SELECTION;
		}
		regex = config.content();
		color = config.string("color");
		background = config.string("background");
		styleclass = config.string("class");
		name = config.string("name");
		visible = (config.string("visible").equalsIgnoreCase("false")) ? false : true;
		bold = config.string("bold").equalsIgnoreCase("true");
	}

	public Filter setColor(String color) {
		this.color = color;
		return this;
	}

	public Filter setBackground(String background) {
		this.background = background;
		return this;
	}

	public Filter setClass(String classname) {
		this.styleclass = classname;
		return this;
	}

	public Filter setName(String name) {
		this.name = name;
		return this;
	}

	public Filter setScope(Scope scope) {
		this.scope = scope;
		return this;
	}

	public Filter setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public Filter setBold(boolean bold) {
		this.bold = bold;
		return this;
	}

	public Filter setRegex(String regex) {
		this.regex = regex;
		return this;
	}

	public String getStyle() {
		StringBuilder style = new StringBuilder();
		if (visible) {
			style.append("style=\"display:inline; ");
		} else {
			style.append("style=\"display:none; ");
		}

		if (bold) {
			style.append("font-weight:bold; ");
		}

		if (!NotesStrings.isBlank(color)) {
			style.append("color:" + color + "; ");
		}

		if (!NotesStrings.isBlank(background)) {
			style.append("background-color:" + background + "; ");
		}

		style.append("\"");

		return style.toString();
	}

	public String getStyleClass() {
		if (NotesStrings.isBlank(styleclass)) {
			return "";
		}
		return "class=\"" + styleclass + "\" ";
	}

	public String getName() {
		return name;
	}

	public Scope getScope() {
		return scope;
	}

	public boolean matches(String source) {
		return source.matches(getRegex());
	}

	public String applyFilter(String source) {
		if (NotesStrings.isBlank(regex)) {
			return source;
		}

		String replacement = NotesStrings.messageFormat("<span {0} {1}>$0{2}</span>", getStyle(), getStyleClass(),
				(scope == Scope.LINE) ? "</br>" : "");

		String result = source.replaceAll(getRegex(), replacement);
		return result;
	}

	private String getRegex() {
		if (NotesStrings.isBlank(regex)) {
			return "";
		}

		if (getScope() == Scope.LINE) {
			return ".*" + regex + ".*";
		}

		return regex;
	}
}
