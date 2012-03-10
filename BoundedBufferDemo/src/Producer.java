import java.util.Random;


public class Producer implements Runnable{

	SemBoundedBuffer buffer;
	public Producer(SemBoundedBuffer buffer) {
		this.buffer = buffer;
	}
	
	public void run(){
		
		Random r = new Random();
		float f;
		while(true)
		{
			f =  r.nextFloat()*1000;
			try {
				Thread.sleep((long) f);
			} catch (InterruptedException e) {
				System.out.println("Couldn't sleep");
			}
			try {
				System.out.println("Producer wants to put something in the buffer");
				buffer.put(new Object());
				System.out.println("Producer sucessfully put something in the buffer");
			} catch (InterruptedException e) {
				System.out.println("Could not put something in the buffer");
			}
			
			
			
			
		}
		
		
	}

}
