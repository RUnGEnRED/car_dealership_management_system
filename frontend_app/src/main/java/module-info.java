module com.project.frontend_app.frontend_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens com.project.frontend_app.frontend_app to javafx.fxml;
    exports com.project.frontend_app.frontend_app;
    exports com.project.frontend_app.frontend_app.controller;
    opens com.project.frontend_app.frontend_app.controller to javafx.fxml;
}