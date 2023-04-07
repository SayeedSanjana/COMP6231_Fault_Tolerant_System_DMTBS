package VerdunServer;

import javax.xml.ws.Endpoint;

import VerdunServer.VerdunOperations;
import VerdunServer.UDPServer;

@SuppressWarnings("unused")
public class VerdunInterfaceWSPublisher {
	public static void main(String[] args) {
		  VerdunOperations verdunOperations = new VerdunOperations();
		  Endpoint.publish("http://localhost:8082/WS/Verdun",verdunOperations);
		  System.out.println("Verdun Server ready and waiting ...");
			
		  Runnable udpServer = new UDPServer(verdunOperations);
		  Thread t1 = new Thread(udpServer);
		  t1.start();
		 }

}
