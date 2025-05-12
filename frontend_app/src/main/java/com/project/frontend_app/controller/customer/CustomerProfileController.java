package com.project.frontend_app.controller.customer;

import com.project.frontend_app.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
/**
 * Controller for the profile page in the customer interface.
 * Dispaly customer personal information.
 */
public class CustomerProfileController {

    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneNumberLabel;

    @FXML private TextField newEmailField;
    @FXML private TextField newPhoneField;


    private Customer loggedInCustomer;

    /**
     * Sets the logged-in customer and updates the UI accordingly.
     * @param customer The Customer object representing the logged-in user
     */
    public void setLoggedInCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        populateProfileData();
    }

    /**
     * Populates the profile data in the UI with the logged-in customer's information.
     */
    private void populateProfileData() {
        if (loggedInCustomer != null) {
            firstNameLabel.setText(loggedInCustomer.getFirstName());
            lastNameLabel.setText(loggedInCustomer.getLastName());
            phoneNumberLabel.setText(loggedInCustomer.getPhoneNumber());
            emailLabel.setText(loggedInCustomer.getEmail());
        }
    }

    /**
     * Handles the "Update Contact" button action.
     * Updates the customer's contact information (email and phone number) if they are not empty.
     * Displays an alert confirming the update.
     */
    @FXML
    private void handleUpdateContact() {
        String newEmail = newEmailField.getText().trim();
        String newPhone = newPhoneField.getText().trim();

        boolean updated = false;

        if (!newEmail.isEmpty()) {
            loggedInCustomer.setEmail(newEmail);
            emailLabel.setText(newEmail);
            updated = true;
        }

        if (!newPhone.isEmpty()) {
            loggedInCustomer.setPhoneNumber(newPhone);
            phoneNumberLabel.setText(newPhone);
            updated = true;
        }

        newEmailField.clear();
        newPhoneField.clear();

        // Show a confirmation alert if there was an update
        if (updated) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("Your contact information has been updated successfully.");
            alert.showAndWait();
        } else {
            // If no update was made, show an info alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
