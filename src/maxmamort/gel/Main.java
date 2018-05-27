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



    public static void main(String[] args) {
        int temp = persistantLayer.addInput("Test input", 22.5, 0);
        int temp2 = persistantLayer.addInput("Input2", 21.2, 0);

        int inputGroup = persistantLayer.createInputGroup(new int[]{temp, temp2});

        int temp3 = persistantLayer.addInput("output1", 1.3, 1);
        int outputGroup = persistantLayer.createInputGroup(new int[]{temp3});

        int conditionId = persistantLayer.createCondition(inputGroup, outputGroup, 1);
        //  System.out.println(conditionId);

        //TODO add column to differentiate input from output

        JSONArray jsonFinal = persistantLayer.getConditionsAndInputs(temp);
        LMThread _lmtLogic = new LMThread(jsonFinal);
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
