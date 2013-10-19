package dk.majkilde.logreader.files;

import java.io.IOException;
import java.util.Date;

public interface IFile extends Comparable<IFile> {
	public String getHtml();

	public String getFilename();

	public String getPath();

	public Date getDate();

	public long getSize();

	public byte[] getByteArray() throws IOException;

	public abstract void download();

}
