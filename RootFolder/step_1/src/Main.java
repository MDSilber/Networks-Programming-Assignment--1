
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		SemBoundedBuffer buffer = new SemBoundedBuffer(65536);
		Thread p1 = new Thread(new Sender(buffer));
		Thread p2 = new Thread(new Receiver(buffer));
		p1.start();
		p2.start();
	}

}
