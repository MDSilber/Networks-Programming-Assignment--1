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
	
	Socket fromGUISocket, toProxySocket;
	BufferedReader buffer;
	DataOutputStream outToProxy;

	SemBoundedBuffer receivedMessagesBuffer;

	public ReceiverFromGUI(Socket proxySocket)
	{
		receivedMessagesBuffer = new SemBoundedBuffer(1024);

		fromGUISocket = null;

		try
		{
			fromGUISocket = new Socket("localhost",1479);
			System.out.println("fromGUISocket connected.");
		}
		catch(IOException e)
		{
			System.out.println("Receiver socket failed to connect.");
		}

		buffer = null;
		try 
		{
			buffer = new BufferedReader(new InputStreamReader(fromGUISocket.getInputStream()));
		} 
		catch (IOException e1) 
		{
			System.out.println("Failed to initailize the socket from the GUI to the client");
		}

		toProxySocket = proxySocket;

		outToProxy = null;
		try 
		{
			if(toProxySocket != null)
			{
				outToProxy = new DataOutputStream(toProxySocket.getOutputStream());
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Output stream to proxy failed to initialize");
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
				getMessagesFromGUI();
			}
		});

		Thread sendMessagesThread = new Thread(new Runnable()
		{
			public void run()
			{
				sendMessagesToServer();
			}
		});

		getMessagesThread.start();
		sendMessagesThread.start();
	}

	
	/**
	 * Loop on its own thread checking to see if new messages from the GUI have been registered
	 * If so, it forwards them to the client
	 */
	public void getMessagesFromGUI()
	{
		System.out.println("GetMessagesFromGUI called");
		while(true)
		{
			String stringFromGUI = null;
			if(buffer != null)
			{
				try 
				{
					System.out.println("Waiting on string from GUI");
					stringFromGUI = buffer.readLine();
				} 
				catch (IOException e) 
				{
					System.out.println("Buffer was unable to read from the GUI");
				}
			}

			if(stringFromGUI != null)
			{
				try 
				{
					receivedMessagesBuffer.put(stringFromGUI);
					System.out.println("Message added to buffer. Size: " + receivedMessagesBuffer.size);
				} 
				catch (InterruptedException e) 
				{
					System.out.println("Unable to post message from GUI into buffer");
				}
			}
		}
	}

	/**
	 * Loop on its own thread checking to see if new messages have come in from the proxy
	 * and sends them to the GUI if necessary
	 */
	public void sendMessagesToServer()
	{
		System.out.println("SendMessagesToServer Called");
		while(true)
		{

				String s = null;
				try 
				{
					s = (String) receivedMessagesBuffer.get();
					System.out.println("Got message from the buffer: " + s);
				} 
				catch (InterruptedException e) 
				{
					System.out.println("Unable to retrieve message from the receivedMessagesBuffer");
				}

				if(s != null)
				{
					//Send it to the server
					try 
					{
						System.out.println("About to write messages to proxy");
						outToProxy.writeBytes(formatMessage(s));
						outToProxy.flush();
					} 
					catch (IOException e) 
					{
						System.out.println("Writing to proxy failed.");
					}
					s = null;
				}
			
		}
	}

	/**
	 * Formats the message for the proxy/server
	 * @param stringFromGUI message from the GUI to be formatted for the server
	 * @return formatted message
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

}
