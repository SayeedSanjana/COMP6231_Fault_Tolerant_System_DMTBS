package AtwaterServer;
import javax.xml.ws.Endpoint;

public class AtwaterInterfaceWSPublisher {
	 public static void main(String[] args) {
		  AtwaterOperations atwaterOperations = new AtwaterOperations();
		  Endpoint.publish("http://localhost:8081/WS/Atwater",atwaterOperations);
		  System.out.println("Atwater Server ready and waiting ...");
			
		  Runnable udpServer = new UDPServer(atwaterOperations);
		  Thread t1 = new Thread(udpServer);
		  t1.start();
		 }
}
