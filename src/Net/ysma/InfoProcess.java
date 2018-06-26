
package Net.ysma;

import static Net.ysma.SerialObjInterface.*;

public class InfoProcess {
    private SerialObj _seoObjSerie;

    public InfoProcess(SerialObj obj) {
        _seoObjSerie = obj;
    }

    public SerialObj Aiguilleur(SerialObj temp) {
        System.out.println("Aiguilleur de mainApp");

        SerialObj seoOutbound = new SerialObj();
        Integer tempint = temp.getTargetType();
        if (tempint == APPSERVER) {
            System.out.println("Communication to APPSERVER");
        } else if (tempint == PERSISTANCE) {
            to_persistance(temp);
        } else if (tempint == LOGIC) {
            to_logic(temp);
        } else if (tempint == DEVICE) {
            to_device(temp);
        } else if (tempint == UI) {
            System.out.println("Communication to UI");
        } else if (tempint == ERRORPROCESS) {
            to_persistance(temp);
        } else {
            System.out.println("1. Ya done fucked up");
        }
        return seoOutbound;
    }


    //ADD methods of processing from each concepts down here
    private void to_persistance(SerialObj sro_temp) {
        CommClient pers_client = new CommClient("127.0.0.1", 45010);
        pers_client.setSerialObject(sro_temp);
        pers_client.start();
    }

    private void to_logic(SerialObj sro_temp) {
        CommClient lm_client = new CommClient("127.0.0.1", 45020);
        lm_client.setSerialObject(sro_temp);
        lm_client.start();
    }

    private void to_device(SerialObj sro_temp) {
        CommClient device_client = new CommClient(sro_temp.getTargetIp(), sro_temp.getTargetPort());
        device_client.setSerialObject(sro_temp);
        device_client.start();
    }

}
