package test.com.sagui.dataset.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.com.sagui.dataset.common.dataset.DatasetTest;
import test.com.sagui.dataset.common.index.IdentityIndexPerformanceTest;
import test.com.sagui.dataset.common.index.IdentityIndexTest;

@RunWith(Suite.class)
@SuiteClasses({ DatasetTest.class, IdentityIndexTest.class, IdentityIndexPerformanceTest.class })
public class AllTests {

}
