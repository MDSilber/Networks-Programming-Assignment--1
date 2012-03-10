import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Proxy Main Class
 * @author MasonSilber
 *
 */
public class Main {

	/**
	 * @param args port number
	 */

	static ServerSocket welcomeSocket;
	static Socket serverSocket;
	static SemBoundedBuffer serverBoundMessages, clientBoundMessages;
	static BufferedReader inFromServerBuffer;
	static DataOutputStream outToServerStream;
	static ClientManager clientManager;
	static int port;

	public static void main(String[] args) 
	{
		if(args[0] == null)
		{
			System.err.println("Usage: Main *port*");
			System.exit(1);
		}


		port = 0;
		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch(NumberFormatException e)
		{
			System.err.println("Please enter an integer for a port number.");
			System.exit(1);
		}

		if(port < 1024 || port > 65536)
		{
			System.err.println("Please enter a valid port number.");
			System.exit(1);
		}

		//Initialize all variables
		setUp();

		Thread receiverThread = new Thread(new Runnable()
		{
			public void run()
			{
				receiveFromServer();
			}
		});

		Thread senderThread = new Thread(new Runnable()
		{
			public void run()
			{
				sendToServer();
			}
		});

		receiverThread.start();
		senderThread.start();
	}

	/**
	 * Set up loop checking for messages from the server
	 */
	public static void receiveFromServer()
	{
		while(true)
		{
			String message = null;
			try 
			{
				message = inFromServerBuffer.readLine();
			} 
			catch (IOException e) 
			{
				System.err.println("inFromServerBuffer unable to read from server");
			}

			if(message != null)
			{
				try 
				{
					clientBoundMessages.put(message);
				} 
				catch (InterruptedException e) 
				{
					System.err.println("clientBoundMessages buffer was unable to receive message: " + message);
				}
			}
		}
	}

	/**
	 * Set up loop checking to see if there are messages to be sent to the server
	 */
	public static void sendToServer()
	{
		while(true)
		{

			String message = null;
			try 
			{
				message = (String) serverBoundMessages.get();
			} 
			catch (InterruptedException e) 
			{
				System.err.println("serverBoundMessages was unable to give us message");
			}

			if(message != null)
			{
				try 
				{
					outToServerStream.writeBytes(message + "\n");
					outToServerStream.flush();
					message = null;
				} 
				catch (IOException e) 
				{
					System.err.println("outToServerStream unable to send message to the server.");
				}
			}

		}
	}

	/**
	 * Set up steps for the class, happens before the threads break off
	 */
	public static void setUp()
	{
		//Set up welcomeSocket
		welcomeSocket = null;
		try
		{
			welcomeSocket = new ServerSocket(port);
		}
		catch(IOException e)
		{
			System.err.println("welcomeSocket failed to initialize.");
			System.exit(1);
		}

		//Set up socket to connect to server
		serverSocket = null;
		try 
		{
			serverSocket = new Socket("csee4119.cs.columbia.edu",1452);
			//serverSocket = new Socket("localhost",1452);
			System.out.println("Server Socket connected");
		} 
		catch (UnknownHostException e) 
		{
			System.err.println("Server socket was unknown host.");
			System.exit(1);
		} 
		catch (IOException e) 
		{
			System.err.println("Server Socket could not be initialized.");
			System.exit(1);
		}

		serverBoundMessages = new SemBoundedBuffer(1024);
		clientBoundMessages = new SemBoundedBuffer(1024);

		inFromServerBuffer = null;
		try 
		{
			inFromServerBuffer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
		} 
		catch (IOException e) 
		{
			System.err.println("inFromServerBuffer failed to initialize");
			System.exit(1);
		}

		outToServerStream = null;
		try 
		{
			outToServerStream = new DataOutputStream(serverSocket.getOutputStream());
		} 
		catch (IOException e) {
			System.err.println("outToServerStream failed to initialize");
		}

		clientManager = new ClientManager(serverBoundMessages, clientBoundMessages, welcomeSocket);

		Thread clientManagerThread = new Thread(clientManager);
		clientManagerThread.start();
	}

}
