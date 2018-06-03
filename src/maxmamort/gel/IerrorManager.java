package maxmamort.gel.persistence;

public interface IerrorManager {
    String logPath = "E://ErrorLog.txt";

    void logError(Exception ex);

    void logError(Exception ex, String _userID);

    void logError(String error, String _userID);
}
