import java.net.*;
import java.util.NoSuchElementException;
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

	Socket toGUISocket;
	Socket fromProxySocket;
	BufferedReader buffer;
	DataOutputStream outToGUI;

	SemBoundedBuffer receivedMessagesBuffer;

	public SenderToGUI(Socket proxySocket)
	{
		Socket toGUISocket = null;
		Socket fromProxySocket = null;
		receivedMessagesBuffer = new SemBoundedBuffer(1024);

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
		fromProxySocket = proxySocket;

		buffer = null;
		try 
		{
			buffer = new BufferedReader(new InputStreamReader(fromProxySocket.getInputStream()));
		} 
		catch (IOException e1) 
		{
			System.out.println("Buffer reading from proxy failed to initialize");
		}

		outToGUI = null;
		try 
		{
			outToGUI = new DataOutputStream(toGUISocket.getOutputStream());
		} 
		catch (IOException e1) 
		{
			System.out.println("Data Output stream to the GUI failed to initialize.");
		}

	}

	/**
	 * Runs when the thread is started
	 */
	@Override
	public void run() 
	{
		Thread getMessagesThread = new Thread(new Runnable()
		{
			public void run()
			{
				getMessagesFromServer();
			}
		});

		Thread sendMessagesThread = new Thread(new Runnable()
		{
			public void run()
			{
				sendMessagesToGUI();
			}
		});

		getMessagesThread.start();
		sendMessagesThread.start();
	}

	/**
	 * Checks to see if messages from the server have come in and handles them accordingly
	 * Runs on its own thread
	 */
	public void getMessagesFromServer()
	{
		System.out.println("Get messages from server called");
		String stringFromServer = null;

		while(true)
		{
			if(buffer != null)
			{
				try 
				{
					System.out.println("Waiting on string from server");
					stringFromServer = buffer.readLine();
				} 
				catch (IOException e) 
				{
					System.out.println("Unable to read from the buffer");
				}
			}

			System.out.println("Got something! " + stringFromServer);

			//If there's a new message from the server
			if(stringFromServer != null)
			{
				System.out.println("String from server: " + stringFromServer);
				try 
				{
					receivedMessagesBuffer.put(stringFromServer);
				} 
				catch (InterruptedException e) 
				{
					System.out.println("Unable to put messages received from server into the buffer");
				}

			}

		}
	}

	/**
	 * Checks to see if messages are waiting to be sent to the GUI
	 * If so, it reformats them and sends them over
	 */
	public void sendMessagesToGUI()
	{
		System.out.println("SendMessageToGUI called");
		while(true)
		{
			String toGoToGUI = null;

			try 
			{
				toGoToGUI = (String)receivedMessagesBuffer.get();
				System.out.println("Just read this from the buffer: " + toGoToGUI);
			} 
			catch (InterruptedException e1) 
			{
				System.out.println("SenderToGUI failed to read from buffer");
			}

			if(toGoToGUI != null)
			{
				String parsedMessage = parseFromServerMessage(toGoToGUI);
				System.out.println("String to go to GUI: " + parsedMessage);

				//And send it to the GUI
				try 
				{
					System.out.println("About to send THIS message to the GUI");
					outToGUI.writeBytes(parsedMessage + "\n");
					outToGUI.flush();
				} 
				catch (IOException e) 
				{
					System.out.println("Unable to send message to the GUI through the output stream");
				}
			}
			else
			{
				System.out.println("Decided not to send malformed message to GUI");
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
		System.out.println("Parsing message from server");
		String parsedOut = "";

		try
		{
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
		}
		catch(NoSuchElementException e)
		{
			System.out.println("Malformed message from the server :(. Discarding it.");
			return null;
		}

		return parsedOut;

	}
}
