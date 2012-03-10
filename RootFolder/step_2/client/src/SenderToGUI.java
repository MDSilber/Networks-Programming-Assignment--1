import java.net.*;
import java.util.StringTokenizer;
import java.io.*;

/**
 * SenderToGUI
 * @author MasonSilber
 * Sends messages from the server to the GUI
 */
public class SenderToGUI implements Runnable{

	/**
	 * Constructor
	 */
	public SenderToGUI()
	{

	}

	/**
	 * Runs when the thread is started
	 */
	@Override
	public void run() 
	{
		Socket toGUISocket = null;
		Socket fromServerSocket = null;

		//Connect to the first socket
		try
		{
			toGUISocket = new Socket("localhost",1480);
			System.out.println("toGUISocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("fromGUISocket failed to connect.");
		}

		//Connect to the second socket
		try
		{
			fromServerSocket = new Socket("csee4119.cs.columbia.edu",1452);
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
				if(fromServerSocket != null)
				{
					BufferedReader buffer = new BufferedReader(new InputStreamReader(fromServerSocket.getInputStream()));
					stringFromServer = buffer.readLine();

					//If there's a new message from the server
					if(stringFromServer != null)
					{
						System.out.println("String from server: " + stringFromServer);
						
						//Parse out the information
						String toGoToGUI = parseFromServerMessage(stringFromServer);
						
						System.out.println("String to go to GUI: " + toGoToGUI);
						
						//And send it to the GUI
						DataOutputStream outToGUI = new DataOutputStream(toGUISocket.getOutputStream());
						outToGUI.writeBytes(toGoToGUI + "\n");
						outToGUI.flush();
					}
				}
			}
			catch(IOException e)
			{

			}
		}
	}

	/**
	 * Parse the message from the server so that the GUI will understand it
	 * @param messageFromServer Message unformatted from the server
	 * @return Formatted message for the GUI
	 */
	public static String parseFromServerMessage(String messageFromServer)
	{
		String parsedOut = "";
		StringTokenizer tokenizer = new StringTokenizer(messageFromServer, " :");
		
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
		
		return parsedOut;
		
	}
}
