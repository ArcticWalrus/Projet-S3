package maxmamort.gel.persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Utils {
    //https://stackoverflow.com/questions/7940711/in-java-how-can-i-combine-two-json-arrays-of-objects

    /**
     * @brief Merge multiple JSONArray together
     * @param xyz The ArrayList of JSONArray to merge
     * @return the new merged JSONArray
     */
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
}
