package maxmamort.gel.persistence;

import org.json.JSONArray;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface IdbAccess {

    //String dbLocation = "jdbc:postgresql://209.226.99.185:5433/projectS3"; //REMOTE IP
    String dbLocation = "jdbc:postgresql://maxmamort3.ddns.net:5433/projectS3-2"; //REMOTE IP
    //String dbLocation = "jdbc:postgresql://192.168.1.12:5433/projectS3"; //LOCAL IP
    String user = "maxmamort";
    String password = "bobol2010";

    void closeConnection();

    Connection getConnection();

    int updateGetIdQuery(String sql);

    boolean isError();

    boolean updateQuery(String sql);

    int insertGetIdQuery(String str, String IdFieldName);

    boolean insertQuery(String str);

    boolean updateQuery(PreparedStatement ps);

    boolean deleteQuery(String str);

    JSONArray selectQuery(String str);
}

