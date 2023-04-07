package Client;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.rmi.NotBoundException;
import java.util.Random;
import java.util.Scanner;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import AtwaterServer.AtwaterInterface;
import VerdunServer.VerdunInterface;
import OutremontServer.OutremontInterface;
import Frontend.FrontEndClient;



public class Client {
	public static void main(String[] args) throws NotBoundException, NumberFormatException, IOException 
	{
		 {
				String userID;
				String user;
				String region;
				int input;
				Admin admin;
				Customer customer;
				
				URL url1=new URL("http://localhost:8081/WS/Atwater?wsdl");
				QName qname1=new QName("http://AtwaterServer/","AtwaterOperationsService");
				Service service1=Service.create(url1,qname1);
				AtwaterInterface atwaterInterface= service1.getPort(AtwaterInterface.class);
				
				
				URL url2=new URL("http://localhost:8082/WS/Verdun?wsdl");
				QName qname2=new QName("http://VerdunServer/","VerdunOperationsService");
				Service service2=Service.create(url2,qname2);
				VerdunInterface verdunInterface= service2.getPort(VerdunInterface.class);
				
				
				URL url3=new URL("http://localhost:8083/WS/Outremont?wsdl");
				QName qname3=new QName("http://OutremontServer/","OutremontOperationsService");
				Service service3=Service.create(url3,qname3);
			    OutremontInterface outremontInterface= service3.getPort(OutremontInterface.class);

				//Taking input
				BufferedReader br= new BufferedReader(new InputStreamReader(System.in));

				
		        while(true)
		        {	System.out.println("Please enter '1' For operaton of the system \nPlease enter '2' If you want to exit from the system");
					
		        
		        //input given by the user
		            input=Integer.parseInt(br.readLine());

					switch(input)
					{    
						case 1:
						{    
							System.out.println("Please enter the User ID");
				
							userID=br.readLine();
							if(userID.charAt(3)=='C')
							{
								//create object of Customer class
								customer = new Customer();
								customer.customerMethod(userID,atwaterInterface,verdunInterface,outremontInterface);
							}

							else if(userID.charAt(3)=='A')
							{
								//create object of Admin class
								
								admin= new Admin();
								admin.adminMethod(userID, atwaterInterface, verdunInterface,outremontInterface);
							}
							else
							{
								try (Scanner scanner1 = new Scanner(System.in);
									Scanner scanner2 = new Scanner(System.in)) {
									Random rand = new Random();
									System.out.println("If don't have UserID, mention 'A' if ypu are admin or 'C if you are a normal customer");
									user=scanner1.next();
									System.out.println("If you are from 'Atwater' press 'ATW' or 'Verdun' press 'VER' OR 'Outremont' press 'OUT'");
									region=scanner2.next();
									userID= region.trim().toUpperCase()+user.trim().toUpperCase()+ rand.nextInt((9999 - 100) + 1) + 10+"";
									System.out.println("Your userID is " + userID);
									System.out.println("Now please enter this UserID");
									userID=br.readLine();
								}
								
							}
							break;
						}
						case 2:
						{
							System.exit(0);
							break;
						}
					}
		        }
			}
		
	}

}
