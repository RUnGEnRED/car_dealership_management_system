package com.project.frontend_app.controller;

import com.project.frontend_app.controller.customer.CustomerDashboardController;
import com.project.frontend_app.model.Customer;
import com.project.frontend_app.controller.employee.EmployeeDashboardController;
import com.project.frontend_app.model.Employee;
import com.project.frontend_app.service.impl.InMemoryAuthenticationService; // Use implementation for now
import com.project.frontend_app.service.interf.IAuthenticationService; // Use interface for now

import com.project.frontend_app.util.WindowHelper;
import static com.project.frontend_app.util.AlertHelper.showAlert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;




/**
 * Controller for handling user login functionality.
 * Manages the login view and authentication process for both customers and employees.
 */
public class LoginController {

    // UI components injected by FXML
    @FXML private ComboBox<String> userTypeComboBox;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    // Authentication service dependency
    private final IAuthenticationService authService = new InMemoryAuthenticationService();

    /**
     * Initializes the login view components when the FXML is loaded.
     * <p>
     * Sets up the user type selection dropdown with available options
     * and initializes the error message label.
     */
    @FXML
    public void initialize() {
        // Populate user type dropdown with available options
        userTypeComboBox.getItems().addAll("Customer", "Employee");
        
        // Initialize error label with empty text
        errorLabel.setText("");
    }

    /**
     * Handles the login button click event.
     * Validates user input and attempts authentication.
     * On successful authentication, navigates to appropriate dashboard.
     */
    @FXML
    private void handleLoginButtonAction() {
        // Get selected user type and input credentials
        String selectedType = userTypeComboBox.getValue();
        String email = emailField.getText();
        String password = passwordField.getText();
        errorLabel.setText("");

        // Input validation
        if (selectedType == null || selectedType.isEmpty()) {
            errorLabel.setText("Please select a role.");
            return;
        }
        if (email == null || email.trim().isEmpty()) {
            errorLabel.setText("Please enter your email.");
            return;
        }
        if (password == null || password.isEmpty()) {
            errorLabel.setText("Please enter your password.");
            return;
        }

        // Customer authentication flow
        if ("Customer".equals(selectedType)) {
            // Attempt customer authentication using the provided credentials
            Optional<Customer> authenticatedCustomer = authService.authenticateCustomer(email, password);

            if (authenticatedCustomer.isPresent()) {
                // Get the authenticated customer object
                Customer customer = authenticatedCustomer.get();
                System.out.println("Login successful for Customer: " + customer.getFirstName() + " " + customer.getLastName());
                errorLabel.setText("");

                // Navigate to customer dashboard
                openCustomerDashboard(customer);
                WindowHelper.closeWindow(loginButton); // Close the login window after successful login
            } else {
                // Display error message for invalid credentials
                errorLabel.setText("Invalid email or password.");
                passwordField.clear();
            }
        }
        else if ("Employee".equals(selectedType)) {
            Optional<Employee> authenticatedEmployee = authService.authenticateEmployee(email, password);

            if (authenticatedEmployee.isPresent()) {
                Employee employee = authenticatedEmployee.get();
                System.out.println("Login successful for Employee: " + employee.getFirstName() + " " + employee.getLastName());
                errorLabel.setText("");

                openEmployeeDashboard(employee);
                WindowHelper.closeWindow(loginButton);
            } else {
                errorLabel.setText("Invalid email or password.");
                passwordField.clear();
            }
        } else {
            errorLabel.setText("Invalid role selected.");
        }
    }

    /**
     * Opens the customer dashboard view after successful authentication.
     * @param customer The authenticated customer object
     */
    private void openCustomerDashboard(Customer customer) {
        try {
            // Load the FXML file for the customer dashboard
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/customer/customer-dashboard-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML: customer-dashboard-view.fxml");
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load customer dashboard.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            // --- Pass data to the new controller ---
            CustomerDashboardController controller = loader.getController();
            controller.setLoggedInCustomer(customer);
            // --- End Pass data ---

            // Create a new stage (window) for the dashboard
            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Customer Dashboard - " + customer.getFirstName());
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the customer dashboard.");
        }
    }

    private void openEmployeeDashboard(Employee employee) {
        try {
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/employee/employee-dashboard-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML: employee-dashboard-view.fxml");
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load employee dashboard.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            EmployeeDashboardController controller = loader.getController();
            controller.setLoggedInEmployee(employee);

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("Employee Dashboard - " + employee.getFirstName());
            dashboardStage.setScene(new Scene(root));
            dashboardStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the employee dashboard.");
        }
    }

}