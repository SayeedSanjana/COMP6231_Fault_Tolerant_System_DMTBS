package VerdunServer;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
 
@WebService
@SOAPBinding(style=Style.RPC)
public interface VerdunInterface {

	 @WebMethod public String bookMovieTickets(String userID, String movieName, String movieID, int noOfTickets);

	 @WebMethod public String cancelMovieTickets(String userID, String movieName, String movieID, int noOfTickets);

	 @WebMethod public String getBookingSchedule(String userID);

	 @WebMethod public String exchangeTickets(String userID, String movieName, String movieID, String newMovieID, String newMovie,
			int noOfTickets);

	 @WebMethod public String addMovieSlots(String movieID, String movieName, int bookingCapacity);

	 @WebMethod public String removeMovieSlots(String movieID, String movieName);

	 @WebMethod public String listMovieShowsAvailability(String movieName);
}
