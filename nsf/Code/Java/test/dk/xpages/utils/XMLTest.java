package test.dk.xpages.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.xpages.utils.XML;

public class XMLTest {

	private static final String xmlfile() {
		StringBuilder xml = new StringBuilder();
		xml.append("<config>");
		xml.append("<author age=\"45\">");
		xml.append("Jakob Majkilde");
		xml.append("</author>");

		xml.append("</config>");

		return xml.toString();
	}

	@Test
	public void ReadAuthorContentFromXML() throws Exception {
		XML xml = new XML(xmlfile(), "config");
		XML element = xml.child("author");
		assertEquals("Jakob Majkilde", element.content());
	}

	@Test
	public void ReadAuthorAgeAttribFromXML() throws Exception {
		XML xml = new XML(xmlfile(), "config");
		XML element = xml.child("author");
		assertEquals(45, element.integer("age"));
	}
}
