package com.project.frontend_app.model;

import com.project.frontend_app.model.enums.VehicleCondition;


/**
 * Represents a vehicle in the car dealership system.
 * Contains all relevant information about a vehicle including its identification,
 * specifications, condition, and availability status.
 */
public class Vehicle {
    /** Vehicle Identification Number (unique identifier) */
    private String vin;
    /** Manufacturer/brand of the vehicle */
    private String make;
    /** Model name of the vehicle */
    private String model;
    /** Year of manufacture */
    private int year;
    /** Current mileage in kilometers */
    private int mileage;
    /** Type/level of equipment/trim */
    private String equipmentType;
    /** Current technical condition of the vehicle */
    private VehicleCondition technicalCondition;
    /** Current asking price */
    private double price;
    /** Additional description/details about the vehicle */
    private String description;
    /** Availability status (true if available for sale) */
    private boolean available;

    /**
     * Constructs a new Vehicle with all required attributes.
     * @param vin Vehicle Identification Number
     * @param make Manufacturer/brand
     * @param model Model name
     * @param year Year of manufacture
     * @param mileage Current mileage in km
     * @param equipmentType Equipment/trim level
     * @param technicalCondition Current condition
     * @param price Asking price
     * @param description Additional details
     * @param available Availability status
     */
    public Vehicle(String vin, String make, String model, int year, int mileage, String equipmentType, 
                  VehicleCondition technicalCondition, double price, String description, boolean available) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.equipmentType = equipmentType;
        this.technicalCondition = technicalCondition;
        this.price = price;
        this.description = description;
        this.available = available;
    }

    // --- Getters ---
    /** @return Vehicle Identification Number */
    public String getVin() { return vin; }
    /** @return Manufacturer/brand */
    public String getMake() { return make; }
    /** @return Model name */
    public String getModel() { return model; }
    /** @return Year of manufacture */
    public int getYear() { return year; }
    /** @return Current mileage in km */
    public int getMileage() { return mileage; }
    /** @return Equipment/trim level */
    public String getEquipmentType() { return equipmentType; }
    /** @return Current technical condition */
    public VehicleCondition getTechnicalCondition() { return technicalCondition; }
    /** @return Current asking price */
    public double getPrice() { return price; }
    /** @return Additional details */
    public String getDescription() { return description; }
    /** @return Availability status */
    public boolean isAvailable() { return available; }

    // --- Setters ---
    /** @param price New asking price */
    public void setPrice(double price) { this.price = price; }
    /** @param available New availability status */
    public void setAvailable(boolean available) { this.available = available; }
    /** @param description New additional details */
    public void setDescription(String description) { this.description = description; }
    /** @param mileage Updated mileage in km */
    public void setMileage(int mileage) { this.mileage = mileage; }
    /** @param technicalCondition Updated condition */
    public void setTechnicalCondition(VehicleCondition technicalCondition) { 
        this.technicalCondition = technicalCondition; 
    }

    /**
     * Returns a string representation of the vehicle.
     * @return String in format "Make Model (Year, VIN: XXXX)"
     */
    @Override
    public String toString() {
        return make + " " + model + " (" + year + ", VIN: " + vin + ")";
    }
}