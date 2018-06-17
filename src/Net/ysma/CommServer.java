package Net.ysma;

import maxmamort.gel.Utils;
import org.json.JSONArray;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CommServer extends Thread {
    private int COMM_PORT; // socket port for client comms

    private ServerSocket _ssoServerSocket;
    private InetSocketAddress _isaInboundAddr;
    private SerialObj _seoPayload;
    public SerialObj _seoLastValid;
    private List<NewDataFrameListener> listeners = new ArrayList<NewDataFrameListener>();

    private boolean _bServerActive = false;

    //Pour le permettre le feedback
    private boolean _booReturnDone;
    private JSONArray _jsaReturnValue;

    public CommServer() {
        this.COMM_PORT = 45000; // socket port for client comms
        this._seoPayload = new SerialObj();
        this._seoLastValid = new SerialObj();
        this._jsaReturnValue = new JSONArray();
        this._booReturnDone = false;
        this._bServerActive = true;
    }

    public CommServer(int iPort) {
        COMM_PORT = iPort; // socket port for client comms
        this._seoPayload = new SerialObj();
        this._seoLastValid = new SerialObj();
        this._jsaReturnValue = new JSONArray();
        this._booReturnDone = false;
        this._bServerActive = true;
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

    public void run() {
        int CommID = 1;
        System.out.println("STARTING Listener Thread...");
        init_ssoServerSocket();

        while (_bServerActive) {
            try {
                // listen for and accept a client connection to _ssoServerSocket
                Socket sock = this._ssoServerSocket.accept();
                OutputStream oStream = sock.getOutputStream();
                ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
                ooStream.writeObject(CommID);
                InputStream iStream = sock.getInputStream();
                ObjectInputStream oiStream = new ObjectInputStream(iStream);
                this._seoPayload = (SerialObj) oiStream.readObject();    // convert serilized _seoPayload
                System.out.println("Received valid serialObj ");
                if (this._seoPayload.getTargetType() != 10)    //Si l'objet est maintenant populé avec des nouvelles valeurs
                {
                    System.out.println("Un contenu valide demande maintenant la notification des listeners");
                    //Mettre l'appel de création de thread approprié ICI
                    this._seoLastValid = this._seoPayload;
                    //Destruction de l'objet car il a été redirigé dans le nouveau thread
                    this._seoPayload = new SerialObj();
                    this.callListeners();
                    if (this._seoLastValid.getIfFeedbackNeeded()) {
                        Date datStartTime = new Date();
                        while ((!_booReturnDone) || ((datStartTime.getTime() + 5000) >= (new Date().getTime()))) {
                            Utils.sleep(10);
                        }
                        if (_booReturnDone) {
                            _booReturnDone = false;
                            ooStream = new ObjectOutputStream(oStream);
                            ooStream.writeObject(_jsaReturnValue);
                        } else {
                            System.out.println("Aucune réponse n'a été reçue assez rapidement... Answer Timeout Exception");
                        }
                    }
                } else {
                    System.out.println("Aucune valeur valide n'a été observée dans l'objet reçu");
                }
                ooStream.close();
                CommID++;
                if (CommID == 65535)
                    CommID = 1;
                Thread.sleep(100);
            } catch (ClassNotFoundException cne) {
                System.out.println("Wanted class TcpPayload, but got class " + cne);
            } catch (SecurityException se) {
                System.err.println("Unable to get host address due to security.");
                System.err.println(se.toString());
                System.exit(1);
            } catch (SocketTimeoutException ste) {

            } catch (IOException ioe) {
                System.err.println("Unable to read data from an open socket.");
                System.err.println(ioe.toString());
                System.exit(1);
            } catch (InterruptedException ie) {
            } // Thread sleep interrupted
        }
        System.out.println("STOPPING Listener Thread...");
        try {
            this._ssoServerSocket.close();
        } catch (IOException ioe) {
            System.err.println("Unable to close an open socket.");
            System.err.println(ioe.toString());
            System.exit(1);
        }
    }

    public void addListener(NewDataFrameListener toAdd) {
        listeners.add(toAdd);
    }

    private void callListeners() {
        System.out.println("New valid data received!");
        // Notify everybody that may be interested.
        for (NewDataFrameListener hl : listeners)
            hl.receivedNewDataFrame();
    }

    public SerialObj getNewPayload() {
        return this._seoLastValid;
    }

    private void setReturnReady() {
        _booReturnDone = true;
    }

    public void setReturnArray(JSONArray arr)
    {
        _jsaReturnValue = arr;
        this.setReturnReady();
    }

    public void commServerStop() {
        _bServerActive = false;
    }
}
