package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Pet;
import model.PetDB;
import view.AppUI;

public class PetInformation {
    private static boolean control = false;

    private static void createPet(String coatColor, String dateOfBirth, String ownerEmail, String petName, String petSpecies, String petNotes) {
        Pet pet = new Pet();
        pet.setName(petName);
        pet.setNotes(petNotes);
        pet.setSpecies(petSpecies);
        pet.setCoatColor(coatColor);
        pet.setOwnerEmail(ownerEmail);
        pet.setDateOfBirth(dateOfBirth);
        try {
            new PetDB().addPet(pet);
            control = true;
        } catch (Exception ignored) {
        }
    }

    private static void updatePet(String coatColor, String dateOfBirth, String ownerEmail, String petName, String petSpecies, String petNotes, String originalName) {
        Pet pet = new Pet();
        pet.setName(petName);
        pet.setNotes(petNotes);
        pet.setSpecies(petSpecies);
        pet.setCoatColor(coatColor);
        pet.setOwnerEmail(ownerEmail);
        pet.setDateOfBirth(dateOfBirth);
        try {
            new PetDB().updatePet(pet, originalName, ownerEmail);
            control = true;
        } catch (Exception ignored) {
        }
    }

    public void addPetWindow(Stage window, String email) {
        window.setTitle("Add Pet");

        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Pet Name field
        final TextField petName = new TextField();
        petName.setPromptText("Enter your pet's name.");
        petName.setPrefColumnCount(10);
        petName.getText();
        GridPane.setConstraints(petName, 0, 0);
        grid.getChildren().add(petName);

        //Defining the Coat Color text field
        final TextField coatColor = new TextField();
        coatColor.setPromptText("Enter their coat color");
        GridPane.setConstraints(coatColor, 0, 1);
        grid.getChildren().add(coatColor);

        //Defining the Date of Birth text field
        final DatePicker dateOfBirth = new DatePicker();
        dateOfBirth.setPromptText("Enter their Date of Birth.");
        GridPane.setConstraints(dateOfBirth, 0, 2);
        grid.getChildren().add(dateOfBirth);

        //Defining the Species text field
        final TextField species = new TextField();
        species.setPromptText("Enter their species.");
        species.getText();
        GridPane.setConstraints(species, 0, 3);
        grid.getChildren().add(species);

        //Defining the Pet Notes text field
        final TextField petNotes = new TextField();
        petNotes.setPromptText("Enter any pet notes.");
        petNotes.getText();
        GridPane.setConstraints(petNotes, 0, 4);
        grid.getChildren().add(petNotes);

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
                        (coatColor.getText() != null && !coatColor.getText().isEmpty()) &&
                        (species.getText() != null && !species.getText().isEmpty()) &&
                        (dateOfBirth.getEditor().getText() != null && !dateOfBirth.getEditor().getText().isEmpty())) {
                    createPet(coatColor.getText().toLowerCase(), dateOfBirth.getEditor().getText().toLowerCase(), email,
                            petName.getText().toLowerCase(), species.getText().toLowerCase(), petNotes.getText());

                    if (control) {
                        label.setText("Successfully Added Pet");
                        petName.clear();
                        coatColor.clear();
                        petNotes.clear();
                        species.clear();
                        dateOfBirth.getEditor().clear();
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
                ex.printStackTrace();
            }
        });

        //Setting an action for the Clear button
        clear.setOnAction(e -> {
            petName.clear();
            coatColor.clear();
            petNotes.clear();
            species.clear();
            dateOfBirth.getEditor().clear();
        });

        back.setOnAction(e -> new MainPage().display(AppUI.window, email));

        Scene scene = new Scene(grid, 270, 190);
        window.setScene(scene);
        window.show();
    }

    public void updatePetInfoAlertBox(String email) {
        Stage window = new Stage();
        VBox layout = new VBox();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);

        window.initModality(Modality.APPLICATION_MODAL); // Block user interaction with other windows until this one is taken care of
        window.setTitle("Update Pet Information");
        window.setMinWidth(200);

        //Defining the Pet Weight text field
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
            if ((petName.getText() != null && !petName.getText().isEmpty())) {
                try {
                    if (new PetDB().verifyPet(petName.getText().toLowerCase(), email)) {
                        updatePetWindow(petName.getText().toLowerCase(), email, window);
                    } else {
                        label.setText("You have no pet with that name.");
                    }
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            } else {
                label.setText("Pet name cannot be left blank.");
            }
        });

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    private void updatePetWindow(String originalName, String email, Stage window) {
        //Creating a GridPane container
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);

        window.setTitle("Update Pet Information");
        window.setMinWidth(310);

        //Defining the Pet Name field
        final TextField petName = new TextField();
        petName.setPromptText("Enter your pet's name.");
        petName.setPrefColumnCount(10);
        GridPane.setConstraints(petName, 0, 0);
        grid.getChildren().add(petName);

        //Defining the Coat Color text field
        final TextField coatColor = new TextField();
        coatColor.setPromptText("Enter their coat color");
        GridPane.setConstraints(coatColor, 0, 1);
        grid.getChildren().add(coatColor);

        //Defining the Date of Birth text field
        final DatePicker dateOfBirth = new DatePicker();
        dateOfBirth.setPromptText("Enter their Date of Birth");
        GridPane.setConstraints(dateOfBirth, 0, 2);
        grid.getChildren().add(dateOfBirth);

        //Defining the Species text field
        final TextField species = new TextField();
        species.setPromptText("Enter their species.");
        species.getText();
        GridPane.setConstraints(species, 0, 3);
        grid.getChildren().add(species);

        //Defining the Pet Notes text field
        final TextField petNotes = new TextField();
        petNotes.setPromptText("Enter any pet notes.");
        petNotes.getText();
        GridPane.setConstraints(petNotes, 0, 4);
        grid.getChildren().add(petNotes);

        //Defining the Clear button
        Button clear = new Button("Clear");
        GridPane.setConstraints(clear, 1, 1);
        grid.getChildren().add(clear);

        //Defining the Submit button
        Button submit = new Button("Submit");
        GridPane.setConstraints(submit, 1, 0);
        grid.getChildren().add(submit);

        //Adding a Label
        final Label label = new Label("Update your pet information");
        GridPane.setConstraints(label, 0, 5);
        GridPane.setColumnSpan(label, 2);
        grid.getChildren().add(label);

        //Defining the Back button
        Button back = new Button("Go Back");
        GridPane.setConstraints(back, 1, 2);
        grid.getChildren().add(back);

        //Setting an action for the Clear button
        clear.setOnAction(e -> {
            petName.clear();
            coatColor.clear();
            petNotes.clear();
            species.clear();
            dateOfBirth.getEditor().clear();
        });

        //Setting an action for the Back button
        back.setOnAction(e -> window.close());

        //Setting an action for the signUp button
        submit.setOnAction(e -> {
            try {
                if ((petName.getText() != null && !petName.getText().isEmpty()) &&
                        (coatColor.getText() != null && !coatColor.getText().isEmpty()) &&
                        (species.getText() != null && !species.getText().isEmpty()) &&
                        (dateOfBirth.getEditor().getText() != null && !dateOfBirth.getEditor().getText().isEmpty())) {
                    updatePet(coatColor.getText().toLowerCase(), dateOfBirth.getEditor().getText().toLowerCase(), email,
                            petName.getText().toLowerCase(), species.getText().toLowerCase(), petNotes.getText(), originalName);

                    if (control) {
                        label.setText("Successfully Updated Pet Information");
                        petName.clear();
                        coatColor.clear();
                        petNotes.clear();
                        species.clear();
                        dateOfBirth.getEditor().clear();
                        window.close();
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

        Scene scene = new Scene(grid);
        window.setScene(scene);
        window.show();
    }
}
