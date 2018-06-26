package Net2.ysma;

import maxmamort.gel.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static Net.ysma.SerialObjInterface.*;

public class FrameworkApplication {
    public List<InfoProcess> _lisThread;
    public WriterAddon _wraObserver;
    private boolean _booAppOn = true;

    private static final int OUTPUT = 1;
    private static final int INPUT = 0;

    public FrameworkApplication() {
        //Le constructeur WriterAddon(int iport) peut être utilisé pour être sur autre port que 45000
        _wraObserver = new WriterAddon(45010);
    }

    public void startApp() {
        //populate();
        //Code to implement where a Listener is required #2
        while (_booAppOn) {
            System.out.println("get object");
            Net.ysma.SerialObj serTemp = _wraObserver.getSerialObject();
            System.out.println("get object done");

            if (serTemp != null) {
                System.out.println("FrameworkApp 1 new object received");
                InfoProcess ifpTemp = new InfoProcess(serTemp);
                if (serTemp.getIfFeedbackNeeded()) {
                    Net.ysma.SerialObj serOutbound = new Net.ysma.SerialObj();
                    serOutbound = ifpTemp.Aiguilleur(serTemp);
                    System.out.println("Aiguilleur a retourner sa valeur");
                    //Doit relayer l'information au LThread
                    _wraObserver.setAnswer(serOutbound, _wraObserver.getIndexOfObject(serTemp));
                    System.out.println("LThread a recu sa reponse");
                } else {
                    ifpTemp.Aiguilleur(serTemp);
                }
            }
            Utils.sleep(5);
        }
        _wraObserver.stopServer();

        //end of code #2
        System.out.println("L'application de persistence ferme...");
    }

    public void stopApp() {
        _booAppOn = false;
    }

    static Net.ysma.SerialObj sendGetPersistanceBlocking(Net.ysma.SerialObj obj, String reqType) {
        obj.setTargetType(PERSISTANCE);
        obj.setRequestType(reqType);
        obj.setIfFeedbackNeeded(true);

        CommClient cc = new CommClient("127.0.0.1", 45010);
        cc.setSerialObject(obj);
        cc.start();
        while (!cc.getIfDataReceived()) {
            Utils.sleep(10);
        }
        cc.setReceivedDataRead();
        return cc.getInputBuffer();
    }
}
