import Net2.ysma.SerialObj;
import maxmamort.gel.persistence.*;
import Net2.ysma.*;
import org.json.JSONArray;
import org.json.JSONObject;

import Net2.ysma.FrameworkApplication;

public class Main_Persistance_Layer {
    private static FrameworkApplication _fapMainApp;

    public static void main(String[] args) {
        _fapMainApp = new FrameworkApplication();
        _fapMainApp.startApp();
        System.out.println("fin du programme");

        /*mainServerAddon msaServerObserver = new mainServerAddon();
        while (true) {
            if (msaServerObserver._booNewData) {
                msaServerObserver._booNewData = false;
                SerialObj srcTemp = msaServerObserver.getSerialObject();
                JSONArray json = aiguilleur(srcTemp);


                Utils.sleep(5);//TODO removed by stacking all incoming requests and looping till done
            } else {
                Utils.sleep(100);
            }
        }*/
    }


}
