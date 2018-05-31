
package maxmamort.gel.persistence;

import java.sql.*;
import java.lang.*;
import java.sql.ResultSet;

import org.json.JSONArray;

//https://www.tutorialspoint.com/postgresql/postgresql_java.htm

public class dbAccess implements IdbAccess {

    private boolean boo_logError;
    private boolean _boolIsError = false;//Bonne variable
    private Connection conn = null;

    /**
     * @brief default constructor, logging disabled
     */
    public dbAccess() {
        boo_logError = true;
        initConnection();
    }

    /**
     * @param _boo_logError true if errors should be logged, false otherwise
     * @brief constructor that specifies if error should be logged
     */
    public dbAccess(boolean _boo_logError) {
        boo_logError = _boo_logError;
        initConnection();
    }

    /**
     * @return true if error, false otherwise
     * @brief Verify if an error occured in last query
     */
    public boolean isError() {
        return _boolIsError;
    }

    /**
     * @return the dB driver connection
     * @brief Get the active db driver connection
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * @brief terminate de dB driver connexion
     */
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

    /**
     * @brief initiate de db driver connexion
     */
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

    /**
     * @param str SQL query to be executed
     * @return true if suceeded, false if failed
     * @brief execute an insert query in db
     */
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

    /**
     * @param str         the SQL query to be executed
     * @param IdFieldName Name of the field to return once executed
     * @return the value of the field to return
     * @brief Execute an SQL insert query and return the id inserted
     */
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

    /**
     * @param str SQL query to be exeuted
     * @return A JSON formatted output of the query
     * @brief execute a select query on the database
     */
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

    /**
     * @param ps The preparedStatement of the SQL query
     * @return true if suceeded, false if failed
     * @brief execute an update query
     */
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

    /**
     * @param sql The SQL query to be executed
     * @return true if suceeded, false if failed
     * @brief execute an update query on the database
     */
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

    /**
     * @param str The SQL delete statement to be executed
     * @return true if succeeded, false if failed
     * @brief execute an SQL delete on de db
     */
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
