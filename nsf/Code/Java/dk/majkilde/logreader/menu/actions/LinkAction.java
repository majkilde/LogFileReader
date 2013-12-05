package dk.majkilde.logreader.menu.actions;

import java.io.IOException;
import java.io.Serializable;

import dk.majkilde.logreader.menu.IMenu;
import dk.xpages.utils.XML;
import dk.xpages.utils.XSPUtils;

public class LinkAction implements IAction, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String url = "";
	private final IMenu parent;

	public LinkAction(XML config, IMenu parent) {
		this.parent = parent;
		setUrl(config.string("url"));
	}

	public IMenu getParent() {
		return parent;
	}

	public String getType() {
		return "link";
	}

	public LinkAction setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void execute() {
		// TODO Auto-generated method stub
		try {
			XSPUtils.getExternalContext().redirect(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isValid() {
		return !getUrl().isEmpty();
	}

}
