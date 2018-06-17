import Net.ysma.*;

import static Net.ysma.SerialObjInterface.*;


public class Main_Application_Layer {

    public static void main(String[] args) {
        mainServerAddon msaServerObserver = new mainServerAddon();
        while (true) {
            if (msaServerObserver._booNewData) {
                msaServerObserver._booNewData = false;
                SerialObj srcTemp = msaServerObserver.getSerialObject();
                aiguilleur(srcTemp);
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println(e.toString());
                }
            }
            else {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

        }
    }

    static void aiguilleur(SerialObj temp) {
        if (temp.getRequestType() == APPSERVER) {
            System.out.println("Communication to APPSERVER");
        } else if (temp.getRequestType() == PERSISTANCE) {
            to_persistance(temp);
        } else if (temp.getRequestType() == LOGIC) {
            to_logic(temp);
        } else if (temp.getRequestType() == DEVICE) {
            to_device(temp);
        } else if (temp.getRequestType() == UI) {
            System.out.println("Communication to UI");
        } else if (temp.getRequestType() == LOGICSETUP) {
            to_logic(temp);
        } else if (temp.getRequestType() == ERRORPROCESS) {
            to_persistance(temp);
        } else {
            System.out.println("1. Ya done fucked up");
        }
    }

    static void to_persistance(SerialObj sro_temp) {
        CommClient pers_client = new CommClient("127.0.0.1", 45010);
        pers_client.setSerialObject(sro_temp);
        pers_client.start();
    }

    static void to_logic(SerialObj sro_temp) {
        CommClient lm_client = new CommClient("127.0.0.1", 45020);
        lm_client.setSerialObject(sro_temp);
        lm_client.start();
    }

    static void to_device(SerialObj sro_temp) {
        CommClient device_client = new CommClient(sro_temp.getTargetIp(), sro_temp.getTargetPort());
        device_client.setSerialObject(sro_temp);
        device_client.start();
    }
}
