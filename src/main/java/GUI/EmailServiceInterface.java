package GUI;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import AppGenFiles.EmailService;
import AppGenFiles.EmailServiceTask;

public class EmailServiceInterface 
{
	public List <TextField> textBoxFields=new ArrayList<TextField>();
	public TextField to_field=new TextField();
	public TextField from_field=new TextField();
	public TextField bcc_field=new TextField();
	public TextField cc_field=new TextField();
	public TextField subject_field=new TextField();
	public TextField text_field=new TextField();
	public void displayEmailForm(EmailServiceTask emailTask)
	{
		System.out.println("\t\t\t****************** SERVICE TASK email tasssk class mai " + emailTask.taskId);

		VerticalLayout layout=new VerticalLayout();
		
		Label taskName=new Label(emailTask.taskName);
		layout.addComponent(taskName);
		
		Label to_lbl=new Label("To");
		to_field.setId("to_field");
		to_field.setWidth("260");
		layout.addComponents(to_lbl, to_field);
		
		Label from_lbl=new Label("From");
		from_field.setId("from_field");
		from_field.setWidth("260");
		layout.addComponents(from_lbl, from_field);
		
		Label bcc_lbl=new Label("BCC");
		bcc_field.setId("bcc_field");
		bcc_field.setWidth("260");
		layout.addComponents(bcc_lbl, bcc_field);
		
		Label cc_lbl=new Label("CC");
		cc_field.setId("cc_field");
		cc_field.setWidth("260");
		layout.addComponents(cc_lbl, cc_field);
		
		Label subject_lbl=new Label("Subject");
		subject_field.setId("subject_field");
		subject_field.setWidth("700");
		layout.addComponents(subject_lbl, subject_field);
		
		Label text_lbl=new Label("Text");
		text_field.setId("text_field"); 
		text_field.setWidth("700");
		text_field.setHeight("400");;
		layout.addComponents(text_lbl, text_field);
		
		Button submitBtn=new Button("Send Email");
		//submitBtn.addClickListener(e -> this.emailButton(emailTask));	
		layout.addComponent(submitBtn);
		//UI.getCurrent().setContent(layout);
		
	}
	
	public void emailButton(EmailServiceTask emailtask)
	{
		emailtask.to=to_field.getValue();
		emailtask.from=from_field.getValue();
		emailtask.bcc=bcc_field.getValue();
		emailtask.cc=cc_field.getValue();
		emailtask.subject=subject_field.getValue();
		emailtask.text=text_field.getValue();
		
		EmailService e=new EmailService();
		e.sendEmail(emailtask);
		
		to_field.clear();
		from_field.clear();
		bcc_field.clear();
		cc_field.clear();
		subject_field.clear();
		text_field.clear();
		
		new UserTaskInterface().buttonPressed(null, emailtask, 0);
		

	}

	
}
