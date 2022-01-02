package model;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PsqlDB extends DB{
    public PsqlDB() throws ClassNotFoundException, SQLException {
        sDriverName = "org.postgresql.Driver";
        Class.forName(sDriverName);
        sJDBC = "jdbc:postgresql";
        sDBURL = sJDBC + ":" + dbName;
        conn = DriverManager.getConnection(sDBURL);
    }

    public PsqlDB(String dbName) throws ClassNotFoundException, SQLException {
        this.dbName = dbName;
        sDriverName = "org.postgresql.Driver";
        Class.forName(sDriverName);
        sJDBC = "jdbc:postgresql";
        sDBURL = sJDBC + "://" + dbName;
        conn = DriverManager.getConnection(sDBURL, "postgres", "password");
    }

    public void close() throws SQLException {
        conn.close();
    }
}