package dk.majkilde.logreader.menu;

import java.util.List;

import dk.majkilde.logreader.menu.actions.IAction;

public interface IMenu {
	public String getName();

	public String getId();

	public IMenu getChild(String id);

	public List<IMenu> getChildren();

	public IAction getAction();

	public void executeAction();

	public boolean isVisible();

}
