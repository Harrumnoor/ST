package my.vaadin.FirstActivitiGUI;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class EndView 
{
	public EndView()
	{
		final VerticalLayout layout = new VerticalLayout();
		
		layout.addComponent(new Label("User Task Activity has ENDED!!! Bye :)"));
	   //	UI.getCurrent().setContent(layout);
	   	 
	}
}
