package dk.majkilde.logreader.files;

import java.util.Date;

public interface IFile extends Comparable<IFile> {
	public String getHtml();

	public String getFilename();

	public String getPath();

	public Date getDate();

	public long getSize();

	public abstract void download();

	public Filters getFilters();

}
