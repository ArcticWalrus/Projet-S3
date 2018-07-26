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

//TODO refractor persistant in more specific models
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

    public void deleteDevice(String mac) {
        String sql = "DELETE FROM public.devices WHERE valmac='" + mac + "';";
        deleteUtil(sql);
    }

    public void deleteUser(String cip) {
        String sql = "DELETE FROM public.users WHERE valcip='" + cip + "';";
        deleteUtil(sql);
    }

    public void deleteIO(String mac, int pinId) {
        String sql = "DELETE FROM public.io WHERE namiogroup = '" + mac + "' AND pinid=" + pinId + ";";
        deleteUtil(sql);
    }

    public JSONArray getConditions() {
        String sql = "SELECT * FROM public.inputgroup JOIN public.io ON inputgroup.valinputid = io.valinputid;";
        return selectUtil(sql);
    }

    //Still have SQL for delete condition
    public void deleteCondition(int cconditionid){
        String sql = "DELETE FROM public.inputgroup WHERE namconditiongroup = 'conditionid';";
    }


    public JSONArray getIo() {
        String sql = "SELECT valcip AS CIP, namdevice AS DeviceName, valmac AS MAC, namio AS IoName, pinid, configurationbits AS Config, valvalue AS value FROM public.io JOIN public.devices ON devices.valmac = io.namiogroup JOIN public.intinput ON intinput.serintinput = io.valinputid;";
        return selectUtil(sql);
    }

    public JSONArray getDevices() {
        String sql = "SELECT valcip, namdevice, valip, valmac FROM public.devices;";
        return selectUtil(sql);
    }

    public JSONArray getUsers() {
        String sql = "SELECT valcip AS cip FROM public.users;";
        return selectUtil(sql);
    }

    public int getInputIDForIO(int IOID) {
        //TODO Test method
        String sql = "SELECT valinputid FROM public.io WHERE serio = " + IOID + ";";
        return selectUtil(sql).getJSONObject(0).getInt("valinputid");
    }

    public JSONArray getIOByDevice(String mac) {
        String sql = "SELECT pinid, valvalue, valtype FROM public.io AS curr_io INNER JOIN public.intinput AS curr_input ON curr_input.serintinput = curr_io.valinputid WHERE namiogroup = '" + mac + "';";
        return selectUtil(sql);
    }

    public void createCondition(String MAC, int pin1, int pin2, int pin3, int operation) {
        int inputId1 = getIntInputFromIO(MAC, pin1);
        int inputId2 = getIntInputFromIO(MAC, pin2);
        int inputId3 = getIntInputFromIO(MAC, pin3);
        createInputGroupCondition(new int[]{inputId1, inputId2, inputId3}, operation);
    }

    //User must exist!
    public void createDevice(String MAC, String cip, String name) {
        String sql = "INSERT INTO public.devices (serdevices, namdevice, valcip, valip, valmac) VALUES ( DEFAULT, '" + name + "', '" + cip + "' , '0.0.0.0', '" + MAC + "' );";
        //System.out.println(sql);
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
        return selectUtil("SELECT * FROM public.userio WHERE valcip = '" + cip + "';");
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
        int sensorType = getSensorTypeFromConfigurationBit(ConfigurationBit);
        String sql = "UPDATE public.io SET configurationbits = " + ConfigurationBit + " WHERE serio = " + IOID + " RETURNING valinputid;";
        int id = updateUtilGetId(sql);

        sql = "UPDATE public.intinput SET valtype = " + sensorType + " WHERE serio = " + id + ";";
        updateUtil(sql);
    }

    public void updatePhysicalMapping(int IOID, int physicalPin) {
        updateUtil("UPDATE public.io SET pinid = " + physicalPin + " WHERE serio = " + IOID + ";");
    }

    public void updateDeviceIP(String mac, String ip) {
        updateUtil("UPDATE public.devices SET valip='" + ip + "' WHERE valmac = '" + mac + "';");
    }

    public int getIntInputFromIO(String mac, int physicalPin) {
        return selectUtil("SELECT valinputid FROM public.io WHERE namiogroup = '" + mac + "' AND pinid = " + physicalPin + ";").getJSONObject(0).getInt("valinputid");
    }

    public int createIO(String IoName, String DeviceId, int physicalPinMapping, int configurationBit) {
        int sensorType = getSensorTypeFromConfigurationBit(configurationBit);
        int inputId = addInput(IoName, 0, sensorType);
        String sql = "INSERT INTO public.io (serio, namio, configurationbits, pinid, valinputid, namiogroup) " +
                "VALUES (DEFAULT, '" + IoName + "', " + configurationBit + ", " + physicalPinMapping + ", " + inputId + ",'" + DeviceId + "') RETURNING serio;";
        int id = insertGetIdUtil(sql, "serio");
        if (id == -1) {//revert back changes
            sql = "DELETE FROM public.intinput WHERE serintinput = " + inputId + ";";
            deleteUtil(sql);
        }
        return id;
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

    public void updateIoValue(String mac, int physicalPin, double newValue) {
        String sql = "INSERT INTO updateintinputmacpin VALUES ('" + mac + "' ," + physicalPin + ", '" + newValue + "');";
        updateUtil(sql);
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
        //System.out.println(sql);
        updateUtil(sql);
    }

    public JSONArray getConditionsAndInputsFromIO(String mac, int physicalpin) {
        int inputId = getIntInputFromIO(mac, physicalpin);
        return getConditionsAndInputs(inputId);
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

    public void clearDB() {
        String sql = "TRUNCATE public.log; TRUNCATE public.io CASCADE; TRUNCATE public.devices CASCADE; TRUNCATE public.inputgroup CASCADE; TRUNCATE public.intinput CASCADE; TRUNCATE public.users CASCADE;";
        insertUtil(sql);
    }

    public int updateUtilGetId(String sql) {
        dbAccess db = new dbAccess();
        int id = db.updateGetIdQuery(sql);
        db.updateQuery(sql);
        db.closeConnection();
        return id;
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

    private void deleteUtil(String sql) {
        dbAccess db = new dbAccess();
        db.deleteQuery(sql);
        db.closeConnection();
    }
}
