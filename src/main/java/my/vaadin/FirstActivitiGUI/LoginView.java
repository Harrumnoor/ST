package my.vaadin.FirstActivitiGUI;

import javax.servlet.http.Cookie;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import AppGenFiles.NewDriver;

public class LoginView 
{
	public TextField id=new TextField();
	public PasswordField pw = new PasswordField();
	
	public void creatLoginView()
	{
		VerticalLayout layout=new VerticalLayout();
		Label id_lbl=new Label("User Name");
		layout.addComponents(id_lbl,id);
		
		Label pw_lbl=new Label("Password");
		layout.addComponents(pw_lbl,pw);
		
		Button btn=new Button("Login");
		layout.addComponent(btn);
	//	btn.addClickListener(e -> this.buttonClicked());	
		
		//UI.getCurrent().setContent(layout);
	}
	
	public void buttonClicked()
	{
		String userName=id.getValue();
		String password=pw.getValue();
		//VaadinSession.getCurrent().setAttribute("user", userName);
		
		//VaadinSession.getCurrent().getSession() //To retrieve
		//System.out.println("current session: "+VaadinSession.getCurrent().getAttribute("user")); 
		
		if (password.equals(userName))
		{
			NewDriver mydriver=new NewDriver();
	    	mydriver.startUp();
			
		}
		
		else
		{
			creatLoginView();
		}
		
	}
			

}
