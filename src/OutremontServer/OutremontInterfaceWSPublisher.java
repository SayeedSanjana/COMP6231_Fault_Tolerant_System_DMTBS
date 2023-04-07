package OutremontServer;

import javax.xml.ws.Endpoint;

public class OutremontInterfaceWSPublisher {
	public static void main(String[] args) {
		  OutremontOperations outremontOperations = new OutremontOperations();
		  Endpoint.publish("http://localhost:8083/WS/Outremont",outremontOperations);
		  System.out.println("Outremont Server ready and waiting ...");
			
		  Runnable udpServer = new UDPServer(outremontOperations);
		  Thread t1 = new Thread(udpServer);
		  t1.start();
		 }

}
