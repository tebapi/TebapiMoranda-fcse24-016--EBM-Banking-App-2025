package model;

import javafx.beans.property.*;

public class Customer {
    private int customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private String phone;
    private String employmentStatus;
    private String companyName;
    private String companyAddress;

    public Customer() {}

    public Customer(String firstName, String lastName, String address, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getCompanyAddress() { return companyAddress; }
    public void setCompanyAddress(String companyAddress) { this.companyAddress = companyAddress; }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (" + email + ")";
    }

    // JavaFX
    public IntegerProperty customerIdProperty() {
        return new SimpleIntegerProperty(customerId);
    }

    public StringProperty firstNameProperty() {
        return new SimpleStringProperty(firstName);
    }

    public StringProperty lastNameProperty() {
        return new SimpleStringProperty(lastName);
    }

    public StringProperty emailProperty() {
        return new SimpleStringProperty(email);
    }

    public StringProperty phoneProperty() {
        return new SimpleStringProperty(phone);
    }

    public StringProperty employmentStatusProperty() {
        return new SimpleStringProperty(employmentStatus);
    }

    public StringProperty companyNameProperty() {
        return new SimpleStringProperty(companyName);
    }
}