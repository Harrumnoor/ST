package Tests;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;

import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.themes.ValoTheme;

import AppGenFiles.UserTask;
import GUI.UserTaskInterface;
import AppGenFiles.EmailServiceTask;
import AppGenFiles.NewDriver;
import my.vaadin.FirstActivitiGUI.EnumField;
import my.vaadin.FirstActivitiGUI.FormProperties;

public class UserTaskInterfaceTests {
	
	@Test
	public void displayUserTaskFormTest()
	{
		
		try
		{
			
			new UserTaskInterface().displayUserTaskForm(new NewDriver().userTaskList.get(0));
		}
		catch(Exception e)
		{
			fail("Test has failed");
		}
	}
	
	@Test
	public void buttonPressedTest ()
	{
		try
		{
			new UserTaskInterface().buttonPressed(new NewDriver().userTaskList.get(2),null,1);
			new UserTaskInterface().buttonPressed(new NewDriver().userTaskList.get(0),null,1);
			new UserTaskInterface().buttonPressed(null,new NewDriver().emailTaskList.get(0),0);
		}
		catch(Exception e)
		{
			fail("Test has failed");
		}
	}
	
	@Test
	public void getFormInputTest()
	{
		for (int i=0;i<new NewDriver().userTaskList.get(2).formPropertiesList.size();i++)
		{	
		List <RadioButtonGroup> radioButtonFields=new UserTaskInterface().radioButtonFields;
		List<String> EnumList = new ArrayList<String>();
		EnumField e=new NewDriver().userTaskList.get(2).formPropertiesList.get(i).enumFields;
		RadioButtonGroup enumFieldBox = new RadioButtonGroup();
		enumFieldBox.setId(e.id);
		radioButtonFields.add(enumFieldBox);
	    Label textLabel=new Label(e.name);
	    textLabel.addStyleName(ValoTheme.LABEL_H3);
		
		String value;
	    
		for (String name: e.enumValues.keySet())
    	{						 
            String key =name.toString();
	            value = e.enumValues.get(name).toString(); 
	            //System.out.println("Key is: "+ key + " " + "Values is: "+ value);
	            EnumList.add(value);
   	    } 
		for(RadioButtonGroup radioButton: radioButtonFields)
		{
			radioButton.setSelectedItem(0);
		}

		}
	
		HashMap<String, String> myHashmap=new UserTaskInterface().getFormInput(new NewDriver().userTaskList.get(0));
		
		assertEquals(myHashmap.size(), 0);

	}

}
