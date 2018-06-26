/**
 * @File: persistantLayer.java
 * @Author: Maxim Bolduc
 * @Date: 2018-05-31
 * @Brief: Fonctions customs de la couche de persistance effectuant la logique de persistance de l'application
 */

//TODO comment out this class

package maxmamort.gel.persistence;

//import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import maxmamort.gel.Utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class persistantLayer {

    /**
     * @param inputName    Name of the input
     * @param defaultValue Value to add as the default one
     * @param sensorType   1 for output, 0 for input. More types to come
     * @return The Id of the newly added input
     * @brief Add input to database
     */
    public int addInput(String inputName, double defaultValue, int sensorType) {
        String sql = "INSERT INTO public.intinput ( serintinput, namname, valvalue, valtype )  VALUES(DEFAULT, '" + inputName + "' , '" + defaultValue + "','" + sensorType + "') RETURNING serintinput;";
        return insertGetIdUtil(sql, "serintinput");
    }

    public int getInputIDForIO(int IOID) {
        //TODO Test method
        String sql = "SELECT valinputid FROM public.io WHERE serio = " + IOID + ";";
        return selectUtil(sql).getJSONObject(0).getInt("valinputid");
    }

    public JSONArray getIOByDevice(String deviceID) {
        //TODO Test method
        String sql = "SELECT * FROM public.io WHERE namio = '" + deviceID + "' ;";
        return selectUtil(sql);
    }

    //User must exist!
    public void createDevice(String MAC, String cip, String name) {
        String sql = "INSERT INTO public.devices (serdevices, namdevice, valcip, valip, valmac) VALUES ( DEFAULT, '" + name + "', '" + cip + "' , '0.0.0.0', '" + MAC + "' );";
        insertUtil(sql);
    }

    public void createUser(String cip) {
        String sql = "INSERT INTO public.users ( serusers, valcip ) VALUES ( DEFAULT , '" + cip + "') ON CONFLICT (valcip) DO NOTHING;";
        insertUtil(sql);
    }


    public JSONArray getDevicesByUser(String cip) {
        String sql = "SELECT * FROM public.devices WHERE valcip = '" + cip + "';";
        return selectUtil(sql);
    }

    public JSONArray getIOForUser(String cip) {
        //TODO complete and test method
        JSONArray json = new JSONArray();
        json = getDevicesByUser(cip);
        String sql = "SELECT * FROM public.io WHERE ";
        for (int i = 0; i < json.length(); i++) {
            if (i == json.length() - 1) {
                sql += " OR ";
            }
        }
        return selectUtil(sql);
    }

    public void renameDevice(String MAC, String name) {
        String sql = "UPDATE public.devices SET namdevice = '" + name + "' WHERE valmac = '" + MAC + "' ;";
        updateUtil(sql);
    }

    public void renameIO(int IOID, String name) {
        String sql = "UPDATE public.io SET namio = " + name + " WHERE serio = " + IOID + ";";
        updateUtil(sql);
    }

    public void updateConfigurationBit(int IOID, int ConfigurationBit) {
        //TODO test method
        int sensorType = getSensorTypeFromConfigurationBit(ConfigurationBit);
        String sql = "UPDATE public.io SET configurationbits = " + ConfigurationBit + " WHERE serio = " + IOID + " RETURNING valinputid;";

        //TODO make updateUtilGetId
        dbAccess db = new dbAccess();
        int id = db.updateGetIdQuery(sql);
        db.updateQuery(sql);
        db.closeConnection();

        sql = "UPDATE public.intinput SET valtype = " + sensorType + " WHERE serio = " + id + ";";
        updateUtil(sql);
    }

    public void updatePhysicalMapping(int IOID, int physicalPin) {
        //TODO test method
        updateUtil("UPDATE public.io SET pinid = " + physicalPin + " WHERE serio = " + IOID + ";");
    }

    public void updateDeviceIP(String mac, String ip) {
        updateUtil("UPDATE public.devices SET valip='" + ip + "' WHERE valmac = '" + mac + "';");
    }

    public int createIO(String IoName, String DeviceId, int physicalPinMapping, int configurationBit) {
        //TODO ADD namio
        //TODO test method
        int sensorType = getSensorTypeFromConfigurationBit(configurationBit);
        int inputId = addInput(IoName, 0, sensorType);
        String sql = "INSERT INTO public.io (serio, namio, configurationbits, pinid, valinputid) " +
                "VALUES (DEFAULT, '" + DeviceId + "', " + configurationBit + ", " + physicalPinMapping + ", " + inputId + ") RETURNING serio;";
        System.out.println(sql);
        return insertGetIdUtil(sql, "serio");
    }

    //Method is there in case some more fancy configuration are added
    public int getSensorTypeFromConfigurationBit(int ConfigurationBit) {
        if (ConfigurationBit == 0) {
            return 0;
        } else if (ConfigurationBit == 1) {
            return 1;
        }
        return 0;
    }

    /**
     * @param outputId The Id of the output that has to be updated
     * @param value    The new value of that output
     * @return True if everything worked, false if failed
     * @brief Update the value of an output
     */
    public void updateOutputValue(int outputId, double value) {
        String query = "UPDATE public.intinput SET valvalue = '" + value + "' WHERE serintinput = '" + outputId + "'";
        updateUtil(query);
    }

    /**
     * @param inputIds array of inputId
     * @return The iD of the inputGroup created
     * @brief Create an input group from a list of inputId
     */
    public int createInputGroupCondition(int[] inputIds, int operation) {
        int returnValue = -1;
        String sql = "INSERT INTO public.inputgroup(serinputgroup, namconditiongroup , valinputid, valordre, valoperation) VALUES (DEFAULT, DEFAULT, " + inputIds[0] + ", 0," + operation + ") RETURNING namconditiongroup;";
        returnValue = insertGetIdUtil(sql, "namconditiongroup");
        for (int i = 1; i < inputIds.length; i++) {
            insertUtil("INSERT INTO public.inputgroup(serinputgroup, namconditiongroup , valinputid, valordre, valoperation) VALUES (DEFAULT, " + returnValue + ", " + inputIds[i] + ", " + i + " ," + operation + ");");
        }
        return returnValue;
    }

    /**
     * @param jsonUpdate Json containing the id of the outputgroupid and the newvalue
     * @return
     */
    public void updateValueOutputGroup(JSONArray jsonUpdate) {
        int conditionGroupId = jsonUpdate.getJSONObject(0).getInt("outputgroupid");
        double newValue = jsonUpdate.getJSONObject(0).getDouble("newvalue");
        String sql = "UPDATE public.intinput SET valvalue = '" + newValue + "' WHERE serintinput IN \n" +
                "\t(SELECT serintinput FROM public.getidofinputinoutputgroup WHERE namconditiongroup = " + conditionGroupId + ");";
        updateUtil(sql);
    }

    /**
     * @param inputId The Id of the updated input. Will be used to get the right conditions
     * @return The JSONArray representing the conditions and what is needed to handle them
     * @brief Get the conditions and what is needed to manage them from an InputID
     */
    public JSONArray getConditionsAndInputs(int inputId) {
        JSONArray json = null;//result JSON
        String sql = "SELECT namconditiongroup FROM public.inputgroup WHERE valinputid = " + inputId;
        json = selectUtil(sql);
        sql = "SELECT * FROM public.inputoutputconditions WHERE ";
        for (int i = 0; i < json.length(); i++) {
            sql += " namconditiongroup = " + Integer.toString(json.getJSONObject(i).getInt("namconditiongroup"));
            if (i != json.length() - 1) {
                sql += " OR ";
            }
        }
        sql += ";";
        json = selectUtil(sql);
        return json;
    }

    private void updateUtil(String sql) {
        dbAccess db = new dbAccess();
        db.updateQuery(sql);
        db.closeConnection();
    }

    private int insertGetIdUtil(String sql, String field) {
        dbAccess db = new dbAccess();
        int value = db.insertGetIdQuery(sql, field);
        db.closeConnection();
        return value;
    }

    private void insertUtil(String sql) {
        dbAccess db = new dbAccess();
        db.insertQuery(sql);
        db.closeConnection();
    }

    private JSONArray selectUtil(String sql) {
        JSONArray json = new JSONArray();
        dbAccess db = new dbAccess();
        json = db.selectQuery(sql);
        db.closeConnection();
        return json;
    }
}
