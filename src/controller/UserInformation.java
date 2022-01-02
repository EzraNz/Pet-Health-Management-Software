package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Account;
import model.AccountDB;
import model.PsqlDB;
import view.AppUI;

import java.sql.ResultSet;

public class UserInformation {
    private static boolean control = false;

    private static void login(String email, String password) throws Exception {
        if (verifyEmail(email)) {
            if (verifyPassword(password)) {
                control = true;
            } else {
                throw new Exception("Incorrect password");
            }
        } else {
            throw new Exception("No user with that email");
        }
    }

    private static boolean verifyEmail(String email) throws Exception {
        String tempEmail;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "Select email from Accounts";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            tempEmail = rs.getString(1);
            if (email.equalsIgnoreCase(tempEmail)) {
                return true;
            }
        }
        psqlDB.close();
        return false;
    }

    private static boolean verifyPassword(String password) throws Exception {
        String tempPassword;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "Select password from Accounts";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            tempPassword = rs.getString(1);
            if (password.equalsIgnoreCase(tempPassword)) {
                return true;
            }
        }
        psqlDB.close();
        return false;
    }

    private static void createAccount(String email, String pass, String firstName, String lastName, long phoneNumber) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(pass);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setPhoneNumber(phoneNumber);
        try {
            new AccountDB().addAcct(account);
            control = true;
        } catch (Exception ignored) {
        }
    }

    private static void updateAccount(String email, String pass, String firstName, String lastName, long phoneNumber, String originalEmail) {
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(pass);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setPhoneNumber(phoneNumber);
        try {
            new AccountDB().updateAcct(account, originalEmail);
            control = true;
        } catch (Exception ignored) {
        }
    }

    public void loginWindow(Stage window) {
        window.setTitle("Log In");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Email text field
        final TextField email = new TextField();
        email.setPromptText("Enter your email.");
        email.setPrefColumnCount(10);
        email.getText();
        GridPane.setConstraints(email, 0, 0);
        grid.getChildren().add(email);

        //Defining the Password text field
        final PasswordField password = new PasswordField();
        password.setPromptText("Enter your password.");
        password.getText();
        GridPane.setConstraints(password, 0, 1);
        grid.getChildren().add(password);

        //Defining the Submit button
        Button logIn = new Button("Login");
        GridPane.setConstraints(logIn, 1, 0);
        grid.getChildren().add(logIn);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);

        //Defining the back button
        Button back = new Button("Go Back");
        GridPane.setConstraints(back, 1, 2);
        grid.getChildren().add(back);

        //Defining the Forgot Password button
        Button forgotPass = new Button("Forgot Password");
        GridPane.setConstraints(forgotPass, 0, 2);
        grid.getChildren().add(forgotPass);

        //Adding a Label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 3);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        //Setting an action for the Submit button
        logIn.setOnAction(e -> {
            if ((email.getText() != null && !email.getText().isEmpty()) && (password.getText() != null && !password.getText().isEmpty())) {
                try {
                    login(email.getText().toLowerCase(), password.getText().toLowerCase());

                    if (control) {
                        label.setText("Logged in Successfully");
                        new MainPage().display(window, email.getText());
                    }
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            } else {
                label.setText("No field can be left blank.");
            }
        });

        //Setting an action for the Clear button
        clear.setOnAction(e -> {
            email.clear();
            password.clear();
            label.setText(null);
        });

        //Setting an action for the Back button
        back.setOnAction(e -> HomePage.display(AppUI.window));

        //Setting an action for the Forgot Password button
        forgotPass.setOnAction(e -> forgotPassword(window));

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.show();
    }

    private void forgotPassword(Stage window) {
        window.setTitle("Forgot Password");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Email text field
        final TextField email = new TextField();
        email.setPromptText("Enter your First Name.");
        email.setPrefColumnCount(10);
        email.getText();
        GridPane.setConstraints(email, 0, 0);
        grid.getChildren().add(email);

        //Defining the Password text field
        final PasswordField password = new PasswordField();
        password.setPromptText("Enter your Last Name.");
        password.getText();
        GridPane.setConstraints(password, 0, 1);
        grid.getChildren().add(password);

        //Defining the Submit button
        Button verify = new Button("Verify");
        GridPane.setConstraints(verify, 1, 0);
        grid.getChildren().add(verify);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);

        //Defining the back button
        Button back = new Button("Go Back");
        GridPane.setConstraints(back, 1, 2);
        grid.getChildren().add(back);

        //Adding a Label
        final Label label = new Label("Enter the first and last name associated with your account to reset your password");
        GridPane.setConstraints(label, 0, 3);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        Scene scene1 = new Scene(grid);
        window.setScene(scene1);
        window.show();
    }

    public void signUpWindow(Stage window) {
        window.setTitle("Sign Up");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Name text field
        final TextField firstName = new TextField();
        firstName.setPromptText("Enter your first name.");
        firstName.setPrefColumnCount(10);
        firstName.getText();
        GridPane.setConstraints(firstName, 0, 0);
        grid.getChildren().add(firstName);

        //Defining the Last Name text field
        final TextField lastName = new TextField();
        lastName.setPromptText("Enter your last name.");
        GridPane.setConstraints(lastName, 0, 1);
        grid.getChildren().add(lastName);

        //Defining the Phone Number text field
        final TextField phoneNumber = new TextField();
        phoneNumber.setPrefColumnCount(15);
        phoneNumber.setPromptText("Enter your phone number.");
        GridPane.setConstraints(phoneNumber, 0, 2);
        grid.getChildren().add(phoneNumber);

        //Defining the Email text field
        final TextField email = new TextField();
        email.setPromptText("Enter your email.");
        email.getText();
        GridPane.setConstraints(email, 0, 3);
        grid.getChildren().add(email);

        //Defining the Password text field
        final PasswordField password = new PasswordField();
        password.setPromptText("Enter your password.");
        password.getText();
        GridPane.setConstraints(password, 0, 4);
        grid.getChildren().add(password);

        //Defining the Sign-Up button
        Button signUp = new Button("Sign up");
        GridPane.setConstraints(signUp, 1, 0);
        grid.getChildren().add(signUp);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);

        //Defining the Back button
        Button back = new Button("Go Back");
        GridPane.setConstraints(back, 1, 2);
        grid.getChildren().add(back);

        //Adding a Label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 5);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        //Setting an action for the signUp button
        signUp.setOnAction(e -> {
            try {
                if ((email.getText() != null && !email.getText().isEmpty()) &&
                        (password.getText() != null && !password.getText().isEmpty()) &&
                        (firstName.getText() != null && !firstName.getText().isEmpty()) &&
                        (lastName.getText() != null && !lastName.getText().isEmpty()) &&
                        (phoneNumber.getText() != null && !phoneNumber.getText().isEmpty())) {
                    createAccount(email.getText().toLowerCase(), password.getText().toLowerCase(),
                            firstName.getText().toLowerCase(), lastName.getText().toLowerCase(), Long.parseLong(phoneNumber.getText()));

                    if (control) {
                        label.setText("Successfully Signed Up");
                        email.clear();
                        password.clear();
                        firstName.clear();
                        lastName.clear();
                        phoneNumber.clear();
                        new UserInformation().loginWindow(window);
                    }
                } else {
                    if (!control) {
                        label.setText("There is already a user with that email");
                    } else {
                        label.setText("No field can be left blank.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //Setting an action for the Clear button
        clear.setOnAction(e -> {
            email.clear();
            password.clear();
            firstName.clear();
            lastName.clear();
            phoneNumber.clear();
            label.setText(null);
        });

        back.setOnAction(e -> HomePage.display(AppUI.window));

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.show();
    }

    public void updateUserInfoWindow(String email) {
        Stage window = new Stage();
        VBox layout = new VBox();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);

        window.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows until this one is taken care of
        window.setTitle("Update your Information");
        window.setMinWidth(200);

        //Defining the Name text field
        final TextField firstName = new TextField();
        firstName.setPromptText("Enter your first name.");
        firstName.setPrefColumnCount(10);
        firstName.getText();
        GridPane.setConstraints(firstName, 0, 0);
        grid.getChildren().add(firstName);

        //Defining the Last Name text field
        final TextField lastName = new TextField();
        lastName.setPromptText("Enter your last name.");
        GridPane.setConstraints(lastName, 0, 1);
        grid.getChildren().add(lastName);

        //Defining the Phone Number text field
        final TextField phoneNumber = new TextField();
        phoneNumber.setPrefColumnCount(15);
        phoneNumber.setPromptText("Enter your phone number.");
        GridPane.setConstraints(phoneNumber, 0, 2);
        grid.getChildren().add(phoneNumber);

        //Defining the Email text field
        final TextField newEmail = new TextField();
        newEmail.setPromptText("Enter your email.");
        newEmail.getText();
        GridPane.setConstraints(newEmail, 0, 3);
        grid.getChildren().add(newEmail);

        //Defining the Password text field
        final PasswordField password = new PasswordField();
        password.setPromptText("Enter your password.");
        password.getText();
        GridPane.setConstraints(password, 0, 4);
        grid.getChildren().add(password);

        //Defining the Update Info button
        Button updateInfo = new Button("Update Info");
        GridPane.setConstraints(updateInfo, 1, 0);
        grid.getChildren().add(updateInfo);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);

        //Defining the Back button
        Button back = new Button("Go Back");
        GridPane.setConstraints(back, 1, 2);
        grid.getChildren().add(back);

        //Adding a Label
        final Label label = new Label("Enter your new information");
        GridPane.setConstraints(label, 0, 5);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        layout.getChildren().add(grid);
        layout.setAlignment(Pos.CENTER);

        //Setting an action for the Clear button
        clear.setOnAction(e -> {
            newEmail.clear();
            password.clear();
            firstName.clear();
            lastName.clear();
            phoneNumber.clear();
        });

        //Setting an action for the Back button
        back.setOnAction(e -> window.close());

        //Setting an action for the signUp button
        updateInfo.setOnAction(e -> {
            try {
                if ((newEmail.getText() != null && !newEmail.getText().isEmpty()) &&
                        (password.getText() != null && !password.getText().isEmpty()) &&
                        (firstName.getText() != null && !firstName.getText().isEmpty()) &&
                        (lastName.getText() != null && !lastName.getText().isEmpty()) &&
                        (phoneNumber.getText() != null && !phoneNumber.getText().isEmpty())) {
                    updateAccount(newEmail.getText().toLowerCase(), password.getText().toLowerCase(),
                            firstName.getText().toLowerCase(), lastName.getText().toLowerCase(), Long.parseLong(phoneNumber.getText()), email);

                    if (control) {
                        label.setText("Successfully Updated Information");
                        newEmail.clear();
                        password.clear();
                        firstName.clear();
                        lastName.clear();
                        phoneNumber.clear();
                        window.close();
                    }
                } else {
                    if (!control) {
                        label.setText("There is already a user with that email");
                    } else {
                        label.setText("No field can be left blank.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
