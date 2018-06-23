/**
 * @File: dbAccess.java
 * @Author: Maxim Bolduc
 * @Date: 2018-05-31
 * @Brief: Gestion des différents types de query pour la database et du JDBC
 * @Reference: https://www.tutorialspoint.com/postgresql/postgresql_java.htm
 */

package maxmamort.gel.persistence;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.sql.ResultSet;
import java.util.Properties;

public class dbAccess implements IdbAccess {

    private boolean boo_logError;
    private boolean _boolIsError = false;//Bonne variable
    private Connection conn = null;
    private String errorMessage = "";


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

    private void setSQLError(SQLException e, String sql) {
        if (boo_logError) {
            errorManager ema = new errorManager();
            ema.logError(sql, e);
        }
        errorMessage = e.fillInStackTrace() + "      " + e.getErrorCode() + "     " + e.getMessage() + "               " + e.getSQLState() + "\n\t\t" + sql;
        _boolIsError = true;
    }

    private void setError(Exception ex) {
        errorMessage = ex.fillInStackTrace() + "      " + ex.getMessage();
    }

    public String getErrorMessage() {
        return errorMessage;
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
            setSQLError(e, "");
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
            ex.printStackTrace();
            if (boo_logError) {
                errorManager ema = new errorManager();
                ema.logError(ex);
            }
            _boolIsError = true;
        } catch (SQLException ex) {
            setSQLError(ex, "");
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
            setSQLError(e, str);
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
            setSQLError(e, str);
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
            return returnValue;
        } catch (SQLException e) {
            setSQLError(e, str);
        } catch (Exception e) {
            setError(e);
        }
        return null;
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
            return true;
        } catch (SQLException ex) {
            setSQLError(ex, ps.toString());
        } catch (Exception ex) {
            setError(ex);
        }
        return false;
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
            return true;
        } catch (SQLException ex) {
            setSQLError(ex, sql);
        } catch (Exception ex) {
            setError(ex);
        }
        return false;
    }

    public int updateGetIdQuery(String sql) {
        _boolIsError = false;
        Statement stmt = null;
        ResultSet rs = null;
        int returnValue = -1;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            conn.commit();
            rs.next();
            returnValue = rs.getInt(1);
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            setSQLError(e, sql);
        }
        return returnValue;
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
            return true;
        } catch (SQLException e) {
            setSQLError(e, str);
        }
        return false;
    }
}
