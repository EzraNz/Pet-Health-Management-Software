package controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HomePage {
    public static void display(Stage window) {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Submit button
        Button logIn = new Button("Login");
        GridPane.setConstraints(logIn, 5, 5);
        grid.getChildren().add(logIn);
        logIn.setOnAction(e -> new UserInformation().loginWindow(window));

        //Defining the Clear button
        Button signUp = new Button("Sign up");
        GridPane.setConstraints(signUp, 6, 5);
        grid.getChildren().add(signUp);
        signUp.setOnAction(e -> new UserInformation().signUpWindow(window));

        Scene homePage = new Scene(grid, 200, 100);

        window.setScene(homePage);
        window.setTitle("Pet Health Management");
        window.show();
    }
}
