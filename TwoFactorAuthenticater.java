package TwoFactorAuthenication;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class TwoFactorAuthenticater {
	//Variables:
	
		//Scanner Variable:
			private static Scanner scan;
		
		//Two-Factor Authentication Variables:
			private static SendEmail emailSender;
			private static String code;
			
			private static boolean timerLeft = false;
			private static Timer timer;
			
		//Encryption Variables:
			private static SecretKeySpec secretKey;
		    private static byte[] key; 
				    
	//Code:	
		//Setup of Two-Factor Authentication:
			
			//Sending Authentication Code to Email:
				@SuppressWarnings("static-access")
				private static void setCode(String email) {
					String tempStr = "";
					int size = (int)(Math.random() * 5) + 5;
					
					for(int i = 0; i < size; i++) {	
						tempStr += (int)(Math.random() * 9);						
					}
					
					System.out.println("");
					System.out.print("<Sending Two Factor Authentication Code>");
					System.out.println("");
					
					size = 0;
					emailSender.setRecievingEmail(email);
					emailSender.setCodeinMessage(tempStr);
					code = encrypt(tempStr) + "";
					tempStr = "";
					emailSender.send();
					timerLeft = true;
				}
			
			//Code Expiration After Timer Ends
				private static class endTimer extends TimerTask {
					public void run() {
						timerLeft = false;
						//System.out.println("<Two Factor Authentication Code Expired>");
						timer.cancel();				
					}
				}
			
			//Encryption of Code
				private static void setKey(String myKey){
			        MessageDigest sha = null;
			        try {
			            key = myKey.getBytes("UTF-8");
			            sha = MessageDigest.getInstance("SHA-1");
			            key = sha.digest(key);
			            key = Arrays.copyOf(key, 16); 
			            secretKey = new SecretKeySpec(key, "AES");
			        } 
			        catch (NoSuchAlgorithmException e) {
			            e.printStackTrace();
			        } 
			        catch (UnsupportedEncodingException e) {
			            e.printStackTrace();
			        }
			    }
			 
			    private static String encrypt(String stringToEncrypt) {
			      	String tempString = "";
			      	
			    	try{
			        	setKey("72326481293284401108842034198539392451084941600194496783808458318799062810509701539726558392262331743172702716433604299542050139101694021515092362407455385753357370058196404262504307389514936030179415112565098492699063627098500244484570348830135781923492297392976984852067185813543169940480327870627818783510761300120867493686768792571027637992271194422029509683521949179737737579380630368950306535984658086877230502110012087221064729220793691624820118087839015016286445852043272782336151416572938392163526841297060494177024167570138384420791955376900544000004324221839815655922283772328094550848560011");						
			            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			            tempString = Base64.getEncoder().encodeToString(cipher.doFinal(stringToEncrypt.getBytes("UTF-8")));
			            secretKey = null;
			            key = null;
			            return tempString;
			        } 
			        catch (Exception e){
			            System.out.println("Error while encrypting: " + e.toString());
			            tempString = "";
			        }
			    	
			        return tempString;
			    }
			
		//Activating TwoFactor Authentication Method:
			    
			    //Method:
				public static boolean activateTwoFactorAuthentication() {
					//Set Up Scanner:
						scan = new Scanner(System.in);
									
					//Two-Factor Authentication:
						System.out.print("Enter Valid Email Address: ");
						setCode(scan.nextLine().trim());
						System.out.println("");
						
						timer = new Timer();
						timer.schedule(new endTimer(), 1000 * 60 * 20);
						
						System.out.print("Enter Two-Factor Authentication Code: ");
						String tempCode = encrypt(scan.nextLine());
						System.out.println("");
						
						scan.close();
						timer.cancel();
						
						if(tempCode.trim().equals(code) && timerLeft) {
							System.out.println("<Two-Factor Authentication Passed>");
							return true;
						}else {
							System.out.println("<Error: Two-Factor Authentication Failed>");
							return false;
						}			
				}
}
