/**
 * @File:       errorManager.java
 * @Author:     Maxim Bolduc
 * @Date:       2018-05-31
 * @Brief:      Gère la gestion et l'archivage des erreurs du programme
 */

package maxmamort.gel.persistence;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public void logError(String sql, Exception ex) {
        String _str = ex.getStackTrace().toString();
        _str = ex.fillInStackTrace().toString();
        String _errorCode = ex.getMessage();
        sendError(_errorCode, sql + "   " +_str, "bolm2210");
    }
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
        if (!dba.insertQuery("INSERT INTO public.log (serSerial, valErrorCode, datDate, details, ValCIP) VALUES( DEFAULT," + data + " )")) {
            System.out.println("Failed db query");
            try {
                Files.write(Paths.get(logPath),( "\r\n\r\n" + data).getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {
                System.out.println("Failed file writting");
            }
        }
        dba.closeConnection();
    }
}
