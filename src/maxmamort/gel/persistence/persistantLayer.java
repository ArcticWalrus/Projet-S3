package maxmamort.gel.persistence;

import maxmamort.gel.Utils;
import maxmamort.gel.persistence.dbAccess;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class persistantLayer {
    public int addInput(String inputName, double defaultValue, int sensorType) {
        int returnValue = -1;
        String sql = "INSERT INTO public.intinput VALUES(DEFAULT, '" + inputName + "' , '" + defaultValue + "','" + sensorType + "') RETURNING serintinput;";
        dbAccess db = new dbAccess();
        returnValue = db.insertGetIdQuery(sql, "serintinput");
        db.closeConnection();
        return returnValue;
    }

    public int createInputGroup(int[] inputIds) {
        int returnValue = -1;
        String sql = "INSERT INTO public.inputgroup (serinputgroup, conditiongroup, inputid, ordre) VALUES(DEFAULT, '" + "-1" + "' , '" + inputIds[0] + "',0) RETURNING serinputgroup;";
        dbAccess db = new dbAccess();
        returnValue = db.insertGetIdQuery(sql, "serinputgroup");
        //TODO remove update and replace with something that tells sql to put id in conditiongroup
        sql = "UPDATE public.inputgroup SET conditiongroup = " + returnValue + " WHERE serinputgroup = " + returnValue;
        db.updateQuery(sql);
        //   System.out.println(returnValue);
        for (int i = 1; i < inputIds.length; i++) {
            //TODO merge sql queries with array rather than each individual call
            sql = "INSERT INTO public.inputgroup (serinputgroup, conditiongroup, inputid, ordre) VALUES(DEFAULT, '" + returnValue + "' , '" + inputIds[i] + "','" + i + "');";
            db.insertQuery(sql);
        }
        db.closeConnection();
        return returnValue;
    }

    public int createCondition(int inputGroup, int outputGroup, int conditionType) {
        int returnValue = -1;
        dbAccess db = new dbAccess();
        String sql = "INSERT INTO public.conditions (serconditions, inputgroup, outputgroup, operation) VALUES(DEFAULT , '" + inputGroup + "','" + outputGroup + "','" + conditionType + "' ) RETURNING serconditions;";
        returnValue = db.insertGetIdQuery(sql, "setconditions");
        db.closeConnection();
        return returnValue;
    }

    public JSONArray getConditionsAndInputs(int inputId) {
        JSONArray json = null;

        ArrayList<JSONArray> conditions = new ArrayList<>();
        ArrayList<JSONArray> inputGroups = new ArrayList<>();
        ArrayList<JSONArray> outputs = new ArrayList<>();

        dbAccess db = new dbAccess();
        json = db.selectQuery("SELECT * FROM public.inputgroup WHERE inputid = '" + inputId + "'");
        System.out.println("Input group for given input is: " + json.toString());
        List<Integer> conditionGroupId = new ArrayList<>();

        //Get conditions assosciated with input
        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = json.getJSONObject(i);
            conditionGroupId.add(obj.getInt("conditiongroup"));// input condition id addition
            // System.out.println("Condition ID: " + conditionGroupId.get(i));
            conditions.add(db.selectQuery("SELECT * FROM public.conditions WHERE inputgroup = " + conditionGroupId.get(i)));
        }
        //  for(int i = 0; i < conditions.size(); i++ ){
        //     System.out.println("data is: " + conditions.get(i).toString());
        //}
        //   System.out.println(conditions.toString() + "\n\n");
        json = Utils.getMergeJson(conditions);

        //Get ouput group
        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = json.getJSONObject(i);
            conditionGroupId.add(obj.getInt("outputgroup"));
            System.out.println("Output group id is: " + obj.getInt("outputgroup"));
        }

        //Get inputGroups with conditions
        for (int i = 0; i < conditionGroupId.size(); i++) {
            inputGroups.add(db.selectQuery("SELECT * FROM public.inputgroup WHERE conditiongroup = " + conditionGroupId.get(i)));
        }
        json = Utils.getMergeJson(inputGroups);

        //Get inputs associated with groupid
        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = json.getJSONObject(i);
            int id = obj.getInt("inputid");
            outputs.add(db.selectQuery("SELECT * FROM public.intinput WHERE serintinput = " + id));
        }

        //Combine all elements
        conditions.addAll(inputGroups);
        conditions.addAll(outputs);
        json = Utils.getMergeJson(conditions);
        return json;
    }
}
