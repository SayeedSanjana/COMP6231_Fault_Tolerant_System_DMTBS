package OutremontServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

public class Sender1 implements Runnable{
	
	String ctx;
//	String serverKey;
	String method;
	int port;
	private String json;
	//public boolean ready = false;
	HashMap<String, Integer> servers = new HashMap<>();
	
	public Sender1(String ctx,String method, String serverKey) {
		
		this.initServers();
		this.ctx= ctx;
		this.port= servers.get(serverKey);
		this.method=method;
	
	}
	
	private void initServers() {
		this.servers.put("ATW", 5000);
		this.servers.put("VER", 5001);
		this.servers.put("OUT", 5002);
	}
	@Override
	public void run(){ 
		System.out.println("Sending from Outremont");
		System.out.println(port);
		DatagramSocket atwaterSocket = null;
		try {
			atwaterSocket = new DatagramSocket();
			String str = ctx + ':' + method;
			byte [] byte1 = str.getBytes();
			InetAddress dstAddress = InetAddress.getLocalHost();
			DatagramPacket request =new DatagramPacket(byte1, byte1.length, dstAddress,port);

			atwaterSocket.send(request);

			//Receiving Request
			byte[] byte2 = new byte[1000];
			DatagramPacket reply = new DatagramPacket(byte2, byte2.length);
			atwaterSocket.receive(reply);
			//byte  d1[]=(reply.getData());
			this.json= new String(reply.getData(),0,reply.getLength());
			//System.out.println("The json is : " + json);
			// ready = true;

		}
		catch (SocketException e)
		{	System.out.println("Socket: " + e.getMessage());
		}
		catch (IOException e)
		{
			System.out.println("IO: " + e.getMessage());
		}
		finally 
		{
			if(atwaterSocket != null)
				atwaterSocket.close();
		}
	}
	public String getValue() {
        return json;
    }
}
