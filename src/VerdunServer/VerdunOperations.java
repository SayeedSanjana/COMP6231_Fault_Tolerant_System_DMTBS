package VerdunServer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import java.util.Map.Entry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;

@WebService(endpointInterface="VerdunServer.VerdunInterface")
@SOAPBinding(style=Style.RPC)
public class VerdunOperations implements VerdunInterface {

	public static HashMap<String,HashMap<String,Integer>> movieList;
    public static HashMap<String,HashMap<String,HashMap<String,Integer>>> customerMovieList;
	
	@SuppressWarnings("static-access")
	public VerdunOperations()
	{
		super();
		movieList = new HashMap<>();
		this.movieList.put("Avatar" , new HashMap<String,Integer>());
		this.movieList.put("Avatar" , new HashMap<String,Integer>());
		this.movieList.put("Titanic" , new HashMap<String,Integer>());
		this.movieList.put("Titanic" , new HashMap<String,Integer>());
		this.movieList.put("Avengers" , new HashMap<String,Integer>());
		this.movieList.put("Titanic" , new HashMap<String,Integer>());
		
		this.movieList.get("Avatar").put("VERM090323", 10);
		this.movieList.get("Avatar").put("VERA240223", 10);
		this.movieList.get("Titanic").put("VERE201123", 6);
		
		//Exchange ticket
		this.movieList.get("Titanic").put("VERE230323", 6);
		
		
		this.movieList.get("Avengers").put("VERM080323", 7);
		//slot clash
		this.movieList.get("Titanic").put("VERM210323", 7);
		
		customerMovieList= new HashMap<>();
		this.customerMovieList.put("VERC1234" , new HashMap<String,HashMap<String,Integer>>());
		this.customerMovieList.put("VERC2456" , new HashMap<String,HashMap<String,Integer>>());
		this.customerMovieList.put("ATWC1234" , new HashMap<String,HashMap<String,Integer>>());
		
		
		this.customerMovieList.get("VERC1234").put("Avatar", new HashMap<String,Integer>());
		this.customerMovieList.get("VERC2456").put("Avengers", new HashMap<String,Integer>());
		this.customerMovieList.get("ATWC1234").put("Titanic", new HashMap<String,Integer>());
		
		
		
		//slot clash
		this.customerMovieList.get("VERC1234").get("Avatar").put("VERM090323",1);
		this.customerMovieList.get("VERC2456").get("Avengers").put("VERM080323",1);
		this.customerMovieList.get("ATWC1234").get("Titanic").put("VERM210323",1);
	}

	private Date movieIdDateConverter(String movieID) {
		movieID = movieID.substring(4, movieID.length());
		movieID = movieID.substring(0,2) + "/" + movieID.substring(2,4) + "/20" + movieID.substring(4);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date format = null;
		try {
			format = formatter.parse(movieID);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return format;
	}

	@SuppressWarnings("unchecked")
	
	public String bookMovieTickets(String userID,String movieName, String movieID , int noOfTickets) {
		String msg="";


		//return c1;
		// check if the movie exists in the movie list
		if(!movieList.containsKey(movieName))
		{
			msg="Movie does not exist in the movie list";
			return msg;
		}
		// if the movie exists then check if it has the movie id
		if(!movieList.get(movieName).containsKey(movieID)) 
		{
			msg="Movie ID does not exist";
			return msg;
		}
		int capacity= movieList.get(movieName).get(movieID);
		// provided movie id exists, check if there is enough capacity
		if(capacity<=0 || capacity< noOfTickets)
		{
			msg="Capacity for this movie slot is full";
			return msg;
		}
		// check the region of the customer
        //if !local && noOfTickets>=3
		String slicedID = userID.substring(0,3) ;
		if (!slicedID.equals("VER") && noOfTickets>3){
			msg="You cannot book more than 3 tickets";
			return msg;
		}
		// check slot for both local and !local
        // if slot conflicts throw err
		Gson gson = new Gson();
		String json = this.getBookingSchedule(userID);
		//System.out.println(json);
		String splitMovieID= movieID.substring(3, movieID.length());
		if(!json.equals("NoListFound")) {
			JsonObject jsonObj = gson.fromJson(json, JsonObject.class);
			if(jsonObj.has("Atwater")) {
				JsonObject jsonAtwater = jsonObj.getAsJsonObject("Atwater");
				System.out.println(jsonAtwater);
				//Checked for Verdun
				if(jsonAtwater.has(movieName)) 
				{
					for (Entry<String, JsonElement> item : jsonAtwater.getAsJsonObject(movieName).entrySet()) {
						String slot = item.getKey().substring(3, item.getKey().length());
						if(splitMovieID.equals(slot)) {
							return "You already have a booking in this slot, so cannot book ticket";
						}
					}
				}
			}
			
			if(jsonObj.has("Outremont")) {
				JsonObject jsonOutremont = jsonObj.getAsJsonObject("Outremont");
				System.out.println(jsonOutremont);
				//Checked for outremont
				if(jsonOutremont.has(movieName)) 
				{
					for (Entry<String, JsonElement> item : jsonOutremont.getAsJsonObject(movieName).entrySet()) {
						String slot = item.getKey().substring(3, item.getKey().length());
						if(splitMovieID.equals(slot)) {
							return "You already have a booking in this slot, so cannot book ticket";
						}
					}
				}
			}
			
		}
		
		// check if customer is not local then count customer's max booking in the bookingList.
		//		maxBooking+noOfTickets > 3 throw err
		int maxBooking=0;
		for(Map.Entry<String, HashMap<String,HashMap<String,Integer>>> i : customerMovieList.entrySet())
		{
			if (i.getKey().contains(userID) && !slicedID.equals("VER")) 
			{
				for(Map.Entry<String,HashMap<String,Integer>> j : i.getValue().entrySet()) {
					for(Map.Entry<String,Integer> k : j.getValue().entrySet()) {
						maxBooking= maxBooking+ k.getValue()+noOfTickets;
					}
				}
			}
		}
		if(!slicedID.equals("VER") && maxBooking>3) {
			return "You have already exceeded the limit of booking 3 tickets";	
		}
		//	start booking from here
		
		// check if the customer does not exist then book the movie
		//Customer Not in the Booking List
		if(!customerMovieList.containsKey(userID)) {
			customerMovieList.put(userID, new HashMap<String,HashMap<String,Integer>>());
			customerMovieList.get(userID).put(movieName, new HashMap<String,Integer>());
			customerMovieList.get(userID).get(movieName).put(movieID,noOfTickets);
			movieList.get(movieName).put(movieID, capacity - noOfTickets);
			msg="Booked successfully";
			System.out.println("Capacity reduced after the movie got booked by the customer" + movieList.toString());
			System.out.println("New booking added" + customerMovieList.toString());
			return msg;
		}
		//Customer Already in the booking list
		else {
			//Customer contains Movie Contains
			if(customerMovieList.get(userID).containsKey(movieName)) {
				//Movie ID contains
				if(customerMovieList.get(userID).get(movieName).containsKey(movieID)) {
					int NoOfAlreadyBookedTickets= customerMovieList.get(userID).get(movieName).get(movieID);
					customerMovieList.get(userID).get(movieName).put(movieID,noOfTickets + NoOfAlreadyBookedTickets);
		    		movieList.get(movieName).put(movieID, capacity-noOfTickets);
		    		msg="Booked successfully";
		    		System.out.println("Capacity reduced after the movie got booked by the customer" + movieList.toString());
					System.out.println("No of booking ticket increased after booking the same show" + customerMovieList.toString());
					return msg;	
				}
				//Movie ID does not contain
				else {
					customerMovieList.get(userID).get(movieName).put(movieID,noOfTickets);
	    			 movieList.get(movieName).put(movieID, capacity-noOfTickets);
	    			 msg="Booked successfully";
	    			 System.out.println("Capacity reduced after the movie got booked by the customer" + movieList.toString());
					 System.out.println("Movie booked for new Movie ID" + customerMovieList.toString());
					 return msg;
				}
			}
			//Customer contains Movie Doesnot Contain
			else {
				customerMovieList.get(userID).put(movieName, new HashMap<String,Integer>());
    			customerMovieList.get(userID).get(movieName).put(movieID,noOfTickets);	
    			movieList.get(movieName).put(movieID, capacity-noOfTickets);
    			msg="Booked successfully";
    			System.out.println("Capacity reduced after the movie got booked by the customer" + movieList.toString());
				System.out.println("New movie booked for a new movie" + customerMovieList.toString());
				return msg;
			}	
		}
		
		//return msg;
	}


	@Override
	public String addMovieSlots(String movieID, String movieName, int bookingCapacity) {
		// TODO Auto-generated method stub
				String message="";
				boolean isWithinWeek;
				//Check if within the week
				
				String ID = movieID = movieID.substring(4, movieID.length());
				ID = movieID.substring(0,2) + "/" + movieID.substring(2,4) + "/20" + movieID.substring(4);
				System.out.println(ID);
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date nDate;
				    try {
						nDate = formatter.parse(ID); 
					    Calendar currentCalendar = Calendar.getInstance();
					    int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
					    int year = currentCalendar.get(Calendar.YEAR);
					    Calendar targetCalendar = Calendar.getInstance();
					    targetCalendar.setTime(nDate);
					    int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
					    int targetYear = targetCalendar.get(Calendar.YEAR);
					    if(week == targetWeek && year == targetYear) {
					    	isWithinWeek=true;
					    }
					    else {
					    	isWithinWeek=false;
					    }
					    
					    if(!isWithinWeek) 
					    {
					    	message="You can add movie for this week only";
					    	return message;
					    }
					    if(!movieList.containsKey(movieName)) {
							movieList.put(movieName , new HashMap<String,Integer>());
							movieList.get(movieName).put(movieID, bookingCapacity);
							System.out.println("New slots added sucessfully for the new movie");
							message= "New slots added sucessfully for the new movie";
						}
						else {
							if(movieList.get(movieName).containsKey(movieID)) {
								int capacity = movieList.get(movieName).get(movieID);
								movieList.get(movieName).put(movieID, capacity+bookingCapacity);
								System.out.println("Booking capacity successully increased for existing movieID and movieName");
								message="Booking capacity successully increased for existing movieID and movieName";
							}
							else {
							movieList.get(movieName).put(movieID, bookingCapacity);
							System.out.println("New slots added sucessfully for the existing movie");
							message="New slots added sucessfully for the existing movie";
							}

						}
						System.out.println("Movie Slot : " + movieList.toString());	
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		return message;
	}

	@Override
	public String removeMovieSlots(String movieID, String movieName) {
		// TODO Auto-generated method stub
		String msg="";
		if(movieList.containsKey(movieName)){
			if(movieList.get(movieName).containsKey(movieID)) {

				//Check if that movie exists in customer movie list
				for(Map.Entry<String, HashMap<String,HashMap<String,Integer>>> i : customerMovieList.entrySet()) {

					//if movie contains in customer booking list
					if(i.getValue().containsKey(movieName) && i.getValue().get(movieName).containsKey(movieID) ){
						System.out.println("in");
						String date = movieID.substring(4, movieID.length());
						date= date.substring(0,2) + "/" + date.substring(2,4) + "/20" + date.substring(4);
						SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
						Date nDate;
						try {
							nDate = formatter.parse(date);
							System.out.println(nDate.after(new Date()));
							if(nDate.after(new Date())){
								System.out.println("date greater than current date");
								HashMap<String,Integer> temp = movieList.get(movieName);
								// string to check date and integer to check capacity for the next date
								for(Map.Entry<String,Integer> entry: temp.entrySet()) {
									String key = entry.getKey();
									int capacity = entry.getValue();
									Date iDate = movieIdDateConverter(key);
									// check the list date if it is after the deleting date and also check capacity is less than or equal to the tickets booked
									if(iDate.after(nDate) && capacity >= i.getValue().get(movieName).get(movieID)) {
										//take the key and place it in the customer movie list

										i.getValue().get(movieName).put(key, i.getValue().get(movieName).get(movieID));

										// finally update the value of the move list capacity
										movieList.get(movieName).put(key,capacity-i.getValue().get(movieName).get(movieID));

										//Removing the old value from the customer movie list
										i.getValue().get(movieName).remove(movieID);
										break;
									}
								}
								movieList.get(movieName).remove(movieID);
								System.out.println("Movie slot cancelled successfully");
							}else {
								msg="Old Date- Cant Delete";
								System.out.println("Old Date- Cant Delete");
								return msg;
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}else {
						movieList.get(movieName).remove(movieID);
						msg="Deleted Succcessfully";
						System.out.println("Deleted Successfully");
					}
				}
			}else {
				msg="Cant delete as movieID does not exist";
				System.out.println("Cant delete as movieID does not exist");
			}
		}
		else {
			msg="Cant delete as movie does not exist";
			System.out.println("Cant delete as movie does not exist");
		}
		System.out.println("Customer Booking list: " + customerMovieList.toString());
		System.out.println("Move List: " + movieList.toString());
		return msg;
	}

	@Override
	public String listMovieShowsAvailability(String movieName) {
		String methodName="listMovieShowsAvailability";
		Sender1 s1= new Sender1(movieName,methodName,"ATW");
		Thread  t1=new Thread(s1);
		
		Sender1 s2= new Sender1(movieName,methodName,"OUT");
		Thread  t2=new Thread(s2);
		
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		String verJson = null;
		JsonObject atwObj = null;
		JsonObject verObj = null;
		JsonObject outObj = null;
		JsonObject collection = new JsonObject();
		if(movieList.containsKey(movieName)) {
			verJson = gson.toJson(movieList.get(movieName));
			verObj= gson.fromJson(verJson, JsonObject.class);
			collection.add("Verdun", atwObj);
		}
		// second fetch VER server Schedules
		if(!s1.getValue().isEmpty() || s1.getValue().length()>0 || s1.getValue()!=null) {
			atwObj = gson.fromJson(s1.getValue(), JsonObject.class);
			collection.add("Atwater", atwObj);
		}
		
		// third fetch OUT server schedules and check for potential errors
		if(!s2.getValue().isEmpty() || s2.getValue().length()>0 || s2.getValue()!=null) {
			outObj = gson.fromJson(s2.getValue(), JsonObject.class);
			collection.add("Outremont", outObj);
		}
			
		if (gson.toJson(collection).equals("{}")) {
		    return "NoListFound";
		}else {
			return gson.toJson(collection);
		}
	}

	@Override
	public String getBookingSchedule(String customerID) {
        String methodName="getBookingSchedule";
		Sender1 s1= new Sender1(customerID,methodName,"ATW");
		Thread t1=new Thread(s1);
		Sender1 s2= new Sender1(customerID,methodName,"OUT");
		Thread t2=new Thread(s2);
		
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		String verJson = null;
		JsonObject atwObj = null;
		JsonObject verObj = null;
		JsonObject outObj = null;
		JsonObject collection = new JsonObject();
		if(customerMovieList.containsKey(customerID)) {
			verJson = gson.toJson(customerMovieList.get(customerID));
			verObj = gson.fromJson(verJson, JsonObject.class);
			collection.add("Verdun", verObj);
		}
		// second fetch VER server Schedules
		if(!s1.getValue().isEmpty() || s1.getValue().length()>0 || s1.getValue()!=null) {
			atwObj = gson.fromJson(s1.getValue(), JsonObject.class);
			collection.add("Atwater", atwObj);
		}
		
		// third fetch OUT server schedules and check for potential errors
		if(!s2.getValue().isEmpty() || s2.getValue().length()>0 || s2.getValue()!=null) {
			outObj = gson.fromJson(s2.getValue(), JsonObject.class);
			collection.add("Outremont", outObj);
		}
			
		if (gson.toJson(collection).equals("{}")) {
		    return "NoListFound";
		}else {
			return gson.toJson(collection);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public String cancelMovieTickets(String customerID, String movieID, String movieName, int numberOfTickets) {
		// TODO Auto-generated method stub
		String msg="";
		if(!customerMovieList.containsKey(customerID)){
			return "Customer Id doesnot exist";
		}
		if (!customerMovieList.get(customerID).containsKey(movieName)) {
			return "Movie name doesnot exist";
		}
		 if (!customerMovieList.get(customerID).get(movieName).containsKey(movieID)) {
			 return "Movie Id doesnot exist" ;
		 }
		 customerMovieList.get(customerID).get(movieName).remove(movieID);
		 int capacity=movieList.get(movieName).get(movieID);
		 movieList.get(movieName).put(movieID, capacity+ numberOfTickets);
		 System.out.println("Movie ticket removed from the customer booking list" + customerMovieList.toString());
		 System.out.println("Capacity increased " + movieList.toString());
		 return "Cancelled Successfuly";	   
	}

	@Override
	public String exchangeTickets(String customerID,String old_movieName, String movieID, String new_movieID, String new_movieName,
			int numberOfTickets) {
		String msg="";
		
		if(!customerMovieList.containsKey(customerID)) 
		{
			msg="Customer doesnot exist";
			return msg;
		}
		if(!customerMovieList.get(customerID).containsKey(old_movieName)) {
			msg= "Movie does not exist";
			return msg;
		}
		if(!customerMovieList.get(customerID).get(old_movieName).containsKey(movieID)) 
		{
			msg= "Movie ID doesnot exist";
			return msg;
		}
		String region = new_movieID.substring(0,3);
		if (region.equals("ATW"))
		{
			System.out.println(region);
			String methodName="canBookInAtwater";
			String str= customerID+","+new_movieID+","+new_movieName+","+numberOfTickets+"";
			Sender1 s1= new Sender1(str,methodName,"ATW");
			Thread  t1=new Thread(s1);
			t1.start();
			try {
				t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!s1.getValue().equals("Booked successfully")) {
				msg= "Could not sucessully exchange ticket with Atwater Server";	
				return msg;
				}
				String res= cancelMovieTickets(customerID, movieID,old_movieName,numberOfTickets);
				System.out.println(res);
				msg="Could sucessfully book ticket in Atwater and cancel ticket in Verdun";
			    return msg;
			//Sender1 s2= new Sender1(customerID,methodName,"OUT");
		}
		if (region.equals("OUT"))
		{
			//System.out.println(region);
			String methodName="canBookInOutremont";
			String str= customerID+","+new_movieID+","+new_movieName+","+numberOfTickets+"";
			Sender1 s1= new Sender1(str,methodName,"OUT");
			Thread  t1=new Thread(s1);
			t1.start();
			try {
				t1.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(s1.getValue());
			if(!s1.getValue().equals("Booked successfully")) {
				msg= "Could not sucessully exchange ticket with Outremont Server";	
				return msg;
				}
				String res= cancelMovieTickets(customerID,movieID,old_movieName,numberOfTickets);
				System.out.println(res);
				msg="Could sucessfully book ticket in outremont and cancel ticket in Verdun";
			    return msg;
			//Sender1 s2= new Sender1(customerID,methodName,"OUT");
		}
		if (region.equals("VER"))
		{
			//System.out.println(s1.getValue());
			String str= bookMovieTickets(customerID,old_movieName, movieID, numberOfTickets);
			if(!str.equals("Booked successfully")) {
				msg= "Could not sucessully exchange ticket";	
				return msg;
				}
				String res= cancelMovieTickets(customerID,movieID,old_movieName,numberOfTickets);
				System.out.println(res);
				msg="Could exchange ticket sccessfully";
			    return msg;
			//Sender1 s2= new Sender1(customerID,methodName,"OUT");
		}
		return msg;
	}

}
