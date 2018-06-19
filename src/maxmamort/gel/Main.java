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
        al.Populate();

        JSONArray jsonFinal = al.pl.getConditionsAndInputs(al.temp_test);

        //for (int i = 0; i < jsonFinal.length(); i++) {
          //  System.out.println(jsonFinal.getJSONObject(i).toString());
        //}

        //LMThread lm = new LMThread(jsonFinal);
               /*
        System.out.println("writer thread");
        CommClient cclWriter = new CommClient();
        cclWriter.setRequestType(ERRORPROCESS);
        cclWriter.setSourceIp("La mère à Josh");
        cclWriter.setTargetIp("La mère à Roger");
        cclWriter.setDataFrame(new JSONArray().put(new JSONObject().put("toto", "tata")));
        cclWriter.setDataFrameTime();
        cclWriter.start();
*/


    }
}
