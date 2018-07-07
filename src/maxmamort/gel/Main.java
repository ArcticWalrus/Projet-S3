package maxmamort.gel;

import maxmamort.gel.persistence.persistantLayer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Properties;

import Net.ysma.*;

import static Net.ysma.SerialObjInterface.ERRORPROCESS;

public class Main {
    public static void main(String[] args) {

        ApplicationLayer al = new ApplicationLayer();
        al.Create_Persistance_Layer();
        //al.Populate();

        al.seedPhysicalTest();
        //JSONArray jsonFinal = al.pl.getConditionsAndInputs(al.temp_test);

        //LMThread lm = new LMThread(jsonFinal);
    }
}
