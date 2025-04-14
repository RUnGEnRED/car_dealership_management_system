package com.project.frontend_app.util;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;


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
}