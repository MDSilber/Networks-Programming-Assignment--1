import java.net.*;
import java.util.*;
import java.io.*;

/**
 * SenderToGUI
 * @author MasonSilber
 * This class is the one that receives messages from the server and sends them to the server
 */
public class SenderToGUI implements Runnable
{

	ReceiverFromGUI receiver;

	/**
	 * Constructor
	 * @param rec Receiver associated with this sender
	 */
	public SenderToGUI(ReceiverFromGUI rec)
	{
		this.receiver = rec;
	}

	/**
	 * Runs when the thread starts
	 */
	@Override
	public void run() 
	{
		Socket toGUISocket = null;
		Socket fromServerSocket = null;

		//Connect to first socket
		try
		{
			toGUISocket = new Socket("localhost",1480);
			System.out.println("toGUISocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("fromGUISocket failed to connect.");
		}

		//Connect to second socket
		try
		{
			//fromServerSocket = new Socket("csee4119.cs.columbia.edu",1453);
			fromServerSocket = new Socket("localhost",1453);
			System.out.println("fromServerSocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("fromServerSocket failed to connect.");
		}

		String stringFromServer = null;

		//Continuously check for new messages from the server
		while(true)
		{	

			try
			{
				//If there's a message from the server
				if(fromServerSocket != null)
				{
					//Read the message
					BufferedReader buffer = new BufferedReader(new InputStreamReader(fromServerSocket.getInputStream()));
					stringFromServer = buffer.readLine();

					//Check to make sure it's the one you sent. If it is, make sure that the object that
					//sent the request knows it's been received. If not, continue
					if(stringFromServer != null)
					{	
						String toGoToGUI = parseFromServerMessage(stringFromServer);

						//Check to make sure that the incoming message from the server
						//is the one that we sent, and not one from somewhere else
						if(toGoToGUI != null)
						{
							if(toGoToGUI.contains(receiver.messageSent()))
							{
								System.out.println("Recevied! YTFD!");
								receiver.setServerResponded(true);
							}
							else
							{
								System.out.println("This side: " + toGoToGUI + " " + toGoToGUI.length());
								System.out.println("That side: " + receiver.messageSent() +  " " + receiver.messageSent().length());
							}
						}


						System.out.println("String to go to GUI: " + toGoToGUI);

						//Send the message out to the GUI
						DataOutputStream outToGUI = new DataOutputStream(toGUISocket.getOutputStream());
						if(toGoToGUI != null)
						{
							outToGUI.writeBytes(toGoToGUI + "\n");
							outToGUI.flush();
						}
					}
				}
			}
			catch(IOException e)
			{

			}
		}
	}

	/**
	 * Parses the server's message so it's readable by the GUI
	 * @param messageFromServer Message received from the server
	 * @return String to send to the GUI
	 */
	public static String parseFromServerMessage(String messageFromServer)
	{
		String parsedOut = "";
		StringTokenizer tokenizer = new StringTokenizer(messageFromServer, " :");

		try
		{
			tokenizer.nextToken();
			tokenizer.nextToken();
			tokenizer.nextToken();

			parsedOut += tokenizer.nextToken() + " ";
			tokenizer.nextToken();
			parsedOut += tokenizer.nextToken() + " ";
			tokenizer.nextToken();
			parsedOut += tokenizer.nextToken() + " ";
			tokenizer.nextToken();
			parsedOut += tokenizer.nextToken() + "\n";
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Malformed message. Discarding.");
			return null;
		}

		return parsedOut;

	}
}
