import java.net.*;
import java.io.*;

/**
 * Sender
 * @author MasonSilber
 * Sends messages to the GUI
 */
public class Sender implements Runnable{

	SemBoundedBuffer senderBuffer;

	/**
	 * Constructor
	 * @param buffer Buffer to use in the constructor
	 */
	public Sender(SemBoundedBuffer buffer)
	{
		this.senderBuffer = buffer;
	}

	/**
	 * Runs when the thread is started
	 */
	@Override
	public void run() 
	{
		Socket senderSocket = null;
		
		try
		{
			senderSocket = new Socket("localhost",1480);
			System.out.println("Sender socket connected.");
		}
		catch(IOException e)
		{
			System.out.println("Sender socket failed to connect.");
		}
		
		while(true)
		{	
			try
			{
				if(senderSocket != null)
				{
					DataOutputStream outToGUI = new DataOutputStream(senderSocket.getOutputStream());

					try 
					{
						//If there's a message in the buffer
						if(senderBuffer.getSize() > 0)
						{
							//Send it to the GUI
							System.out.println("Buffer has more than zero things inside it! YTFD!");
							String message = (String) senderBuffer.get();
							System.out.println("Got message correctly. It is:\n" + message);
							outToGUI.writeBytes(message + "\n");
							outToGUI.flush();
						}
					} 
					catch (InterruptedException e) 
					{
						System.out.println("Failed to get object from the buffer.");
					}
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("FACK. OutToGUI failed.");
				e.printStackTrace();
			}
		}
	}

}
