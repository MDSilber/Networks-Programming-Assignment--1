
public class Main {

	public static void main(String[] args) {
		
		//Create a bounded buffer of size 3
		SemBoundedBuffer buffer = new SemBoundedBuffer(3);
		//Create 3 producer and 2 consumers that will interact with the buffer
		Thread p1 = new Thread( new Producer(buffer));
		Thread p2 = new Thread(new Producer(buffer));
		Thread p3 = new Thread(new Producer(buffer));
		Thread c1 = new Thread(new Consumer(buffer));
		Thread c2 = new Thread(new Consumer(buffer));
		//Start all the threads
		p1.start();
		p2.start();
		p3.start();
		c1.start();
		c2.start();
		
		
		
		

	}

}
