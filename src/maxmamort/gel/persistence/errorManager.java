package maxmamort.gel.persistence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class errorManager implements IerrorManager {
    public void logError(Exception ex) {
        String _str = ex.getStackTrace().toString();
        _str = ex.fillInStackTrace().toString();
        String _errorCode = ex.getMessage();
        sendError(_errorCode, _str, "NULL0000");
    }

    public void logError(Exception ex, String _userID) {
        String _str = ex.getStackTrace().toString();
        String _errorCode = ex.toString();
        sendError(_errorCode, _str, _userID);
    }

    public void logError(String error, String _userID) {
        sendError("Custom", error, _userID);
    }

    private void sendError(String errorCode, String message, String user) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        dbAccess dba = new dbAccess(false);
        String data = "'" + errorCode + "','" + dateFormat.format(date) + "','" + message + "','" + user + "'";
        if (!dba.insertQuery("INSERT INTO log (serSerial, valErrorCode, datDate, details, ValCIP) VALUES( DEFAULT," + data + " )")) {
            System.out.println("Failed db query");
            String fileName = "E://ErrorLog.txt";
            try {
                Files.write(Paths.get(fileName), data.getBytes());
            } catch (Exception e) {
                System.out.println("Failed file writting");
            }
        }
        dba.closeConnection();
    }
}
