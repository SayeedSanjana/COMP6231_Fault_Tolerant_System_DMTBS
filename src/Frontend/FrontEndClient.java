package Frontend;



public class FrontEndClient {
	private Sequencer seq;
	
	public FrontEndClient(Sequencer seq) {
		this.seq = seq;
	}
	
	public String sendRequest(String msg) {
		long sq = this.seq.getNextSequenceNumber();
		String message = msg;
		this.seq.addQueue(sq, message);
		return null;
	}
//	public synchronized String bookEvent(String customerID, String eventID, String eventType) {
//		// TODO Auto-generated method stub
//		System.out.println("Inside BookEvent");
//		String s_msg = customerID+",BOOK,"+eventID+","+eventType;
//		String r_msg = call_other(s_msg);
//		return r_msg;
//	}
}
