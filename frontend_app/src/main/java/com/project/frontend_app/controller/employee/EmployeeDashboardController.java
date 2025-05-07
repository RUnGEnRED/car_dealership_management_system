package com.project.frontend_app.controller.employee;

import com.project.frontend_app.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for the Employee Dashboard view.
 * Displays a personalized welcome message to the logged-in employee.
 */
public class EmployeeDashboardController {

    @FXML
    private Label welcomeLabel;

    /** Currently logged-in employee */
    private Employee loggedInEmployee;

    /**
     * Called automatically after FXML is loaded.
     * If employee is already set, updates welcome label.
     */
    public void initialize() {
        if (loggedInEmployee != null) {
            updateWelcomeMessage();
        }
    }

    /**
     * Sets the logged-in employee and updates the welcome message.
     * This method should be called from the login controller or main application.
     * @param employee The currently logged-in employee
     */
    public void setLoggedInEmployee(Employee employee) {
        this.loggedInEmployee = employee;
        updateWelcomeMessage();
    }

    /**
     * Updates the welcome label with the employee's name.
     */
    private void updateWelcomeMessage() {
        String fullName = loggedInEmployee.getFirstName() + " " + loggedInEmployee.getLastName();
        welcomeLabel.setText("Witaj " + fullName);
    }
}
