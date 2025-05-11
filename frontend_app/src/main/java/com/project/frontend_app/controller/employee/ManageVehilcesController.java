package com.project.frontend_app.controller.employee;

import com.project.frontend_app.model.Vehicle;
import com.project.frontend_app.model.enums.VehicleCondition;
import com.project.frontend_app.service.impl.InMemoryVehicleService;
import com.project.frontend_app.service.interf.IVehicleService;
import com.project.frontend_app.util.WindowHelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller for vehicle management: viewing, adding, updating, and deleting vehicles.
 */
public class ManageVehilcesController {

    // TableView and its columns
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, String> vinColumn;
    @FXML private TableColumn<Vehicle, String> makeColumn;
    @FXML private TableColumn<Vehicle, String> modelColumn;
    @FXML private TableColumn<Vehicle, Integer> yearColumn;
    @FXML private TableColumn<Vehicle, Integer> mileageColumn;
    @FXML private TableColumn<Vehicle, VehicleCondition> conditionColumn;
    @FXML private TableColumn<Vehicle, Double> priceColumn;
    @FXML private TableColumn<Vehicle, String> descriptionColumn;

    // Form fields for adding/updating vehicle
    @FXML private TextField vinField;
    @FXML private TextField makeField;
    @FXML private TextField modelField;
    @FXML private TextField yearField;
    @FXML private TextField mileageField;
    @FXML private TextField equipmentField;
    @FXML private ComboBox<VehicleCondition> conditionBox;
    @FXML private TextField priceField;
    @FXML private TextField descriptionField;
    @FXML private Button saveVehicleButton;

    // Service and data list
    private final IVehicleService vehicleService = new InMemoryVehicleService();
    private final ObservableList<Vehicle> vehicleData = FXCollections.observableArrayList();

    // Currently selected vehicle for editing
    private Vehicle selectedVehicle = null;

    /**
     * Initializes the controller. Sets up table columns, loads data and configures selection listener.
     */
    @FXML
    public void initialize() {
        // Setup table columns with property bindings
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("make"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        mileageColumn.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        conditionColumn.setCellValueFactory(new PropertyValueFactory<>("technicalCondition"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Load condition options into combo box
        conditionBox.setItems(FXCollections.observableArrayList(VehicleCondition.values()));

        // Load vehicle data and bind to table
        loadVehicleData();
        vehicleTable.setItems(vehicleData);

        // Enable form editing when a vehicle is selected from the table
        vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedVehicle = newVal;
                populateFormWithVehicle(selectedVehicle);
                saveVehicleButton.setText("Update Vehicle");
                vinField.setDisable(true); // VIN should not be editable during update
            }
        });
    }

    /**
     * Loads available vehicles from service and updates the observable list.
     */
    private void loadVehicleData() {
        vehicleData.clear();
        vehicleData.addAll(vehicleService.findAllAvailable());
    }

    /**
     * Handles saving a vehicle: adds new or updates existing based on form input.
     */
    @FXML
    private void handleSaveVehicle() {
        try {
            String vin = vinField.getText().trim();
            String make = makeField.getText().trim();
            String model = modelField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            int mileage = Integer.parseInt(mileageField.getText().trim());
            String equipment = equipmentField.getText().trim();
            VehicleCondition condition = conditionBox.getValue();
            double price = Double.parseDouble(priceField.getText().trim());
            String description = descriptionField.getText().trim();

            // Create new vehicle object (used for both add and update)
            Vehicle vehicle = new Vehicle(vin, make, model, year, mileage, equipment, condition, price, description, true);

            vehicleService.save(vehicle); // Service will determine whether to add or update
            loadVehicleData();
            clearFormFields();
        } catch (Exception e) {
            showAlert("Error", "Invalid input. Please check all fields.");
        }
    }

    /**
     * Deletes the selected vehicle from the table.
     */
    @FXML
    private void handleDeleteVehicle() {
        Vehicle selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            vehicleService.delete(selected.getVin());
            loadVehicleData();
            clearFormFields();
        } else {
            showAlert("No selection", "Please select a vehicle to delete.");
        }
    }

    /**
     * Closes the window using the WindowHelper utility.
     */
    @FXML
    private void handleClose() {
        WindowHelper.closeWindow(vehicleTable);
    }

    /**
     * Populates the form fields with data from the selected vehicle.
     * @param vehicle the vehicle to populate the form with
     */
    private void populateFormWithVehicle(Vehicle vehicle) {
        vinField.setText(vehicle.getVin());
        makeField.setText(vehicle.getMake());
        modelField.setText(vehicle.getModel());
        yearField.setText(String.valueOf(vehicle.getYear()));
        mileageField.setText(String.valueOf(vehicle.getMileage()));
        equipmentField.setText(vehicle.getEquipmentType());
        conditionBox.setValue(vehicle.getTechnicalCondition());
        priceField.setText(String.valueOf(vehicle.getPrice()));
        descriptionField.setText(vehicle.getDescription());
    }

    /**
     * Clears the form fields and resets the state.
     */
    private void clearFormFields() {
        vinField.clear();
        vinField.setDisable(false);
        makeField.clear();
        modelField.clear();
        yearField.clear();
        mileageField.clear();
        equipmentField.clear();
        conditionBox.setValue(null);
        priceField.clear();
        descriptionField.clear();
        selectedVehicle = null;
        saveVehicleButton.setText("Add Vehicle");
        vehicleTable.getSelectionModel().clearSelection();
    }

    /**
     * Displays an alert dialog with provided title and content.
     * @param title the title of the alert
     * @param content the content/message of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
