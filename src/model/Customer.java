package model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String customerId;
    private String firstName;
    private String surName;
    private String address;
    private List<Account> accounts;

    public Customer(String customerId, String firstName, String surName, String address) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surName = surName;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId; }
    public void setCustomerId(String customerId) {
        this.customerId = customerId; }

    public String getFirstName() {
        return firstName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName; }

    public String getSurName() {
        return surName; }
    public void setSurName(String surName) {
        this.surName = surName; }

    public String getAddress() {
        return address; }
    public void setAddress(String address) {
        this.address = address; }
}

