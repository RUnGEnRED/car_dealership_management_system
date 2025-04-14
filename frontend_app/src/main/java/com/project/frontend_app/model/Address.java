package com.project.frontend_app.model;


/**
 * Represents a physical address with street, house number, city, and postal code information.
 * The apartment number is optional and can be null if not applicable.
 */
public class Address {
    private String street;
    private String houseNumber;
    private String apartmentNumber; // Optional field, can be null
    private String city;
    private String postalCode;

    /**
     * Constructs a new Address object.
     * @param street The street name
     * @param houseNumber The house/building number
     * @param apartmentNumber The apartment/unit number (optional, can be null or empty)
     * @param city The city name
     * @param postalCode The postal/zip code
     */
    public Address(String street, String houseNumber, String apartmentNumber, String city, String postalCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = (apartmentNumber != null && !apartmentNumber.trim().isEmpty()) ? apartmentNumber : null;
        this.city = city;
        this.postalCode = postalCode;
    }

    // --- Getters ---
    /** @return The street name */
    public String getStreet() { return street; }
    /** @return The house/building number */
    public String getHouseNumber() { return houseNumber; }
    /** @return The apartment/unit number (may be null) */
    public String getApartmentNumber() { return apartmentNumber; }
    /** @return The city name */
    public String getCity() { return city; }
    /** @return The postal/zip code */
    public String getPostalCode() { return postalCode; }

    // --- Setters ---
    /** @param street The new street name */
    public void setStreet(String street) { this.street = street; }
    /** @param houseNumber The new house/building number */
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    /** 
     * @param apartmentNumber The new apartment/unit number (can be null or empty)
     */
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = (apartmentNumber != null && !apartmentNumber.trim().isEmpty()) ? apartmentNumber : null; }
    /** @param city The new city name */
    public void setCity(String city) { this.city = city; }
    /** @param postalCode The new postal/zip code */
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    /**
     * Returns a formatted string representation of the address.
     * @return String in format "Street HouseNumber[/ApartmentNumber], PostalCode City"
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(street != null ? street : "").append(" ");
        sb.append(houseNumber != null ? houseNumber : "");
        if (apartmentNumber != null) {
            sb.append("/").append(apartmentNumber);
        }
        sb.append(", ");
        sb.append(postalCode != null ? postalCode : "").append(" ");
        sb.append(city != null ? city : "");
        return sb.toString().trim();
    }
}