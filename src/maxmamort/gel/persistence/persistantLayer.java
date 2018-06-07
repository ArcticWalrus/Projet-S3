/**
 * @File: persistantLayer.java
 * @Author: Maxim Bolduc
 * @Date: 2018-05-31
 * @Brief: Fonctions customs de la couche de persistance effectuant la logique de persistance de l'application
 */

//TODO comment out this class

package maxmamort.gel.persistence;

import maxmamort.gel.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class persistantLayer implements IpersistantLayer {

    /**
     * @param inputName    Name of the input
     * @param defaultValue Value to add as the default one
     * @param sensorType   1 for output, 0 for input. More types to come
     * @return The Id of the newly added input
     * @brief Add input to database
     */
    public int addInput(String inputName, double defaultValue, int sensorType) {
        int returnValue = -1;
        String sql = "INSERT INTO public.intinput VALUES(DEFAULT, '" + inputName + "' , '" + defaultValue + "','" + sensorType + "') RETURNING serintinput;";
        dbAccess db = new dbAccess();
        returnValue = db.insertGetIdQuery(sql, "serintinput");
        db.closeConnection();
        return returnValue;
    }

    /**
     * @param outputId The Id of the output that has to be updated
     * @param value    The new value of that output
     * @return True if everything worked, false if failed
     * @brief Update the value of an output
     */
    public boolean updateOutputValue(int outputId, double value) {
        dbAccess db = new dbAccess();
        String query = "UPDATE public.intinput SET value='" + value + "' WHERE serintinput = '" + outputId + "'";
        db.updateQuery(query);
        db.closeConnection();
        return db.isError();
    }

    /**
     * @param inputIds array of inputId
     * @return The iD of the inputGroup created
     * @brief Create an input group from a list of inputId
     */
    public int createInputGroup(int[] inputIds) {
        int returnValue = -1;
        String sql = "INSERT INTO public.inputgroup (serinputgroup, conditiongroup, inputid, ordre) VALUES(DEFAULT, '" + "-1" + "' , '" + inputIds[0] + "',0) RETURNING serinputgroup;";
        dbAccess db = new dbAccess();
        returnValue = db.insertGetIdQuery(sql, "serinputgroup");
        sql = "UPDATE public.inputgroup SET conditiongroup = " + returnValue + " WHERE serinputgroup = " + returnValue;
        db.updateQuery(sql);
        //   System.out.println(returnValue);
        for (int i = 1; i < inputIds.length; i++) {
            sql = "INSERT INTO public.inputgroup (serinputgroup, conditiongroup, inputid, ordre) VALUES(DEFAULT, '" + returnValue + "' , '" + inputIds[i] + "','" + i + "');";
            db.insertQuery(sql);
        }
        db.closeConnection();
        return returnValue;
    }

    /**
     * @param inputGroup    The Id of the inputGroup
     * @param outputGroup   The Id of the output group
     * @param conditionType The type of condition (see logic module for the different kinds)
     * @return The Id of the newly created Condition
     * @brief Create a new condition
     */
    public int createCondition(int inputGroup, int outputGroup, int conditionType) {
        int returnValue = -1;
        dbAccess db = new dbAccess();
        String sql = "INSERT INTO public.conditions (serconditions, inputgroup, outputgroup, operation) VALUES(DEFAULT , '" + inputGroup + "','" + outputGroup + "','" + conditionType + "' ) RETURNING serconditions;";
        returnValue = db.insertGetIdQuery(sql, "setconditions");
        db.closeConnection();
        return returnValue;
    }

    /**
     * @param jsonUpdate Json containing the id of the outputgroupid and the newvalue
     * @return
     */
    public boolean updateValueOutputGroup(JSONArray jsonUpdate) {
        dbAccess db = new dbAccess();
        JSONArray json = db.selectQuery("SELECT * FROM public.inputgroup WHERE conditiongroup = " + jsonUpdate.getJSONObject(0).getInt("outputgroupid"));
        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = json.getJSONObject(i);
            int id = obj.getInt("inputid");
            updateOutputValue(id, jsonUpdate.getJSONObject(0).getInt("newvalue"));
        }
        db.closeConnection();
        return db.isError();
    }

    /**
     * @param inputId The Id of the updated input. Will be used to get the right conditions
     * @return The JSONArray representing the conditions and what is needed to handle them
     * @brief Get the conditions and what is needed to manage them from an InputID
     */
    public JSONArray getConditionsAndInputs(int inputId) {
        JSONArray json = null;//result JSON

        ArrayList<JSONArray> conditions = new ArrayList<>();
        ArrayList<JSONArray> inputGroups = new ArrayList<>();
        ArrayList<JSONArray> outputs = new ArrayList<>();

        dbAccess db = new dbAccess();
        json = db.selectQuery("SELECT * FROM public.inputgroup WHERE inputid = '" + inputId + "'");
        //System.out.println("Input group for given input is: " + json.toString());
        List<Integer> conditionGroupId = new ArrayList<>();

        //Get conditions assosciated with input
        for (int i = 0; i < json.length(); i++) {
            JSONObject obj = json.getJSONObject(i);
            conditionGroupId.add(obj.getInt("conditiongroup"));// input condition id addition
            // System.out.println("Condition ID: " + conditionGroupId.get(i));
            conditions.add(db.selectQuery("SELECT * FROM public.conditions WHERE inputgroup = " + conditionGroupId.get(i)));
        }

        json = maxmamort.gel.Utils.getMergeJson(conditions);

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
        json = maxmamort.gel.Utils.getMergeJson(inputGroups);

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
