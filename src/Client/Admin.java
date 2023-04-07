package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AtwaterServer.AtwaterInterface;
import OutremontServer.OutremontInterface;
import VerdunServer.VerdunInterface;

public class Admin {

	public void adminMethod(String userID, AtwaterInterface atwaterInterface, VerdunInterface verdunInterface,OutremontInterface outremontInterface) throws NumberFormatException, IOException 
	{
		String movieName;
		String movieID;
		String date;
		String timeSlot;
		String theatreName;
		String afterAddingSlots;
		String afterRemovingSlots;
		int bookingCapacity;
		String customerMovieList="";
		String customerMovieListRemoved;
		int noOfTickets;
		int option1;
		String result;
		String specificMovieListAllServer;
		String customerID;
		//int slot;
		String campuss=userID.substring(0,3);
	    BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
	    Logger logger= Logger.getLogger("Admin");
		FileHandler fh;
		fh=new FileHandler("F:/WebService/Assignment03/Admin.log", true);
		LogManager.getLogManager().reset();
		logger.addHandler(fh);
		 
		fh.setFormatter(new SimpleFormatter());
					
		System.out.println("Enter '1' To add movie slots \nEnter '2' To remove movie slots \nEnter '3' To get list of movie show availability \nEnter '4' To book movie on behalf of a customer \nEnter '5' To cancel movie ticket on behalf of a customer \nEnter '6' To get booking schedule on behalf of a customer");
		option1 = Integer.parseInt(br.readLine());
    	switch(option1)
    	{
    	case 1:
    	{ 
    		System.out.println("Enter one after another - \nTheatre name - 'ATW'/'VER'/'OUT \nMovie name,\nDate(DDMMYY), \nSelect 'M' for 'Morning Slot', 'E' for 'Evening Slot', 'A' for 'Afternoon Slot ,\nBooking capacity");
    		theatreName=br.readLine();
    		movieName=br.readLine();
    		date=br.readLine();
    		timeSlot=br.readLine();
    		bookingCapacity=Integer.parseInt(br.readLine());
    		if(theatreName.equals(new String("ATW")))
    		{		
    			movieID = theatreName.trim() + timeSlot.trim() + date.trim();
    			System.out.println(movieID);
    			afterAddingSlots=atwaterInterface.addMovieSlots(movieID, movieName, bookingCapacity);
    			System.out.println(afterAddingSlots);
    			try {
    				
    				logger.info(afterAddingSlots);
    				
    			}catch(Exception e){
    				logger.log(Level.WARNING,"Exception: "+e);
    				
    			}
    		}
    		else if(theatreName.equals(new String("VER")))
    		{		
    			movieID = theatreName.trim() + timeSlot.trim() + date.trim();
    			afterAddingSlots=verdunInterface.addMovieSlots(movieID, movieName, bookingCapacity);
    			System.out.println(afterAddingSlots);
                try {
    				
    				logger.info(afterAddingSlots);
    				
    			}catch(Exception e){
    				logger.log(Level.WARNING,"Exception: "+e);
    				
    			}
    		}
    		else if(theatreName.equals(new String("OUT")))
    		{		
    			movieID = theatreName.trim() + timeSlot.trim() + date.trim();
    			afterAddingSlots=outremontInterface.addMovieSlots(movieID, movieName, bookingCapacity);
    			System.out.println(afterAddingSlots);
                try {
    				
    				logger.info(afterAddingSlots);
    				
    			}catch(Exception e){
    				logger.log(Level.WARNING,"Exception: "+e);
    				
    			}
    		}
    		break;
    	}
    	case 2:
    	{
    		System.out.println("Enter one after another- \nTheatre name - 'ATW'/'VER'/'OUT \nMovie name \nMovieID");
    		theatreName=br.readLine();
    		movieName=br.readLine();
    		movieID=br.readLine();
    		if(theatreName.equals(new String("ATW")))
    		{	
				afterRemovingSlots=atwaterInterface.removeMovieSlots(movieID, movieName);
				System.out.println(afterRemovingSlots);
                 try {
    				
    				logger.info(afterRemovingSlots);
    				
    			}catch(Exception e){
    				logger.log(Level.WARNING,"Exception: "+e);
    				
    			}
    		} 
    		else if(theatreName.equals(new String("VER")))
    		{	
				afterRemovingSlots=verdunInterface.removeMovieSlots(movieID, movieName);
				System.out.println(afterRemovingSlots);
                try {
    				
    				logger.info(afterRemovingSlots);
    				
    			}catch(Exception e){
    				logger.log(Level.WARNING,"Exception: "+e);
    				
    			}
    		} 
    		else if(theatreName.equals(new String("OUT")))
    		{	
				afterRemovingSlots=outremontInterface.removeMovieSlots(movieID, movieName);
				System.out.println(afterRemovingSlots);
                try {
    				
    				logger.info(afterRemovingSlots);
    				
    			}catch(Exception e){
    				logger.log(Level.WARNING,"Exception: "+e);
    				
    			}
    		} 
			
    		break;
    	}
    	case 3:
    	{
    		System.out.println("Enter one after another- \nTheatre name - 'ATW'/'VER'/'OUT \nMovie name");
    		theatreName=br.readLine();
    		movieName=br.readLine();
    		if(theatreName.equals(new String("ATW")))
    		{	
    			specificMovieListAllServer=atwaterInterface.listMovieShowsAvailability(movieName);
    			System.out.println("The  number of tickets available for "+ movieName +" movie shows in all the servers :  \n" + specificMovieListAllServer);
                try {
    				
    				logger.info(specificMovieListAllServer);
    				
    			}catch(Exception e){
    				logger.log(Level.WARNING,"Exception: "+e);
    				
    			}
    		}
    		else if(theatreName.equals(new String("VER")))
    		{	
    			specificMovieListAllServer=verdunInterface.listMovieShowsAvailability(movieName);
    			System.out.println("The  number of tickets available for "+ movieName +" movie shows in all the servers :  \n" + specificMovieListAllServer);
    			try {
     				
     				logger.info(specificMovieListAllServer);
     				
     			}catch(Exception e){
     				logger.log(Level.WARNING,"Exception: "+e);
     				
     			}
    		}
    		else if(theatreName.equals(new String("OUT")))
    		{	
    			specificMovieListAllServer=outremontInterface.listMovieShowsAvailability(movieName);
    			System.out.println("The  number of tickets available for "+ movieName +" movie shows in all the servers :  \n" + specificMovieListAllServer);
    			try {
     				
     				logger.info(specificMovieListAllServer);
     				
     			}catch(Exception e){
     				logger.log(Level.WARNING,"Exception: "+e);
     				
     			}
    		}
    		
    		break;
    	}
    	
    	case 4:
    	{
    		System.out.println("Enter one after another- \nThe customer Id for whom you want to book movie ticket \nTheatre name - 'ATW'/'VER'/'OUT \nMovie name \nMovieID \nNo. of tickets you want to book");
    	    customerID=br.readLine();
    	    theatreName=br.readLine();
    		movieName=br.readLine();
    		movieID=br.readLine();
    		noOfTickets= Integer.parseInt(br.readLine());
    		if(theatreName.equals(new String("ATW")))
    		{	
    			customerMovieList = atwaterInterface.bookMovieTickets(customerID, movieName, movieID,noOfTickets);  
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
    	case 5:
    	{
    		System.out.println("Enter one after another- \nThe customer Id for whom you want to book movie ticket \nTheatre name - 'ATW'/'VER'/'OUT' \nMovie name \nMovieID \nNo. of tickets you want to cancel");
    		customerID=br.readLine();
    		theatreName=br.readLine();
    		movieName=br.readLine();
    		movieID=br.readLine();
    		noOfTickets= Integer.parseInt(br.readLine());
    		if(theatreName.equals(new String("ATW")))
    		{	
    			customerMovieListRemoved = atwaterInterface.cancelMovieTickets(customerID, movieName, movieID,noOfTickets );
    			System.out.println(customerMovieListRemoved.toString());
    		}
    		else if(theatreName.equals(new String("VER")))
    		{	
    			customerMovieListRemoved = verdunInterface.cancelMovieTickets(customerID, movieName, movieID,noOfTickets );
    			System.out.println(customerMovieListRemoved.toString());
    		}
    		else if(theatreName.equals(new String("OUT")))
    		{	
    			customerMovieListRemoved = outremontInterface.cancelMovieTickets(customerID, movieName, movieID,noOfTickets );
    			System.out.println(customerMovieListRemoved.toString());
    		}
    		else {
    			System.out.println("Put correct theatre name");
    		}
    		
    		break;
    	}
    	case 6:
    	{
    		System.out.println("Enter one after another- \nThe customer Id for whom you want to get booking schedule \nTheatre name - 'ATW'/'VER'/'OUT'");
    		customerID=br.readLine();
    		theatreName=br.readLine();
    		if(theatreName.equals(new String("ATW")))
    		{	
    			result = atwaterInterface.getBookingSchedule(customerID);
    			System.out.println("RESULT IS:" +result);
    			
    		}
    		else if(theatreName.equals(new String("VER")))
    		{	
    			result = verdunInterface.getBookingSchedule(customerID);
    			System.out.println("RESULT IS:" +result);
    		}
    		else if(theatreName.equals(new String("OUT")))
    		{	
    			result = outremontInterface.getBookingSchedule(customerID);
    			System.out.println("RESULT IS:" +result);
    		}
    		else {
    			System.out.println("Put correct theatre name");
    		}
    		break;
    	}

		}
	}
}
