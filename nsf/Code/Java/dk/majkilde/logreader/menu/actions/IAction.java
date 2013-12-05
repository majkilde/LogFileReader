package dk.majkilde.logreader.menu.actions;

import dk.majkilde.logreader.menu.IMenu;

public interface IAction {

	public String getType();

	public IMenu getParent();

	public void execute();

	public boolean isValid();
}
