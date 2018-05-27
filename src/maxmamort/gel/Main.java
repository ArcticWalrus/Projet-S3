package maxmamort.gel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
import java.util.Date;

public class Main {

    public static int addInput(String inputName, double defaultValue, int sensorType) {
        int returnValue = -1;
        String sql = "INSERT INTO public.intinput VALUES(DEFAULT, '" + inputName + "' , '" + defaultValue + "','" + sensorType + "') RETURNING serintinput;";
        dbAccess db = new dbAccess();
        returnValue = db.insertGetIdQuery(sql, "serintinput");
        db.closeConnection();
        return returnValue;
    }

    public static int createInputGroup(int[] inputIds) {
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

    public static int createCondition(int inputGroup, int outputGroup, int conditionType) {
        int returnValue = -1;
        dbAccess db = new dbAccess();
        String sql = "INSERT INTO public.conditions (serconditions, inputgroup, outputgroup, operation) VALUES(DEFAULT , '" + inputGroup + "','" + outputGroup + "','" + conditionType + "' ) RETURNING serconditions;";
        returnValue = db.insertGetIdQuery(sql, "setconditions");
        db.closeConnection();
        return returnValue;
    }

    public static JSONArray getConditionsAndInputs(int inputId) {
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

    public static void main(String[] args) {
        int temp = addInput("Test input", 22.5, 0);
        int temp2 = addInput("Input2", 21.2, 0);

        int inputGroup = createInputGroup(new int[]{temp, temp2});

        int temp3 = addInput("output1", 1.3, 1);
        int outputGroup = createInputGroup(new int[]{temp3});

        int conditionId = createCondition(inputGroup, outputGroup, 1);
        //  System.out.println(conditionId);

        //TODO add column to differentiate input from output

        JSONArray jsonFinal = getConditionsAndInputs(temp);
        for (int i = 0; i < jsonFinal.length(); i++) {
            System.out.println(jsonFinal.get(i).toString());
        }
        System.out.println("\n\n" + jsonFinal.toString());


        //TEST methods for error manager
     /*   System.out.println("\n Division by zero exception handling in database");
        try {
            int test = 0;
            double value = test / 0;
        } catch (Exception ex) {
            errorManager em = new errorManager();
            em.logError(ex);
        }

        //TEST select 50 logs
        System.out.println("\n\n select last 50 log");
        dbAccess db = new dbAccess();
        JSONArray json = db.selectQuery("SELECT * FROM log LIMIT 50");

        JSONObject objType = new JSONObject();
        objType.put("datatype", "Double");
        json.put(objType);
        for (int i = 0; i < json.length(); i++) {
            System.out.println(json.get(i).toString());
        }
        System.out.print("Full json string: ");
        System.out.println(json.toString());


        //TEST update a log
        System.out.println("\n\n Update a log");
        int logId = 1;
        Date date = new Date();
        try {
            PreparedStatement ps = db.getConnection().prepareStatement("UPDATE log SET valerrorcode = ?, valcip = ? , datdate = ?, details = ? WHERE serserial = ?;");
            ps.setString(1, "ERROR_CODE");
            ps.setString(2, "BOLM2210");
            ps.setTimestamp(3, new Timestamp(date.getTime()));
            ps.setString(4, "NEW DETAILS");
            ps.setInt(5, 5);

            db.updateQuery(ps);
        } catch (SQLException ex) {
            errorManager em = new errorManager();
            em.logError(ex);
        }*/


        //TEST Delete
   /*     System.out.println("\n\n Delete log with given ID");
        try {
            PreparedStatement ps = db.getConnection().
                    prepareStatement("DELETE FROM log WHERE serserial = ?");
            ps.setInt(1, 4);
            db.deleteQuery(ps.toString());
        } catch (SQLException ex) {
            errorManager em = new errorManager();
            em.logError(ex);
        }*/


        //Close dB connection
        //db.closeConnection();
    }
}
