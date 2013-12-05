package test.dk.majkilde.logreader.files;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import dk.majkilde.logreader.files.Filter;

public class FilterTest {
	@Test
	public void TestASimpleFilter() throws Exception {
		Filter filter = new Filter("test");

		filter.setRegex("Hello");
		filter.setBold(true);

		assertEquals("<span style=\"display:inline; font-weight:bold; \" >Hello</span> world", filter.applyFilter("Hello world"));

	}
}
