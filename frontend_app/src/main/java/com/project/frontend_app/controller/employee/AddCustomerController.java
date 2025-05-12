package com.project.frontend_app.controller.employee;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static com.project.frontend_app.util.AlertHelper.showAlert;

/**
 * Controller for adding a new customer in the employee interface.
 * Displays a form to add a new customer.
 */
public class AddCustomerController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField streetField;
    @FXML private TextField houseNumberField;
    @FXML private TextField apartmentField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;

    /**
     * Handles the "Save Customer" button action.
     * Temporarily, only alert messages are displayed for validation and success.
     * Further implementation is needed to actually save the customer to the data store.
     *
     * @param event The event triggered when the button is clicked.
     */
    @FXML
    public void handleSaveCustomer(ActionEvent event) {
        try {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String street = streetField.getText().trim();
            String houseNumber = houseNumberField.getText().trim();
            String apartment = apartmentField.getText().trim();
            String city = cityField.getText().trim();
            String postalCode = postalCodeField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please fill in all required fields.");
                return;
            }

            showAlert(Alert.AlertType.INFORMATION, "Customer Added", "The customer has been successfully added.");
            clearFormFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while adding the customer.");
        }
    }

    /**
     * Clears all the fields in the form after the customer has been added.
     * This is called when the "Save Customer" action is successful.
     */
    private void clearFormFields() {
        firstNameField.clear();
        lastNameField.clear();
        phoneField.clear();
        emailField.clear();
        streetField.clear();
        houseNumberField.clear();
        apartmentField.clear();
        cityField.clear();
        postalCodeField.clear();
    }

    /**
     * Handles the close button action.
     * Closes the current window/stage.
     */
    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
