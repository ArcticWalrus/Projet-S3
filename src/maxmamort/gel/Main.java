package maxmamort.gel;

import maxmamort.gel.persistence.persistantLayer;
import org.json.JSONArray;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        ApplicationLayer al = new ApplicationLayer();
        al.Create_Persistance_Layer();
        al.Populate();

        JSONArray jsonFinal = al.pl.getConditionsAndInputs(al.temp_test);

        for (int i = 0; i < jsonFinal.length(); i++) {
            System.out.println(jsonFinal.getJSONObject(i).toString());
        }

        LMThread lm = new LMThread(jsonFinal);

    }
}
