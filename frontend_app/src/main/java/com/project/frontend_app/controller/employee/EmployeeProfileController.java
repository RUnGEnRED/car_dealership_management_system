package com.project.frontend_app.controller.employee;

import com.project.frontend_app.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controller for the profile page in the employee interface.
 * Manages employee personal information and update contact info.
 */
public class EmployeeProfileController {

    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneNumberLabel;
    @FXML private Label positionLabel;

    @FXML private TextField newEmailField;
    @FXML private TextField newPhoneField;

    private Employee loggedInEmployee;

    /**
     * Sets the logged-in employee and updates the UI accordingly.
     * @param employee The Employee object representing the logged-in user
     */
    public void setLoggedInEmployee(Employee employee) {
        this.loggedInEmployee = employee;
        populateProfileData();
    }

    /**
     * Populates the profile data in the UI with the logged-in employee's information.
     * Updates the labels with the employee's first name, last name, email, phone number, and position.
     * If the employee is not set, no action will be performed.
     */
    private void populateProfileData() {
        if (loggedInEmployee != null) {
            firstNameLabel.setText(loggedInEmployee.getFirstName());
            lastNameLabel.setText(loggedInEmployee.getLastName());
            phoneNumberLabel.setText(loggedInEmployee.getPhoneNumber());
            emailLabel.setText(loggedInEmployee.getEmail());
            positionLabel.setText(loggedInEmployee.getPosition());
        }
    }

    /**
     * Handles the "Update Contact" button action.
     * Updates the employee's contact information (email and phone number) if they are not empty.
     * Displays an alert confirming the update.
     */
    @FXML
    private void handleUpdateContact() {
        String newEmail = newEmailField.getText().trim();
        String newPhone = newPhoneField.getText().trim();

        boolean updated = false;

        if (!newEmail.isEmpty()) {
            loggedInEmployee.setEmail(newEmail);
            emailLabel.setText(newEmail);
            updated = true;
        }

        if (!newPhone.isEmpty()) {
            loggedInEmployee.setPhoneNumber(newPhone);
            phoneNumberLabel.setText(newPhone);
            updated = true;
        }

        newEmailField.clear();
        newPhoneField.clear();

        // Show a confirmation alert if there was an update
        if (updated) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your contact information has been updated successfully.");
            alert.showAndWait();
        } else {
            // If no update was made, show an info alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Changes Made");
            alert.setHeaderText(null);
            alert.setContentText("Please enter new information to update.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
