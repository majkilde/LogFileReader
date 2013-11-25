package dk.majkilde.logreader.menu;

public interface IAction {

	public String getType();

	public IMenu getParent();

	public void execute();

	public boolean isValid();
}
