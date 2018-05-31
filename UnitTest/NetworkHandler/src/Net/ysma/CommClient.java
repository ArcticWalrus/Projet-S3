package Net.ysma;

import java.net.*;
import java.io.*;

public class CommClient extends Thread
{
    public final static String SERVER_HOSTNAME = "127.0.0.1";
    public final static int COMM_PORT = 8442;  // socket port for client comms

    private Socket _socSocket;
    private int _intPayload;
    public SerialObj _seoToSend;

    /** Default constructor. */
    public CommClient()
    {
        this._seoToSend = new SerialObj();
    }

    public void run()
    {
        System.out.println("STARTING Sender Thread...");
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
        }
        catch (IOException ioe)
        {
            System.out.println("Couldn't get I/O for the connection to: " +
                    SERVER_HOSTNAME + ":" + COMM_PORT);
        }
        catch(ClassNotFoundException cne)
        {
            System.out.println("Wanted class int, but got class " + cne);
        }
        System.out.println("STOPPING Sender Thread...");
    }
}
