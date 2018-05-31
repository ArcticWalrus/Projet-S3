package maxmamort.gel.persistence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class errorManager implements IerrorManager {
    /**
     * @param ex The exception to log
     * @brief Log and exception
     */
    public void logError(Exception ex) {
        String _str = ex.getStackTrace().toString();
        _str = ex.fillInStackTrace().toString();
        String _errorCode = ex.getMessage();
        sendError(_errorCode, _str, "NULL0000");
    }

    /**
     * @param ex      The exception to be logged
     * @param _userID The user (CIP) that fired the exception
     * @brief log an exception
     */
    public void logError(Exception ex, String _userID) {
        String _str = ex.getStackTrace().toString();
        String _errorCode = ex.toString();
        sendError(_errorCode, _str, _userID);
    }

    /**
     * @param error   The error string to log
     * @param _userID The user that fired this error
     * @brief log an error
     */
    public void logError(String error, String _userID) {
        sendError("Custom", error, _userID);
    }

    /**
     * @param errorCode The code of the error
     * @param message   The string representing the exception or the error
     * @param user      The user that fired the exception
     * @brief Send the error or exception to the database or file system
     */
    private void sendError(String errorCode, String message, String user) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        dbAccess dba = new dbAccess(false);
        String data = "'" + errorCode + "','" + dateFormat.format(date) + "','" + message + "','" + user + "'";
        if (!dba.insertQuery("INSERT INTO log (serSerial, valErrorCode, datDate, details, ValCIP) VALUES( DEFAULT," + data + " )")) {
            System.out.println("Failed db query");
            try {
                Files.write(Paths.get(logPath), data.getBytes());
            } catch (Exception e) {
                System.out.println("Failed file writting");
            }
        }
        dba.closeConnection();
    }
}
