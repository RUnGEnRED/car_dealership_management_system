package com.project.frontend_app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;


/**
 * Utility class for common JavaFX Window/Stage operations.
 */
public final class WindowHelper {

    private WindowHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Closes the Stage (window) containing the given JavaFX Node.
     *
     * @param node A Node (like a Button, Label, Pane, etc.) that is currently
     *             part of the scene whose window should be closed.
     */
    public static void closeWindow(Node node) {
        if (node == null) {
            System.err.println("WindowHelper Error: Provided node is null. Cannot determine window to close.");
            return;
        }
        Scene scene = node.getScene();
        if (scene == null) {
            System.err.println("WindowHelper Error: Node is not part of a scene. Cannot determine window to close.");
            return;
        }
        Window window = scene.getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        } else {
            System.err.println("WindowHelper Warning: The window containing the node is not a Stage. Cannot close.");
        }
    }


    public static void openLoginWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(WindowHelper.class.getResource("/com/project/frontend_app/view/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to open login window");
            alert.setContentText("Could not load login-view.fxml");
            alert.showAndWait();
        }
    }
}