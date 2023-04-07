package OutremontServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import com.google.gson.Gson;




public class UDPServer implements Runnable
{
	OutremontOperations out;
	public final int port = 5002;
	public UDPServer(OutremontOperations out) {
		this.out=out;
	}
	
	
	@SuppressWarnings("resource")
	public void run() 	
	{
		//String received="";
		DatagramSocket verdunSocket = null;
		try {
			verdunSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] byte3 = new byte[1000];
		//byte3=Integer.toString(i).getBytes();
		while(true)
		{

			{
				//receiving request
				DatagramPacket request = new DatagramPacket(byte3, byte3.length);
				try {
					verdunSocket.receive(request);
					byte[] received = request.getData();
					
					//first find the byte length
					int byteLength = 0;
					while(received[byteLength]!=0) {
						byteLength++;
					}
					// copy the byte to new length
					byte [] trimData = new byte[byteLength];
					for (int i = 0; i < trimData.length; i++) {
						trimData[i] = received[i];
					}
					
					
					String method = new String(trimData, 0, trimData.length);
					String json ="";
					String ctx ="";
					if(method.indexOf(':')>-1) {
						String [] param = method.split(":");
						ctx = param[0];
						method = param[1];
					}
					System.out.println(method);
					switch (method) {
					case "listMovieShowsAvailability":
						Gson gson1 = new Gson();
						if(OutremontOperations.movieList.containsKey(ctx)) {
							json = gson1.toJson(OutremontOperations.movieList.get(ctx));
						}
						break;
					case "getBookingSchedule":
						Gson gson = new Gson();
						if(OutremontOperations.customerMovieList.containsKey(ctx)) {
							json = gson.toJson(OutremontOperations.customerMovieList.get(ctx));
						}
						break;
					case "canBookInOutremont":
						String[] ctxArray = ctx.split(",");
						String customerID= ctxArray[0];
						String movieID= ctxArray[1];
						String movieName= ctxArray[2];
						int noOfBooking= Integer.parseInt(ctxArray[3].trim());
//						commonPOA out = new OutremontOperations();
						json= out.bookMovieTickets(customerID, movieName, movieID, noOfBooking);
						//System.out.println(msg);
						break;
					default:
						break;
					}
					
//				sending request
					byte[] buffer = json.getBytes();
					InetAddress ia = InetAddress.getLocalHost();
					DatagramPacket reply = new DatagramPacket(buffer, buffer.length,ia,request.getPort());
					verdunSocket.send(reply);   

					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
}