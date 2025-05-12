package com.project.frontend_app.controller.employee;

import com.project.frontend_app.model.Customer;
import com.project.frontend_app.service.impl.InMemoryAuthenticationService;
import com.project.frontend_app.service.interf.IAuthenticationService;
import com.project.frontend_app.util.AlertHelper;
import com.project.frontend_app.util.WindowHelper;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static com.project.frontend_app.util.AlertHelper.showAlert;

import java.io.IOException;
import java.net.URL;

/**
 * Controller for the customer list view in the employee interface.
 * Manages the display of registered customers in a TableView.
 */
public class CustomerListController {

    // TableView and columns for displaying customer information
    @FXML private TableView<Customer> customersTable;
    @FXML private TableColumn<Customer, Long> idColumn;
    @FXML private TableColumn<Customer, String> firstNameColumn;
    @FXML private TableColumn<Customer, String> lastNameColumn;
    @FXML private TableColumn<Customer, String> phoneNumberColumn;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, String> streetColumn;
    @FXML private TableColumn<Customer, String> houseNumberColumn;
    @FXML private TableColumn<Customer, String> apartmentColumn;
    @FXML private TableColumn<Customer, String> cityColumn;
    @FXML private TableColumn<Customer, String> postalCodeColumn;

    // Service to fetch vehicle customer's (currently using in-memory implementation)
    private final IAuthenticationService customerService = new InMemoryAuthenticationService();

    // Observable list to hold data for the table (automatically updates UI when changed)
    private final ObservableList<Customer> customerData = FXCollections.observableArrayList();

    /**
     * Initializes the controller after FXML loading is complete.
     * Configures table columns and loads initial customer data.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        streetColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getStreet()));
        houseNumberColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getHouseNumber()));
        apartmentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getApartmentNumber() != null ?
                        cellData.getValue().getAddress().getApartmentNumber() : ""));
        cityColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getCity()));
        postalCodeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAddress().getPostalCode()));

        loadCustomerData();
        customersTable.setItems(customerData);
    }


    /**
     * Loads registered customers from the service into the observable list.
     * Clears existing data before loading new data.
     */
    private void loadCustomerData() {
        customerData.clear();
        customerData.addAll(customerService.findAll());
    }

    /**
     * Show a view with a form for adding a new customer.
     */
    @FXML
    public void handleAddCustomer(javafx.event.ActionEvent actionEvent) {
        try {
            // Load the FXML file for the form to add a new customer
            URL fxmlLocation = getClass().getResource("/com/project/frontend_app/view/employee/add-customer-form-view.fxml");

            if (fxmlLocation == null) {
                System.err.println("Cannot find FXML: add-customer-view.fxml");
                showAlert(Alert.AlertType.ERROR, "Error", "Could not load add customer form.");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Stage vehicleListStage = new Stage();
            vehicleListStage.setTitle("Add new customer");
            vehicleListStage.setScene(new Scene(root));
            vehicleListStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Failed to load the customer add form.");
        }
    }

    /**
     * Show a view with a form for editing and deleting the customer's account.
     * Placeholder for future implementation.
     */
    @FXML
    public void handleEditProfile(javafx.event.ActionEvent event) {
        Customer selected = customersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a customer to edit.");
        } else {
            AlertHelper.showAlert(Alert.AlertType.INFORMATION, "Edit Customer", "Editing customer: " + selected.getFirstName());
        }
    }

    /**
     * Handles the close button action.
     * Closes the current window/stage.
     */
    @FXML
    private void handleClose() {
        WindowHelper.closeWindow(customersTable);
    }
}
