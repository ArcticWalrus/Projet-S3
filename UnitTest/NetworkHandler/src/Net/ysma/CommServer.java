package Net.ysma;

import java.net.*;
import java.io.*;

public class CommServer extends Thread
{
    public final static int COMM_PORT = 8443; // socket port for client comms

    private ServerSocket _ssoServerSocket;
    private InetSocketAddress _isaInboundAddr;
    private SerialObj _seoPayload;
    public SerialObj _seoLastValid;


    boolean _bServerActive = false;

    public CommServer() {
        this._seoPayload = new SerialObj();
        this._seoLastValid = new SerialObj();
        _bServerActive = true;
    }

    private void init_ssoServerSocket() {
        try {
            this._isaInboundAddr = new InetSocketAddress(COMM_PORT);
            this._ssoServerSocket = new java.net.ServerSocket(COMM_PORT);
            assert this._ssoServerSocket.isBound();
            if (this._ssoServerSocket.isBound()) {
                System.out.println("SERVER inbound data port " + this._ssoServerSocket.getLocalPort()
                        + " is ready and waiting for client to connect...");
                _ssoServerSocket.setSoTimeout(1000);
            }
        } catch (SocketException se) {
            System.err.println("Unable to create socket.");
            System.err.println(se.toString());
            System.exit(1);
        } catch (IOException ioe) {
            System.err.println("Unable to read data from an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
    }

    public void run()
    {
        int CommID = 1;
        init_ssoServerSocket();

        System.out.println("STARTING Listener Thread...");

        while (_bServerActive)
        {
            try
            {
                // listen for and accept a client connection to _ssoServerSocket
                Socket sock = this._ssoServerSocket.accept();
                OutputStream oStream = sock.getOutputStream();
                ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
                ooStream.writeObject(CommID);
                InputStream iStream = sock.getInputStream();
                ObjectInputStream oiStream = new ObjectInputStream(iStream);
                this._seoPayload = (SerialObj) oiStream.readObject();    // convert serilized _seoPayload
                System.out.println("Received valid serialObj ");
                ooStream.close();
                CommID++;
                if (CommID == 65535)
                    CommID = 1;

                if (this._seoPayload._reqType != 0)    //Si l'objet est maintenant populé avec des nouvelles valeurs
                {
                    System.out.println("Un contenu valide demande maintenant la création d'un nouveau thread");
                    //Mettre l'appel de création de thread approprié ICI
                    this._seoLastValid = this._seoPayload;
                }
                else
                {
                    System.out.println("Aucune valeur valide n'a été observée dans l'objet reçu");
                }
                //Destruction de l'objet car il a été redirigé dans le nouveau thread
                this._seoPayload = new SerialObj();
                Thread.sleep(100);
            }
            catch(ClassNotFoundException cne)
            {
                System.out.println("Wanted class TcpPayload, but got class " + cne);
            }
            catch(SecurityException se)
            {
                System.err.println("Unable to get host address due to security.");
                System.err.println(se.toString());
                System.exit(1);
            }
            catch(SocketTimeoutException ste)
            {

            }
            catch(IOException ioe)
            {
                System.err.println("Unable to read data from an open socket.");
                System.err.println(ioe.toString());
                System.exit(1);
            }
            catch(InterruptedException ie)
            {
            } // Thread sleep interrupted
        }
        System.out.println("STOPPING Listener Thread...");
        try
        {
            this._ssoServerSocket.close();
        }
        catch (IOException ioe)
        {
            System.err.println("Unable to close an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
    }

    void commServerStop() {
        _bServerActive = false;
    }
}
