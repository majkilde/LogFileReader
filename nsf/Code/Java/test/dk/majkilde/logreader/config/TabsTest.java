package test.dk.majkilde.logreader.config;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.majkilde.logreader.config.LeafMenuItem;
import dk.majkilde.logreader.config.CompositeMenuItem;
import dk.xpages.utils.XML;

public class TabsTest {

	private static XML xml;

	@BeforeClass
	public static void Setup() throws UnsupportedEncodingException {
		xml = new XML(xmlfile(), "config");
	}

	private static final String xmlfile() {
		StringBuilder xml = new StringBuilder();
		xml.append("<config>");
		xml.append("<tab id=\"id001\">");
		xml.append("</tab>");
		xml.append("<tab id=\"id002\" name=\"Named Tab\">");
		xml.append("</tab>");
		xml.append("<tab name=\"No id\">");
		xml.append("</tab>");
		xml.append("</config>");

		return xml.toString();
	}

	@Test
	public void LoadThreeTabs() throws Exception {
		CompositeMenuItem tabs = CompositeMenuItem.createTabsFromXML(xml);
		assertEquals("Have we loaded 3 tabs?", 3, tabs.getMap().size());

	}

	@Test
	public void CorrectAutoCreatedId() throws Exception {
		CompositeMenuItem tabs = CompositeMenuItem.createTabsFromXML(xml);
		LeafMenuItem tab = tabs.getMap().get("noid");

		assertEquals("Check the auto generated id", "No id", tab.getName());

	}
}
