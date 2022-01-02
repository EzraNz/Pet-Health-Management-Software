package model;

public class Appointment {
    private String appointmentType;
    private String appointmentPet;
    private String appointmentDate;
    private String appointmentName;
    private String userEmail;

    public Appointment() {
    }

    public Appointment(String appointmentType, String appointmentPet, String appointmentDate, String userEmail, String appointmentName) {
        this.appointmentType = appointmentType;
        this.appointmentPet = appointmentPet;
        this.appointmentDate = appointmentDate;
        this.appointmentName = appointmentName;
        this.userEmail = userEmail;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getAppointmentPet() {
        return appointmentPet;
    }

    public void setAppointmentPet(String appointmentPet) {
        this.appointmentPet = appointmentPet;
    }

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAppointmentName() {
        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }
}
