package test.dk.xpages;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( { test.dk.majkilde.logreader.ConfigTest.class, test.dk.xpages.utils.XMLTest.class,
		test.dk.majkilde.logreader.config.TabsTest.class })
public final class TestSuite {

}
