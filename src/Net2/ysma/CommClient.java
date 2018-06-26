package Net2.ysma;

import org.json.JSONArray;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommClient extends Thread {
    private String SERVER_HOSTNAME;
    private int COMM_PORT;  // socket port for client comms

    private Socket _socSocket;
    private int _intPayload;
    private Net.ysma.SerialObj _seoToSend;
    private Net.ysma.SerialObj _seoToReceive;
    private boolean _booDataReceived;

    /**
     * Constructeur par défault qui pointe le target hostname "localhost"
     * et le port 45000
     */
    public CommClient() {
        this.SERVER_HOSTNAME = "127.0.0.1";
        this.COMM_PORT = 45000;  // socket port for client comms
        this._seoToSend = new Net.ysma.SerialObj();
    }

    /**
     * Constructeur qui reçoit une ip spécifique mais conserve le port du "Application CORE"
     *
     * @param strHostName Permet de spécifier un hostname différent de "localhost"
     */
    public CommClient(String strHostName) {
        this.SERVER_HOSTNAME = strHostName;
        this.COMM_PORT = 45000;  // socket port for client comms
        this._seoToSend = new Net.ysma.SerialObj();
    }

    /**
     * Constucteur qui doit être utilisé pour viser un autre module que le "Core"
     *
     * @param strHostName Spécifie l'adresse IP target
     * @param iPort       Sécifie le port qui doit être pointé
     */
    public CommClient(String strHostName, int iPort) {
        this.SERVER_HOSTNAME = strHostName;
        this.COMM_PORT = iPort;  // socket port for client comms
        this._seoToSend = new Net.ysma.SerialObj();
    }

    /**
     * Application qui sera initialisé par le thread start
     */
    public void run() {
        System.out.println("STARTING Writer Thread...");
        try {
            this._socSocket = new Socket(SERVER_HOSTNAME, COMM_PORT);    //Ouvre la connection vers un serveur distant

            //Attends la réponse du serveur distant
            InputStream iStream = this._socSocket.getInputStream();
            ObjectInputStream oiStream = new ObjectInputStream(iStream);
            this._intPayload = (int) oiStream.readObject();
            System.out.println("Received server acknowledge: Id = " + String.valueOf(this._intPayload));

            //Envoi de l'objet sérialisé
            OutputStream oStream = this._socSocket.getOutputStream();
            ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
            ooStream.writeObject(_seoToSend);
            if (_seoToSend.getIfFeedbackNeeded()) {
                iStream = this._socSocket.getInputStream();
                oiStream = new ObjectInputStream(iStream);
                this._seoToReceive = (Net.ysma.SerialObj) oiStream.readObject();
                _booDataReceived = true;
                while (_booDataReceived) {
                    //TODO put sleep a Max
                }
            }
        } catch (UnknownHostException uhe) {
            System.out.println("Don't know about host: " + SERVER_HOSTNAME);
        } catch (IOException ioe) {
            System.out.println("Couldn't get I/O for the connection to: " +
                    SERVER_HOSTNAME + ":" + COMM_PORT);
        } catch (ClassNotFoundException cne) {
            System.out.println("Wanted class int, but got class " + cne);
        }
        System.out.println("STOPPING Sender Thread...");
    }

    public Net.ysma.SerialObj getInputBuffer() {
        return this._seoToReceive;
    }

    public boolean getIfDataReceived() {
        return this._booDataReceived;
    }

    public void setReceivedDataRead() {
        _booDataReceived = false;
    }

    //Méthodes d'accès pour populer l'objet série ci-dessous

    /**
     * Permet d'écrire l'adresse d'origine dans le produit de communication
     *
     * @param str
     */
    public void setSourceIp(String str) {
        _seoToSend.setSourceIp(str);
    }

    /**
     * Permet d'écrire l'adresse visée dans le produit de communication
     *
     * @param str
     */
    public void setTargetIp(String str) {
        _seoToSend.setTargetIp(str);
    }

    /**
     * Permet de mettre le timestamp dans le produit de communication
     */
    public void setDataFrameTime() {
        _seoToSend.setDataFrameTime();
    }

    /**
     * Permet de populer le fichier JSON dans le produit de communication
     *
     * @param arr Reçoit le contenu sous forme de JSONArray pour en permettre la transmission
     * @return Une booléenne pour confirmer la réussite de l'opération
     */
    public void setDataFrame(JSONArray arr) {
        _seoToSend.setDataFrame(arr);
    }

    /**
     * Permet d'indiquer le type de trame véhiculée
     *
     * @param rt Doit être un Integer qui est dans le range proposé dans l'interface de l'objet série
     * @return Booléenne qui confirme la réussite de de l'enregistrement du type
     */
    public void setRequestType(Integer rt) {
        _seoToSend.setTargetType(rt);
    }

    public void setIfFeedbackNeeded(boolean boo) {
        _seoToSend.setIfFeedbackNeeded(boo);
    }

    public void setSerialObject(Net.ysma.SerialObj obj) {
        _seoToSend = obj;
    }
}
