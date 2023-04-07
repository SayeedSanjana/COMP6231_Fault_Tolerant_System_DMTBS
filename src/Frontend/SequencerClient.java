package Frontend;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.google.gson.Gson;

public class SequencerClient{


	public static void main(String[] args) {
		String msg = null;
		String method = null;
		int port = 7000;
		String json;
		
		System.out.println("Sending from Sequencer");
		DatagramSocket sequencerSocket = null;
		Sequencer seq = new Sequencer();
		Gson gson = new Gson();
		
		try {
			while(true) {
				long sequenceNumber = seq.getNextSequenceNumber();
				seq.addQueue(sequenceNumber, msg);
				String data = gson.toJson(seq.getQueue());
				sequencerSocket = new DatagramSocket();
				
				//send request
				
				byte [] byte1 = data.getBytes();
				InetAddress dstAddress = InetAddress.getLocalHost();
				DatagramPacket request =new DatagramPacket(byte1, byte1.length, dstAddress,port);
	
				sequencerSocket.send(request);
	
				//Receiving Request
				byte[] byte2 = new byte[1000];
					
					DatagramPacket reply = new DatagramPacket(byte2, byte2.length);
					sequencerSocket.receive(reply);
					json= new String(reply.getData(),0,reply.getLength());
			}
		}
		catch (SocketException e)
		{	
			System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
		finally 
		{
			if(sequencerSocket != null)
				sequencerSocket.close();
		}

	}


//	public String getValue() {
//		return json;
//	}
}
