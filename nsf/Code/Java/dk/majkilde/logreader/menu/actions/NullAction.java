package dk.majkilde.logreader.menu.actions;

import java.io.Serializable;

import dk.majkilde.logreader.menu.IMenu;

public class NullAction implements IAction, Serializable {

	private static final long serialVersionUID = 1L;

	public void execute() {
		//This is a null action -> dont do anything ...
	}

	public IMenu getParent() {
		return null;
	}

	public boolean isValid() {
		return false;
	}

}
