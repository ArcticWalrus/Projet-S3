import Net.ysma.*;
import maxmamort.gel.persistence.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main_Persistance_Layer
{
    private static FrameworkApplication _fapMainApp;

    public static void main(String[] args)
    {
//        _fapMainApp = new FrameworkApplication(2);
//        _fapMainApp.startApp();
//        System.out.println("fin du programme");

        //Extraction for html tables
        persistantLayer pl = new persistantLayer();
        JSONArray json = pl.getConditions();
        System.out.println(json.toString());

        pl.createCondition("123", 1, 1, 2, 1);
        JSONArray json1 = pl.getConditions();
        System.out.println(json1.toString());


    }
}
