package com.project.frontend_app.controller.customer;

import com.project.frontend_app.controller.employee.EmployeeProfileController;
import com.project.frontend_app.model.Customer;

import static com.project.frontend_app.util.AlertHelper.showAlert;

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


/**
 * Controller class for the Customer Dashboard view.
 * Handles navigation between different customer functionalities including:
 * - Viewing available vehicles
 * - Requesting vehicle purchases
 * - Viewing purchase requests
 * - Managing profile
 * - Logging out
 */
public class CustomerDashboardController {

    @FXML private Button viewVehiclesButton;
    @FXML private Button requestPurchaseButton;
    @FXML private Button myRequestsButton;
    @FXML private Button profileButton;
    @FXML private Button logoutButton;
    @FXML private Label welcomeLabel;

    // Placeholder for the logged-in customer
    private Customer loggedInCustomer;

    /**
     * Initializes the controller and performs basic initialization checks.
     */
    @FXML
    public void initialize() {
        // Basic initialization checks
        if (welcomeLabel == null) {
            System.err.println("Error: welcomeLabel is null during initialization.");
        }
    }

    /**
     * Sets the logged-in customer and updates the UI accordingly.
     * @param customer The Customer object representing the logged-in user
     */
    public void setLoggedInCustomer(Customer customer) {
        this.loggedInCustomer = customer;
        if (loggedInCustomer != null) {
            welcomeLabel.setText("Welcome, " + loggedInCustomer.getFirstName() + "!");
        } else {
            welcomeLabel.setText("Welcome!");
        }
    }

    /**
     * Handles the "View Vehicle List" button action.
     * Opens a new window displaying available vehicles.
     * Shows error alert if the view cannot be loaded.
     */
    @FXML
    private void handleViewVehiclesList() {
        try {
            // Load the FXML file for the vehicle list
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/customer/vehicle-list-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML: vehicle-list-view.fxml");
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
     * Handles the "Request Vehicle Purchase" button action.
     * Opens a new window for submitting vehicle purchase requests.
     * Requires valid customer data and shows appropriate errors if missing.
     */
    @FXML
    private void handleRequestPurchase() {
        if (loggedInCustomer == null) {
            System.err.println("Error: Logged in customer data is null.");
            showAlert(Alert.AlertType.ERROR, "Error", "Cannot open request view. Logged in customer data is missing.");
            return;
        }

        try {
            // Load the FXML file for the vehicle request
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/customer/vehicle-request-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML: vehicle-request-view.fxml");
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load vehicle request view.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // --- Pass data to the new controller ---
            VehicleRequestController controller = loader.getController();
            controller.initializeData(loggedInCustomer);
            // --- End Pass data ---

            // Create a new stage (window) for the vehicle request
            Stage requestStage = new Stage();
            requestStage.setTitle("Request Vehicle Purchase");
            requestStage.setScene(new Scene(root));
            requestStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the vehicle request view.");
        }
    }

    /**
     * Handles the "My Requests" button action.
     * Placeholder for future implementation.
     */
    @FXML
    private void handleMyRequests() {
        showAlert(Alert.AlertType.INFORMATION, "Navigation", "Navigating to My Requests... (Not implemented)");
    }

    /**
     * Handles the "My Profile" button action.
     */
    @FXML
    public void handleProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/project/frontend_app/view/customer/customer-profile-view.fxml"));
            Parent root = loader.load();

            CustomerProfileController controller = loader.getController();
            controller.setLoggedInCustomer(loggedInCustomer);

            Stage profileStage = new Stage();
            profileStage.setTitle("My Profile");
            profileStage.setScene(new Scene(root));
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the customer profile view.");
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