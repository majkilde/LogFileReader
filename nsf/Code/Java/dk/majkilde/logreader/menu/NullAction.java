package dk.majkilde.logreader.menu;

public class NullAction implements IAction {

	public String getType() {
		return "null";
	}

	public void execute() {
		//This is a null action -> dont do anything ...
	}

	public IMenu getParent() {
		return null;
	}

}
