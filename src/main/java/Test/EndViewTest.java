package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import my.vaadin.FirstActivitiGUI.EndView;
import my.vaadin.FirstActivitiGUI.EnumField;
import my.vaadin.FirstActivitiGUI.FormProperties;
import my.vaadin.FirstActivitiGUI.LoginView;

public class EndViewTest {

	@Test
	public void test() {
		new EndView();
		new EnumField();
		new FormProperties ();
		try
		{
			new LoginView().creatLoginView();
			LoginView l= new LoginView();
			l.id.setValue("Customer");
			l.pw.setValue("Customer");
			l.buttonClicked();
			l.pw.setValue("Customerr");
			l.buttonClicked();
		}
		catch (Exception e)
		{
			fail("Button clicked failed");
		}

}
	
	
}