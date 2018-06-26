package Net2.ysma;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static Net2.ysma.SerialObjInterface.ERRORPROCESS;
import static Net2.ysma.SerialObjInterface.UNKNOWN;

public class LThread extends Thread {
    private Integer _intArrayIndex;
    private Socket _socClientLink;
    private boolean _booReadyToAnswer = false;
    private SerialObj _seoObjInbound;
    private SerialObj _seoObjOutbound;
    private List<CustomClassListener> listeners = new ArrayList<>();

    public LThread(Integer iThreadIndex, Socket sock) {
        _socClientLink = sock;
        _intArrayIndex = iThreadIndex;
    }

    public void run() {
        try {
            OutputStream oStream = _socClientLink.getOutputStream();
            ObjectOutputStream ooStream = new ObjectOutputStream(oStream);
            ooStream.writeObject(0);    //TODO get a valid int that makes sense
            InputStream iStream = _socClientLink.getInputStream();
            ObjectInputStream oiStream = new ObjectInputStream(iStream);
            this._seoObjInbound = (SerialObj) oiStream.readObject();    // convert serilized _seoPayload
            System.out.println("Received valid serialObj");
            if ((this._seoObjInbound.getTargetType() >= UNKNOWN) && (this._seoObjInbound.getTargetType() <= ERRORPROCESS))    //Si l'objet est maintenant populé avec des nouvelles valeurs et des champs valides
            {
                System.out.println("Un contenu valide demande maintenant la notification des listeners");
                //Mettre l'appel de création de thread approprié ICI
                this.callListeners();
                if (this._seoObjInbound.getIfFeedbackNeeded()) {
                    while (_booReadyToAnswer != true) {
                        //TODO add sleep here
                    }
                    ooStream = new ObjectOutputStream(oStream);
                    ooStream.writeObject(_seoObjOutbound);    //TODO get a valid int that makes sense
                }
            } else {
                System.out.println("Aucune valeur valide n'a été observée dans l'objet reçu");
            }
            ooStream.close();
        } catch (ClassNotFoundException cne) {
            System.out.println("LThread 1 Wanted class TcpPayload, but got class " + cne);
        } catch (Exception e) {
            System.out.println("LThread 2 " + e.toString());
        }
    }

    public void addListener(CustomClassListener toAdd) {
        listeners.add(toAdd);
    }

    private void callListeners() {
        System.out.println("New valid data received!");
        // Notify everybody that may be interested.
        for (CustomClassListener hl : listeners)
            hl.receivedNewDataFrame(_intArrayIndex);
    }

    public SerialObj getNewPayload() {
        return this._seoObjInbound;
    }

    public void setOutboundPayload(SerialObj ser) {
        this._seoObjOutbound = ser;
    }

    public void setOutboundFlagToSend() {
        _booReadyToAnswer = true;
    }
}
