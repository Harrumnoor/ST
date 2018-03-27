package GUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import Activiti.OrderedBusinessFlow;
import AppGenFiles.EmailServiceTask;
import AppGenFiles.UserTask;
import my.vaadin.FirstActivitiGUI.EndView;
import my.vaadin.FirstActivitiGUI.EnumField;
public class UserTaskInterface 
{
	List <TextField> textBoxFields=new ArrayList<TextField>();
	public List <RadioButtonGroup> radioButtonFields=new ArrayList<RadioButtonGroup>();
	public static HashMap<String, String> enumIds_input=new HashMap<String, String>();
	//String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
	public void displayUserTaskForm(UserTask usertask)
	{
	//	String currentAssignee=VaadinSession.getCurrent().getAttribute("user").toString();
		String currentAssignee="Customer";
		WebPageGen w = new WebPageGen();
		Button title = new Button("Welcome " +currentAssignee);
		title.addStyleNames(ValoTheme.BUTTON_LARGE,ValoTheme.MENU_ITEM,ValoTheme.BUTTON_PRIMARY);
		CssLayout menu = new CssLayout(title);
		Button item1,item2,item3;
		for (OrderedBusinessFlow traverseObj: w.orderedFlowList)
		{
			if(traverseObj.assignee.equals(currentAssignee))
			{
				 item1 = new Button (traverseObj.source);
				if(traverseObj.source.equals(usertask.id))
				{
				
			       item1.addStyleNames(ValoTheme.BUTTON_LARGE,ValoTheme.BUTTON_FRIENDLY,ValoTheme.MENU_ITEM);
			       
				}
				else
				{
					 item1.addStyleNames(ValoTheme.BUTTON_BORDERLESS_COLORED,ValoTheme.MENU_ITEM,ValoTheme.BUTTON_LARGE);

				}
				
				 menu.addComponent(item1);
			}
		}
		
		
		

		menu.addStyleName(ValoTheme.MENU_ROOT);
		menu.setSizeFull();
		VerticalLayout layout=new VerticalLayout();	
		
		layout.setSizeFull();
		for (int i=0;i<usertask.formPropertiesList.size();i++)
		{	
			if(!usertask.formPropertiesList.get(i).type.equals("enum"))
			{
				Label textLabel=new Label(usertask.formPropertiesList.get(i).name);
			    textLabel.addStyleName(ValoTheme.LABEL_H3);
			    
				TextField textBox=new TextField();
				textBox.addStyleNames("background-color", "gray");
				textBox.setId(usertask.formPropertiesList.get(i).id);
				
				textBoxFields.add(textBox);
				
				layout.addComponents(textLabel, textBox);
			}
		
			else if (usertask.formPropertiesList.get(i).type.equals("enum"))
			{	
				List<String> EnumList = new ArrayList<String>();
				EnumField e=usertask.formPropertiesList.get(i).enumFields;
				RadioButtonGroup enumFieldBox = new RadioButtonGroup();
				enumFieldBox.setId(e.id);
				
				
				radioButtonFields.add(enumFieldBox);
			    Label textLabel=new Label(e.name);
			    textLabel.addStyleName(ValoTheme.LABEL_H3);
				layout.addComponent(textLabel);
				String value;
			    
				for (String name: e.enumValues.keySet())
	        	{						 
                    String key =name.toString();
       	            value = e.enumValues.get(name).toString(); 
       	            //System.out.println("Key is: "+ key + " " + "Values is: "+ value);
       	            EnumList.add(value);
	       	    } 
					 
				enumFieldBox.setItems(EnumList);
				layout.addComponent(enumFieldBox);
			}
		}
		
		Button submitBtn=new Button("Submit");
	    submitBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
	    layout.addComponent(submitBtn);
	    layout.setSizeFull();
		//submitBtn.addClickListener(e -> this.buttonPressed(usertask,null,1));	
		HorizontalLayout mainn = new HorizontalLayout(menu,layout);
		mainn.setHeight("135%");
		//HorizontalLayout main2 = new CssLayout(mainn,layout);
		//main2.setSizeFull();
		//UI.getCurrent().setContent(mainn);
	}
	
	
	public void buttonPressed (UserTask usertask, EmailServiceTask emailtask, int flag)
	{
		WebPageGen w=new WebPageGen();
		String destination="";
		HashMap<String, String> enumVariables_input=new HashMap<String, String>();
		
		if (flag==1) //If flag is 1 this is user task
		{
			w.updataTaskStatus(usertask.id,0);
			destination=(w.findTaskbySource(usertask.id)).destination;
			enumIds_input=getFormInput(usertask);
		}
		
		else
		{
			w.updataTaskStatus(emailtask.taskId,0);
			destination=(w.findTaskbySource(emailtask.taskId)).destination;
			
		}
		
		textBoxFields.clear();
		radioButtonFields.clear();
		
		if (destination.contains("exclusivegateway"))
		{
			if (usertask!=null)
			{
				String nextDestination=w.selectedExclusivePath(usertask, enumIds_input,0);
				if (nextDestination.equals("Not Found"))
				{
					String nextToNextDetination=(w.findTaskbySource(destination)).destination;
					//w.GenerateWebPage(nextToNextDetination);
				}
				
				else
				{
				//	w.GenerateWebPage(nextDestination);
				}
				
			}
			
			else //in case of service task
			{
				//Assumption: Next to next Destination can never have Parallel or Exclusive
				String nextToNextDetination=(w.findTaskbySource(destination)).destination;
				//w.GenerateWebPage(nextToNextDetination);
				
			}
			
			//w.exclusiveInDestination(destination);
		}
		
		else if (destination.contains("parallelgateway"))
		{
			w.parallelInDestination(destination,0);
		}
		
		else if (destination.contains("endevent"))
		{
			//new EndView();
		}
		
		else
		{
			//w.GenerateWebPage(destination);
		}

		
	}

	
	


	public HashMap<String, String> getFormInput(UserTask usertask)
	{
		HashMap<String, String> enumVariables_input=new HashMap<String, String>();
		
		/*for (RadioButtonGroup radioButton: radioButtonFields)
	    {
			String v=radioButton.getSelectedItem().toString();
			String selectedRadioBtn=v.substring(v.indexOf("[")+1, v.indexOf("]"));
			
			enumVariables_input.put(radioButton.getId(), selectedRadioBtn);
		    System.out.println(radioButton.getId() +" "+ selectedRadioBtn);
	    }
		*/
		return enumVariables_input;
	}
}