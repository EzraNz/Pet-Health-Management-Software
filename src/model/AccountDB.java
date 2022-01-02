package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDB {
    public static void buildDatabase() throws Exception {
        boolean control = false;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/");
        String sql = "SELECT datname FROM pg_database;";
        ArrayList<String> databases = new ArrayList<>();

        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            String databaseName = rs.getString("datname");
            databases.add(databaseName);
        }

        for (String name : databases) {
            if (name.equalsIgnoreCase("Records")) {
                control = true;
                break;
            }
        }

        if (!control) {
            sql = "CREATE DATABASE Records";
            psqlDB.updateDB(sql);
            psqlDB.close();
            buildAccountTable();
        } else {
            buildAccountTable();
        }
    }

    private static void buildAccountTable() throws SQLException, ClassNotFoundException {
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = """
                CREATE TABLE IF NOT EXISTS Accounts (email TEXT NOT NULL PRIMARY KEY,
                firstName TEXT NOT NULL, lastName TEXT NOT NULL, password TEXT NOT NULL,
                phoneNumber BIGINT NOT NULL)
                """;
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    public void addAcct(Account account) throws Exception {
        buildDatabase();

        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        checkUnique(account.getEmail());
        String sql = "INSERT INTO Accounts(email, password, firstName, lastName, phoneNumber) Values('" + account.getEmail() + "', '" +
                account.getPassword() + "', '" + account.getFirstName() + "', '" + account.getLastName() + "', " + account.getPhoneNumber() + ")";
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    private void checkUnique(String email) throws Exception {
        String name;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "Select email from Accounts";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            name = rs.getString(1);
            if (email.equalsIgnoreCase(name)) {
                throw new Exception(("There is already a user with that email."));
            }
        }
        psqlDB.close();
    }

    public void updateAcct(Account account, String originalEmail) throws Exception {
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "UPDATE accounts SET email = '" + account.getEmail() + "', firstName = '" + account.getFirstName() + "', lastName = '" +
                account.getLastName() + "', password = '" + account.getPassword() + "', phoneNumber = " + account.getPhoneNumber() +
                " WHERE email = '" + originalEmail + "'";
        psqlDB.updateDB(sql);
        psqlDB.close();
    }
}