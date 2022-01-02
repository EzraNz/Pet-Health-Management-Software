package controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentDB;
import view.AppUI;

import java.util.ArrayList;

public class MainPage {
    private String currentUserEmail;
    private Stage mainWindow;
    private MenuBar menuBar;
    private GridPane mainArea;

    public void display(Stage window, String email) {
        VBox root = new VBox();
        currentUserEmail = email;
        mainWindow = window;

        buildMenuBar();
        buildMainArea();
        root.getChildren().addAll(menuBar, mainArea);

        Scene homePage = new Scene(root, 400, 400);

        mainWindow.setScene(homePage);
        mainWindow.setTitle("Pet Health Management");
        mainWindow.show();
    }

    private void buildMenuBar() {
        final Menu accountMenu = new Menu("Account");
        final Menu petMenu = new Menu("Pet");
        final Menu optionsMenu = new Menu("Options");

        menuBar = new MenuBar();
        menuBar.getMenus().addAll(accountMenu, petMenu, optionsMenu);

        //Defining the Log Out menu item
        MenuItem logOut = new MenuItem("Log Out");
        accountMenu.getItems().add(logOut);

        //Defining the Change Account Information menu item
        MenuItem changeInformation = new MenuItem("Change Account Information");
        accountMenu.getItems().add(changeInformation);

        //Defining the View Pet History menu item
        MenuItem viewPetHistory = new MenuItem("View Pet Medical History");
        petMenu.getItems().add(viewPetHistory);

        //Defining the Update Pet Information menu item
        MenuItem updatePetInformation = new MenuItem("Update Pet Information");
        petMenu.getItems().add(updatePetInformation);

        //Defining the Cancel Appointment menu item
        MenuItem cancelAppointment = new MenuItem("Cancel Appointment");
        petMenu.getItems().add(cancelAppointment);

        //Defining the View Appointment History menu item
        MenuItem viewAppointmentHistory = new MenuItem("View Appointment History");
        petMenu.getItems().add(viewAppointmentHistory);

        //Defining the exit button
        MenuItem exit = new MenuItem("Exit");
        optionsMenu.getItems().add(exit);

        //Setting an action for the Log-out menu item
        logOut.setOnAction(e -> {
            currentUserEmail = "";
            HomePage.display(AppUI.window);
        });

        //Setting an action for the Change Account Information menu item
        changeInformation.setOnAction(e -> new UserInformation().updateUserInfoWindow(currentUserEmail));

        //Setting an action for the View Pet History menu item
        viewPetHistory.setOnAction(e -> new PetMedical().petHistoryAlertBox(currentUserEmail));

        //Setting an action for the Update Pet Information menu item
        updatePetInformation.setOnAction(e -> new PetInformation().updatePetInfoAlertBox(currentUserEmail));

        //Setting an action for the View Appointment History menu item
        viewAppointmentHistory.setOnAction(e -> {
            ArrayList<Appointment> userAppointments = new ArrayList<>();
            try {
                userAppointments = new AppointmentDB().appointmentHistory(currentUserEmail);
            } catch (Exception ignored) {
            }
            new AppointmentInformation().displayAppointmentHistory(userAppointments);
        });

        //Setting an action for the View Appointment History menu item
        cancelAppointment.setOnAction(e -> {
            try {
                new AppointmentInformation().cancelAppointmentWindow(currentUserEmail);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        //Setting an action for the Exit menu item
        exit.setOnAction(e -> mainWindow.close());
    }

    private void buildMainArea() {
        mainArea = new GridPane();
        mainArea.setPadding(new Insets(10, 10, 10, 10));
        mainArea.setVgap(5);
        mainArea.setHgap(5);

        //Defining the addPetButton button
        Button addPetButton = new Button("Add a Pet");
        GridPane.setConstraints(addPetButton, 10, 10);
        mainArea.getChildren().add(addPetButton);
        addPetButton.setOnAction(e -> new PetInformation().addPetWindow(mainWindow, currentUserEmail));

        //Defining the Set Appointment button
        Button setAppointment = new Button("Set Appointment");
        GridPane.setConstraints(setAppointment, 10, 12);
        mainArea.getChildren().add(setAppointment);
        setAppointment.setOnAction(e -> new AppointmentInformation().createAppointmentWindow(mainWindow, currentUserEmail));

        //Defining the updatePetMedicalInformation button
        Button updatePetMedicalInformation = new Button("Update Pet Medical Information");
        GridPane.setConstraints(updatePetMedicalInformation, 10, 11);
        mainArea.getChildren().add(updatePetMedicalInformation);
        updatePetMedicalInformation.setOnAction(e -> new PetMedical().displayMedicalInformationWindow(mainWindow, currentUserEmail));
    }
}
