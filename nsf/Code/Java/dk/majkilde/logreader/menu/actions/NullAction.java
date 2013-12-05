package dk.majkilde.logreader.menu.actions;

import dk.majkilde.logreader.menu.IMenu;

public class NullAction extends Action {

	private static final long serialVersionUID = 1L;

	public NullAction(IMenu parent) {
		super(parent);
	}

	public String getType() {
		return "null";
	}

	@Override
	protected void executeAction() {
	}

	public boolean isValid() {
		return false;
	}

}
