package dk.xpages.utils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import lotus.domino.Database;
import lotus.domino.Session;

import com.ibm.xsp.designer.context.XSPContext;
import com.ibm.xsp.designer.context.XSPUrl;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * 
 * Note: More handy utils in com.ibm.xsp.extlib.util.ExtLibUtil
 *
 */
public class XSPUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static XSPUrl getUrl() {
		try {
			return XSPContext.getXSPContext(FacesContext.getCurrentInstance()).getUrl();
		} catch (Exception e) {
			return null;
		}
	}

	public static String getUrlParameter(String key) {
		try {
			return getUrl().getParameter(key);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public static Object getVariableValue(String varName) {
		FacesContext context = FacesContext.getCurrentInstance();
		return context.getApplication().getVariableResolver().resolveVariable(context, varName);
	}

	public static Session getCurrentSession() {
		return (Session) getVariableValue("session");
	}

	public static Database getCurrentDatabase() {
		return (Database) getVariableValue("database");
	}

	public static DominoDocument getCurrentDocument() {
		return (DominoDocument) getVariableValue("currentDocument");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getSessionScope() {
		return (Map<String, Object>) getVariableValue("sessionScope");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getApplicationScope() {
		return (Map<String, Object>) getVariableValue("applicationScope");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getViewScope() {
		return (Map<String, Object>) getVariableValue("viewScope");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getRequestScope() {
		return (Map<String, Object>) getVariableValue("requestScope");
	}

	public static InputStream getResourceAsStream(String filename) {
		return FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(filename);
	}

	public static String getFullyQualifiedDatabaseURL() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String contextPath = request.getContextPath();

		return scheme + "://" + serverName + ((serverPort == 80) ? "" : ":" + serverPort) + contextPath;
	}
}
