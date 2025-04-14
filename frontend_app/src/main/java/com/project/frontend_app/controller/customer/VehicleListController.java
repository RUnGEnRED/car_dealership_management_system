package com.project.frontend_app.controller.customer;

import com.project.frontend_app.model.Vehicle;
import com.project.frontend_app.model.enums.VehicleCondition;
import com.project.frontend_app.service.impl.InMemoryVehicleService; // Use implementation for now
import com.project.frontend_app.service.interf.IVehicleService; // Use interface for now

import com.project.frontend_app.util.WindowHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 * Controller for the vehicle list view in the customer interface.
 * Manages the display of available vehicles in a TableView.
 */
public class VehicleListController {

    // TableView and columns for displaying vehicle information
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, String> vinColumn; // Vehicle Identification Number
    @FXML private TableColumn<Vehicle, String> makeColumn; // Vehicle manufacturer
    @FXML private TableColumn<Vehicle, String> modelColumn; // Vehicle model
    @FXML private TableColumn<Vehicle, Integer> yearColumn; // Manufacturing year
    @FXML private TableColumn<Vehicle, Integer> mileageColumn; // Current mileage
    @FXML private TableColumn<Vehicle, VehicleCondition> conditionColumn; // Technical condition (enum)
    @FXML private TableColumn<Vehicle, Double> priceColumn; // Asking price
    @FXML private TableColumn<Vehicle, String> descriptionColumn; // Vehicle description

    // Service to fetch vehicle data (currently using in-memory implementation)
    private final IVehicleService vehicleService = new InMemoryVehicleService();

    // Observable list to hold data for the table (automatically updates UI when changed)
    private final ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();

    /**
     * Initializes the controller after FXML loading is complete.
     * Configures table columns and loads initial vehicle data.
     */
    @FXML
    public void initialize() {
        // Configure table columns to use Vehicle object properties
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("technicalCondition"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load initial vehicle data
        loadVehicleData();

        // Bind the loaded data to the table view
        vehicleTable.setItems(vehicleData);

        System.out.println("Vehicle List View Initialized and data loaded.");
    }

    /**
     * Loads available vehicles from the service into the observable list.
     * Clears existing data before loading new data.
     */
    private void loadVehicleData() {
        vehicleData.clear(); // Clear existing data
        vehicleData.addAll(vehicleService.findAllAvailable()); // Add only available vehicles
        System.out.println("Loaded " + vehicleData.size() + " available vehicles.");
    }

    /**
     * Handles the close button action.
     * Closes the current window/stage.
     */
    @FXML
    private void handleClose() {
        WindowHelper.closeWindow(vehicleTable); // Close the current window
    }
}