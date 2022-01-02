package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Pet;
import model.PetDB;
import view.AppUI;

import java.util.ArrayList;

public class PetMedical {
    private static boolean control = false;
    final ObservableList<Pet> petObservableList = FXCollections.observableArrayList();

    private static void createPetMedicals(String ownerEmail, String petName, String petMedication, String petWeight) throws Exception {
        Pet pet = new Pet();
        pet.setName(petName);
        pet.setWeight(petWeight);
        pet.setMedication(petMedication);
        pet.setOwnerEmail(ownerEmail);

        if (new PetDB().verifyPet(petName, ownerEmail)) {
            try {
                new PetDB().addMedicalInformation(pet);
                control = true;
            } catch (Exception ignored) {
            }
        } else {
            throw new Exception("You have no pet with that name");
        }
    }

    public void petHistoryAlertBox(String userEmail) {
        Stage window = new Stage();
        VBox layout = new VBox();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);

        window.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows until this one is taken care of
        window.setTitle("Pet Medical History");
        window.setMinWidth(200);

        //Defining the Pet Name text field
        final TextField petName = new TextField();
        petName.setPromptText("Enter the pet's name.");
        GridPane.setConstraints(petName, 0, 1);
        grid.getChildren().add(petName);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 0, 3);
        grid.getChildren().add(clear);

        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 0, 2);
        grid.getChildren().add(submit);

        //Adding a Label
        final Label label = new Label("What pet?");
        GridPane.setConstraints(label, 0, 5);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        //Defining the Back button
        Button back = new Button("Close");
        GridPane.setConstraints(back, 1, 1);
        grid.getChildren().add(back);

        layout.getChildren().add(grid);
        layout.setAlignment(Pos.CENTER);

        //Setting an action for the Clear button
        clear.setOnAction(e -> petName.clear());

        //Setting an action for the Back button
        back.setOnAction(e -> window.close());

        //Setting an action for the Submit button
        submit.setOnAction(e -> {
            ArrayList<Pet> userPets = new ArrayList<>();
            if ((petName.getText() != null && !petName.getText().isEmpty())) {
                try {
                    userPets = new PetDB().medicalHistory(petName.getText().toLowerCase(), userEmail);
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
                displayPetHistory(userPets, window);
            } else {
                label.setText("Pet name cannot be left blank.");
            }
        });

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private void displayPetHistory(ArrayList<Pet> pets, Stage stage) {
        petObservableList.addAll(pets);
        TableView<Pet> table = new TableView<>();
        Scene scene = new Scene(new Group());
        stage.setTitle("Pet Medical History");
        stage.setWidth(435);
        stage.setHeight(500);

        final Label label = new Label("Pets");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn<Pet, String> nameCol = new TableColumn<>("Pet Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Pet, String> weightCol = new TableColumn<>("Pet Weight");
        weightCol.setMinWidth(100);
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

        TableColumn<Pet, String> medicationCol = new TableColumn<>("Medication");
        medicationCol.setMinWidth(200);
        medicationCol.setCellValueFactory(new PropertyValueFactory<>("medication"));

        table.setItems(petObservableList);
        table.getColumns().addAll(nameCol, weightCol, medicationCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    public void displayMedicalInformationWindow(Stage window, String email) {
        window.setTitle("Update Medical Information");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);

        //Defining the Pet Name field
        final TextField petName = new TextField();
        petName.setPromptText("Enter your pet's name.");
        petName.setPrefColumnCount(10);
        GridPane.setConstraints(petName, 0, 0);
        grid.getChildren().add(petName);

        //Defining the Pet Weight text field
        final TextField petWeight = new TextField();
        petWeight.setPromptText("Enter their current weight.");
        GridPane.setConstraints(petWeight, 0, 1);
        grid.getChildren().add(petWeight);

        //Defining the Medication text field
        final TextField petMedication = new TextField();
        petMedication.setPrefColumnCount(20);
        petMedication.setPromptText("Enter their current medication. (Or N/A)");
        GridPane.setConstraints(petMedication, 0, 2);
        grid.getChildren().add(petMedication);

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
                if ((petName.getText() != null && !petName.getText().isEmpty()) &&
                        (petWeight.getText() != null && !petWeight.getText().isEmpty()) &&
                        (petMedication.getText() != null && !petMedication.getText().isEmpty())) {
                    createPetMedicals(email, petName.getText().toLowerCase(), petMedication.getText().toLowerCase(), petWeight.getText().toLowerCase());

                    if (control) {
                        label.setText("Successfully Updated Pet Medical Information");
                        petName.clear();
                        petWeight.clear();
                        petMedication.clear();
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
            petName.clear();
            petWeight.clear();
            petMedication.clear();
        });

        back.setOnAction(e -> new MainPage().display(AppUI.window, email));

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.show();
    }
}