package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Appointment;
import model.AppointmentDB;
import model.PetDB;
import view.AppUI;

import java.util.ArrayList;
import java.util.HashMap;

public class AppointmentInformation {
    private static boolean control = false;
    final ObservableList<Appointment> appointmentObservableList = FXCollections.observableArrayList();

    private static void addAppointment(String appointmentType, String appointmentPet,
                                       String appointmentDate, String appointmentName,
                                       String userEmail) throws Exception {
        Appointment appt = new Appointment();
        appt.setAppointmentType(appointmentType);
        appt.setAppointmentPet(appointmentPet);
        appt.setAppointmentDate(appointmentDate);
        appt.setAppointmentName(appointmentName);
        appt.setUserEmail(userEmail);


        if (new PetDB().verifyPet(appointmentPet, userEmail)) {
            try {
                new AppointmentDB().addAppointment(appt);
                control = true;
            } catch (Exception ignored) {
            }
        } else {
            throw new Exception("You have no pet with that name");
        }
    }

    public void displayAppointmentHistory(ArrayList<Appointment> appt) {
        Stage stage = new Stage();

        appointmentObservableList.addAll(appt);
        TableView<Appointment> table = new TableView<>();
        Scene scene = new Scene(new Group());
        stage.setTitle("Appointment History");
        stage.setWidth(435);
        stage.setHeight(500);

        final Label label = new Label("Appointments");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn<Appointment, String> nameCol = new TableColumn<>("Pet Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("appointmentPet"));

        TableColumn<Appointment, String> appointmentTypeCol = new TableColumn<>("Type");
        appointmentTypeCol.setMinWidth(100);
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));

        TableColumn<Appointment, String> appointmentDateCol = new TableColumn<>("Date");
        appointmentDateCol.setMinWidth(100);
        appointmentDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        TableColumn<Appointment, String> ownerCol = new TableColumn<>("Owner Name");
        ownerCol.setMinWidth(100);
        ownerCol.setCellValueFactory(new PropertyValueFactory<>("appointmentName"));

        table.setItems(appointmentObservableList);
        table.getColumns().addAll(nameCol, appointmentTypeCol, appointmentDateCol, ownerCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public void createAppointmentWindow(Stage window, String email) {
        window.setTitle("Create Appointment");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Appointment Type field
        final ComboBox<String> appointmentType = new ComboBox<>();
        appointmentType.setPromptText("Select an appointment type");
        ObservableList<String> types = appointmentType.getItems();
        types.add("Vet Visit");
        types.add("Grooming Session");
        appointmentType.setItems(types);
        GridPane.setConstraints(appointmentType, 0, 0);
        grid.getChildren().add(appointmentType);

        //Defining the Appointment Pet text field
        final TextField appointmentPet = new TextField();
        appointmentPet.setPromptText("Enter your pet's name.");
        GridPane.setConstraints(appointmentPet, 0, 1);
        grid.getChildren().add(appointmentPet);

        //Defining the Appointment Date text field
        final DatePicker appointmentDate = new DatePicker();
        appointmentDate.setPromptText("Set a date for your appointment.");
        GridPane.setConstraints(appointmentDate, 0, 2);
        grid.getChildren().add(appointmentDate);

        //Defining the Appointment Name text field
        final TextField appointmentName = new TextField();
        appointmentName.setPromptText("Enter a name for your appointment.");
        appointmentName.getText();
        GridPane.setConstraints(appointmentName, 0, 3);
        grid.getChildren().add(appointmentName);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);

        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        //Adding a Label
        final Label label = new Label();
        GridPane.setConstraints(label, 0, 5);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        //Defining the Back button
        Button back = new Button("Go Back");
        GridPane.setConstraints(back, 1, 2);
        grid.getChildren().add(back);

        //Setting an action for the signUp button
        submit.setOnAction(e -> {
            try {
                if ((appointmentPet.getText() != null && !appointmentPet.getText().isEmpty()) &&
                        (appointmentName.getText() != null && !appointmentName.getText().isEmpty()) &&
                        (appointmentDate.getEditor().getText() != null && !appointmentDate.getEditor().getText().isEmpty())) {
                    addAppointment(appointmentType.getValue(), appointmentPet.getText().toLowerCase(),
                            appointmentDate.getEditor().getText().toLowerCase(), appointmentName.getText().toLowerCase(), email);

                    if (control) {
                        label.setText("Successfully Added an Appointment");
                        appointmentPet.clear();
                        appointmentName.clear();
                        appointmentType.valueProperty().set(null);
                        appointmentDate.getEditor().clear();
                        new MainPage().display(window, email);
                    }
                } else {
                    if (!control) {
                        label.setText("There was an error");
                    } else {
                        label.setText("No field can be left blank.");
                    }
                }
            } catch (Exception ex) {
                label.setText(ex.getMessage());
            }
        });

        //Setting an action for the Clear button
        clear.setOnAction(e -> {
            appointmentPet.clear();
            appointmentType.valueProperty().set(null);
            appointmentName.clear();
            appointmentDate.getEditor().clear();

        });

        back.setOnAction(e -> new MainPage().display(AppUI.window, email));

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.show();
    }

    public void cancelAppointmentWindow(String email) throws Exception {
        Stage stage = AppUI.window;
        VBox vBox = new VBox(5);
        ArrayList<Appointment> appointments = new AppointmentDB().appointmentHistory(email);
        HashMap<CheckBox, Appointment> checkBoxAppointmentHashMap = new HashMap<>();

        Label label = new Label("Select the appointments you want to cancel:");
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12);
        label.setFont(font);
        vBox.setPadding(new Insets(5, 5, 5, 50));

        for (Appointment appointment : appointments) {
            String petName = appointment.getAppointmentPet().substring(0, 1).toUpperCase() + appointment.getAppointmentPet().substring(1);
            String appointmentType = appointment.getAppointmentType();
            String appointmentDate = appointment.getAppointmentDate();
            String checkboxText = petName + "-" + appointmentType + "-" + appointmentDate;

            CheckBox currentCheckBox = new CheckBox(checkboxText);
            checkBoxAppointmentHashMap.put(currentCheckBox, appointment);
            vBox.getChildren().add(currentCheckBox);
        }

        //Defining the Submit button
        Button submit = new Button("Submit");
        vBox.getChildren().add(submit);

        //Defining the Close button
        Button back = new Button("Go Back");
        vBox.getChildren().add(back);

        //Setting an action for the Back button
        back.setOnAction(e -> new MainPage().display(AppUI.window, email));

        //Setting an action for the signUp button
        submit.setOnAction(e -> {
            ArrayList<Appointment> appointmentsToRemove = new ArrayList<>();
            for (CheckBox currentCheckBox : checkBoxAppointmentHashMap.keySet()) {
                if (currentCheckBox.isSelected()) {
                    appointmentsToRemove.add(checkBoxAppointmentHashMap.get(currentCheckBox));
                }
            }
            try {
                new AppointmentDB().removeAppointment(appointmentsToRemove);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            new MainPage().display(AppUI.window, email);
        });

        //Setting the stage
        Scene scene = new Scene(vBox, 340, 210);
        stage.setTitle("Cancel Appointments");
        stage.setScene(scene);
        stage.show();
    }
}
