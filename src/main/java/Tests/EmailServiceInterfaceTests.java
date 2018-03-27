package Tests;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.Test;

import GUI.EmailServiceInterface;
import GUI.WebPageGen;
import AppGenFiles.EmailServiceTask;
import AppGenFiles.NewDriver;
public class EmailServiceInterfaceTests {
	
		
   

	@Test 
	public void displayEmailFormTest()
	{
		
		EmailServiceTask emailTask = new EmailServiceTask();
		try
		{
		emailTask.taskId="SendAcceptanceEmail";
		emailTask.assignee="Manager";
		emailTask.taskName="Send Acceptance Email";
		new EmailServiceInterface().displayEmailForm(emailTask);
		EmailServiceInterface e = new EmailServiceInterface();
		e.to_field.setValue("smartappfyp@gmail.com");
		e.from_field.setValue("smartappfyp@gmail.com");
		e.cc_field.setValue("smartappfyp@gmail.com");
		e.bcc_field.setValue("harrum4@gmail.com");
		e.subject_field.setValue("smartappfyp@gmail.com");
		e.text_field.setValue("smartappfyp@gmail.com");
		new NewDriver();
		NewDriver.startUp();
		e.emailButton(emailTask);
		
		}
		catch(Exception e)
		{
			fail("test has failed");
		}
	
	}
}

