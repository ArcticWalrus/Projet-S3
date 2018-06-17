import maxmamort.gel.persistence.*;
import Net.ysma.*;
import org.json.JSONObject;

import static Net.ysma.SerialObjInterface.*;

public class Main_Persistance_Layer {
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
            } else {
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            }

        }
    }

    static void aiguilleur(SerialObj temp) {
        Integer tempint = temp.getTargetType();
        if (tempint == PERSISTANCE) {

            to_persistance(temp);
        } else if (tempint == ERRORPROCESS) {
            to_error_logger(temp);
        } else {
            System.out.println("2. Ya done fucked up");
        }
    }

    static void to_persistance(SerialObj sro_temp) {
        String tempvalue = sro_temp.getRequestType();
        persistantLayer pl = new persistantLayer();
        JSONObject jo = sro_temp.getDataFrame().getJSONObject(0);
        if ("addInput".equalsIgnoreCase(tempvalue)) {
            pl.addInput(jo.getString("inputName"), jo.getDouble("defaultValue"), jo.getInt("sensorType"));
        } else if ("getInputIDForIO".equalsIgnoreCase(tempvalue)) {

        } else if ("getIOByDevice".equalsIgnoreCase(tempvalue)) {

        } else if ("createDevice".equalsIgnoreCase(tempvalue)) {

        } else if ("createUser".equalsIgnoreCase(tempvalue)) {

        } else if ("getDevicesByUser".equalsIgnoreCase(tempvalue)) {

        } else if ("getIOForUser".equalsIgnoreCase(tempvalue)) {

        } else if ("renameDevice".equalsIgnoreCase(tempvalue)) {

        } else if ("renameIO".equalsIgnoreCase(tempvalue)) {

        } else if ("updateConfigurationBit".equalsIgnoreCase(tempvalue)) {

        } else if ("updatePhysicalMapping".equalsIgnoreCase(tempvalue)) {

        } else if ("updateDeviceIP".equalsIgnoreCase(tempvalue)) {

        } else if ("createIO".equalsIgnoreCase(tempvalue)) {

        } else if ("updateOutputValue".equalsIgnoreCase(tempvalue)) {

        } else if ("createInputGroupCondition".equalsIgnoreCase(tempvalue)) {

        } else if ("updateValueOutputGroup".equalsIgnoreCase(tempvalue)) {

        } else if ("getConditionsandInputs".equalsIgnoreCase(tempvalue)) {

        }
    }

    static void to_error_logger(SerialObj sro_temp) {
        errorManager er = new errorManager();
        er.logError(sro_temp.toString(), "stjm2505");
    }

}
