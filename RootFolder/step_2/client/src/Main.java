
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//SemBoundedBuffer buffer = new SemBoundedBuffer(1024);
		
		Thread p1 = new Thread(new SenderToGUI());
		Thread p2 = new Thread(new ReceiverFromGUI());
		p1.start();
		p2.start();
	}

}
