import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

/**
 * ReceiverFromGUI
 * @author MasonSilber
 * Receives messages from the GUI and sends them to the server
 */
public class ReceiverFromGUI implements Runnable{

	/**
	 * Constructor
	 */
	public ReceiverFromGUI()
	{
		
	}

	/**
	 * Runs when the thread is started
	 */
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		Socket fromGUISocket = null;
		Socket toServerSocket = null;

		//Connect to first socket
		try
		{
			fromGUISocket = new Socket("localhost",1479);
			System.out.println("fromGUISocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("Receiver socket failed to connect.");
		}

		//Connect to second socket
		try
		{
			toServerSocket = new Socket("csee4119.cs.columbia.edu",1452);
			System.out.println("toServerSocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("toServerSocket failed to connect.");
		}
		
		String stringFromGUI = null;
		
		//Continuously check for new messages from the GUI to send to the server
		while(true)
		{
			try
			{
				if(toServerSocket != null)
				{
					BufferedReader buffer = new BufferedReader(new InputStreamReader(fromGUISocket.getInputStream()));
					stringFromGUI = buffer.readLine();
					
					DataOutputStream outToServer = new DataOutputStream(toServerSocket.getOutputStream());
					
					//If there is a new message from the GUI
					if(stringFromGUI != null)
					{
						//Send it to the server
						outToServer.writeBytes(formatMessage(stringFromGUI));
						outToServer.flush();
						stringFromGUI = null;
					}
				}
			}
			catch(IOException e)
			{
				//System.out.println("Failed to get something from the server");
			}
		}
	}
	
	//Format the message appropriately for the server
	public static String formatMessage(String stringFromGUI)
	{
		String message = "UNI:mds2161 Shape:";
		
		StringTokenizer tokenizer = new StringTokenizer(stringFromGUI," ");
		
		message += tokenizer.nextToken() + " X:";
		message += tokenizer.nextToken() + " Y:";
		message += tokenizer.nextToken() + " Color:";
		message += tokenizer.nextToken() + " AdditionalInfo:test\n";
		
		return message;
	}

}
