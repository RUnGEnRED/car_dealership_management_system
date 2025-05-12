package com.project.frontend_app.controller.employee;

import com.project.frontend_app.model.Employee;
import com.project.frontend_app.util.WindowHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static com.project.frontend_app.util.AlertHelper.showAlert;

/**
 * Controller for the Employee Dashboard view.
 * Displays a personalized welcome message to the logged-in employee.
 */
public class EmployeeDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;

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

    /**
     * Handles the "Manage Vehicles" button action.
     * Opens a new window displaying available vehicles, a form for adding vehicles, and a button for deleting vehicles.
     * Shows error alert if the view cannot be loaded.
     */
    @FXML
    private void handleManageVehicles() {
        try {
            // Load the FXML file for the vehicle list
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/employee/manage-vehicles-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML: manage-vehicles-view.fxml");
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load vehicle list view.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Create a new stage (window) for the vehicle list
            Stage vehicleListStage = new Stage();
            vehicleListStage.setTitle("Available Vehicles");
            vehicleListStage.setScene(new Scene(root));
            vehicleListStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the vehicle list view.");
        }
    }

    /**
     * Handles the "Purchase Requests" button action.
     * Placeholder for future implementation.
     */
    @FXML
    public void handlePurchaseRequests(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, "Purchase Requests", "Purchase Requests... (Not implemented)");
    }

    /**
     * Handles the "Service Requests" button action.
     * Placeholder for future implementation.
     */
    @FXML
    public void handleServiceRequests(ActionEvent actionEvent) {
        showAlert(Alert.AlertType.INFORMATION, "Service Requests", "Service Requests... (Not implemented)");
    }

    /**
     * Handles the "My Profile" button action.
     */
    @FXML
    public void handleProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/frontend_app/view/employee/employee-profile-view.fxml"));
            Parent root = loader.load();

            EmployeeProfileController controller = loader.getController();
            controller.setLoggedInEmployee(loggedInEmployee);

            Stage profileStage = new Stage();
            profileStage.setTitle("My Profile");
            profileStage.setScene(new Scene(root));
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the employee profile view.");
        }
    }


    /**
     * Handles the "Customers Profiles" button action.
     */
    @FXML
    public void handleCustomersProfile(ActionEvent actionEvent) {
        try {
            // Load the FXML file for the customers list
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/employee/customer-list-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML: customer-list-view.fxml");
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load customers list view.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // Create a new stage (window) for the vehicle list
            Stage vehicleListStage = new Stage();
            vehicleListStage.setTitle("Customers");
            vehicleListStage.setScene(new Scene(root));
            vehicleListStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the customers list view.");
        }
    }

    /**
     * Handles the "Logout" button action.
     */
    @FXML
    private void handleLogout() {
        WindowHelper.openLoginWindow();
        WindowHelper.closeWindow(logoutButton);
    }
}
