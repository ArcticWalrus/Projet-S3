package Net2.ysma;

import maxmamort.gel.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static Net.ysma.SerialObjInterface.APPSERVER;
import static Net.ysma.SerialObjInterface.PERSISTANCE;

public class FrameworkApplication {
    public List<InfoProcess> _lisThread;
    public WriterAddon _wraObserver;
    private boolean _booAppOn = true;

    private static final int OUTPUT = 1;
    private static final int INPUT = 0;

    public FrameworkApplication() {
        //Code to implement where a Listener is required #1
        //Le constructeur WriterAddon(int iport) peut être utilisé pour être sur autre port que 45000
        _wraObserver = new WriterAddon(45010);
        //end of code #1

    }

    public void startApp() {
        System.out.println("Populating test db");
        populate();
        System.out.println("Done populating");


        //Code to implement where a Listener is required #2
        while (_booAppOn) {
            SerialObj serTemp = _wraObserver.getSerialObject();
            if (serTemp != null) {
                System.out.println("FrameworkApp 1 new object received");
                InfoProcess ifpTemp = new InfoProcess(serTemp);
                if (serTemp.getIfFeedbackNeeded()) {
                    SerialObj serOutbound = new SerialObj();
                    serOutbound = ifpTemp.Aiguilleur(serTemp);
                    //Doit relayer l'information au LThread
                    _wraObserver.setAnswer(serOutbound, _wraObserver.getIndexOfObject(serTemp));
                } else {
                    ifpTemp.Aiguilleur(serTemp);
                }

                //Add code to do with new serial object (Start thread or other)

            }
            //TODO mettre sleep a max
            try {
                Thread.sleep(100);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        _wraObserver.stopServer();

        //end of code #2
        System.out.println("L'application ferme...");
    }

    public void stopApp() {
        _booAppOn = false;
    }

    private void populate() {
        SerialObj temp_obj = new SerialObj();
        temp_obj.setDataFrame(new JSONArray().put(new JSONObject().put("inputName", "Test input").put("defaultValue", 22.5).put("sensorType", INPUT)));
        temp_obj.setTargetType(PERSISTANCE);
        temp_obj.setRequestType("addInput");

        //TODO refractor
        CommClient cc = new CommClient("127.0.0.1", 45010);
        cc.setSerialObject(temp_obj);
        cc.setIfFeedbackNeeded(true);
        cc.start();
        while (!cc.getIfDataReceived()) {
            Utils.sleep(10);
        }
        temp_obj = cc.getInputBuffer();
        cc.setReceivedDataRead();

        System.out.println(temp_obj.getDataFrame().toString());
        System.out.println("Done populating");
    }
}
