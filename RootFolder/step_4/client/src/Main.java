import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Client Main class
 * @author MasonSilber
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		//SemBoundedBuffer buffer = new SemBoundedBuffer(1024);
		Socket proxySocket = null;
		
		if(args[0] == null || args[1] == null)
		{
			System.err.println("Usage: Main *host* *port*");
			System.exit(1);
		}
		
		
		int port = 0;
		try
		{
			System.err.println(args[1]);
			port = Integer.parseInt(args[1]);
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
		
		
		try 
		{
			proxySocket = new Socket(args[0],port);
			
			System.out.println("Connected to proxy");
		} 
		catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			System.err.println("Proxy is unknown host");
			System.exit(1);
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("ProxySocket could not initialize.");
			System.exit(1);
		}
		
		if(args.length > 2)
		{
			if(args[2].substring(0,2).equals("-a") || args[2].substring(0,2).equals("-r"))
			{
				HandleUNIs(proxySocket,args[2]);
			}
			else
			{
				System.err.println("Please put in correctly formatted command line arguments.");
				System.exit(1);
			}
		}
		
		if(args.length > 3)
		{
			if(args[3].substring(0,2).equals("-a") || args[3].substring(0,2).equals("-r"))
			{
				HandleUNIs(proxySocket,args[3]);
			}
			else
			{
				System.err.println("Please put in correctly formatted command line arguments.");
				System.exit(1);
			}
		}
		
		Thread p1 = new Thread(new SenderToGUI(proxySocket));
		Thread p2 = new Thread(new ReceiverFromGUI(proxySocket));
		p1.start();
		p2.start();
	}

	public static void HandleUNIs(Socket proxySocket, String UNIs)
	{
		//System.out.println("UNIs: " + UNIs);
		DataOutputStream sendUNIs = null;
		
		try 
		{
			sendUNIs = new DataOutputStream(proxySocket.getOutputStream());
		} 
		catch (IOException e) 
		{
			System.err.println("Data output stream to send UNIs failed");
			return;
		}
		
		try 
		{
			sendUNIs.writeBytes(UNIs + "\n");
			sendUNIs.flush();
			return;
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to send UNIs to proxy");
		}
	}
}
