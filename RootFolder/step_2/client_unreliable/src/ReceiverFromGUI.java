import java.net.*;
import java.io.*;
import java.util.*;

/**
 * ReceiverFromGUI
 * @author MasonSilber
 * This class is the one that receives messages from the GUI and sends them to the server
 */
public class ReceiverFromGUI implements Runnable{

	boolean serverResponded;
	SenderToGUI sender; 
	String messageSent;
	
	/**
	 * Constructor
	 */
	public ReceiverFromGUI()
	{
		serverResponded = false;
		sender = null;
		messageSent = null;
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
		
		//Connect to the first socket
		try
		{
			fromGUISocket = new Socket("localhost",1479);
			System.out.println("fromGUISocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("Receiver socket failed to connect.");
		}

		//Connect to the second socket
		try
		{
			//toServerSocket = new Socket("csee4119.cs.columbia.edu",1453);
			toServerSocket = new Socket("localhost",1453);
			System.out.println("toServerSocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("toServerSocket failed to connect.");
		}
		
		String stringFromGUI = null;
		
		//Continuously check to see if there's a new string from the GUI to send to the server
		while(true)
		{
			try
			{
				if(toServerSocket != null)
				{
					BufferedReader buffer = new BufferedReader(new InputStreamReader(fromGUISocket.getInputStream()));
					System.out.println("Waiting for something from GUI");
					stringFromGUI = buffer.readLine();
					
					//If there's a new string
					if(stringFromGUI != null)
					{
						//Send the request
						sendToServer(toServerSocket, stringFromGUI);
						
						//Create date to see if server times out
						Date now = new Date();
						
						//While the server hasn't responded
						while(!serverResponded)
						{
							Date someTime = new Date();
							
							//Decide if it's time to resend request
							if((someTime.getTime() - now.getTime()) > 300)
							{
								System.out.println("Timeout by server");
								//Resend request
								sendToServer(toServerSocket, stringFromGUI);
								//Reset the "now" variable to reflect the new request time
								now = new Date();
							}
						}
						
						if(serverResponded)
						{
							System.out.println("Server has responded");
							serverResponded = false;
							stringFromGUI = null;
							continue;
						}
						stringFromGUI = null;
					}
				}
				
				serverResponded = false; 
			}
			catch(IOException e)
			{
				//System.out.println("Failed to get something from the server");
			}
		}
	}

	/**
	 * Send the message from the GUI to the server
	 * @param toServer Socket connected to the server
	 * @param messageToSend Message to send to the server
	 */
	public void sendToServer(Socket toServer, String messageToSend)
	{
		System.out.println("Send to server called");
		try
		{
			//Create output stream, format the message, and send it to the server
			DataOutputStream outToServer = new DataOutputStream(toServer.getOutputStream());
			messageSent = messageToSend;
			outToServer.writeBytes(formatMessage(messageToSend));
			outToServer.flush();
		}
		catch(IOException e)
		{
			
		}
	}
	
	/**
	 * Setter method for "serverResponded"
	 * @param responded the value to which we want to set serverResponded
	 */
	public void setServerResponded(boolean responded)
	{
		serverResponded = responded;
	}
	
	/**
	 * Format the message appropriately for the server
	 * @param stringFromGUI String to format before sending it to the server
	 * @return Formatted message to send to the server
	 */
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
	
	public void setSenderToGUI(SenderToGUI s)
	{
		this.sender = s;
	}
	
	public String messageSent()
	{
		return messageSent;
	}
}
