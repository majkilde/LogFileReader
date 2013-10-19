package dk.xpages.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dk.xpages.log.LogManager;
import dk.xpages.log.Logger;

/*
 * @link http://argonrain.wordpress.com/2009/10/27/000/
 * Quote: "All you need is this class, which you can of course customise however you like."
 */
public class XML implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private final Logger log = LogManager.getLogger();

	private final String name;
	private String content;
	private final Map<String, String> nameAttributes = new HashMap<String, String>();
	private final Map<String, ArrayList<XML>> nameChildren = new HashMap<String, ArrayList<XML>>();

	public XML(InputStream inputStream, String rootName) {
		//		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		//		builderFactory.createXMLEventReader(
		this(rootElement(inputStream, rootName));
	}

	public XML(String xml, String rootName) throws UnsupportedEncodingException {
		this(rootElement(toStream(xml), rootName));
	}

	public XML(File file, String rootName) {
		this(fileInputStream(file), rootName);
	}

	public XML(String rootName) {
		this.name = rootName;
	}

	private XML(Element element) {
		this.name = element.getNodeName();
		this.content = element.getTextContent();
		NamedNodeMap namedNodeMap = element.getAttributes();
		int n = namedNodeMap.getLength();
		for (int i = 0; i < n; i++) {
			Node node = namedNodeMap.item(i);
			String name = node.getNodeName();
			addAttribute(name, node.getNodeValue());
		}
		NodeList nodes = element.getChildNodes();
		n = nodes.getLength();
		for (int i = 0; i < n; i++) {
			Node node = nodes.item(i);
			int type = node.getNodeType();
			if (type == Node.ELEMENT_NODE) {
				XML child = new XML((Element) node);
				addChild(node.getNodeName(), child);
			}
		}
	}

	private static InputStream toStream(String source) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(source.getBytes("utf-8"));
	}

	public void addAttribute(String name, String value) {
		nameAttributes.put(name, value);
	}

	private void addChild(String name, XML child) {
		ArrayList<XML> children = nameChildren.get(name);
		if (children == null) {
			children = new ArrayList<XML>();
			nameChildren.put(name, children);
		}
		children.add(child);
	}

	public String name() {
		return name;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String content() {
		return content;
	}

	public void addChild(XML xml) {
		addChild(xml.name(), xml);
	}

	public void addChildren(XML... xmls) {
		for (XML xml : xmls)
			addChild(xml.name(), xml);
	}

	public XML child(String name) {
		XML child = optChild(name);
		return child;
	}

	public XML optChild(String name) {
		ArrayList<XML> children = children(name);
		int n = children.size();
		if (n > 1)
			throw new RuntimeException("Could not find individual child node: " + name);
		return n == 0 ? null : children.get(0);
	}

	public boolean option(String name) {
		return optChild(name) != null;
	}

	public ArrayList<XML> children(String name) {
		ArrayList<XML> children = nameChildren.get(name);
		return children == null ? new ArrayList<XML>() : children;
	}

	public String string(String name) {
		String value = optString(name);
		if (value == null) {
			return "";
		}
		return value;
	}

	public String optString(String name) {
		return nameAttributes.get(name);
	}

	public int integer(String name) {
		return Integer.parseInt(string(name));
	}

	public Integer optInteger(String name) {
		String string = optString(name);
		return string == null ? null : integer(name);
	}

	public double doubleValue(String name) {
		return Double.parseDouble(optString(name));
	}

	public Double optDouble(String name) {
		String string = optString(name);
		return string == null ? null : doubleValue(name);
	}

	private static DocumentBuilder getBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		return builderFactory.newDocumentBuilder();
	}

	private static Element rootElement(InputStream inputStream, String rootName) {
		try {
			Document document = getBuilder().parse(inputStream);
			Element rootElement = document.getDocumentElement();
			if (!rootElement.getNodeName().equals(rootName))
				throw new RuntimeException("Could not find root node: " + rootName);
			return rootElement;
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		} catch (ParserConfigurationException exception) {
			throw new RuntimeException(exception);
		} catch (SAXException exception) {
			throw new RuntimeException(exception);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception exception) {
					throw new RuntimeException(exception);
				}
			}
		}
	}

	private static FileInputStream fileInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

}