package AppGenFiles;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService 
{
	public void sendEmail(EmailServiceTask emailtask)
	{
		System.out.println();
		System.out.println("Sending Email...");
		System.out.println();
	
		String To=emailtask.to;
		String From=emailtask.from;
		String Cc=emailtask.cc;
		String Bcc=emailtask.bcc;
		String Subject=emailtask.subject;
		String Text=emailtask.text;
		
	   	final String username = "smartappfyp@gmail.com";
		final String password = "smartappfyp";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.required", "true");
		

		Session session = Session.getInstance(props,
		new javax.mail.Authenticator() 
		{
			protected PasswordAuthentication getPasswordAuthentication() 
			{
				return new PasswordAuthentication(username, password);
			}
		});

		try 
		{
			System.out.println("Sending Email .....");
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("sana.shah94.ss@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(To));
			message.addRecipient(Message.RecipientType.BCC, new InternetAddress(Bcc));
			message.addRecipient(Message.RecipientType.CC, new InternetAddress(Cc));
			message.setSubject(Subject);
			message.setText(Text);
			System.out.println("DOING");
			//Transport.send(message);
			System.out.println();
			System.out.println("Email Sent");
		} 
		catch (MessagingException e) 
		{
			e.printStackTrace();

			//throw new RuntimeException(e);
		}
		
		

		
	}

}
