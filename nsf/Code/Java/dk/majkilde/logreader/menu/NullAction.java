package dk.majkilde.logreader.menu;

import java.io.Serializable;

public class NullAction implements IAction, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getType() {
		return "null";
	}

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
