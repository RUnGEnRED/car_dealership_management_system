package com.project.frontend_app.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


/**
 * Utility class for displaying common JavaFX Alert dialogs.
 */
public final class AlertHelper {

    private AlertHelper() {
        throw new IllegalStateException("Utility class should not be instantiated.");
    }

    /**
     * Displays a standard JavaFX Alert dialog.
     *
     * @param alertType The type of the alert (e.g., INFORMATION, WARNING, ERROR).
     * @param title     The text for the window title bar.
     * @param content   The main message text to display in the alert.
     */
    public static void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait(); // Show the alert and wait for the user to close it
    }

    /**
     * Overloaded version to include header text if needed.
     *
     * @param alertType The type of the alert.
     * @param title     The text for the window title bar.
     * @param header    The header text (usually a brief summary).
     * @param content   The main message text.
     */
    public static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait(); // Show the alert and wait for the user to close it
    }
}