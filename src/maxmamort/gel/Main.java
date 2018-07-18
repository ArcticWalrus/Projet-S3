package maxmamort.gel;

import maxmamort.gel.persistence.persistantLayer;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.rmi.CORBA.Util;
import java.util.ArrayList;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        ApplicationLayer al = new ApplicationLayer();
        al.Create_Persistance_Layer();
        
        //al.pl.clearDB();
        //System.out.println("Cleared dB");
        //Utils.sleep(5000);

        al.Populate();

        al.seedPhysicalTest();

        JSONArray jsonFinal = al.pl.getConditionsAndInputs(al.temp_test);

        LMThread lm = new LMThread(jsonFinal);
    }
}
