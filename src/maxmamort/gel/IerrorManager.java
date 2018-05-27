package maxmamort.gel;

import java.lang.*;

public interface IerrorManager {
    void logError(Exception ex);

    void logError(Exception ex, String _userID);

    void logError(String error, String _userID);
}
