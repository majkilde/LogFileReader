package dk.xpages.utils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLConnection;

import javax.faces.context.FacesContext;

import com.ibm.xsp.webapp.XspHttpServletResponse;

public class Download implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String charset = null;
	private String contenttype = null;

	public Download() {
		this("utf-8"); //"iso-8859-1";
	}

	public Download(String charset) {
		this.charset = charset;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContenttype() {
		return contenttype;
	}

	public String getContenttype(String filename) {
		return URLConnection.guessContentTypeFromName(filename);
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public void setContenttype(File file) {
		this.contenttype = getContenttype(file.getName());
	}

	public void save(final byte[] data, String filename) throws IOException {
		XspHttpServletResponse response = (XspHttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

		if (contenttype == null) {
			response.setContentType(getContenttype(filename));
		} else {
			response.setContentType(contenttype);
		}

		response.setCharacterEncoding(charset);
		response.setHeader("Content-disposition", "attachment; filename=" + filename);
		OutputStream output = response.getOutputStream();
		output.write(data);
		output.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

}
