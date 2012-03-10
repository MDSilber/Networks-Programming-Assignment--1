import java.io.*;
import java.net.*;


/**
 * Client
 * @author MasonSilber
 * Basically a container class for a socket, buffered reader, and output stream
 * It facilitates the multithreading of the proxy
 */
public class Client 
{
	Socket mySocket;
	BufferedReader myBufferedReader;
	DataOutputStream myDataOutputStream;
	
	/**
	 * Constructor
	 * @param socket Client's socket to the proxy
	 * @param buffer Client's buffered reader
	 * @param output Client's output stream
	 */
	public Client(Socket socket, BufferedReader buffer, DataOutputStream output)
	{
		mySocket = socket;
		myBufferedReader = buffer;
		myDataOutputStream = output;
	}
	
	/**
	 * Getter method for socket
	 * @return Client's socket
	 */
	public Socket getSocket()
	{
		return mySocket;
	}
	
	/**
	 * Getter method for buffered reader
	 * @return Client's buffered reader
	 */
	public BufferedReader getBufferedReader()
	{
		return myBufferedReader;
	}
	
	/**
	 * Getter method for output stream
	 * @return Cient's output stream
	 */
	public DataOutputStream getOutputStream()
	{
		return myDataOutputStream;
	}
}
