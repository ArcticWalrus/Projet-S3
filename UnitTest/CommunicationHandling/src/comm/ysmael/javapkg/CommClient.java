package comm.ysmael.javapkg;

import java.net.*;
import java.io.*;

public class CommClient
{
	 public final static String SERVER_HOSTNAME = "gsoler.arc.nasa.gov";
	    public final static int COMM_PORT = 5050;  // socket port for client comms

	    private Socket _socSocket;
	    private int _intPayload;
	    public SerialObj _seoToSend;

	    /** Default constructor. */
	    public CommClient()
	    {
	    	this._seoToSend = new SerialObj();
	    }
	    
	    public boolean sendData()
	    {
	    	try
	        {
	            this._socSocket = new Socket(SERVER_HOSTNAME, COMM_PORT);	//Ouvre la connection vers un serveur distant
	            
	            //Attends la réponse du serveur distant
	            InputStream iStream = this._socSocket.getInputStream();
	            ObjectInputStream oiStream = new ObjectInputStream(iStream);
	            this._intPayload = (int) oiStream.readObject();
	            System.out.println("Received server acknowledge: Id = " + String.valueOf(this._intPayload));
	            
	            //Envoi de l'objet sérialisé
            	OutputStream oStream = this._socSocket.getOutputStream();
				ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
				ooStream.writeObject(_seoToSend);
	        }
	        catch (UnknownHostException uhe)
	        {
	            System.out.println("Don't know about host: " + SERVER_HOSTNAME);
	            return false;
	        }
	        catch (IOException ioe)
	        {
	            System.out.println("Couldn't get I/O for the connection to: " +
	                SERVER_HOSTNAME + ":" + COMM_PORT);
	            return false;
	        }
	        catch(ClassNotFoundException cne)
	        {
	            System.out.println("Wanted class int, but got class " + cne);
	            return false;
	        }
	    	return true;
	    }
}
