package dk.xpages.utils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author Jakob Majkilde
 * 
 *         Note: Use StringEscapeUtils to encode/decode strings
 *         Note: To parse a String you can either use String.format or MessageFormat
 *
 *         Note: more handy String util can be found in com.ibm.commons.util.StringUtil;
 * 
 */

public class NotesStrings extends StringUtils {

	/**
	 * MessageFormat: http://docs.oracle.com/javase/7/docs/api/java/text/MessageFormat.html
	 */
	public static String messageFormat(String source, Object... params) {
		MessageFormat msg = new MessageFormat(source);
		return msg.format(params);
	}

	/**
	 * 
	 * @param source
	 * @param regEx RegularExpression: http://regexlib.com/CheatSheet.aspx
	 * @param index
	 * @return

	 */

	public static String strToken(String source, String regEx, int index) {
		if (source == null)
			return null;

		String[] arr = source.split(regEx);
		if (index < arr.length) {
			return arr[index];
		} else {
			return "";
		}
	}

	/**
	 * 
	 * @param source
	 * @param delimiter	Any char
	 * @param index First index=0
	 * @return
	 */
	public static String strToken(String source, char delimiter, int index) {
		return strToken(source, "[" + delimiter + "]", index);
	}

	public static String join(String delimiter, Object[] values) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (values[i].toString().trim() != "") {
				ret.append(values[i]);

				if (i != values.length - 1)
					ret.append(delimiter);
			}
		}
		return ret.toString();
	}

	public static <T> String join(String delimiter, Vector<T> values) {
		return join(delimiter, values.toArray());
	}

	public static <T> String join(String delimiter, List<T> values) {
		return join(delimiter, values.toArray());
	}

	public static String join(String delimiter, double[] values) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			ret.append(values[i]);
			if (i != values.length - 1)
				ret.append(delimiter);
		}
		return ret.toString();
	}

	public static String strLeft(String input, String delimiter) {
		return input.substring(0, input.indexOf(delimiter));
	}

	public static String strRight(String input, String delimiter) {
		return input.substring(input.indexOf(delimiter) + delimiter.length());
	}

	public static String strLeftBack(String input, String delimiter) {
		return input.substring(0, input.lastIndexOf(delimiter));
	}

	public static String strLeftBack(String input, int chars) {
		return input.substring(0, input.length() - chars);
	}

	public static String strRightBack(String input, String delimiter) {
		return input.substring(input.lastIndexOf(delimiter) + delimiter.length());
	}

	public static String strRightBack(String input, int chars) {
		return input.substring(input.length() - chars);
	}

}
