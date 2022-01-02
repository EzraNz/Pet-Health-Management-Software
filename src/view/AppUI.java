package view;

import controller.HomePage;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppUI extends Application {
    public static Stage window;

    @Override
    public void start(Stage stage) {
        window = stage;
        HomePage.display(window);
    }
}