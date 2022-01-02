package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDB {
    private static void buildAppointmentTable() throws SQLException, ClassNotFoundException {
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = """
                CREATE TABLE IF NOT EXISTS Appointments (appointmentNumber INTEGER NOT NULL PRIMARY KEY,
                appointmentType TEXT NOT NULL, appointmentPet TEXT NOT NULL, appointmentDate TEXT NOT NULL,
                appointmentName TEXT NOT NULL, userEmail TEXT NOT NULL)
                """;
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    public void addAppointment(Appointment appointment) throws Exception {
        buildAppointmentTable();

        int appointmentNumber = 0;
        var numberOfEntries = 0;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "SELECT COUNT(*) from appointments";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            numberOfEntries = rs.getInt(1);
            appointmentNumber = numberOfEntries == 0 ? 1 : numberOfEntries + 1;
        }

        psqlDB = new PsqlDB("localhost:5432/records");
        sql = "INSERT INTO Appointments (appointmentNumber, appointmentType, appointmentPet," +
                "appointmentDate, appointmentName, userEmail) Values(" + appointmentNumber +
                ", '" + appointment.getAppointmentType() + "', '" + appointment.getAppointmentPet() + "', '" +
                appointment.getAppointmentDate() + "', '" + appointment.getAppointmentName() + "', '" +
                appointment.getUserEmail() + "')";
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    public ArrayList<Appointment> appointmentHistory(String ownerEmail) throws Exception {
        ArrayList<Appointment> userAppointments = new ArrayList<>();
        if (verifyAppointment(ownerEmail)) {
            PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
            String sql = "SELECT * from appointments WHERE userEmail = '" + ownerEmail + "'";
            ResultSet rs = psqlDB.queryDB(sql);
            while (rs.next()) {
                String appointmentType = rs.getString("appointmentType");
                String appointmentPet = rs.getString("appointmentPet");
                String appointmentDate = rs.getString("appointmentDate");
                String appointmentName = rs.getString("appointmentName");
                String userEmail = rs.getString("userEmail");

                Appointment appointment = new Appointment(appointmentType, appointmentPet, appointmentDate, appointmentName, userEmail);
                userAppointments.add(appointment);
            }
            psqlDB.close();
        } else {
            throw new Exception("You have no appointments.");
        }
        return userAppointments;
    }

    public boolean verifyAppointment(String ownerEmail) throws Exception {
        int count;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "SELECT COUNT(*) from appointments WHERE userEmail = '" + ownerEmail + "'";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            count = rs.getInt(1);
            if (count > 0) {
                return true;
            }
        }
        psqlDB.close();
        return false;
    }

    public void removeAppointment(ArrayList<Appointment> appointments) throws Exception {
        String appointmentDate, petName, appointmentType;
        for (Appointment appointment: appointments) {
            appointmentDate = appointment.getAppointmentDate();
            petName = appointment.getAppointmentPet();
            appointmentType = appointment.getAppointmentType();

            PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
            String sql = "DELETE from appointments WHERE appointmentDate = '" + appointmentDate +
                    "' AND (appointmentPet = '" + petName + "' AND appointmentType = '" + appointmentType + "')";
            psqlDB.updateDB(sql);
            psqlDB.close();
        }

    }
}
