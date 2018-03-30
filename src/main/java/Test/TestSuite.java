package Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import AppGenFiles.NewDriver;
import GUI.UserTaskInterface;

@RunWith(Suite.class)
@SuiteClasses({WebPageGenTest.class,EmailServiceInterfaceTest.class,EmailServiceTest.class,InitTest.class,NewDriverTest.class,UserTaskInterfaceTest.class, XMLParserTest.class})
public class TestSuite {


}