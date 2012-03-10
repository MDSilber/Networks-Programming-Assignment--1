import java.net.*;
import java.io.*;

/**
 * Receiver
 * @author MasonSilber
 * Receives messages from the GUI
 */
public class Receiver implements Runnable{

	SemBoundedBuffer receiverBuffer;

	/**
	 * Constructor
	 * @param buffer Buffer in which to place the messages from the GUI
	 */
	public Receiver(SemBoundedBuffer buffer)
	{
		this.receiverBuffer = buffer;
	}

	/**
	 * Runs when the thread is started
	 */
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		Socket receiverSocket = null;

		try
		{
			receiverSocket = new Socket("localhost",1479);
			System.out.println("Receiver socket connected");
		}
		catch(IOException e)
		{
			System.out.println("Receiver socket failed to connect.");
		}

		while(true)
		{
			try
			{

				if(receiverSocket != null)
				{
					BufferedReader inFromGUI = new BufferedReader(new InputStreamReader(receiverSocket.getInputStream()));
					
					//Get the string from the GUI
					String stringFromGUI = inFromGUI.readLine();
					System.out.println(stringFromGUI);
					try 
					{
						//And put it in the buffer
						receiverBuffer.put(stringFromGUI);
					} 
					catch (InterruptedException e) 
					{
						System.out.println("Putting the message on the buffer failed.");
					}
				}
				
				//System.out.println("Successfully got something from the buffer");
			}
			catch(IOException e)
			{
				//System.out.println("Failed to get something from the buffer");
			}
		}
	}

}
