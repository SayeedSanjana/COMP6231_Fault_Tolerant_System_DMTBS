package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AtwaterServer.AtwaterInterface;
import VerdunServer.VerdunInterface;
import OutremontServer.OutremontInterface;


public class Customer {

	public void customerMethod(String userID, AtwaterInterface atwaterInterface, VerdunInterface verdunInterface, OutremontInterface outremontInterface) throws IOException, NotBoundException
	{
		String movieName;
		String movieID = null;
		String theatreName;
		String customerMovieList="";
		String customerMovieListRemoved;
		String newMovie;
		String newMovieID;
		String exchangeMovie;
		String result;
		int noOfTickets;
		int option1;
		
		Logger logger= Logger.getLogger("Customer");
		FileHandler fh;
		fh=new FileHandler("F:/WebService/Assignment03/Customer.log", true);
		LogManager.getLogManager().reset();
		logger.addHandler(fh);
		 
		fh.setFormatter(new SimpleFormatter());

	    BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
					
		System.out.println("Enter '1' To book movie tickets in your own area \nEnter '2' To cancel movie tickets \nEnter '3' To get booking schedule \nEnter '4' To exchange tickets");

		
					option1 = Integer.parseInt(br.readLine());
		        	switch(option1)
		        	{
		        	case 1:
		        	{
		        		System.out.println("Enter one after another- \nTheatre name - 'ATW'/'VER'/'OUT \nMovie name \nMovieID \nNo. of tickets you want to book");
		        	    theatreName=br.readLine();
		        		movieName=br.readLine();
		        		movieID=br.readLine();
		        		noOfTickets= Integer.parseInt(br.readLine());
		        		if(theatreName.equals(new String("ATW")))
		        		{	
		        			
		        			customerMovieList = atwaterInterface.bookMovieTickets(userID, movieName, movieID,noOfTickets);  
		        			System.out.println(customerMovieList.toString());
		        		}
		        		else if(theatreName.equals(new String("VER")))
		        		{	
		        			customerMovieList = verdunInterface.bookMovieTickets(userID, movieName, movieID,noOfTickets);  
		        			System.out.println(customerMovieList.toString());
		        		}
		        		else if(theatreName.equals(new String("OUT")))
		        		{	
		        			customerMovieList = outremontInterface.bookMovieTickets(userID, movieName, movieID,noOfTickets);  
		        			System.out.println(customerMovieList.toString());
		        		}
		        		else {
		        			System.out.println("Put correct theatre name");
		        		}
		        		break;
		        	}
		        	case 2:
		        	{
		        		System.out.println("Enter one after another- \nTheatre name - 'ATW'/'VER'/'OUT' \nMovie name \nMovieID \nNo. of tickets you want to cancel");
		        		theatreName=br.readLine();
		        		movieName=br.readLine();
		        		movieID=br.readLine();
		        		noOfTickets= Integer.parseInt(br.readLine());
		        		if(theatreName.equals(new String("ATW")))
		        		{	
		        			customerMovieListRemoved = atwaterInterface.cancelMovieTickets(userID, movieName, movieID,noOfTickets );
		        			System.out.println(customerMovieListRemoved.toString());
		        		}
		        		else if(theatreName.equals(new String("VER")))
		        		{	
		        			customerMovieListRemoved = verdunInterface.cancelMovieTickets(userID, movieName, movieID,noOfTickets );
		        			System.out.println(customerMovieListRemoved.toString());
		        		}
		        		else if(theatreName.equals(new String("OUT")))
		        		{	
		        			customerMovieListRemoved = outremontInterface.cancelMovieTickets(userID, movieName, movieID,noOfTickets );
		        			System.out.println(customerMovieListRemoved.toString());
		        		}
		        		else {
		        			System.out.println("Put correct theatre name");
		        		}
       		
		        		break;
		        	}
		        	case 3:
		        	{
		        		System.out.println("Enter one after another- \nTheatre name - 'ATW'/'VER'/'OUT'");
		        		theatreName=br.readLine();
		        		if(theatreName.equals(new String("ATW")))
		        		{	
		        			result = atwaterInterface.getBookingSchedule(userID);
		        			System.out.println("RESULT IS:" +result);
		        			
		        		}
		        		else if(theatreName.equals(new String("VER")))
		        		{	
		        			result = verdunInterface.getBookingSchedule(userID);
		        			System.out.println("RESULT IS:" +result);
		        		}
		        		else if(theatreName.equals(new String("OUT")))
		        		{	
		        			result = outremontInterface.getBookingSchedule(userID);
		        			System.out.println("RESULT IS:" +result);
		        		}
		        		else {
		        			System.out.println("Put correct theatre name");
		        		}	
		        		break;
		        	}
		        	case 4:
		        	{
		        		System.out.println("Enter one after another- \nTheatre name - 'ATW'/'VER'/'OUT \nOld Movie name \nOld MovieID  \nNew MovieID \nNew Movie name  \nNo. of tickets you want to book");
		        		theatreName=br.readLine();
		        		movieName=br.readLine();
		        		movieID=br.readLine();
		        		newMovieID=br.readLine();
		        		newMovie=br.readLine();
		        		noOfTickets= Integer.parseInt(br.readLine());
		        		
		        		if(theatreName.equals(new String("ATW")))
		        		{	
		        			exchangeMovie =atwaterInterface.exchangeTickets(userID, movieName, movieID, newMovieID, newMovie, noOfTickets);
		        			System.out.println(exchangeMovie);
		        			try {
		        				logger.info(exchangeMovie);
		        				
		        			}catch(Exception e){
		        				logger.log(Level.WARNING,"Exception: "+e);
		        				
		        			}
		        		}
		        		if(theatreName.equals(new String("VER")))
		        		{	
		        			exchangeMovie =verdunInterface.exchangeTickets(userID, movieName, movieID, newMovieID, newMovie, noOfTickets);
		        			System.out.println(exchangeMovie);
		        			try {
		        				logger.info(exchangeMovie);
		        				
		        			}catch(Exception e){
		        				logger.log(Level.WARNING,"Exception: "+e);
		        				
		        			}
		        		}
		        		if(theatreName.equals(new String("OUT")))
		        		{	
		        			exchangeMovie =outremontInterface.exchangeTickets(userID, movieName, movieID, newMovieID, newMovie, noOfTickets);
		        			System.out.println(exchangeMovie);
		        			try {
		        				logger.info(exchangeMovie);
		        				
		        			}catch(Exception e){
		        				logger.log(Level.WARNING,"Exception: "+e);
		        				
		        			}
		        		}
		        		break;
		        	}
//		        	
		}
	}

}
