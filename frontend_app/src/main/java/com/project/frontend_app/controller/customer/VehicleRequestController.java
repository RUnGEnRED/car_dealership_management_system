package com.project.frontend_app.controller.customer;

import com.project.frontend_app.model.Customer;
import com.project.frontend_app.model.Request;
import com.project.frontend_app.model.Vehicle;
import com.project.frontend_app.model.enums.VehicleCondition;
import com.project.frontend_app.service.impl.InMemoryRequestService; // Use implementation for now
import com.project.frontend_app.service.impl.InMemoryVehicleService; // Use implementation for now
import com.project.frontend_app.service.interf.IRequestService; // Use interface for now
import com.project.frontend_app.service.interf.IVehicleService; // Use interface for now

import static com.project.frontend_app.util.AlertHelper.showAlert;

import com.project.frontend_app.util.WindowHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Controller for handling vehicle purchase requests from customers.
 * Manages the UI for viewing available vehicles and submitting purchase requests.
 */
public class VehicleRequestController {

    // ===== FXML ELEMENTS =====

    // TableView and columns for displaying vehicle information
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, String> vinColumn;
    @FXML private TableColumn<Vehicle, String> makeColumn;
    @FXML private TableColumn<Vehicle, String> modelColumn;
    @FXML private TableColumn<Vehicle, Integer> yearColumn;
    @FXML private TableColumn<Vehicle, Integer> mileageColumn;
    @FXML private TableColumn<Vehicle, VehicleCondition> conditionColumn;
    @FXML private TableColumn<Vehicle, Double> priceColumn;
    @FXML private TableColumn<Vehicle, String> descriptionColumn;

    // Customer Info Form Fields
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    // Buttons
    @FXML private Button submitRequestButton;
    @FXML private Button closeButton;

    // ===== SERVICES =====

    // Service to fetch vehicle data (currently using in-memory implementation)
    private final IVehicleService vehicleService = new InMemoryVehicleService();

    // Service to submit vehicle purchase requests (currently using in-memory implementation)
    private final IRequestService requestService = new InMemoryRequestService();

    // ===== DATA =====

    // Observable list to hold data for the table (automatically updates UI when changed)
    private final ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();

    // Placeholder for the logged-in customer
    private Customer loggedInCustomer;

    /**
     * Initializes the controller after FXML loading.
     * Sets up table columns, loads vehicle data, and configures selection listener.
     */
    @FXML
    public void initialize() {
        configureTableColumns();
        loadVehicleData();
        vehicleTable.setItems(vehicleData);
        // Disable submit button when no vehicle is selected
        vehicleTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> submitRequestButton.setDisable(newSelection == null)
        );
        System.out.println("Vehicle Request View Initialized.");
    }

    /**
     * Initializes customer data for the request form.
     * @param customer The logged-in customer making the request
     */
    public void initializeData(Customer customer) {
        this.loggedInCustomer = customer;
        if (customer == null) {
            System.err.println("Error: Logged in customer data is null.");
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Could not load customer data.");
            return;
        }
        populateCustomerForm();
        System.out.println("Customer data populated for request view: " + customer.getEmail());
    }

    /**
     * Populates the customer form fields with logged-in customer data.
     */
    private void populateCustomerForm() {
        // (Code as before)
        if (loggedInCustomer != null) {
            firstNameField.setText(loggedInCustomer.getFirstName());
            lastNameField.setText(loggedInCustomer.getLastName());
            emailField.setText(loggedInCustomer.getEmail());
            phoneField.setText(loggedInCustomer.getPhoneNumber());
            if (loggedInCustomer.getAddress() != null) {
                addressField.setText(loggedInCustomer.getAddress().toString());
            } else {
                addressField.setText("N/A");
            }
        }
    }

    /**
     * Configures table columns to display vehicle properties.
     */
    private void configureTableColumns() {
        // Bind each column to corresponding Vehicle property
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("technicalCondition"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    /**
     * Loads available vehicles from service and updates the table.
     */
    private void loadVehicleData() {
        vehicleData.clear();
        vehicleData.addAll(vehicleService.findAllAvailable());
        vehicleTable.refresh(); // Ensure table UI updates
        System.out.println("Loaded/Refreshed " + vehicleData.size() + " available vehicles.");
    }

    /**
     * Handles submit request button action.
     * Validates input, submits request through service, and handles response.
     */
    @FXML
    private void handleSubmitRequest() {
        // Validate input
        Vehicle selectedVehicle = vehicleTable.getSelectionModel().getSelectedItem();
        if (selectedVehicle == null) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please select a vehicle from the list.");
            return;
        }
        if (loggedInCustomer == null) {
            showAlert(Alert.AlertType.ERROR, "Internal Error", "Customer data is missing. Cannot submit request.");
            return;
        }

        // Submit request
        boolean submissionSuccess = requestService.submitPurchaseRequest(loggedInCustomer, selectedVehicle);

        // Handle response
        if (submissionSuccess) {
            showAlert(Alert.AlertType.INFORMATION, "Request Submitted",
                    "Your purchase request has been submitted successfully.");
            loadVehicleData(); // Refresh available vehicles
            vehicleTable.getSelectionModel().clearSelection();
            submitRequestButton.setDisable(true);
        } else {
            showAlert(Alert.AlertType.ERROR, "Submission Failed",
                    "There was a problem submitting your request.");
            loadVehicleData();
        }
    }

    /**
     * Handles the close button action.
     * Closes the current window/stage.
     */
    @FXML
    private void handleClose() {
        WindowHelper.closeWindow(closeButton); // Close the current window
    }
}