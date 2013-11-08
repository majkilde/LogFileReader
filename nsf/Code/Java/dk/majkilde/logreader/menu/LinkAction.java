package dk.majkilde.logreader.menu;

import java.io.IOException;

import dk.xpages.utils.XSPUtils;

public class LinkAction implements IAction {
	private String url = "";
	private final IMenu parent;

	public LinkAction(IMenu parent) {
		this.parent = parent;
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

}
