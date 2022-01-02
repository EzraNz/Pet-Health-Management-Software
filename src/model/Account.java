package model;

public class Account {
    private static String email;
    private static String password;
    public static String firstName;
    private static String lastName;
    private static long phoneNumber;

    public Account() {
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long number) {
        Account.phoneNumber = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Account.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Account.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        Account.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        Account.lastName = lastName;
    }
}
