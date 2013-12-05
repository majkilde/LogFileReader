package test.dk.majkilde.logreader.menu;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;

import org.junit.BeforeClass;
import org.junit.Test;

import dk.majkilde.logreader.menu.IMenu;
import dk.majkilde.logreader.menu.MenuItem;
import dk.majkilde.logreader.menu.actions.IAction;
import dk.majkilde.logreader.menu.actions.LinkAction;
import dk.xpages.utils.XML;

public class MenuTest {

	private static XML menuXML;
	private static IMenu menu;

	@BeforeClass
	public static void Setup() throws UnsupportedEncodingException {
		menuXML = new XML(xmlfile(), "menu");
		menu = MenuItem.createFromXML(menuXML);
	}

	private static final String xmlfile() {
		StringBuilder xml = new StringBuilder();
		xml.append("<menu>");
		xml.append("<item name=\"Menu 1\">");
		xml.append("</item>");
		xml.append("<item id=\"id001\" name=\"Named Tab\">");
		xml.append("</item>");
		xml.append("<item name=\"Link\">");
		xml.append("<action type=\"link\" url=\"www.dr.dk\"/>");
		xml.append("</item>");
		xml.append("<item name=\"Main\">");
		xml.append("<item name=\"Sub 1\">");
		xml.append("</item>");
		xml.append("<item name=\"Sub 2\">");
		xml.append("</item>");
		xml.append("<item name=\"Sub 3\">");
		xml.append("</item>");

		xml.append("</item>");

		xml.append("</menu>");

		return xml.toString();
	}

	@Test
	public void RootMenu_Name_root() throws Exception {
		assertEquals("root", menu.getName());

	}

	@Test
	public void LinkMenuHasALinkAction_drdk() throws Exception {
		IMenu link = menu.getChild("link");
		IAction action = link.getAction();
		assertEquals("link", action.getType());

		LinkAction linkaction = (LinkAction) action;
		assertEquals("www.dr.dk", linkaction.getUrl());

	}

}
