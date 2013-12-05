package dk.majkilde.logreader.menu.actions;

import java.io.Serializable;

import dk.majkilde.logreader.menu.IAction;
import dk.majkilde.logreader.menu.IMenu;
import dk.xpages.utils.XSPUtils;

public abstract class Action implements IAction, Serializable {

	private static final long serialVersionUID = 1L;
	private final IMenu parent;

	public Action(IMenu parent) {
		this.parent = parent;
	}

	public IMenu getParent() {
		return parent;
	}

	//protected abstract void initialize();

	protected abstract void executeAction();

	protected void updateMenu() {
		XSPUtils.getViewScope().put("currentMenu", getParent());
	}

	public void execute() {
		//	initialize();
		executeAction();
		updateMenu();
	}
}
