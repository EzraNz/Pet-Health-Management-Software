package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    protected String dbName;
    protected String sJDBC;
    protected String sDriverName;
    public Connection conn;
    protected String sDBURL;
    protected int timeout;

    public ResultSet queryDB(String sql) throws SQLException {
        Statement statement = conn.createStatement();
        statement.setQueryTimeout(timeout);
        return statement.executeQuery(sql);
    }

    public boolean updateDB(String sql) throws SQLException {
        Statement statement = conn.createStatement();
        return statement.execute(sql);
    }
}