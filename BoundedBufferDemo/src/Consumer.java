import java.util.Random;


public class Consumer implements Runnable{

	SemBoundedBuffer buffer;
	public Consumer(SemBoundedBuffer buffer) {
		this.buffer = buffer;
	}
	
	public void run(){
		
		Random r = new Random();
		float f;
		while(true)
		{
			
			f = r.nextFloat()*2000;
			
			try {
				Thread.sleep((long) f);
			} catch (InterruptedException e) {
				System.out.println("Couldn't sleep");
			}
			try {
				System.out.println("Consumer wants to get something from the buffer");
				buffer.get();
				System.out.println("Consumer sucessfully got something from the buffer");
			} catch (InterruptedException e) {
				System.out.println("Could not put something in the buffer");
			}
			
			
			
			
		}
		
		
	}

}
