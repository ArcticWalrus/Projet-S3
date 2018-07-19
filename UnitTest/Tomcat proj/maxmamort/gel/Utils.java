package maxmamort.gel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Utils {
    //https://stackoverflow.com/questions/7940711/in-java-how-can-i-combine-two-json-arrays-of-objects
    public static JSONArray getMergeJson(ArrayList<JSONArray> xyz) {
        JSONArray result = null;
        JSONObject obj = new JSONObject();
        obj.put("key", result);
        for (JSONArray tmp : xyz) {
            for (int i = 0; i < tmp.length(); i++) {
                obj.append("key", tmp.getJSONObject(i));
            }
        }
        return obj.getJSONArray("key");
    }


    public static void sleep(int time) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
