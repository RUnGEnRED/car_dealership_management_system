package com.project.frontend_app;

import static com.project.frontend_app.util.AlertHelper.showAlert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;


/**
 * START POINT OF THE APPLICATION
 * Main class for the frontend application.
 * Initializes the login screen and launches the application.
 */
public class FrontendAppApplication extends Application {

    // Adjusted size for login
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 300;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Load the FXML file
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/login-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML file: login-view.fxml. Check the path.");
                return;
            }

            // Load the FXML file
            System.out.println("Loading FXML from: " + fxmlLocation);
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
            Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
            stage.setTitle("Car Dealership - Login");
            stage.setScene(scene);
            stage.setResizable(false); // Disable window resizing
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the login screen.");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}