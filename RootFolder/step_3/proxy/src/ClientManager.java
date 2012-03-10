import java.util.*;
import java.net.*;
import java.io.*;

/**
 * ClientManager
 * @author MasonSilber
 * Manages clients for the proxy, allows as many clients to connect as the proxy can handle
 */
public class ClientManager implements Runnable
{
	ServerSocket welcomeSocket;
	ArrayList<Socket> sockets;
	ArrayList<BufferedReader> bufferedReaders;
	ArrayList<DataOutputStream> outputStreams;
	SemBoundedBuffer serverBoundMessages;
	SemBoundedBuffer clientBoundMessages;
	int clientCounter;

	/**
	 * Constructor
	 * @param toServer Buffer of messages going to the sever
	 * @param toClient Buffer of messages coming from the server
	 * @param welcome ServerSocket that accepts incoming connections
	 */
	public ClientManager(SemBoundedBuffer toServer, SemBoundedBuffer toClient, ServerSocket welcome)
	{
		welcomeSocket = welcome;
		serverBoundMessages = toServer;
		clientBoundMessages = toClient;

		sockets = new ArrayList<Socket>();
		bufferedReaders = new ArrayList<BufferedReader>();
		outputStreams = new ArrayList<DataOutputStream>();

		clientCounter = 0;
	}

	/**
	 * Runs when the thread containing this Runnable is started
	 */
	public void run()
	{
		while(true)
		{
			try 
			{
				Socket newClient = welcomeSocket.accept();
				System.out.println("New Client!");

				//Add information about client
				sockets.add(newClient);
				bufferedReaders.add(new BufferedReader(new InputStreamReader(newClient.getInputStream())));
				outputStreams.add(new DataOutputStream(newClient.getOutputStream()));
			} 
			catch (IOException e) 
			{
				System.out.println("Failed to connect with incoming client.");
			}




			Thread toClient = new Thread(new Runnable()
			{
				public void run()
				{
					//System.out.println("TO CLIENT Counting value: " + clientCounter + " and list size: " + sockets.size());
					Client newClient = new Client(sockets.get(sockets.size() - 1),bufferedReaders.get(bufferedReaders.size() - 1),outputStreams.get(outputStreams.size() - 1));
					toClient(newClient);
				}
			});

			Thread fromClient = new Thread(new Runnable()
			{
				public void run()
				{
					//System.out.println("FROM CLIENT Counting value: " + clientCounter + " and list size: " + sockets.size());
					Client newClient = new Client(sockets.get(sockets.size() - 1),bufferedReaders.get(bufferedReaders.size() - 1),outputStreams.get(outputStreams.size() - 1));
					fromClient(newClient);
				}
			});

			toClient.start();
			fromClient.start();
			clientCounter++;
		}
	}

	/**
	 * Sends messages to the client from the server (after being detached onto a new thread)
	 * @param thisClient
	 */
	public void toClient(Client thisClient)
	{
		while(true)
		{

			System.out.println("Sending a message to client");
			String s = null;

			try 
			{
				s = (String) clientBoundMessages.get();
			} 
			catch (InterruptedException e) 
			{
				System.err.println("ClientManager was unable to get message from clientBoundMessages");
			}

			if(s != null)
			{
				System.out.println("Message to send to client: " + s);
				try 
				{
					thisClient.getOutputStream().writeBytes(s + "\n");
					thisClient.getOutputStream().flush();
					s = null;
				} 
				catch (IOException e) 
				{
					System.err.println("ClientManager was unable to send message to the client");
					try 
					{
						thisClient.getSocket().close();
						System.out.println("Client disconnected");
					} 
					catch (IOException e1) 
					{
						System.err.println("Failed to close client socket in toClient");
					}
					return;
				}
			}

		}
	}

	/**
	 * Sends messages from the client to the server (after detaching onto a new thread)
	 * @param thisClient
	 */
	public void fromClient(Client thisClient)
	{
		while(true)
		{
			String s = null;

			try 
			{
				s = thisClient.getBufferedReader().readLine();
				System.out.println("Received message from client: " + s);
			} 
			catch (IOException e) 
			{
				System.err.println("ClientManager unable to read line from the client's buffer");
			}

			if(s != null)
			{
				try 
				{
					serverBoundMessages.put(s);
					s = null;
				} 
				catch (InterruptedException e) 
				{
					System.err.println("ClientManager was unable to add a message to the serverBoundMessages buffer");
				}
			}
			else
			{
				try 
				{
					thisClient.getSocket().close();
					System.out.println("Client disconnected");
				} 
				catch (IOException e) 
				{
					System.err.println("Failed to close client socket in fromClient");
				}
				return;
			}
		}
	}
}
