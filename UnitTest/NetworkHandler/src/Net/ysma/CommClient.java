package Net.ysma;

import java.io.*;
import java.net.*;

public class CommClient extends Thread
{
    private String SERVER_HOSTNAME;
    private int COMM_PORT;  // socket port for client comms

    private Socket _socSocket;
    private int _intPayload;
    public SerialObj _seoToSend;

    /** Default constructor. */
    public CommClient()
    {
        this.SERVER_HOSTNAME = "127.0.0.1";
        this.COMM_PORT = 8443;  // socket port for client comms
        this._seoToSend = new SerialObj();
    }

    public CommClient(String strHostName)
    {
        this.SERVER_HOSTNAME = strHostName;
        this.COMM_PORT = 8443;  // socket port for client comms
        this._seoToSend = new SerialObj();
    }

    public CommClient(String strHostName, int iPort)
    {
        this.SERVER_HOSTNAME = strHostName;
        this.COMM_PORT = iPort;  // socket port for client comms
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
