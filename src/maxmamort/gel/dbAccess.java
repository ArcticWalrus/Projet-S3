package maxmamort.gel;

import java.sql.*;
import java.lang.*;
import java.sql.ResultSet;

import org.json.JSONArray;

//https://www.tutorialspoint.com/postgresql/postgresql_java.htm

public class dbAccess implements IdbAccess {

    private boolean boo_logError;
    private boolean _boolIsError = false;//Bonne variable
    private Connection conn = null;

    public dbAccess() {
        boo_logError = true;
        initConnection();
    }

    public dbAccess(boolean _boo_logError) {
        boo_logError = _boo_logError;
        initConnection();
    }

    public boolean isError() {
        return _boolIsError;
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException e) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(e);
            }
            _boolIsError = true;
        }
    }

    private void initConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(dbLocation, user, password);
            conn.setAutoCommit(false);
        } catch (ClassNotFoundException ex) {
            //System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
            ex.printStackTrace();
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(ex);
            }
            _boolIsError = true;
        } catch (SQLException ex) {
            //System.out.println("Could not access dB. Check path, user and password");
            ex.printStackTrace();
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(ex);
            }
            _boolIsError = true;
        }
    }

    public boolean insertQuery(String str) {
        _boolIsError = false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            //System.out.println(str);
            stmt.executeUpdate(str);
            stmt.close();
            conn.commit();
        } catch (SQLException e) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(e);
            }
            _boolIsError = true;
            return false;
        }
        return true;
    }

    public int insertGetIdQuery(String str, String IdFieldName) {
        _boolIsError = false;
        Statement stmt = null;
        ResultSet rs = null;
        int returnValue = -1;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(str);
            conn.commit();
            rs.next();
            returnValue = rs.getInt(1);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(e);
            }
            _boolIsError = true;
            return returnValue;
        }
        return returnValue;
    }

    public JSONArray selectQuery(String str) {
        _boolIsError = false;
        Statement stmt = null;
        JSONArray returnValue = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(str);
            returnValue = Convertor.convertToJSON(rs);
            rs.close();
            stmt.close();
        } catch (Exception e) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(e);
            }
            _boolIsError = true;
            return null;
        }
        return returnValue;
    }

    public boolean updateQuery(PreparedStatement ps) {
        _boolIsError = false;
        try {
            // call executeUpdate to execute our sql update statement
            ps.executeUpdate();
            ps.close();
            conn.commit();
        } catch (SQLException ex) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(ex);
            }
            _boolIsError = true;
            return false;
        } catch (Exception ex) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(ex);
            }
            _boolIsError = true;
            return false;
        }
        return true;
    }

    public boolean updateQuery(String sql) {
        _boolIsError = false;
        try {
            // call executeUpdate to execute our sql update statement
            conn.createStatement().executeUpdate(sql);
            conn.commit();
        } catch (SQLException ex) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(ex);
            }
            _boolIsError = true;
            return false;
        } catch (Exception ex) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(ex);
            }
            _boolIsError = true;
            return false;
        }
        return true;
    }

    public boolean deleteQuery(String str) {
        _boolIsError = false;
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            //System.out.println(str);
            stmt.executeUpdate(str);
            stmt.close();
            conn.commit();
        } catch (SQLException e) {
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(e);
            }
            _boolIsError = true;
            return false;
        }
        return true;
    }
}
