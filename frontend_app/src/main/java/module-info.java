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
    requires java.desktop;

    opens com.project.frontend_app to javafx.fxml;
    opens com.project.frontend_app.controller to javafx.fxml;
    opens com.project.frontend_app.controller.customer to javafx.fxml;
    opens com.project.frontend_app.controller.employee to javafx.fxml;

    opens com.project.frontend_app.model to javafx.base;
    opens com.project.frontend_app.model.enums to javafx.base;

    exports com.project.frontend_app;
    exports com.project.frontend_app.controller;
    exports com.project.frontend_app.controller.customer;
    exports com.project.frontend_app.controller.employee;
    exports com.project.frontend_app.model;
    exports com.project.frontend_app.model.enums;
}