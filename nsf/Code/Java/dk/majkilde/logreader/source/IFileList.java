package dk.majkilde.logreader.source;

import java.util.List;

import dk.majkilde.logreader.files.IFile;

public interface IFileList {

	public abstract String getPattern();

	public abstract String getPath();

	public abstract IFile getNewest();

	public abstract List<IFile> getFiles();

	public abstract String getTitle();

	public abstract int getCount();

	public abstract void setCurrent(IFile current);

	public abstract IFile getCurrent();

}