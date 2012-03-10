
public class Main {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) 
	{		
		ReceiverFromGUI receiver = new ReceiverFromGUI();
		SenderToGUI sender = new SenderToGUI(receiver);
		receiver.setSenderToGUI(sender);
		
		Thread p1 = new Thread(receiver);
		Thread p2 = new Thread(sender);
		p1.start();
		p2.start();
	}

}
