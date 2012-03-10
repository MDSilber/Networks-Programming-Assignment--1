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
			Socket proxySocket = new Socket(args[0],port);
			
			System.out.println("Connected to proxy");
			
			Thread p1 = new Thread(new SenderToGUI(proxySocket));
			Thread p2 = new Thread(new ReceiverFromGUI(proxySocket));
			p1.start();
			p2.start();
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
	}

}
