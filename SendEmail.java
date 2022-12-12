package TwoFactorAuthenication;
import java.util.Properties;    
import javax.mail.*;    
import javax.mail.internet.*;    

class Mailer{  
    public static void send(String from,String password,String to,String sub,String msg){  
          //Create Properties Object    
	    	Properties props = new Properties();  
	        props.setProperty("mail.transport.protocol", "smtp");     
	        props.setProperty("mail.host", "smtp.gmail.com");  
	        props.put("mail.smtp.auth", "true");  
	        props.put("mail.smtp.port", "465"); //465 = Gmail Port  	    
	        props.put("mail.debug", "false"); //Hiding Email Contents
	        props.put("mail.smtp.socketFactory.port", "465");  
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
	        props.put("mail.smtp.socketFactory.fallback", "false");  
          
          //Setup Session   
	        Session session = Session.getDefaultInstance(props,    
	        new javax.mail.Authenticator() {    
	        protected PasswordAuthentication getPasswordAuthentication() {    
	        return new PasswordAuthentication(from,password);  
           }    
          });    
	        
          //Compose Message:    
          try {    
           MimeMessage message = new MimeMessage(session);    
           message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));    
           message.setSubject(sub);    
           message.setText(msg);    
           
           //Sending Message: 
           	Transport.send(message);    
           	//System.out.println("message sent successfully");    
          } catch (MessagingException e) {
        	  throw new RuntimeException(e);
          }    
             
    }  
}  

public class SendEmail{  
	//Email Properties Variables:
		private static String sendingEmail = "*insert configured email here*";
		private static String encryptedPassword = "*insert password of configured email here*";
		private static String recievingEmail = "";
		private static String emailSubject = "Your Two-Factor Authentication Code";
		private static String emailMessage = "";	
		private static String code = "234234234";
	
	//Code:
		public SendEmail() {
			
		}
		
		public static void setRecievingEmail(String newEmail) {
		    recievingEmail = newEmail;
		}
			
		public static void setMessage(String text) {
		    emailMessage = text;
		}
		
		public static void setCodeinMessage(String key) {
			code = key;
		}
		
		public static void send() {
			setMessage("Hi," + "\n" +
					   "Thank you for using two-factor authentication." + "\n" +
					   "Here's your authentication code:" + "\n" +
					   "                                                            " + code + "\n" +
					   "This code expires in 20 minutes, but you can generate another by logging in again." + "\n" +
					   "\n" +
					   "This email is sent automatically when you try to log into your account. If you haven't" + "\n" +
					   "logged in recently, someone else might be trying to access your account." + "\n" +
					   "\n" +
					   "Kind regards," + "\n" +
					   "*insert name here*" + "\n" +
					   "From the vanbestindustries security team"
					);
			
			Mailer.send(sendingEmail,encryptedPassword,recievingEmail,emailSubject, emailMessage);  
		}   
}