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
import java.util.Random;

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
        String sql = "INSERT INTO public.intinput ( serintinput, namname, valvalue, valtype )  VALUES(DEFAULT, '" + inputName + "' , '" + defaultValue + "','" + sensorType + "') RETURNING serintinput;";
        dbAccess db = new dbAccess();
        returnValue = db.insertGetIdQuery(sql, "serintinput");
        db.closeConnection();
        return returnValue;
    }

    public void bindInputToIO(int inputId, int IOId) {
        //TODO write method
    }


    /**
     * @param outputId The Id of the output that has to be updated
     * @param value    The new value of that output
     * @return True if everything worked, false if failed
     * @brief Update the value of an output
     */
    public boolean updateOutputValue(int outputId, double value) {
        dbAccess db = new dbAccess();
        String query = "UPDATE public.intinput SET valvalue='" + value + "' WHERE serintinput = '" + outputId + "'";
        db.updateQuery(query);
        db.closeConnection();
        return db.isError();
    }

    /**
     * @param inputIds array of inputId
     * @return The iD of the inputGroup created
     * @brief Create an input group from a list of inputId
     */
    public int createInputGroupCondition(int[] inputIds, int operation) {
        int returnValue = -1;
        dbAccess db = new dbAccess();
        String sql = "INSERT INTO public.inputgroup(serinputgroup, namconditiongroup , valinputid, valordre, valoperation) VALUES (DEFAULT, DEFAULT, " + inputIds[0] + ", 0," + operation + ") RETURNING namconditiongroup;";
        returnValue = db.insertGetIdQuery(sql, "namconditiongroup");
        for (int i = 1; i < inputIds.length; i++) {
            db.insertQuery("INSERT INTO public.inputgroup(serinputgroup, namconditiongroup , valinputid, valordre, valoperation) VALUES (DEFAULT, " + returnValue + ", " + inputIds[i] + ", " + i + " ," + operation + ");");
        }
        db.insertQuery(sql);
        return returnValue;
    }

    /**
     * @param jsonUpdate Json containing the id of the outputgroupid and the newvalue
     * @return
     */
    public boolean updateValueOutputGroup(JSONArray jsonUpdate) {
        dbAccess db = new dbAccess();
        JSONArray json = db.selectQuery("SELECT * FROM public.inputgroup WHERE namconditiongroup = " + jsonUpdate.getJSONObject(0).getInt("outputgroupid"));
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
        dbAccess db = new dbAccess();
        JSONArray json = null;//result JSON
        String sql = "SELECT namconditiongroup FROM public.inputgroup WHERE valinputid = " + inputId;
        json = db.selectQuery(sql);
        sql = "SELECT * FROM public.inputoutputconditions WHERE ";
        for (int i = 0; i < json.length(); i++) {
            sql += " namconditiongroup = " + Integer.toString(json.getJSONObject(i).getInt("namconditiongroup"));
            if (i != json.length() - 1) {
                sql += " OR ";
            }
        }
        sql += ";";
        return db.selectQuery(sql);
    }
}
