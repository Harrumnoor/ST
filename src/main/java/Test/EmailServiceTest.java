package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import AppGenFiles.EmailService;
import AppGenFiles.EmailServiceTask;

public class EmailServiceTest {

	@Test
	public void EmailServiceTest01() {
		try
		{
		EmailServiceTask et = new EmailServiceTask();
		et.bcc="smartappfyp@gmail.com";
		et.to="smartappfyp@gmail.com";
		et.from="smartappfyp@gmail.com";
		et.cc="smartappfyp@gmail.com";
		et.subject="hi";
		et.text="test case";
		new EmailService().sendEmail(et);
		}
		catch(RuntimeException r)
		{
			r.printStackTrace();
			fail("Test Failed for Email Service task");

		}
	}
	
	
	@Test
	public void EmailServiceTest02() {
		boolean flag=false;
		try
		{
		EmailServiceTask et = new EmailServiceTask();
		et.bcc="";
		et.to="harrum4@gmail.com";
		et.from="smartappfyp@gmail.com";
		et.cc="harrum4@gmail.com";
		et.subject="hi";
		et.text="test case";
		new EmailService().sendEmail(et);
		}
		catch(RuntimeException r)
		{
			flag=true;
			assertEquals(true,flag);
		}
	}
	

	@Test
	public void EmailServiceTaskTest01() {
		try
		{
			EmailServiceTask et = new EmailServiceTask();
		}
		catch(Exception r)
		{
			fail("Test Case Failed");
		}
	}
	

}
